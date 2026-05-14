package kr.co.teamo.common.code;

import org.springframework.http.HttpStatus;

public interface ResponseCode {

	String getCode();

	HttpStatus getHttpStatus();

	String getMessage();

}
