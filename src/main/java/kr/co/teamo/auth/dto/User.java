package kr.co.teamo.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Setter
public class User {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private String role;
    private String profileImageUrl;
    private String provider;
    private String passwordHash;
    private String status;
    private List<TechStackResponse> languages = new ArrayList<>();
}
