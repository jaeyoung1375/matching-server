package kr.co.matching.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "API 응답코드 DTO")
public class ApiResponse<T> {

	@Schema(description = "응답코드", example = "0000")
    private final String code;
    private final T data;

    private ApiResponse(String code, T data) {
        this.code = code;
        this.data = data;
    }

    /* ====== static factory ====== */

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>("0000", data);
    }

    public static <T> ApiResponse<T> of(String code, T data) {
        return new ApiResponse<>(code, data);
    }

    public static ApiResponse<Void> ok() {
        return new ApiResponse<>("0000", null);
    }
}