package com.example.flightsearch.adapter.out.persistence;

import com.example.flightsearch.domain.Flight;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String flightNumber;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private LocalDateTime departureDateTime;

    @Column(nullable = false)
    private Long durationMinutes;

    @Column(nullable = false)
    private String airline;

    public Flight toDomain() {
        return new Flight(
                flightNumber,
                origin,
                destination,
                departureDateTime,
                Duration.ofMinutes(durationMinutes),
                airline
        );
    }

    public static FlightEntity fromDomain(Flight flight) {
        return new FlightEntity(
                null,
                flight.flightNumber(),
                flight.origin(),
                flight.destination(),
                flight.departureDateTime(),
                flight.duration().toMinutes(),
                flight.airline()
        );
    }
}
