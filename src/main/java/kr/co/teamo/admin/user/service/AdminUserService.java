package kr.co.teamo.admin.user.service;

import kr.co.teamo.admin.mapper.AdminMapper;
import kr.co.teamo.admin.user.dto.AdminUserDto;
import kr.co.teamo.auth.service.UserProfileCacheService;
import kr.co.teamo.common.code.UserErrorCode;
import kr.co.teamo.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUserService {
    private final AdminMapper adminMapper;
    private final RedisTemplate<Object,Object> redisTemplate;
    private final UserProfileCacheService userProfileCacheService;

    public List<AdminUserDto> searchUsers(String name, String email){
        List<AdminUserDto> users = adminMapper.searchUsers(name, email);

        users.forEach(user->{
            if(user.getProvider() == null){
                user.setProvider("EMAIL");
            }
        });
        return users;
    }

    // ACTIVE 상태 여부 검증
    private void validateActiveUser(Long userId) {
        String status = adminMapper.selectUserStatus(userId);
        if (status == null) {
            throw new CustomException(UserErrorCode.USER_NOT_FOUND);
        }
        if (!"ACTIVE".equals(status)) {
            throw new CustomException(UserErrorCode.USER_NOT_ACTIVE);
        }
    }

    // 회원 상세 > 권한 변경
    public void updateUserRole(Long userId, String role){
        validateActiveUser(userId);
        adminMapper.updateUserRole(userId, role);
        userProfileCacheService.delete(userId);
    }

    // 회원 상세 > 강제 로그아웃
    public void forceLogout(Long userId) {
        validateActiveUser(userId);
        redisTemplate.delete("refresh:" + userId);
        redisTemplate.opsForValue().set(
                "force-logout:" + userId,
                "true",
                Duration.ofHours(24)
        );
    }

}
