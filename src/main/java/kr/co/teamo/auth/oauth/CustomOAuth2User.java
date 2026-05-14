package kr.co.teamo.auth.oauth;

import lombok.Getter;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User {

    private final String email;
    private final String name;
    private final String provider;
    private final String providerUserId;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(String email, String name,
                            String provider, String providerUserId,
                            Map<String, Object> attributes) {
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.providerUserId = providerUserId;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_USER");
    }

    @Override
    public String getName() {
        return provider + "_" + providerUserId;
    }
}
