package com.welfareMap.popular.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Supabase public.popular_services 매핑.
 * score 와 updated_at 은 DB 가 채워주므로 insertable/updatable = false.
 */
@Entity
@Table(name = "popular_services")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PopularServiceStats {

    @Id
    @Column(name = "cache_key", length = 512, nullable = false)
    private String cacheKey;

    @Column(name = "view_count", nullable = false)
    private long viewCount;

    @Column(name = "click_count", nullable = false)
    private long clickCount;

    @Column(name = "save_count", nullable = false)
    private long saveCount;

    @Column(name = "score", insertable = false, updatable = false)
    private BigDecimal score;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;
}
