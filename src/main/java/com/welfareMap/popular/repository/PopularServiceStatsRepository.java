package com.welfareMap.popular.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.welfareMap.popular.domain.PopularServiceStats;

public interface PopularServiceStatsRepository extends JpaRepository<PopularServiceStats, String> {

    @Query(value = """
        SELECT *
        FROM popular_services
        ORDER BY score DESC NULLS LAST, updated_at DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<PopularServiceStats> findTopByScore(@Param("limit") int limit);

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
