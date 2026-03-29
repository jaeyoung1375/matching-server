
package kr.co.teamo.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

	@Schema(description = "게시판아이디")
	private Long postId;

	@Schema(description = "사용자아이디")
	private Long userId;

	@Schema(description = "카테고리아이디")
	private Long categoryId;

	@Schema(description = "제목")
	private String title;

	@Schema(description = "내용")
	private String content;

	@Schema(description = "상태")
	private String status;

	@Schema(description = "진행방식구분코드")
	private String progressTypeCd;

	@Schema(description = "진행방식구분코드명")
	private String progressTypeNm;

	@Schema(description = "상태")
	private String progressPeriod;

	@Schema(description = "모집포지션구분코드")
	private String recruitPositTypeCd;

	@Schema(description = "모집포지션구분코드명")
	private String recruitPositTypeNm;

	@Schema(description = "모집구분코드명")
	private String recruitTypeCd;

	@Schema(description = "모집구분코드명")
	private String recruitTypeNm;

	@Schema(description = "기술스택")
	private String techStack;

	@Schema(description = "모집마감일")
	private String recruitEndDate;

	@Schema(description = "사용여부")
	private String useYn;

	@Schema(description = "조회수")
	private Long viewCnt;

	@Schema(description = "등록일시")
	private String regDt;

	@Schema(description = "수정일시")
	private String modDt;
}
