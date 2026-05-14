package kr.co.teamo.comment.dto;

import lombok.*;

/**
 * 댓글/대댓글 INSERT 용 DTO.
 * MyBatis &lt;selectKey&gt; 가 commentId 를 NEXTVAL 로 채워준다.
 */
@Getter
@Setter   // selectKey 로 commentId 를 주입받기 위해 Setter 필요
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentInsertDto {

    /** PK — selectKey 로 POST_COMMENT_SEQ.NEXTVAL 주입 */
    private Long commentId;

    /** 게시글 아이디 */
    private Long postId;

    /** 작성자 아이디 */
    private Long userId;

    /** 댓글 내용 */
    private String content;

    /** 부모 댓글 아이디 (null = 루트 댓글) */
    private Long parentId;
}
