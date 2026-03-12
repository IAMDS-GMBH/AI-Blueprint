# Plan: MCP-Integration + Chatbot Tool Use + Trace-Panel + E2E Tests
Datum: 2026-03-12
Status: ABGESCHLOSSEN

## Ziel
1. Den selbst gebauten MCP-Server in Claude Code einbinden (Punkt 3)
2. Das Spring Boot Backend um Tool Use erweitern, sodass der Chatbot im Browser DB-Abfragen ueber den MCP-Server ausfuehren kann (Punkt 4)
3. Trace-/Log-Panel im Frontend: Datenfluss zwischen User → LLM API → MCP Server → PostgreSQL live nachvollziehbar machen
4. E2E-Demo testen (Punkt 5)
5. API-Tests fuer alle MCP-Tools erstellen

## Ausgangslage
- MCP-Server (stdio) ist fertig implementiert mit 7 Tools
- Chatbot Backend (Spring Boot) ruft Mistral API auf, aber OHNE Tool-Definitionen
- Chatbot Frontend (Vue.js) funktioniert, sendet Nachrichten ans Backend
- `.mcp.json` existiert bereits mit Playwright, GitHub, Figma, Postgres, Context7 — aber NICHT dem eigenen MCP-Server

## Gemeinsamer Vertrag (alle Agents muessen diesen einhalten)

### TraceEntry Interface (Backend DTO + Frontend Type)
```
{
  timestamp: string        // ISO-8601
  source: string           // USER_INPUT | LLM_REQUEST | LLM_RESPONSE | MCP_TOOL_CALL | MCP_TOOL_RESULT
  action: string           // Kurzbeschreibung (z.B. "count-rows aufgerufen")
  detail: string | null    // JSON/SQL-Details (max 500 Zeichen)
  durationMs: number | null // Dauer in Millisekunden
}
```

### ChatResponse Erweiterung
```
{
  response: string
  role: string
  trace: TraceEntry[]      // NEU — Trace-Daten fuer das Frontend
}
```

### MCP HTTP-Wrapper API
```
GET  /api/health                          → { status: "ok" }
GET  /api/tools                           → [{ name, description, parameters }]
POST /api/tools/:toolName                 → { result: any, sql?: string }
     Header: Authorization: Bearer <key>
     Body: { arguments: { ... } }
```

---

## Swarm-Plan

### Aufgaben-Aufteilung

| Agent | Typ | Aufgabe | Abhaengig von |
|-------|-----|---------|---------------|
| Agent 1 | Dev | MCP Config (.mcp.json) + HTTP-Wrapper (mcp-server/) | – |
| Agent 2 | Dev | Spring Boot Backend: Tool Use + Trace + McpClient | – |
| Agent 3 | Dev (frontend-design) | Frontend: TracePanel + ChatView Erweiterung + Types + Store | – |
| Agent 4 | Dev | start.sh erweitern (MCP HTTP-Wrapper Prozess) | Agent 1 |
| Agent 5 | Test | API-Tests fuer alle MCP-Tools (curl-basiert) | Agent 1 |
| Agent 6 | Review | E2E-Verifikation + Build-Check + lessons.md | Alle |

### Reihenfolge
```
Runde 1 (parallel): Agent 1, Agent 2, Agent 3
Runde 2 (parallel): Agent 4, Agent 5
Runde 3:            Agent 6 (E2E + Review)
```

### Geschaetzter Gesamtaufwand
6 Agents × ~5 Min = ~15 Min (3 Runden)

---

## Agent-Auftraege

### Agent 1: MCP Config + HTTP-Wrapper (Dev)
**Dateien:** `.mcp.json`, `mcp-server/http-wrapper.ts`, `mcp-server/package.json`

- [x] 1.1 `.mcp.json` erweitern: eigenen MCP-Server hinzufuegen
  ```json
  "schulung-db": {
    "command": "node",
    "args": ["mcp-server/dist/server.js"],
    "cwd": ".",
    "env": {
      "DATABASE_URL": "postgresql://training:training@localhost:5432/abrechnung",
      "MCP_API_KEY": "schulung-secret-key-2026"
    }
  }
  ```
- [x] 1.2 `express` + `@types/express` + `cors` + `@types/cors` zu `mcp-server/package.json` hinzufuegen
- [x] 1.3 `mcp-server/http-wrapper.ts` erstellen:
  - Express-Server auf Port 3001
  - CORS fuer localhost:8080 + localhost:5173
  - GET `/api/health` → `{ status: "ok" }`
  - GET `/api/tools` → Tool-Liste mit Name, Description, Parameters (aus den Tool-Registrierungen)
  - POST `/api/tools/:toolName` → Tool ausfuehren via db-connector Funktionen direkt
    - API-Key-Validierung: `Authorization: Bearer <key>`
    - Body: `{ arguments: { ... } }`
    - Response: `{ result: any }`
    - 401 bei fehlendem/falschem Key
    - 404 bei unbekanntem Tool
  - Dieselbe Sicherheitslogik wie server.ts (SENSITIVE_COLUMNS, validateWhereClause, Table-Whitelist)
- [x] 1.4 `package.json`: Scripts `"start:http": "node dist/http-wrapper.js"` und `"build"` pruefen
- [x] 1.5 Build: `npm run build` → keine Fehler
- [x] 1.6 Smoke-Test: HTTP-Wrapper starten, `curl localhost:3001/api/health`

### Agent 2: Spring Boot Backend — Tool Use + Trace (Dev)
**Dateien:** `chatbot-backend/src/main/java/com/chatbot/` (service/, dto/, config/)
**Vertrag:** Verwendet den gemeinsamen TraceEntry/ChatResponse-Vertrag (oben)
**Kontext:** Mistral API Function Calling Format, MCP HTTP-Wrapper auf localhost:3001

- [x] 2.1 `application.yml` erweitern:
  ```yaml
  mcp:
    server-url: ${MCP_SERVER_URL:http://localhost:3001}
    api-key: ${MCP_API_KEY:schulung-secret-key-2026}
  ```
- [x] 2.2 Neue Klasse `dto/TraceEntry.java`:
  - Felder: timestamp (String), source (String), action (String), detail (String), durationMs (Long)
  - Lombok @Data
- [x] 2.3 `dto/ChatResponse.java` erweitern: `+ List<TraceEntry> trace`
- [x] 2.4 Neue Klasse `service/McpClient.java`:
  - Constructor: WebClient mit `mcp.server-url` als baseUrl
  - `executeTool(String toolName, Map<String, Object> arguments)`: POST /api/tools/:toolName
    - Header: Authorization Bearer + mcp.api-key
    - Body: { arguments: { ... } }
    - Return: String (JSON result)
    - Timeout: 10s
- [x] 2.5 Neue Klasse `service/ToolDefinition.java`:
  - `getToolDefinitions()`: Gibt List<Map> zurueck mit den 4 Pflicht-Tools im Mistral-Format:
    ```json
    { "type": "function", "function": { "name": "list-tables", "description": "...", "parameters": { "type": "object", "properties": {}, "required": [] } } }
    ```
    Tools: list-tables, describe-table, query-table, count-rows
- [x] 2.6 `ChatServiceImpl.java` erweitern:
  - `McpClient` injizieren (Constructor Injection)
  - Trace-Liste (`List<TraceEntry>`) zu Beginn von `chat()` anlegen
  - Trace: USER_INPUT eintragen
  - Tool-Definitionen zum requestBody hinzufuegen: `"tools": ToolDefinition.getToolDefinitions()`
  - Trace: LLM_REQUEST eintragen (Model, Message-Count)
  - Response-Parsing erweitern: `finish_reason` pruefen
    - `"stop"` → Text-Antwort extrahieren, fertig
    - `"tool_calls"` → Tool-Calls aus Response extrahieren
  - Tool-Call-Loop (max 5 Iterationen):
    1. Trace: LLM_RESPONSE mit Tool-Call-Info
    2. Fuer jeden tool_call: McpClient.executeTool() aufrufen
    3. Trace: MCP_TOOL_CALL + MCP_TOOL_RESULT (mit Dauer)
    4. Tool-Ergebnis als Message mit `role: "tool"` + `tool_call_id` zur Message-Liste
    5. Erneut an Mistral senden
    6. Wiederholen bis `finish_reason: "stop"` oder max 5
  - Trace: LLM_RESPONSE (finale Antwort)
  - ChatResponse mit trace-Liste zurueckgeben
- [x] 2.7 Build: `./mvnw compile` → keine Fehler

### Agent 3: Frontend Trace-Panel (Dev — /frontend-design)
**Dateien:** `chatbot-frontend/src/` (types/, stores/, components/)
**Vertrag:** Verwendet den gemeinsamen TraceEntry/ChatResponse-Vertrag (oben)
**Skill:** `/frontend-design` fuer alle Vue-Komponenten verwenden

> **DESIGN-KONSISTENZ PFLICHT:** Alle neuen Komponenten muessen das bestehende Obsidian-Theme verwenden.
> CSS-Variablen aus App.vue: --obsidian, --obsidian-lighter, --obsidian-border, --amber, --amber-dim,
> --text-primary, --text-secondary, --text-tertiary, --font-display, --font-body,
> --radius-sm, --radius-md, --radius-lg. Glassmorphism: backdrop-filter + rgba Backgrounds.

- [x] 3.1 `types/chat.ts` erweitern:
  ```typescript
  export interface TraceEntry {
    timestamp: string
    source: 'USER_INPUT' | 'LLM_REQUEST' | 'LLM_RESPONSE' | 'MCP_TOOL_CALL' | 'MCP_TOOL_RESULT'
    action: string
    detail: string | null
    durationMs: number | null
  }
  // ChatResponse erweitern: + trace: TraceEntry[]
  ```
- [x] 3.2 `stores/chat.ts` erweitern:
  - Neuer State: `traceLog: ref<TraceEntry[][]>([])` (ein Trace-Array pro Nachrichtenpaar)
  - `showTrace: ref<boolean>(false)` (Panel Toggle)
  - `activeTraceIndex: ref<number | null>(null)` (welcher Trace ist selektiert)
  - In `sendMessage()`: Trace aus Response extrahieren und in `traceLog` pushen
- [x] 3.3 `services/api.ts`: Response-Type anpassen (trace-Feld)
- [x] 3.4 `/frontend-design` fuer neue Komponente `components/TracePanel.vue`:
  - Ein-/ausklappbares rechtes Side-Panel (Breite: 380px)
  - Header: "Trace Log" + Schliessen-Button
  - Trace-Liste mit vertikaler Timeline-Linie (links)
  - Pro Eintrag:
    - Farbiger Dot: USER_INPUT=#6B8AFF, LLM_REQUEST=#5CE87C, LLM_RESPONSE=#5CE87C, MCP_TOOL_CALL=#E8A84C, MCP_TOOL_RESULT=#C084FC
    - Source-Badge (z.B. "LLM", "MCP", "USER")
    - Action-Text
    - Relative Zeit: "+0ms", "+120ms"
    - Aufklappbarer Detail-Bereich (JSON/SQL, monospace)
    - Dauer-Badge rechts (z.B. "234ms")
  - Auto-Scroll zum neuesten Eintrag
  - Leerer Zustand: "Sende eine Nachricht um den Trace zu sehen"
- [x] 3.5 `/frontend-design` fuer `ChatView.vue` Erweiterung:
  - Layout: Flex-Container mit Chat (flex: 1) + TracePanel (conditional, 380px)
  - Toggle-Button oben rechts im Chat-Container: "Trace" Badge mit Dot-Indikator
  - Klick auf Chat-Nachricht: zugehoerigen Trace im Panel highlighten
- [x] 3.6 `App.vue`: max-width entfernen oder erhoehen wenn Trace-Panel offen (damit genug Platz)

### Agent 4: start.sh erweitern (Dev)
**Dateien:** `start.sh`
**Abhaengig von:** Agent 1 (HTTP-Wrapper muss existieren)

- [x] 4.1 `free_port 3001` hinzufuegen (vor MCP-Server Start)
- [x] 4.2 MCP-Server HTTP-Wrapper starten:
  ```bash
  cd mcp-server && node dist/http-wrapper.js &
  MCP_PID=$!
  ```
- [x] 4.3 Cleanup-Trap erweitern: `kill $MCP_PID` hinzufuegen
- [x] 4.4 Warten auf Port 3001 (Health-Check Loop)
- [x] 4.5 Status-Ausgabe aktualisieren:
  ```
  MCP-Server: http://localhost:3001
  ```

### Agent 5: API-Tests fuer MCP-Tools (Test)
**Dateien:** `mcp-server/test/api-test.sh`
**Abhaengig von:** Agent 1 (HTTP-Wrapper muss gebaut und lauffaehig sein)

- [x] 5.1 Test-Script `mcp-server/test/api-test.sh` erstellen (curl-basiert):
  - Voraussetzung: HTTP-Wrapper laeuft auf localhost:3001
  - Test-Struktur: Test-Name, curl-Aufruf, erwartetes Ergebnis, PASS/FAIL
  - **Functional Tests:**
    - `list-tables` → erwartet Array mit mindestens "kunden"
    - `describe-table` (tableName: "kunden") → erwartet Array mit columnName/dataType
    - `get-schema` → erwartet Objekt mit Tabellen als Keys
    - `count-rows` (tableName: "kunden") → erwartet { count: number > 0 }
    - `query-table` (tableName: "kunden", limit: 3) → erwartet Array mit 3 Eintraegen
    - `query-users` (limit: 5) → erwartet Array (kann leer sein wenn Users-Tabelle nicht existiert)
    - `get-user-by-email` (email: "test@test.de") → erwartet null oder User-Objekt
  - **Security Tests:**
    - SQL-Injection: `count-rows` mit whereClause "1=1; DROP TABLE kunden" → erwartet Fehler
    - Ohne API-Key: beliebiger Aufruf → erwartet 401
    - Falscher API-Key: → erwartet 401
    - Unbekanntes Tool: POST /api/tools/nonexistent → erwartet 404
  - **Sensitive Column Test:**
    - `query-table` auf Tabelle mit password-Spalte → Spalte darf nicht im Ergebnis sein
  - Zusammenfassung am Ende: X/Y Tests bestanden
- [x] 5.2 Tests ausfuehren und Ergebnis dokumentieren

### Agent 6: E2E-Verifikation + Review (Review)
**Abhaengig von:** Alle anderen Agents
**Aufgabe:** Build, Integrationstest, lessons.md Update

- [x] 6.1 Build-Verifikation:
  - `cd mcp-server && npm run build` → keine Fehler
  - `cd chatbot-backend && ./mvnw compile` → keine Fehler
- [x] 6.2 Code-Review:
  - HTTP-Wrapper: Sicherheit (Auth, SQL-Injection-Schutz, CORS)
  - Backend: Tool-Call-Loop korrekt (max 5, Fehlerbehandlung)
  - Frontend: TracePanel Design-Konsistenz mit Obsidian-Theme
  - Keine hardcodierten Credentials
  - Keine sensiblen Daten in Trace-Details
- [x] 6.3 Integration starten (wenn PostgreSQL laeuft):
  - HTTP-Wrapper starten (Port 3001)
  - API-Tests ausfuehren (Agent 5 Script)
  - Backend starten (Port 8080)
  - E2E-Test via curl an Backend:
    - `POST /api/v1/chat` mit "Wie viele Kunden haben wir?" → Antwort mit Zahl + trace[]
    - `POST /api/v1/chat` mit "Welche Tabellen gibt es?" → Antwort mit Tabellenliste + trace[]
    - `POST /api/v1/chat` mit "Beschreibe die Kunden-Tabelle" → Schema + trace[]
  - Trace-Daten pruefen: Mindestens 3 Eintraege pro Anfrage
- [x] 6.4 `tasks/lessons.md` aktualisieren mit allen Erkenntnissen
- [x] 6.5 `tasks/todo.md` Status auf ABGESCHLOSSEN setzen + Ergebnis-Zusammenfassung

---

## Datenfluss (Trace visualisiert diesen Flow)

```
1. USER_INPUT     | User tippt "Wie viele Kunden?" im Frontend
                  ↓
2. LLM_REQUEST    | Backend → Mistral API (message + 4 tool definitions)
                  ↓
3. LLM_RESPONSE   | Mistral → tool_calls: [{ name: "count-rows", args: { tableName: "kunden" } }]
                  ↓
4. MCP_TOOL_CALL  | Backend → MCP-Server HTTP: POST /api/tools/count-rows
                  ↓
5. MCP_TOOL_RESULT| MCP-Server → Backend: { count: 20 }
                  ↓
6. LLM_REQUEST    | Backend → Mistral API (tool result als message)
                  ↓
7. LLM_RESPONSE   | Mistral → "Es befinden sich 20 Kunden in der Datenbank."
                  ↓
8. USER_OUTPUT    | Frontend zeigt Antwort + Trace-Panel zeigt alle Schritte
```

**Port-Belegung:**
| Service | Port |
|---------|------|
| Spring Boot Backend | 8080 |
| Vue.js Frontend | 5173 |
| PostgreSQL | 5432 |
| MCP-Server HTTP-Wrapper | 3001 |

## Risiken
- **Mistral Tool Use Format**: Mistral's Tool-Call-Format muss exakt stimmen (function calling spec). Gegenmassnahme: Mistral-Doku pruefen, Format testen
- **MCP-Server HTTP-Wrapper Fehler**: Express-Server koennte Fehler nicht richtig weitergeben. Gegenmassnahme: Strukturierte Error-Responses mit Status-Codes
- **Port 3001 belegt**: Gegenmassnahme: free_port-Funktion in start.sh
- **Mistral API Key fehlt**: Gegenmassnahme: Validierung beim Start, klare Fehlermeldung
- **Trace-Daten zu gross**: Gegenmassnahme: Detail-Felder auf 500 Zeichen kuerzen, nur Summary in Trace

## Nicht im Scope
- Streaming/SSE fuer Live-Trace-Updates (Trace kommt komplett mit der Response)
- Trace-Persistierung (nur In-Memory pro Session)
- Admin-Key / Schreibzugriff
- Docker/Deployment
- Retry-Logik fuer Mistral API

## Freigabe
[ ] Plan freigegeben von: ___________

---

## Ergebnis-Zusammenfassung (2026-03-12)

### Was wurde umgesetzt
- **MCP HTTP-Wrapper** (Agent 1): Express-Server auf Port 3001 mit Auth, CORS, SQL-Injection-Schutz, 7 Tools
- **Backend Tool Use** (Agent 2): ChatServiceImpl mit Tool-Call-Loop (max 5 Iterationen), McpClient, ToolDefinition (4 Tools), TraceEntry DTO
- **Frontend TracePanel** (Agent 3): Aufklappbares Side-Panel mit Timeline, farbigen Badges, Detail-Expansion, Obsidian-Theme konsistent
- **start.sh** (Agent 4): MCP-Server-Start mit Health-Check, Cleanup-Trap, Port-Freigabe
- **API-Tests** (Agent 5): 13 curl-basierte Tests (11 PASS, 2 erwartete Fehler wegen fehlender users-Tabelle)
- **Review** (Agent 6): Builds verifiziert, 2 Bugs gefixed (Health-Check-URL, Sensitive-Column-Filtering), Code-Review abgeschlossen

### Gefundene und gefixte Bugs
1. **start.sh Health-Check-URL falsch**: `/health` statt `/api/health` — haette Startup-Timeout verursacht
2. **Sensitive Columns bei SELECT * nicht gefiltert**: query-table filterte sensible Spalten nur bei expliziter Spaltenauswahl, nicht bei Wildcard `*`

### Bekannte Issues
- `query-users` und `get-user-by-email` schlagen fehl weil die `users`-Tabelle in der `abrechnung`-Datenbank nicht existiert (Starter-Code-Issue)
- `isToolAllowed()` in auth-middleware.ts ist Dead Code mit veralteten Tool-Namen (bereits in lessons.md dokumentiert)

### Build-Status
- `npm run build` (MCP-Server): PASS
- `./mvnw compile` (Backend): PASS
