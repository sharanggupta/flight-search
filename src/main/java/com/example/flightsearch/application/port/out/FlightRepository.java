package com.example.flightsearch.application.port.out;

import com.example.flightsearch.domain.Flight;

import java.util.List;

public interface FlightRepository {
    List<Flight> findByOrigin(String origin);
}
