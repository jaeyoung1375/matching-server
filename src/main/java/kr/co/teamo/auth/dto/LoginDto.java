package kr.co.teamo.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private Long userId;
    private String email;
    private String passwordHash;
    private String status;
}
