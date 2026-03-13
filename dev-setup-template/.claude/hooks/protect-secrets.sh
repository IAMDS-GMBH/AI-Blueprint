#!/bin/bash
# PreToolUse Hook: Schuetzt sensible Dateien vor Lesen/Editieren
# Aktivieren in .claude/settings.json unter hooks.PreToolUse

FILE="$CLAUDE_TOOL_ARG_FILE_PATH"

if [[ -z "$FILE" ]]; then
  exit 0
fi

# Sensible Dateien blocken
case "$FILE" in
  *.env|*.env.*|.env)
    echo "BLOCKED: .env Dateien enthalten Secrets — nicht lesen/editieren"
    exit 2
    ;;
  */secrets/*|*/credentials/*|*/.secrets/*)
    echo "BLOCKED: Secrets-Verzeichnis — nicht zugreifen"
    exit 2
    ;;
  *id_rsa*|*id_ed25519*|*.pem|*.key)
    echo "BLOCKED: Private Keys — nicht zugreifen"
    exit 2
    ;;
esac

exit 0
