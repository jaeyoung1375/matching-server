package kr.co.teamo.common.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum UserErrorCode implements ResponseCode {

	/** 이메일 또는 비밀번호가 올바르지 않습니다. */
	INVALID_INFO("U0001", HttpStatus.OK, "이메일 또는 비밀번호가 올바르지 않습니다." ),
	/** 비밀번호가 올바르지 않습니다. */
	INVALID_PASSWORD("U0002", HttpStatus.OK, "비밀번호가 올바르지 않습니다."),
	/** 유효하지 않은 refresh token 입니다. */
	INVALID_REFRESH_TOKEN("U0003", HttpStatus.INTERNAL_SERVER_ERROR, "유효하지 않은 refresh token 입니다."),
	/** 로그아웃된 토큰입니다. */
	INVALID_LOGOUT_TOKEN("U0004", HttpStatus.INTERNAL_SERVER_ERROR, "로그아웃된 토큰입니다.");



	/** 코드 */
    private final String code;

    /** HttpStatus */
    private final HttpStatus httpStatus;

    /** 메시지 키 */
    private final String message;

}
