package kr.co.teamo.comment.controller;

import jakarta.validation.Valid;
import kr.co.teamo.auth.util.JwtTokenUtil;
import kr.co.teamo.comment.dto.CommentRequestDto;
import kr.co.teamo.comment.dto.CommentResponseDto;
import kr.co.teamo.comment.service.CommentService;
import kr.co.teamo.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * 댓글 목록 조회 — 비로그인도 가능 (public 엔드포인트)
     * 로그인 상태면 isOwner 가 올바르게 반환된다.
     */
    @GetMapping("/public/posts/{postId}/comments")
    public ApiResponse<List<CommentResponseDto>> getComments(
            @PathVariable(name = "postId") Long postId) {

        Long userId = resolveCurrentUserId();
        log.debug("[getComments] postId={}, userId={}", postId, userId);

        return ApiResponse.ok(commentService.getComments(postId, userId));
    }

    /**
     * 댓글/대댓글 등록 — 로그인 필수
     * body: { content: string, parentId: number | null }
     */
    @PostMapping("/posts/{postId}/comments")
    public ApiResponse<Void> createComment(
            @PathVariable(name = "postId") Long postId,
            @RequestBody @Valid CommentRequestDto req) {

        Long userId = jwtTokenUtil.getMemberIdFromSecurityContext();
        commentService.createComment(postId, userId, req);
        return ApiResponse.ok();
    }

    /**
     * 댓글/대댓글 삭제 (소프트 삭제) — 본인만 가능
     */
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ApiResponse<Void> deleteComment(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId) {

        Long userId = jwtTokenUtil.getMemberIdFromSecurityContext();
        commentService.deleteComment(commentId, userId);
        return ApiResponse.ok();
    }

    /**
     * SecurityContext 에서 userId 를 꺼내되,
     * 비로그인(anonymous) 상태면 null 을 반환한다.
     */
    private Long resolveCurrentUserId() {
        try {
            return jwtTokenUtil.getMemberIdFromSecurityContext();
        } catch (Exception e) {
            return null;
        }
    }
}
