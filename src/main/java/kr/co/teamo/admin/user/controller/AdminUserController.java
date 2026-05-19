package kr.co.teamo.admin.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.teamo.admin.dashboard.dto.UserCountDto;
import kr.co.teamo.admin.user.dto.AdminUserDto;
import kr.co.teamo.admin.user.service.AdminUserService;
import kr.co.teamo.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Admin - Users", description = "관리자 회원관리 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/users")
public class AdminUserController {
    private final AdminUserService adminUserService;

    @Operation(summary = "회원 조회", description = "닉네임,이메일 조회 API")
    @GetMapping("/search")
    public ApiResponse<List<AdminUserDto>> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email
    ) {
        return ApiResponse.ok(adminUserService.searchUsers(name,email));
    }

}
