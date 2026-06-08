package kr.co.teamo.post.dto;



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
public class PostRecruitPositDto {

	@Schema(description = "게시판아이디")
	private Long postId;

	@Schema(description = "모집포지션구분코드")
	private String recruitPositTypeCd;

	@Schema(description = "모집포지션구분코드")
	private String recruitPositTypeNm;

	@Schema(description = "현재인원")
	private Long currentCnt;

	@Schema(description = "모집인원")
	private Long recruitCnt;
}
