# Skill: Config-Sync (Triple-Config)

Synchronisiere die drei Config-Systeme im dev-setup-template.

## Mapping: .claude/ ↔ .github/ ↔ .vibe/

| Claude Code | GitHub Copilot | Mistral Vibe |
|-------------|---------------|--------------|
| `.claude/agents/<name>.md` | `.github/agents/<name>.md` | `.vibe/agents/<name>.toml` |
| `.claude/rules/<name>.md` | `.github/instructions/<name>.instructions.md` | `.vibe/config.toml` [rules] |
| `.claude/skills/<name>.md` | — (kein Pendant) | `.vibe/skills/<name>/SKILL.md` |
| `.claude/commands/<name>.md` | `.github/copilot-prompts/<name>.prompt.md` | — (kein Pendant) |
| `CLAUDE.md` | `.github/copilot-instructions.md` | `.vibe/config.toml` |
| `.mcp.json` | `.vscode/mcp.json` | — |

## Checkliste bei Aenderungen

### Agent hinzufuegen/aendern
- [ ] `.claude/agents/<name>.md` erstellen/updaten
- [ ] `.github/agents/<name>.md` mit gleicher Rolle erstellen/updaten
- [ ] `.vibe/agents/<name>.toml` erstellen/updaten
- [ ] `AGENTS.md` aktualisieren (Vibe-Referenz)

### Rule hinzufuegen/aendern
- [ ] `.claude/rules/<name>.md` mit globs/alwaysApply
- [ ] `.github/instructions/<name>.instructions.md` mit applyTo
- [ ] `.vibe/config.toml` Rules-Sektion updaten

### Globale Anweisungen aendern
- [ ] `CLAUDE.md` updaten
- [ ] `.github/copilot-instructions.md` synchronisieren
- [ ] `.vibe/config.toml` system_prompt updaten

## Hinweise
- Copilot hat keine Skills → Claude-Skills ohne Copilot-Pendant
- Vibe hat keine Commands → Claude-Commands ohne Vibe-Pendant
- TOML-Syntax fuer Vibe beachten (kein Markdown-Frontmatter)
