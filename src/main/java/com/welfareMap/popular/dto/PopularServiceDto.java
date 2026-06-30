package com.welfareMap.popular.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.welfareMap.popular.domain.PopularServiceStats;

public record PopularServiceDto(
    String cacheKey,
    long viewCount,
    long clickCount,
    long saveCount,
    BigDecimal score,
    OffsetDateTime updatedAt
) {
    public static PopularServiceDto from(PopularServiceStats e) {
        return new PopularServiceDto(
            e.getCacheKey(),
            e.getViewCount(),
            e.getClickCount(),
            e.getSaveCount(),
            e.getScore(),
            e.getUpdatedAt()
        );
    }
}
