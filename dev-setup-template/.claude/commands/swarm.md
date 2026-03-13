# /swarm – Aufgabe auf mehrere Agents verteilen

Aufgabe: $ARGUMENTS

Zerlege eine komplexe Aufgabe in Teilaufgaben fuer spezialisierte Agents.
IMPORTANT: Auf Freigabe warten bevor Agents gestartet werden.

## Agent-Empfehlung
| Task-Typ | Agent |
|----------|-------|
| Code schreiben/aendern | Dev Agent |
| Tests schreiben | Test Agent |
| Docs/ADRs erstellen | Docs Agent |
| Code pruefen (read-only) | Review Agent |

## MUST: Swarm-Reihenfolge (verbindlich)
```
1. Dev Agent(s)    → Feature-Implementierung (parallel wenn unabhaengig)
2. Test Agent(s)   → Tests schreiben (NACH Dev Agents)
3. Review Agent    → Gesamtreview + lessons.md Update (IMMER als Letzter)
```

## Regeln
- Test Agents sind PFLICHT — kein Swarm ohne Tests
- Max 5 parallele Agents
- Schnittstellen zwischen Agents explizit definieren — NEVER raten
- Review Agent MUST tasks/lessons.md als letzten Schritt aktualisieren

## Output-Format
```
## Swarm-Plan: [AUFGABE]

| Agent | Typ | Aufgabe | Abhaengig von |
|-------|-----|---------|---------------|
| 1 | Dev | [...] | – |
| 2 | Test | [...] | Agent 1 |
| 3 | Review | Gesamtreview | Alle |

Reihenfolge:
Runde 1: Agent 1 (parallel)
Runde 2: Agent 2
Runde 3: Agent 3 (Review)
```
