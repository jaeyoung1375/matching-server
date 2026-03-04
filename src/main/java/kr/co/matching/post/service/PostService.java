package kr.co.matching.post.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.matching.post.dto.PostRequestDto;
import kr.co.matching.post.dto.PostResponseDto;
import kr.co.matching.post.mapper.PostMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostMapper postMapper;

	/**
	 * 게시물 목록 조회
	 * @param PostRequestDto
	 * @return List<PostResponseDto>
	 */
	public List<PostResponseDto> selectAllPosts(PostRequestDto req){
		return postMapper.selectAllPosts(req);
	}

}
