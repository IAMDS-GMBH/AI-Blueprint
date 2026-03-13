# AI Workspace Setup – [COMPANY NAME]

> KI-Konfiguration fuer Entwicklerteams. Ein Script → Claude Code, GitHub Copilot und Mistral Vibe ready.

## Quickstart

```bash
# AI-Blueprint auschecken (einmalig)
git clone <AI-Blueprint-Repo>

# In euer Projekt kopieren
bash /pfad/zu/dev-setup-template/setup.sh claude /pfad/zu/mein-projekt
```

Interaktive Auswahl oder direkt per Argument:
```bash
bash setup.sh claude /pfad/zu/projekt          # Nur Claude Code
bash setup.sh copilot mistral /pfad/zu/projekt  # Copilot + Mistral
bash setup.sh --all /pfad/zu/projekt            # Alle drei
bash setup.sh --update claude /pfad/zu/projekt  # Update bestehende Installation
```

> Ohne Pfadangabe wird das aktuelle Verzeichnis verwendet.

## Nach dem Setup — /configure

Nach dem Kopieren: KI-Tool starten und `/configure` ausfuehren.
Der Befehl erkennt automatisch euren Tech-Stack und passt alle Dateien an.

| Tool | Aufruf |
|------|--------|
| Claude Code | `/configure` |
| GitHub Copilot | `@workspace /configure` |
| Mistral Vibe | `vibe --agent configure` |

### Was /configure macht
1. **Erkennt installierte Tools** — prueft ob .claude/, .github/, .vibe/ vorhanden
2. **Erkennt den Tech-Stack** — analysiert pom.xml, package.json, angular.json, etc.
3. **Aktiviert passende Rules** — kopiert Stack-Rules aus stacks/ in die aktiven Ordner
4. **Ersetzt Placeholders** — [COMPANY NAME], [PROJECT NAME], [STACK] in allen Config-Dateien
5. **Initialisiert MEMORY.md** — traegt Stack und Projektstruktur ein

Bei leerem Repo fragt /configure interaktiv nach Stack-Details.
Bei bereits konfiguriertem Projekt bietet /configure Feintuning an.

## Was wird installiert

```
.claude/
  agents/        dev, test, review, docs (4 Agents)
  commands/      /plan, /review, /swarm, /configure (4 Commands)
  rules/         quality.md (alwaysApply) + stacks/ (verfuegbare Stack-Rules)
  skills/        verification, standards, debugging (3 Skills)
  hooks/         format-on-save.sh, protect-secrets.sh
  settings.json  Permissions, Hooks, Plugins

.github/
  agents/          @DevAgent @TestAgent @ReviewAgent @DocsAgent
  instructions/    stacks/ (verfuegbare Stack-Instructions)
  copilot-prompts/ configure.prompt.md
  copilot-instructions.md

.vibe/
  agents/        dev, test, review, docs, configure (TOML)
  config.toml    Modell + MCP

CLAUDE.md        Haupt-Config (Claude Code)
AGENTS.md        Haupt-Config (Copilot + Vibe)
MEMORY.md        KI-Langzeitgedaechtnis
.mcp.json        MCP Server (context7 Default)
tasks/           lessons.md + todo.md
```

## Verfuegbare Stacks

| Stack | Ordner | Inhalt |
|-------|--------|--------|
| Java Spring Boot | stacks/java-spring/ | Rules + Snippets |
| Vue.js 3 + TS | stacks/vue-typescript/ | Rules + Snippets |
| React + TS | stacks/react-typescript/ | Rules + Snippets |
| Angular | stacks/angular/ | Rules + Snippets |
| Python FastAPI | stacks/python-fastapi/ | Rules + Snippets |
| .NET / C# | stacks/dotnet/ | Rules + Snippets |
| Oracle DB | stacks/oracle-db/ | Rules + Snippets |
| PostgreSQL | stacks/postgresql/ | Rules + Snippets |
| Node + Hono + Bun | stacks/node-hono/ | Rules + Snippets |
| COBOL (Migration) | stacks/cobol/ | Rules + Snippets |

> Stacks werden von setup.sh mitkopiert. /configure aktiviert die passenden automatisch.

## Architektur — Warum so?

```
CLAUDE.md (~60 Zeilen)     Verhalten + Session-Protokoll    → JEDE Nachricht
Rules quality.md           Security + Qualitaet              → JEDE Nachricht (alwaysApply)
Rules mit Globs            Code-Konventionen                 → NUR bei passenden Dateien
Agents                     Rollen (erben CLAUDE.md + Rules)  → NUR bei Agent-Aufruf
Commands                   Workflows                         → NUR bei /command
Skills                     Wissen on-demand                  → NUR bei Bedarf
Hooks                      Automatisierung                   → 0 Tokens
```

**Token-Optimierung:** CLAUDE.md wird bei JEDER Nachricht geladen.
Alles was nicht bei jeder Interaktion gebraucht wird → Rules/Skills/Commands.

## Best Practices — Wie konfigurieren

### CLAUDE.md
- Erste 20 Zeilen = wichtigste Regeln (Attention-Bias)
- Imperativ: "Fix bugs autonom" statt "Du solltest versuchen..."
- Verbote > Empfehlungen: "NEVER double fuer Geld" wirkt staerker
- Konkret: "max 30 Zeilen/Methode" statt "halte Methoden kurz"
- Keine Code-Beispiele (→ Rules), keine Erklaerungen warum

### Rules
- alwaysApply=true NUR fuer universelle Regeln (max 3)
- Globs so spezifisch wie moeglich: "src/**/*.java" statt "**/*.java"
- DOs und DON'Ts zusammen, nicht getrennte Dateien

### Agents
- Erben CLAUDE.md + Rules automatisch → NIE Konventionen wiederholen
- Nur definieren: Rolle, Verhalten, Output-Format (max 35 Zeilen)
- Frontmatter nutzen: description, maxTurns, disallowedTools (z.B. Review Agent = read-only)

### Skills
- trigger-when fuer automatisches Laden (z.B. "User fragt nach Tests" → standards-Skill)
- Skills bleiben on-demand — kein Token-Overhead wenn nicht getriggert

### Hooks
- Kosten 0 Tokens — laufen ausserhalb des Kontextfensters
- format-on-save: Automatisches Formatting nach Edit/Write
- protect-secrets: Blockiert Zugriff auf .env, credentials, Secrets

### Settings
- deny-Rules fuer destruktive Operationen (rm -rf, push --force, DROP, TRUNCATE)
- Hooks-Sektion verbindet Pre/PostToolUse mit Hook-Scripts

### Anti-Patterns
- Tech-Konventionen in CLAUDE.md → Rules mit Globs
- Gleicher Inhalt in Agent + CLAUDE.md → Agent erbt automatisch
- Mehr als 3 alwaysApply-Rules → Token-Explosion

## Slash Commands

| Command | Wann |
|---------|------|
| /plan | Aufgaben mit 3+ Schritten planen |
| /review | Vor jedem PR-Merge |
| /swarm | Grosse Aufgabe auf Agents verteilen |
| /configure | Nach setup.sh: Stack erkennen, Rules aktivieren, Placeholders ersetzen |

## Context-Management

- /compact bei 70% Auslastung (Qualitaet sinkt ab 70%)
- /clear zwischen unrelateden Tasks
- Sub-Agents fuer Research (Hauptkontext sauber halten)

## Feature-Paritaet

| Feature | Claude Code | Copilot | Vibe |
|---------|------------|---------|------|
| Config | CLAUDE.md | copilot-instructions.md + CLAUDE.md (Fallback) | AGENTS.md |
| Agents | .claude/agents/ (4) | .github/agents/ (4) | .vibe/agents/ (5) |
| Rules | .claude/rules/ | .github/instructions/ | — (in AGENTS.md) |
| Commands | .claude/commands/ (4) | — | — |
| Skills | .claude/skills/ (3) | — | — |
| MCP | .mcp.json | .vscode/mcp.json | config.toml |
| /configure | .claude/commands/ | .github/copilot-prompts/ | .vibe/agents/configure.toml |
