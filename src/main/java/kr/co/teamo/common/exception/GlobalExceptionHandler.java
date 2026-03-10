package kr.co.teamo.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.co.teamo.common.code.ResponseCode;
import kr.co.teamo.common.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	  @ExceptionHandler(CustomException.class)
	    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e){

	        ResponseCode code = e.getResponseCode();

	        return ResponseEntity.status(code.getHttpStatus())
	        		.body(ApiResponse.error(code));
	    }
}
