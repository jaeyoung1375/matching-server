package kr.co.teamo.admin.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDto {
    private String userId;
    private String name;
    private String email;
    private String provider;
    private String role;
    private String status;
    private String regDt;
    private String lastLoginDt;
    private String filePath;
}
