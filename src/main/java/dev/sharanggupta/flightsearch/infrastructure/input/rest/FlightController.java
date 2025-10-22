package dev.sharanggupta.flightsearch.infrastructure.input.rest;

import dev.sharanggupta.flightsearch.application.ports.input.SearchFlightsUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Driving adapter (input adapter) for flight search via REST API.
 * This is outside the hexagon - it's part of the infrastructure layer.
 * It CALLS the driving port (SearchFlightsUseCase) to interact with the hexagon.
 */
@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final SearchFlightsUseCase searchFlightsUseCase;

    public FlightController(SearchFlightsUseCase searchFlightsUseCase) {
        this.searchFlightsUseCase = searchFlightsUseCase;
    }

    @GetMapping
    public List<FlightResponse> searchFlights(@RequestParam String origin) {
        return searchFlightsUseCase.searchByOrigin(origin)
                .stream()
                .map(FlightResponse::from)
                .toList();
    }
}
