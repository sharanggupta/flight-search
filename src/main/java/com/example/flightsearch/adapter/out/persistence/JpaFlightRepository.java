package com.example.flightsearch.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaFlightRepository extends JpaRepository<FlightEntity, Long> {
    List<FlightEntity> findByOrigin(String origin);
}
