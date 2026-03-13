# Tag 2 – Aufgabe: MCP-Server bauen und Chatbot erweitern

**Arbeitsverzeichnis:** `Aufgabe/` (Fortsetzung von Tag 1)

---

## Ziel

Der Chatbot aus Tag 1 erhaelt Zugriff auf eine PostgreSQL-Datenbank — ueber einen selbst implementierten MCP-Server.

```
VORHER (Tag 1):
  User: "Wie viele Kunden haben wir?"
  Chatbot: "Das kann ich leider nicht beantworten."

NACHHER (Tag 2):
  User: "Wie viele Kunden haben wir?"
  Chatbot → MCP Tool → DB-Abfrage → "Aktuell befinden sich 1.847 Kunden in der Datenbank."
```

**Architektur:**
```
Vue.js Frontend
  ↓ POST /api/v1/chat
Spring Boot Backend
  ↓ sendet Nachricht + Tool-Definitionen
Mistral API
  ↓ antwortet mit Tool-Call-Request
Spring Boot Backend
  ↓ HTTP REST (POST /api/tools/:toolName)
HTTP-Wrapper (Express.js, Port 3001)
  ↓
MCP Server → PostgreSQL
```

> **Warum ein HTTP-Wrapper?** MCP nutzt stdio-Transport (stdin/stdout). Das Spring Boot Backend braucht eine HTTP-Bridge um die MCP-Tools aufrufen zu koennen.

**Datenbank:** Die PostgreSQL-Instanz wird ueber Docker Compose gestartet — siehe Setup-Schritte weiter unten.

---

## 1. Datenbank starten

```bash
cp -r <pfad-zum>/Database-Starter ./Database-Starter
cd Database-Starter && docker compose up -d
```

Die Datenbank enthaelt folgende Tabellen:
- `kunden`, `produkte`, `rechnungen`, `rechnungspositionen`, `zahlungen`, `mahnungen`

---

## 2. MCP-Server Projekt aufsetzen

### Starter-Code uebernehmen

```bash
cd Aufgabe
cp -r <pfad-zum>/Schulung/Tag-2-Coding-Assistant-MCP/mcp-starter ./mcp-server
cd mcp-server
npm install
cp .env.example .env
```

Die `.env`-Datei mit den PostgreSQL-Verbindungsdaten befuellen (werden vom Trainer bereitgestellt).

### Starter-Code analysieren

Die Datei `server.ts` oeffnen und von der KI erklaeren lassen.
Vor der Implementierung muss klar sein: Was ist vorhanden, was fehlt, wie funktioniert das MCP SDK?

> **Achtung — bekannte Probleme im Starter-Code:**
>
> - **`auth-middleware.ts`:** Die Funktion `validateApiKey()` wirft "Noch nicht implementiert" — muss implementiert werden.
> - **`isToolAllowed()`:** Ist Dead Code mit Tool-Namen die nicht existieren (`health-check`, `user-statistics`). Die tatsaechlichen Tools (`list-tables`, `describe-table`, `count-rows`, `query-table`) fehlen. Funktion entfernen oder an die echten Tools anpassen.
> - **`query-users` / `get-user-by-email`:** Diese Tools greifen auf eine `users`-Tabelle zu die in der `abrechnung`-Datenbank nicht existiert. Durch generische Tools ersetzen (siehe Anforderungen unten).

---

## 3. MCP-Server implementieren

Ueber den **Plan Mode** einen Implementierungsplan erstellen lassen.

### Anforderungen

**Datenbank-Verbindung:**
- PostgreSQL-Verbindung ueber das `pg` Package (Connection Pooling)
- Verbindungsdaten aus Environment Variables
- Parameterized Queries (keine String-Konkatenation)

**MCP-Tools:**

| Tool | Funktion | Parameter |
|------|----------|-----------|
| `list-tables` | Alle Tabellen der Datenbank auflisten | keine |
| `describe-table` | Spaltenstruktur einer Tabelle anzeigen | `tableName` |
| `query-table` | SELECT-Abfrage ausfuehren (max. 50 Zeilen) | `tableName`, `columns`, `whereClause`, `limit` |
| `count-rows` | Anzahl der Eintraege zaehlen | `tableName`, `whereClause` |

**Sicherheitsanforderungen:**
- Ausschliesslich SELECT-Abfragen erlaubt
- Sensible Spalten automatisch ausschliessen (PASSWORD, SECRET, API_KEY) — auf **allen** Code-Pfaden, auch bei `SELECT *`
- Tabellennamen validieren
- API-Key-Authentifizierung fuer alle Tool-Aufrufe
- Keine Query-Parameter in Logs

Plan pruefen, dann umsetzen lassen.

---

## 4. HTTP-Wrapper implementieren

Da MCP ueber stdio kommuniziert, wird ein HTTP-Wrapper (Express.js) benoetigt, der die MCP-Tools als REST-Endpoints bereitstellt.

### Anforderungen

| Endpoint | Funktion |
|----------|----------|
| `GET /api/health` | Health-Check |
| `GET /api/tools` | Liste aller verfuegbaren Tools |
| `POST /api/tools/:toolName` | Tool ausfuehren (Parameter im Request Body) |

- Port: **3001**
- Auth: Bearer Token via `MCP_API_KEY` Environment Variable
- Fehler als JSON mit HTTP-Statuscode zurueckgeben

---

## 5. MCP-Server in das KI-Tool einbinden

Der selbst gebaute MCP-Server kann vom KI-Tool direkt genutzt werden.

**MCP-Konfiguration hinzufuegen:**

| Tool | Konfigurationsdatei |
|------|---------------------|
| Claude Code | `.mcp.json` |
| GitHub Copilot | `.vscode/mcp.json` |
| Mistral Vibe | `.vibe/config.toml` |

> Die KI kann bei der korrekten MCP-Konfiguration unterstuetzen.

**Verifikation:**

Das KI-Tool im Chatbot-Backend-Ordner neu starten und testen:
- "Welche Tabellen gibt es in der Datenbank?"
- "Wie ist die Struktur der groessten Tabelle?"
- "Wie viele Datensaetze enthaelt die Kunden-Tabelle?"

Die KI ruft die selbst implementierten Tools auf und beantwortet die Fragen mit echten Daten.

---

## 6. Chatbot Backend erweitern — Tool Use

Das Spring Boot Backend aus Tag 1 muss um **Tool Use** erweitert werden,
damit der Chatbot im Browser die Datenbank-Tools nutzen kann.

### Konzept

```
1. Frontend sendet Chat-Nachricht ans Backend
2. Backend sendet Nachricht + Tool-Definitionen an LLM API
3. LLM entscheidet: Text-Antwort ODER Tool-Aufruf
4. Bei Tool-Aufruf: Backend ruft HTTP-Wrapper auf (POST /api/tools/:toolName)
5. Ergebnis zurueck an LLM → LLM formuliert finale Antwort
6. Antwort zurueck ans Frontend
```

### Anforderungen

Im `chatbot-backend`-Verzeichnis ueber den Plan Mode vorgehen:

- LLM Service erweitern: Tool-Definitionen im API-Request mitschicken
- Tool-Call-Handling: Wenn das LLM ein Tool aufrufen moechte, den HTTP-Wrapper ansprechen
- Ergebnis-Schleife: Ergebnis zurueck an das LLM senden, bis eine Text-Antwort kommt
- Schutz: Maximal 5 Tool-Calls pro Anfrage
- MCP-Server URL und API-Key in `application.properties` (oder `application.yml`)

---

## 7. start.sh erweitern

Das `start.sh` aus Tag 1 um den MCP-Server erweitern:

1. MCP-Server bauen (`npm run build`)
2. HTTP-Wrapper starten (Port 3001)
3. Health-Check warten (`/api/health` erreichbar)
4. Dann Backend + Frontend starten (wie Tag 1)

---

## 8. Demo

Alle Komponenten starten: HTTP-Wrapper + MCP-Server + Backend + Frontend.

Im Chat testen:
- "Wie viele Kunden haben wir insgesamt?"
- "Zeige mir die neuesten 5 Bestellungen."
- "Welche Tabellen gibt es in der Datenbank?"

---

## Retrospektive

1. Welchen Unterschied macht der Datenbank-Zugriff fuer die Qualitaet der Antworten?
2. Welche Sicherheitsrisiken wurden beim Implementieren identifiziert?
3. Wie hat der Plan Mode bei der Backend-Erweiterung geholfen?
4. Fuer welche anderen Systeme im Unternehmen waere ein MCP-Server sinnvoll?
5. Erkenntnisse in `tasks/lessons.md` eintragen
