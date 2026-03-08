package kr.co.teamo.post.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

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

}
