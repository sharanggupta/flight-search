package com.example.flightsearch.e2e;

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

    @Test
    void shouldSearchFlightsByOriginAirport() {
        // Given: flights exist in the system
        // (We'll seed data in future iterations)

        // When: searching for flights from JFK airport
        webTestClient.get()
                .uri("/api/flights?origin=JFK")
                .exchange()
                // Then: should return 200 OK
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBody()
                // And: should return a list of flights
                .jsonPath("$").isArray()
                .jsonPath("$[0].flightNumber").value(notNullValue())
                .jsonPath("$[0].origin").value(equalTo("JFK"))
                .jsonPath("$[0].destination").value(notNullValue())
                .jsonPath("$[0].departureDateTime").value(notNullValue())
                .jsonPath("$[0].duration").value(notNullValue())
                .jsonPath("$[0].airline").value(notNullValue());
    }
}
