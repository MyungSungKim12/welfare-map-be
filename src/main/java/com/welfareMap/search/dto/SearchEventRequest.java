package com.welfareMap.search.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SearchEventRequest(
    @NotBlank
    @Size(max = 500)
    String query,

    @Size(max = 80)
    String region,

    @Size(max = 40)
    String source,

    @Min(0)
    @Max(1000)
    int resultCount,

    @Size(max = 20)
    String cacheStatus,

    @Min(0)
    long latencyMs
) {
}
