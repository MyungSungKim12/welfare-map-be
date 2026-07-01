package com.welfareMap.popular.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.junit.jupiter.api.Test;

class PopularServiceDtoTests {

    private static PopularServiceProjection projection(
        String cacheKey,
        long views, long clicks, long saves,
        BigDecimal score,
        OffsetDateTime updatedAt,
        String title, String summary, String link,
        String region, String target, String category
    ) {
        return new PopularServiceProjection() {
            @Override public String getCacheKey()        { return cacheKey; }
            @Override public long getViewCount()         { return views; }
            @Override public long getClickCount()        { return clicks; }
            @Override public long getSaveCount()         { return saves; }
            @Override public BigDecimal getScore()       { return score; }
            @Override public OffsetDateTime getUpdatedAt(){ return updatedAt; }
            @Override public String getTitle()           { return title; }
            @Override public String getSummary()         { return summary; }
            @Override public String getLink()            { return link; }
            @Override public String getRegion()          { return region; }
            @Override public String getTarget()          { return target; }
            @Override public String getCategory()        { return category; }
        };
    }

    @Test
    void from_projection_mapsAllPopularAndWelfareFields() {
        var updatedAt = OffsetDateTime.parse("2026-07-01T00:00:00Z");
        var p = projection(
            "local:A100", 10L, 3L, 2L, new BigDecimal("29"), updatedAt,
            "청년 월세 지원", "월세 지원 요약", "https://example.com",
            "인천 미추홀구", "청년", "주거"
        );

        var dto = PopularServiceDto.from(p);

        assertEquals("local:A100", dto.cacheKey());
        assertEquals(10L, dto.viewCount());
        assertEquals(3L, dto.clickCount());
        assertEquals(2L, dto.saveCount());
        assertEquals(new BigDecimal("29"), dto.score());
        assertEquals(updatedAt, dto.updatedAt());
        assertEquals("청년 월세 지원", dto.title());
        assertEquals("월세 지원 요약", dto.summary());
        assertEquals("https://example.com", dto.link());
        assertEquals("인천 미추홀구", dto.region());
        assertEquals("청년", dto.target());
        assertEquals("주거", dto.category());
    }

    @Test
    void from_projection_allowsNullWelfareFieldsForOrphanedCounters() {
        var p = projection(
            "national:ORPHAN", 1L, 0L, 0L, BigDecimal.ONE, OffsetDateTime.parse("2026-07-01T00:00:00Z"),
            null, null, null, null, null, null
        );

        var dto = PopularServiceDto.from(p);

        assertEquals("national:ORPHAN", dto.cacheKey());
        assertEquals(1L, dto.viewCount());
        assertNull(dto.title());
        assertNull(dto.summary());
        assertNull(dto.link());
        assertNull(dto.region());
        assertNull(dto.target());
        assertNull(dto.category());
    }
}
