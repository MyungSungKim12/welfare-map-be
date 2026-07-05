package com.welfareMap.auth.oauth;

import java.util.Map;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.welfareMap.auth.dto.AuthUserDto;

@Component
public class OAuthUserMapper {

    public AuthUserDto map(OAuth2AuthenticationToken authentication) {
        String provider = authentication.getAuthorizedClientRegistrationId();
        OAuth2User oAuth2User = authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        return switch (provider) {
            case "google" -> mapGoogle(attributes);
            case "kakao" -> mapKakao(attributes);
            case "naver" -> mapNaver(attributes);
            default -> new AuthUserDto(
                null,
                provider,
                value(attributes.get("id")),
                value(attributes.get("email")),
                value(attributes.get("name")),
                value(attributes.get("picture"))
            );
        };
    }

    private AuthUserDto mapGoogle(Map<String, Object> attributes) {
        return new AuthUserDto(
            null,
            "google",
            value(attributes.get("sub")),
            value(attributes.get("email")),
            value(attributes.get("name")),
            value(attributes.get("picture"))
        );
    }

    private AuthUserDto mapKakao(Map<String, Object> attributes) {
        Map<String, Object> properties = map(attributes.get("properties"));
        Map<String, Object> kakaoAccount = map(attributes.get("kakao_account"));
        Map<String, Object> profile = map(kakaoAccount.get("profile"));

        String nickname = firstValue(
            properties.get("nickname"),
            profile.get("nickname")
        );
        String profileImageUrl = firstValue(
            properties.get("profile_image"),
            profile.get("profile_image_url"),
            profile.get("thumbnail_image_url")
        );

        return new AuthUserDto(
            null,
            "kakao",
            value(attributes.get("id")),
            value(kakaoAccount.get("email")),
            nickname,
            profileImageUrl
        );
    }

    private AuthUserDto mapNaver(Map<String, Object> attributes) {
        Map<String, Object> response = map(attributes.get("response"));
        return new AuthUserDto(
            null,
            "naver",
            value(response.get("id")),
            value(response.get("email")),
            firstValue(response.get("nickname"), response.get("name")),
            value(response.get("profile_image"))
        );
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> map(Object value) {
        if (value instanceof Map<?, ?> rawMap) {
            return (Map<String, Object>) rawMap;
        }
        return Map.of();
    }

    private String firstValue(Object... values) {
        for (Object value : values) {
            String text = value(value);
            if (text != null && !text.isBlank()) {
                return text;
            }
        }
        return null;
    }

    private String value(Object value) {
        return value == null ? null : String.valueOf(value);
    }
}
