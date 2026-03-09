# AI Workspace Setup – [COMPANY NAME]

> Vollstaendige KI-Konfiguration fuer alle Teams.
> Ein Script bringt alles in dein Projekt – Claude Code und GitHub Copilot ready.

---

## Quickstart (1 Befehl)

```bash
# Repo klonen (einmalig, z.B. neben euren Projekten)
git clone https://[euer-server]/ai-knowledgebase.git

# In euer Projekt wechseln und Setup ausfuehren
cd mein-projekt
bash ../ai-knowledgebase/DEV-SETUP/setup.sh
```

**Das war's.** Danach nur noch Platzhalter in `CLAUDE.md` ersetzen und `claude` starten.

Das Script:
- Kopiert alle Agents, Rules, Skills, Commands in dein `.claude/`
- Kopiert alle Copilot Agents und `copilot-instructions.md` in `.github/`
- Kopiert `.mcp.json` und `.vscode/mcp.json`
- Legt `tasks/lessons.md` und `tasks/todo.md` an (falls nicht vorhanden)
- Ueberspringt Dateien die bereits existieren (kein Ueberschreiben!)

---

## Updates einspielen

```bash
# Repo aktualisieren
cd ai-knowledgebase && git pull

# Update ins Projekt einspielen
# Agents, Rules, Skills werden aktualisiert
# CLAUDE.md, copilot-instructions.md und lessons.md bleiben erhalten
cd mein-projekt
bash ../ai-knowledgebase/DEV-SETUP/setup.sh --update
```

**Was --update aktualisiert:** `.claude/agents/`, `.claude/rules/`, `.claude/skills/`,
`.claude/commands/`, `.github/agents/`, `.mcp.json`, `.vscode/mcp.json`

**Was --update NICHT anfasst:** `CLAUDE.md`, `MEMORY.md`, `.github/copilot-instructions.md`,
`tasks/lessons.md`, `tasks/todo.md`, `.claude/settings.json`

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
bash .ki-setup/DEV-SETUP/setup.sh --update
```

Tipp: Als Makefile-Target anlegen:
```makefile
ki-update:
	cd .ki-setup && git pull
	bash .ki-setup/DEV-SETUP/setup.sh --update
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

.mcp.json              # MCP Server: Playwright, GitHub, PostgreSQL, Context7, Figma, Draw.io
.vscode/mcp.json       # Gleiche Server fuer VS Code / GitHub Copilot

CLAUDE.md              # Unternehmens-Kontext (ANPASSEN!)
MEMORY.md              # KI-Langzeitgedaechtnis fuer dieses Projekt (wird NIE ueberschrieben)
tasks/lessons.md       # Team-Learnings (wird NIE ueberschrieben)
tasks/todo.md          # Task-Plan (wird NIE ueberschrieben)
```

---

## Nach dem Setup: Platzhalter anpassen

| Datei | Was anpassen |
|-------|-------------|
| `CLAUDE.md` | `[COMPANY NAME]`, `[BRANCHE]`, CI/CD, Hosting, Monitoring |
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

## MCP Server

| Server | Zweck | Token noetig? |
|--------|-------|---------------|
| `playwright` | Browser-Tests, E2E, Verifikation im Browser | Nein |
| `github` | PRs, Issues, Repos | `GITHUB_TOKEN` |
| `context7` | Aktuelle Library-Docs (kein Halluzinieren) | Nein |
| `drawio` | Architektur-Diagramme auto-generieren | Nein |
| `figma` | Design-to-Code, Vue-Komponenten | `FIGMA_TOKEN` |
| `postgres` | DB-Queries, Schema-Analyse | `POSTGRES_CONNECTION_STRING` |

---

## Weiterentwicklung

Neue Erkenntnisse → `tasks/lessons.md`
Neue Team-Standards → `CLAUDE.md` + `.github/copilot-instructions.md`
Neuer Agent/Skill → in diesem Repo ergaenzen, dann `--update` an alle verteilen
