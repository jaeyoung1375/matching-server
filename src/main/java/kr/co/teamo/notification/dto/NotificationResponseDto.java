package kr.co.teamo.notification.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationResponseDto {
    private Long notificationId;

    private Long userId;
    private Long senderId;
    private String senderName;

    private String targetType;
    private String targetTypeNm;
    private Long targetId;

    private String notificationTypeCd;
    private String notificationTypeNm;

    private String title;
    private String content;

    private String readYn;
    private String regDt;
    private String readDt;
}
