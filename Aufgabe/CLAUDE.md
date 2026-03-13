# CLAUDE.md – [COMPANY NAME] AI Workspace

> Diese Datei wird bei jeder KI-Session automatisch geladen.
> Sie definiert unternehmensweite Standards fuer alle KI-gestuetzten Arbeiten.
> Projekt-spezifische Anpassungen: Diese Datei im jeweiligen Projekt-Repository kopieren und editieren.

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
Monitoring:   [PLACEHOLDER – z.B. Datadog, Prometheus]

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

Alle KI-Konfigurationsdateien muessen aktuell gehalten werden. Das ist keine optionale Aufgabe – veraltete Dateien fuehren zu schlechteren Ergebnissen.

| Datei | Wann aktualisieren | Wer aktualisiert |
|-------|--------------------|-----------------|
| `tasks/lessons.md` | Nach jeder Korrektur sofort | Claude Code (automatisch) |
| `tasks/todo.md` | Wenn Tasks abgeschlossen oder neu entstehen | Claude Code (automatisch) |
| `MEMORY.md` | Bei neuen Architekturentscheidungen, geloesten Problemen, Praeferenzaenderungen | Claude Code (automatisch) |
| `CLAUDE.md` | Bei Stack-Aenderungen, neuen Team-Standards | Ihr (manuell) |
| `.github/copilot-instructions.md` | Synchron zu CLAUDE.md halten | Ihr (manuell) |
| `KI-Wissensbasis/` | Wenn neue KI-Tools oder Erkenntnisse relevant werden | Claude Code oder manuell |

**Goldene Regel:** Was Claude Code in der MEMORY.md erkennt und eintraegt, muss manuell in die `copilot-instructions.md` uebertragen werden – damit beide Tools denselben Stand haben.

**MEMORY.md Ablageort:**
```
MEMORY.md        # Im Projekt-Root (via setup.sh angelegt, CLAUDE.md laedt sie automatisch)
```
> `MEMORY.md` liegt direkt im Projekt – versioniert, fuer alle Entwickler im Team sichtbar.
> Claude Code laedt sie automatisch weil CLAUDE.md sie im Session-Start referenziert.

---

## Agent-Konfiguration

Spezialisierte Agents fuer verschiedene Aufgaben. Jeder laeuft isoliert im eigenen Kontextfenster.

### Verfuegbare Agents (.claude/agents/)

| Agent | Datei | Wann einsetzen |
|-------|-------|----------------|
| Dev Agent | `dev-agent.md` | Feature-Implementierung, Bug Fixes, Refactoring |
| Test Agent | `test-agent.md` | Unit Tests, E2E Tests, Coverage-Analyse |
| Review Agent | `review-agent.md` | Code Review vor jedem PR-Merge |
| Docs Agent | `docs-agent.md` | API-Docs, ADRs, Onboarding-Guides |

### Wann Sub-Agents einsetzen
- Task braucht Research/Exploration → Sub-Agent (Hauptkontext sauber halten)
- Parallele, unabhaengige Aufgaben → `/swarm` nutzen
- Task > 30 Minuten Arbeit → Aufteilen, je 1 Agent pro Teilaufgabe

### Grundregeln fuer Agents
1. Ein Task pro Agent – fokussierte Ausfuehrung
2. Abhaengigkeiten zwischen Agents explizit dokumentieren
3. Review-Agent immer am Ende des Swarms
4. Eigene Arbeit verifizieren bevor "fertig" gemeldet wird

---

## Arbeitsweise (fuer alle Rollen)

### 1. Plan First
- Bei 3+ Schritten: Plan in `tasks/todo.md` mit abhakbaren Items schreiben: `- [ ] Schritt`
- Plan vor Implementierung kurz pruefen lassen
- Fortschritt waehrend der Arbeit abhaken: `- [x] Schritt`
- Am Ende: kurze Ergebnis-Zusammenfassung in `tasks/todo.md` ergaenzen
- In Claude Code: `/plan` Command nutzen

### 2. Stop and Re-Plan
- Wenn etwas nicht wie erwartet funktioniert: STOP – nicht weiter pushen
- Neu analysieren, neuen Plan aufstellen, dann weitermachen
- Kein Weiterhaemmern in falscher Richtung

### 3. Verification Before Done
- Nie fertig melden ohne Beweis: Tests laufen, Build erfolgreich, Diff reviewt
- Frage vor Abgabe: "Would a staff engineer approve this?"
- Root Causes finden – keine temporaeren Fixes, keine Workarounds

### 4. Demand Elegance
- Bei nicht-trivialen Aenderungen kurz innehalten: "Gibt es eine elegantere Loesung?"
- Wenn eine Loesung hacky wirkt: "Knowing everything I know now – wie wuerde ich das richtig bauen?"
- Fuer einfache, offensichtliche Fixes ueberspringen – kein Over-Engineering

### 5. Minimal Impact (Simplicity First)
- Jede Aenderung so klein wie moeglich – nur anfassen was fuer die Aufgabe noetig ist
- Keine Features, Refactorings oder Verbesserungen die nicht explizit angefragt wurden
- Keine Error-Handler, Fallbacks oder Validation fuer Szenarien die nicht eintreten koennen
- Keine Abstraktionen fuer einmalige Operationen

### 6. Autonomous Bug Fixing
- Bei einem Bug-Report: einfach fixen – Logs lesen, Fehler analysieren, loesen
- Kein Hand-holding benoetigen – direkt auf Logs/Errors/failing Tests zeigen und beheben
- Fehlschlagende CI-Tests fixen ohne explizite Anleitung

### 7. Investigate Before Answering
- Nie ueber Code spekulieren der nicht gelesen wurde
- Wenn User eine spezifische Datei referenziert: Datei zuerst lesen, dann antworten
- Relevante Dateien immer zuerst untersuchen bevor Fragen beantwortet werden

### 8. Self-Improvement Loop
- Nach JEDER Korrektur durch den User: `tasks/lessons.md` sofort updaten
- Regel schreiben die denselben Fehler kuenftig verhindert
- Neue KI-Erkenntnisse → `KI-Wissensbasis/` dokumentieren

### 9. Reversibilitaet beachten
- Lokale, reversible Aktionen (Dateien editieren, Tests laufen) → direkt ausfuehren
- Destruktive Aktionen erst bestaetigen lassen:
  - Dateien/Branches loeschen, DB-Tabellen droppen
  - `git push --force`, `git reset --hard`
  - Aktionen die andere sehen (Push, PR-Kommentare, externe Services)

### 10. Parallele Tool-Calls
- Unabhaengige Aktionen immer parallel ausfuehren (z.B. 3 Dateien gleichzeitig lesen)
- Sequenziell nur wenn spaetere Schritte vom Ergebnis frueherer abhaengen

**Nicht gewuenscht:**
- Unnoetige Rueckfragen bei klaren Aufgaben
- Over-Engineering und hypothetische "nice-to-have" Erweiterungen
- Lange Erklaerungen wenn eine kurze reicht
- Neue Abhaengigkeiten ohne explizite Freigabe
- Hardcoding oder Test-Gaming – allgemeine Loesungen, keine Einzel-Fall-Hacks

---

## GSD-Workflow (Get Shit Done) — Optional

> GSD ist ein Add-on fuer grosse Projekte. Installation: `npx get-shit-done-cc@latest`
> Fuer kleine Tasks (Bug Fix, einzelnes Feature) unsere eigenen Commands nutzen.

### Wann GSD statt unserer Commands

| Situation | Empfohlener Workflow |
|---|---|
| Bug Fix, kleines Feature (< 5 Dateien) | `/plan` → implementieren → `/review` |
| Mittleres Feature (5-15 Dateien) | `/swarm` mit Dev + Test + Review Agent |
| Grosses Feature / Neues Modul (15+ Dateien) | `/gsd:plan-phase` → `/gsd:execute-phase` |
| Greenfield-Projekt (komplett neu) | `/gsd:new-project` → phasenweise aufbauen |
| Legacy-Migration (viele Module) | `/gsd:map-codebase` → phasenweise migrieren |

### GSD + unser Setup — Zusammenspiel

GSD erstellt eigene Planungsdateien in `.planning/`. Diese ergaenzen unsere Struktur:
- **CLAUDE.md** bleibt die Quelle fuer Code-Standards, Tech Stack, Konventionen
- **Rules** (Java, Vue, SQL) gelten auch fuer GSD-generiertem Code
- **MEMORY.md** und `tasks/lessons.md` werden weiterhin gepflegt
- **MCP-Server** stehen auch GSD-Agents zur Verfuegung

### GSD-Session starten

```
# Bestehendes Projekt analysieren
/gsd:map-codebase

# Oder neues Projekt aufsetzen
/gsd:new-project
```

---

## Rollen-Kontext

> Je nach Anwender gibt es unterschiedliche Beduerfnisse:

**Developer:**
- Fokus auf Code-Qualitaet, Tests, Architektur
- Skill `dev-standards` automatisch aktiv
- Commands: /review, /plan, /test, /ralph

**CEO / Management:**
- Fokus auf Outcomes, Status, Entscheidungen
- Skill `ceo-brief` bei Bedarf laden
- Commands: /brief, /automate-check

**DevOps / Automatisierung:**
- Fokus auf Pipelines, Deployments, Monitoring
- Skill `automation-design` bei Bedarf laden
- Commands: /automate, /deploy-check

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
    scripts/            # Utility-Scripts
  .claude/
    agents/             # Spezialisierte Agent-Configs
    commands/           # Slash Commands (/plan, /review, /swarm, ...)
    skills/             # On-Demand Skills (dev-standards, ceo-brief, ...)
  tasks/
    todo.md             # Aktueller Plan
    lessons.md          # Team-Learnings (immer lesen zu Session-Start!)
  KI-Wissensbasis/      # Wachsende KI-Wissensdatenbank des Unternehmens
  .mcp.json             # MCP Tool-Konfiguration
  CLAUDE.md             # Diese Datei
```
