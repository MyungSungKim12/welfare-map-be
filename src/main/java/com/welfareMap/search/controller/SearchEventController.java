package com.welfareMap.search.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.welfareMap.search.dto.SearchEventRequest;
import com.welfareMap.search.dto.SearchEventResponse;
import com.welfareMap.search.service.SearchEventService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchEventController {

    private final SearchEventService service;

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
            "status", "ok",
            "domain", "search"
        );
    }

    @PostMapping("/events")
    public ResponseEntity<SearchEventResponse> accept(@Valid @RequestBody SearchEventRequest request) {
        return ResponseEntity.accepted().body(service.accept(request));
    }
}
