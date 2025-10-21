package com.example.flightsearch.domain;

import java.time.Duration;
import java.time.LocalDateTime;

public record Flight(
        String flightNumber,
        String origin,
        String destination,
        LocalDateTime departureDateTime,
        Duration duration,
        String airline
) {
}
