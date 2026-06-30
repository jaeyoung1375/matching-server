package kr.co.teamo.admin.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.teamo.admin.code.dto.AdminCodeDtlDto;
import kr.co.teamo.admin.code.dto.AdminCodeDtlUpdateRequest;
import kr.co.teamo.admin.code.dto.AdminCodeGroupUpdateRequest;
import kr.co.teamo.admin.code.service.AdminCodeService;
import kr.co.teamo.auth.util.JwtTokenUtil;
import kr.co.teamo.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Admin - Codes", description = "관리자 공통코드 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/code")
public class AdminCodeAliasController {

    private final AdminCodeService adminCodeService;
    private final JwtTokenUtil jwtTokenUtil;

    @Operation(summary = "상세코드 목록 조회")
    @GetMapping({"/{comCdId}", "/{comCdId}/details", "/{comCdId}/dtl"})
    public ApiResponse<List<AdminCodeDtlDto>> getCodeDetails(@PathVariable(name = "comCdId") String comCdId) {
        return ApiResponse.ok(adminCodeService.getCodeDetails(comCdId));
    }

    @Operation(summary = "공통코드 그룹 수정 (코드명/사용여부/정렬순서)")
    @PutMapping("/{comCdId}")
    public ApiResponse<Void> updateCodeGroup(
            @PathVariable(name = "comCdId") String comCdId,
            @RequestBody @Valid AdminCodeGroupUpdateRequest req
    ) {
        String adminId = String.valueOf(jwtTokenUtil.getMemberIdFromSecurityContext());
        adminCodeService.modifyCodeGroup(comCdId, req, adminId);
        return ApiResponse.ok();
    }

    @Operation(summary = "상세코드 전체 사용여부 일괄 변경")
    @PutMapping("/{comCdId}/use-yn")
    public ApiResponse<Void> updateAllDetailsUseYn(
            @PathVariable(name = "comCdId") String comCdId,
            @RequestBody Map<String, String> body
    ) {
        String useYn = body.get("useYn");
        String adminId = String.valueOf(jwtTokenUtil.getMemberIdFromSecurityContext());
        adminCodeService.modifyAllCodeDetailsUseYn(comCdId, useYn, adminId);
        return ApiResponse.ok();
    }

    @Operation(summary = "상세코드 수정")
    @PutMapping("/{comCdId}/{dtlCdId}")
    public ApiResponse<Void> updateCodeDetail(
            @PathVariable(name = "comCdId") String comCdId,
            @PathVariable(name = "dtlCdId") String dtlCdId,
            @RequestBody @Valid AdminCodeDtlUpdateRequest req
    ) {
        String adminId = String.valueOf(jwtTokenUtil.getMemberIdFromSecurityContext());
        adminCodeService.modifyCodeDetail(comCdId, dtlCdId, req, adminId);
        return ApiResponse.ok();
    }
}
