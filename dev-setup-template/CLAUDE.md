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
- **Stop & Re-Plan:** Wenn etwas nicht klappt: STOP, neu analysieren
- **Verify Before Done:** Nie fertig melden ohne Beweis (Tests, Build OK)
- **Fix Bugs Autonom:** Logs lesen, Fehler analysieren, direkt beheben
- **Learn:** Nach JEDER Korrektur → tasks/lessons.md sofort updaten

## NEVER
- Rueckfragen bei klaren Aufgaben
- Features/Refactorings die nicht angefragt wurden
- Neue Abhaengigkeiten ohne explizite Freigabe
- Credentials/Secrets im Code oder Git
- Hardcoding oder Test-Gaming

## Agents

| Agent | Wann |
|-------|------|
| dev.md | Features, Bug Fixes, Refactoring |
| test.md | Unit Tests, E2E Tests, Coverage |
| review.md | Code Review vor PR-Merge |
| docs.md | API-Docs, ADRs, Guides |

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
