# Flight Search API

[![CI](https://github.com/sharanggupta/flight-search/actions/workflows/ci.yml/badge.svg)](https://github.com/sharanggupta/flight-search/actions/workflows/ci.yml)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

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

### Unit Tests Only (all tests except E2E)
```bash
./mvnw test -Dtest="!E2E*"
```

### E2E Tests Only
```bash
./mvnw test -Dtest="E2E*"
```

### All Tests
```bash
./mvnw test
```

## Continuous Integration

The project uses GitHub Actions for automated testing on every push:

1. **Unit Tests** - Fast, isolated tests (no Docker required)
2. **E2E Tests** - Full integration tests with TestContainers
3. **Build** - Creates JAR artifact

Test results are automatically published and visible in PR checks. View the [Actions tab](https://github.com/sharanggupta/flight-search/actions) for detailed test reports.

## Running the Application Locally

### Prerequisites
- Java 21
- Docker & Docker Compose
- Maven 3.6+

**Note:** The Docker setup uses port **5433** for PostgreSQL (not 5432) to avoid conflicts with local PostgreSQL installations.

### Option 1: Local Development (Recommended for Development)

App runs locally with hot reload, database in Docker:

```bash
# Start PostgreSQL and run app locally
./scripts/start-local.sh
```

This automatically:
- ✅ Starts PostgreSQL container
- ✅ Initializes database with 10 sample flights
- ✅ Runs Spring Boot app with `local` profile
- ✅ Enables debug logging and SQL output

### Option 2: Full Docker Stack (Production-like)

Everything runs in Docker containers:

```bash
# Build and start all services
./scripts/start-docker.sh
```

This automatically:
- ✅ Builds application Docker image
- ✅ Starts PostgreSQL and app containers
- ✅ Waits for services to be healthy
- ✅ Verifies API is responding

### Option 3: Manual Control

```bash
# Start only PostgreSQL
docker-compose up -d postgres

# Run application with specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# Or run full stack
docker-compose up --build
```

### Stopping Services

```bash
./scripts/stop.sh

# Or manually
docker-compose down

# Remove database volume (fresh start)
docker-compose down -v
```

## API Endpoints

### Search Flights by Origin
```http
GET /api/flights?origin={airportCode}
```

**Example requests:**
```bash
# Flights from New York JFK
curl http://localhost:8080/api/flights?origin=JFK

# Flights from Los Angeles
curl http://localhost:8080/api/flights?origin=LAX

# Flights from San Francisco
curl http://localhost:8080/api/flights?origin=SFO
```

**Example response:**
```json
[
  {
    "flightNumber": "AA100",
    "origin": "JFK",
    "destination": "LAX",
    "departureDateTime": "2025-10-22T10:30:00",
    "duration": "5h 30m",
    "airline": "American Airlines"
  },
  {
    "flightNumber": "AA101",
    "origin": "JFK",
    "destination": "SFO",
    "departureDateTime": "2025-10-22T14:15:00",
    "duration": "6h 00m",
    "airline": "American Airlines"
  }
]
```

### Sample Data

The database is initialized with 10 sample flights covering routes between:
- **JFK** (New York) ↔ LAX, SFO, LHR, CDG
- **LAX** (Los Angeles) ↔ SFO, ORD
- **SFO** (San Francisco) ↔ JFK, ATL
- **ORD** (Chicago) → DEN, LAS

Airlines: American Airlines, United Airlines, Delta Airlines, Southwest Airlines, British Airways

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
