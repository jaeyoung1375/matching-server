package kr.co.teamo.apply.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "지원 요청 DTO")
public class ApplyRequestDto {

    @Schema(description = "게시글 아이디")
    private Long postId;

    @Schema(description = "사용자 아이디 (서버에서 주입)")
    private Long userId;

    @NotNull(message = "지원 포지션을 선택해주세요.")
    @Schema(description = "지원 포지션 코드", example = "10")
    private String recruitPositTypeCd;

    @Schema(description = "기술스택 코드 목록", example = "[\"React\", \"TypeScript\"]")
    private List<String> techStackCd;

    @Schema(description = "기술스택 콤마 구분 문자열 (서버에서 변환)", hidden = true)
    private String techStackCdStr;

    @NotBlank(message = "지원 동기를 입력해주세요.")
    @Schema(description = "지원 동기")
    private String applyReason;

    @Schema(description = "포트폴리오 URL", example = "https://github.com/yourname")
    private String portfolioUrl;
}
