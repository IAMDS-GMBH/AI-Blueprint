# 06 – Claude Code Workflows

> Optimale Arbeitsabläufe und Patterns für die produktive Nutzung von Claude Code (und übertragbar auf GitHub Copilot).

---

## Der optimale Workflow 📸 `⭐ HOCH`

### Paralleles Arbeiten
1. **Mehrere Claude-Tabs parallel** öffnen für verschiedene Tasks
2. **Browser-Agents** für längere, weniger komplexe Aufgaben nutzen
3. **Claude Code Mobile App** um von unterwegs Input an laufende Sessions zu senden
4. **Shared CLAUDE.md** für konsistente Konfiguration über alle Sessions

### Plan Mode → Execution `⭐ HOCH`
1. **Guten Plan im Plan Mode erstellen** (detaillierte Spezifikation)
2. Dann **One-Shot mit Auto-Accept** ausführen
3. Agent arbeitet den Plan selbstständig ab

### Fehler-Lernschleife
- Wenn Claude etwas **falsch macht** → **sofort in die .md-Datei** aufnehmen
- Claude in PRs **taggen**, damit Lessons gelernt werden
- Dokumentation-GitHub für Docs on Commits nutzen

### Sub-Agents für Automatisierung
- **Detaillierte CLAUDE.md-Files** pro Sub-Agent
- Sub-Agents für automatische Routinen:
  - PR-Erstellung & Review
  - Test-Ausführung
  - Refactoring
- Auslösung über **Slash Commands** in `.claude/commands/`

### Verifikationsprozess `⭐ HOCH`
- **Claude Chrome Extension** nutzen, damit Claude seine eigene Arbeit verifiziert
- Für **jede Domäne einen Verifikationsprozess** definieren
- Claude diesen Prozess automatisch ausführen lassen
- Bei längeren Tasks: Agent beauftragen, seine Arbeit **selbst mit einem Sub-Agent zu verifizieren**
- Permission Mode `dontAsk` damit der Agent ungestört arbeiten kann

---

## Workflow Orchestration Patterns 📸 `⭐ HOCH`

> Aus den angehängten Workflow-Orchestration-Bildern extrahiert:

### 1. Plan Mode Default
- **Plan Mode für JEDEN nicht-trivialen Task** (3+ Schritte oder Architektur-Entscheidungen)
- Wenn etwas schiefgeht: **STOPP, neu planen** – nicht weiter pushen
- Plan Mode für **Verifikationsschritte** nutzen, nicht nur zum Bauen
- **Detaillierte Specs vorab** schreiben um Ambiguität zu reduzieren

### 2. Subagent-Strategie
- Subagents **liberal einsetzen** um den Hauptkontext sauber zu halten
- Forschung, Exploration und parallele Analyse auslagern
- Für komplexe Probleme: Mehr Compute via Subagents
- **Ein Task pro Subagent** für fokussierte Ausführung

### 3. Self-Improvement Loop
- Nach JEDER Korrektur vom User: `tasks/lessons.md` updaten
- Regeln schreiben die denselben Fehler verhindern
- **Ruthlessly iterieren** bis die Fehlerrate sinkt
- Lessons zu Beginn jeder Session reviewen

### 4. Verification Before Done
- **Nie eine Aufgabe als fertig markieren** ohne Beweis dass sie funktioniert
- Diff zwischen main und Changes prüfen
- Frage: *"Would a staff engineer approve this?"*
- Tests laufen lassen, Logs checken, Korrektheit demonstrieren

### 5. Demand Elegance (Balanced)
- Bei nicht-trivialen Changes: *"Is there a more elegant way?"*
- Wenn ein Fix hacky wirkt: Die elegante Lösung implementieren
- Bei einfachen, offensichtlichen Fixes: **Nicht over-engineeren**
- Eigene Arbeit challengen bevor man sie präsentiert

### 6. Autonomous Bug Fixing
- Bei Bug-Reports: **Einfach fixen**, nicht nach Anleitung fragen
- Logs, Errors, fehlschlagende Tests finden → dann lösen
- Zero Context-Switching vom User nötig
- CI-Tests fixen ohne explizite Anweisung

---

## Task Management 📸

1. **Plan First:** Plan in `tasks/todo.md` mit checkbaren Items schreiben
2. **Verify Plan:** Check-in vor der Implementation
3. **Track Progress:** Items abhaken während der Arbeit
4. **Explain Changes:** High-Level Summary bei jedem Schritt
5. **Document Results:** Review-Section zu `tasks/todo.md` hinzufügen
6. **Capture Lessons:** `tasks/lessons.md` nach Korrekturen updaten

---

## Core Principles 📸

- **Simplicity First:** Jede Änderung so einfach wie möglich. Minimaler Code-Impact.
- **No Laziness:** Root Causes finden. Keine temporären Fixes. Senior-Developer-Standards.
- **Minimal Impact:** Nur das Nötige anfassen. Keine Bugs einführen.

---

## Built-in Commands & Features `⭐ HOCH`

> Was Claude Code out-of-the-box liefert – ohne Setup.

### Slash Commands (sofort nutzbar)

| Command | Was es macht | Copilot-Äquivalent |
|---------|-------------|-------------------|
| `/plan` | Erstellt einen Plan vor der Umsetzung – keine Änderungen ohne Freigabe | `"Plan first, then implement"` als Prompt-Prefix |
| `/review` | Code-Review des aktuellen Diffs (Skill) | GitHub PR Review / `@workspace review this code` |
| `/commit` | Analysiert Änderungen, schreibt commit message, commited | Manuell im Source-Control-Panel |
| `/swarm` | Startet parallele Sub-Agents für große Aufgaben | Kein direktes Äquivalent |
| `/loop [interval] [cmd]` | Führt einen Command wiederholt aus (z.B. alle 5 Minuten) | GitHub Actions für Automatisierung |
| `/init` | Initialisiert CLAUDE.md für das aktuelle Projekt | Manuell anlegen |
| `/compact` | Komprimiert den Kontext wenn er zu voll wird | Neuen Chat starten |
| `/clear` | Löscht den aktuellen Kontext komplett | Neuen Chat starten |
| `/ralph` | Iterativer Selbst-Verifikations-Loop – prüft Output gegen 6 Qualitätskriterien | In copilot-instructions.md als Verhaltensregel eingebaut |

### Hintergrund-Features (automatisch aktiv)

| Feature | Was es macht | Copilot-Äquivalent |
|---------|-------------|-------------------|
| **MEMORY.md** | Projektgedächtnis – wird bei jeder Session automatisch geladen | Manuell in `copilot-instructions.md` pflegen |
| **Auto-Accept Mode** | Agent arbeitet Änderungen ohne Rückfrage ab | Agent Mode mit Trust-Settings |
| **Parallele Tool-Calls** | Liest mehrere Dateien gleichzeitig statt sequenziell | Eingeschränkt verfügbar |
| **Worktree Isolation** | Sub-Agent arbeitet in eigenem Git-Branch – kein Risiko für main | Nicht verfügbar |
| **Hooks** | Shell-Kommandos die vor/nach Tool-Calls ausgeführt werden | Nicht verfügbar |
| **MCP Integration** | Verbindet KI mit externen Tools (DB, APIs, etc.) | MCP in VS Code Settings |
| **TodoWrite** | KI pflegt automatisch eine Todo-Liste während der Arbeit | Manuell |

### Konfigurierbare Features (.claude/)

| Datei/Ordner | Was es macht | Copilot-Äquivalent |
|---|---|---|
| `.claude/agents/` | Spezialisierte Sub-Agents (Dev, Test, Review, Docs) | `.github/agents/` |
| `.claude/commands/` | Eigene Slash Commands definieren | `.github/copilot-prompts/` |
| `.claude/skills/` | Wiederverwendbare Prompts on-demand laden | Custom Instructions |
| `.claude/settings.json` | Permissions, erlaubte Tools, Verhalten konfigurieren | VS Code Settings |

### CoWork – Claude Desktop (Autonomous Knowledge Work) `🔵 MITTEL`

> Feature für Claude Desktop (Pro/Max/Team/Enterprise) – kein Developer-Tool

- **Was:** Autonome Multi-Step-Aufgaben direkt auf dem lokalen Rechner
- **Zugriff:** Lokale Dateien ohne Upload/Download – Claude liest/schreibt direkt
- **Outputs:** Excel mit Formeln, PowerPoint, Word-Dokumente, Reports
- **Sub-Agents:** Parallelisierung von Workflows automatisch
- **Für wen:** Knowledge Worker (kein Coding nötig) – Datenanalyse, Recherche, Dokumentation
- **Link:** https://support.claude.com/de/articles/13345190-erste-schritte-mit-cowork

---

## Autonomes Feature-Development – Feature-Liste übergeben `⭐ HOCH`

> Das mächtigste Muster: KI bekommt Requirements → baut alles selbst durch → meldet fertig.

### Pattern 1: Single Feature (schnell, direkt)

Wenn ein Feature klar definiert ist, einfach vollständig beschreiben:

```
Baue mir den kompletten [Feature-Name]:

Requirements:
- [Endpoint / UI / Verhalten konkret beschreiben]
- [Datenmodell / Felder]
- [Business Rules]

Stack: [aus CLAUDE.md]
Konventionen aus CLAUDE.md gelten.
Arbeite autonom bis alles fertig ist. Status am Ende.
```

**Schlüssel:** Konkrete Requirements (Endpunkte, Felder, Regeln) → Claude braucht keine Rückfragen.

---

### Pattern 2: Feature-Liste (prd.md → sequenziell)

Für mehrere Features — als Datei anlegen, dann übergeben:

**`tasks/prd.md` anlegen:**
```markdown
# Product Requirements

## Feature 1: Passwort-Reset (Priorität: HOCH)
- POST /api/v1/auth/forgot-password → E-Mail mit Reset-Token
- POST /api/v1/auth/reset-password → Token validieren, Passwort setzen
- Token läuft nach 24h ab, einmalig verwendbar

## Feature 2: User-Profil (Priorität: MITTEL)
- GET /api/v1/users/me → aktueller User
- PUT /api/v1/users/me → Name und Avatar ändern
- FrontEnd: ProfileView.vue (Composition API)

## Feature 3: Audit-Log (Priorität: NIEDRIG)
- Login-Events in AUDIT_LOG Tabelle schreiben
- Flyway-Migration inklusive
```

**Dann übergeben:**
```
Lies tasks/prd.md.
Baue alle Features sequenziell nach Priorität.
Nach jedem Feature: /ralph zur Qualitätsprüfung.
Halte tasks/todo.md aktuell.
Fang an.
```

---

### Pattern 3: Parallel-Swarm (für große Feature-Pakete)

Wenn Features voneinander unabhängig sind:

```
/swarm
Lies tasks/prd.md.
Feature 1 und Feature 2 sind unabhängig → parallel bauen.
Feature 3 braucht Feature 1 → danach.
Review-Agent prüft alle am Ende.
```

**Ergebnis:** Mehrere Claude-Subagents arbeiten gleichzeitig — jeder an seinem Feature.

---

### Pattern 4: Copilot (manuell, aber strukturiert)

Copilot hat kein `/swarm`, aber dieselbe Logik funktioniert manuell:

```
@DevAgent
Lies tasks/prd.md.
Implementiere Feature 1: Passwort-Reset.
Requirements: [konkret beschreiben]
Wenn fertig: kurze Zusammenfassung was gebaut wurde.
```
→ Dann `@DevAgent` für Feature 2 aufrufen.
→ Am Ende: `@ReviewAgent` für den Gesamt-Review.

---

### Wann welches Pattern?

| Situation | Pattern |
|-----------|---------|
| 1 klar definiertes Feature | Pattern 1 – direkt beschreiben |
| 3-10 Features, aufeinander aufbauend | Pattern 2 – prd.md sequenziell |
| 3+ Features, unabhängig voneinander | Pattern 3 – /swarm parallel |
| GitHub Copilot | Pattern 4 – @DevAgent manuell |
| COBOL-Migration (Tag 3!) | Pattern 3 – je Modul ein Agent |

---

### Der Schlüssel zum autonomen Arbeiten

```
Vage:    "Baue mir eine Login-Funktion"
         → KI fragt nach, stoppt, wartet

Konkret: "POST /api/v1/auth/login
          Body: { email: string, password: string }
          Response: { token: string, expiresIn: 3600 }
          Fehler: 401 bei falschen Credentials
          JWT mit 1h Ablauf, BCrypt für Passwort-Vergleich"
         → KI baut durch ohne Unterbrechung
```

**Faustregel:** Wenn ihr die Requirements selbst nicht klar formulieren könnt → erst `/plan` nutzen, Claude lässt euch die Lücken finden.

---

## GitHub Copilot Übertragbarkeit

| Claude Code Feature | Copilot Äquivalent | Parität |
|--------------------|--------------------|---------|
| CLAUDE.md | copilot-instructions.md | ✅ Gut |
| .claude/commands/ | .github/copilot-prompts/ | ✅ Gut |
| Sub-Agents (.claude/agents/) | .github/agents/ + `@AgentName` | 🟡 Manueller Aufruf |
| MCP (.mcp.json) | MCP in VS Code Settings | ✅ Gut |
| Plan Mode (/plan) | "Plan first, then implement" als Prompt | 🟡 Manuell |
| Auto-Accept | Agent Mode mit Trust-Settings | 🟡 Eingeschränkt |
| MEMORY.md | Manuell in copilot-instructions.md | 🔴 Kein Auto-Update |
| /swarm | Kein Äquivalent | 🔴 Nicht verfügbar |
| /loop | GitHub Actions | 🔴 Anderes Tool |
| Hooks | Kein Äquivalent | 🔴 Nicht verfügbar |
| Worktree Isolation | Kein Äquivalent | 🔴 Nicht verfügbar |
