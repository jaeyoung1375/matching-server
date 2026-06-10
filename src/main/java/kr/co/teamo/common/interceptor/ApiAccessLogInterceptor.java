package kr.co.teamo.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.teamo.admin.log.dto.SystemLogCreateDto;
import kr.co.teamo.admin.log.service.SystemLogService;
import kr.co.teamo.common.util.ClientIpProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiAccessLogInterceptor implements HandlerInterceptor {

    private static final String START_TIME_ATTRIBUTE = "apiAccessLogStartTime";

    private final ClientIpProvider clientIpProvider;
    private final SystemLogService systemLogService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME_ATTRIBUTE, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        long elapsedMs = getElapsedMs(request);
        String ipAddress = clientIpProvider.getClientIp(request);
        Long userId = getUserId();

        log.info("[API] method={}, uri={}, status={}, elapsedMs={}, ip={}, userId={}",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                elapsedMs,
                ipAddress,
                userId);

        systemLogService.saveLog(SystemLogCreateDto.builder()
                .logTypeCd("BE")
                .userId(userId)
                .actionCd("API")
                .message(buildMessage(request, response))
                .detailContent(buildDetailContent(request, response, elapsedMs, ex))
                .ipAddress(ipAddress)
                .build());
    }

    private long getElapsedMs(HttpServletRequest request) {
        Object startTime = request.getAttribute(START_TIME_ATTRIBUTE);
        if (startTime instanceof Long) {
            return System.currentTimeMillis() - (Long) startTime;
        }
        return -1;
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

    private String buildMessage(HttpServletRequest request, HttpServletResponse response) {
        return request.getMethod() + " " + request.getRequestURI() + " " + response.getStatus();
    }

    private String buildDetailContent(HttpServletRequest request,
                                      HttpServletResponse response,
                                      long elapsedMs,
                                      Exception ex) {
        StringBuilder detail = new StringBuilder();
        detail.append("method=").append(request.getMethod()).append('\n');
        detail.append("uri=").append(request.getRequestURI()).append('\n');
        detail.append("queryString=").append(request.getQueryString()).append('\n');
        detail.append("status=").append(response.getStatus()).append('\n');
        detail.append("elapsedMs=").append(elapsedMs).append('\n');
        detail.append("userAgent=").append(request.getHeader("User-Agent")).append('\n');
        if (ex != null) {
            detail.append("exception=").append(ex.getClass().getName()).append(": ").append(ex.getMessage());
        }
        return detail.toString();
    }
}
