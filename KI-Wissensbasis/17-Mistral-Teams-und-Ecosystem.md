# 17 – Mistral Teams & Ecosystem

> **Prioritaet:** ⭐ HOCH
> **Letzte Aktualisierung:** 2026-03-10
> **Status:** Aktiv – Mistral AI als europaeische KI-Plattform mit CLI-Agent, IDE-Integration und Team-Lizenz

---

## Ueberblick

Mistral AI ist ein franzoesisches KI-Unternehmen, das eine vollstaendige Plattform fuer Entwicklerteams bietet: von Coding-Modellen ueber einen CLI-Agent (Vibe) bis hin zu Team-Lizenzen mit Admin-Console. Als europaeischer Anbieter kann Mistral fuer DSGVO- und Datenhoheit-Anforderungen besonders relevant sein.

- **Teams-Lizenz:** 25 EUR/User/Monat (annual), 30 EUR/User/Monat (monthly)
- **CLI-Agent:** Mistral Vibe (Open Source, MIT/Apache 2.0)
- **Chat-Interface:** Le Chat (chat.mistral.ai)
- **Flagship-Modell:** Devstral 2 (123B Parameter, 72.2% SWE-bench Verified)

→ Offizielle Seite: https://mistral.ai
→ API-Console: https://console.mistral.ai
→ Vibe CLI: https://github.com/mistralai/mistral-vibe

---

## Mistral Teams `⭐ HOCH`

Team-Lizenz fuer Mistral AI mit Zugang zu allen Modellen und Collaboration-Features.

| Feature | Details |
|---|---|
| Preis (annual) | 25 EUR/User/Monat |
| Preis (monthly) | 30 EUR/User/Monat |
| Modelle | Alle Mistral-Modelle via Le Chat |
| Canvas | Kollaboratives Editing |
| Agents | Bis zu 50 Agent-Publikationen |
| Admin | Admin-Console, SSO/SCIM |
| Datenschutz | Kein Training auf Kundendaten |

- [ ] Team-Lizenz aktivieren und API-Keys verteilen
- [ ] SSO/SCIM-Anbindung an bestehende Identity-Loesung pruefen

---

## Mistral Vibe CLI `⭐ HOCH`

CLI-basierter Coding-Agent von Mistral, vergleichbar mit Claude Code. Open Source unter MIT/Apache 2.0.

- **GitHub:** https://github.com/mistralai/mistral-vibe (3.2k Stars, v2.2.1)
- **Modell:** Devstral 2 (123B Parameter, 72.2% SWE-bench Verified)
- **Runtime:** Python 3.12+

### Installation

```bash
curl -LsSf https://mistral.ai/vibe/install.sh | bash
```

### Konfiguration

| Ebene | Pfad | Format |
|---|---|---|
| Projekt | `.vibe/config.toml` | TOML |
| Global | `~/.vibe/config.toml` | TOML |
| Agents | `~/.vibe/agents/*.toml` | TOML |
| Skills | `.vibe/skills/*/SKILL.md` | Markdown |
| Workspace | `AGENTS.md` (Root) | Markdown |

### Built-in Agents

- **default** – Standard-Agent, fragt vor Tool-Ausfuehrung
- **plan** – Nur Planung, keine Code-Aenderungen
- **accept-edits** – Automatisches Akzeptieren von Edits
- **auto-approve** – Alle Tool-Aufrufe automatisch genehmigt
- **explore** – Subagent fuer Codebase-Exploration

### Features

- File Manipulation (Lesen, Schreiben, Erstellen)
- Code Search (grep-basiert)
- Version Control (Git-Integration)
- Shell Commands
- Subagents (explore Subagent, task Tool)
- MCP vollstaendig unterstuetzt via `[[mcp_servers]]` in config.toml (stdio + http + streamable-http)
- Tool Permissions: always/ask Modell, Glob/Regex Pattern-Matching

### Vergleich: Claude Code vs. Mistral Vibe

| Aspekt | Claude Code | Mistral Vibe |
|---|---|---|
| Projekt-Config | `CLAUDE.md` (Markdown) | `.vibe/config.toml` (TOML) |
| Agents | `.claude/agents/*.md` | `~/.vibe/agents/*.toml` |
| Skills | `.claude/skills/*.md` | `.vibe/skills/*/SKILL.md` |
| Rules | `.claude/rules/*.md` | (kein Pendant) |
| MCP-Config | `.mcp.json` (JSON) | `[[mcp_servers]]` in config.toml |
| Modell | Claude Sonnet/Opus | Devstral 2 / Mistral Large |
| Runtime | Node.js (npm) | Python (pip/uv) |
| Memory | `MEMORY.md` | (kein Pendant) |
| Plan Mode | `/plan` | `--agent plan` |
| Open Source | Nein (proprietaer) | Ja (MIT/Apache 2.0) |
| Subagents | `/swarm`, Task Tool | task Tool (explore Subagent) |

→ siehe [05-Claude-Code-Konfiguration.md](05-Claude-Code-Konfiguration.md) fuer Claude Code Details

- [ ] Vibe installieren und mit eigenem Projekt testen
- [ ] `.vibe/config.toml` Projekt-Template erstellen
- [ ] Parallelbetrieb Claude Code + Vibe im selben Repo testen

---

## Modell-Uebersicht

### Devstral 2 (123B) `⭐ HOCH`

Flagship Coding-Modell fuer Software Engineering Agents.

| Eigenschaft | Wert |
|---|---|
| Parameter | 123B |
| Context | 256K Tokens |
| SWE-bench Verified | 72.2% (bis zu 7x kosteneffizienter als Claude Sonnet) |
| Lizenz | Modified MIT |
| Pricing | Aktuell GRATIS via Mistral API |

**Staerken:** Repository-Scale Reasoning, Multi-File Orchestration, Legacy-Erkennung (inkl. COBOL)

- [ ] Vergleichstest Devstral 2 vs. Claude Sonnet fuer Code-Generierung

### Devstral Small 2 (24B) `🔵 MITTEL`

Kompakte Version fuer Laptops und Edge-Deployment.

| Eigenschaft | Wert |
|---|---|
| Parameter | 24B |
| Context | 256K Tokens |
| SWE-bench Verified | 68.0% |
| Lizenz | Apache 2.0 |
| Lokal | Via OLLAMA betreibbar |

→ siehe [11-Lokale-KI.md](11-Lokale-KI.md)

- [ ] Lokalen Betrieb via OLLAMA testen (Hardware-Anforderungen pruefen)

### Codestral (25.01) `🔵 MITTEL`

Code-Generierung und Autocomplete mit FIM (Fill-in-the-Middle) Support.

| Eigenschaft | Wert |
|---|---|
| Context | 256K Tokens |
| Sprachen | 80+ inkl. COBOL |
| Pricing | 0.30/0.90 EUR pro Million Tokens |
| Endpoint | `codestral.mistral.ai` (speziell fuer IDE-Integrationen) |

**Staerke:** FIM-Support fuer Tab-Completion in IDEs (Continue.dev)

- [ ] Continue.dev mit Codestral fuer Autocomplete konfigurieren

### Mistral Large (25.01) `🔵 MITTEL`

Reasoning-Modell, Mistrals Pendant zu Claude Opus.

| Eigenschaft | Wert |
|---|---|
| Context | 128K Tokens |
| Pricing | 2/6 EUR pro Million Tokens |
| Tool Use | Vollstaendig unterstuetzt (Function Calling) |

### Pixtral Large `🔵 MITTEL`

Multimodal-Modell (Vision + Text) mit 128K Context.

**Use Case:** Bild-Analyse, Screenshot-zu-Code

---

## Le Chat `⭐ HOCH`

Mistrals Chat-Interface, vergleichbar mit Claude.ai und ChatGPT.

- **URL:** https://chat.mistral.ai
- **Features:** Canvas (kollaboratives Editing), Web-Suche mit Citations, Custom Agents, Code Execution (Python Sandbox)
- **MCP-Support:** Remote MCP-Server via SSE-Transport (KEIN stdio!)
- **Teams-Features:** Alle Modelle, Collaboration, Admin-Console

→ siehe [12-MCP-und-Integrationen.md](12-MCP-und-Integrationen.md) fuer MCP-Grundlagen

- [ ] Le Chat mit MCP-Server verbinden (Remote SSE testen)
- [ ] Canvas-Feature fuer Team-Collaboration evaluieren

---

## API & SDKs

### Endpoints

| Endpoint | Zweck |
|---|---|
| `/chat/completions` | Chat (alle Modelle) |
| `/fim/completions` | Fill-in-the-Middle (Codestral) |
| `/embeddings` | Embedding-Generierung |
| `/models` | Verfuegbare Modelle |
| `/files` | Datei-Upload |
| `/fine-tuning` | Fine-Tuning Jobs |
| `/agents` (beta) | Agent-Erstellung |

### Technische Details

- **Base URL:** `https://api.mistral.ai/v1/`
- **Format:** OpenAI-kompatibel (messages array)
- **SDKs:** Python (`mistralai`), TypeScript (`@mistralai/mistralai`), Java, Go
- **Tool Use:** Vollstaendig unterstuetzt (parallel + sequential Function Calling, JSON Mode)
- **API-Key:** Via console.mistral.ai, Environment Variable `MISTRAL_API_KEY`

```bash
# API-Key setzen
export MISTRAL_API_KEY="your-key-here"
```

---

## IDE-Integration

| Tool | Integration | Funktion |
|---|---|---|
| Continue.dev | Codestral als Autocomplete + Chat Provider | VS Code + JetBrains |
| GitHub Copilot | Codestral als Backend-Modell waehlbar | VS Code + JetBrains |

- **Codestral Endpoint:** `codestral.mistral.ai` (speziell fuer IDE-Integrationen)

→ siehe [02-KI-Coding-Tools.md](02-KI-Coding-Tools.md) fuer Vergleich aller IDE-Integrationen

- [ ] Continue.dev Extension installieren und Codestral konfigurieren
- [ ] Codestral Autocomplete-Qualitaet im Vergleich zu Copilot bewerten

---

## Entscheidungshilfe: Mistral vs. Claude

| Szenario | Empfehlung | Grund |
|---|---|---|
| CLI Coding Agent (Haupttool) | Claude Code | Reiferes Ecosystem, CLAUDE.md, Rules, Skills |
| Kostenguenstiger Coding Agent | Mistral Vibe | Devstral 2 aktuell GRATIS, Open Source |
| IDE Autocomplete | Codestral + Continue.dev | FIM-Support, sehr schnell, guenstig |
| Chat / Canvas | Je nach Praeferenz | Le Chat vs. Claude — beide gut |
| COBOL/Legacy Migration | Beide testen | Devstral 2 explizit COBOL-faehig |
| Lokaler Betrieb (offline) | Devstral Small 2 | Apache 2.0, 24B, OLLAMA-faehig |
| MCP-Server (stdio) | Claude Code / Vibe | Le Chat nur Remote SSE |
| Team-Collaboration | Le Chat Teams | Canvas, Agents, Admin-Console |

---

## Praktische Empfehlungen

1. **Nicht dogmatisch entscheiden** – Mistral Vibe und Claude Code koennen parallel im selben Projekt genutzt werden (`.claude/` und `.vibe/` stoeren sich nicht)
2. **MCP-Server wiederverwenden** – Einmal gebaute MCP-Server funktionieren mit beiden Tools
3. **Codestral fuer Autocomplete** – Continue.dev mit Codestral ist eine kosteneffiziente Alternative zu GitHub Copilot
4. **Devstral Small 2 lokal** – Fuer datenschutzkritische Szenarien via OLLAMA betreiben
5. **DSGVO-Aspekt pruefen** – Mistral ist europaeisch (Frankreich) und kann fuer regulierte Umgebungen Vorteile bieten
6. **Kostenlos testen** – Devstral 2 ist aktuell gratis ueber die Mistral API verfuegbar
