package kr.co.teamo.apply.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "지원 응답 DTO")
public class ApplyResponseDto {

    @Schema(description = "지원 아이디")
    private Long applyId;

    @Schema(description = "게시글 아이디")
    private Long postId;

    @Schema(description = "사용자 아이디")
    private Long userId;

    @Schema(description = "지원 포지션 코드")
    private String recruitPositTypeCd;

    @Schema(description = "지원 포지션명")
    private String recruitPositTypeNm;

    @Schema(description = "기술스택")
    private String techStackCd;

    @Schema(description = "지원 동기")
    private String applyReason;

    @Schema(description = "포트폴리오 URL")
    private String portfolioUrl;

    @Schema(description = "지원 상태 코드", example = "10")
    private String applyStatusCd;

    @Schema(description = "지원 상태명", example = "대기")
    private String statusNm;

    @Schema(description = "등록일")
    private String regDt;
}
