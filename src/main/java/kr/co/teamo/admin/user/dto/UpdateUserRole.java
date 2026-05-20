package kr.co.teamo.admin.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UpdateUserRole {
    @NotBlank
    @Pattern(regexp = "USER|ADMIN", message = "role은 USER 또는 ADMIN만 가능합니다.")
    private String role;
}
