#!/bin/bash
# PostToolUse Hook: Auto-Format nach Edit/Write
# Aktivieren in .claude/settings.json unter hooks.PostToolUse
#
# Passt den Formatter an euren Stack an:
# - JavaScript/TypeScript: prettier oder biome
# - Python: black oder ruff
# - Java: google-java-format
# - Go: gofmt

FILE="$CLAUDE_TOOL_ARG_FILE_PATH"

if [[ -z "$FILE" || ! -f "$FILE" ]]; then
  exit 0
fi

case "$FILE" in
  *.ts|*.tsx|*.js|*.jsx|*.json|*.css|*.html|*.vue)
    if command -v bunx &>/dev/null; then
      bunx prettier --write "$FILE" 2>/dev/null
    elif command -v npx &>/dev/null; then
      npx prettier --write "$FILE" 2>/dev/null
    fi
    ;;
  *.py)
    if command -v ruff &>/dev/null; then
      ruff format "$FILE" 2>/dev/null
    elif command -v black &>/dev/null; then
      black --quiet "$FILE" 2>/dev/null
    fi
    ;;
  *.java)
    # google-java-format muss installiert sein
    # java -jar google-java-format.jar --replace "$FILE" 2>/dev/null
    ;;
esac
