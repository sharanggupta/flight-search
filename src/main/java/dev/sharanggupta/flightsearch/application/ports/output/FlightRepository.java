package dev.sharanggupta.flightsearch.application.ports.output;

import dev.sharanggupta.flightsearch.domain.Flight;

import java.util.List;

/**
 * Output port (driven port) for flight persistence.
 * This interface defines what the hexagon needs from the outside world.
 * Driven adapters (e.g., JPA implementation) implement this interface.
 * Defined in terms of domain concepts, technology-agnostic.
 */
public interface FlightRepository {
    List<Flight> findByOrigin(String origin);
}
