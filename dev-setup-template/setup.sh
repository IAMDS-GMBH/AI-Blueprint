#!/bin/bash

# =============================================================================
# KI-Setup Installer
# Kopiert die KI-Konfiguration (Claude Code / Copilot / Mistral Vibe)
# in ein bestehendes Projekt.
#
# Aufruf:
#   bash setup.sh                        # Interaktive Auswahl
#   bash setup.sh claude                 # Nur Claude Code
#   bash setup.sh copilot mistral        # Copilot + Mistral
#   bash setup.sh --all                  # Alles
#   bash setup.sh --update claude        # Update nur Claude
# =============================================================================

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TARGET_DIR="${PWD}"
UPDATE_MODE=false
INSTALL_CLAUDE=false
INSTALL_COPILOT=false
INSTALL_MISTRAL=false
TOOLS_SPECIFIED=false

# Argumente parsen
for arg in "$@"; do
  case "$arg" in
    --update)  UPDATE_MODE=true ;;
    claude)    INSTALL_CLAUDE=true;  TOOLS_SPECIFIED=true ;;
    copilot)   INSTALL_COPILOT=true; TOOLS_SPECIFIED=true ;;
    mistral)   INSTALL_MISTRAL=true; TOOLS_SPECIFIED=true ;;
    --all)     INSTALL_CLAUDE=true; INSTALL_COPILOT=true; INSTALL_MISTRAL=true; TOOLS_SPECIFIED=true ;;
    *)         echo "Unbekanntes Argument: $arg"; echo "Nutzung: bash setup.sh [--update] [claude] [copilot] [mistral] [--all]"; exit 1 ;;
  esac
done

# Farben
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  KI-Setup Installer                    ${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo "Ziel-Verzeichnis: ${TARGET_DIR}"
echo ""

# -----------------------------------------------------------------------------
# Interaktive Auswahl (wenn kein Tool angegeben)
# -----------------------------------------------------------------------------
if [[ "$TOOLS_SPECIFIED" == false ]]; then
  echo -e "${CYAN}Welche Tools moechtest du einrichten?${NC}"
  echo ""
  echo "  1) Claude Code        (.claude/, CLAUDE.md, .mcp.json)"
  echo "  2) GitHub Copilot     (.github/, .vscode/mcp.json)"
  echo "  3) Mistral Vibe       (.vibe/, AGENTS.md)"
  echo "  4) Alle drei"
  echo ""
  read -rp "Auswahl (z.B. 1,3 oder 4): " SELECTION

  IFS=',' read -ra CHOICES <<< "$SELECTION"
  for choice in "${CHOICES[@]}"; do
    choice=$(echo "$choice" | tr -d ' ')
    case "$choice" in
      1) INSTALL_CLAUDE=true ;;
      2) INSTALL_COPILOT=true ;;
      3) INSTALL_MISTRAL=true ;;
      4) INSTALL_CLAUDE=true; INSTALL_COPILOT=true; INSTALL_MISTRAL=true ;;
      *) echo "Ungueltige Auswahl: $choice"; exit 1 ;;
    esac
  done
  echo ""
fi

# Mindestens ein Tool muss gewaehlt sein
if [[ "$INSTALL_CLAUDE" == false && "$INSTALL_COPILOT" == false && "$INSTALL_MISTRAL" == false ]]; then
  echo "Kein Tool ausgewaehlt. Abbruch."
  exit 1
fi

# Auswahl anzeigen
SELECTED=""
[[ "$INSTALL_CLAUDE"  == true ]] && SELECTED+="Claude Code, "
[[ "$INSTALL_COPILOT" == true ]] && SELECTED+="GitHub Copilot, "
[[ "$INSTALL_MISTRAL" == true ]] && SELECTED+="Mistral Vibe, "
SELECTED="${SELECTED%, }"
echo -e "${GREEN}Ausgewaehlt: ${SELECTED}${NC}"
[[ "$UPDATE_MODE" == true ]] && echo -e "${YELLOW}Modus: --update (bestehende Dateien werden ueberschrieben)${NC}"
echo ""

# -----------------------------------------------------------------------------
# Hilfsfunktionen
# -----------------------------------------------------------------------------

copy_if_not_exists() {
  local src="$1"
  local dest="$2"
  if [[ -f "$dest" && "$UPDATE_MODE" == false ]]; then
    echo -e "  ${YELLOW}SKIP${NC}  $dest (bereits vorhanden)"
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

copy_dir_recursive() {
  local src="$1"
  local dest="$2"
  if [[ -d "$src" ]]; then
    while IFS= read -r -d '' file; do
      local rel_path="${file#$src/}"
      copy_if_not_exists "$file" "$dest/$rel_path"
    done < <(find "$src" -type f -print0)
  fi
}

STEP=0
TOTAL_STEPS=0
[[ "$INSTALL_CLAUDE"  == true ]] && TOTAL_STEPS=$((TOTAL_STEPS + 1))
[[ "$INSTALL_COPILOT" == true ]] && TOTAL_STEPS=$((TOTAL_STEPS + 1))
[[ "$INSTALL_MISTRAL" == true ]] && TOTAL_STEPS=$((TOTAL_STEPS + 1))
TOTAL_STEPS=$((TOTAL_STEPS + 2))  # Gemeinsame Dateien + Zusammenfassung

# -----------------------------------------------------------------------------
# Claude Code
# -----------------------------------------------------------------------------
if [[ "$INSTALL_CLAUDE" == true ]]; then
  STEP=$((STEP + 1))
  echo -e "${BLUE}[${STEP}/${TOTAL_STEPS}] Claude Code${NC}"

  copy_dir_merge "$SCRIPT_DIR/.claude/agents"   "$TARGET_DIR/.claude/agents"
  copy_dir_merge "$SCRIPT_DIR/.claude/commands" "$TARGET_DIR/.claude/commands"
  copy_dir_merge "$SCRIPT_DIR/.claude/skills"   "$TARGET_DIR/.claude/skills"
  copy_dir_merge "$SCRIPT_DIR/.claude/rules"    "$TARGET_DIR/.claude/rules"
  copy_if_not_exists "$SCRIPT_DIR/.claude/settings.json" "$TARGET_DIR/.claude/settings.json"
  copy_if_not_exists "$SCRIPT_DIR/.mcp.json"    "$TARGET_DIR/.mcp.json"
  copy_if_not_exists "$SCRIPT_DIR/CLAUDE.md"    "$TARGET_DIR/CLAUDE.md"
  echo ""
fi

# -----------------------------------------------------------------------------
# GitHub Copilot
# -----------------------------------------------------------------------------
if [[ "$INSTALL_COPILOT" == true ]]; then
  STEP=$((STEP + 1))
  echo -e "${BLUE}[${STEP}/${TOTAL_STEPS}] GitHub Copilot${NC}"

  copy_dir_merge "$SCRIPT_DIR/.github/agents"          "$TARGET_DIR/.github/agents"
  copy_dir_merge "$SCRIPT_DIR/.github/instructions"    "$TARGET_DIR/.github/instructions"
  copy_dir_merge "$SCRIPT_DIR/.github/copilot-prompts" "$TARGET_DIR/.github/copilot-prompts"
  copy_if_not_exists "$SCRIPT_DIR/.github/copilot-instructions.md" "$TARGET_DIR/.github/copilot-instructions.md"
  copy_if_not_exists "$SCRIPT_DIR/.vscode/mcp.json"    "$TARGET_DIR/.vscode/mcp.json"
  echo ""
fi

# -----------------------------------------------------------------------------
# Mistral Vibe
# -----------------------------------------------------------------------------
if [[ "$INSTALL_MISTRAL" == true ]]; then
  STEP=$((STEP + 1))
  echo -e "${BLUE}[${STEP}/${TOTAL_STEPS}] Mistral Vibe${NC}"

  copy_if_not_exists "$SCRIPT_DIR/.vibe/config.toml" "$TARGET_DIR/.vibe/config.toml"
  copy_dir_merge "$SCRIPT_DIR/.vibe/agents"          "$TARGET_DIR/.vibe/agents"
  copy_dir_recursive "$SCRIPT_DIR/.vibe/skills"      "$TARGET_DIR/.vibe/skills"
  copy_if_not_exists "$SCRIPT_DIR/AGENTS.md"         "$TARGET_DIR/AGENTS.md"
  echo ""
fi

# -----------------------------------------------------------------------------
# Gemeinsame Dateien (immer)
# -----------------------------------------------------------------------------
STEP=$((STEP + 1))
echo -e "${BLUE}[${STEP}/${TOTAL_STEPS}] Gemeinsame Dateien${NC}"

copy_if_not_exists "$SCRIPT_DIR/MEMORY.md"           "$TARGET_DIR/MEMORY.md"
copy_if_not_exists "$SCRIPT_DIR/tasks/lessons.md"    "$TARGET_DIR/tasks/lessons.md"
copy_if_not_exists "$SCRIPT_DIR/tasks/todo.md"       "$TARGET_DIR/tasks/todo.md"

# -----------------------------------------------------------------------------
# Zusammenfassung
# -----------------------------------------------------------------------------
STEP=$((STEP + 1))
echo ""
echo -e "${BLUE}[${STEP}/${TOTAL_STEPS}] Zusammenfassung${NC}"

COPIED=$(find "$TARGET_DIR" -newer "$SCRIPT_DIR/setup.sh" -type f 2>/dev/null | wc -l | tr -d ' ')
echo -e "  ${GREEN}OK${NC}    Setup abgeschlossen"

echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}  Setup abgeschlossen!${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# -----------------------------------------------------------------------------
# Naechste Schritte (kontextabhaengig)
# -----------------------------------------------------------------------------
echo -e "${YELLOW}NAECHSTE SCHRITTE:${NC}"
echo ""

if [[ "$INSTALL_CLAUDE" == true ]]; then
  echo -e "  ${CYAN}Claude Code:${NC}"
  echo "    - CLAUDE.md anpassen: [COMPANY NAME], [BRANCHE], Tech Stack"
  echo "    - .mcp.json: Tokens eintragen (GITHUB_TOKEN, POSTGRES_CONNECTION_STRING)"
  echo "    - Plugins aktivieren:"
  echo "      /plugin install context7@claude-plugins-official"
  echo "      /plugin install security-guidance@claude-plugins-official"
  echo ""
fi

if [[ "$INSTALL_COPILOT" == true ]]; then
  echo -e "  ${CYAN}GitHub Copilot:${NC}"
  echo "    - .github/copilot-instructions.md anpassen: [COMPANY NAME], [BRANCHE]"
  echo "    - .vscode/mcp.json: Tokens eintragen"
  echo ""
fi

if [[ "$INSTALL_MISTRAL" == true ]]; then
  echo -e "  ${CYAN}Mistral Vibe:${NC}"
  echo "    - AGENTS.md anpassen: [COMPANY NAME], [BRANCHE], Tech Stack"
  echo "    - .vibe/config.toml: Tokens eintragen"
  echo "    - Vibe installieren (falls noch nicht vorhanden):"
  echo "      curl -LsSf https://mistral.ai/vibe/install.sh | bash"
  echo "      export MISTRAL_API_KEY=your-key-here"
  echo ""
fi

echo "  MEMORY.md anpassen: [COMPANY NAME], [PROJECT NAME]"
echo ""
echo "  Was ihr nicht braucht, koennt ihr einfach loeschen."
echo ""
