package kr.co.teamo.admin.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCodeDtlRequest {

    @Schema(description = "공통코드아이디 (내부 처리)")
    private String comCdId;

    @Schema(description = "상세코드아이디")
    @NotBlank(message = "상세코드아이디는 필수입니다.")
    private String dtlCdId;

    @Schema(description = "상세코드명")
    @NotBlank(message = "상세코드명은 필수입니다.")
    private String dtlCdNm;

    @Schema(description = "상세코드설명")
    private String dtlCdExpln;

    @Schema(description = "연결상세코드아이디1")
    private String lnkgDtlCdId1;

    @Schema(description = "연결상세코드명1")
    private String lnkgDtlCdNm1;

    @Schema(description = "연결상세코드아이디2")
    private String lnkgDtlCdId2;

    @Schema(description = "연결상세코드명2")
    private String lnkgDtlCdNm2;

    @Schema(description = "사용여부 (Y/N)")
    @NotBlank(message = "사용여부는 필수입니다.")
    @Pattern(regexp = "^[YN]$", message = "사용여부는 Y 또는 N 이어야 합니다.")
    private String useYn;

    @Schema(description = "정렬순서")
    private Long sortSeq;

    @Schema(description = "등록자 아이디 (내부 처리)")
    private String regId;

    @Schema(description = "수정자 아이디 (내부 처리)")
    private String modId;
}
