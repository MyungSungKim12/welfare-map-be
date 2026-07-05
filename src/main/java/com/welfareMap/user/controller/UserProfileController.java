package com.welfareMap.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.welfareMap.auth.dto.AuthUserDto;
import com.welfareMap.user.dto.UserProfileRequest;
import com.welfareMap.user.dto.UserProfileResponse;
import com.welfareMap.user.service.UserProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> me(@AuthenticationPrincipal AuthUserDto user) {
        if (user == null || user.userId() == null) {
            return ResponseEntity.status(401).build();
        }

        UserProfileResponse profile = userProfileService.getProfile(user);
        if (profile == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> save(
        @AuthenticationPrincipal AuthUserDto user,
        @RequestBody UserProfileRequest request
    ) {
        if (user == null || user.userId() == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(userProfileService.saveProfile(user, request));
    }
}
