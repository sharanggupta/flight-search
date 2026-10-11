# Flight Search API

A small Spring Boot REST API that lists flights leaving an airport, written test-first with a hexagonal structure.

It is a learning project. It demonstrates one thing: a hexagon (domain and application code) with no Spring or JPA in it, wired to a REST adapter and a Postgres adapter from the outside. The only feature is searching by origin airport.

## Quick start

Prerequisites: Java 21, Docker, and Docker Compose (the scripts call `docker-compose`). The Maven wrapper is included.

```bash
./scripts/start-local.sh
```

This starts Postgres in Docker on port 5433 (seeded with 10 flights from `docker/db/init-db.sql`) and runs the app on port 8080 with the `local` profile. In another terminal:

```bash
curl "http://localhost:8080/api/flights?origin=JFK"
```

```json
[
  {"flightNumber":"AA100","origin":"JFK","destination":"LAX","departureDateTime":"2025-10-22T10:30:00","duration":"5h 30m","airline":"American Airlines"},
  {"flightNumber":"AA101","origin":"JFK","destination":"SFO","departureDateTime":"2025-10-22T14:15:00","duration":"6h 00m","airline":"American Airlines"},
  {"flightNumber":"BA500","origin":"JFK","destination":"LHR","departureDateTime":"2025-10-24T18:00:00","duration":"7h 00m","airline":"British Airways"},
  {"flightNumber":"BA501","origin":"JFK","destination":"CDG","departureDateTime":"2025-10-24T20:30:00","duration":"7h 30m","airline":"British Airways"}
]
```

The sample flights have origins JFK, LAX, SFO and ORD, with departure dates in October 2025.

To stop: press Ctrl+C in the app terminal, then run `./scripts/stop.sh`. For a fresh database, run `docker-compose -f docker/docker-compose.yml down -v` instead.

`./scripts/start-docker.sh` builds the app image and runs app and database together in Docker (Ctrl+C is not needed: it runs detached, and `./scripts/stop.sh` stops it).

## The API

`GET /api/flights?origin={code}` returns the flights with that origin.

- The match is exact and case-sensitive: `origin=jfk` returns `[]`.
- An origin with no flights returns `[]`.
- A missing `origin` returns 400.

## Structure

The rule: the hexagon never depends on the outside at compile time. `domain` and `application` import neither Spring nor JPA, and the use case is plain Java that the config class wires as a `@Bean`.

```
src/main/java/dev/sharanggupta/flightsearch/
├── domain/               Flight (a record)
├── application/
│   ├── ports/input/      SearchFlightsUseCase     (called by the REST adapter)
│   ├── ports/output/     FlightRepository         (implemented by the JPA adapter)
│   └── services/         SearchFlightsService     (the use case, no annotations)
└── infrastructure/
    ├── config/           ApplicationConfig        (wires the use case)
    ├── input/rest/       FlightController, FlightResponse
    └── output/persistence/  FlightEntity, JpaFlightRepository, FlightRepositoryAdapter
```

At runtime a request goes controller, then use case, then the `FlightRepository` port, then the adapter and Postgres. At compile time the adapter depends on the port, not the other way round.

The rule is kept by convention in a single Maven module. Nothing in the build fails if someone adds a Spring import to `domain`. (The sister project [Orderly](https://github.com/sharanggupta/orderly) enforces it with separate modules.)

Diagrams are in `docs/diagrams/` (PlantUML sources and PNGs). The reasoning behind the structure is in [HEXAGONAL_ARCHITECTURE_REFACTORING.md](HEXAGONAL_ARCHITECTURE_REFACTORING.md) and [REFACTORING_SUMMARY.md](REFACTORING_SUMMARY.md).

## Tests

```bash
./mvnw test -Dtest='!E2E*'   # unit tests: no Spring, no Docker
./mvnw test -Dtest='E2E*'    # end-to-end: Testcontainers starts a real Postgres, so Docker must be running
./mvnw test                  # both
```

- The unit test checks the use case against a hand-written fake `FlightRepository`. No mocking framework.
- The end-to-end test starts the whole application against a Testcontainers Postgres and calls the REST endpoint.

There is one test of each kind: this is a thin vertical slice, not broad coverage.

GitHub Actions (`.github/workflows/ci.yml`) runs the unit tests, then the end-to-end tests, then builds the jar, on every push and pull request to `main`.

## Stack

Java 21, Spring Boot 3.4.0, Spring Data JPA, PostgreSQL, Maven, JUnit 5, Testcontainers.

## Limitations

- Search by origin only; no destination, date or airline filters, no booking, no authentication.
- No LICENSE file in the repository.
- Origin matching is case-sensitive and there is no validation of airport codes.
- The `local` profile validates the schema that `init-db.sql` created, while the default profile uses `ddl-auto: create-drop`, so running the app without the `local` profile against an existing database will drop its tables.
- Checked by running: both Maven test commands, `start-local.sh`, `start-docker.sh` (image builds, the API answers on port 8080) and the API calls above.
