---
name: agent-orchestration
description: Wird geladen wenn komplexe Aufgaben auf mehrere Agents verteilt werden sollen, oder wenn ein Task zu gross fuer ein einzelnes Kontextfenster ist.
license: MIT
compatibility: Python 3.12+
user-invocable: true
allowed-tools:
  - read_file
  - grep
  - bash
---

# Skill: Agent-Orchestrierung

## Grundprinzip

Jeder Sub-Agent laeuft in einem isolierten Kontextfenster – er sieht nicht was andere Agents tun.
Das haelt den Haupt-Kontext sauber und ermoeglicht paralleles Arbeiten.

```
Haupt-Agent (Orchestrator)
├── Sub-Agent A: Feature-Implementation  → dev-agent.toml
├── Sub-Agent B: Tests schreiben         → test-agent.toml
├── Sub-Agent C: Docs aktualisieren      → docs-agent.toml
└── Sub-Agent D: Code Review             → review-agent.toml
```

---

## Wann Sub-Agents einsetzen

| Situation | Empfehlung |
|-----------|-----------|
| Aufgabe braucht Research/Exploration | Sub-Agent – Hauptkontext sauber halten |
| Parallele unabhaengige Tasks | Mehrere Sub-Agents gleichzeitig |
| Task > 30 min Arbeit | Aufteilen, je 1 Agent pro Teilaufgabe |
| Spezialisiertes Wissen noetig | Passenden Agent laden |
| Grosses Refactoring | Je 1 Agent pro Modul |

## Wann KEIN Sub-Agent

- Einfache, direkte Aufgaben (< 5 Schritte)
- Wenn Kontext zwischen Schritten weitergegeben werden muss
- Wenn du die Kontrolle Schritt fuer Schritt behalten willst

---

## Verfuegbare Agents (in .vibe/agents/)

| Agent | Datei | Einsatz |
|-------|-------|---------|
| Dev Agent | `dev-agent.toml` | Feature-Implementierung, Bug Fixes, Refactoring |
| Test Agent | `test-agent.toml` | Unit Tests, E2E Tests, Coverage-Analyse |
| Review Agent | `review-agent.toml` | Code Review vor PR-Merge |
| Docs Agent | `docs-agent.toml` | API-Docs, ADRs, Onboarding-Guides |
| Knowledge Agent | `knowledge-agent.toml` | KI-Wissensbasis pflegen, Fragen beantworten |

---

## Swarm-Pattern fuer grosse Aufgaben

**Wann:** Grosse Features, grosse Refactorings, projektweite Aenderungen

**Ablauf:**
```
1. Plan erstellen → Gesamtaufgabe in unabhaengige Teilaufgaben zerlegen
2. Fuer jede Teilaufgabe: Passenden Agent + klare Beschreibung definieren
3. Agents parallel starten (vibe --agent [name])
4. Ergebnisse einsammeln und integrieren
5. Review-Agent prueft das Gesamtergebnis
```

**Beispiel – Neues Feature "Bestellverwaltung":**
```
Agent 1 (Dev):    Backend – OrderService, OrderController, DTOs
Agent 2 (Dev):    Frontend – OrderList.vue, OrderForm.vue, Pinia Store
Agent 3 (Test):   Tests fuer Backend + Frontend
Agent 4 (Docs):   API-Dokumentation + ADR
→ Danach: Review-Agent prueft alles zusammen
```

---

## Aufgabe an Sub-Agent uebergeben (Prompt-Template)

```
Du bist ein [ROLLE] Agent. Deine Konfiguration: [agent-name].toml

Aufgabe: [KLARE AUFGABENBESCHREIBUNG]

Kontext:
- Projekt-Stack: Java Spring Backend, Vue.js Frontend, Oracle + PostgreSQL
- Betroffene Dateien: [DATEIPFADE]
- Abhaengigkeiten: [Was muss vorher fertig sein]

Nicht im Scope:
- [Was der Agent NICHT anfassen soll]

Ergebnis-Format:
[Erwartetes Output-Format aus der agent-config]
```

---

## Self-Improvement nach Agent-Laeufen

Nach jedem Agent-Lauf:
- War die Aufgabenbeschreibung praezise genug? → Falls nein: Template verbessern
- Hat der Agent etwas ausserhalb des Scopes gemacht? → Constraints schaerfen
- Fehler gemacht? → In tasks/lessons.md eintragen
