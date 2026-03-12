package kr.co.teamo.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class User {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private String role;
    private String profileImageUrl;
}
