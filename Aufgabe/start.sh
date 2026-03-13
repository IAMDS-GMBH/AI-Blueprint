#!/bin/bash
# Aufruf: ./start.sh <MISTRAL_API_KEY>
#
# Ports:
# - MCP-Server: 3001 (HTTP-Wrapper)
# - Backend:    8080
# - Frontend:   5173
# - PostgreSQL: 5432 (extern, nicht von diesem Script verwaltet)

if [ -z "$1" ]; then
    echo "Fehler: Kein API-Key angegeben."
    echo "Aufruf: ./start.sh <MISTRAL_API_KEY>"
    exit 1
fi

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

# Env-Vars laden
if [ -f "$SCRIPT_DIR/.env" ]; then
    set -a
    source "$SCRIPT_DIR/.env"
    set +a
fi
if [ -f "$SCRIPT_DIR/mcp-server/.env" ]; then
    set -a
    source "$SCRIPT_DIR/mcp-server/.env"
    set +a
fi

# MCP-Server bauen (HTTP-Wrapper auf Port 3001)
echo "MCP-Server wird gebaut..."
cd "$SCRIPT_DIR/mcp-server" && npm install && npm run build
if [ $? -ne 0 ]; then
    echo "Fehler: MCP-Server Build fehlgeschlagen. Script wird abgebrochen."
    exit 1
fi
echo "MCP-Server Build erfolgreich (dist/ bereit)."

# MCP_API_KEY fuer Backend verfuegbar machen
export MCP_API_KEY

# Funktion: Port freigeben und warten bis er frei ist
free_port() {
    local port=$1
    if lsof -ti:"$port" > /dev/null 2>&1; then
        echo "Port $port belegt — Prozess wird beendet..."
        kill -9 $(lsof -ti:"$port") 2>/dev/null
        # Warten bis Port tatsaechlich frei ist (max 10 Sekunden)
        local tries=0
        while lsof -ti:"$port" > /dev/null 2>&1; do
            sleep 1
            tries=$((tries + 1))
            if [ $tries -ge 10 ]; then
                echo "Fehler: Port $port konnte nicht freigegeben werden."
                exit 1
            fi
        done
        echo "Port $port freigegeben."
    fi
}

# Ports freigeben (MCP-Server zuerst, Backend braucht ihn)
free_port 3001
free_port 8080
free_port 5173

# Cleanup bei SIGINT/SIGTERM
# kill -- -$PID sendet Signal an die gesamte Prozessgruppe (inkl. Kind-Prozesse wie Java)
cleanup() {
    echo ""
    echo "Beende MCP-Server, Backend und Frontend..."
    # Ports direkt freigeben — erwischt auch Kind-Prozesse (z.B. Java hinter Maven)
    for port in 3001 8080 5173; do
        local pids=$(lsof -ti:"$port" 2>/dev/null)
        if [ -n "$pids" ]; then
            kill $pids 2>/dev/null
        fi
    done
    sleep 1
    # Falls noch etwas laeuft: force kill
    for port in 3001 8080 5173; do
        local pids=$(lsof -ti:"$port" 2>/dev/null)
        if [ -n "$pids" ]; then
            kill -9 $pids 2>/dev/null
        fi
    done
    echo "Beendet."
    exit 0
}

trap cleanup SIGINT SIGTERM

# MCP-Server HTTP-Wrapper starten (Backend braucht ihn)
echo "MCP-Server HTTP-Wrapper wird gestartet (Port 3001)..."
cd "$SCRIPT_DIR/mcp-server" && node dist/http-wrapper.js &
MCP_PID=$!

# Warten bis MCP-Server erreichbar ist (max 10s)
echo "Warte auf MCP-Server..."
mcp_tries=0
while ! curl -s http://localhost:3001/api/health > /dev/null 2>&1; do
    sleep 1
    mcp_tries=$((mcp_tries + 1))
    if [ $mcp_tries -ge 10 ]; then
        echo "Fehler: MCP-Server konnte nicht gestartet werden (Port 3001 nicht erreichbar)."
        kill $MCP_PID 2>/dev/null
        exit 1
    fi
done
echo "MCP-Server laeuft auf Port 3001."

# Backend starten
echo "Backend wird gestartet..."
cd "$SCRIPT_DIR/chatbot-backend" && MISTRAL_API_KEY="$1" ./mvnw spring-boot:run &
BACKEND_PID=$!

# Frontend starten
echo "Frontend wird gestartet..."
cd "$SCRIPT_DIR/chatbot-frontend" && npm run dev &
FRONTEND_PID=$!

echo ""
echo "==============================="
echo "  MCP-Server: http://localhost:3001"
echo "  Backend:    http://localhost:8080"
echo "  Frontend:   http://localhost:5173"
echo "  Beenden mit Ctrl+C"
echo "==============================="
echo ""

wait $MCP_PID $BACKEND_PID $FRONTEND_PID
