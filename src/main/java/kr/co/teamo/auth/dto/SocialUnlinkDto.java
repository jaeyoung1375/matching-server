package kr.co.teamo.auth.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SocialUnlinkDto {
    private String provider;
    private String providerAccessToken;
    private LocalDateTime expiresAt;
}
