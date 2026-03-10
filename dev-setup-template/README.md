# AI Workspace Setup – [COMPANY NAME]

> Vollstaendige KI-Konfiguration fuer alle Teams.
> Ein Script bringt alles in dein Projekt – Claude Code, GitHub Copilot und Mistral Vibe ready.

---

## Quickstart

```bash
# Repo klonen (einmalig, z.B. neben euren Projekten)
git clone https://[euer-server]/ai-knowledgebase.git

# In euer Projekt wechseln und Setup ausfuehren
cd mein-projekt
bash ../ai-knowledgebase/dev-setup-template/setup.sh
```

Das Script fragt interaktiv welche Tools eingerichtet werden sollen:
```
Welche Tools moechtest du einrichten?

  1) Claude Code        (.claude/, CLAUDE.md, .mcp.json)
  2) GitHub Copilot     (.github/, .vscode/mcp.json)
  3) Mistral Vibe       (.vibe/, AGENTS.md)
  4) Alle drei

Auswahl (z.B. 1,3 oder 4):
```

Alternativ direkt per Argument:
```bash
bash setup.sh claude              # Nur Claude Code
bash setup.sh copilot mistral     # Copilot + Mistral
bash setup.sh --all               # Alle drei
```

Danach Platzhalter in der jeweiligen Config-Datei ersetzen und loslegen.

Das Script:
- Kopiert nur die Dateien fuer das gewaehlte Tool
- Legt `MEMORY.md`, `tasks/lessons.md` und `tasks/todo.md` immer an (gemeinsam genutzt)
- Ueberspringt Dateien die bereits existieren (kein Ueberschreiben!)

---

## Updates einspielen

```bash
# Repo aktualisieren
cd ai-knowledgebase && git pull

# Update ins Projekt einspielen (nur fuer die genutzten Tools)
cd mein-projekt
bash ../ai-knowledgebase/dev-setup-template/setup.sh --update claude
bash ../ai-knowledgebase/dev-setup-template/setup.sh --update --all
```

**Was --update aktualisiert:** Agents, Rules, Skills, Commands, MCP-Configs des gewaehlten Tools

**Was --update NICHT anfasst:** `CLAUDE.md`, `AGENTS.md`, `MEMORY.md`, `.github/copilot-instructions.md`,
`tasks/lessons.md`, `tasks/todo.md`, `.claude/settings.json`, `.vibe/config.toml`

---

## Git Subtree (automatische Updates im Team)

Fuer Teams die Updates direkt im Projekt-Repository tracken wollen:

```bash
# Einmalig hinzufuegen
git subtree add --prefix=.ki-setup \
  https://[euer-server]/ai-knowledgebase.git main --squash

# Spaeter updaten – alle bekommen es via git pull
git subtree pull --prefix=.ki-setup \
  https://[euer-server]/ai-knowledgebase.git main --squash

# Setup ausfuehren
bash .ki-setup/dev-setup-template/setup.sh --update
```

Tipp: Als Makefile-Target anlegen:
```makefile
ki-update:
	cd .ki-setup && git pull
	bash .ki-setup/dev-setup-template/setup.sh --update
```
Dann reicht `make ki-update` fuer alle.

---

## Was wird installiert?

```
.claude/
  agents/        # Dev, Test, Review, Docs, Knowledge Agent (Claude Code)
  commands/      # /plan, /review, /swarm, /ralph, /brief, /automate
  rules/         # Auto-Standards: Java (*.java), Vue (*.vue), SQL (*.sql)
  skills/        # Dev-Standards, Frontend-Design, Document-Creation
  settings.json  # Permissions + Plugins vorkonfiguriert

.github/
  agents/        # @DevAgent @TestAgent @ReviewAgent @DocsAgent @KnowledgeAgent
  copilot-instructions.md  # Copilot-Konfiguration + Security + Ralph-Loop

.vibe/
  config.toml    # Mistral Vibe Projekt-Config (Modell, MCP-Server, Permissions)
  agents/        # Dev, Test, Review, Docs, Knowledge Agent (TOML)
  skills/        # dev-standards, frontend-design, ceo-brief, automation-design,
                 # document-creation, agent-orchestration (SKILL.md Format)

.mcp.json              # MCP Server: Playwright, GitHub, PostgreSQL, Context7, Figma
.vscode/mcp.json       # Gleiche Server fuer VS Code / GitHub Copilot

CLAUDE.md              # Unternehmens-Kontext fuer Claude Code (ANPASSEN!)
AGENTS.md              # Unternehmens-Kontext fuer Mistral Vibe (ANPASSEN!)
MEMORY.md              # KI-Langzeitgedaechtnis fuer dieses Projekt (wird NIE ueberschrieben)
tasks/lessons.md       # Team-Learnings (wird NIE ueberschrieben)
tasks/todo.md          # Task-Plan (wird NIE ueberschrieben)
```

---

## Nach dem Setup: Platzhalter anpassen

| Datei | Was anpassen |
|-------|-------------|
| `CLAUDE.md` | `[COMPANY NAME]`, `[BRANCHE]`, CI/CD, Hosting, Monitoring |
| `AGENTS.md` | `[COMPANY NAME]`, `[BRANCHE]` (gleiche Werte wie CLAUDE.md) |
| `MEMORY.md` | `[COMPANY NAME]`, `[PROJECT NAME]`, initiale Architekturentscheidungen |
| `.github/copilot-instructions.md` | Gleiche Platzhalter wie CLAUDE.md |
| `.mcp.json` | `POSTGRES_CONNECTION_STRING`, `GITHUB_TOKEN`, `FIGMA_TOKEN` |
| `.vscode/mcp.json` | Gleiche Tokens |

---

## Claude Code Plugins aktivieren (einmalig pro Projekt)

Im Projektverzeichnis in Claude Code ausfuehren:

```
/plugin install ralph-loop@claude-plugins-official
/plugin install context7@claude-plugins-official
/plugin install security-guidance@claude-plugins-official
/plugin install typescript-lsp@claude-plugins-official
/plugin install jdtls-lsp@claude-plugins-official
```

Plugins werden in `.claude/settings.json` gespeichert und bleiben bei `--update` erhalten.

---

## Slash Commands (Claude Code)

| Command | Wann verwenden |
|---------|----------------|
| `/plan` | Aufgaben mit mehr als 3 Schritten: erst planen, dann ausfuehren |
| `/review` | Vor jedem PR-Merge |
| `/swarm` | 3+ unabhaengige Tasks parallel |
| `/ralph` | Qualitaets-Check vor Commit (Vollstaendig? Korrekt? Sicher?) |
| `/brief` | Status-Zusammenfassung fuer Management |
| `/automate` | Automatisierungspotenzial analysieren |

---

## Copilot Agents

| Agent | Aufruf | Wann |
|-------|--------|------|
| DevAgent | `@DevAgent` | Feature-Implementierung, Bug Fixes |
| TestAgent | `@TestAgent` | Unit Tests, E2E Tests, Coverage |
| ReviewAgent | `@ReviewAgent` | Code Review vor PR-Merge |
| DocsAgent | `@DocsAgent` | API-Docs, ADRs, Onboarding |
| KnowledgeAgent | `@KnowledgeAgent` | KI-Tools, Best Practices |

---

## Mistral Vibe (optional)

Falls euer Team **Mistral Teams Lizenzen** hat, koennt ihr Mistral Vibe als CLI-Agent nutzen.
Vibe arbeitet parallel zu Claude Code im selben Repo (`.claude/` und `.vibe/` stoeren sich nicht).

### Installation

```bash
curl -LsSf https://mistral.ai/vibe/install.sh | bash
export MISTRAL_API_KEY=your-key-here
vibe
```

### Was ist konfiguriert

| Datei | Zweck |
|-------|-------|
| `.vibe/config.toml` | Modell (Devstral 2), MCP-Server, Tool-Permissions |
| `.vibe/agents/*.toml` | 5 Agents: Dev, Test, Review, Docs, Knowledge |
| `.vibe/skills/*/SKILL.md` | 6 Skills (gleiche wie Claude Code) |
| `AGENTS.md` | Projekt-Kontext fuer Vibe (Pendant zu CLAUDE.md) |

### Vibe Agents

**Projekt-Agents** (gleiche Rollen wie Claude Code):

| Agent | Aufruf | Wann |
|-------|--------|------|
| Dev Agent | `vibe --agent dev` | Feature-Implementierung, Bug Fixes |
| Test Agent | `vibe --agent test` | Unit Tests, E2E Tests, Coverage |
| Review Agent | `vibe --agent review` | Code Review vor PR-Merge |
| Docs Agent | `vibe --agent docs` | API-Docs, ADRs, Onboarding |
| Knowledge Agent | `vibe --agent knowledge` | KI-Wissensbasis, Best Practices |

**Built-in Agents:**

| Agent | Aufruf | Wann |
|-------|--------|------|
| default | `vibe` | Allgemeine Tasks (fragt vor Tool-Ausfuehrung) |
| plan | `vibe --agent plan` | Nur Planung, keine Code-Aenderungen |
| accept-edits | `vibe --agent accept-edits` | Auto-Akzeptieren von Edits |
| auto-approve | `vibe --agent auto-approve` | Alle Tools automatisch genehmigt |

### Feature-Paritaet: Claude Code vs. Mistral Vibe vs. GitHub Copilot

| Feature | Claude Code | Mistral Vibe | GitHub Copilot |
|---------|------------|--------------|----------------|
| Haupt-Config | `CLAUDE.md` | `AGENTS.md` + `.vibe/config.toml` | `copilot-instructions.md` |
| Agents | `.claude/agents/*.md` (5) | `.vibe/agents/*.toml` (5) | `.github/agents/*.md` (5) |
| Skills | `.claude/skills/*.md` (6) | `.vibe/skills/*/SKILL.md` (6) | – |
| Rules | `.claude/rules/*.md` (3) | – (integriert in AGENTS.md) | `.github/instructions/*.md` (3) |
| Commands | `.claude/commands/*.md` (6) | – (Vibe hat kein Command-System) | `.github/prompts/*.md` (4) |
| MCP | `.mcp.json` | `config.toml [[mcp_servers]]` | `.vscode/mcp.json` |

---

## MCP Server

| Server | Zweck | Token noetig? |
|--------|-------|---------------|
| `playwright` | Browser-Tests, E2E, Verifikation im Browser | Nein |
| `github` | PRs, Issues, Repos | `GITHUB_TOKEN` |
| `context7` | Aktuelle Library-Docs (kein Halluzinieren) | Nein |
| `figma` | Design-to-Code, Vue-Komponenten | `FIGMA_TOKEN` |
| `postgres` | DB-Queries, Schema-Analyse | `POSTGRES_CONNECTION_STRING` |

---

## GSD (Get Shit Done) — Optionales Add-on

GSD ist ein Spec-Driven-Development-System fuer Claude Code. Es loest das Problem von **Context Rot**
(Qualitaetsverlust bei langen Sessions) durch atomare Plaene die jeweils in einem frischen Context-Window laufen.

**Wann GSD nutzen:**
- Grosse Features (10+ Dateien, mehrere Tage Arbeit)
- Greenfield-Projekte (neues Repo von Null aufbauen)
- Komplexe Migrationen mit vielen Modulen

**Wann NICHT:**
- Kleine Bug-Fixes, einzelne Features (unsere `/plan` + `/review` Commands reichen)
- Ad-hoc-Fragen und Exploration

### Installation (einmalig)

```bash
npx get-shit-done-cc@latest
# → Waehle "Claude Code" + "Local" (pro Projekt) oder "Global" (alle Projekte)
```

Verifizieren: In Claude Code `/gsd:help` ausfuehren.

### Wie GSD unser Setup ergaenzt

| Ebene | dev-setup-template | GSD |
|---|---|---|
| Projekt-Kontext | CLAUDE.md, MEMORY.md | PROJECT.md, STATE.md |
| Code-Standards | Rules (Java, Vue, SQL) | — |
| Agents | Dev, Test, Review, Docs | 12 spezialisierte Agents |
| Workflow (klein) | /plan, /review, /ralph | — |
| Workflow (gross) | /swarm | /gsd:plan-phase, /gsd:execute-phase |
| MCP-Server | Playwright, GitHub, Oracle | — |
| Context-Management | — | Frischer Context pro Task |

**Empfehlung:** Fuer den Alltag unsere Commands nutzen. Fuer grosse Projekte GSD aktivieren.

### GSD-Kernbefehle

```
/gsd:new-project        — Neues Projekt initialisieren (Research + Roadmap)
/gsd:map-codebase       — Bestehendes Projekt analysieren
/gsd:plan-phase N       — Phase N planen (atomare Task-Plaene)
/gsd:execute-phase N    — Phase N ausfuehren (parallele Waves, frischer Context)
/gsd:verify-work N      — Ergebnisse verifizieren
/gsd:quick              — Ad-hoc Tasks ohne vollen Overhead
/gsd:pause-work         — Session unterbrechen + spaeter fortsetzen
```

---

## Weiterentwicklung

Neue Erkenntnisse → `tasks/lessons.md`
Neue Team-Standards → `CLAUDE.md` + `.github/copilot-instructions.md`
Neuer Agent/Skill → in diesem Repo ergaenzen, dann `--update` an alle verteilen
