package kr.co.teamo.auth.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.teamo.auth.dto.TechStackResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class TechStackCacheService {

    private final RedisTemplate<String, Object> jsonRedisTemplate;
    private final ObjectMapper objectMapper;

    public TechStackCacheService(@Qualifier("jsonRedisTemplate") RedisTemplate<String, Object> jsonRedisTemplate,
                                 ObjectMapper objectMapper) {
        this.jsonRedisTemplate = jsonRedisTemplate;
        this.objectMapper = objectMapper;
    }

    private static final String KEY = "code:tech-stacks";
    private static final Duration TTL = Duration.ofHours(6);

    public void save(List<TechStackResponse> list) {
        jsonRedisTemplate.opsForValue().set(KEY, list, TTL);
    }

    public List<TechStackResponse> findAll() {
        Object value = jsonRedisTemplate.opsForValue().get(KEY);
        if (value == null) return null;
        return objectMapper.convertValue(value, new TypeReference<List<TechStackResponse>>() {});
    }
}
