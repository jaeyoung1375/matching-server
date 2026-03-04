package kr.co.matching.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.matching.auth.dto.*;
import kr.co.matching.auth.service.AuthService;
import kr.co.matching.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AuthPublic", description = "회원 관련(PUBLIC) API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth/public")
public class AuthPublicController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "회원가입 API")
    @PostMapping("/signup")
    public ApiResponse<SignupResponse> signup(@RequestBody SignupRequest req) {
        SignupResponse res = authService.signup(req);
        return ApiResponse.ok(res);
    }

    @Operation(summary = "로그인", description = "로그인 API")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest req){
        LoginResponse res = authService.login(req);
        return ApiResponse.ok(res);
    }

    @Operation(summary = "RefreshToken 재발급", description = "RefreshToken 재발급 API")
    @PostMapping("/refresh")
    public ApiResponse<RefreshResponse> refreshToken(@RequestBody RefreshRequest req){
        RefreshResponse res = authService.refreshToken(req);
        return ApiResponse.ok(res);
    }
}
