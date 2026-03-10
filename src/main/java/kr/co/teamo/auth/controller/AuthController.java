package kr.co.teamo.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenUtil jwtTokenUtil;

    @Operation(summary = "회원탈퇴", description = "회원탈퇴 API")
    @DeleteMapping("/me")
    public ApiResponse<Void> me(){
        Long userId = jwtTokenUtil.getMemberIdFromSecurityContext();
        authService.withdrawUser(userId);
        return ApiResponse.ok();
    }

}
