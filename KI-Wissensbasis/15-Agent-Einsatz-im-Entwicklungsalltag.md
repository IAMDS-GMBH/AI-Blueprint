# 15 – Agent-Einsatz im Entwicklungsalltag

> Wie Agents konkret eingesetzt werden, wie sie zusammenspielen, und wie ein
> vollstaendiger KI-gestuetzter Entwicklungs-Workflow aussieht.

---

## 1. Was ist ein Agent – Kurzdefinition

Ein **Agent** ist eine KI-Instanz mit:
- Eigenem isoliertem Kontextfenster (sieht nur was er braucht)
- Spezifischer Rolle und Constraints (macht nur seinen Job)
- Faehigkeit Werkzeuge zu nutzen (Dateien lesen/schreiben, Bash, Web, MCP)
- Einem definierten Output-Format

Der Unterschied zum normalen Chat-Prompt:
```
Chat-Prompt:  KI weiss alles, macht alles, liefert Text
Agent:        KI hat Rolle + Tools + Context + handelt autonom bis zu einem Ziel
```

---

## 2. Agents in Claude Code

### Wie Agents definiert werden
Agenten-Konfigurationen liegen in `.claude/agents/`:
```
.claude/agents/
  dev-agent.md         # Feature-Implementierung, Bug Fixes
  test-agent.md        # Unit Tests, E2E Tests
  review-agent.md      # Code Review
  docs-agent.md        # Dokumentation
  knowledge-agent.md   # KI-Wissensbasis Fragen
```

Jede Datei beschreibt:
- **Rolle** – was dieser Agent ist
- **Wann einsetzen** – in welchen Situationen
- **Kontext** – welche Dateien/Informationen er bekommt
- **Verhalten** – wie er arbeitet (autonom, rueckfragearm, ...)
- **Constraints** – was er nicht tun darf
- **Output-Format** – wie das Ergebnis aussieht

### Wie man einen Agent aufruft

**Option 1: Natuerliche Sprache (implizit)**
Claude Code erkennt den passenden Agent anhand der Aufgabe:
```
"Implementiere einen UserService mit CRUD-Methoden"
→ Claude Code startet intern den Dev Agent
```

**Option 2: Task-Delegation (explizit)**
Im Claude Code Chat direkt einen Sub-Agent starten:
```
"Nutze den Test Agent um JUnit 5 Tests fuer UserService zu schreiben"
```

**Option 3: /swarm Command**
Fuer grosse Aufgaben die parallel abgearbeitet werden sollen:
```
/swarm
"Migriere alle drei COBOL-Module gleichzeitig:
 - Modul 1: payroll.cbl → Java PayrollService
 - Modul 2: inventory.cbl → Java InventoryService
 - Modul 3: orders.cbl → Java OrderService
Jedes Modul bekommt seinen eigenen Agent."
```

**Option 4: Knowledge Agent**
Fuer Fragen an die interne Wissensbasis:
```
"Was ist der beste Weg einen MCP Server zu bauen? Schau in der Wissensbasis nach."
→ Knowledge Agent liest KI-Wissensbasis und antwortet
```

### Sub-Agent vs. Direkte Aufgabe: Wann was?

| Situation | Empfehlung |
|-----------|-----------|
| Klare, einfache Implementierungsaufgabe | Direkt, kein Sub-Agent |
| Aufgabe braucht Research (Bibliotheken, Docs) | Sub-Agent (Hauptkontext sauber halten) |
| 3+ parallele unabhaengige Tasks | /swarm |
| Task > 30 Minuten geschaetzte Arbeit | Aufteilen, je 1 Agent pro Teil |
| Code Review vor PR-Merge | Review Agent explizit aufrufen |
| Frage zu KI-Tools oder Workflows | Knowledge Agent |

### Automatisch vs. Manuell: Wann springt ein Agent von selbst ein?

**Claude Code (.claude/agents/):**
- Agents werden **automatisch** ausgewaehlt – Claude liest alle `.md`-Dateien in `.claude/agents/` und entscheidet anhand der `## Wann einsetzen`-Sektion welcher Agent passt
- Optional: `description:`-Frontmatter hinzufuegen fuer noch praeziseres Auto-Matching
- Expliziter Aufruf auch moeglich: *"Nutze den Review Agent fuer..."*

**Skills (.claude/skills/):**
- Werden ebenfalls **automatisch** geladen – ueber den `description:`-Wert im Frontmatter
- Claude laedt den Skill wenn die Anfrage inhaltlich dazu passt

**GitHub Copilot (.github/agents/):**
- Copilot startet Agents **nie automatisch** – immer explizit ueber `@AgentName`
- Das Frontmatter (`name:`, `description:`, `tools:`) ist Pflicht damit Copilot den Agent kennt

**Gegenueberstellung:**

| | Claude Code Agents | Claude Code Skills | Copilot Agents |
|---|---|---|---|
| Trigger | Automatisch (kontextbasiert) | Automatisch (description-Match) | Manuell via `@Name` |
| Konfiguration noetig? | Nein – Dateisystem reicht | `description:` im Frontmatter | Frontmatter Pflicht |
| Explizit aufrufbar? | Ja | Ja (Skill-Name erwaehnen) | Ja (`@AgentName`) |

**Faustregel:**
```
Mehr Kontrolle gewuenscht?  → Kein description:-Frontmatter → Agent nur auf Anfrage
Maximale Automatisierung?   → description: hinzufuegen → Claude entscheidet selbst
Copilot?                    → Immer explizit mit @AgentName
```

---

## 3. Agents in GitHub Copilot

### Custom Agents (@AgentName)
Copilot Custom Agents liegen in `.github/agents/`:
```
.github/agents/
  dev-agent.md          # @DevAgent
  review-agent.md       # @ReviewAgent
  test-agent.md         # @TestAgent
  docs-agent.md         # @DocsAgent
  knowledge-agent.md    # @KnowledgeAgent
```

### Wie man einen Copilot Agent aufruft
Im Copilot Chat (VS Code Seitenleiste, Ctrl+Shift+I):
```
@DevAgent Implementiere einen UserService mit CRUD
@ReviewAgent Prüfe meinen letzten Commit auf Security-Issues
@TestAgent Schreibe JUnit 5 Tests fuer UserController
@KnowledgeAgent Was ist das KERNEL Framework?
```

### Copilot Inline vs. Agent Mode

| Modus | Wie aufrufen | Wann |
|-------|-------------|------|
| **Inline Completion** | Einfach tippen, KI vervollstaendigt | Einzelne Zeilen, bekannte Patterns |
| **Inline Chat** | Ctrl+I im Editor | Bestehenden Code erklaeren/aendern |
| **Chat (Panel)** | Ctrl+Shift+I | Groessere Diskussionen, Planung |
| **@Agent** | `@AgentName` im Chat | Spezialisierte Aufgaben |

---

## 4. Der vollstaendige Entwicklungs-Workflow mit KI

Wie alle Elemente zusammenspielen – von Requirement bis Deployment:

```
Schritt 1: PLANUNG (Plan Mode / /plan)
────────────────────────────────────
User: "Neues Feature: Passwort-Reset per E-Mail"
  ↓
Claude Code / Copilot:
  → Liest CLAUDE.md fuer Stack-Kontext
  → Erstellt Plan in tasks/todo.md
  → User approves Plan
  ↓
Ergebnis: tasks/todo.md mit konkreten Steps

Schritt 2: IMPLEMENTIERUNG (Dev Agent)
────────────────────────────────────
Dev Agent:
  → Liest todo.md + betroffene Dateien
  → Implementiert Entity → Service → Controller
  → Nutzt dev-standards Skill fuer Code-Konventionen
  → Schreibt Tests automatisch
  ↓
Ergebnis: Fertige Implementierung + Tests

Schritt 3: REVIEW (Review Agent)
────────────────────────────────────
/review  (oder @ReviewAgent fuer Copilot)
  → Analysiert git diff
  → Prueft Security, Quality, Tests, Conventions
  → Liefert BLOCKER vs. Empfehlung
  ↓
Ergebnis: Review-Bericht mit konkreten Aenderungen

Schritt 4: DOKUMENTATION (Docs Agent)
────────────────────────────────────
Docs Agent:
  → Generiert OpenAPI-Dokumentation
  → Erstellt ADR falls Architekturentscheidung
  ↓
Ergebnis: Aktuelle API-Docs

Schritt 5: LERNSCHLEIFE (Lessons Learned)
────────────────────────────────────
Nach dem Feature:
  → tasks/lessons.md aktualisieren
  → KI-Wissensbasis aktualisieren falls neue KI-Erkenntnisse
  → Naechste Session startet mit Lessons-Read
```

---

## 5. Wie MCP, Skills, Commands und Agents zusammenspielen

```
                    ┌─────────────────────────────────┐
                    │         CLAUDE.md / Copilot      │
                    │    copilot-instructions.md       │
                    │   (Unternehmens-Kontext, Stack)  │
                    └──────────────┬──────────────────┘
                                   │ laedt
              ┌────────────────────┼───────────────────────┐
              │                    │                       │
     ┌────────▼────────┐  ┌────────▼────────┐  ┌──────────▼────────┐
     │    SKILLS       │  │    COMMANDS     │  │    MCP-SERVER     │
     │  (on-demand)    │  │  (/ shortcuts)  │  │   (Werkzeuge)     │
     ├─────────────────┤  ├─────────────────┤  ├───────────────────┤
     │ dev-standards   │  │ /plan           │  │ playwright        │
     │ frontend-design │  │ /review         │  │ github            │
     │ document-       │  │ /swarm          │  │ postgres          │
     │   creation      │  │ /ralph ◄ NEU   │  │ context7 ◄ NEU   │
     │ ceo-brief       │  │ /brief          │  │ drawio            │
     │ agent-          │  │ /automate       │  │ figma             │
     │   orchestration │  └─────────────────┘  └───────────────────┘
     └─────────────────┘
              │                    │                       │
              └────────────────────▼───────────────────────┘
                                   │ alle gebuendelt in
                    ┌──────────────▼──────────────────┐
                    │           AGENTS                │
                    │  dev / test / review / docs /   │
                    │  knowledge                      │
                    └─────────────────────────────────┘
```

**Skills** = Wissen und Regeln die bei Bedarf in den Kontext geladen werden
**Commands** = Vordefinierte Workflows (plan → approve → execute)
**MCP** = Externe Werkzeuge (Browser, DB, GitHub, Design) die der Agent nutzen kann
**Agents** = Kombinieren Skills + Commands + MCP in einer spezialisierten Rolle

---

## 6. Praktische Einstiegs-Szenarien

### Szenario A: Bug Fix
```
1. Bug beschreiben: "UserService.findByEmail wirft NPE wenn E-Mail null ist"
2. Dev Agent: Root Cause finden → Fix implementieren → Test schreiben
3. /review: Prueft ob Fix korrekt und vollstaendig
4. lessons.md: "Null-Checks bei allen Service-Eingabes pflichtend"
```

### Szenario B: Neues Feature
```
1. /plan "Passwort-Reset per E-Mail implementieren"
2. Plan approven
3. Dev Agent: Entity → Service → Controller → Tests
4. /review: Code Review
5. Docs Agent: OpenAPI aktualisieren
```

### Szenario C: Unbekanntes Thema
```
1. @KnowledgeAgent "Wie integriere ich Redis als Cache in Spring Boot?"
2. Knowledge Agent liest interne Wissensbasis, ergaenzt mit Web
3. Antwortet mit konkretem Beispiel fuer unseren Stack
4. Neue Erkenntnisse werden in KI-Wissensbasis gespeichert
```

### Szenario D: Grosse Migration (Swarm)
```
1. /swarm "Migriere alle 5 Legacy-Module nach Java Spring Boot"
2. 5 parallele Dev-Agents (je 1 pro Modul)
3. Review Agent prueft alle Ergebnisse
4. Integration und Tests
```

---

## 7. Ralph – Autonomes Iteratives Feature-Development `🔵 MITTEL`

> **Repo:** https://github.com/snarktank/ralph
> Framework für autonome KI-Agents die iterativ Features entwickeln bis alles grün ist.

**Das Prinzip:**
```
Klassisch:  Ein langer Chat → Kontext wird voll → Qualität sinkt
Ralph:      Jede Iteration = neuer frischer Kontext → Qualität bleibt konstant
```

**Wie Ralph arbeitet:**
1. Requirements in `prd.json` definieren (priorisierte Task-Queue)
2. Ralph wählt nächste Aufgabe aus der Queue
3. Generiert Code, führt Tests aus, prüft Qualität
4. Bei Fehler: analysiert, korrigiert, iteriert
5. Bei Erfolg: committet, markiert Task done, nächste Aufgabe
6. `AGENTS.md` dokumentiert Fortschritt und Entscheidungen

**Features:**
- Fresh Context per Iteration (verhindert Token-Bloat)
- Priority-Queue aus Requirements
- Automatische Quality Checks (Types, Tests)
- Progress Tracking via git history + `progress.txt`
- Kompatibel mit Amp CLI oder Claude Code

**Installation:**
```bash
# Als Claude Code Plugin (global, alle Projekte)
/plugin install ralph@anthropic-agent-skills
```

**Wann sinnvoll:**
```
✓ Große Features die in viele kleine Tasks aufgeteilt werden können
✓ Wenn der Hauptkontext immer voll wird (komplexe Codebases)
✓ COBOL-Migration: jedes Modul = autonome Ralph-Session
✗ Einfache, kurze Tasks (Overhead nicht lohnt sich)
✗ Tasks die starke menschliche Überprüfung brauchen
```

**Verbindung zu unserer Schulung:** Ralph ist das Automatisierungs-Pattern für Tag 3 – die COBOL-Migration könnte exakt so ablaufen.

---

## 8. Haeufige Fehler und wie man sie vermeidet

| Fehler | Besser |
|--------|--------|
| Alles in einem langen Chat-Thread | Agents fuer Teilaufgaben nutzen |
| KI entscheiden lassen ob Review noetig | Immer /review vor PR-Merge |
| Neue Feature ohne /plan | Erst planen, dann ausfuehren |
| Fehler ignorieren, naechste Aufgabe | lessons.md updaten |
| Gleiche Frage immer neu stellen | In KI-Wissensbasis dokumentieren |
| Sub-Agent fuer triviale 1-Zeilen-Fixes | Direkt im Hauptchat reicht |

---

## 8. Konfiguration pruefen

Bevor Agents eingesetzt werden – diese Dateien sollten vorhanden sein:

**Claude Code:**
```
.claude/agents/       # Agenten-Definitionen
.claude/commands/     # Slash Commands (/plan, /review, /swarm, /ralph)
.claude/skills/       # Skills (dev-standards, frontend-design, document-creation)
.claude/rules/        # Pfad-spezifische Regeln (java-spring, vue-frontend, db-migrations)
.claude/settings.json # Permissions + enabledPlugins (Plugin Marketplace)
.mcp.json             # MCP-Server (playwright, github, postgres, context7, figma, drawio)
CLAUDE.md             # Unternehmens-Kontext
tasks/lessons.md      # Team-Learnings
```

**GitHub Copilot:**
```
.github/agents/                       # Custom @Agents (DevAgent, TestAgent, ReviewAgent, ...)
.github/instructions/                 # File-type Instructions (applyTo: *.java)
.github/copilot-instructions.md       # Basis-Konfiguration + Security + Ralph-Loop-Verhalten
.vscode/mcp.json                      # MCP fuer VS Code (gleiche Server wie .mcp.json)
```
