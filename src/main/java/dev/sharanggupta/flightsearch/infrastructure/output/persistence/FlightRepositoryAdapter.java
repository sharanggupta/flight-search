package dev.sharanggupta.flightsearch.infrastructure.output.persistence;

import dev.sharanggupta.flightsearch.application.ports.output.FlightRepository;
import dev.sharanggupta.flightsearch.domain.Flight;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Driven adapter (output adapter) that implements the FlightRepository port using JPA.
 * This is outside the hexagon - it's part of the infrastructure layer.
 * It IMPLEMENTS the driven port interface defined by the hexagon.
 */
@Component
public class FlightRepositoryAdapter implements FlightRepository {

    private final JpaFlightRepository jpaFlightRepository;

    public FlightRepositoryAdapter(JpaFlightRepository jpaFlightRepository) {
        this.jpaFlightRepository = jpaFlightRepository;
    }

    @Override
    public List<Flight> findByOrigin(String origin) {
        return jpaFlightRepository.findByOrigin(origin)
                .stream()
                .map(FlightEntity::toDomain)
                .toList();
    }
}
