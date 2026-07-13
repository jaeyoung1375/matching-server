package kr.co.teamo.apply.controller;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.teamo.apply.dto.ApplyRequestDto;
import kr.co.teamo.apply.dto.ApplyResponseDto;
import kr.co.teamo.apply.enums.ApplyStatus;
import kr.co.teamo.apply.service.ApplyService;
import kr.co.teamo.auth.util.JwtTokenUtil;
import kr.co.teamo.common.code.CommonErrorCode;
import kr.co.teamo.common.code.UserErrorCode;
import kr.co.teamo.common.exception.CustomException;
import kr.co.teamo.common.response.ApiResponse;
import kr.co.teamo.post.service.PostService;
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
    private final PostService postService;
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
            @PathVariable(name = "postId") Long postId) {

        return ApiResponse.ok(applyService.getApplyList(postId));
    }


    /**
     * 지원 수락/거절
     */
    @Operation(summary = "지원 수락/거절", description = "게시글에 지원한 목록을 수락/거절합니다.")
    @PatchMapping("/applies/{applyId}/status")
    public ApiResponse<Void> modifyApplyStatus(
            @PathVariable(name = "applyId") Long applyId, @RequestBody ApplyRequestDto req) {

    	// 현재 로그안 사용자 조회
    	Long userId = jwtTokenUtil.getMemberIdFromSecurityContext();
    	Long PostId = applyService.findByApplyId(applyId).getPostId();

    	// 스터디장 조회
    	Long leaderId = postService.findByPostId(PostId).getUserId();


    	// 현재 로그인 사용자가 스터디장이 아니면 예외처리 (보안취약점으로 클라이언트가 변조 요청 우려)
    	if(!userId.equals(leaderId)) {
    		throw new CustomException(CommonErrorCode.ACCESS_DENIED);
    	}


    	// 상태코드를 화이트르스토로 관리
    	ApplyStatus status = ApplyStatus.from(req.getStatusCd());



    	applyService.updateApply(applyId, status);

        return ApiResponse.ok();
    }
}
