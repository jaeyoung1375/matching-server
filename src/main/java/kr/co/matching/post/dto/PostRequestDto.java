package kr.co.matching.post.dto;


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



}
