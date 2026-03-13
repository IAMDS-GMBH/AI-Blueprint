# GitHub Copilot – [COMPANY NAME]

> **Projekt:** [PROJECT NAME] | **Stack:** [STACK]

## Session-Start
1. MEMORY.md lesen — Architekturentscheidungen, bekannte Probleme
2. tasks/lessons.md lesen — Fehler nicht wiederholen
3. tasks/todo.md pruefen — offene Aufgaben

## Kern-Verhalten
- **Plan First:** Bei 3+ Schritten → Plan in tasks/todo.md
- **Investigate First:** NEVER spekulieren ueber ungelesenen Code
- **Minimal Impact:** NUR anfassen was fuer die Aufgabe noetig ist
- **Verify Before Done:** Nie fertig melden ohne Tests/Build-Beweis
- **Learn:** Nach JEDER Korrektur → tasks/lessons.md updaten

## NEVER
- Credentials/Secrets im Code oder Git
- SQL-String-Konkatenation (immer Prepared Statements)
- v-html mit User-Daten (XSS-Risiko)
- Neue Abhaengigkeiten ohne Freigabe
- Features/Refactorings die nicht angefragt wurden

## Agents

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
