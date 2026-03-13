Pruefe ob die Triple-Config im dev-setup-template synchron ist:

1. Liste alle Agents in .claude/agents/, .github/agents/ und .vibe/agents/
2. Vergleiche: Hat jeder Claude-Agent ein Copilot- und Vibe-Pendant?
3. Liste alle Rules in .claude/rules/ und .github/instructions/
4. Vergleiche: Sind die Inhalte konsistent?
5. Pruefe CLAUDE.md vs .github/copilot-instructions.md vs .vibe/config.toml
6. Pruefe .mcp.json vs .vscode/mcp.json

Erstelle einen Sync-Bericht:
- Fehlende Dateien (Agent in .claude/ aber nicht in .github/)
- Inhaltliche Abweichungen
- Empfohlene Aenderungen
