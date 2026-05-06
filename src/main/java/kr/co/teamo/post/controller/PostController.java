package kr.co.teamo.post.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import kr.co.teamo.common.response.ApiResponse;
import kr.co.teamo.common.util.PageResponseDto;
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

	@GetMapping("/public/posts")
	public ApiResponse<PageResponseDto<PostResponseDto>> posts(@ModelAttribute @Valid PostRequestDto req){


		PageResponseDto<PostResponseDto> posts = postService.selectAllPosts(req);

		return ApiResponse.ok(posts);
	}

	@GetMapping("/public/posts/{postId}")
	public ApiResponse<PostResponseDto> post(@PathVariable(name = "postId") Long postId,  @Valid @ModelAttribute PostRequestDto req){

		PostResponseDto post = postService.findByPostId(req);

		return ApiResponse.ok(post);
	}

	@PostMapping("/posts")
	public ApiResponse<?> createPost(@RequestBody PostRequestDto req){

		postService.createPost(req);
		return ApiResponse.ok();

	}
}
