package kr.co.teamo.admin.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.teamo.admin.code.dto.AdminCodeDtlDto;
import kr.co.teamo.admin.code.dto.AdminCodeDtlRequest;
import kr.co.teamo.admin.code.dto.AdminCodeDtlUpdateRequest;
import kr.co.teamo.admin.code.dto.AdminCodeGroupDto;
import kr.co.teamo.admin.code.dto.AdminCodeGroupRequest;
import kr.co.teamo.admin.code.dto.AdminCodeGroupUpdateRequest;
import kr.co.teamo.admin.code.service.AdminCodeService;
import kr.co.teamo.auth.util.JwtTokenUtil;
import kr.co.teamo.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin - Codes", description = "관리자 공통코드 관리 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/codes")
public class AdminCodeController {

    private final AdminCodeService adminCodeService;
    private final JwtTokenUtil jwtTokenUtil;

    @Operation(summary = "공통코드 그룹 목록 조회", description = "comCdId, comCdNm, useYn으로 검색 가능")
    @GetMapping
    public ApiResponse<List<AdminCodeGroupDto>> getCodeGroups(@ModelAttribute AdminCodeGroupRequest req) {
        return ApiResponse.ok(adminCodeService.getCodeGroups(req));
    }

    @Operation(summary = "공통코드 그룹 등록")
    @PostMapping
    public ApiResponse<Void> createCodeGroup(@RequestBody @Valid AdminCodeGroupRequest req) {
        String adminId = String.valueOf(jwtTokenUtil.getMemberIdFromSecurityContext());
        adminCodeService.createCodeGroup(req, adminId);
        return ApiResponse.ok();
    }

    @Operation(summary = "공통코드 그룹 수정")
    @PutMapping("/{comCdId}")
    public ApiResponse<Void> updateCodeGroup(
            @PathVariable(name = "comCdId") String comCdId,
            @RequestBody @Valid AdminCodeGroupUpdateRequest req
    ) {
        String adminId = String.valueOf(jwtTokenUtil.getMemberIdFromSecurityContext());
        adminCodeService.modifyCodeGroup(comCdId, req, adminId);
        return ApiResponse.ok();
    }

    @Operation(summary = "공통코드 그룹 삭제 (USE_YN=N 처리)")
    @DeleteMapping("/{comCdId}")
    public ApiResponse<Void> deleteCodeGroup(@PathVariable(name = "comCdId") String comCdId) {
        String adminId = String.valueOf(jwtTokenUtil.getMemberIdFromSecurityContext());
        adminCodeService.removeCodeGroup(comCdId, adminId);
        return ApiResponse.ok();
    }

    @Operation(summary = "상세코드 목록 조회")
    @GetMapping({"/{comCdId}/details", "/{comCdId}"})
    public ApiResponse<List<AdminCodeDtlDto>> getCodeDetails(@PathVariable(name = "comCdId") String comCdId) {
        return ApiResponse.ok(adminCodeService.getCodeDetails(comCdId));
    }

    @Operation(summary = "상세코드 등록")
    @PostMapping("/{comCdId}/details")
    public ApiResponse<Void> createCodeDetail(
            @PathVariable(name = "comCdId") String comCdId,
            @RequestBody @Valid AdminCodeDtlRequest req
    ) {
        String adminId = String.valueOf(jwtTokenUtil.getMemberIdFromSecurityContext());
        adminCodeService.createCodeDetail(comCdId, req, adminId);
        return ApiResponse.ok();
    }

    @Operation(summary = "상세코드 수정")
    @PutMapping("/{comCdId}/details/{dtlCdId}")
    public ApiResponse<Void> updateCodeDetail(
            @PathVariable(name = "comCdId") String comCdId,
            @PathVariable(name = "dtlCdId") String dtlCdId,
            @RequestBody @Valid AdminCodeDtlUpdateRequest req
    ) {
        String adminId = String.valueOf(jwtTokenUtil.getMemberIdFromSecurityContext());
        adminCodeService.modifyCodeDetail(comCdId, dtlCdId, req, adminId);
        return ApiResponse.ok();
    }

    @Operation(summary = "상세코드 삭제 (USE_YN=N 처리)")
    @DeleteMapping("/{comCdId}/details/{dtlCdId}")
    public ApiResponse<Void> deleteCodeDetail(
            @PathVariable(name = "comCdId") String comCdId,
            @PathVariable(name = "dtlCdId") String dtlCdId
    ) {
        String adminId = String.valueOf(jwtTokenUtil.getMemberIdFromSecurityContext());
        adminCodeService.removeCodeDetail(comCdId, dtlCdId, adminId);
        return ApiResponse.ok();
    }
}
