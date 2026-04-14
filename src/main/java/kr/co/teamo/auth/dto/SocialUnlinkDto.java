package kr.co.teamo.auth.dto;

import lombok.Getter;

@Getter
public class SocialUnlinkDto {
    private String provider;
    private String providerAccessToken;
}
