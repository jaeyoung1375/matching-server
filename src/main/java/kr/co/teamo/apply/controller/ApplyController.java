package kr.co.teamo.apply.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.teamo.apply.dto.ApplyRequestDto;
import kr.co.teamo.apply.dto.ApplyResponseDto;
import kr.co.teamo.apply.service.ApplyService;
import kr.co.teamo.auth.util.JwtTokenUtil;
import kr.co.teamo.common.code.UserErrorCode;
import kr.co.teamo.common.exception.CustomException;
import kr.co.teamo.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Apply", description = "스터디/프로젝트 지원 API")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ApplyController {

    private final ApplyService applyService;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * 지원 등록
     */
    @Operation(summary = "지원 등록", description = "스터디/프로젝트에 지원합니다.")
    @PostMapping("/applies/{postId}")
    public ApiResponse<Void> createApply(
            @PathVariable(name = "postId") Long postId,
            @RequestBody @Valid ApplyRequestDto req) {

        Long userId = jwtTokenUtil.getMemberIdFromSecurityContext();
        if (ObjectUtils.isEmpty(userId)) {
            throw new CustomException(UserErrorCode.USER_NOT_FOUND);
        }

        req.setPostId(postId);
        req.setUserId(userId);

        applyService.createApply(req);
        return ApiResponse.ok();
    }

    /**
     * 게시글별 지원 목록 조회 (리더 전용)
     */
    @Operation(summary = "지원 목록 조회", description = "게시글에 지원한 목록을 조회합니다.")
    @GetMapping("/applies/{postId}")
    public ApiResponse<List<ApplyResponseDto>> getApplyList(
            @PathVariable Long postId) {

        return ApiResponse.ok(applyService.getApplyList(postId));
    }
}
