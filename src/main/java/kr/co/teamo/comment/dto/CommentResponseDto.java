package kr.co.teamo.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    /** 댓글 아이디 */
    private Long commentId;

    /** 게시글 아이디 */
    private Long postId;

    /** 작성자 아이디 */
    private Long userId;

    /** 작성자 이름 */
    private String userName;

    /** 프로필 이미지 URL */
    private String profileImageUrl;

    /** 댓글 내용 */
    private String content;

    /** 부모 댓글 아이디 (null = 루트 댓글) */
    private Long parentId;

    /** 사용여부 */
    private String useYn;

    /** 등록일시 */
    private String regDt;

    /** 수정일시 */
    private String modDt;

    @JsonProperty("isOwner")
    private Boolean owner;

    /** 대댓글 목록 (서비스에서 트리 구성 후 주입) */
    private List<CommentResponseDto> children;
}
