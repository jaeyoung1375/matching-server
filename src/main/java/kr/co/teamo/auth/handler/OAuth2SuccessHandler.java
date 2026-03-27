package kr.co.teamo.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.teamo.auth.dto.SocialLoginResponse;
import kr.co.teamo.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        Object emailObj = user.getAttribute("email");
        Object nameObj = user.getAttribute("name");
        Object subObj = user.getAttribute("sub");

        String email = emailObj != null ? emailObj.toString() : null;
        String name = nameObj != null ? nameObj.toString() : null;
        String providerUserId = subObj != null ? subObj.toString() : null;

        if (providerUserId == null) {
            throw new RuntimeException("providerUserId 없음");
        }

        String provider = "GOOGLE";

        SocialLoginResponse loginResponse = authService.socialLogin(email, name, provider, providerUserId);

        String accessToken = loginResponse.getAccessToken();
        boolean isNew = loginResponse.isNew();

        response.sendRedirect("http://localhost:3000/?token=" + accessToken + "&isNew=" + isNew);
    }
}