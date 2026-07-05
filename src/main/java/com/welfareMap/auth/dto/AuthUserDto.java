package com.welfareMap.auth.dto;

public record AuthUserDto(
    String userId,
    String provider,
    String providerUserId,
    String email,
    String nickname,
    String profileImageUrl
) {
    public AuthUserDto withUserId(String nextUserId) {
        return new AuthUserDto(nextUserId, provider, providerUserId, email, nickname, profileImageUrl);
    }

    public String subject() {
        return provider + ":" + providerUserId;
    }
}
