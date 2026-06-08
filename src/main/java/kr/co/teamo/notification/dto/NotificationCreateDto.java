package kr.co.teamo.notification.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCreateDto {
    private Long notificationId;

    // 알림 받을 사용자
    private Long userId;

    // 알림을 발생시킨 사용자
    private Long senderId;

    // POST, COMMENT, APPLY, USER, SYSTEM
    private String targetType;

    // targetType에 해당하는 PK
    private Long targetId;

    // CMM_CODE_DTL 기준: APPLY, APPROVE, REJECT, COMMENT, REPLY, SYSTEM
    private String notificationTypeCd;

    private String title;
    private String content;
}
