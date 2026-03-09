# Tag 2 – Aufgabe: PostgreSQL MCP-Server bauen + Chatbot verbinden

**Beide Teams gemeinsam**
**Zeit:** 12:45–15:15 Uhr
**Ziel:** Einen MCP-Server für die PostgreSQL-Datenbank bauen und den Chatbot aus Tag 1 damit verbinden

---

## Das Gesamtbild: Was entsteht heute?

```
VORHER (Tag 1):
  User: "Wie viele Kunden haben wir?"
  Chatbot: "Das weiß ich nicht, ich habe keinen DB-Zugriff."

NACHHER (Tag 2):
  User: "Wie viele Kunden haben wir?"
  Chatbot → ruft PostgreSQL MCP Tool auf → liest DB → antwortet:
  "Aktuell sind 1.847 Kunden in der Datenbank, davon 312 seit diesem Monat."
```

**Was ihr baut:**

```
Vue.js Frontend (Tag 1)
  ↓
Spring Boot Backend (Tag 1, wird heute erweitert)
  ↓ schickt Tool-Definitionen mit zur Claude API
Claude API
  ↓ ruft PostgreSQL-Tools auf
MCP Server (TypeScript) ← ihr baut das heute
  ↓
PostgreSQL Datenbank
```

---

## KI-Tool-Ablauf

```
Setup      Starter verstehen → Claude Code / Copilot   – Code erklären lassen
Schritt 1  DB-Connector      → Claude Code / Copilot   – PostgreSQL-Verbindung aufbauen
Schritt 2  MCP-Tools         → Claude Code / Copilot   – Tools implementieren
Schritt 3  Auth              → Claude Code / Copilot   – API-Key Schutz
Schritt 4  Test mit KI       → Claude Code (MCP aktiv) – KI nutzt euren Server!
Schritt 5  Chatbot erweitern → Claude Code / Copilot   – Spring Boot + Tool Use
Demo       Alles zusammen    → Freitext                – Chatbot beantwortet PostgreSQL-Fragen
```

**Besonderheit Tag 2:** Sobald der MCP-Server läuft, nutzt ihn Claude Code direkt –
die KI baut sich selbst weiter mit einem Tool das sie gerade gebaut hat.

---

## Ausgangssituation

Ihr habt:
- Den Chatbot aus Tag 1 (Spring Boot Backend + Vue.js Frontend)
- Zugriff auf die PostgreSQL-Datenbank (Verbindungsdaten vom Trainer)
- Den Starter-Code in `mcp-starter/` (TypeScript-Vorlage)

---

## Setup (12:45–13:15)

### Starter-Code vorbereiten

Der MCP-Starter liegt im Schulungs-Repo. Ihr arbeitet direkt darin:

```bash
cd ai-knowledgebase/Schulung/Tag-2-Coding-Assistant-MCP/mcp-starter
npm install
cp .env.example .env
```

Das dev-setup-template ist bereits vom Schulungs-Repo vorhanden – das `.mcp.json` und
`.vscode/mcp.json` in eurem Chatbot-Backend-Ordner werden in Schritt 4 ergänzt.

### `.env` mit PostgreSQL-Verbindungsdaten ausfüllen

```env
# PostgreSQL Verbindung (vom Trainer bereitgestellt)
DB_HOST=localhost
DB_PORT=5432
DB_NAME=schulungdb
DB_USER=schulung_user
DB_PASSWORD=schulung_pass

# MCP Server Absicherung
MCP_API_KEY=schulung-secret-2026
PORT=3100
```

### Starter-Code verstehen (15 Min)

Öffnet `server.ts` und fragt die KI:

```
Erkläre mir diesen MCP-Server-Code.
Gehe auf jede Funktion ein: Was macht sie, wofür ist sie da?
Der Server soll später PostgreSQL-Datenbankzugriff für einen KI-Chatbot bereitstellen.
Was muss ich noch implementieren damit die PostgreSQL-Verbindung funktioniert?
```

---

## Schritt 1: PostgreSQL-Datenbankverbindung (30 Min)

Der Starter-Code enthält eine Grundstruktur. Wir bauen die PostgreSQL-Verbindung auf.

```
Implementiere den db-connector.ts für PostgreSQL.

Context: Node.js TypeScript, PostgreSQL 15
Task:
  1. pg-Package nutzen (bereits in package.json)
  2. Verbindung aufbauen mit:
     host: process.env.DB_HOST
     port: process.env.DB_PORT (als Zahl)
     database: process.env.DB_NAME
     user: process.env.DB_USER
     password: process.env.DB_PASSWORD
  3. Funktion executeQuery(sql: string, params?: any[]): Promise<any[]>
     - Verbindung öffnen, Query ausführen, Verbindung schließen
     - Parameterized Queries nutzen (Sicherheit!)
  4. Funktion listTables(): Promise<string[]>
     - PostgreSQL: SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' ORDER BY table_name
  5. Funktion describeTable(tableName: string): Promise<ColumnInfo[]>
     - PostgreSQL: SELECT column_name, data_type, is_nullable FROM information_schema.columns WHERE table_schema = 'public' AND table_name = $1
     - ColumnInfo: { name: string, type: string, nullable: boolean }

Constraints:
  - Connection Pooling (new Pool()) für Performance
  - Tabellennamen nur aus Whitelist (keine direkte String-Interpolation)
  - Kein SELECT * zurückgeben – max. 100 Rows
```

**Test:**
```bash
npx ts-node -e "
const {listTables} = require('./db-connector');
listTables().then(tables => console.log('Tabellen:', tables)).catch(console.error);
"
```

---

## Schritt 2: MCP-Tools implementieren (50 Min)

```
Füge folgende MCP-Tools zum server.ts hinzu:

Tool 1: list-tables
  Beschreibung: "Listet alle Tabellen der PostgreSQL-Datenbank auf"
  Parameter: keine
  Implementierung: listTables() aus db-connector.ts
  Output: Formatierte Liste der Tabellennamen

Tool 2: describe-table
  Beschreibung: "Zeigt die Spaltenstruktur einer Tabelle (Spaltenname, Typ, nullable)"
  Parameter: tableName (string, required) – Name der Tabelle
  Implementierung: describeTable(tableName) aus db-connector.ts
  Sicherheit: Tabellennamen-Validierung (nur alphanumerisch + Unterstrich)

Tool 3: query-table
  Beschreibung: "Führt eine SELECT-Abfrage auf einer Tabelle aus (max. 50 Zeilen)"
  Parameter:
    - tableName (string, required)
    - columns (string[], optional, default: alle außer Passwörter/Schlüssel)
    - whereClause (string, optional) – nur einfache Bedingungen
    - limit (number, optional, default: 10, max: 50)
  Sicherheit:
    - NUR SELECT erlaubt (kein INSERT/UPDATE/DELETE/DROP)
    - Spalten CUSTOMERS_PASSWORD, PASSWORD, SECRET, API_KEY automatisch ausschließen
    - whereClause nur auf whitelist-Zeichen prüfen

Tool 4: count-rows
  Beschreibung: "Zählt die Anzahl der Einträge in einer Tabelle"
  Parameter: tableName (string, required), whereClause (string, optional)
  Implementierung: SELECT COUNT(*) FROM {table} WHERE {clause}

Kontext: TypeScript, @modelcontextprotocol/sdk, pg
```

---

## Schritt 3: API-Key Authentifizierung (20 Min)

```
Füge API-Key-Authentifizierung zur auth-middleware.ts hinzu.

Anforderungen:
  - API-Key aus process.env.MCP_API_KEY laden
  - Jede Tool-Anfrage prüft den Header x-api-key
  - Falscher oder fehlender Key: Fehler zurückgeben, kein Tool ausführen
  - Jeder Tool-Aufruf wird geloggt: Timestamp, Tool-Name, Erfolg/Fehler
  - Keine Parameter-Werte im Log (kein Daten-Logging)
```

---

## Schritt 4: In Claude Code einbinden + testen (20 Min)

```bash
# Server kompilieren und starten
npm run build
npm start
# → MCP Server läuft auf Port 3100
```

### `.mcp.json` im Chatbot-Backend-Projektordner ergänzen

```json
{
  "mcpServers": {
    "postgres-db": {
      "command": "node",
      "args": ["../ai-knowledgebase/Schulung/Tag-2-Coding-Assistant-MCP/mcp-starter/dist/server.js"],
      "env": {
        "DB_HOST": "localhost",
        "DB_PORT": "5432",
        "DB_NAME": "schulungdb",
        "DB_USER": "schulung_user",
        "DB_PASSWORD": "schulung_pass",
        "MCP_API_KEY": "schulung-secret-2026"
      }
    }
  }
}
```

### `.vscode/mcp.json` für Copilot

```json
{
  "servers": {
    "postgres-db": {
      "type": "stdio",
      "command": "node",
      "args": ["../ai-knowledgebase/Schulung/Tag-2-Coding-Assistant-MCP/mcp-starter/dist/server.js"],
      "env": {
        "DB_HOST": "localhost",
        "DB_PORT": "5432",
        "DB_NAME": "schulungdb",
        "DB_USER": "schulung_user",
        "DB_PASSWORD": "schulung_pass",
        "MCP_API_KEY": "schulung-secret-2026"
      }
    }
  }
}
```

### Test: KI nutzt euren Server direkt

Claude Code neu starten. Dann:

```
Nutze den postgres-db MCP-Server.
1. Welche Tabellen gibt es in der Datenbank?
2. Zeige mir die Struktur der größten Tabelle.
3. Wie viele Datensätze hat sie?
```

**Was ihr seht:** Claude ruft selbstständig `list-tables`, `describe-table` und `count-rows` auf –
ohne dass ihr einen Query selbst schreibt.

---

## Schritt 5: Chatbot Backend erweitern – Tool Use (30 Min)

Jetzt kommt der spannende Teil: Der Chatbot aus Tag 1 bekommt PostgreSQL-Zugriff.

**Konzept:** Claude API unterstützt "Tool Use" – ihr sendet die Tool-Definitionen
mit jedem API-Call, und Claude entscheidet ob es ein Tool aufrufen will.

```
Erweitere den LlmService im Spring Boot Backend (Tag 1) um Tool Use.

Konzept:
  1. Beim API-Call an Claude werden die PostgreSQL-Tool-Definitionen mitgesendet
     (list-tables, describe-table, query-table, count-rows)
  2. Claude antwortet entweder mit Text ODER mit einem Tool-Call
  3. Wenn Tool-Call: Spring Boot ruft den MCP-Server auf
     POST http://localhost:3100/tool/{toolName} mit den Parametern
  4. MCP-Server-Ergebnis wird zurück an Claude gesendet
  5. Claude antwortet mit der finalen Antwort

Task:
  a) PostgresMcpClient Service: ruft den MCP-Server auf
     POST http://localhost:3100/tool/{name} mit API-Key Header
  b) LlmService.sendMessage() erweitern:
     - Tool-Definitionen (als JSON) zum API-Request hinzufügen
     - Prüfen ob Response ein tool_use-Block ist
     - Falls ja: PostgresMcpClient aufrufen + Ergebnis an Claude zurückgeben
     - Schleife bis Claude mit Text antwortet

Constraints:
  - Max. 5 Tool-Calls pro Anfrage (Schutz gegen Endlosschleifen)
  - MCP-Server-URL in application.properties: postgres.mcp.url=http://localhost:3100
  - MCP-API-Key in application.properties: postgres.mcp.api-key=${POSTGRES_MCP_API_KEY}
```

---

## Demo: Chatbot beantwortet PostgreSQL-Fragen (15:00 Uhr)

Startet Backend + Frontend + MCP-Server. Dann im Chat:

```
"Wie viele Kunden haben wir insgesamt?"
"Zeige mir die neuesten 5 Bestellungen"
"Welche Tabellen gibt es in der Datenbank?"
"Wie viele Bestellungen gab es diese Woche?"
```

Der Chatbot beantwortet diese Fragen mit echten Daten aus PostgreSQL –
ohne dass ihr die Queries selbst geschrieben habt.

---

## Retrospektive (15:15 Uhr)

1. Was hat KI mit PostgreSQL-Zugriff anders gemacht als ohne?
2. Welche Sicherheitsrisiken habt ihr beim Implementieren entdeckt?
3. Für welche anderen Systeme im Unternehmen wäre ein MCP-Server wertvoll?
4. Was müsste noch gebaut werden damit das produktionstauglich ist?
