# 03 – KI Agents & Plattformen

> Agent-Frameworks, autonome Systeme und Plattformen zum Erstellen und Betreiben von KI-Agents.

---

## Agent-Plattformen

### Claude Code Swarms `⭐ HOCH`
- **Was:** Agent-Distribution-System – mehrere Claude-Instanzen arbeiten koordiniert an Aufgaben
- **Konzept:** Ein "Schwarm" von Agents übernimmt verteilte Aufgaben (z.B. einer für Code, einer für Tests, einer für Docs)
- **Warum relevant:** Skalierung von KI-Arbeit für komplexe Projekte
- **Aktion:** [ ] Deep-Dive: Wie funktioniert die Koordination? Welche Tasks eignen sich?

### OpenAI Frontier (Coworkers) `⭐ HOCH`
- **Was:** OpenAIs neues "Coworker"-Konzept – KI-Agents als vollwertige Teammitglieder
- **Link:** https://openai.com/de-DE/index/introducing-openai-frontier/
- **Warum relevant:** Paradigmenwechsel: KI nicht nur als Tool, sondern als Kollege
- **Aktion:** [ ] Whitepaper lesen, interne Diskussion zum Thema "KI als Coworker"

### OpenAI Agent Builder `🔵 MITTEL`
- **Was:** OpenAIs Plattform zum Erstellen eigener Agents
- **Warum relevant:** Ermöglicht Custom Agents ohne eigene Infrastruktur
- **Aktion:** [ ] Feature-Set evaluieren vs. Claude Skills / Google ADK

### Google Agent Development Kit (ADK) `⭐ HOCH`
- **Was:** Googles SDK zum Bauen von KI-Agents
- **Link:** https://cloud.google.com/products/agent-builder
- **Warum relevant:** Enterprise-ready Agent-Framework im Google-Ökosystem
- **Aktion:** [ ] Vergleich mit Claude-basiertem Agent-Setup

### Gemini Gems (Google) `🔵 MITTEL`
- **Was:** Custom AI Agents für Workflows innerhalb des Google-Ökosystems
- **Warum relevant:** Spezialisierte Agents für wiederkehrende Aufgaben
- **Aktion:** [ ] Use Cases definieren (z.B. Reporting, Datenanalyse)

---

## Spezialisierte Agents

### ASTRAL AI Agent `🔵 MITTEL`
- **Was:** Marketing-Agent (Self-Hosted Alternative zu OpenClaw)
- **Warum relevant:** Autonomes Marketing-Content-Erstellen, Self-Hosted für Datenschutz
- **Aktion:** [ ] Evaluieren für Content-Marketing-Workflows

### Moltbook / Moltbot Agents `🔵 MITTEL`
- **Was:** Agent-Framework für Moltbot-basierte Automatisierung
- **Warum relevant:** Flexible Agent-Erstellung
- **Aktion:** [ ] Dokumentation sichten, Use Cases identifizieren

### Claude CoWork (Desktop App Agent) `⭐ HOCH`
- **Was:** Claude als lokaler Desktop-App-Agent
- **Link:** https://support.claude.com/de/articles/13345190-erste-schritte-mit-cowork
- **Warum relevant:** KI-Agent der direkt auf dem Desktop arbeitet – Dateien, Apps, Workflows
- **Aktion:** [ ] Einrichtung und initiale Tests durchführen

---

## Agent-Architektur (Best Practices) 📸

> Aus den Workflow-Orchestration-Patterns:

### Subagent-Strategie
- Subagents **liberal einsetzen**, um das Hauptkontextfenster sauber zu halten
- Forschung, Exploration und parallele Analyse an Subagents delegieren
- Für komplexe Probleme: **mehr Compute drauf werfen** via Subagents
- **Ein Task pro Subagent** für fokussierte Ausführung

### Autonomes Bug-Fixing
- Bei Bug-Reports: **Einfach fixen**, nicht nach Anleitung fragen
- Logs, Errors, fehlschlagende Tests finden → lösen
- Kein Context-Switching vom User nötig
- CI-Tests fixen ohne explizite Anweisung

---

## Claude Code – Spezialisierte Agent-Konfiguration `⭐ HOCH`

> Umgesetzt im dev-setup-template (`.claude/agents/`). Jeder Agent läuft isoliert im eigenen Kontextfenster.

### Konzept: Rollenbasierte Agents

Statt einen generischen Agenten alles machen zu lassen, bekommt jede Aufgabe einen **spezialisierten Agenten** mit eigenem Kontext und eigenen Constraints.

| Agent | Datei | Aufgabe |
|-------|-------|---------|
| Dev Agent | `dev-agent.md` | Feature-Implementierung, Refactoring, Bug Fixes |
| Test Agent | `test-agent.md` | Unit Tests (JUnit/Vitest), E2E (Playwright), Coverage |
| Review Agent | `review-agent.md` | Code Review – Security, Qualität, Konventionen |
| Docs Agent | `docs-agent.md` | API-Docs (OpenAPI), ADRs, Onboarding-Guides |

### Aufbau einer Agent-Config (.claude/agents/name.md)

```markdown
# Agent: [Name]

## Rolle
[Was dieser Agent ist und tut]

## Wann einsetzen
[Trigger-Beschreibung]

## Kontext den dieser Agent bekommt
[Was mitgegeben wird]

## Verhalten
[Wie der Agent arbeitet, was er nicht tut]

## Constraints
[Harte Grenzen]

## Output-Format
[Erwartetes Ergebnis-Format]
```

### Swarm-Pattern `⭐ HOCH`

Für große Aufgaben (z.B. neues Feature mit Backend + Frontend + Tests + Docs):

```
Runde 1 (parallel): Dev-Agent Backend + Dev-Agent Frontend
Runde 2 (parallel): Test-Agent + Docs-Agent
Runde 3:            Review-Agent prueft alles zusammen
```

- Command: `/swarm` zerlegt die Aufgabe und koordiniert die Agents
- Jeder Agent bekommt seinen isolierten Kontext + die passende Agent-Config
- Nie mehr als 5 parallele Agents (Qualität leidet)

### Session-Start-Regel
- `tasks/lessons.md` lesen → Fehler aus vergangenen Sessions nicht wiederholen
- In CLAUDE.md als Pflicht-Schritt verankern

### Wann Sub-Agents NICHT einsetzen
- Einfache Aufgaben (< 5 Schritte)
- Wenn Kontext zwischen Schritten weitergegeben werden muss
- Wenn Schritt-für-Schritt-Kontrolle gewünscht ist
