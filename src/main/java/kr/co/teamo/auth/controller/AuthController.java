package kr.co.teamo.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.teamo.auth.dto.UpdateUserRequest;
import kr.co.teamo.auth.dto.User;
import kr.co.teamo.auth.dto.WithdrawRequest;
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
    public ApiResponse<Void> withdrawMe(@RequestBody(required = false) WithdrawRequest request){
        Long userId = jwtTokenUtil.getMemberIdFromSecurityContext();
        String currentPassword = request != null ? request.getCurrentPassword() : null;
        authService.withdrawUser(userId, currentPassword);
        return ApiResponse.ok();
    }

    @Operation(summary = "로그아웃", description = "로그아웃 API")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request){
        Long userId = jwtTokenUtil.getMemberIdFromSecurityContext();

        String accessToken = resolveToken(request);

        authService.logout(userId, accessToken);
        return ApiResponse.ok();
    }

    @Operation(summary = "회원수정", description = "회원수정 API")
    @PutMapping("/me")
    public ApiResponse<Void> updateMe(@RequestBody UpdateUserRequest updateUserRequest){
        Long userId = jwtTokenUtil.getMemberIdFromSecurityContext();
        authService.updateMe(userId,updateUserRequest);
        return ApiResponse.ok();
    }

    // 토큰 추출 메서드
    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");

        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

}
