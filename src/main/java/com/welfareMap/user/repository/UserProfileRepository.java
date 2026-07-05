package com.welfareMap.user.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.welfareMap.user.domain.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
}
