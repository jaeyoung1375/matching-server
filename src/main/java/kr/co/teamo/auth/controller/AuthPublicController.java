package kr.co.teamo.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.teamo.auth.dto.*;
import kr.co.teamo.auth.service.AuthService;
import kr.co.teamo.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "AuthPublic", description = "회원 관련(PUBLIC) API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth/public")
public class AuthPublicController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "회원가입 API")
    @PostMapping("/signup")
    public ApiResponse<SignupResponse> signup(@Valid @RequestBody SignupRequest req) {
        SignupResponse res = authService.signup(req);
        return ApiResponse.ok(res);
    }

    @Operation(summary = "회원가입 이메일 중복검사", description = "회원가입 이메일 중복검사 API")
    @GetMapping("/exists-email")
    public boolean existsByEmail(@RequestParam String email) {
        return authService.existsByEmail(email);
    }

    @Operation(summary = "언어 종류 조회", description = "언어 종류 조회 API")
    @GetMapping("/tech-stacks")
    public ApiResponse<List<TechStackResponse>> getTechStack() {
        return ApiResponse.ok(
                authService.getTechStacks()
        );
    }

    @Operation(summary = "로그인", description = "로그인 API")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest req){
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
