# 09 – Automatisierung & Workflows

> Workflow-Automatisierung, Orchestrierung und Integration von KI in Business-Prozesse.

---

## n8n + KI `⭐ HOCH`

### Claude Opus für n8n Workflows
- **Frage:** Wie nutzt man Claude Opus zum Bauen von n8n Workflows?
- **Ansatz:**
  1. Claude Opus als "Workflow-Designer" nutzen – Beschreibe den gewünschten Workflow in natürlicher Sprache
  2. Claude generiert die n8n-JSON-Konfiguration
  3. Import in n8n und Feintuning
- **Aktion:** [ ] Proof of Concept: Einen bestehenden manuellen Workflow mit Claude automatisieren

### N8N zu Python Code mit GraphRAG `🔵 MITTEL`
- **Konzept:** n8n-Workflows in Python-Code konvertieren, ergänzt um GraphRAG
- **Warum relevant:** 
  - Python-Code ist versionierbar und testbar
  - GraphRAG ermöglicht intelligente Wissensverknüpfung
  - Kombination aus Automatisierung + Wissensmanagement
- **Aktion:** [ ] Evaluieren: Welche n8n-Workflows würden von GraphRAG profitieren?

---

## Workflow-Architektur Patterns 📸

### Aus den angehängten Bildern – Core Principles:

**Simplicity First:**
- Jede Änderung so einfach wie möglich
- Minimaler Code-Impact
- Nicht über-engineeren

**No Laziness:**
- Root Causes finden
- Keine temporären Fixes
- Senior-Developer-Standards

**Minimal Impact:**
- Nur das Nötige anfassen
- Keine neuen Bugs einführen

### Task Management Pattern:
```
1. Plan schreiben (tasks/todo.md)
2. Plan verifizieren lassen
3. Fortschritt tracken
4. Änderungen erklären
5. Ergebnisse dokumentieren
6. Lessons Learned erfassen
```

→ Ausführliche Details in [06-Claude-Code-Workflow.md](06-Claude-Code-Workflow.md)

---

## Google Opal `🔵 MITTEL`
- **Was:** Low-Code-Basis für AI Workflows
- **Warum relevant:** Business-User können KI-Workflows ohne Coding erstellen
- → Details in [07-Google-KI-Oekosystem.md](07-Google-KI-Oekosystem.md)

---

## Automatisierungs-Maturity-Modell

```
Level 1: Manuell           → Mensch macht alles
Level 2: KI-unterstützt    → KI schlägt vor, Mensch entscheidet
Level 3: KI-orchestriert   → KI führt aus, Mensch verifiziert
Level 4: KI-autonom         → KI arbeitet selbständig, Mensch überwacht
Level 5: Multi-Agent        → Agent-Schwärme, Mensch setzt Ziele
```

**Unsere Zielposition:** Level 3-4 für Routine-Tasks, Level 2-3 für kritische Aufgaben
