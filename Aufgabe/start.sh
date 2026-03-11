#!/bin/bash
# Aufruf: ./start.sh <MISTRAL_API_KEY>

if [ -z "$1" ]; then
    echo "Fehler: Kein API-Key angegeben."
    echo "Aufruf: ./start.sh <MISTRAL_API_KEY>"
    exit 1
fi

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

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

# Ports freigeben
free_port 8080
free_port 5173

# Cleanup bei SIGINT/SIGTERM
cleanup() {
    echo ""
    echo "Beende Backend und Frontend..."
    kill $BACKEND_PID 2>/dev/null
    kill $FRONTEND_PID 2>/dev/null
    # Auch Kind-Prozesse beenden
    wait $BACKEND_PID 2>/dev/null
    wait $FRONTEND_PID 2>/dev/null
    echo "Beendet."
    exit 0
}

trap cleanup SIGINT SIGTERM

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
echo "  Backend:  http://localhost:8080"
echo "  Frontend: http://localhost:5173"
echo "  Beenden mit Ctrl+C"
echo "==============================="
echo ""

wait $BACKEND_PID $FRONTEND_PID
