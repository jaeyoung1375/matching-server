package kr.co.teamo.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "포지션별 모집인원 DTO")
public class RecruitPositionDto {

    @Schema(description = "모집 포지션 코드", example = "10")
    private String recruitPositTypeCd;

    @Schema(description = "모집 인원", example = "2")
    private int recruitCnt;
}
