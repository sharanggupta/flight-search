package dev.sharanggupta.flightsearch.application.ports.input;

import dev.sharanggupta.flightsearch.domain.Flight;

import java.util.List;

/**
 * Input port (driving port) for searching flights.
 * This defines the use case interface that the hexagon provides to the outside.
 * Driving adapters (e.g., REST controller) call methods on this interface.
 */
public interface SearchFlightsUseCase {
    /**
     * Search for flights originating from a specific airport.
     *
     * @param origin the airport code (e.g., "JFK", "LAX")
     * @return list of flights from that origin
     */
    List<Flight> searchByOrigin(String origin);
}
