package kr.co.teamo.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialAccount {
    private Long socialId;
    private Long userId;
    private String provider;
    private String providerUserId;
}