package kr.co.teamo.post.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.teamo.common.code.UserErrorCode;
import kr.co.teamo.common.exception.CustomException;
import kr.co.teamo.post.dto.PostRequestDto;
import kr.co.teamo.post.dto.PostResponseDto;
import kr.co.teamo.post.mapper.PostMapper;
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

	/**
	 * 게시물 등록
	 * @param PostRequestDto
	 */
	@Transactional
	public void createPost(PostRequestDto req) {

		postMapper.createPost(req);
		postMapper.insertPostTechStack(req);
	}

}
