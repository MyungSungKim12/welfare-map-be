package com.welfareMap.popular.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Spring Data JPA interface projection for popular_services LEFT JOIN welfare_services.
 * Native query aliases must be double-quoted to preserve camelCase (Postgres lowercases unquoted).
 */
public interface PopularServiceProjection {

    String getCacheKey();

    long getViewCount();

    long getClickCount();

    long getSaveCount();

    BigDecimal getScore();

    OffsetDateTime getUpdatedAt();

    String getTitle();

    String getSummary();

    String getLink();

    String getRegion();

    String getTarget();

    String getCategory();
}
