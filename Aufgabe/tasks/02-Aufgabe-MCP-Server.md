# Tag 2 – Aufgabe: MCP-Server bauen und Chatbot erweitern

**Arbeitsverzeichnis:** `Aufgaben/` (Fortsetzung von Tag 1)

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
Vue.js Frontend (Tag 1)
  ↓
Spring Boot Backend (Tag 1, wird erweitert)
  ↓ sendet Tool-Definitionen an LLM API
LLM API
  ↓ ruft Tools auf
MCP Server (TypeScript) ← wird heute gebaut
  ↓
PostgreSQL Datenbank
```

**Datenbank:** Die PostgreSQL-Instanz wird ueber Docker Compose gestartet – siehe `Database-Starter/` im selben Verzeichnis.

---

## 1. MCP-Server Projekt aufsetzen

### Starter-Code uebernehmen

```bash
cd Aufgaben
cp -r <pfad-zum>/Schulung/Tag-2-Coding-Assistant-MCP/mcp-starter ./mcp-server
cd mcp-server
npm install
cp .env.example .env
```

Die `.env`-Datei mit den PostgreSQL-Verbindungsdaten befuellen (werden vom Trainer bereitgestellt).

### Starter-Code analysieren

Die Datei `server.ts` oeffnen und von der KI erklaeren lassen.
Vor der Implementierung muss klar sein: Was ist vorhanden, was fehlt, wie funktioniert das MCP SDK?

---

## 2. MCP-Server implementieren

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
- Sensible Spalten automatisch ausschliessen (PASSWORD, SECRET, API_KEY)
- Tabellennamen validieren
- API-Key-Authentifizierung fuer alle Tool-Aufrufe
- Keine Query-Parameter in Logs

Plan pruefen, dann umsetzen lassen.

---

## 3. MCP-Server in das KI-Tool einbinden

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

## 4. Chatbot Backend erweitern — Tool Use

Das Spring Boot Backend aus Tag 1 muss um **Tool Use** erweitert werden,
damit der Chatbot im Browser die Datenbank-Tools nutzen kann.

### Konzept

```
1. Frontend sendet Chat-Nachricht ans Backend
2. Backend sendet Nachricht + Tool-Definitionen an LLM API
3. LLM entscheidet: Text-Antwort ODER Tool-Aufruf
4. Bei Tool-Aufruf: Backend ruft MCP-Server auf
5. Ergebnis zurueck an LLM → LLM formuliert finale Antwort
6. Antwort zurueck ans Frontend
```

### Anforderungen

Im `chatbot-backend`-Verzeichnis ueber den Plan Mode vorgehen:

- LLM Service erweitern: Tool-Definitionen im API-Request mitschicken
- Tool-Call-Handling: Wenn das LLM ein Tool aufrufen moechte, den MCP-Server ansprechen
- Ergebnis-Schleife: MCP-Ergebnis zurueck an das LLM senden, bis eine Text-Antwort kommt
- Schutz: Maximal 5 Tool-Calls pro Anfrage
- MCP-Server URL und API-Key in `application.properties`

---

## 5. Demo

Alle Komponenten starten: MCP-Server + Backend + Frontend.

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
