package com.welfareMap.search.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class SearchEventResponseTests {

    @Test
    void createsAcceptedResponse() {
        UUID eventId = UUID.randomUUID();
        Instant acceptedAt = Instant.now();

        var response = new SearchEventResponse(eventId, "accepted", acceptedAt);

        assertThat(response.eventId()).isEqualTo(eventId);
        assertThat(response.status()).isEqualTo("accepted");
        assertThat(response.acceptedAt()).isEqualTo(acceptedAt);
    }
}
