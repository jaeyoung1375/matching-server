package kr.co.matching.common;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

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