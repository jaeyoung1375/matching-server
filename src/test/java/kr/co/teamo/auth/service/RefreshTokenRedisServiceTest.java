package kr.co.teamo.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenRedisServiceTest {

    @Mock
    StringRedisTemplate redisTemplate;

    @Mock
    ValueOperations<String, String> valueOperations;

    @InjectMocks
    RefreshTokenRedisService service;

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("토큰 저장 시 키 형식과 TTL이 올바르게 지정된다")
    void save_storesTokenWithCorrectKeyAndTtl() {
        service.save(1L, "token123");
        verify(valueOperations).set("refresh:1", "token123", Duration.ofDays(14));
    }

    @Test
    @DisplayName("userId로 저장된 토큰을 조회한다")
    void findByUserId_returnsStoredToken() {
        when(valueOperations.get("refresh:1")).thenReturn("token123");
        assertEquals("token123", service.findByUserId(1L));
    }

    @Test
    @DisplayName("저장된 토큰과 일치하면 isValid가 true를 반환한다")
    void isValid_returnsTrueWhenTokenMatches() {
        when(valueOperations.get("refresh:1")).thenReturn("token123");
        assertTrue(service.isValid(1L, "token123"));
    }

    @Test
    @DisplayName("저장된 토큰과 다르면 isValid가 false를 반환한다")
    void isValid_returnsFalseWhenTokenMismatch() {
        when(valueOperations.get("refresh:1")).thenReturn("other-token");
        assertFalse(service.isValid(1L, "token123"));
    }

    @Test
    @DisplayName("Redis에 토큰이 없으면 isValid가 false를 반환한다")
    void isValid_returnsFalseWhenNoTokenFound() {
        when(valueOperations.get("refresh:1")).thenReturn(null);
        assertFalse(service.isValid(1L, "token123"));
    }

    @Test
    @DisplayName("delete 호출 시 refresh:{userId} 키가 삭제된다")
    void delete_removesRefreshKey() {
        service.delete(1L);
        verify(redisTemplate).delete("refresh:1");
    }
}
