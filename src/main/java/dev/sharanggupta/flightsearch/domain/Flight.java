package dev.sharanggupta.flightsearch.domain;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Domain entity representing a flight.
 * This is a pure domain object with no framework dependencies.
 */
public record Flight(
        String flightNumber,
        String origin,
        String destination,
        LocalDateTime departureDateTime,
        Duration duration,
        String airline
) {
}
