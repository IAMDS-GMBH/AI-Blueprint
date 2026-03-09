---
applyTo: "**/*.java"
---

# Java Spring Standards

- Java 17+, keine deprecated APIs
- Services immer via Interface abstrahieren (`UserService` → `UserServiceImpl`)
- DTOs für API-Layer – Entities nie direkt in Response-Body exposen
- Exception Handling ausschließlich via `@ControllerAdvice`
- Logging: SLF4J + Logback – kein `System.out.println`
- Keine Magic Numbers, keine hardcodierten Credentials
- Methoden max. 30 Zeilen – danach aufteilen
- `@PreAuthorize` für alle gesicherten Endpoints
- Input-Validierung via `@Valid` + Bean Validation
- SQL-Queries: PreparedStatements / JPA – keine String-Konkatenation
