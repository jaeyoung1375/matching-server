package kr.co.teamo.admin.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class AdminCodeGroupDto {

    @Schema(description = "공통코드아이디")
    private String comCdId;

    @Schema(description = "공통코드명")
    private String comCdNm;

    @Schema(description = "사용여부")
    private String useYn;

    @Schema(description = "정렬순서")
    private Long sortSeq;

    @Schema(description = "등록아이디")
    private String regId;

    @Schema(description = "등록일시")
    private Date regDt;

    @Schema(description = "변경아이디")
    private String modId;

    @Schema(description = "변경일시")
    private Date modDt;
}
