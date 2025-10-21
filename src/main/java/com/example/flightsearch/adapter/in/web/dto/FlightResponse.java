package com.example.flightsearch.adapter.in.web.dto;

import com.example.flightsearch.domain.Flight;

import java.time.LocalDateTime;

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
