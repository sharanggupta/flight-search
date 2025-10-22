package dev.sharanggupta.flightsearch.infrastructure.input.rest;

import dev.sharanggupta.flightsearch.domain.Flight;

import java.time.LocalDateTime;

/**
 * DTO for flight HTTP responses.
 * This is part of the infrastructure layer - shapes domain data for REST API.
 */
public record FlightResponse(
        String flightNumber,
        String origin,
        String destination,
        LocalDateTime departureDateTime,
        String duration,
        String airline
) {
    public static FlightResponse from(Flight flight) {
        return new FlightResponse(
                flight.flightNumber(),
                flight.origin(),
                flight.destination(),
                flight.departureDateTime(),
                formatDuration(flight.duration()),
                flight.airline()
        );
    }

    private static String formatDuration(java.time.Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        return String.format("%dh %02dm", hours, minutes);
    }
}
