package kr.co.teamo.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeRequestDto {

	@Schema(description = "공통코드아이디")
	private String comCdId;

	@Schema(description = "상세코드아이디")
	private String dtlCdId;




}
