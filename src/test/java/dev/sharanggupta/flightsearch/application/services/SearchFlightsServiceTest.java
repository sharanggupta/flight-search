package dev.sharanggupta.flightsearch.application.services;

import dev.sharanggupta.flightsearch.application.ports.output.FlightRepository;
import dev.sharanggupta.flightsearch.domain.Flight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SearchFlightsServiceTest {

    private FlightRepository flightRepository;
    private SearchFlightsService searchFlightsService;

    @BeforeEach
    void setUp() {
        flightRepository = new FakeFlightRepository();
        searchFlightsService = new SearchFlightsService(flightRepository);
    }

    @Test
    void shouldReturnFlightsFromOriginAirport() {
        // Given
        String origin = "JFK";

        // When
        List<Flight> result = searchFlightsService.searchByOrigin(origin);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).flightNumber()).isEqualTo("AA100");
        assertThat(result.get(0).origin()).isEqualTo("JFK");
        assertThat(result.get(0).destination()).isEqualTo("LAX");
        assertThat(result.get(0).airline()).isEqualTo("American Airlines");
    }

    /**
     * Test double - no mocking framework needed.
     * This is a simple fake implementation for testing.
     */
    private static class FakeFlightRepository implements FlightRepository {
        @Override
        public List<Flight> findByOrigin(String origin) {
            if ("JFK".equals(origin)) {
                return List.of(new Flight(
                        "AA100",
                        "JFK",
                        "LAX",
                        LocalDateTime.of(2025, 10, 22, 10, 30),
                        Duration.ofHours(5).plusMinutes(30),
                        "American Airlines"
                ));
            }
            return new ArrayList<>();
        }
    }
}
