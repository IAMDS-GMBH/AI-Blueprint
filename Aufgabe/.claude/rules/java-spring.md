---
description: Regeln fuer Java Spring Boot Entwicklung – automatisch aktiv bei .java Dateien
globs: ["**/*.java"]
alwaysApply: false
---

# Java Spring Boot – Pflichtregeln

## Architektur
- Services **immer** via Interface abstrahieren (`UserService` → `UserServiceImpl`)
- DTOs fuer den API-Layer – Entities niemals direkt exposen
- `@RequiredArgsConstructor` statt `@Autowired` (Lombok)
- Exception Handling nur via `@RestControllerAdvice` – nie try/catch fuer Business-Logik

## API-Konventionen
- GET `/api/v1/resources` → Liste (200)
- POST `/api/v1/resources` → Erstellen (201 Created)
- PUT `/api/v1/resources/{id}` → Vollstaendiges Update (200)
- PATCH `/api/v1/resources/{id}` → Partial Update (200)
- DELETE `/api/v1/resources/{id}` → Loeschen (204 No Content)

## Sicherheit
- Alle Endpoints mit `@PreAuthorize` absichern
- Input-Validierung mit `@Valid` + Bean Validation Annotations
- Keine hardcodierten Credentials – immer `@Value` oder Secrets Manager
- Kein `System.out.println` – nur `log.info/warn/error` (SLF4J)

## Verbote
- Kein `@Autowired` auf Feldern (Constructor Injection via Lombok)
- Keine Logik in Entities oder DTOs
- Kein `Optional.get()` ohne `.isPresent()` – immer `.orElseThrow()`
- Keine Magic Numbers – benannte Konstanten verwenden
