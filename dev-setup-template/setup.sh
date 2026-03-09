#!/bin/bash

# =============================================================================
# KI-Setup Installer
# Kopiert die KI-Konfiguration (Claude Code + Copilot) in ein bestehendes Projekt.
# Aufruf: bash setup.sh [--update]
# =============================================================================

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TARGET_DIR="${PWD}"
UPDATE_MODE=false

if [[ "$1" == "--update" ]]; then
  UPDATE_MODE=true
fi

# Farben
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  KI-Setup Installer – dev-setup-template        ${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo "Ziel-Verzeichnis: ${TARGET_DIR}"
echo ""

# -----------------------------------------------------------------------------
# Hilfsfunktionen
# -----------------------------------------------------------------------------

copy_if_not_exists() {
  local src="$1"
  local dest="$2"
  if [[ -f "$dest" && "$UPDATE_MODE" == false ]]; then
    echo -e "  ${YELLOW}SKIP${NC}  $dest (bereits vorhanden, --update zum Ueberschreiben)"
  else
    mkdir -p "$(dirname "$dest")"
    cp "$src" "$dest"
    echo -e "  ${GREEN}OK${NC}    $dest"
  fi
}

copy_dir_merge() {
  local src="$1"
  local dest="$2"
  if [[ -d "$src" ]]; then
    mkdir -p "$dest"
    for file in "$src"/*; do
      [[ -f "$file" ]] && copy_if_not_exists "$file" "$dest/$(basename "$file")"
    done
  fi
}

# -----------------------------------------------------------------------------
# 1. Claude Code Konfiguration
# -----------------------------------------------------------------------------
echo -e "${BLUE}[1/5] Claude Code (.claude/)${NC}"

copy_dir_merge "$SCRIPT_DIR/.claude/agents"   "$TARGET_DIR/.claude/agents"
copy_dir_merge "$SCRIPT_DIR/.claude/commands" "$TARGET_DIR/.claude/commands"
copy_dir_merge "$SCRIPT_DIR/.claude/skills"   "$TARGET_DIR/.claude/skills"
copy_dir_merge "$SCRIPT_DIR/.claude/rules"    "$TARGET_DIR/.claude/rules"
copy_if_not_exists "$SCRIPT_DIR/.claude/settings.json" "$TARGET_DIR/.claude/settings.json"

# -----------------------------------------------------------------------------
# 2. GitHub Copilot Konfiguration
# -----------------------------------------------------------------------------
echo ""
echo -e "${BLUE}[2/5] GitHub Copilot (.github/)${NC}"

copy_dir_merge "$SCRIPT_DIR/.github/agents"       "$TARGET_DIR/.github/agents"
copy_dir_merge "$SCRIPT_DIR/.github/instructions" "$TARGET_DIR/.github/instructions"
copy_if_not_exists "$SCRIPT_DIR/.github/copilot-instructions.md" "$TARGET_DIR/.github/copilot-instructions.md"

# -----------------------------------------------------------------------------
# 3. MCP Server
# -----------------------------------------------------------------------------
echo ""
echo -e "${BLUE}[3/5] MCP Server (.mcp.json + .vscode/mcp.json)${NC}"

copy_if_not_exists "$SCRIPT_DIR/.mcp.json"         "$TARGET_DIR/.mcp.json"
copy_if_not_exists "$SCRIPT_DIR/.vscode/mcp.json"  "$TARGET_DIR/.vscode/mcp.json"

# -----------------------------------------------------------------------------
# 4. CLAUDE.md + MEMORY.md (Haupt-Konfiguration)
# -----------------------------------------------------------------------------
echo ""
echo -e "${BLUE}[4/5] CLAUDE.md + MEMORY.md${NC}"

copy_if_not_exists "$SCRIPT_DIR/CLAUDE.md"   "$TARGET_DIR/CLAUDE.md"
copy_if_not_exists "$SCRIPT_DIR/MEMORY.md"   "$TARGET_DIR/MEMORY.md"

# -----------------------------------------------------------------------------
# 5. Tasks-Ordner (nur anlegen, nicht ueberschreiben)
# -----------------------------------------------------------------------------
echo ""
echo -e "${BLUE}[5/5] Tasks-Ordner${NC}"

mkdir -p "$TARGET_DIR/tasks"

if [[ ! -f "$TARGET_DIR/tasks/lessons.md" ]]; then
  cat > "$TARGET_DIR/tasks/lessons.md" << 'EOF'
# Lessons Learned

> Wird zu Beginn jeder KI-Session geladen.
> Fehler sofort hier eintragen damit sie sich nicht wiederholen.

## Format
```
## YYYY-MM-DD – [Kurztitel]
Root Cause: [Was war das eigentliche Problem?]
Regel: [Welche Regel verhindert das kuenftig?]
```

EOF
  echo -e "  ${GREEN}OK${NC}    tasks/lessons.md (neu angelegt)"
else
  echo -e "  ${YELLOW}SKIP${NC}  tasks/lessons.md (bereits vorhanden – Inhalt bleibt erhalten)"
fi

if [[ ! -f "$TARGET_DIR/tasks/todo.md" ]]; then
  cat > "$TARGET_DIR/tasks/todo.md" << 'EOF'
# Todo

> Aktueller Task-Plan. Wird von der KI waehrend der Arbeit aktuell gehalten.

EOF
  echo -e "  ${GREEN}OK${NC}    tasks/todo.md (neu angelegt)"
else
  echo -e "  ${YELLOW}SKIP${NC}  tasks/todo.md (bereits vorhanden)"
fi

# -----------------------------------------------------------------------------
# Abschluss
# -----------------------------------------------------------------------------
echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}  Setup abgeschlossen!${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo -e "${YELLOW}NAECHSTE SCHRITTE:${NC}"
echo ""
echo "  1. CLAUDE.md anpassen:"
echo "     - [COMPANY NAME] → euer Unternehmensname"
echo "     - [BRANCHE] → eure Branche"
echo "     - Tech Stack pruefen und anpassen"
echo ""
echo "  2. .github/copilot-instructions.md anpassen:"
echo "     - Gleiche Platzhalter wie CLAUDE.md ersetzen"
echo ""
echo "  3. .mcp.json / .vscode/mcp.json anpassen:"
echo "     - POSTGRES_CONNECTION_STRING eintragen"
echo "     - FIGMA_TOKEN eintragen (falls Figma genutzt wird)"
echo "     - GITHUB_TOKEN eintragen"
echo ""
echo "  4. Claude Code Plugins aktivieren (im Projektverzeichnis):"
echo "     /plugin install ralph-loop@claude-plugins-official"
echo "     /plugin install context7@claude-plugins-official"
echo "     /plugin install security-guidance@claude-plugins-official"
echo ""
echo "  Alles fertig? → Claude Code starten: claude"
echo ""
