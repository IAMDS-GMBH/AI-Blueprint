#!/bin/bash
# Hook: PreToolUse (Edit|Write)
# Blockiert Edits an geschuetzten Dateien.
# Aktivierung: In .claude/settings.json unter "hooks" konfigurieren.
#
# Beispiel settings.json:
# "hooks": {
#   "PreToolUse": [{
#     "matcher": "Edit|Write",
#     "command": "bash .claude/hooks/protect-files.sh \"$FILE_PATH\""
#   }]
# }

PROTECTED_FILES=(
  "CLAUDE.md"
  ".claude/settings.json"
  # Weitere geschuetzte Dateien hier ergaenzen
)

FILE="$1"

for protected in "${PROTECTED_FILES[@]}"; do
  if [[ "$FILE" == *"$protected"* ]]; then
    echo "BLOCKED: $FILE ist geschuetzt. Manuelle Aenderung erforderlich."
    exit 1
  fi
done

exit 0
