package kr.co.teamo.post.service;

import java.util.List;


import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;

import kr.co.teamo.apply.dto.ApplyRequestDto;
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
public class PostService {

	private final PostMapper postMapper;

	private final NotificationService notificationService;

	private final JwtTokenUtil jwtTokenUtil;

	private final ApplyService applyService;


	/**
	 * 게시물 목록 조회
	 * @param PostRequestDto
	 * @return List<PostResponseDto>
	 */
	public PageResponseDto<PostResponseDto> selectAllPosts(PostRequestDto req){

		int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
		PageHelper.startPage(pageNum,12);
		List<PostResponseDto> list = postMapper.selectAllPosts(req);

		return PageResponseDto.of(list);
	}

	/**
	 * 게시판 상세 조회
	 * @param PostRequestDto
	 * @return PostResponseDto
	 */
	public PostResponseDto findByPostId(Long postId) {


		return postMapper.findByPostId(postId);
	}

	public List<PostRecruitPositDto> recruitPositList(PostRequestDto req){

		return postMapper.recruitPositList(req);
	}

	public List<PostApplyUserDto> selectApplyUsers(PostRequestDto req){

		return postMapper.selectApplyUsers(req.getPostId());
	}



	/**
	 * 게시물 등록
	 * @param PostRequestDto
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


		// 작성자 자동 등록
		ApplyRequestDto leader = ApplyRequestDto
				.builder()
				.postId(req.getPostId())
				.userId(jwtTokenUtil.getMemberIdFromSecurityContext())
				.statusCd("20")
				.build();

		applyService.createApply(leader);


		if (postMapper.countPostsByUser(req.getUserId()) == 1) {
			notificationService.createFirstPostNotification(req.getUserId(), req.getPostId());
		}
//
//		// 3. TEMP_YN = N 업데이트
//		List<FileDto> tempFiles = fileService.selectTempFiles(req.getTempKey());
//		fileService.confirmTempFiles(req.getTempKey());
//		// 4. 게시판 <-> 파일 연결
//
//		if(tempFiles.isEmpty()) {
//			throw new CustomException(FileErrorCode.FILE_EMPTY);
//		}


//		List<PostFileDto> postFiles = tempFiles.stream()
//				.map(f -> PostFileDto.builder()
//						.postId(req.getPostId())
//						.fileId(f.getFileId())
//						.build())
//				.toList();
//
//		postMapper.insertPostFiles(postFiles);

		PostResponseDto response = PostResponseDto.builder()
				.postId(req.getPostId())
				.build();


		return response;

	}

}
