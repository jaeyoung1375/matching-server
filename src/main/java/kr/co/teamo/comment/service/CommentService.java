package kr.co.teamo.comment.service;

import kr.co.teamo.comment.code.CommentErrorCode;
import kr.co.teamo.comment.dto.CommentInsertDto;
import kr.co.teamo.comment.dto.CommentRequestDto;
import kr.co.teamo.comment.dto.CommentResponseDto;
import kr.co.teamo.comment.mapper.CommentMapper;
import kr.co.teamo.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentMapper commentMapper;

    /**
     * 게시글 댓글 목록 조회 (트리 구조로 변환하여 반환)
     *
     * @param postId 게시글 아이디
     * @param userId 현재 로그인 사용자 (비로그인 시 null → isOwner 모두 false)
     */
    public List<CommentResponseDto> getComments(Long postId, Long userId) {
        // 1. DB에서 flat list 조회
        List<CommentResponseDto> flat = commentMapper.selectCommentsByPostId(postId, userId);

        // 2. parentId 기준으로 자식 댓글 그룹핑
        Map<Long, List<CommentResponseDto>> childMap = flat.stream()
                .filter(c -> c.getParentId() != null)
                .collect(Collectors.groupingBy(CommentResponseDto::getParentId));

        // 3. 루트 댓글에 children 부착 후 반환
        return flat.stream()
                .filter(c -> c.getParentId() == null)
                .peek(c -> c.setChildren(
                        childMap.getOrDefault(c.getCommentId(), Collections.emptyList())
                ))
                .collect(Collectors.toList());
    }

    /**
     * 댓글/대댓글 등록
     *
     * @param postId 게시글 아이디
     * @param userId 작성자 아이디
     * @param req    댓글 내용 + parentId
     */
    @Transactional
    public void createComment(Long postId, Long userId, CommentRequestDto req) {
        log.info("[createComment] postId={}, userId={}, parentId={}", postId, userId, req.getParentId());

        CommentInsertDto dto = CommentInsertDto.builder()
                .postId(postId)
                .userId(userId)
                .content(req.getContent())
                .parentId(req.getParentId())
                .build();

        commentMapper.insertComment(dto);

        log.info("[createComment] 등록 완료 — commentId={}", dto.getCommentId());
    }

    /**
     * 댓글/대댓글 소프트 삭제 (USE_YN = 'N')
     * 본인 댓글이 아닐 경우 COMMENT_NO_PERMISSION 예외 발생
     *
     * @param commentId 삭제할 댓글 아이디
     * @param userId    요청자 아이디
     */
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        log.info("[deleteComment] commentId={}, userId={}", commentId, userId);

        int affected = commentMapper.softDeleteComment(commentId, userId);

        if (affected == 0) {
            throw new CustomException(CommentErrorCode.COMMENT_NO_PERMISSION);
        }

        log.info("[deleteComment] 삭제 완료 — commentId={}", commentId);
    }
}
