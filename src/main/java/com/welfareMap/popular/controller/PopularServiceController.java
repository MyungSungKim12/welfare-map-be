package com.welfareMap.popular.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.welfareMap.popular.dto.InteractionType;
import com.welfareMap.popular.dto.PopularServiceDto;
import com.welfareMap.popular.service.PopularServiceFacade;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/popular")
@RequiredArgsConstructor
public class PopularServiceController {

    private static final int DEFAULT_LIMIT = 10;
    private static final int MAX_LIMIT = 50;

    private final PopularServiceFacade facade;

    @GetMapping("/top")
    public List<PopularServiceDto> getTop(
        @RequestParam(defaultValue = "" + DEFAULT_LIMIT)
        @Min(1) @Max(MAX_LIMIT) int limit
    ) {
        return facade.findTop(limit);
    }

    @GetMapping("/{cacheKey}")
    public PopularServiceDto getOne(@PathVariable String cacheKey) {
        return facade.findOne(cacheKey);
    }

    @PostMapping("/{cacheKey}/{type}")
    public ResponseEntity<Void> increment(
        @PathVariable String cacheKey,
        @PathVariable InteractionType type
    ) {
        facade.increment(cacheKey, type);
        return ResponseEntity.noContent().build();
    }
}
