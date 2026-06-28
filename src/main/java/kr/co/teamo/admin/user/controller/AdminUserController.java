package kr.co.teamo.admin.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.teamo.admin.user.dto.AdminUserDto;
import kr.co.teamo.admin.user.dto.UpdateUserRole;
import kr.co.teamo.admin.user.service.AdminUserService;
import kr.co.teamo.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "email", required = false) String email
    ) {
        return ApiResponse.ok(adminUserService.searchUsers(name,email));
    }

    @Operation(summary = "회원 권한 변경", description = "회원 권한 변경 API")
    @PatchMapping("/{userId}/role")
    public ApiResponse<Void> updateUserRole(
            @PathVariable(name = "userId") Long userId, @RequestBody @Valid UpdateUserRole request
    ){
        adminUserService.updateUserRole(userId, request.getRole());
        return ApiResponse.ok();
    }

    @Operation(summary = "강제로그아웃", description = "강제로그아웃 API")
    @PostMapping("/{userId}/force-logout")
    public ApiResponse<Void> forceLogout(
            @PathVariable(name = "userId") Long userId
    ){
        adminUserService.forceLogout(userId);
        return ApiResponse.ok();
    }
}
