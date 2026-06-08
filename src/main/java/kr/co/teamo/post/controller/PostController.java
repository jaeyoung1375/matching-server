package kr.co.teamo.post.controller;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import kr.co.teamo.auth.util.JwtTokenUtil;
import kr.co.teamo.common.code.UserErrorCode;
import kr.co.teamo.common.exception.CustomException;
import kr.co.teamo.common.response.ApiResponse;
import kr.co.teamo.common.util.PageResponseDto;
import kr.co.teamo.post.dto.PostRecruitPositDto;
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

	private final JwtTokenUtil jwtTokenUtil;

	@GetMapping("/public/posts")
	public ApiResponse<PageResponseDto<PostResponseDto>> posts(@ModelAttribute @Valid PostRequestDto req){


		PageResponseDto<PostResponseDto> posts = postService.selectAllPosts(req);

		return ApiResponse.ok(posts);
	}

	@GetMapping("/public/posts/{postId}")
	public ApiResponse<PostResponseDto> post(@PathVariable(name = "postId") Long postId,  @Valid @ModelAttribute PostRequestDto req){

		PostResponseDto post = postService.findByPostId(req);

		List<PostRecruitPositDto> positions = postService.recruitPositList(req);
		post.setPositions(positions);

		return ApiResponse.ok(post);
	}

	@PostMapping("/posts")
	public ApiResponse<PostResponseDto> createPost(@RequestBody PostRequestDto req){

		Long userId = jwtTokenUtil.getMemberIdFromSecurityContext();

		//  사용자ID를 못가져오면 예외처리
		if(ObjectUtils.isEmpty(userId)) {
			throw new CustomException(UserErrorCode.USER_NOT_FOUND);
		}
		req.setUserId(userId);


		return ApiResponse.ok(postService.createPost(req));

	}
}
