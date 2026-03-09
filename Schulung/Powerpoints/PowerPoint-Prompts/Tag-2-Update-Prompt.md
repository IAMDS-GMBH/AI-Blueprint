# Update-Prompt: Tag 2 Präsentation anpassen

## Anweisung
Die Tag-2-Präsentation existiert bereits mit dem alten Design und der alten Folienstruktur. Führe folgende Änderungen durch — bestehende Folien anpassen, neue einfügen wo angegeben.

---

## 1. Design-Update (ALLE Folien)

Ändere das Farbschema der gesamten Präsentation:

| Element | ALT | NEU |
|---------|-----|-----|
| Primärfarbe (Überschriften, Highlights) | Dunkelblau #1a365d | Deep Blue #3f59ff |
| Titel, dunkle Flächen | Dunkelblau #1a365d | Navy #212b80 |
| Akzentfarbe (Hervorhebungen) | Orange #ed8936 | Golden Yellow #ffd440 |
| Hintergrund | Grau #f7fafc | Weiß #ffffff |
| Text | Schwarz | Dunkelgrau #212121 |

- Schriftart auf **Open Sans** ändern (Calibri als Fallback)
- Mehr Weißraum zwischen Elementen — clean, modern, tech-professional (inspiriert von iamds.com)

---

## 2. Oracle → PostgreSQL (ALLE betroffenen Folien)

Überall wo "Oracle" steht, ersetze durch "PostgreSQL":
- Agenda: "Hands-on: MCP-Server für PostgreSQL" (statt Oracle)
- Architektur-Diagramm: "MCP Server (TypeScript) ↔ PostgreSQL" (statt Oracle Database)
- Schritt 2: "PostgreSQL DB Connector" mit `pg` Package (statt oracledb)
- Alle Erwähnungen von Oracle-Credentials → PostgreSQL-Credentials

---

## 3. Neue MCP-Grundlagen-Folien EINFÜGEN (nach der Überleitung MCP, vor den bestehenden MCP-Folien)

Die bestehenden MCP-Folien (Warum MCP besser als REST, Tool-Call, Tool-Descriptions, Anatomie, Konfiguration) bleiben erhalten. DAVOR folgende neue Folien einfügen:

### NEUE Folie: "Was ist MCP? — Die USB-Analogie"
- Zwei Vergleichsboxen:
  - VOR USB: Jedes Gerät = eigener Anschluss (Drucker, Scanner, Kamera)
  - VOR MCP: Jedes Tool = eigene Integration (DB, Browser, GitHub)
- Pfeil zu:
  - MIT USB: Ein Standard → jedes Gerät funktioniert überall
  - MIT MCP: Ein Protokoll → jeder Server funktioniert in jedem Client
- MCP-Clients auflisten: Claude Code, Copilot, Cursor, Windsurf, Claude Desktop
- Kernaussage: "MCP = offener Standard von Anthropic, basiert auf JSON-RPC 2.0"

### NEUE Folie: "MCP auf Protokoll-Ebene (JSON-RPC 2.0)"
- Zwei JSON-Blöcke nebeneinander:
  - Links (Request): `{ "method": "tools/list" }` → Server antwortet mit Tool-Definitionen
  - Rechts (Tool-Call): `{ "method": "tools/call", "params": { "name": "query-table", "arguments": {...} } }`
- Kernaussage: "Einfaches Request/Response über JSON — kein komplexes Framework"

### NEUE Folie: "Transport — stdio vs. HTTP"
- Zwei Spalten:
  - **stdio (lokal):** Server als Child-Process, stdin/stdout, kein Netzwerk, stoppt mit Client
  - **HTTP + SSE (remote):** Server als Web-Service, mehrere Clients gleichzeitig, Server-Sent Events
- Entscheidung: "Lokaler MCP-Server → stdio. Shared Team-Server → HTTP"
- Box: "Wir nutzen heute stdio — einfacher, sicherer"

### NEUE Folie: "Der MCP-Lifecycle (4 Phasen)"
- Sequenz-Diagramm (vertikal):
  1. **INITIALIZE:** Client startet Server → Server meldet Capabilities
  2. **DISCOVERY:** Client fragt `tools/list` → Server liefert alle Tools mit Schemas
  3. **OPERATION:** Client ruft `tools/call` → Server führt aus → Ergebnis zurück (wiederholbar)
  4. **SHUTDOWN:** Client beendet Verbindung → Server stoppt

### NEUE Folie: "MCP-Server bauen — 15 Zeilen TypeScript"
- Code-Block: Minimaler MCP-Server mit `@modelcontextprotocol/sdk`
  ```typescript
  const server = new McpServer({ name: "demo", version: "1.0.0" });
  server.tool("greet", "Begrüßt einen User", { name: z.string() },
    async ({ name }) => ({
      content: [{ type: "text", text: `Hallo ${name}!` }]
    })
  );
  await server.connect(new StdioServerTransport());
  ```
- Kernaussage: "Das ist ein vollständiger MCP-Server. Sofort nutzbar in Claude Code oder Copilot."

---

## 4. Bestehende MCP-Folien BEHALTEN und prüfen

Diese Folien sollten bereits existieren — falls ja, nicht ändern (nur Design-Update):
- Warum MCP besser als REST (Vergleichstabelle)
- Wie ein MCP-Tool-Call abläuft (5-Schritte Sequenz-Diagramm)
- Tool-Descriptions Schlecht vs. Gut
- Anatomie eines MCP-Servers (Tools, Resources, Prompts)
- MCP-Konfiguration (.mcp.json vs. .vscode/mcp.json)
- MCP + Datenbank Workflow
- Authentifizierung 3 Ebenen
- SQL-Injection im MCP-Kontext
- Security-Checklist
- Playwright MCP Live-Demo
- Wann MCP einsetzen

---

## 5. Zusammenfassung anpassen

Letzte Folie "Zusammenfassung Tag 2" — Key Takeaways:
1. Kontext-Hierarchie: CLAUDE.md → Rules → Skills → Prompt
2. lessons.md = Team-Gedächtnis gegen wiederkehrende Fehler
3. /plan, /ralph, /swarm als Qualitäts-Workflow
4. MCP = USB-Standard für KI-Tool-Interaktion (JSON-RPC 2.0, stdio/HTTP)
5. Security first: 3-Ebenen-Auth + Unternehmens-Checklist
