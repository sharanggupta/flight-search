package com.example.flightsearch.adapter.out.persistence;

import com.example.flightsearch.application.port.out.FlightRepository;
import com.example.flightsearch.domain.Flight;
import org.springframework.stereotype.Component;

import java.util.List;

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
