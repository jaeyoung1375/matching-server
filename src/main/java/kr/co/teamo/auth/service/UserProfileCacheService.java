package kr.co.teamo.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.teamo.auth.dto.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class UserProfileCacheService {

    private final RedisTemplate<String, Object> jsonRedisTemplate;
    private final ObjectMapper objectMapper;

    public UserProfileCacheService(@Qualifier("jsonRedisTemplate") RedisTemplate<String, Object> jsonRedisTemplate,
                                   ObjectMapper objectMapper) {
        this.jsonRedisTemplate = jsonRedisTemplate;
        this.objectMapper = objectMapper;
    }

    private static final String PREFIX = "user:profile:";
    private static final Duration TTL = Duration.ofMinutes(30);

    public void save(Long userId, User user) {
        jsonRedisTemplate.opsForValue().set(PREFIX + userId, user, TTL);
    }

    public User findByUserId(Long userId) {
        Object value = jsonRedisTemplate.opsForValue().get(PREFIX + userId);
        if (value == null) return null;
        return objectMapper.convertValue(value, User.class);
    }

    public void delete(Long userId) {
        jsonRedisTemplate.delete(PREFIX + userId);
    }
}
