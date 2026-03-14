package kr.co.teamo.post.dto;


import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {

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

	@Schema(description = "조회수")
	private Long viewCnt;

	@Schema(description = "모집구분코드")
	private String recruitTypeCd;

	@Schema(description = "모집인원")
	private Long recruitCnt;

	@Schema(description = "진행방식구분코드")
	private String progressTypeCd;

	@Schema(description = "진행기간")
	private String progressPeriod;

	@Schema(description = "기술스택구분코드")
	private String techStackTypeCd;

	@Schema(description = "모집마감일")
	private Date recruitEndDate;

	@Schema(description = "모집포지션구분코드")
	private String recruitPositTypeCd;

	@Schema(description = "연락방법구분코드")
	private String contactMethodCd;








}
