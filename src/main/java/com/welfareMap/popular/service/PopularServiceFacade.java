package com.welfareMap.popular.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.welfareMap.popular.domain.PopularServiceStats;
import com.welfareMap.popular.dto.InteractionType;
import com.welfareMap.popular.dto.PopularServiceDto;
import com.welfareMap.popular.repository.PopularServiceStatsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PopularServiceFacade {

    private final PopularServiceStatsRepository repository;

    @Transactional
    public void increment(String cacheKey, InteractionType type) {
        long viewDelta = type == InteractionType.view ? 1L : 0L;
        long clickDelta = type == InteractionType.click ? 1L : 0L;
        long saveDelta = type == InteractionType.save ? 1L : 0L;

        try {
            repository.upsertCounters(cacheKey, viewDelta, clickDelta, saveDelta);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Unknown cache_key: " + cacheKey + " (welfare_services 에 먼저 캐시되어야 함)",
                e
            );
        }
    }

    @Transactional(readOnly = true)
    public List<PopularServiceDto> findTop(int limit) {
        return repository.findTopProjectedByScore(limit).stream()
            .map(PopularServiceDto::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public PopularServiceDto findOne(String cacheKey) {
        PopularServiceStats stats = repository.findById(cacheKey)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No stats for: " + cacheKey));
        return PopularServiceDto.from(stats);
    }
}
