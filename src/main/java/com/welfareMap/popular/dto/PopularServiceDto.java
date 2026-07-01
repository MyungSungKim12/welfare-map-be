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
    OffsetDateTime updatedAt,
    String title,
    String summary,
    String link,
    String region,
    String target,
    String category
) {
    /** Popular 카운터만 있는 경우 (welfare_services JOIN 없이 단건 조회 등) */
    public static PopularServiceDto from(PopularServiceStats e) {
        return new PopularServiceDto(
            e.getCacheKey(),
            e.getViewCount(),
            e.getClickCount(),
            e.getSaveCount(),
            e.getScore(),
            e.getUpdatedAt(),
            null, null, null, null, null, null
        );
    }

    /** popular_services + welfare_services JOIN projection */
    public static PopularServiceDto from(PopularServiceProjection p) {
        return new PopularServiceDto(
            p.getCacheKey(),
            p.getViewCount(),
            p.getClickCount(),
            p.getSaveCount(),
            p.getScore(),
            p.getUpdatedAt(),
            p.getTitle(),
            p.getSummary(),
            p.getLink(),
            p.getRegion(),
            p.getTarget(),
            p.getCategory()
        );
    }
}
