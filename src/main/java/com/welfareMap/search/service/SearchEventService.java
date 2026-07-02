package com.welfareMap.search.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.welfareMap.search.dto.SearchEventRequest;
import com.welfareMap.search.dto.SearchEventResponse;

@Service
public class SearchEventService {

    public SearchEventResponse accept(SearchEventRequest request) {
        return new SearchEventResponse(UUID.randomUUID(), "accepted", Instant.now());
    }
}
