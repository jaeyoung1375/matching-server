package kr.co.teamo.common.log.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.co.teamo.admin.log.dto.SystemLogCreateDto;
import kr.co.teamo.admin.log.service.SystemLogService;
import kr.co.teamo.common.log.dto.FrontendLogRequest;
import kr.co.teamo.common.response.ApiResponse;
import kr.co.teamo.common.util.ClientIpProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/client-logs")
public class ClientLogController {

    private static final int MESSAGE_MAX_LENGTH = 500;

    private final SystemLogService systemLogService;
    private final ClientIpProvider clientIpProvider;

    @PostMapping
    public ApiResponse<Void> saveFrontendLog(@Valid @RequestBody FrontendLogRequest request,
                                             HttpServletRequest httpServletRequest) {
        systemLogService.saveLog(SystemLogCreateDto.builder()
                .logTypeCd("FE")
                .userId(getUserId())
                .actionCd(normalizeLevel(request.getLevel()))
                .message(truncate(request.getMessage(), MESSAGE_MAX_LENGTH))
                .detailContent(buildDetailContent(request, httpServletRequest))
                .ipAddress(clientIpProvider.getClientIp(httpServletRequest))
                .build());

        return ApiResponse.ok();
    }

    private String normalizeLevel(String level) {
        return truncate(level == null ? "ERROR" : level.trim().toUpperCase(), 10);
    }

    private String buildDetailContent(FrontendLogRequest request, HttpServletRequest httpServletRequest) {
        StringBuilder detail = new StringBuilder();
        detail.append("level=").append(request.getLevel()).append('\n');
        detail.append("pageUrl=").append(request.getPageUrl()).append('\n');
        detail.append("componentName=").append(request.getComponentName()).append('\n');
        detail.append("userAgent=").append(resolveUserAgent(request, httpServletRequest)).append('\n');
        detail.append("message=").append(request.getMessage()).append('\n');
        detail.append("stackTrace=").append(request.getStackTrace());
        return detail.toString();
    }

    private String resolveUserAgent(FrontendLogRequest request, HttpServletRequest httpServletRequest) {
        if (request.getUserAgent() != null && !request.getUserAgent().isBlank()) {
            return request.getUserAgent();
        }
        return httpServletRequest.getHeader("User-Agent");
    }

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Long) {
            return (Long) principal;
        }
        if (principal instanceof String) {
            try {
                return Long.parseLong((String) principal);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }
}
