package kr.co.teamo.notification.service;

import kr.co.teamo.notification.dto.NotificationCreateDto;
import kr.co.teamo.notification.dto.NotificationResponseDto;
import kr.co.teamo.notification.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationMapper notificationMapper;

    public List<NotificationResponseDto> getNotifications(Long userId) {
        return notificationMapper.selectNotifications(userId);
    }

    public int countUnreadNotifications(Long userId) {
        return notificationMapper.countUnreadNotifications(userId);
    }
    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        notificationMapper.markAsRead(notificationId, userId);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        notificationMapper.markAllAsRead(userId);
    }

    @Transactional
    public void createWelcomeNotification(Long userId) {
        createNotification(NotificationCreateDto.builder()
                .userId(userId)
                .senderId(null)
                .targetType("USER")
                .targetId(userId)
                .notificationTypeCd("SYSTEM")
                .title("회원가입을 환영합니다")
                .content("TEA:MO에 오신 것을 환영합니다.")
                .build(), true);
    }

    @Transactional
    public void createFirstPostNotification(Long userId, Long postId) {
        createNotification(NotificationCreateDto.builder()
                .userId(userId)
                .senderId(null)
                .targetType("POST")
                .targetId(postId)
                .notificationTypeCd("SYSTEM")
                .title("첫 게시글 작성을 축하드려요")
                .content("첫 모집글이 등록되었습니다.")
                .build(), true);
    }

    @Transactional
    public void createApplyNotification(Long postOwnerId, Long applicantId, Long applyId) {
        createNotification(NotificationCreateDto.builder()
                .userId(postOwnerId)
                .senderId(applicantId)
                .targetType("APPLY")
                .targetId(applyId)
                .notificationTypeCd("APPLY")
                .title("새로운 참가 신청")
                .content("모집글에 새로운 참가 신청이 도착했습니다.")
                .build(), false);
    }

    @Transactional
    public void createApproveNotification(Long applicantId, Long postOwnerId, Long applyId) {
        createNotification(NotificationCreateDto.builder()
                .userId(applicantId)
                .senderId(postOwnerId)
                .targetType("APPLY")
                .targetId(applyId)
                .notificationTypeCd("APPROVE")
                .title("참가 신청 승인")
                .content("신청한 모집글의 참가가 승인되었습니다.")
                .build(), false);
    }

    @Transactional
    public void createRejectNotification(Long applicantId, Long postOwnerId, Long applyId) {
        createNotification(NotificationCreateDto.builder()
                .userId(applicantId)
                .senderId(postOwnerId)
                .targetType("APPLY")
                .targetId(applyId)
                .notificationTypeCd("REJECT")
                .title("참가 신청 거절")
                .content("신청한 모집글의 참가가 거절되었습니다.")
                .build(), false);
    }

    @Transactional
    public void createCommentOnMyPostNotification(Long postOwnerId, Long commentWriterId, Long commentId) {
        createNotification(NotificationCreateDto.builder()
                .userId(postOwnerId)
                .senderId(commentWriterId)
                .targetType("COMMENT")
                .targetId(commentId)
                .notificationTypeCd("COMMENT")
                .title("새 댓글")
                .content("내 모집글에 댓글이 작성되었습니다.")
                .build(), false);
    }

    @Transactional
    public void createReplyOnMyCommentNotification(Long commentWriterId, Long replyWriterId, Long replyCommentId) {
        createNotification(NotificationCreateDto.builder()
                .userId(commentWriterId)
                .senderId(replyWriterId)
                .targetType("COMMENT")
                .targetId(replyCommentId)
                .notificationTypeCd("REPLY")
                .title("새 대댓글")
                .content("내 댓글에 대댓글이 작성되었습니다.")
                .build(), false);
    }

    @Transactional
    public void createReplyOnMyPostNotification(Long postOwnerId, Long replyWriterId, Long replyCommentId) {
        createNotification(NotificationCreateDto.builder()
                .userId(postOwnerId)
                .senderId(replyWriterId)
                .targetType("COMMENT")
                .targetId(replyCommentId)
                .notificationTypeCd("REPLY")
                .title("새 대댓글")
                .content("내 모집글에 대댓글이 작성되었습니다.")
                .build(), false);
    }

    @Transactional
    public void createPostClosedNotification(Long userId, Long postId) {
        createNotification(NotificationCreateDto.builder()
                .userId(userId)
                .senderId(null)
                .targetType("POST")
                .targetId(postId)
                .notificationTypeCd("SYSTEM")
                .title("모집 마감")
                .content("참여 중이거나 신청한 모집글이 마감되었습니다.")
                .build(), true);
    }

    private void createNotification(NotificationCreateDto dto, boolean allowSelfNotification) {
        if (dto.getUserId() == null) {
            return;
        }

        if (!allowSelfNotification && Objects.equals(dto.getUserId(), dto.getSenderId())) {
            return;
        }

        notificationMapper.insertNotification(dto);
    }
}
