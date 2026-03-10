package kr.co.teamo.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Schema(description = "회원 DTO")
public class UserInsertDto {

    @Schema(description = "회원아이디")
    private Long userId;          // selectKey로 채워짐

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "암호화비밀번호")
    private String passwordHash;

    @Schema(description = "상태값")
    private String status;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "폰번호")
    private String phone;

    @Schema(description = "권한")
    private String role;

    @Schema(description = "프로필")
    private Long profileFileId;

}
