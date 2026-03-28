# /swarm – Aufgabe auf mehrere Agents verteilen

Aufgabe: $ARGUMENTS

Zerlege eine komplexe Aufgabe in Teilaufgaben fuer spezialisierte Agents.
IMPORTANT: Auf Freigabe warten bevor Agents gestartet werden.

## Pre-Check: Brainstorming
Vor dem Swarm-Plan pruefen ob Brainstorming noetig ist:
- Task ist komplex (Aufwand >= Gross) → Brainstorming PFLICHT
- Task ist unklar oder hat mehrere Loesungswege → Brainstorming PFLICHT
- Task ist klar und klein → Brainstorming ueberspringen

Bei Brainstorming: Erst Design erarbeiten (2-3 Ansaetze mit Trade-offs), dann Swarm-Plan erstellen.

## Agent-Empfehlung
| Task-Typ | Agent |
|----------|-------|
| Code schreiben/aendern | Dev Agent (mit TDD) |
| Tests schreiben/erweitern | Test Agent |
| Docs/ADRs erstellen | Docs Agent |
| Code pruefen (read-only) | Review Agent (mit Security + Simplify) |

## MUST: Swarm-Reihenfolge (verbindlich)
```
0. [Skill]          → /brainstorming bei komplexen/unklaren Tasks (in Hauptkonversation)
1. [Skill]          → /frontend-design bei UI-Arbeit (in Hauptkonversation)
2. Dev Agent(s)     → Feature-Implementierung mit TDD (parallel wenn unabhaengig)
3. [Skill]          → /simplify auf geaenderten Code (in Hauptkonversation)
4. Test Agent(s)    → Zusaetzliche Tests, E2E, Coverage (NACH Dev Agents)
5. Review Agent     → Gesamtreview + Security + Simplify + lessons.md (IMMER als Letzter)
```

### Skills vs. Agents
- **Skills** (Schritte 0, 1, 3) laufen in der Hauptkonversation — voller Zugriff auf Skill-Tool
- **Agents** (Schritte 2, 4, 5) werden dispatcht — arbeiten autonom mit ihren Configs
- Skills koennen Ergebnisse als Kontext an nachfolgende Agents weitergeben

## Regeln
- Test Agents sind PFLICHT — kein Swarm ohne Tests
- Dev Agents arbeiten mit TDD (Red-Green-Refactor) — siehe dev.md
- Max 5 parallele Agents
- Schnittstellen zwischen Agents explizit definieren — NEVER raten
- Review Agent MUST tasks/lessons.md als letzten Schritt aktualisieren
- Alle Agents MUST Verifikation liefern (Build/Test-Output)

## Output-Format
```
## Swarm-Plan: [AUFGABE]

Skills: /brainstorming [Ja/Nein] | /frontend-design [Ja/Nein]

| # | Typ | Aufgabe | Abhaengig von |
|---|-----|---------|---------------|
| 0 | Skill: /brainstorming | [Design erarbeiten] | – |
| 1 | Skill: /frontend-design | [UI-Konzept] | 0 |
| 2 | Dev Agent | [...] (TDD) | 0, 1 |
| 3 | Skill: /simplify | [Code vereinfachen] | 2 |
| 4 | Test Agent | [...] | 2 |
| 5 | Review Agent | Gesamtreview + Security + Simplify | Alle |

Reihenfolge:
Runde 0: Skills in Hauptkonversation (/brainstorming, /frontend-design)
Runde 1: Dev Agent(s) (parallel, mit TDD)
Runde 2: /simplify (Skill) + Test Agent parallel
Runde 3: Review Agent
```
