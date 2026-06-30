package kr.co.teamo.admin.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCodeGroupRequest {

    @Schema(description = "공통코드아이디 (검색/수정/삭제 시 사용)")
    @NotBlank(message = "공통코드아이디는 필수입니다.")
    private String comCdId;

    @Schema(description = "공통코드명")
    @NotBlank(message = "공통코드명은 필수입니다.")
    private String comCdNm;

    @Schema(description = "사용여부 (Y/N)")
    @NotBlank(message = "사용여부는 필수입니다.")
    @Pattern(regexp = "^[YN]$", message = "사용여부는 Y 또는 N 이어야 합니다.")
    private String useYn;

    @Schema(description = "정렬순서")
    private Long sortSeq;

    @Schema(description = "등록/수정자 아이디 (내부 처리)")
    private String regId;

    @Schema(description = "수정자 아이디 (내부 처리)")
    private String modId;
}
