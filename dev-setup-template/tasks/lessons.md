# Lessons Learned

> Nach jeder Korrektur/Fehler sofort aktualisieren. Nie denselben Fehler zweimal.
> KI liest diese Datei bei Session-Start.

## Format
```
### [DATUM] – [Kurztitel]
**Problem:** [Was schiefging]
**Ursache:** [Root Cause]
**Regel:** [Konkrete Regel fuer die Zukunft]
```

## Lessons

### BEISPIEL – Lombok-Version inkompatibel mit Java 21+
**Problem:** `mvn compile` schlug fehl mit Lombok-Fehler
**Ursache:** Spring Boot Default-Lombok zu alt fuer Java 21+
**Regel:** Bei Java 21+ Lombok-Version explizit auf 1.18.44+ pinnen

### BEISPIEL – Swarm ohne Tests
**Problem:** Swarm hatte nur Dev + Review, keine Tests
**Ursache:** Test Agent nicht im Plan vorgesehen
**Regel:** Swarm-Reihenfolge: Dev → Test → Review. Tests sind Pflicht.
