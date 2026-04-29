package kr.co.teamo.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.teamo.auth.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate<Object, Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();

        // 화이트리스트(로그인, oauth2) 경로는 바로 통과
        if (uri.startsWith("/oauth2") || uri.startsWith("/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 추출
        String token = resolveBearerToken(request);

        // 토큰이 있는 경우에만 로직 수행
        if (StringUtils.hasText(token)) {

            // 블랙리스트 확인 (토큰이 존재할 때만 수행)
            if (Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token))) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // 토큰 유효성 검증 및 인증 처리
            if (jwtTokenUtil.validateToken(token)) {
                Long userId = jwtTokenUtil.getUserId(token);
                var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
                var authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveBearerToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(header)) return null;

        // "Bearer xxx"
        if (header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
