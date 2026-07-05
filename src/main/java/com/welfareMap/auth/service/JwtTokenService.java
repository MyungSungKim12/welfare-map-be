package com.welfareMap.auth.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.welfareMap.auth.dto.AuthUserDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtTokenService {

    private final SecretKey signingKey;
    private final long accessExpirationMillis;

    public JwtTokenService(
        @Value("${jwt.secret}") String jwtSecret,
        @Value("${jwt.access-expiration:3600000}") long accessExpirationMillis
    ) {
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.accessExpirationMillis = accessExpirationMillis;
    }

    public String createAccessToken(AuthUserDto user) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + accessExpirationMillis);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.userId());
        claims.put("provider", user.provider());
        claims.put("providerUserId", user.providerUserId());
        claims.put("email", user.email());
        claims.put("nickname", user.nickname());
        claims.put("profileImageUrl", user.profileImageUrl());

        return Jwts.builder()
            .subject(user.subject())
            .claims(claims)
            .issuedAt(now)
            .expiration(expiresAt)
            .signWith(signingKey)
            .compact();
    }

    public Optional<Claims> parse(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
            return Optional.of(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public AuthUserDto toUser(Claims claims) {
        return new AuthUserDto(
            stringValue(claims.get("userId")),
            claims.get("provider", String.class),
            claims.get("providerUserId", String.class),
            claims.get("email", String.class),
            claims.get("nickname", String.class),
            claims.get("profileImageUrl", String.class)
        );
    }

    private String stringValue(Object value) {
        return value == null ? null : String.valueOf(value);
    }
}
