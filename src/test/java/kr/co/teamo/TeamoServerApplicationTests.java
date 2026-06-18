package kr.co.teamo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import javax.sql.DataSource;

@SpringBootTest
class teamoServerApplicationTests {

    @MockBean
    DataSource dataSource;

    @MockBean
    RedisConnectionFactory redisConnectionFactory;

    @Test
    void contextLoads() {
    }
}
