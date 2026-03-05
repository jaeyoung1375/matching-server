package kr.co.matching.common.exception;

import kr.co.matching.common.code.ResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {

	 private final ResponseCode responseCode;
}
