# Tag 2 (Nachmittag) – MCP-Server: KI mit der Außenwelt verbinden

## Lernziele
- Verstehen was MCP ist und warum KI ein eigenes Protokoll braucht
- Einen MCP-Server von Grund auf aufbauen
- PostgreSQL-Datenbank über MCP an KI anbinden
- Authentifizierung und Autorisierung im MCP-Kontext implementieren

---

## 1. Das Problem: Warum MCP besser ist als REST für KI

KI *kann* REST-APIs nutzen (über Tool Use / Function Calling). Aber MCP löst Probleme die REST nicht adressiert:

| Aspekt | REST API + Tool Use | MCP |
|--------|---------------------|-----|
| Tool-Discovery | Ihr definiert Tools manuell in JSON | Server liefert Tool-Liste automatisch |
| Schema-Inference | Ihr schreibt JSON Schema pro Endpoint | Server deklariert Parameter + Typen |
| Fehlerbehandlung | HTTP-Statuscodes → KI muss interpretieren | Standardisiertes Error-Format mit Kontext |
| Transport | HTTP/HTTPS (ein Protokoll) | stdio (lokal, kein Netzwerk) oder HTTP |
| Multi-Client | Für jeden Client neu integrieren | Einmal bauen → Claude Code, Copilot, jeder MCP-Client |

**Technisch: Was passiert bei einem MCP-Tool-Call?**
```
1. Claude Code startet MCP-Server als Child-Process (stdio)
   → Server läuft lokal, keine HTTP-Verbindung nötig

2. Server schickt Tool-Liste an Claude:
   { tools: [{ name: "query-table", description: "...", inputSchema: {...} }] }

3. Claude entscheidet basierend auf User-Prompt:
   "Wie viele Kunden haben wir?" → wählt "query-table" weil description passt

4. Claude sendet Tool-Call:
   { name: "query-table", arguments: { tableName: "CUSTOMERS", limit: 1 } }
   → Query: SELECT COUNT(*) FROM CUSTOMERS

5. Server antwortet mit Ergebnis → Claude formuliert Antwort
```

**Wichtig:** Die `description` eurer Tools ist ein Prompt an die KI. Sie entscheidet anhand dieser Beschreibung welches Tool sie aufruft. Schlechte Description = falsches Tool wird gewählt.

---

## 2. Was ist MCP?

**MCP = Model Context Protocol**

Ein offener Standard (von Anthropic, Open Source) der definiert, wie KI-Modelle mit externen Tools und Datenquellen kommunizieren – standardisiert, typsicher und wiederverwendbar.

```
Ohne MCP:           KI ← Text-Prompt → Du ← manuell → System
Mit MCP:            KI ← MCP-Protokoll → MCP-Server → System (direkt)
```

**Die USB-Analogie:**
```
Vor USB:   Jedes Gerät = eigener Anschluss (Drucker, Scanner, Kamera = 3 verschiedene Ports)
Mit USB:   Ein Standard → jedes Gerät funktioniert überall

Vor MCP:   Jedes Tool = eigene Integration (DB, Browser, GitHub = 3 verschiedene Implementierungen)
Mit MCP:   Ein Protokoll → jeder MCP-Server funktioniert in jedem MCP-Client
```

### MCP-Clients (die KI nutzt den Server)
- Claude Code, Claude Desktop
- **Mistral Vibe CLI** (stdio + http, konfiguriert in `.vibe/config.toml`)
- **Le Chat** (Mistral) — nur Remote MCP-Server via SSE, kein stdio
- GitHub Copilot (VS Code)
- Cursor, Windsurf, Cline
- Jede App die das MCP-Protokoll implementiert

> **Gut zu wissen:** MCP-Server die ihr in dieser Aufgabe baut funktionieren mit Claude Code UND Mistral Vibe. Einmal bauen, überall nutzen.

**MCP-Config: Claude Code vs. Mistral Vibe:**

Claude Code (`.mcp.json` — JSON):
```json
{ "mcpServers": { "postgres": { "command": "npx", "args": ["-y", "@modelcontextprotocol/server-postgres"] } } }
```

Mistral Vibe (`.vibe/config.toml` — TOML):
```toml
[[mcp_servers]]
name = "postgres"
transport = "stdio"
command = "npx"
args = ["-y", "@modelcontextprotocol/server-postgres"]
env = { POSTGRES_CONNECTION_STRING = "${POSTGRES_CONNECTION_STRING}" }
```

---

## 3. MCP auf Protokoll-Ebene

### Das Protokoll: JSON-RPC 2.0

MCP basiert auf **JSON-RPC 2.0** – ein einfaches Request/Response-Format über JSON:

```typescript
// Client → Server: "Welche Tools hast du?"
{ "jsonrpc": "2.0", "method": "tools/list", "id": 1 }

// Server → Client: "Hier sind meine Tools"
{ "jsonrpc": "2.0", "result": {
    "tools": [{
      "name": "query-table",
      "description": "Führt eine SELECT-Query auf einer Tabelle aus",
      "inputSchema": {
        "type": "object",
        "properties": {
          "tableName": { "type": "string" },
          "limit": { "type": "number", "default": 50 }
        },
        "required": ["tableName"]
      }
    }]
  }, "id": 1 }

// Client → Server: "Führe dieses Tool aus"
{ "jsonrpc": "2.0", "method": "tools/call", "params": {
    "name": "query-table",
    "arguments": { "tableName": "CUSTOMERS", "limit": 10 }
  }, "id": 2 }

// Server → Client: "Hier ist das Ergebnis"
{ "jsonrpc": "2.0", "result": {
    "content": [{ "type": "text", "text": "10 Kunden gefunden: ..." }]
  }, "id": 2 }
```

### Transport: Wie Server und Client kommunizieren

**stdio (Standard für lokale Server):**
```
Claude Code startet den MCP-Server als Child-Process
  → Kommunikation über stdin/stdout (wie eine Pipe)
  → Kein HTTP, kein Port, kein Netzwerk
  → Server läuft nur solange Claude Code läuft
```

**HTTP + SSE (für remote/shared Server):**
```
MCP-Server läuft als Web-Service (z.B. auf einem internen Server)
  → Client verbindet sich über HTTP
  → Server-Sent Events (SSE) für Streaming-Antworten
  → Mehrere Clients können sich gleichzeitig verbinden
```

**Für unsere Schulung:** Wir nutzen **stdio** – einfacher, sicherer, kein Netzwerk-Setup.

### Der MCP-Lifecycle (4 Phasen)

```
Phase 1: INITIALIZE
  Client startet Server → Server meldet seine Capabilities
  (welche Features unterstützt er: tools, resources, prompts?)

Phase 2: DISCOVERY
  Client fragt: tools/list, resources/list
  → Server liefert alle verfügbaren Tools mit Namen, Descriptions, Schemas

Phase 3: OPERATION
  Client ruft Tools auf: tools/call → Server führt aus → Ergebnis zurück
  (kann beliebig oft wiederholt werden)

Phase 4: SHUTDOWN
  Client beendet Verbindung → Server stoppt (bei stdio)
```

---

## 4. Einen MCP-Server bauen (TypeScript)

### Minimaler Server mit dem offiziellen SDK

```typescript
import { McpServer } from "@modelcontextprotocol/sdk/server/mcp.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";
import { z } from "zod";

// 1. Server erstellen
const server = new McpServer({
  name: "demo-server",
  version: "1.0.0"
});

// 2. Tool registrieren
server.tool(
  "greet",                              // Tool-Name
  "Begrüßt einen User mit Namen",       // Description (= Prompt für die KI!)
  { name: z.string() },                 // Input-Schema (Zod-Validierung)
  async ({ name }) => ({                // Handler-Funktion
    content: [{ type: "text", text: `Hallo ${name}!` }]
  })
);

// 3. Transport starten (stdio)
const transport = new StdioServerTransport();
await server.connect(transport);
```

**Das ist ein vollständiger MCP-Server in 15 Zeilen.** Er kann sofort in Claude Code oder Copilot genutzt werden.

### Resource registrieren

```typescript
server.resource(
  "db-schema",                                    // Resource-Name
  "db://schema",                                  // URI
  async () => ({
    contents: [{
      uri: "db://schema",
      text: "CUSTOMERS(id, name, email)\nORDERS(id, customer_id, total)"
    }]
  })
);
```

---

## 5. Anatomie eines MCP-Servers

Ein MCP-Server stellt drei Dinge bereit:

### Tools (Aktionen)
Was die KI *tun* kann – mit Parametern und Rückgabewerten.
```typescript
{
  name: "get-user",
  description: "Lädt einen User aus der Datenbank anhand der ID",
  inputSchema: {
    type: "object",
    properties: {
      userId: { type: "string", description: "UUID des Users" }
    },
    required: ["userId"]
  }
}
```

### Resources (Daten)
Was die KI *lesen* kann – statische oder dynamische Datenquellen.
```typescript
{
  uri: "db://schema",
  description: "Aktuelles Datenbankschema mit allen Tabellen und Spalten"
}
```

### Prompts (Vorlagen)
Vorgefertigte Prompt-Templates für häufige Aufgaben.
```typescript
server.prompt(
  "analyze-table",
  { tableName: z.string() },
  ({ tableName }) => ({
    messages: [{
      role: "user",
      content: { type: "text", text: `Analysiere die Tabelle ${tableName}: Struktur, Datenqualität, Anomalien.` }
    }]
  })
);
```

### Wie Claude entscheidet welches Tool aufgerufen wird (Tool-Auswahl)

Die KI bekommt alle Tool-Definitionen als Teil ihres System-Prompts. Pro Tool: `name`, `description`, `inputSchema`. Die KI matcht den User-Prompt gegen die Descriptions.

**Schlechte Tool-Description → KI wählt falsches Tool:**
```typescript
// ❌ Zu vage – KI weiß nicht wann sie es nutzen soll
{ name: "get-data", description: "Holt Daten aus der Datenbank" }

// ❌ Zu ähnlich zu anderem Tool – KI verwechselt sie
{ name: "list-tables", description: "Zeigt Tabellen" }
{ name: "describe-table", description: "Zeigt Tabellendetails" }
```

**Gute Tool-Description → KI wählt zuverlässig:**
```typescript
// ✅ Klar, spezifisch, mit Anwendungsfall
{ name: "list-tables",
  description: "Listet alle Tabellennamen der PostgreSQL-Datenbank auf.
                Nutze dieses Tool als ERSTEN Schritt um zu verstehen
                welche Daten verfügbar sind." }

{ name: "describe-table",
  description: "Zeigt die Spaltenstruktur einer einzelnen Tabelle
                (Spaltenname, Datentyp, nullable). Nutze dieses Tool
                NACHDEM du list-tables aufgerufen hast." }
```

→ **Tipp:** Beschreibungen sollen der KI sagen *wann* und *warum* sie das Tool nutzen soll – nicht nur *was* es tut.

### Token-Kosten von MCP-Tools

Jeder MCP-Server kostet Tokens – auch wenn kein Tool aufgerufen wird:

```
Tool-Definitionen werden bei JEDEM API-Call mitgesendet:
  4 Tools × ~200 Tokens pro Definition = ~800 Tokens pro Nachricht
  → Bei 100 Nachrichten in einer Session: 80.000 Tokens nur für Tool-Definitionen

Tool-Call Ergebnis:
  SELECT * FROM CUSTOMERS LIMIT 50 → ~2.000-5.000 Tokens für die Response
  → Große Ergebnisse füllen den Context schneller
```

**Deshalb:**
- Max. 50 Rows pro Query (nicht 1000) → Token-Sparsamkeit
- Sensible Spalten (Passwörter) filtern → weniger nutzlose Tokens
- Nicht mehr MCP-Server konfigurieren als nötig → Tool-Definitionen kosten

---

## 6. MCP in der Praxis: Konfiguration

### In Claude Code (`.mcp.json`):
```json
{
  "mcpServers": {
    "mein-server": {
      "command": "node",
      "args": ["./mcp-server/dist/server.js"],
      "env": {
        "DATABASE_URL": "postgresql://localhost:5432/schulungdb"
      }
    }
  }
}
```

### In VS Code / Copilot (`.vscode/mcp.json`):
```json
{
  "servers": {
    "mein-server": {
      "type": "stdio",
      "command": "node",
      "args": ["./mcp-server/dist/server.js"]
    }
  }
}
```

**Danach kann KI sagen:**
> "Ich nutze jetzt `get-user` aus dem MCP-Server um den User zu laden..."

---

## 7. MCP + PostgreSQL

Warum ist das so mächtig?

```
Ohne MCP: Du schreibst Queries manuell, KI kann nur Vorschläge machen
Mit MCP:  KI führt Queries selbst aus, liest Schema, analysiert Daten
```

**Beispiel-Workflow:**
1. KI liest Datenbankschema via Resource `db://schema`
2. KI analysiert Tabellen und Beziehungen
3. KI schreibt Query und führt ihn via Tool `execute-query` aus
4. KI interpretiert das Ergebnis und antwortet auf natürliche Sprache

---

## 8. Authentifizierung & Autorisierung

Ein MCP-Server der auf eine Datenbank zugreift braucht Schutz.

### Ebene 1: Transport-Security (MCP-Server selbst)
```typescript
// API-Key in Header prüfen
if (request.headers['x-api-key'] !== process.env.MCP_API_KEY) {
  return { error: "Unauthorized" }
}
```

### Ebene 2: Datenbank-Berechtigungen
```sql
-- Nur lesender DB-User für den MCP-Server
CREATE USER mcp_reader WITH PASSWORD '...';
GRANT SELECT ON ALL TABLES IN SCHEMA public TO mcp_reader;
-- Kein INSERT, UPDATE, DELETE
```

### Ebene 3: Tool-Level Authorization
```typescript
// Schreibende Tools nur für bestimmte Rollen erlauben
if (toolName === 'delete-user' && !hasAdminRole(apiKey)) {
  return { error: "Insufficient permissions" }
}
```

### Konkret: SQL-Injection im MCP-Kontext

**Das Risiko:** Die KI generiert den WHERE-Clause – und könnte (halluziniert oder durch Prompt Injection) schädliches SQL erzeugen.

```typescript
// ❌ UNSICHER: KI-generierter String direkt in Query
const query = `SELECT * FROM ${tableName} WHERE ${whereClause}`;
// → KI könnte generieren: whereClause = "1=1; DROP TABLE CUSTOMERS--"

// ✅ SICHER: Parameterized Query + Whitelist
const ALLOWED_TABLES = ['CUSTOMERS', 'ORDERS', 'PRODUCTS'];
if (!ALLOWED_TABLES.includes(tableName.toUpperCase())) {
  throw new Error(`Tabelle ${tableName} nicht erlaubt`);
}
// whereClause nur einfache Bedingungen erlauben (Regex-Validierung)
if (!/^[A-Z_]+ (=|>|<|LIKE) :[0-9]+$/.test(whereClause)) {
  throw new Error('Ungültige WHERE-Bedingung');
}
const result = await connection.execute(
  `SELECT * FROM ${tableName} WHERE ${whereClause}`,
  bindParams  // Parameterized!
);
```

### Sicherheitsregeln:
- [ ] Keine Datenbankpasswörter im Code – nur Umgebungsvariablen
- [ ] Minimale DB-Rechte (Principle of Least Privilege)
- [ ] SQL-Injection-Schutz: Parameterized Queries + Tabellen-Whitelist
- [ ] Sensible Spalten (PASSWORD, SECRET, API_KEY) automatisch ausfiltern
- [ ] Rate Limiting: Max. 10 Tool-Calls pro Minute pro Client
- [ ] Logging: Jeder Tool-Call wird geloggt (aber KEINE Query-Parameter → Datenschutz)

---

## 8b. Security-Checklist für KI-Einsatz im Unternehmen

Über MCP hinaus gelten diese Security-Grundregeln für jeden KI-Einsatz:

```
✅ Datenklassifizierung:   Welche Daten dürfen an welche KI? (Cloud vs. lokal)
✅ Anbieter-Compliance:    DSGVO, SOC2, ISO 27001 geprüft?
✅ API-Key-Management:     Sichere Verwaltung aller KI-API-Keys (Secret Manager, Rotation)
✅ Output-Validierung:     KI-Outputs vor Nutzung immer prüfen
✅ Logging:                Alle KI-Interaktionen auditierbar loggen
✅ Lokale Alternativen:    Sensible Daten nur mit lokalen Modellen (OLLAMA)
✅ Security Scans:         Automatisch in CI/CD integriert (Claude Code Security Scans)
✅ Penetration Testing:    Regelmäßig mit KI-Support
```

**Risiko-Matrix:**

| Risiko | Wahrscheinlichkeit | Impact | Maßnahme |
|--------|-------------------|--------|----------|
| Datenleck an Cloud-KI | Mittel | Hoch | Datenklassifizierung, lokale Modelle |
| Halluzinierte Code-Fehler | Hoch | Mittel | Code Review, Security Scans, Tests |
| API-Key-Exposure | Niedrig | Hoch | Secret Management, Key Rotation |
| Compliance-Verstöße | Niedrig | Sehr Hoch | Regelmäßige Audits, Trusted AI Framework |

> Vollständige Details → `KI-Wissensbasis/10-KI-Security.md`

---

## 9. Live-Demo: Playwright MCP (Microsoft, offiziell)

Der Playwright MCP-Server ist schon fertig – einfach nutzen:

**Was ihn besonders macht:**
- Entwickelt von Microsoft, offiziell unterstützt
- Nutzt **Accessibility-Snapshots** statt Screenshots → kein Vision-Modell nötig, deutlich token-effizienter
- KI sieht die DOM-Struktur direkt → zuverlässigere, deterministische Interaktionen
- **Selbstheilende Tests:** KI analysiert den DOM und repariert kaputte Tests eigenständig

```json
// .mcp.json
{
  "mcpServers": {
    "playwright": {
      "command": "npx",
      "args": ["@playwright/mcp@latest"]
    }
  }
}
```

**Demo-Prompt:**
```
Navigiere zu http://localhost:5173 (unser Frontend von Tag 1).
Öffne die Login-Seite, fülle das Formular mit test@example.com / Test1234! aus,
klicke auf Login und prüfe ob die Profil-Seite geladen wird.
Berichte was du siehst.
```

→ KI verifiziert ihre eigene Arbeit im Browser – ohne Screenshot, ohne Vision-Modell!

**Warum das mächtiger ist als manuelles Testing:**
```
Klassisch:  KI schreibt Test → Du führst aus → Du schickst Fehler zurück → KI fixt
Mit MCP:    KI schreibt Test → führt ihn selbst aus → sieht Fehler → fixt direkt
```

---

## Zusammenfassung: Wann MCP, wann direkte API?

| Situation | Empfehlung |
|-----------|-----------|
| KI soll DB-Schema verstehen und analysieren | MCP |
| KI soll wiederholt auf Daten zugreifen | MCP |
| Einmalige API-Abfrage im Prompt | Direkt via URL |
| KI soll Daten transformieren ohne externe Quelle | Kein MCP nötig |
| Team nutzt denselben Datenzugriff ständig | MCP (einmal bauen, alle nutzen) |
