package kr.co.teamo.admin.log.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.teamo.admin.log.dto.AdminLogDto;
import kr.co.teamo.admin.log.dto.AdminLogSearchRequest;
import kr.co.teamo.admin.log.service.SystemLogService;
import kr.co.teamo.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Admin - Logs", description = "관리자 로그 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/logs")
public class AdminLogController {
    private final SystemLogService systemLogService;

    @Operation(summary = "로그 조회", description = "일반회원 로그 / 관리자 로그 조회 API")
    @GetMapping
    public ApiResponse<List<AdminLogDto>> searchLogs(AdminLogSearchRequest adminLogSearchRequest) {
        return ApiResponse.ok(systemLogService.searchLogs(adminLogSearchRequest));
    }

}
