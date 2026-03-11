# MEMORY – [PROJECT NAME]

> Claude Code Langzeit-Gedächtnis für dieses Projekt.
> Wird automatisch bei jeder Session geladen (via CLAUDE.md Referenz).
> Hier stehen stabile Erkenntnisse – kein Session-Kontext, keine temporären Notizen.

---

## Projekt-Übersicht

- **Unternehmen:** [COMPANY NAME]
- **Projekt:** [PROJECT NAME]
- **Stack:** Java Spring Boot / Vue.js 3 / Oracle / PostgreSQL
- **Status:** [z.B. In Entwicklung / Produktiv]

---

## Architektur-Entscheidungen

> Hier eingetragene Entscheidungen gelten als verbindlich – nicht ohne Rücksprache ändern.

<!-- Beispiel:
## 2024-03-01 – REST über GraphQL
Root Cause: GraphQL-Komplexität war für unser Team zu hoch.
Entscheidung: Wir nutzen REST mit OpenAPI 3.0 für alle neuen APIs.
-->

---

## Bekannte Probleme & Lösungen

> Wiederkehrende Probleme und ihre bewährten Lösungen.

<!-- Beispiel:
## Oracle Sequence Reset nach Datenbankwechsel
Problem: Sequences in Oracle wurden bei Migration nicht mitgenommen.
Lösung: V099__reset_sequences.sql Flyway-Migration anlegen.
-->

---

## Team-Präferenzen

> Was das Team explizit bevorzugt oder ablehnt.

<!-- Beispiele:
- Lombok @RequiredArgsConstructor bevorzugt (kein @Autowired auf Feldern)
- DTOs immer als Record (Java 17+)
- Kein MapStruct – manuelle Mapper sind transparenter
-->

---

## Wichtige Dateipfade

> Nicht-offensichtliche Dateien die man kennen muss.

<!-- Beispiele:
- Shared Config: src/main/resources/application-shared.yml
- Security Config: src/main/java/com/example/config/SecurityConfig.java
- API Base URL: frontend/src/config/api.ts
-->

---

## Notizen für Claude Code

> Spezifisches Verhalten das Claude Code in diesem Projekt haben soll.

<!-- Beispiele:
- Bei Oracle-Queries immer UPPER_CASE Tabellennamen verwenden
- Tests immer in src/test/java (nie inline)
- Copilot-Sync: Änderungen an CLAUDE.md → copilot-instructions.md angleichen
-->
