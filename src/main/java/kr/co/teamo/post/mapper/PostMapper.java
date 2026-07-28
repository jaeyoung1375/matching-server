package kr.co.teamo.post.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.teamo.post.dto.PostApplyUserDto;
import kr.co.teamo.post.dto.PostFileDto;
import kr.co.teamo.post.dto.PostRecruitPositDto;
import kr.co.teamo.post.dto.PostRequestDto;
import kr.co.teamo.post.dto.PostResponseDto;

@Mapper
public interface PostMapper {

	/**
	 * 게시물 목록 조회
	 * @param PostRequestDto
	 * @return List<PostResponseDto>
	 */
	List<PostResponseDto> selectAllPosts(PostRequestDto req);

	/**
	 * 게시판 상세 조회
	 * @param PostRequestDto
	 * @return PostResponseDto
	 */
	PostResponseDto findByPostId(Long postId);

	/**
	 * 스터디 모집포지션 목록 조회
	 * @param PostRecruitPositDto
	 * @return List<PostRecruitPositDto>
	 */
	List<PostRecruitPositDto> recruitPositList(PostRequestDto req);


	/**
	 * 게시물 등록
	 * @param PostRequestDto
	 *
	 */
	void createPost(PostRequestDto req);

	/**
	 * 게시물 수정
	 * @param PostRequestDto
	 *
	 */
	void modifyPost(PostRequestDto req);

	/**
	 * 기술스택 삭제
	 * @param PostRequestDto
	 */
	void deleteAllPostTechStack(PostRequestDto req);

	void insertPostTechStack(PostRequestDto req);

	void deleteAllPostRecruitPosit(PostRequestDto req);

	void insertPostRecruitPosit(PostRequestDto req);

	void insertPostFiles(@Param("list") List<PostFileDto> postFileDto);

	int countPostsByUser(@Param("userId") Long userId);

	List<PostApplyUserDto> selectApplyUsers(@Param("postId") Long postId);

	/**
	 * 조회수 증가
	 * @return
	 */
	int increaseViewCnt(@Param("postId") Long postId, @Param("viewCnt") Long viewCnt);

}
