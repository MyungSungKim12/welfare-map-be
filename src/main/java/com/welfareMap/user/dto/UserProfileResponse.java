package com.welfareMap.user.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record UserProfileResponse(
    String userId,
    String birthDate,
    String region,
    String gender,
    String maritalStatus,
    String childrenStatus,
    List<String> lifeStages,
    List<String> interests,
    OffsetDateTime updatedAt
) {
}
