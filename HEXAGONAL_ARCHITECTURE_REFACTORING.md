# Hexagonal Architecture Refactoring Summary

## What Changed

We refactored the project from a **pseudo-hexagonal architecture** to a **true hexagonal architecture** based on the Stack Overflow answer about hexagonal architecture principles.

## Key Understanding: The Hexagon vs Outside

### The Golden Rule
**The hexagon NEVER depends on the outside at compile time.**

- **Inside the Hexagon**: Domain + Application layers (pure business logic, NO Spring dependencies)
- **Outside the Hexagon**: Infrastructure layer (ALL framework code - Spring, JPA, REST, etc.)

## New Package Structure

```
src/main/java/dev/sharanggupta/flightsearch/
├── domain/                           # INSIDE HEXAGON
│   └── Flight.java                  # Pure Java - NO annotations
│
├── application/                      # INSIDE HEXAGON
│   ├── ports/
│   │   ├── input/                   # Driving ports (what hexagon PROVIDES)
│   │   │   └── SearchFlightsUseCase.java
│   │   └── output/                  # Driven ports (what hexagon NEEDS)
│   │       └── FlightRepository.java
│   └── services/                    # Use case implementations (NO @Service!)
│       └── SearchFlightsService.java
│
└── infrastructure/                   # OUTSIDE HEXAGON
    ├── config/                      # Spring wiring
    │   └── ApplicationConfig.java   # @Configuration with @Bean
    ├── input/                       # Driving adapters (call the hexagon)
    │   └── rest/
    │       ├── FlightController.java        # @RestController
    │       └── FlightResponse.java
    └── output/                      # Driven adapters (implement ports)
        └── persistence/
            ├── FlightEntity.java            # @Entity
            ├── JpaFlightRepository.java     # Spring Data JPA
            └── FlightRepositoryAdapter.java # @Component
```

## Critical Concepts Learned

### 1. Naming Confusion
- **Hexagonal Architecture** calls the inside part "the application"
- **DDD** has an "application layer" inside the hexagon
- In this project: **Domain + Application = The Hexagon**

### 2. Ports & Adapters

**Driving Ports (Input Ports):**
- Interfaces defining what the hexagon **OFFERS** to the outside
- Located inside hexagon: `application/ports/input/`
- Implemented by use case classes in `application/services/`

**Driving Adapters (Input Adapters):**
- **DON'T implement** driving ports, they **CALL** them
- Located outside hexagon: `infrastructure/input/`
- Example: REST controller calls `SearchFlightsUseCase.searchByOrigin()`

**Driven Ports (Output Ports):**
- Interfaces defining what the hexagon **NEEDS** from the outside
- Located inside hexagon: `application/ports/output/`
- Defined in terms of domain concepts, technology-agnostic

**Driven Adapters (Output Adapters):**
- **DO implement** driven ports
- Located outside hexagon: `infrastructure/output/`
- Example: `FlightRepositoryAdapter implements FlightRepository`

### 3. Dependency Direction

```
✅ Outside → Inside (allowed)
✅ Infrastructure → Application/Domain (allowed)

❌ Inside → Outside (FORBIDDEN at compile time)
❌ Application/Domain → Infrastructure (FORBIDDEN)
```

### 4. Why Manual @Bean Configuration is Correct

**The Question:** "Won't manual @Bean configuration get out of hand as the project grows?"

**The Answer:** Yes, but it's the **correct approach** for true hexagonal architecture because:

1. **Use case implementations CANNOT have @Service** - that's a framework dependency
2. The hexagon (domain + application) must be **pure Java** with NO Spring annotations
3. Infrastructure layer wires everything at **runtime** using `@Configuration` and `@Bean`
4. This maintains the golden rule: hexagon doesn't depend on infrastructure

**Alternatives considered:**
- ❌ **@Service on use cases** - Violates framework independence
- ⚠️ **Custom @UseCase annotation** - Still a framework dependency (meta-annotated with @Service)
- ✅ **Manual @Bean configuration** - Only correct approach for true hexagonal architecture

The trade-off is intentional: **architectural purity over convenience**.

## Before vs After

### Before (Pseudo-Hexagonal)

```java
// WRONG: Use case with framework annotation
@Service
public class SearchFlightsService implements SearchFlightsUseCase {
    // ...
}

// WRONG: Ports at top level, not inside application
src/main/java/.../ports/input/
src/main/java/.../ports/output/

// WRONG: Adapters not clearly marked as infrastructure
src/main/java/.../adapters/
```

### After (True Hexagonal)

```java
// RIGHT: Use case is pure Java
public class SearchFlightsService implements SearchFlightsUseCase {
    // NO @Service annotation!
}

// RIGHT: Infrastructure wires it
@Configuration
public class ApplicationConfig {
    @Bean
    public SearchFlightsUseCase searchFlightsUseCase(FlightRepository repo) {
        return new SearchFlightsService(repo);
    }
}

// RIGHT: Clear hexagon boundaries
application/ports/input/    # Inside hexagon
application/ports/output/   # Inside hexagon
infrastructure/input/       # Outside hexagon
infrastructure/output/      # Outside hexagon
```

## Test Results

✅ **All tests passing:**
- Unit tests: 1 test (uses `FakeFlightRepository` test double)
- E2E tests: 1 test (TestContainers + PostgreSQL + full Spring Boot)
- **Total: 2 tests, 0 failures, 0 errors**

## References

1. **Stack Overflow Answer**: Hexagonal architecture - Spring Boot
   - Key source for understanding driving vs driven ports/adapters
   - Clarified that @Bean configuration is correct for hexagonal architecture

2. **Hexagonal Architecture Resources**:
   - https://hexagonalarchitecture.org/
   - https://alistaircockburn.com/Articles
   - https://jmgarridopaz.github.io/content/articles.html

## What This Means Going Forward

1. **New use cases**: Add to `application/services/`, wire in `ApplicationConfig`
2. **New ports**: Add interfaces to `application/ports/input|output/`
3. **New adapters**: Add to `infrastructure/input|output/`
4. **Keep hexagon pure**: NO Spring annotations in domain/application layers
5. **Configuration scales**: Each new use case needs a @Bean method (this is intentional!)

## Benefits Achieved

✅ **True framework independence** - Hexagon is pure Java
✅ **Testability** - Can test use cases without Spring
✅ **Clear boundaries** - Explicit hexagon vs infrastructure separation
✅ **Architectural integrity** - Dependency rule enforced at compile time
✅ **Technology agnostic** - Could swap Spring for another DI framework

The cost is more verbose configuration, but this is the **correct trade-off** for maintaining architectural principles.
