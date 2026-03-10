package kr.co.teamo.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter
@Schema(description = "이메일 회원가입 DTO")
public class SignupRequest {
    private String email;
    private String password;
    private String name;
    private String phone;
}
