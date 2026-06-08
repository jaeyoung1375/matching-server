package kr.co.teamo.notification.mapper;

import kr.co.teamo.notification.dto.NotificationCreateDto;
import kr.co.teamo.notification.dto.NotificationResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationMapper {
    void insertNotification(NotificationCreateDto notificationCreateDto);
    List<NotificationResponseDto> selectNotifications(@Param("userId") Long userId);
    int countUnreadNotifications(@Param("userId") Long userId);
    int markAsRead(@Param("notificationId") Long notificationId, @Param("userId") Long userId);
    int markAllAsRead(@Param("userId") Long userId);
}
