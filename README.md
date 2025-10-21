# Flight Search API

A Spring Boot REST API for searching flights, built using Test-Driven Development (TDD) and Hexagonal Architecture principles.

## Technologies

- **Java 21** (LTS)
- **Spring Boot 3.4.0**
- **PostgreSQL** (with TestContainers for testing)
- **Maven**
- **JUnit 5**
- **Spring WebFlux** (WebTestClient for E2E tests)

## Architecture

This project follows **Hexagonal Architecture** (Ports and Adapters pattern):

```
src/main/java/com/example/flightsearch/
├── domain/                    # Core business logic (no framework dependencies)
│   └── Flight.java
├── application/               # Use cases and ports
│   ├── port/
│   │   ├── in/               # Input ports (use case interfaces)
│   │   └── out/              # Output ports (repository interfaces)
│   │       └── FlightRepository.java
│   └── service/              # Application services
│       └── SearchFlightsService.java
└── adapter/                  # Framework-specific implementations
    ├── in/
    │   └── web/             # REST controllers
    └── out/
        └── persistence/     # JPA repositories
```

## Development Approach

### Double-Loop TDD

This project strictly follows **Double-Loop TDD**:

1. **Outer Loop (E2E Test)**: Write a failing end-to-end test
2. **Inner Loop (Unit Tests)**: Write unit tests and implement with simplest code (including hardcoded values)
3. **Verify**: Ensure E2E test passes
4. **Commit**: Commit when all tests pass

### Testing Strategy

- **E2E Tests**: Full application context with TestContainers (PostgreSQL)
- **Unit Tests**: No mocking - using simple test doubles/fakes
- **Clean Code**: Following Bob Martin's principles

## Running the Tests

### Unit Tests Only
```bash
./mvnw test -Dtest="*ServiceTest"
```

### E2E Tests Only
```bash
./mvnw test -Dtest="E2E*"
```

### All Tests
```bash
./mvnw test
```

## Running the Application

```bash
./mvnw spring-boot:run
```

## Requirements

- Java 21
- Docker (for TestContainers)
- Maven 3.6+

## API Endpoints

### Search Flights by Origin
```
GET /api/flights?origin={airportCode}
```

Example:
```bash
curl http://localhost:8080/api/flights?origin=JFK
```

Response:
```json
[
  {
    "flightNumber": "AA100",
    "origin": "JFK",
    "destination": "LAX",
    "departureDateTime": "2025-10-22T10:30:00",
    "duration": "5h 30m",
    "airline": "American Airlines"
  }
]
```

## Project Status

✅ Project setup with Spring Boot 3.4.0 and Java 21
✅ E2E test infrastructure with TestContainers
✅ Domain model (Flight)
✅ Application service layer with TDD
⏳ REST API implementation (in progress)
⏳ Database persistence layer

## Contributing

This project follows strict TDD practices. All features must:
1. Start with a failing E2E test
2. Be driven by unit tests
3. Pass all tests before committing

See `.claude.md` for detailed development guidelines.
