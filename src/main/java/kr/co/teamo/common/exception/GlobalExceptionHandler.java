package kr.co.teamo.common.exception;

import kr.co.teamo.common.code.CommonErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.co.teamo.common.code.ResponseCode;
import kr.co.teamo.common.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e){

        ResponseCode code = e.getResponseCode();

        return ResponseEntity
                .status(code.getHttpStatus())
                .body(ApiResponse.error(code));
    }

    /** @Valid 검증 실패 → 첫 번째 필드 에러 메시지를 400으로 반환 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException e){

        String message = e.getBindingResult().getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse(CommonErrorCode.INVALID_INPUT.getMessage());

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(CommonErrorCode.INVALID_INPUT, message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e){

        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.error(CommonErrorCode.INTERNAL_SERVER_ERROR));
    }
}