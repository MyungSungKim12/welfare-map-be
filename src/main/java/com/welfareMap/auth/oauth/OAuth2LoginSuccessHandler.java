package com.welfareMap.auth.oauth;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.welfareMap.auth.config.AuthProperties;
import com.welfareMap.auth.dto.AuthUserDto;
import com.welfareMap.auth.service.JwtTokenService;
import com.welfareMap.user.service.UserAccountService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuthUserMapper oAuthUserMapper;
    private final JwtTokenService jwtTokenService;
    private final AuthProperties authProperties;
    private final UserAccountService userAccountService;

    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException {
        if (!(authentication instanceof OAuth2AuthenticationToken oAuth2Authentication)) {
            response.sendRedirect(frontendUrl + "/login?error=unsupported_auth");
            return;
        }

        AuthUserDto user = userAccountService.upsertOAuthUser(oAuthUserMapper.map(oAuth2Authentication));
        String token = jwtTokenService.createAccessToken(user);
        ResponseCookie cookie = ResponseCookie.from(authProperties.getCookieName(), token)
            .httpOnly(true)
            .secure(authProperties.isCookieSecure())
            .sameSite("Lax")
            .path("/")
            .maxAge(Duration.ofSeconds(authProperties.getCookieMaxAgeSeconds()))
            .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.sendRedirect(
            frontendUrl
                + "/api/auth/callback?token=" + encode(token)
                + "&provider=" + encode(user.provider())
        );
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
