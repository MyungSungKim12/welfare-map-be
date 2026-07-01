package com.welfareMap.popular.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.welfareMap.popular.domain.PopularServiceStats;
import com.welfareMap.popular.dto.PopularServiceProjection;

public interface PopularServiceStatsRepository extends JpaRepository<PopularServiceStats, String> {

    @Query(value = """
        SELECT *
        FROM popular_services
        ORDER BY score DESC NULLS LAST, updated_at DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<PopularServiceStats> findTopByScore(@Param("limit") int limit);

    /**
     * popular_services + welfare_services LEFT JOIN projection.
     * welfare_services row 가 정리됐어도 카운터는 유지해서 반환.
     * alias 는 반드시 double-quoted 로 camelCase 보존.
     */
    @Query(value = """
        SELECT
          p.cache_key    AS "cacheKey",
          p.view_count   AS "viewCount",
          p.click_count  AS "clickCount",
          p.save_count   AS "saveCount",
          p.score        AS "score",
          p.updated_at   AS "updatedAt",
          w.title        AS "title",
          w.summary      AS "summary",
          w.link         AS "link",
          w.region       AS "region",
          w.target       AS "target",
          w.category     AS "category"
        FROM popular_services p
        LEFT JOIN welfare_services w ON w.cache_key = p.cache_key
        ORDER BY p.score DESC NULLS LAST, p.updated_at DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<PopularServiceProjection> findTopProjectedByScore(@Param("limit") int limit);

    /**
     * Postgres ON CONFLICT 업서트. 단일 쿼리로 동시성 문제 없이 카운터 증분.
     * cache_key 가 welfare_services 에 없으면 FK 위반으로 DataIntegrityViolationException 발생.
     */
    @Modifying
    @Query(value = """
        INSERT INTO popular_services (cache_key, view_count, click_count, save_count, updated_at)
        VALUES (:cacheKey, :viewDelta, :clickDelta, :saveDelta, now())
        ON CONFLICT (cache_key) DO UPDATE SET
            view_count  = popular_services.view_count  + :viewDelta,
            click_count = popular_services.click_count + :clickDelta,
            save_count  = popular_services.save_count  + :saveDelta,
            updated_at  = now()
        """, nativeQuery = true)
    int upsertCounters(
        @Param("cacheKey") String cacheKey,
        @Param("viewDelta") long viewDelta,
        @Param("clickDelta") long clickDelta,
        @Param("saveDelta") long saveDelta
    );
}
