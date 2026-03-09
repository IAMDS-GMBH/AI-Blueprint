# Trainer-Leitfaden: 3-Tages KI-Schulung

## Ziel der Schulung
Entwickler lernen KI praktisch als Werkzeug im Entwickleralltag einzusetzen –
vom ersten KI-Chatbot über MCP-Server bis zur COBOL-Migration.

## Teilnehmer & Aufbau

**6 Personen – alle bauen dasselbe, einzeln:**
- Kein Team-Split mehr für die Übungen
- Jeder baut Backend + Frontend + MCP Server selbst
- Kreuzweiser Code-Review am Ende jedes Tages

---

## Tages-Struktur (gilt für alle 3 Tage)

**Effektive Arbeitszeit: 6 Stunden (ohne Mittagspause)**

```
09:00–09:15  Warm-up + Recap Vortag (entfällt Tag 1)        [15 min]
09:15–10:45  Theorie + Live-Demo (Trainer)                   [90 min]
10:45–11:00  Kurze Pause                                     [15 min]
11:00–12:00  Hands-on Start                                  [60 min]
12:00–12:45  Mittagspause                                    [45 min]
12:45–15:15  Hands-on weiter                                 [150 min]
15:15–15:45  Ergebnisse präsentieren + Retrospektive         [30 min]
─────────────────────────────────────────────────────────────────────
Effektiv:    6h 00min (ohne Mittagspause)
Hands-on:    3h 30min gesamt
```

**Harte 15:15 Uhr-Grenze einhalten.** OPTIONAL-Schritte nur wenn Zeit übrig.

---

## Technische Voraussetzungen (vor Schulungsbeginn prüfen)

### Jeder Teilnehmer benötigt:
- [ ] VS Code installiert
- [ ] GitHub Copilot aktiviert (GitHub-Account mit Copilot-Zugang)
- [ ] Claude Code CLI installiert (`npm install -g @anthropic-ai/claude-code`)
- [ ] **Anthropic API Key** (für Claude Code + ChatBot-Backend)
- [ ] Node.js 20+ installiert
- [ ] Java 17 (JDK) installiert
- [ ] Maven installiert
- [ ] Git installiert

### Tag 2 zusätzlich:
- [ ] **Oracle DB Zugang** (Host, Port, Service-Name, User, Password – vom Trainer)
- [ ] Oracle Instant Client auf den Teilnehmer-Rechnern (für `oracledb` npm Package)

### Tag 1 + 2 alternativ (kein API-Key vorhanden):
- [ ] **Ollama installiert** (`brew install ollama`) + Modell geladen (`ollama pull llama3.2`)
  - Claude Code nutzen mit: `claude config set apiBaseUrl http://localhost:11434/v1`

### Trainer benötigt zusätzlich:
- [ ] Projektor-Setup (Terminal + IDE gleichzeitig sichtbar)
- [ ] Dieses Repository geclont und lokal verfügbar
- [ ] Oracle DB mit Beispieldaten für Tag 2
- [ ] `db-setup.sql` für Oracle (falls Tag 1 Chatbot nicht fertig)

### API-Keys vorbereiten:
```bash
# Anthropic API Key (für Claude Code + Chatbot)
export ANTHROPIC_API_KEY=sk-ant-...

# GitHub Copilot: VS Code → Extensions → GitHub Copilot → Sign in
```

---

## Verteilung des Schulungsmaterials

### Alle Teilnehmer erhalten das vollständige Repo

```bash
# Trainer: Repo auf GitHub/GitLab pushen
# Teilnehmer: Repo klonen
git clone https://[url]/ai-knowledgebase.git
cd ai-knowledgebase
```

**Wichtig:** Das Repo enthält in `dev-setup-template/` die vollständige KI-Konfiguration
(CLAUDE.md, .claude/, .github/, .mcp.json). Die Teilnehmer nutzen dieses Setup
für ihre eigenen Übungs-Projekte.

### Was die Teilnehmer sehen

```
ai-knowledgebase/
  dev-setup-template/                  ← KI-Setup für Projekte (setup.sh + alle Configs)
  KI-Wissensbasis/            ← Nachschlagewerk
  Schulung/
    README-Trainer.md         ← NUR für Trainer (ausblenden oder Branch)
    Tag-1-Greenfield/         ← Theorie + Aufgaben Tag 1
    Tag-2-Coding-Assistant-MCP/ ← Theorie + Aufgaben + MCP-Starter
    Tag-3-Migration/          ← Theorie + COBOL-Beispiele
```

**Empfehlung:** `README-Trainer.md` in einem separaten Branch (`trainer-only`) ablegen.

### Was jeder Teilnehmer selbst erstellt (lokal, NICHT im Repo)

```
chatbot-backend/    ← Tag 1: Spring Boot + LLM API (mit setup.sh aus dev-setup-template)
chatbot-frontend/   ← Tag 1: Vue.js Chat-UI (mit setup.sh aus dev-setup-template)
```
Tag 2 und Tag 3: Arbeiten direkt mit den Dateien aus dem Schulungs-Repo.

### Setup-Workflow für Teilnehmer

Für jedes neue Übungs-Projekt:
```bash
mkdir chatbot-backend && cd chatbot-backend
bash ../ai-knowledgebase/dev-setup-template/setup.sh
# → KI-Setup ist drin. Dann CLAUDE.md + MEMORY.md projektspezifisch anpassen.
```

### Setup am ersten Morgen (alle, ~15 Min)

```bash
# 1. Repo klonen
git clone https://[url]/ai-knowledgebase.git
cd ai-knowledgebase

# 2. Claude Code installieren (falls noch nicht)
npm install -g @anthropic-ai/claude-code

# 3. API Key setzen
export ANTHROPIC_API_KEY=sk-ant-...

# 4. Claude Code testen
claude --version

# 5. VS Code Copilot prüfen
# Extensions → GitHub Copilot → Status = aktiv

# 6. setup.sh testen (im Chatbot-Projektordner)
mkdir ~/chatbot-test && cd ~/chatbot-test
bash ../ai-knowledgebase/dev-setup-template/setup.sh
ls .claude/   # → agents/ commands/ rules/ skills/ vorhanden?
```

---

## Übersicht: Was wird wann gelernt?

| Tag | Thema | Übung |
|-----|-------|-------|
| Tag 1 | Greenfield, CLAUDE.md, KERNEL, Plan Mode | KI-Chatbot (Spring Boot Backend + Vue.js Frontend) |
| Tag 2 (VM) | KI als Coding Assistant, Kontext-Hierarchie | Coding mit dev-setup-template, lessons.md |
| Tag 2 (NM) | MCP-Server bauen | Oracle MCP + Chatbot aus Tag 1 verbinden |
| Tag 3 (VM) | COBOL-Analyse mit KI | Synthetische Beispiele (payroll + inventory) |
| Tag 3 (NM) | COBOL-Migration zu Java | Eigenes COBOL-Modul des Unternehmens |

---

## Tag 1: KI-Chatbot – Trainer-Hinweise

### Zeitplan Tag 1
```
09:00–09:15  Warm-up: Erwartungen, Tool-Setup prüfen, setup.sh zeigen
09:15–10:45  Theorie (01-Theorie.md) + Live-Demo
10:45–11:00  Pause
11:00–12:00  Hands-on Start (Setup + Plan + DTOs)
12:00–12:45  Mittagspause
12:45–15:15  Hands-on weiter (LLM Service, Controller, Frontend, Verbinden)
15:15–15:45  Jeder zeigt seinen Chatbot live
```

### Theorie-Demo (09:15–10:45)
**Live zeigen (90 Min):**
1. `dev-setup-template/CLAUDE.md` öffnen – erklären was jede Sektion macht (15 Min)
2. `setup.sh` live ausführen – zeigen was kopiert wird (10 Min)
3. KERNEL-Framework: Schlechter Prompt vs. KERNEL-Prompt (20 Min)
4. `/plan` live zeigen (20 Min)
5. Chain-of-Verification demonstrieren (15 Min)
6. Agents + /review kurz zeigen (10 Min)

**Häufige Fragen:**
- *"Kann ich der KI vertrauen?"* → Nein blind, aber mit Verifikation ist die Fehlerrate niedrig
- *"Was wenn KI Mist baut?"* → /review zeigen, lessons.md erklären

### Hands-on-Begleitung
- Alle bauen `02-Aufgabe-Chatbot.md` durch
- Ziel: Lauffähiger Chatbot der auf LLM-Fragen antwortet
- Optionale Erweiterung: Markdown in Chat-Antworten rendern
- **Wenn jemand feststeckt:** "Welchen Fehler siehst du? → Zeig ihn der KI"
- **Häufiges Problem:** CORS-Fehler beim Verbinden → `WebMvcConfig` zeigen

---

## Tag 2: Coding Assistant + Oracle MCP – Trainer-Hinweise

### Zeitplan Tag 2
```
09:00–09:15  Warm-up + Recap Tag 1 (Chatbot zeigen)
09:15–10:45  Theorie: Coding Assistant + MCP
10:45–11:00  Pause
11:00–12:00  Hands-on Start (Starter-Code verstehen + Oracle-Connector)
12:00–12:45  Mittagspause
12:45–15:15  Hands-on weiter (MCP-Tools + Auth + Chatbot erweitern)
15:15–15:45  Demo: Chatbot beantwortet Oracle-Fragen live
```

### Theorie Tag 2 (09:15–10:45)
- 30 Min: KI ohne Kontext vs. mit CLAUDE.md (live vergleichen ohne Setup)
- 30 Min: Was ist MCP, wie funktioniert es, wofür nutzen wir es
- 30 Min: Live-Demo Playwright MCP + Oracle-Konzept erklären

### Oracle-DB Vorbereitung vor Hands-on
```bash
# Verbindung prüfen
sqlplus schulung_user/schulung_pass@//localhost:1521/SCHULUNGDB

# Beispieldaten einspielen falls nötig
# (Trainer: Oracle-equivalent von db-setup.sql bereitstellen)
```

**WICHTIG:** Oracle Instant Client muss auf allen Teilnehmer-Rechnern installiert sein.
Ohne das läuft `oracledb` npm Package nicht.

### Starter-Code
- `Tag-2-Coding-Assistant-MCP/mcp-starter/` – TypeScript-Vorlage
- Aktuell: PostgreSQL-Connector → Teams migrieren auf Oracle (das ist die Übung!)
- **Häufiges Problem:** `oracledb` Installation schlägt fehl → Instant Client Pfad prüfen

---

## Tag 3: COBOL-Migration – Trainer-Hinweise

### Zeitplan Tag 3
```
09:00–09:20  Warm-up + Recap Tag 2 (MCP-Demo)
09:20–10:50  Theorie: Migration, Business Case, COBOL-Basics
10:50–11:00  Pause
11:00–12:00  Live Demo: payroll.cbl analysieren + migrieren
12:00–12:45  Mittagspause
12:45–14:15  Hands-on Warm-up (payroll + inventory)
14:15–15:30  Hauptübung: Eigenes COBOL
15:30–16:00  3-Tages-Abschluss + Lessons Learned
```

### Vorbereitung durch Teilnehmer (vor Tag 3 mitteilen)
- 1 COBOL-Modul aus dem Unternehmen mitbringen (anonymisiert)
- **Richtwert: max. 100 Zeilen** für 1,5 Stunden Hauptübung
- Bekannte Test-Inputs + erwartete Outputs

### Theorie Tag 3 (09:20–10:50)
1. Business Case: Warum modernisieren (20 Min)
2. Migrationsworkflow: 5 Schritte (20 Min)
3. COBOL-Basics für Nicht-COBOL-Entwickler (25 Min)
4. Strangler Fig + Swarm-Überblick (25 Min)

**Typische KI-Fehler bei COBOL (zwingend erwähnen):**
- `PIC 9(7)V99` → NIEMALS `double` → immer `BigDecimal`
- `PERFORM UNTIL` = `while`, nicht `for-each`
- Level-Numbers → verschachtelte Klassen/Records

---

## Häufige Probleme & Lösungen

| Problem | Lösung |
|---------|--------|
| KI antwortet auf Englisch | Prompt auf Deutsch + "Antworte immer auf Deutsch" in CLAUDE.md |
| setup.sh läuft nicht | `chmod +x dev-setup-template/setup.sh` oder direkt `bash setup.sh` |
| ANTHROPIC_API_KEY nicht gesetzt | `export ANTHROPIC_API_KEY=sk-ant-...` im Terminal |
| CORS-Fehler Frontend→Backend | `WebMvcConfig` mit `localhost:5173` allowedOrigins prüfen |
| oracledb Installation schlägt fehl | Oracle Instant Client Pfad in `.npmrc` oder ENV setzen |
| MCP-Server antwortet nicht | `.mcp.json` prüfen, Port-Konflikte, Server-Logs |
| COBOL-Migration hat Logikfehler | Chain-of-Verification + BigDecimal erzwingen |
| KI generiert nicht-kompilierenden Code | "Kompiliere und repariere jeden Fehler" als Follow-up |

---

## Material-Übersicht

```
Schulung/
  README-Trainer.md                     ← Diese Datei (nur Trainer)
  Tag-1-Greenfield/
    01-Theorie.md                        ← Theorie
    02-Aufgabe-Chatbot.md               ← Hauptübung (alle bauen beides)
    02-Aufgabe-Backend.md               ← Detailreferenz Backend (optional)
    02-Aufgabe-Frontend.md              ← Detailreferenz Frontend (optional)
  Tag-2-Coding-Assistant-MCP/
    01-Theorie-Coding-Assistant.md
    01-Theorie-MCP.md
    02-Aufgabe-MCP-Server.md            ← Oracle MCP + Chatbot verbinden
    mcp-starter/                        ← TypeScript Starter-Code
  Tag-3-Migration/
    01-Theorie.md
    02-Aufgabe-Migration.md
    cobol-beispiele/
dev-setup-template/                              ← KI-Setup das alle Teilnehmer nutzen
  setup.sh                             ← Einzeilen-Setup für Projekte
  CLAUDE.md, MEMORY.md                 ← Templates
  .claude/, .github/, .mcp.json        ← KI-Konfiguration
```
