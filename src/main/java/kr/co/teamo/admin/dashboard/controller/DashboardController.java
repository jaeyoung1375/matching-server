package kr.co.teamo.admin.dashboard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.teamo.admin.dashboard.dto.NewUserCountDto;
import kr.co.teamo.admin.dashboard.dto.PostCountDto;
import kr.co.teamo.admin.dashboard.dto.UserCountDto;
import kr.co.teamo.admin.dashboard.service.DashboardService;
import kr.co.teamo.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin - Dashboard", description = "관리자 대시보드 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "회원수 조회", description = "전체/활성/탈퇴 회원수 조회 API")
    @GetMapping("/users/counts")
    public ApiResponse<UserCountDto> getUserCount() {
        return ApiResponse.ok(dashboardService.getUserCount());
    }

    @Operation(summary = "이번달 신규 회원수 조회", description = "이번달 신규 가입 회원수 조회 API")
    @GetMapping("/users/new-counts")
    public ApiResponse<NewUserCountDto> getNewUserCount() {
        return ApiResponse.ok(dashboardService.getNewUserCount());
    }

    @Operation(summary = "게시글 수 조회", description = "전체 게시글 수 조회 API")
    @GetMapping("/posts/counts")
    public ApiResponse<PostCountDto> getPostCount() {
        return ApiResponse.ok(dashboardService.getPostCount());
    }
}
