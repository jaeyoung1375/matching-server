package kr.co.teamo.admin.log.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class SystemLogCreateDto {
    private String logTypeCd;      // BE, FE
    private Long userId;
    private String actionCd;
    private String message;
    private String detailContent;
    private String ipAddress;
}
