package com.welfareMap.user.dto;

import java.util.List;

public record UserProfileRequest(
    String birthDate,
    String region,
    String gender,
    String maritalStatus,
    String childrenStatus,
    List<String> lifeStages,
    List<String> interests
) {
}
