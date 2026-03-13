# Plan: Pseudocode-to-Java Agent
Datum: 2026-03-13
Status: ABGESCHLOSSEN

## Ziel
Einen neuen Agent `.claude/agents/pseudocode-java-agent.md` erstellen, der Pseudo-Code (insbesondere den Output des COBOL-Pseudocode-Agents) in sauberen Java-Code uebersetzt. Der Agent erbt die Coding-Standards und das Verhalten des Dev-Agents, ergaenzt um spezifische Regeln fuer die Pseudo-Code → Java Transformation.

## Schritte
- [x] Schritt 1: Agent-Datei `pseudocode-java-agent.md` erstellen – `.claude/agents/`
- [x] Schritt 2: Agent gegen Pseudo-Code von `inventory.cbl` testen – 9 Java-Dateien, 10 Tests, alle Standards OK
- [x] Schritt 3: Agent gegen Pseudo-Code von `payroll.cbl` testen – 7 Java-Dateien, 10 Tests, alle Standards OK
- [x] Schritt 4: Verifikation – Spring-Konventionen, BigDecimal, Records, Exceptions statt Error-Codes ✓

## Design-Entscheidungen

### Pseudo-Code → Java Mapping
| Pseudo-Code | Java |
|---|---|
| Datenstruktur (Felder + Typen) | Record (immutable DTO) oder Class mit Lombok @Data |
| Benannte Werte (OK="OK", FEHLER="ER") | Enum mit String-Werten |
| FUNKTION X | Methode in Service-Interface + Impl |
| WENN ... SONST ... | if/else |
| PRUEFE: FALL ... → ... | switch-Expression (Java 17+) |
| FUER I = 1 BIS N | for-Loop oder IntStream |
| SOLANGE NICHT ... | while-Loop |
| X = A + B | Direkte Zuweisung |
| Dezimalzahl | BigDecimal (fuer Geldbetraege) |
| Ganzzahl (n Stellen) | int oder long (abhaengig von n) |
| Text (n Zeichen) | String |

### Architektur-Regeln (aus dev-agent + java-spring.md)
- Services via Interface abstrahieren
- Constructor Injection via Lombok @RequiredArgsConstructor
- DTOs/Records fuer Datenstrukturen, keine Logik darin
- Keine Magic Numbers → Konstanten oder Enum
- Max 30 Zeilen pro Methode
- SLF4J Logging, kein System.out
- JUnit 5 Tests mitliefern

## Risiken
- **Pseudo-Code Qualitaet variiert**: Nicht jeder Pseudo-Code kommt vom COBOL-Agent. Gegenmassnahme: Agent soll auch mit freigeschriebenem Pseudo-Code umgehen koennen
- **Geldbetraege als double**: Gegenmassnahme: Explizite Regel → BigDecimal fuer alle Waehrungen/Betraege

## Nicht im Scope
- Spring Boot Projekt-Setup (pom.xml, Application.java) – nur die Java-Klassen selbst
- Frontend-Code
- Datenbankschicht (JPA Entities, Repositories) – nur Service-Layer
- Aenderungen am bestehenden Dev-Agent oder COBOL-Pseudocode-Agent

## Freigabe
[ ] Plan freigegeben von: ___________
