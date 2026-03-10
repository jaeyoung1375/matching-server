package kr.co.teamo.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenRedisService {

    private final StringRedisTemplate redisTemplate;

    private static final String PREFIX = "refresh:";
    private static final Duration REFRESH_TOKEN_TTL = Duration.ofDays(14);

    public void save(Long userId, String refreshToken) {
        redisTemplate.opsForValue().set(
                PREFIX + userId,
                refreshToken,
                REFRESH_TOKEN_TTL
        );
    }

    public String findByUserId(Long userId) {
        return redisTemplate.opsForValue().get(PREFIX + userId);
    }

    public boolean isValid(Long userId, String refreshToken) {
        String savedToken = findByUserId(userId);
        return savedToken != null && savedToken.equals(refreshToken);
    }

    public void delete(Long userId) {
        redisTemplate.delete(PREFIX + userId);
    }
}