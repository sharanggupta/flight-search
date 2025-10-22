package dev.sharanggupta.flightsearch.application.services;

import dev.sharanggupta.flightsearch.application.ports.input.SearchFlightsUseCase;
import dev.sharanggupta.flightsearch.application.ports.output.FlightRepository;
import dev.sharanggupta.flightsearch.domain.Flight;

import java.util.List;

/**
 * Use case implementation for searching flights.
 * This is inside the hexagon - pure business logic, NO framework dependencies.
 * The infrastructure layer will wire this via dependency injection.
 */
public class SearchFlightsService implements SearchFlightsUseCase {

    private final FlightRepository flightRepository;

    public SearchFlightsService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public List<Flight> searchByOrigin(String origin) {
        return flightRepository.findByOrigin(origin);
    }
}
