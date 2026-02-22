package kr.co.matching.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")               // 모든 URL에 대해
                        .allowedOrigins("*")             // 모든 출처 허용 (특정 도메인만 허용 시 도메인 명시)
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드
                        .allowedHeaders("*")             // 모든 헤더 허용
                        .allowCredentials(false)         // 인증정보 포함 여부 (쿠키 등), false면 allowedOrigins에 * 가능
                        .maxAge(3600);                   // preflight 요청 캐시 시간 (초)
            }
        };
    }
}