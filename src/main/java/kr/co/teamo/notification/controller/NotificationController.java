package kr.co.teamo.notification.controller;

import kr.co.teamo.auth.util.JwtTokenUtil;
import kr.co.teamo.common.response.ApiResponse;
import kr.co.teamo.notification.dto.NotificationResponseDto;
import kr.co.teamo.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping("/notifications")
    public ApiResponse<List<NotificationResponseDto>> getNotifications() {
        Long userId = jwtTokenUtil.getMemberIdFromSecurityContext();
        return ApiResponse.ok(notificationService.getNotifications(userId));
    }

    @GetMapping("/notifications/unread-count")
    public ApiResponse<Integer> countUnreadNotifications() {
        Long userId = jwtTokenUtil.getMemberIdFromSecurityContext();
        return ApiResponse.ok(notificationService.countUnreadNotifications(userId));
    }

    @PatchMapping("/notifications/{notificationId}/read")
    public ApiResponse<Void> markAsRead(@PathVariable(name = "notificationId") Long notificationId) {
        Long userId = jwtTokenUtil.getMemberIdFromSecurityContext();
        notificationService.markAsRead(notificationId, userId);
        return ApiResponse.ok();
    }

    @PatchMapping("/notifications/read-all")
    public ApiResponse<Void> markAllAsRead() {
        Long userId = jwtTokenUtil.getMemberIdFromSecurityContext();
        notificationService.markAllAsRead(userId);
        return ApiResponse.ok();
    }
}
