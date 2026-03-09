# GitHub Copilot – [COMPANY NAME] AI Workspace

> Diese Datei ist das Copilot-Äquivalent zu CLAUDE.md.
> Sie wird bei jeder Copilot Agent-Session automatisch geladen.
> Projekt-spezifische Anpassungen: Diese Datei im jeweiligen Projekt-Repository kopieren und editieren.

---

## Unternehmen & Kontext

**Unternehmen:** [COMPANY NAME]
**Branche:** [BRANCHE]
**Ziel:** Maximale Automatisierung repetitiver Aufgaben über alle Teams hinweg.

---

## Tech Stack

> ANPASSEN: Diesen Abschnitt pro Projekt anpassen.

```
# Backend
Framework:    Java Spring Boot 3.x
Build:        Maven / Gradle
API-Stil:     REST (OpenAPI 3.0)
Auth:         Spring Security + JWT

# Frontend
Framework:    Vue.js 3 (Composition API)
Build:        Vite
State:        Pinia
HTTP:         Axios

# Datenbanken
Primary DB:   Oracle Database 19c
Secondary DB: PostgreSQL 15
ORM/Access:   Hibernate / JPA (Oracle), JDBC / Flyway (PostgreSQL)
Migrations:   Flyway

# Infrastruktur
Container:    Docker / Docker Compose
CI/CD:        [PLACEHOLDER – z.B. GitHub Actions, GitLab CI]
Hosting:      [PLACEHOLDER – z.B. AWS, Azure, On-Premise]

# Testing
Backend:      JUnit 5, Mockito, Testcontainers
Frontend:     Vitest, Vue Test Utils, Playwright (E2E)
```

---

## Code-Konventionen

### Java / Spring
- Java 17+, keine deprecated APIs
- Services via Interface abstrahieren
- DTOs für API-Layer, Entities nie direkt exposen
- Exception Handling via @ControllerAdvice
- Logging: SLF4J + Logback, kein System.out.println
- Keine Magic Numbers, keine hardcodierten Credentials

### Vue.js
- Composition API (keine Options API in neuem Code)
- Komponenten: PascalCase, Single Responsibility
- Props validieren (defineProps mit Types)
- Keine direkten API-Calls in Komponenten – immer via Pinia Store oder Composable
- CSS: Scoped styles, keine globalen Overrides

### Datenbank
- Oracle: Tabellennamen UPPER_CASE, keine reserved words als Spaltennamen
- PostgreSQL: snake_case für alle Bezeichner
- Alle Schema-Änderungen via Flyway Migration (niemals manuell)
- Kein SELECT * in Produktion

### Allgemein
- Keine Credentials, Secrets oder Tokens im Code oder Git
- Kommentare nur wenn die Logik nicht selbsterklärend ist
- Funktionen/Methoden max. 30 Zeilen, danach aufteilen
- Tests sind Pflicht für neue Features

---

## Session-Start (immer ausführen)

1. `tasks/lessons.md` lesen – Fehler aus vergangenen Sessions nicht wiederholen
2. `tasks/todo.md` prüfen – Gibt es offene Aufgaben?

## Datei-Pflege (nach jeder Session)

Alle KI-Konfigurationsdateien müssen aktuell gehalten werden. Das ist keine optionale Aufgabe – veraltete Dateien führen zu schlechteren Ergebnissen.

| Datei | Wann aktualisieren | Wer aktualisiert |
|-------|--------------------|-----------------|
| `tasks/lessons.md` | Nach jeder Korrektur sofort | KI (automatisch) |
| `tasks/todo.md` | Wenn Tasks abgeschlossen oder neu entstehen | KI (automatisch) |
| `copilot-instructions.md` | Bei Stack-Änderungen, neuen Standards, neuen Erkenntnissen | Ihr (manuell) |
| `CLAUDE.md` | Synchron zu dieser Datei halten | Ihr (manuell) |
| `KI-Wissensbasis/` | Wenn neue KI-Tools oder Erkenntnisse relevant werden | KI oder manuell |

**Wichtig – kein automatisches Gedächtnis bei Copilot:**
Copilot merkt sich nichts über Sessions hinaus. Was Claude Code automatisch in der `MEMORY.md` festhält, muss hier manuell eingetragen werden.

**Faustregel:** Nach einer Claude Code Session prüfen: Hat die KI etwas Wichtiges in der MEMORY.md ergänzt? → Hier nachtragen.

---

## Arbeitsweise

### 1. Plan First
- Bei 3+ Schritten: Plan in `tasks/todo.md` mit abhakbaren Items: `- [ ] Schritt`
- Plan vor Implementierung prüfen, Fortschritt abhaken: `- [x] Schritt`
- Am Ende: kurze Ergebnis-Zusammenfassung ergänzen

### 2. Stop and Re-Plan
- Wenn etwas nicht funktioniert: STOP – nicht weiter pushen
- Neu analysieren, neuen Plan aufstellen, dann weitermachen

### 3. Verification Before Done
- Nie fertig melden ohne Beweis: Tests laufen, Build erfolgreich
- Frage: "Would a staff engineer approve this?"
- Root Causes finden – keine temporären Fixes

### 4. Demand Elegance
- Bei nicht-trivialen Änderungen: "Gibt es eine elegantere Lösung?"
- Wenn hacky: "Knowing everything I know now – wie würde ich das richtig bauen?"
- Bei einfachen Fixes überspringen

### 5. Minimal Impact (Simplicity First)
- Nur anfassen was für die Aufgabe nötig ist
- Keine nicht angeforderten Features, Refactorings oder Verbesserungen
- Keine Abstraktionen für einmalige Operationen

### 6. Autonomous Bug Fixing
- Bei Bug-Report: einfach fixen – Logs lesen, analysieren, lösen
- Fehlschlagende Tests direkt beheben ohne explizite Anleitung

### 7. Investigate Before Answering
- Nie über Code spekulieren der nicht gelesen wurde
- Relevante Dateien zuerst lesen, dann antworten

### 8. Self-Improvement Loop
- Nach JEDER Korrektur: `tasks/lessons.md` sofort updaten
- Neue KI-Erkenntnisse → `KI-Wissensbasis/` dokumentieren

### 9. Reversibilität beachten
- Destruktive Aktionen erst bestätigen lassen:
  - Dateien/Branches löschen, DB-Tabellen droppen
  - Aktionen die andere sehen (Push, PR-Kommentare, externe Services)

**Nicht gewünscht:**
- Unnötige Rückfragen bei klaren Aufgaben
- Over-Engineering und hypothetische Erweiterungen
- Lange Erklärungen wenn eine kurze reicht
- Neue Abhängigkeiten ohne explizite Freigabe
- Hardcoding oder Test-Gaming

---

## Agent-System – Verfügbare Agents

Spezialisierte Agents für verschiedene Aufgaben. Jeder läuft isoliert und hat seinen eigenen Fokus.

### Verfügbare Agents

| Agent | Aufruf | Wann einsetzen 1|
|-------|--------|----------------1|
| DevAgent | `@DevAgent` | Feature-Implementierung, Bug Fixes, Refactoring |
| TestAgent | `@TestAgent` | Unit Tests, E2E Tests, Coverage-Analyse |
| ReviewAgent | `@ReviewAgent` | Code Review vor jedem PR-Merge |
| DocsAgent | `@DocsAgent` | API-Docs, ADRs, Onboarding-Guides |
| KnowledgeAgent | `@KnowledgeAgent` | Fragen zu KI-Tools, Workflows, Best Practices |

### Situationen erkennen – Agent proaktiv vorschlagen

Du startest Agents nicht automatisch, aber du erkennst Situationen und schlägst proaktiv den passenden Agent vor:

| Situation | Agent vorschlagen |
|-----------|-----------------|
| "Reviewe / prüfe den Code / ist das PR-ready?" | "Soll ich `@ReviewAgent` aufrufen?" |
| "Schreib Tests / erhöhe Coverage / was fehlt?" | "Soll ich `@TestAgent` aufrufen?" |
| "Implementiere Feature / fix Bug / refactore" | "Soll ich `@DevAgent` aufrufen?" |
| "Erstell Docs / OpenAPI / ADR / README" | "Soll ich `@DocsAgent` aufrufen?" |
| "Was ist ... / wie funktioniert ... / KI-Frage" | "Soll ich `@KnowledgeAgent` aufrufen?" |
| Vue-Komponente geändert aber kein Test erwähnt | Proaktiv: "Möchtest du `@TestAgent` für den Vitest-Test nutzen?" |
| Neuer User-Flow implementiert | Proaktiv: "Möchtest du `@TestAgent` für den Playwright E2E-Test nutzen?" |
| Feature fertig, PR-Merge steht an | Proaktiv: "Möchtest du `@ReviewAgent` vor dem Merge aufrufen?" |

### Empfohlene Workflows

```
Neues Feature:
  1. Plan erstellen (tasks/todo.md)
  2. @DevAgent implementiert (Backend + Frontend + Unit-Tests)
  3. @TestAgent für E2E-Tests falls neuer User-Flow
  4. @ReviewAgent vor PR-Merge
  5. @DocsAgent aktualisiert API-Docs

Bug Fix:
  1. @DevAgent analysiert und fixt
  2. @TestAgent schreibt Regression-Test
  3. @ReviewAgent gibt Freigabe

Frage zu KI / Tooling / Best Practices:
  → @KnowledgeAgent liest interne Wissensbasis + Web
```

### Grundregeln für Agents
1. Ein Task pro Agent – fokussierter Scope
2. @ReviewAgent immer vor PR-Merge aufrufen
3. Eigene Arbeit verifizieren bevor "fertig" gemeldet wird
4. Bei Unklarheit über Scope: kurz nachfragen, dann autonom arbeiten

---

## Security-Guidance (vor jedem Commit prüfen)

Diese Checkliste gilt für jeden Code-Output – nicht optional:

| Bereich | Regel |
|---------|-------|
| **Credentials** | Keine Passwörter, Tokens, API-Keys im Code oder in Git |
| **SQL** | Nur PreparedStatements / JPA-Queries – keine String-Konkatenation |
| **Input-Validierung** | User-Input immer validieren (`@Valid`, `@NotNull`, DTO-Level) |
| **API-Sicherheit** | Alle Endpoints mit Rollen absichern (`@PreAuthorize`) |
| **Logging** | Keine sensiblen Daten (Passwörter, PII) in Logs |
| **Dependencies** | Keine neuen Abhängigkeiten ohne explizite Freigabe |
| **XSS** | User-Inputs in Vue Templates immer escapen – kein `v-html` mit User-Daten |
| **CORS** | Keine Wildcard-Origins in Produktion |

**Wenn du unsicheren Code erkennst: sofort melden und Fix vorschlagen – nicht stillschweigend weitermachen.**

---

## Selbst-Verifikation vor Antwort (Ralph-Loop)

Bevor du eine Antwort oder einen Code-Output abgibst, prüfe intern:

1. **Vollständig?** – Wurden alle Anforderungen aus der Aufgabe erfüllt?
2. **Korrekt?** – Ist der Code syntaktisch und logisch korrekt?
3. **Konventionen?** – Entspricht er unseren Code-Standards (Java/Vue/DB-Regeln oben)?
4. **Sicher?** – Keine Security-Lücken (Checkliste oben)?
5. **Minimal?** – Keine unnötigen Änderungen außerhalb des Aufgaben-Scopes?

Falls eine Prüfung fehlschlägt: **Verbessern, dann erst antworten.** Kein "fast richtig" abliefern.
