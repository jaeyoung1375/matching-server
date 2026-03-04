package kr.co.matching.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.matching.auth.dto.*;
import kr.co.matching.auth.service.AuthService;
import kr.co.matching.auth.util.JwtTokenUtil;
import kr.co.matching.common.ApiResponse;
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
