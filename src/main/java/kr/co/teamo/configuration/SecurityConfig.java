package kr.co.teamo.configuration;

import kr.co.teamo.auth.filter.JwtAuthenticationFilter;
import kr.co.teamo.auth.handler.OAuth2SuccessHandler;
import kr.co.teamo.auth.security.RestAccessDeniedHandler;
import kr.co.teamo.auth.security.RestAuthenticationEntryPoint;
import kr.co.teamo.auth.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenUtil jwtTokenUtil;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;
    private final CorsConfig corsConfig;

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            OAuth2SuccessHandler oAuth2SuccessHandler,
            ClientRegistrationRepository clientRegistrationRepository,
            RedisTemplate<Object, Object> redisTemplate) throws Exception {

        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtTokenUtil,redisTemplate);

        http
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                        .accessDeniedHandler(restAccessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/v1/public/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers(
                                "/oauth2/**",
                                "/login/oauth2/**",
                                "/login/**",
                                "/upload/**"
                        ).permitAll()
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(auth -> auth
                                .authorizationRequestResolver(
                                        authorizationRequestResolver(clientRegistrationRepository)
                                )
                        )
                        .successHandler(oAuth2SuccessHandler)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * PKCE 제거 (카카오 로그인 오류 해결용)
     */
    @Bean
    public OAuth2AuthorizationRequestResolver authorizationRequestResolver(
            ClientRegistrationRepository repo) {

        DefaultOAuth2AuthorizationRequestResolver resolver =
                new DefaultOAuth2AuthorizationRequestResolver(
                        repo, "/oauth2/authorization");

        resolver.setAuthorizationRequestCustomizer(builder -> {
            // 카카오는 PKCE 미지원 → PKCE 파라미터 제거. Google/GitHub은 PKCE 필수이므로 유지
            final String[] registrationId = {null};
            builder.attributes(attrs -> {
                registrationId[0] = (String) attrs.get("registration_id");
                if ("kakao".equals(registrationId[0])) {
                    attrs.remove("code_challenge_method");
                }
            });
            if ("kakao".equals(registrationId[0])) {
                builder.additionalParameters(params -> {
                    params.remove("code_challenge");
                    params.remove("code_challenge_method");
                });
            }
        });

        return resolver;
    }


}
