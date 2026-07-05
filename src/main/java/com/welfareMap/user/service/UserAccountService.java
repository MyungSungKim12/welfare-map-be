package com.welfareMap.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.welfareMap.auth.dto.AuthUserDto;
import com.welfareMap.user.domain.UserAccount;
import com.welfareMap.user.repository.UserAccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Transactional
    public AuthUserDto upsertOAuthUser(AuthUserDto authUser) {
        UserAccount user = userAccountRepository
            .findByProviderAndProviderId(authUser.provider(), authUser.providerUserId())
            .orElseGet(() -> new UserAccount(authUser.provider(), authUser.providerUserId()));

        user.updateOAuthProfile(authUser.email(), authUser.nickname(), authUser.profileImageUrl());
        UserAccount saved = userAccountRepository.save(user);
        return authUser.withUserId(saved.getId().toString());
    }
}
