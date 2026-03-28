# CLAUDE.md – [COMPANY NAME]

> **Projekt:** [PROJECT NAME] | **Stack:** [STACK]

## Session-Start
1. @MEMORY.md — Architekturentscheidungen, bekannte Probleme
2. @tasks/lessons.md — Fehler nicht wiederholen
3. tasks/todo.md pruefen — offene Aufgaben fortsetzen

## Kern-Verhalten
- **Plan First:** Bei 3+ Schritten → /plan, in tasks/todo.md dokumentieren
- **Investigate First:** NEVER spekulieren ueber ungelesenen Code
- **Minimal Impact:** NUR anfassen was fuer die Aufgabe noetig ist
- **TDD:** Features und Bug Fixes immer mit Red-Green-Refactor (Test → Fail → Code → Pass → Refactor)
- **Systematic Debugging:** Bei Bugs: Root Cause finden (Fehler lesen → reproduzieren → vergleichen → Hypothese), NEVER Symptome flicken
- **Stop & Re-Plan:** Wenn etwas nicht klappt: STOP, neu analysieren. Nach 3 gescheiterten Fixes: Architektur hinterfragen
- **Verify Before Done:** NEVER fertig melden ohne Build/Test-Output als Beweis
- **Simplify:** Nach Implementierung pruefen: Unnoetige Abstraktionen, Duplikate, ueber-engineerte Loesungen
- **Fix Bugs Autonom:** Logs lesen, Fehler analysieren, direkt beheben
- **Learn:** Nach JEDER Korrektur → tasks/lessons.md sofort updaten

## NEVER
- Rueckfragen bei klaren Aufgaben
- Features/Refactorings die nicht angefragt wurden
- Neue Abhaengigkeiten ohne explizite Freigabe
- Credentials/Secrets im Code oder Git
- Hardcoding oder Test-Gaming

## Standard-Workflow (IMMER bei Implementierungen)

Die Hauptkonversation orchestriert mit Skills, Agents fuehren aus.
Gilt fuer JEDE Implementierung — nicht nur bei /swarm.

### Workflow
```
1. Analyse        → Aufgabe verstehen, relevante Dateien lesen
2. [Skill]        → /brainstorming bei komplexen/unklaren Tasks
3. [Skill]        → /frontend-design bei UI-Arbeit
4. Dev Agent      → Implementierung (TDD erzwungen durch Agent-Config)
5. [Skill]        → /simplify auf geaenderten Code
6. Test Agent     → Tests erweitern, E2E, Coverage
7. Review Agent   → Security + Simplify + lessons.md
8. [Skill]        → /commit wenn alles gruen
```

### Wann welchen Skill nutzen
| Trigger | Skill |
|---------|-------|
| Neues Feature, neue Komponente, Konzeptarbeit | /brainstorming |
| UI-Komponenten, Seiten, visuelles Design | /frontend-design |
| Nach Implementierung (> 50 Zeilen neuer Code) | /simplify |
| Bug, Testfehler, unerwartetes Verhalten | /systematic-debugging |
| Vor Commit oder PR | /review |
| Fertigmeldung, "ist erledigt" | /verification-before-completion |

### Ausnahme: Kleine Tasks (< 1 Schritt)
Triviale Aenderungen (Typo, Config-Wert, einzelne Zeile) direkt in der Hauptkonversation — kein Agent-Dispatch noetig.

## Agents

| Agent | Wann | Integrierte Prinzipien |
|-------|------|----------------------|
| dev.md | Features, Bug Fixes, Refactoring | TDD, Systematic Debugging, Frontend-Design, Verification |
| test.md | Unit Tests, E2E Tests, Coverage | Verification-Gate, Coverage-Reporting |
| review.md | Code Review vor PR-Merge | Security (erweitert), Simplify, lessons.md |
| docs.md | API-Docs, ADRs, Guides | — |

## Swarm-Pipeline (bei /swarm — mehrere Agents parallel)
```
0. [Optional] Brainstorming  → Bei komplexen/unklaren Tasks
1. Dev Agent(s)              → TDD (Red-Green-Refactor), parallel wenn unabhaengig
2. /simplify                 → Code-Vereinfachung (Skill in Hauptkonversation)
3. Test Agent(s)             → Tests, E2E, Coverage
4. Review Agent              → Security + Simplify + lessons.md (IMMER zuletzt)
```

## Plugins

Aktive Claude Code Plugins (konfiguriert in `.claude/settings.json`):

| Plugin | Zweck | Stack-abhaengig |
|--------|-------|-----------------|
| context7 | Aktuelle Doku fuer Libraries | Nein |
| commit-commands | Git Commit Workflows | Nein |
| superpowers | Erweiterte Faehigkeiten | Nein |
| github | GitHub Integration | Nein |
| code-simplifier | Code vereinfachen | Nein |
| ralph-loop | Iterative Verbesserung | Nein |
| security-guidance | Security Best Practices | Nein |
| claude-md-management | CLAUDE.md Verwaltung | Nein |
| memory | Persistentes Gedaechtnis | Nein |
| test-runner | Tests ausfuehren | Nein |
| typescript-lsp | TypeScript Analyse | Ja — nur bei TS/JS |
| frontend-design | UI/UX Unterstuetzung | Ja — nur bei Frontend |
| chrome-devtools-mcp | Browser DevTools | Ja — nur bei Frontend |
| docker | Container Management | Ja — nur bei Docker |
| playwright | E2E Browser-Tests | Ja — nur bei E2E |

> Stack-abhaengige Plugins werden von `/configure` automatisch aktiviert/deaktiviert.

## Datei-Pflege

| Datei | Wer |
|-------|-----|
| tasks/lessons.md | KI — nach jeder Korrektur sofort |
| tasks/todo.md | KI — bei Task-Aenderungen |
| MEMORY.md | KI — bei Architekturentscheidungen |
| CLAUDE.md | Team — bei Stack/Standard-Aenderungen |

## Context
- /compact bei 70% Auslastung
- /clear zwischen unrelateden Tasks
- Sub-Agents fuer Research
- Parallele Tool-Calls wo moeglich
