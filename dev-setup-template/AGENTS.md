# AGENTS.md – [COMPANY NAME]

> **Projekt:** [PROJECT NAME] | **Stack:** [STACK]

## Session-Start
1. MEMORY.md lesen — Architekturentscheidungen, bekannte Probleme
2. tasks/lessons.md lesen — Fehler nicht wiederholen
3. tasks/todo.md pruefen — offene Aufgaben fortsetzen

## Kern-Verhalten
- **Plan First:** Bei 3+ Schritten → Plan in tasks/todo.md dokumentieren
- **Investigate First:** NEVER spekulieren ueber ungelesenen Code
- **Minimal Impact:** NUR anfassen was fuer die Aufgabe noetig ist
- **Stop & Re-Plan:** Wenn etwas nicht klappt: STOP, neu analysieren
- **Verify Before Done:** Nie fertig melden ohne Beweis (Tests, Build OK)
- **Learn:** Nach JEDER Korrektur → tasks/lessons.md sofort updaten

## NEVER
- Credentials/Secrets im Code oder Git
- SQL-String-Konkatenation (immer Prepared Statements)
- v-html mit User-Daten (XSS-Risiko)
- Neue Abhaengigkeiten ohne Freigabe
- Features/Refactorings die nicht angefragt wurden

## Agents

### Vibe
| Agent | Aufruf | Wann |
|-------|--------|------|
| Dev | `vibe --agent dev` | Features, Bug Fixes, Refactoring |
| Test | `vibe --agent test` | Unit Tests, E2E Tests, Coverage |
| Review | `vibe --agent review` | Code Review vor Merge (read-only) |
| Docs | `vibe --agent docs` | API-Docs, ADRs, Guides |

### Copilot
| Agent | Aufruf | Wann |
|-------|--------|------|
| DevAgent | @DevAgent | Features, Bug Fixes, Refactoring |
| TestAgent | @TestAgent | Unit Tests, E2E Tests, Coverage |
| ReviewAgent | @ReviewAgent | Code Review vor PR-Merge (read-only) |
| DocsAgent | @DocsAgent | API-Docs, ADRs, Guides |

## Code-Standards
- Methoden max. 30 Zeilen
- Keine Magic Numbers — benannte Konstanten
- Tests sind Pflicht fuer neue Features
- Kommentare nur wenn Logik nicht selbsterklaerend

## Datei-Pflege
| Datei | Wer |
|-------|-----|
| tasks/lessons.md | KI — nach jeder Korrektur sofort |
| tasks/todo.md | KI — bei Task-Aenderungen |
| MEMORY.md | KI — bei Architekturentscheidungen |
| AGENTS.md | Team — bei Stack/Standard-Aenderungen |
