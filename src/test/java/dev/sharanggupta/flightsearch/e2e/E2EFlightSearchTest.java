package dev.sharanggupta.flightsearch.e2e;

import dev.sharanggupta.flightsearch.infrastructure.output.persistence.FlightEntity;
import dev.sharanggupta.flightsearch.infrastructure.output.persistence.JpaFlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
class E2EFlightSearchTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("flightdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JpaFlightRepository jpaFlightRepository;

    @BeforeEach
    void setUp() {
        jpaFlightRepository.deleteAll();

        // Seed test data
        jpaFlightRepository.save(new FlightEntity(
                null,
                "AA100",
                "JFK",
                "LAX",
                LocalDateTime.of(2025, 10, 22, 10, 30),
                330L, // 5h 30m in minutes
                "American Airlines"
        ));

        jpaFlightRepository.save(new FlightEntity(
                null,
                "UA200",
                "LAX",
                "SFO",
                LocalDateTime.of(2025, 10, 22, 14, 0),
                90L, // 1h 30m in minutes
                "United Airlines"
        ));
    }

    @Test
    void shouldSearchFlightsByOriginAirport() {
        // When: searching for flights from JFK airport
        webTestClient.get()
                .uri("/api/flights?origin=JFK")
                .exchange()
                // Then: should return 200 OK
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBody()
                // And: should return a list with one flight from JFK
                .jsonPath("$.length()").value(equalTo(1))
                .jsonPath("$[0].flightNumber").value(equalTo("AA100"))
                .jsonPath("$[0].origin").value(equalTo("JFK"))
                .jsonPath("$[0].destination").value(equalTo("LAX"))
                .jsonPath("$[0].departureDateTime").value(equalTo("2025-10-22T10:30:00"))
                .jsonPath("$[0].duration").value(equalTo("5h 30m"))
                .jsonPath("$[0].airline").value(equalTo("American Airlines"));
    }
}
