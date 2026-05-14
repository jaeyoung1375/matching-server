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
        String uri    = request.getRequestURI();
        String method = request.getMethod();

        log.debug("[JWT Filter] 요청 진입 → {} {}", method, uri);

        // 화이트리스트(소셜 로그인, oauth2) 경로는 바로 통과
        if (uri.startsWith("/oauth2") || uri.startsWith("/login")) {
            log.debug("[JWT Filter] 화이트리스트 경로 — 필터 통과: {}", uri);
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 추출
        String token = resolveBearerToken(request);

        // 토큰이 있는 경우에만 로직 수행
        if (StringUtils.hasText(token)) {
            log.debug("[JWT Filter] 토큰 존재 — 블랙리스트 및 유효성 검사 진행");

            // 블랙리스트 확인 (토큰이 존재할 때만 수행)
            if (Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token))) {
                log.warn("[JWT Filter] 블랙리스트 토큰 차단: {}", uri);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // 토큰 유효성 검증 및 인증 처리
            if (jwtTokenUtil.validateToken(token)) {
                Long userId = jwtTokenUtil.getUserId(token);
                String role = jwtTokenUtil.getRole(token);
                String authority = "ROLE_" + role;
                var authorities = List.of(new SimpleGrantedAuthority(authority));
                var authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("[JWT Filter] 인증 완료 — userId: {}, role: {}, authority: {}", userId, role, authority);
            } else {
                log.warn("[JWT Filter] 유효하지 않은 토큰 — 인증 없이 진행: {}", uri);
            }
        } else {
            log.debug("[JWT Filter] 토큰 없음 — 비인증 요청 통과: {}", uri);
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
