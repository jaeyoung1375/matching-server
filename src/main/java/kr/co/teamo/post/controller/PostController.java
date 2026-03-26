package kr.co.teamo.post.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.teamo.common.response.ApiResponse;
import kr.co.teamo.post.dto.PostRequestDto;
import kr.co.teamo.post.dto.PostResponseDto;
import kr.co.teamo.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

	private final PostService postService;

	@GetMapping("/posts")
	public ApiResponse<List<PostResponseDto>> posts(@RequestBody @Valid PostRequestDto req){

		List<PostResponseDto> posts = postService.selectAllPosts(req);

		return ApiResponse.ok(posts);
	}

	@PostMapping("/posts")
	public ApiResponse<?> createPost(@RequestBody PostRequestDto req){

		postService.createPost(req);
		return ApiResponse.ok();

	}
}
