package kr.co.teamo.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

    /** 댓글 내용 */
    @NotBlank(message = "댓글 내용을 입력해주세요.")
    @Size(max = 2000, message = "댓글은 2000자 이내로 입력해주세요.")
    private String content;

    /** 부모 댓글 아이디 (null = 루트 댓글, 값 있으면 대댓글) */
    private Long parentId;
}
