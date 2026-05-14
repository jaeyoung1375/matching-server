package kr.co.teamo.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.teamo.admin.dto.UserCountDto;
import kr.co.teamo.admin.service.AdminService;
import kr.co.teamo.auth.util.JwtTokenUtil;
import kr.co.teamo.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin", description = "관리자 관련 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final JwtTokenUtil jwtTokenUtil;

    private Long resolveCurrentUserId() {
        try {
            return jwtTokenUtil.getMemberIdFromSecurityContext();
        } catch (Exception e) {
            return null;
        }
    }

    @Operation(summary = "회원수 조회", description = "회원수 조회 API")
    @GetMapping("/users/counts")
    public ApiResponse<UserCountDto> selectUser(){
        return ApiResponse.ok(adminService.getUserCount());
    }
}
