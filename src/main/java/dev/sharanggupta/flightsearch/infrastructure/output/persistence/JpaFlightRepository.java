package dev.sharanggupta.flightsearch.infrastructure.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository interface.
 * This is framework-specific infrastructure code.
 */
public interface JpaFlightRepository extends JpaRepository<FlightEntity, Long> {
    List<FlightEntity> findByOrigin(String origin);
}
