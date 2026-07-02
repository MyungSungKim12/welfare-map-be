package com.welfareMap.search.dto;

import java.time.Instant;
import java.util.UUID;

public record SearchEventResponse(
    UUID eventId,
    String status,
    Instant acceptedAt
) {
}
