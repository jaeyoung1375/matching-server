package kr.co.teamo.admin.log.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminLogDto {
    private Long logId;
    private String logTypeCd;
    private Long userId;
    private String actionCd;
    private String message;
    private String detailContent;
    private String ipAddress;
    private LocalDateTime createdDt;
    private String useYn;
}
