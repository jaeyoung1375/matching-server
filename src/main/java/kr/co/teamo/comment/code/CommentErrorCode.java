package kr.co.teamo.comment.code;

import kr.co.teamo.common.code.ResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@ToString
public enum CommentErrorCode implements ResponseCode {

    /** 댓글을 찾을 수 없습니다. */
    COMMENT_NOT_FOUND("C0001", HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),

    /** 댓글 삭제 권한이 없습니다. */
    COMMENT_NO_PERMISSION("C0002", HttpStatus.FORBIDDEN, "댓글 삭제 권한이 없습니다.");

    /** 코드 */
    private final String code;

    /** HttpStatus */
    private final HttpStatus httpStatus;

    /** 메시지 */
    private final String message;
}
