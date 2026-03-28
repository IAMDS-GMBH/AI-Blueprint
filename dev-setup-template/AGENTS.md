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
- **TDD:** Features und Bug Fixes mit Red-Green-Refactor
- **Systematic Debugging:** Root Cause finden, NEVER Symptome flicken. Nach 3 Fixes: Architektur hinterfragen
- **Stop & Re-Plan:** Wenn etwas nicht klappt: STOP, neu analysieren
- **Verify Before Done:** NEVER fertig melden ohne Build/Test-Output als Beweis
- **Simplify:** Unnoetige Abstraktionen, Duplikate, Ueber-Engineering pruefen
- **Learn:** Nach JEDER Korrektur → tasks/lessons.md sofort updaten

## Standard-Workflow
```
1. Analyse → 2. Dev Agent (TDD) → 3. Test Agent → 4. Review Agent
```
Kleine Tasks (Typo, Config-Wert) direkt — kein Agent-Dispatch noetig.

## NEVER
- Credentials/Secrets im Code oder Git
- SQL-String-Konkatenation (immer Prepared Statements)
- User-Daten unescaped rendern (XSS-Risiko)
- Neue Abhaengigkeiten ohne Freigabe
- Features/Refactorings die nicht angefragt wurden

## Agents

### Vibe
| Agent | Aufruf | Wann | Integrierte Prinzipien |
|-------|--------|------|----------------------|
| Dev | `vibe --agent dev` | Features, Bug Fixes, Refactoring | TDD, Systematic Debugging, Verification |
| Test | `vibe --agent test` | Unit Tests, E2E Tests, Coverage | Verification-Gate, Coverage-Reporting |
| Review | `vibe --agent review` | Code Review vor Merge (read-only) | Security, Simplify, lessons.md |
| Docs | `vibe --agent docs` | API-Docs, ADRs, Guides | — |

### Copilot
| Agent | Aufruf | Wann | Integrierte Prinzipien |
|-------|--------|------|----------------------|
| DevAgent | @DevAgent | Features, Bug Fixes, Refactoring | TDD, Systematic Debugging, Verification |
| TestAgent | @TestAgent | Unit Tests, E2E Tests, Coverage | Verification-Gate, Coverage-Reporting |
| ReviewAgent | @ReviewAgent | Code Review vor PR-Merge (read-only) | Security, Simplify, lessons.md |
| DocsAgent | @DocsAgent | API-Docs, ADRs, Guides | — |

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
