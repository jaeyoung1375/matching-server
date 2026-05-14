package kr.co.teamo.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SocialLoginResponse {

    private String accessToken;
    private String refreshToken;
    private boolean isNew; //신규 유저 여부

    public SocialLoginResponse(String accessToken, String refreshToken, boolean isNew) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isNew = isNew;
    }
}
