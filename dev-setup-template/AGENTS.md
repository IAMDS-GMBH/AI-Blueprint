# AGENTS.md – [COMPANY NAME] AI Workspace

> Diese Datei wird von Mistral Vibe automatisch gelesen.
> Sie definiert unternehmensweite Standards fuer KI-gestuetzte Entwicklung.
> Pendant zu CLAUDE.md (fuer Claude Code) – beide koennen parallel existieren.

---

## Unternehmen & Kontext

**Unternehmen:** [COMPANY NAME]
**Branche:** [BRANCHE]
**Ziel dieser KI-Konfiguration:** Maximale Automatisierung repetitiver Aufgaben ueber alle Teams hinweg.

---

## Tech Stack

> ANPASSEN: Diesen Abschnitt pro Projekt anpassen. Die Werte unten sind Beispiele.

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

> ANPASSEN: Projektspezifische Konventionen hier eintragen.

### Java / Spring
- Java 17+, keine deprecated APIs
- Services via Interface abstrahieren
- DTOs fuer API-Layer, Entities nie direkt exposen
- Exception Handling via @RestControllerAdvice
- Constructor Injection via @RequiredArgsConstructor (Lombok)
- Logging: SLF4J + Logback, kein System.out.println
- Keine Magic Numbers, keine hardcodierten Credentials

### Vue.js
- Composition API (keine Options API in neuem Code)
- Komponenten: PascalCase, Single Responsibility
- Props validieren (defineProps mit Types)
- Keine direkten API-Calls in Komponenten – immer via Pinia Store oder Composable
- CSS: Scoped styles, keine globalen Overrides

### Datenbank
- Oracle: Tabellennamen UPPER_CASE, keine reserved words
- PostgreSQL: snake_case fuer alle Bezeichner
- Alle Schema-Aenderungen via Flyway Migration (niemals manuell)
- Keine SELECT * in Produktion

### Allgemein
- Keine Credentials, Secrets oder Tokens im Code oder Git
- Kommentare nur wenn die Logik nicht selbsterklaerend ist
- Funktionen/Methoden max. 30 Zeilen, danach aufteilen
- Tests sind Pflicht fuer neue Features

---

## Session-Start (immer ausfuehren)

1. `MEMORY.md` lesen – Architekturentscheidungen, bekannte Probleme, Team-Praeferenzen
2. `tasks/lessons.md` lesen – Fehler aus vergangenen Sessions nicht wiederholen
3. `tasks/todo.md` pruefen – Gibt es offene Aufgaben?

## Datei-Pflege (nach jeder Session)

| Datei | Wann aktualisieren | Wer aktualisiert |
|-------|--------------------|-----------------|
| `tasks/lessons.md` | Nach jeder Korrektur sofort | Vibe (automatisch) |
| `tasks/todo.md` | Wenn Tasks abgeschlossen oder neu entstehen | Vibe (automatisch) |
| `MEMORY.md` | Bei neuen Architekturentscheidungen | Vibe (automatisch) |
| `AGENTS.md` | Bei Stack-Aenderungen, neuen Team-Standards | Ihr (manuell) |

**MEMORY.md Ablageort:**
```
MEMORY.md        # Im Projekt-Root (via setup.sh angelegt)
```

---

## Agent-Konfiguration

Spezialisierte Agents fuer verschiedene Aufgaben. Jeder laeuft isoliert im eigenen Kontextfenster.

### Verfuegbare Agents (.vibe/agents/)

| Agent | Datei | Wann einsetzen |
|-------|-------|----------------|
| Dev Agent | `dev-agent.toml` | Feature-Implementierung, Bug Fixes, Refactoring |
| Test Agent | `test-agent.toml` | Unit Tests, E2E Tests, Coverage-Analyse |
| Review Agent | `review-agent.toml` | Code Review vor jedem PR-Merge |
| Docs Agent | `docs-agent.toml` | API-Docs, ADRs, Onboarding-Guides |
| Knowledge Agent | `knowledge-agent.toml` | KI-Wissensbasis pflegen, Fragen beantworten |

### Agents aufrufen
```bash
vibe --agent dev       # Dev Agent starten
vibe --agent test      # Test Agent starten
vibe --agent review    # Review Agent starten
vibe --agent plan      # Built-in Plan Agent (nur planen, nicht ausfuehren)
```

### Wann Sub-Agents einsetzen
- Task braucht Research/Exploration → Sub-Agent (Hauptkontext sauber halten)
- Parallele, unabhaengige Aufgaben → Mehrere Agents gleichzeitig
- Task > 30 Minuten Arbeit → Aufteilen, je 1 Agent pro Teilaufgabe

### Grundregeln fuer Agents
1. Ein Task pro Agent – fokussierte Ausfuehrung
2. Abhaengigkeiten zwischen Agents explizit dokumentieren
3. Review-Agent immer am Ende
4. Eigene Arbeit verifizieren bevor "fertig" gemeldet wird

---

## Verfuegbare Skills (.vibe/skills/)

| Skill | Ordner | Wann laden |
|-------|--------|-----------|
| dev-standards | `dev-standards/SKILL.md` | Code schreiben, reviewen, refactoren |
| frontend-design | `frontend-design/SKILL.md` | Vue.js Komponenten aus Design-Vorgaben |
| ceo-brief | `ceo-brief/SKILL.md` | Executive-Zusammenfassungen, Status-Reports |
| automation-design | `automation-design/SKILL.md` | Automatisierungen entwerfen, n8n-Workflows |
| document-creation | `document-creation/SKILL.md` | ADRs, Tech-Specs, Onboarding-Guides |
| agent-orchestration | `agent-orchestration/SKILL.md` | Komplexe Aufgaben auf mehrere Agents verteilen |

---

## Arbeitsweise (fuer alle Rollen)

### 1. Plan First
- Bei 3+ Schritten: Plan in `tasks/todo.md` mit abhakbaren Items schreiben
- Plan vor Implementierung kurz pruefen lassen
- Fortschritt waehrend der Arbeit abhaken
- In Mistral Vibe: `vibe --agent plan` nutzen

### 2. Stop and Re-Plan
- Wenn etwas nicht wie erwartet funktioniert: STOP – nicht weiter pushen
- Neu analysieren, neuen Plan aufstellen, dann weitermachen

### 3. Verification Before Done
- Nie fertig melden ohne Beweis: Tests laufen, Build erfolgreich, Diff reviewt
- Frage vor Abgabe: "Would a staff engineer approve this?"
- Root Causes finden – keine temporaeren Fixes

### 4. Demand Elegance
- Bei nicht-trivialen Aenderungen: "Gibt es eine elegantere Loesung?"
- Fuer einfache Fixes ueberspringen – kein Over-Engineering

### 5. Minimal Impact (Simplicity First)
- Jede Aenderung so klein wie moeglich
- Keine Features, Refactorings oder Verbesserungen die nicht angefragt wurden
- Keine Abstraktionen fuer einmalige Operationen

### 6. Autonomous Bug Fixing
- Bei Bug-Reports: Logs lesen, Fehler analysieren, loesen
- Kein Hand-holding benoetigen

### 7. Investigate Before Answering
- Nie ueber Code spekulieren der nicht gelesen wurde
- Relevante Dateien immer zuerst untersuchen

### 8. Self-Improvement Loop
- Nach JEDER Korrektur: `tasks/lessons.md` sofort updaten
- Neue Erkenntnisse → `KI-Wissensbasis/` dokumentieren

### 9. Reversibilitaet beachten
- Lokale, reversible Aktionen → direkt ausfuehren
- Destruktive Aktionen erst bestaetigen lassen

**Nicht gewuenscht:**
- Unnoetige Rueckfragen bei klaren Aufgaben
- Over-Engineering und hypothetische Erweiterungen
- Lange Erklaerungen wenn eine kurze reicht
- Neue Abhaengigkeiten ohne explizite Freigabe
- Hardcoding oder Test-Gaming

---

## MCP-Server

Die MCP-Server sind in `.vibe/config.toml` konfiguriert (gleiche Server wie in `.mcp.json` fuer Claude Code):

| Server | Zweck |
|--------|-------|
| playwright | Browser-Tests und E2E-Verifikation |
| github | PRs, Issues, Repos |
| figma | Design-to-Code Pipeline |
| postgres | DB-Queries und Schema-Analyse |
| context7 | Aktuelle Library-Dokumentation |

---

## Rollen-Kontext

**Developer:**
- Fokus auf Code-Qualitaet, Tests, Architektur
- Skill `dev-standards` automatisch aktiv

**CEO / Management:**
- Fokus auf Outcomes, Status, Entscheidungen
- Skill `ceo-brief` bei Bedarf laden

**DevOps / Automatisierung:**
- Fokus auf Pipelines, Deployments, Monitoring
- Skill `automation-design` bei Bedarf laden

---

## Automatisierungsstrategie

**Zielposition:** Level 3-4 (KI fuehrt aus, Mensch verifiziert)

**Prioritaet:**
1. Alles was mehr als 1x manuell gemacht wird → automatisieren
2. Alles was Recherche braucht → Sub-Agent
3. Alles was Review braucht → KI-First, Mensch approved

---

## Projektstruktur (Beispiel)

> ANPASSEN: Hier die tatsaechliche Projektstruktur eintragen.

```
[PROJECT-NAME]/
  backend/              # Java Spring Boot
  frontend/             # Vue.js 3
  db/
    migrations/         # Flyway
  .vibe/
    config.toml         # Vibe-Konfiguration + MCP-Server
    agents/             # Spezialisierte Agent-Configs (TOML)
    skills/             # On-Demand Skills (SKILL.md)
  .claude/
    agents/             # Claude Code Agent-Configs (Markdown)
    commands/           # Slash Commands (/plan, /review, ...)
    skills/             # Claude Code Skills
    rules/              # Code-Standards (Java, Vue, SQL)
  tasks/
    todo.md             # Aktueller Plan
    lessons.md          # Team-Learnings
  .mcp.json             # MCP fuer Claude Code (JSON)
  CLAUDE.md             # Claude Code Haupt-Config
  AGENTS.md             # Mistral Vibe Haupt-Config (diese Datei)
  MEMORY.md             # Persistentes Projekt-Gedaechtnis
```

---

## Hinweise

- CLAUDE.md und AGENTS.md koennen parallel existieren (fuer Claude Code bzw. Mistral Vibe)
- Bei Diskrepanzen: CLAUDE.md hat Vorrang (ist die Primaer-Config)
- `.claude/` und `.vibe/` stoeren sich nicht – beide Tools im selben Repo nutzbar
- MCP-Server sind identisch konfiguriert (JSON in .mcp.json, TOML in config.toml)
