package kr.co.teamo.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.teamo.auth.dto.SocialLoginResponse;
import kr.co.teamo.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2AuthenticationToken oauthToken =
                (OAuth2AuthenticationToken) authentication;

        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        String registrationId = oauthToken.getAuthorizedClientRegistrationId();

        String email = null;
        String name = null;
        String provider = registrationId.toUpperCase();
        String providerUserId = null;

        OAuth2AuthorizedClient client =
                authorizedClientService.loadAuthorizedClient(
                        oauthToken.getAuthorizedClientRegistrationId(),
                        oauthToken.getName()
                );

        String providerAccessToken = client.getAccessToken().getTokenValue();


        switch (registrationId) {

            // ✅ GOOGLE
            case "google":
                email = user.getAttribute("email");
                name = user.getAttribute("name");
                providerUserId = user.getAttribute("sub");
                break;

            // ✅ KAKAO
            case "kakao":
                Map<String, Object> kakaoAccount = user.getAttribute("kakao_account");
                Map<String, Object> profile = kakaoAccount != null
                        ? (Map<String, Object>) kakaoAccount.get("profile")
                        : null;

                email = kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
                name = profile != null ? (String) profile.get("nickname") : null;

                Object kakaoId = user.getAttribute("id");
                providerUserId = kakaoId != null ? kakaoId.toString() : null;
                break;

            // ✅ GITHUB (추가된 부분)
            case "github":
                email = user.getAttribute("email"); // 없을 수 있음
                name = user.getAttribute("login"); // username
                Object githubId = user.getAttribute("id");
                providerUserId = githubId != null ? githubId.toString() : null;
                break;

            default:
                throw new RuntimeException("지원하지 않는 소셜 로그인: " + registrationId);
        }

        // ✅ providerUserId 필수 체크
        if (providerUserId == null) {
            throw new RuntimeException("providerUserId 없음");
        }

        // ✅ email 없을 경우 (카카오 + GitHub 공통 처리)
        if (email == null || email.isBlank()) {
            email = provider + "_" + providerUserId + "@social.local";
        }

        // ✅ name 없을 경우 대비 (선택)
        if (name == null || name.isBlank()) {
            name = provider + "_user";
        }

        // ✅ 로그인 처리
        SocialLoginResponse loginResponse =
                authService.socialLogin(email, name, provider, providerUserId, providerAccessToken );

        String accessToken = loginResponse.getAccessToken();
        boolean isNew = loginResponse.isNew();

        // ✅ 프론트로 리다이렉트
        response.sendRedirect(
                frontendUrl + "/?token=" + accessToken + "&isNew=" + isNew
        );
    }
}