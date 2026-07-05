package com.welfareMap.auth.controller;

import java.time.Duration;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.welfareMap.auth.config.AuthProperties;
import com.welfareMap.auth.dto.AuthUserDto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthProperties authProperties;

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }

    @GetMapping("/me")
    public ResponseEntity<AuthUserDto> me(@AuthenticationPrincipal AuthUserDto user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        ResponseCookie expiredCookie = ResponseCookie.from(authProperties.getCookieName(), "")
            .httpOnly(true)
            .secure(authProperties.isCookieSecure())
            .sameSite("Lax")
            .path("/")
            .maxAge(Duration.ZERO)
            .build();
        ResponseCookie expiredSessionCookie = ResponseCookie.from("JSESSIONID", "")
            .httpOnly(true)
            .secure(authProperties.isCookieSecure())
            .sameSite("Lax")
            .path("/")
            .maxAge(Duration.ZERO)
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, expiredCookie.toString())
            .header(HttpHeaders.SET_COOKIE, expiredSessionCookie.toString())
            .body(Map.of("status", "ok"));
    }
}
