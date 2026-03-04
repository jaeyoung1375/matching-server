package kr.co.matching.post.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import kr.co.matching.common.ApiResponse;
import kr.co.matching.post.dto.PostRequestDto;
import kr.co.matching.post.dto.PostResponseDto;
import kr.co.matching.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class PostController {

	private final PostService postService;

	@GetMapping("/posts")
	public ApiResponse<List<PostResponseDto>> posts(@RequestBody @Valid PostRequestDto req){

		List<PostResponseDto> posts = postService.selectAllPosts(req);

		return ApiResponse.ok(posts);
	}
}
