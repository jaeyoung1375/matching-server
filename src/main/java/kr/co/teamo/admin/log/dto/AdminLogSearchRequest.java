package kr.co.teamo.admin.log.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AdminLogSearchRequest {
    private String logTypeCd;
    private Long userId;
    private String actionCd;
    private String keyword;
    private String startDate;
    private String endDate;
}
