package kr.co.teamo.configuration;

import kr.co.teamo.auth.filter.JwtAuthenticationFilter;
import kr.co.teamo.auth.handler.OAuth2SuccessHandler;
import kr.co.teamo.auth.security.RestAuthenticationEntryPoint;
import kr.co.teamo.auth.service.CustomOAuth2UserService;
import kr.co.teamo.auth.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenUtil jwtTokenUtil;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final CorsConfig corsConfig;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            .oauth2Login(oauth -> oauth
                .authorizationEndpoint(auth -> auth
                    .authorizationRequestResolver(
                        authorizationRequestResolver(clientRegistrationRepository)
                    )
                )
            );

        return http.build();
    }

    @Bean
    public OAuth2AuthorizationRequestResolver authorizationRequestResolver(
            ClientRegistrationRepository repo) {

        DefaultOAuth2AuthorizationRequestResolver resolver =
                new DefaultOAuth2AuthorizationRequestResolver(
                        repo, "/oauth2/authorization");

        resolver.setAuthorizationRequestCustomizer(customizer -> {
            customizer.additionalParameters(params -> {
                params.remove("code_challenge");
                params.remove("code_challenge_method");
            });
        });

        return resolver;
    }
}
