package com.example.flightsearch.application.service;

import com.example.flightsearch.application.port.out.FlightRepository;
import com.example.flightsearch.domain.Flight;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SearchFlightsService {

    private final FlightRepository flightRepository;

    public SearchFlightsService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> searchByOrigin(String origin) {
        return flightRepository.findByOrigin(origin);
    }
}
