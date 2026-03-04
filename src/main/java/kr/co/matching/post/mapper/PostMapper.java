package kr.co.matching.post.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.matching.post.dto.PostRequestDto;
import kr.co.matching.post.dto.PostResponseDto;

@Mapper
public interface PostMapper {

	/**
	 * 게시물 목록 조회
	 * @param PostRequestDto
	 * @return List<PostResponseDto>
	 */
	List<PostResponseDto> selectAllPosts(PostRequestDto req);

}
