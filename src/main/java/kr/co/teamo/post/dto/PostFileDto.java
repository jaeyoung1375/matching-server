package kr.co.teamo.post.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostFileDto {

	@Schema(description = "게시판파일아이디")
	private Long postFileId;

	@Schema(description = "게시판아이디")
	private Long postId;

	@Schema(description = "파일아이디")
	private Long fileId;

	@Schema(description = "정렬순서")
	private Long sortSeq;

	@Schema(description = "등록일자")
	private Date regDt;

	@Schema(description = "변경일자")
	private Date modDt;

}
