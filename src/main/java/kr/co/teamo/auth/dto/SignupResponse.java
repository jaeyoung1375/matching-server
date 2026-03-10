package kr.co.teamo.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "이메일 회원가입 응답 DTO")
public class SignupResponse {
    private Long userId;
    private String email;
    private String message;
}
