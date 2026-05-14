package kr.co.teamo.common.exception;

import kr.co.teamo.common.code.ResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {

	 private final ResponseCode responseCode;
}
