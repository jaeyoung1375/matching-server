package kr.co.teamo.post.service;

import java.time.Duration;
import java.util.List;


import kr.co.teamo.common.code.CommonErrorCode;
import kr.co.teamo.common.exception.CustomException;
import kr.co.teamo.common.util.ViewerKeyProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;

import kr.co.teamo.apply.service.ApplyService;
import kr.co.teamo.auth.util.JwtTokenUtil;
import kr.co.teamo.common.util.PageResponseDto;
import kr.co.teamo.notification.service.NotificationService;
import kr.co.teamo.post.dto.PostApplyUserDto;
import kr.co.teamo.post.dto.PostRecruitPositDto;
import kr.co.teamo.post.dto.PostRequestDto;
import kr.co.teamo.post.dto.PostResponseDto;
import kr.co.teamo.post.mapper.PostMapper;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

	private static final int DEFAULT_PAGE_NUM = 1;
	private static final int POSTS_PAGE_SIZE = 12;
    private static final String POST_VIEW_COUNT_KEY_PREFIX = "post:view-count:";
	private static final String POST_VIEWED_KEY_PREFIX = "post:viewed:";
	private static final Duration POST_VIEW_COUNT_DUPLICATE_TTL = Duration.ofMinutes(30);

	private final PostMapper postMapper;

	private final NotificationService notificationService;

	private final JwtTokenUtil jwtTokenUtil;

	private final ApplyService applyService;

    private final StringRedisTemplate redisTemplate;

	private final ViewerKeyProvider viewerKeyProvider;


	/**
	 * 게시물 목록 조회
	 * @param req ㅣ
	 * @return List<PostResponseDto>
	 */
	public PageResponseDto<PostResponseDto> selectAllPosts(PostRequestDto req){

		int pageNum = req.getPageNum() == null ? DEFAULT_PAGE_NUM : req.getPageNum();
		PageHelper.startPage(pageNum,POSTS_PAGE_SIZE);
		List<PostResponseDto> list = postMapper.selectAllPosts(req);


		return PageResponseDto.of(list);
	}

	/**
	 * 게시판 상세 조회
	 * @param postId 게시판아이디
	 * @return PostResponseDto
	 */
	public PostResponseDto findByPostId(Long postId) {

		log.info("게시글 상세 조회 호출. postId={}", postId);

        PostResponseDto post = postMapper.findByPostId(postId);

        if(post == null){
			throw new CustomException(CommonErrorCode.DATA_NOT_FOUND);
        }

		String viewerKey = viewerKeyProvider.getViewerKey();

		// 조회수 증가
		increaseViewCnt(postId, viewerKey);

		// 현재 조회수 + redis 조회수
		post.setViewCnt(post.getViewCnt() + getRedisViewCnt(postId));


		return post;
	}

	public List<PostRecruitPositDto> recruitPositList(PostRequestDto req){

		return postMapper.recruitPositList(req);
	}

	public List<PostApplyUserDto> selectApplyUsers(PostRequestDto req){

		return postMapper.selectApplyUsers(req.getPostId());
	}



	/**
	 * 게시물 등록
	 * @param req
	 */
	@Transactional
	public PostResponseDto createPost(PostRequestDto req) {

		// 1. 포지션별 모집인원 합산 → recruitCnt 세팅
		if (!ObjectUtils.isEmpty(req.getRecruitPositions())) {
			long totalRecruitCnt = req.getRecruitPositions().stream()
					.mapToLong(PostRecruitPositDto::getRecruitCnt)
					.sum();
			req.setRecruitCnt(totalRecruitCnt);
		}


		// 2. POST 테이블 INSERT
		postMapper.createPost(req);

		// 3. POST_TECH 테이블 INSERT
		if (!ObjectUtils.isEmpty(req.getTechStackTypeCd())) {
			postMapper.insertPostTechStack(req);
		}

		// 4. POST_RECRUIT_POSITION 테이블 INSERT
		if (!ObjectUtils.isEmpty(req.getRecruitPositions())) {
			postMapper.insertPostRecruitPosit(req);
		}


		// 작성자 자동 등록 (승인 상태)
		applyService.registerLeader(req.getPostId(), jwtTokenUtil.getMemberIdFromSecurityContext());


		if (postMapper.countPostsByUser(req.getUserId()) == 1) {
			notificationService.createFirstPostNotification(req.getUserId(), req.getPostId());
		}


        return PostResponseDto.builder()
                .postId(req.getPostId())
                .build();

	}

	@Transactional
	public PostResponseDto modifyPost(PostRequestDto req) {

		// 1. 포지션별 모집인원 합산 → recruitCnt 세팅
		if (!ObjectUtils.isEmpty(req.getRecruitPositions())) {
			long totalRecruitCnt = req.getRecruitPositions().stream()
					.mapToLong(PostRecruitPositDto::getRecruitCnt)
					.sum();
			req.setRecruitCnt(totalRecruitCnt);
		}

		// 2. POST 테이블 UPDATE
		postMapper.modifyPost(req);

		// 3. POST_TECH 테이블 INSERT
		if (!ObjectUtils.isEmpty(req.getTechStackTypeCd())) {
			postMapper.deleteAllPostTechStack(req);
			postMapper.insertPostTechStack(req);
		}

		// 4. POST_RECRUIT_POSITION 테이블 INSERT
		if (!ObjectUtils.isEmpty(req.getRecruitPositions())) {
			postMapper.deleteAllPostRecruitPosit(req);
			postMapper.insertPostRecruitPosit(req);
		}


        return PostResponseDto.builder()
                .postId(req.getPostId())
                .build();

	}

	/**
	 * 조회수 결과를 반환합니다
	 * @param postId 게시글 ID
	 * @return 조회수
	 */
    private long getRedisViewCnt(Long postId){
        String value = redisTemplate.opsForValue().get(getViewCountKey(postId));

        if(value == null){
            return 0L;
        }

        return Long.parseLong(value);
    }

	/**
	 * 조회수를 증가하여 Redis에 저장합니다
	 * @param postId 게시글 ID
	 */
    private void increaseViewCnt(Long postId, String viewerKey){

		String viewedKey = getViewedKey(postId, viewerKey);
		Boolean isFirstView = redisTemplate.opsForValue()
				.setIfAbsent(viewedKey, "Y", POST_VIEW_COUNT_DUPLICATE_TTL);

		if(Boolean.TRUE.equals(isFirstView)){
			Long viewCnt = redisTemplate.opsForValue().increment(getViewCountKey(postId));

			log.info("Redis 조회수 증가. postId={}, viewCnt={}, viewedKey={}, thread={}",
					postId,
					viewCnt,
					viewedKey,
					Thread.currentThread().getName());

			return;
		}

		log.debug("중복 조회로 조회수 증가 생략. postId={}, viewedKey={}", postId, viewedKey);


    }


	/**
	 * 게시글 조회수 Redis 키를 생성합니다
	 * @param postId 게시글 ID
	 * @return 게시글 조회수 Redis 키
	 */
    private String getViewCountKey(Long postId){
        return POST_VIEW_COUNT_KEY_PREFIX + postId;
    }

	/**
	 * 게시글 조회 여부 Redis 키를 생성합니다
	 * @param postId 게시글 ID
	 * @param viewerKey 조회자 ID
	 * @return 게시글 조회 여부 Redis 키
	 */
	private String getViewedKey(Long postId, String viewerKey){
        return POST_VIEWED_KEY_PREFIX + postId + ":" + viewerKey;
    }





}
