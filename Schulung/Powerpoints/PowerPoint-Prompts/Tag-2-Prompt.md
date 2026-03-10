# PowerPoint-Prompt: Tag 2 — KI als Coding-Partner + MCP

## Anweisung
Erstelle eine professionelle PowerPoint-Präsentation für Tag 2 einer KI-Schulung. Der Tag ist zweigeteilt: Vormittag "KI als Coding Assistant" und Nachmittag "MCP-Server bauen". Zielgruppe: 6 erfahrene Software-Entwickler (Java, Spring Boot, Vue.js, TypeScript) mit Tag-1-Erfahrung.

**WICHTIG — Zielgruppe:** Erfahrene Entwickler. Keine Basics erklären. Stattdessen: Token-Kosten bei Sub-Agents, technische Unterschiede Inline vs. Agent Mode (Context-Größe, Latenz, Kosten), SQL-Injection-Beispiele im MCP-Kontext, Tool-Auswahl-Algorithmus. Mindestens 40% der Folien sollen Code-Beispiele oder technische Diagramme enthalten.

## Design-Vorgaben
- Stil inspiriert von iamds.com: modern, clean, viel Weißraum, tech-professional — gleiches Design wie Tag 1
- Wenig Text pro Folie (max. 5-6 Bulletpoints, kurze Sätze)
- Große Schrift (min. 24pt Body, 36pt Titel)
- Schriftart: Open Sans (oder Calibri als Fallback)
- Diagramme und Tabellen wo möglich
- Farbschema:
  - Primärfarbe: Deep Blue (#3f59ff) — für Überschriften, Buttons, Highlights
  - Sekundärfarbe: Navy (#212b80) — für Titel, dunkle Hintergründe
  - Akzentfarbe: Golden Yellow (#ffd440) — für Hervorhebungen, Call-to-Actions
  - Hintergrund: Weiß (#ffffff) mit viel Weißraum
  - Text: Dunkelgrau (#212121)
- Sprache: Deutsch

## Folienstruktur (39 Folien)

### TEIL 1: KI als Coding Assistant (Folien 1-14)

### Folie 1: Titelfolie
- Titel: "Tag 2: KI als Coding-Partner + MCP"
- Untertitel: "AI-Blueprint Schulung"

### Folie 2: Agenda
- Zeitplan:
  - 09:00-09:30 — Recap Tag 1 + Warm-up
  - 09:30-10:30 — Theorie: KI als Coding Assistant
  - 10:30-11:00 — Live-Demo: Kontext-Hierarchie
  - 11:00-12:00 — Theorie: MCP-Protokoll
  - 12:00-12:45 — Mittagspause
  - 12:45-15:15 — Hands-on: MCP-Server für PostgreSQL

### Folie 3: Das Problem — KI ohne Kontext
- Zwei Boxen:
  - OHNE Kontext: "Füge einen Endpoint hinzu" → Generischer Code, falsche Dependencies, ignorierte Konventionen
  - MIT Kontext: Gleicher Prompt → Passt perfekt zum Projekt
- Kernaussage: "Kontext aktiv liefern — nicht darauf warten dass KI ihn findet."

### Folie 4: Die Kontext-Hierarchie
- Pyramiden-Diagramm (von unten nach oben):
  - Basis: CLAUDE.md / copilot-instructions.md (immer geladen)
  - Mitte: Rules / Instructions (automatisch bei Dateitypen)
  - Oben: Skills / Reusable Prompts (bei Bedarf)
  - Spitze: Dein Prompt (spezifischer Task)
- Kleine Tabelle: Was gehört wohin (Tech Stack → CLAUDE.md, Dateityp-Standards → Rule, Domänen-Wissen → Skill)

### Folie 5: lessons.md — Team-Gedächtnis
- Beispiel-Eintrag: "KI nutzt Options API statt Composition API → Regel: Explizit Composition API fordern"
- Workflow (3 Schritte): KI macht Fehler → Lesson aufschreiben → KI liest beim nächsten Start
- Kernaussage: "Jeder korrigierte Fehler gehört in lessons.md"

### Folie 6: Copilot — Inline vs. Agent Mode (technisch)
- Vergleichstabelle mit technischen Details:
  | | Inline | Agent Mode |
  | Context | Nur aktuelle Datei (~2-5 Dateien) | Gesamtes Workspace |
  | Latenz | 50-200ms | 2-15 Sekunden |
  | Token-Kosten | ~100-500 pro Completion | ~5.000-50.000 pro Anfrage |
  | Instructions | Nicht geladen | copilot-instructions.md geladen |
- Entscheidungsbaum: Vervollständigung? → Inline. Mehrere Dateien? → Agent. Konventionen nötig? → Agent.

### Folie 7: Sub-Agents — Kontext sauber halten
- Diagramm: Hauptkontext (30% belegt) → Sub-Agent (neues 200k Window, 0% belegt) → Ergebnis zurück
- Kosten-Box: "5 parallele Agents = 5x Token-Kosten, ABER alle laufen gleichzeitig"
- Wann lohnt es sich vs. sequentiell arbeiten
- Warnsignale für vollen Kontext: Wiederholte Fragen, ignorierte Constraints, Stil-Abweichungen

### Folie 8: Context-Window-Management
- Grafik: Kurze Session (alles klar) → Lange Session (vergisst) → Sehr lange Session (Widersprüche)
- Faustregel: ">30 Min aktives Coding → neuen Chat oder /swarm"
- Lösung: Sub-Agents für alles was nicht zum Kern-Task gehört

### Folie 9: Built-in Commands
- Tabelle:
  | Command | Was es macht |
  | /plan | Plan erstellen, keine Änderungen ohne Freigabe |
  | /review | Code-Review des aktuellen Diffs |
  | /commit | Commit-Message schreiben + committen |
  | /swarm | Parallele Sub-Agents starten |
  | /loop | Wiederkehrende Tasks (z.B. /loop 5m /review) |
  | /ralph | Iterativer Selbst-Verifikations-Loop |

### Folie 10: /ralph — Qualitäts-Firewall (mit echtem Output)
- 6-Punkte-Checkliste als Grafik:
  1. Vollständig? (alle Requirements)
  2. Korrekt? (syntaktisch + logisch)
  3. Konventionen? (Standards eingehalten)
  4. Sicher? (keine Security-Lücken)
  5. Minimal? (nichts außerhalb Scope)
  6. Elegant? (Staff-Engineer-Standard)
- Beispiel-Output: "✅ Vollständig: 3 Endpoints ✅ Korrekt: Tests grün ⚠️ Konventionen: Interface fehlt → FIX"
- Warnung: "/ralph kann selbst halluzinieren — nach /ralph immer mvn test laufen lassen"

### Folie 11: KI autonom arbeiten lassen
- 3 Patterns:
  1. Ein Feature direkt beschreiben (konkreter Prompt)
  2. Feature-Liste via tasks/prd.md übergeben
  3. /swarm für parallele unabhängige Features
- Der Schlüssel: Konkreter Prompt = autonome KI

### Folie 12: /swarm — Parallele Agents
- Ablauf-Diagramm:
  - Runde 1 (parallel): Agent 1 → Service A, Agent 2 → Service B
  - Runde 2 (parallel): Agent 3 → Tests A, Agent 4 → Tests B
  - Runde 3: Agent 5 → Review aller Ergebnisse
- Wann lohnt sich /swarm? 3+ unabhängige Module, große Refactorings, COBOL-Migration

### Folie 13: Täglicher Workflow
- Timeline-Grafik:
  - Morgens: Claude/Copilot öffnen → lessons.md geladen → todo.md checken
  - Feature: >3 Schritte? → /plan | Klare Requirements? → autonom bauen | Fertig? → /ralph + /review
  - Abends: Lessons learned aufschreiben

### Folie 14: Überleitung — MCP
- Große Frage: "Was wenn KI nicht nur Code schreiben soll, sondern mit echten Systemen interagieren?"
- Pfeil zu: "Model Context Protocol"

---

### TEIL 2: MCP — Model Context Protocol (Folien 15-33)

### Folie 15: Titelfolie Teil 2
- Titel: "MCP — KI mit der Außenwelt verbinden"
- Untertitel: "Model Context Protocol"

### Folie 16: Was ist MCP? — Die USB-Analogie
- Zwei Vergleichsboxen:
  - VOR USB: Jedes Gerät = eigener Anschluss (Drucker, Scanner, Kamera)
  - VOR MCP: Jedes Tool = eigene Integration (DB, Browser, GitHub)
- Pfeil zu:
  - MIT USB: Ein Standard → jedes Gerät funktioniert überall
  - MIT MCP: Ein Protokoll → jeder MCP-Server funktioniert in jedem Client
- MCP-Clients: Claude Code, **Mistral Vibe**, Copilot, Cursor, Windsurf, Claude Desktop, **Le Chat** (nur Remote SSE)
- Kernaussage: "MCP = offener Standard von Anthropic, basiert auf JSON-RPC 2.0 — einmal bauen, überall nutzen"

### Folie 17: MCP auf Protokoll-Ebene (JSON-RPC 2.0)
- Zwei JSON-Blöcke nebeneinander:
  - Links (Request): `{ "method": "tools/list" }` → Server antwortet mit Tool-Definitionen
  - Rechts (Tool-Call): `{ "method": "tools/call", "params": { "name": "query-table", "arguments": {...} } }`
- Kernaussage: "Einfaches Request/Response über JSON — kein komplexes Framework"

### Folie 18: Transport — stdio vs. HTTP
- Zwei Spalten:
  - **stdio (lokal):** Server als Child-Process, Kommunikation über stdin/stdout, kein Netzwerk, stoppt mit Client
  - **HTTP + SSE (remote):** Server als Web-Service, mehrere Clients gleichzeitig, Server-Sent Events für Streaming
- Entscheidung: "Lokaler MCP-Server → stdio. Shared Team-Server → HTTP"
- Box: "Wir nutzen heute stdio — einfacher, sicherer"

### Folie 19: Der MCP-Lifecycle (4 Phasen)
- Sequenz-Diagramm (vertikal):
  1. **INITIALIZE:** Client startet Server → Server meldet Capabilities
  2. **DISCOVERY:** Client fragt `tools/list` → Server liefert alle Tools mit Schemas
  3. **OPERATION:** Client ruft `tools/call` → Server führt aus → Ergebnis zurück (wiederholbar)
  4. **SHUTDOWN:** Client beendet Verbindung → Server stoppt

### Folie 20: MCP-Server bauen — 15 Zeilen TypeScript
- Code-Block: Minimaler MCP-Server mit `@modelcontextprotocol/sdk`
  ```
  const server = new McpServer({ name: "demo", version: "1.0.0" });
  server.tool("greet", "Begrüßt einen User", { name: z.string() },
    async ({ name }) => ({ content: [{ type: "text", text: `Hallo ${name}!` }] })
  );
  await server.connect(new StdioServerTransport());
  ```
- Kernaussage: "Das ist ein vollständiger MCP-Server. Sofort nutzbar in Claude Code oder Copilot."

### Folie 21: Warum MCP besser ist als REST für KI (technisch)
- Vergleichstabelle:
  | Aspekt | REST + Tool Use | MCP |
  | Tool-Discovery | Manuell in JSON definieren | Server liefert automatisch |
  | Schema-Inference | JSON Schema pro Endpoint schreiben | Server deklariert Parameter + Typen |
  | Fehlerbehandlung | HTTP-Codes interpretieren | Standardisiertes Error-Format |
  | Transport | HTTP/HTTPS | stdio (lokal) oder HTTP |
  | Multi-Client | Pro Client neu integrieren | Einmal bauen → überall nutzen |
- Kernaussage: "REST geht auch (Tool Use API). MCP ist der Standard dafür."

### Folie 22: Wie ein MCP-Tool-Call technisch abläuft
- Sequenz-Diagramm (5 Schritte):
  1. Claude Code startet MCP-Server als Child-Process (stdio)
  2. Server schickt Tool-Liste (name, description, inputSchema)
  3. Claude matched User-Prompt gegen Tool-Descriptions → wählt passendes Tool
  4. Claude sendet Tool-Call mit Argumenten → Server führt aus
  5. Server antwortet → Claude formuliert Antwort
- Box: "Die description ist ein Prompt an die KI — schlechte Description = falsches Tool"

### Folie 23: Tool-Descriptions — Schlecht vs. Gut
- Zwei Code-Blöcke:
  - ❌ Schlecht: `{ name: "get-data", description: "Holt Daten aus der DB" }` — zu vage
  - ✅ Gut: `{ name: "list-tables", description: "Listet alle Tabellennamen auf. Nutze als ERSTEN Schritt um verfügbare Daten zu verstehen." }`
- Token-Kosten-Box: "4 Tools × 200 Tokens = 800 Tokens pro Nachricht nur für Definitionen"

### Folie 24: Anatomie eines MCP-Servers
- 3-Spalten-Grafik:
  - Tools (Aktionen): Was KI tun kann (z.B. get-user, execute-query)
  - Resources (Daten): Was KI lesen kann (z.B. db://schema)
  - Prompts (Vorlagen): Vorgefertigte Templates

### Folie 25: MCP-Konfiguration — 3 Tools, 1 Server
- Drei Code-Blöcke nebeneinander:
  - Claude Code (.mcp.json): `{ "mcpServers": { "postgres": { "command": "npx", ... } } }`
  - Mistral Vibe (.vibe/config.toml): `[[mcp_servers]]` name, transport, command, args, env
  - Copilot (.vscode/mcp.json): servers → type, command, args
- Kernaussage: "Gleicher MCP-Server, 3 verschiedene Configs — einmal bauen, überall nutzen"

### Folie 26: MCP + Datenbank
- Workflow-Diagramm (4 Schritte):
  1. KI liest Schema via Resource
  2. KI analysiert Tabellen + Beziehungen
  3. KI schreibt + führt Query via Tool aus
  4. KI interpretiert und antwortet
- Kernaussage: "KI führt Queries selbst aus, nicht nur Vorschläge"

### Folie 27: Authentifizierung — 3 Ebenen
- Schichten-Diagramm (gestapelt):
  - Ebene 1: Transport-Security (API-Key im Header)
  - Ebene 2: DB-Berechtigungen (Read-Only User)
  - Ebene 3: Tool-Level Authorization (Admin-Only für delete)

### Folie 28: SQL-Injection im MCP-Kontext (Code-Beispiel)
- Zwei Code-Blöcke:
  - ❌ UNSICHER: `SELECT * FROM ${tableName} WHERE ${whereClause}` — KI könnte `1=1; DROP TABLE` generieren
  - ✅ SICHER: Tabellen-Whitelist + Parameterized Queries + Regex-Validierung für WHERE
- Checklist:
  - Tabellen-Whitelist (nur erlaubte Tabellen)
  - Parameterized Queries (keine String-Interpolation)
  - Sensible Spalten filtern (PASSWORD, SECRET, API_KEY)
  - Rate Limiting: Max. 10 Tool-Calls/Minute
  - Logging: Tool-Calls loggen, KEINE Query-Parameter (Datenschutz)

### Folie 29: Security-Checklist für KI im Unternehmen
- 8-Punkte-Checklist:
  - Datenklassifizierung: Welche Daten dürfen an welche KI?
  - Anbieter-Compliance: DSGVO, SOC2, ISO 27001?
  - API-Key-Management: Sichere Verwaltung + Rotation
  - Output-Validierung: KI-Outputs vor Nutzung prüfen
  - Logging: Alle KI-Interaktionen auditierbar
  - Lokale Alternativen: Sensible Daten nur lokal (OLLAMA)
  - Security Scans: Automatisch in CI/CD
  - Penetration Testing: Regelmäßig mit KI-Support
- Risiko-Matrix als kleine Tabelle

### Folie 30: Playwright MCP — Live-Demo
- Was es kann: Selbstheilende Tests, Accessibility-Snapshots (kein Screenshot nötig)
- Vergleich: Klassisch (KI schreibt → du testest → Fehler → KI fixt) vs. MCP (KI schreibt → testet selbst → fixt direkt)

### Folie 31: Wann MCP einsetzen?
- Entscheidungstabelle:
  | Situation | Empfehlung |
  | DB-Schema verstehen | MCP |
  | Wiederholt auf Daten zugreifen | MCP |
  | Einmalige API-Abfrage | Direkt via URL |
  | Team nutzt gleichen Datenzugriff | MCP (einmal bauen, alle nutzen) |

### Folie 32: Übersicht Hands-on Aufgabe
- Architektur-Diagramm:
  Vue.js Frontend ↔ Spring Boot Backend ↔ Claude API (mit Tool Use) ↔ MCP Server (TypeScript) ↔ PostgreSQL
- "Wir erweitern den Chatbot von Tag 1!"

### Folie 33: Schritt 1 — Starter verstehen (15 Min)
- Dateien: server.ts, db-connector.ts, auth-middleware.ts
- Aufgabe: MCP-Struktur erklären lassen

### Folie 34: Schritt 2 — PostgreSQL DB Connector (30 Min)
- PostgreSQL-Verbindung mit pg Package aufbauen
- .env mit PostgreSQL-Credentials füllen

### Folie 35: Schritt 3 — MCP Tools bauen (50 Min)
- 4 Tools implementieren:
  1. list-tables — Alle Tabellen auflisten
  2. describe-table — Spalten + Typen einer Tabelle
  3. query-table — SELECT mit Parametern
  4. count-rows — Anzahl Zeilen einer Tabelle

### Folie 36: Schritt 4 — API-Key Auth (20 Min)
- Security-Layer mit x-api-key Header
- Parameterized Queries, Table Whitelist

### Folie 37: Schritt 5 — Claude Code anbinden (20 Min)
- .mcp.json konfigurieren
- KI testet den MCP-Server direkt

### Folie 38: Schritt 6 — Backend Tool Use (30 Min)
- Spring Boot erweitern: Claude API mit tool_use Blocks
- Chatbot beantwortet jetzt echte DB-Fragen!

### Folie 39: Zusammenfassung Tag 2
- Key Takeaways:
  1. Kontext-Hierarchie: CLAUDE.md → Rules → Skills → Prompt
  2. lessons.md = Team-Gedächtnis gegen wiederkehrende Fehler
  3. /plan, /ralph, /swarm als Qualitäts-Workflow
  4. MCP = USB-Standard für KI-Tool-Interaktion
  5. Security first: 3-Ebenen-Auth + Unternehmens-Checklist
