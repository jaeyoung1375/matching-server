package kr.co.teamo.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.teamo.auth.dto.User;
import kr.co.teamo.auth.service.AuthService;
import kr.co.teamo.auth.util.JwtTokenUtil;
import kr.co.teamo.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenUtil jwtTokenUtil;

    @Operation(summary = "로그인한 사용자 정보 조회", description = "로그인한 사용자 정보 조회 API")
    @GetMapping("/me")
    public ApiResponse<User> getMe(){
        return ApiResponse.ok(authService.getMe());
    }

    @Operation(summary = "회원탈퇴", description = "회원탈퇴 API")
    @DeleteMapping("/me")
    public ApiResponse<Void> withdrawMe(){
        Long userId = jwtTokenUtil.getMemberIdFromSecurityContext();
        authService.withdrawUser(userId);
        return ApiResponse.ok();
    }

    @Operation(summary = "로그아웃", description = "로그아웃 API")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(){
        Long userId = jwtTokenUtil.getMemberIdFromSecurityContext();
        authService.logout(userId);
        return ApiResponse.ok();
    }

}
