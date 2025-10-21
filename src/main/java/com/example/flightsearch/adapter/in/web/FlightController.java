package com.example.flightsearch.adapter.in.web;

import com.example.flightsearch.adapter.in.web.dto.FlightResponse;
import com.example.flightsearch.application.service.SearchFlightsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final SearchFlightsService searchFlightsService;

    public FlightController(SearchFlightsService searchFlightsService) {
        this.searchFlightsService = searchFlightsService;
    }

    @GetMapping
    public List<FlightResponse> searchFlights(@RequestParam String origin) {
        return searchFlightsService.searchByOrigin(origin)
                .stream()
                .map(FlightResponse::from)
                .toList();
    }
}
