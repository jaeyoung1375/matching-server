package kr.co.teamo.admin.user.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDto {
    private Long userId;
    private String name;
    private String email;
    private String provider;
    private String role;
    private String status;
    private LocalDateTime regDt;
    private LocalDateTime lastLoginDt;
    private String filePath;
}
