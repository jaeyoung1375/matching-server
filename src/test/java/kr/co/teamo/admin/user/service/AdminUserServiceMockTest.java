package kr.co.teamo.admin.user.service;

import kr.co.teamo.admin.mapper.AdminMapper;
import kr.co.teamo.admin.user.dto.AdminUserDto;
import kr.co.teamo.common.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceMockTest {

    @Mock
    AdminMapper adminMapper;

    @Mock
    RedisTemplate<Object, Object> redisTemplate;

    @Mock
    ValueOperations<Object, Object> valueOperations;

    @InjectMocks
    AdminUserService adminUserService;

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("searchUsers: provider가 null인 사용자는 EMAIL로 변환된다")
    void searchUsers_nullProviderConvertedToEmail() {
        AdminUserDto dto = new AdminUserDto();
        dto.setProvider(null);
        when(adminMapper.searchUsers(null, null)).thenReturn(List.of(dto));

        List<AdminUserDto> result = adminUserService.searchUsers(null, null);

        assertEquals("EMAIL", result.get(0).getProvider());
    }

    @Test
    @DisplayName("searchUsers: provider가 있는 사용자는 변경되지 않는다")
    void searchUsers_nonNullProviderUnchanged() {
        AdminUserDto dto = new AdminUserDto();
        dto.setProvider("KAKAO");
        when(adminMapper.searchUsers("홍길동", null)).thenReturn(List.of(dto));

        List<AdminUserDto> result = adminUserService.searchUsers("홍길동", null);

        assertEquals("KAKAO", result.get(0).getProvider());
    }

    @Test
    @DisplayName("searchUsers: 혼합 목록에서 null인 항목만 EMAIL로 변환된다")
    void searchUsers_mixedList_onlyNullConverted() {
        AdminUserDto dto1 = new AdminUserDto();
        dto1.setProvider(null);
        AdminUserDto dto2 = new AdminUserDto();
        dto2.setProvider("GITHUB");
        when(adminMapper.searchUsers(null, null)).thenReturn(List.of(dto1, dto2));

        List<AdminUserDto> result = adminUserService.searchUsers(null, null);

        assertEquals("EMAIL", result.get(0).getProvider());
        assertEquals("GITHUB", result.get(1).getProvider());
    }

    @Test
    @DisplayName("updateUserRole: 사용자가 존재하지 않으면 CustomException이 발생한다")
    void updateUserRole_throwsWhenUserNotFound() {
        when(adminMapper.selectUserStatus(1L)).thenReturn(null);

        assertThrows(CustomException.class, () -> adminUserService.updateUserRole(1L, "ROLE_ADMIN"));
        verify(adminMapper, never()).updateUserRole(anyLong(), anyString());
    }

    @Test
    @DisplayName("updateUserRole: ACTIVE가 아닌 사용자는 CustomException이 발생한다")
    void updateUserRole_throwsWhenUserNotActive() {
        when(adminMapper.selectUserStatus(1L)).thenReturn("WITHDRAWN");

        assertThrows(CustomException.class, () -> adminUserService.updateUserRole(1L, "ROLE_ADMIN"));
        verify(adminMapper, never()).updateUserRole(anyLong(), anyString());
    }

    @Test
    @DisplayName("updateUserRole: ACTIVE 사용자는 권한이 변경된다")
    void updateUserRole_successForActiveUser() {
        when(adminMapper.selectUserStatus(1L)).thenReturn("ACTIVE");

        adminUserService.updateUserRole(1L, "ROLE_ADMIN");

        verify(adminMapper).updateUserRole(1L, "ROLE_ADMIN");
    }

    @Test
    @DisplayName("forceLogout: ACTIVE 사용자는 refresh 토큰 삭제 및 force-logout 키가 설정된다")
    void forceLogout_setsRedisKeysForActiveUser() {
        when(adminMapper.selectUserStatus(1L)).thenReturn("ACTIVE");

        adminUserService.forceLogout(1L);

        verify(redisTemplate).delete("refresh:1");
        verify(valueOperations).set(eq("force-logout:1"), eq("true"), any(Duration.class));
    }

    @Test
    @DisplayName("forceLogout: 사용자가 존재하지 않으면 CustomException이 발생한다")
    void forceLogout_throwsWhenUserNotFound() {
        when(adminMapper.selectUserStatus(1L)).thenReturn(null);

        assertThrows(CustomException.class, () -> adminUserService.forceLogout(1L));
        verify(redisTemplate, never()).delete(any());
    }

    @Test
    @DisplayName("forceLogout: BLOCKED 사용자는 CustomException이 발생한다")
    void forceLogout_throwsWhenUserBlocked() {
        when(adminMapper.selectUserStatus(1L)).thenReturn("BLOCKED");

        assertThrows(CustomException.class, () -> adminUserService.forceLogout(1L));
    }
}
