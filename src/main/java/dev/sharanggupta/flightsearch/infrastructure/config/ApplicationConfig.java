package dev.sharanggupta.flightsearch.infrastructure.config;

import dev.sharanggupta.flightsearch.application.ports.input.SearchFlightsUseCase;
import dev.sharanggupta.flightsearch.application.ports.output.FlightRepository;
import dev.sharanggupta.flightsearch.application.services.SearchFlightsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Infrastructure layer configuration.
 * This is where Spring (the framework, outside the hexagon) wires everything together.
 *
 * The hexagon (domain + application) is pure Java with no Spring dependencies.
 * This configuration class injects driven adapters into use cases at runtime.
 */
@Configuration
public class ApplicationConfig {

    /**
     * Creates the SearchFlightsUseCase bean.
     * Driving adapters (REST controller) depend on this interface, not the implementation.
     *
     * @param flightRepository The driven adapter implementation (injected by Spring)
     * @return The use case implementation
     */
    @Bean
    public SearchFlightsUseCase searchFlightsUseCase(FlightRepository flightRepository) {
        return new SearchFlightsService(flightRepository);
    }
}
