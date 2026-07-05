package com.welfareMap.user.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.welfareMap.auth.dto.AuthUserDto;
import com.welfareMap.user.domain.UserAccount;
import com.welfareMap.user.domain.UserProfile;
import com.welfareMap.user.dto.UserProfileRequest;
import com.welfareMap.user.dto.UserProfileResponse;
import com.welfareMap.user.repository.UserAccountRepository;
import com.welfareMap.user.repository.UserProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserAccountRepository userAccountRepository;
    private final UserProfileRepository userProfileRepository;

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(AuthUserDto authUser) {
        return userProfileRepository.findById(requireUserId(authUser))
            .map(this::toResponse)
            .orElse(null);
    }

    @Transactional
    public UserProfileResponse saveProfile(AuthUserDto authUser, UserProfileRequest request) {
        UUID userId = requireUserId(authUser);
        UserAccount user = userAccountRepository.findById(userId)
            .orElseThrow(() -> new IllegalStateException("Authenticated user was not found."));

        UserProfile profile = userProfileRepository.findById(userId)
            .orElseGet(() -> new UserProfile(user));

        profile.update(
            parseDate(request.birthDate()),
            request.region(),
            request.gender(),
            request.maritalStatus(),
            request.childrenStatus(),
            toArray(request.lifeStages()),
            toArray(request.interests())
        );

        return toResponse(userProfileRepository.save(profile));
    }

    private UUID requireUserId(AuthUserDto authUser) {
        if (authUser == null || authUser.userId() == null) {
            throw new IllegalArgumentException("Authentication is required.");
        }
        return UUID.fromString(authUser.userId());
    }

    private LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return LocalDate.parse(value);
    }

    private String[] toArray(List<String> values) {
        if (values == null) {
            return new String[0];
        }
        return values.stream()
            .filter(value -> value != null && !value.isBlank())
            .distinct()
            .toArray(String[]::new);
    }

    private UserProfileResponse toResponse(UserProfile profile) {
        return new UserProfileResponse(
            profile.getUserId().toString(),
            profile.getBirthDate() == null ? "" : profile.getBirthDate().toString(),
            profile.getRegion(),
            profile.getGender(),
            profile.getMaritalStatus(),
            profile.getChildrenStatus(),
            Arrays.asList(profile.getLifeStages()),
            Arrays.asList(profile.getInterests()),
            profile.getUpdatedAt()
        );
    }
}
