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

## Folienstruktur (33 Folien)

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
  - 12:45-15:15 — Hands-on: MCP-Server für Oracle DB

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

### Folie 16: Warum MCP besser ist als REST für KI (technisch)
- Vergleichstabelle:
  | Aspekt | REST + Tool Use | MCP |
  | Tool-Discovery | Manuell in JSON definieren | Server liefert automatisch |
  | Schema-Inference | JSON Schema pro Endpoint schreiben | Server deklariert Parameter + Typen |
  | Fehlerbehandlung | HTTP-Codes interpretieren | Standardisiertes Error-Format |
  | Transport | HTTP/HTTPS | stdio (lokal) oder HTTP |
  | Multi-Client | Pro Client neu integrieren | Einmal bauen → überall nutzen |
- Kernaussage: "REST geht auch (Tool Use API). MCP ist der Standard dafür."

### Folie 17: Wie ein MCP-Tool-Call technisch abläuft
- Sequenz-Diagramm (5 Schritte):
  1. Claude Code startet MCP-Server als Child-Process (stdio)
  2. Server schickt Tool-Liste (name, description, inputSchema)
  3. Claude matched User-Prompt gegen Tool-Descriptions → wählt passendes Tool
  4. Claude sendet Tool-Call mit Argumenten → Server führt aus
  5. Server antwortet → Claude formuliert Antwort
- Box: "Die description ist ein Prompt an die KI — schlechte Description = falsches Tool"

### Folie 18: Tool-Descriptions — Schlecht vs. Gut
- Zwei Code-Blöcke:
  - ❌ Schlecht: `{ name: "get-data", description: "Holt Daten aus der DB" }` — zu vage
  - ✅ Gut: `{ name: "list-tables", description: "Listet alle Tabellennamen auf. Nutze als ERSTEN Schritt um verfügbare Daten zu verstehen." }`
- Token-Kosten-Box: "4 Tools × 200 Tokens = 800 Tokens pro Nachricht nur für Definitionen"

### Folie 19: Anatomie eines MCP-Servers
- 3-Spalten-Grafik:
  - Tools (Aktionen): Was KI tun kann (z.B. get-user, execute-query)
  - Resources (Daten): Was KI lesen kann (z.B. db://schema)
  - Prompts (Vorlagen): Vorgefertigte Templates

### Folie 19: MCP-Konfiguration
- Zwei Code-Blöcke nebeneinander:
  - Claude Code (.mcp.json): mcpServers → command, args, env
  - Copilot (.vscode/mcp.json): servers → type, command, args
- "Danach kann KI sagen: 'Ich nutze jetzt get-user aus dem MCP-Server...'"

### Folie 20: MCP + Datenbank
- Workflow-Diagramm (4 Schritte):
  1. KI liest Schema via Resource
  2. KI analysiert Tabellen + Beziehungen
  3. KI schreibt + führt Query via Tool aus
  4. KI interpretiert und antwortet
- Kernaussage: "KI führt Queries selbst aus, nicht nur Vorschläge"

### Folie 21: Authentifizierung — 3 Ebenen
- Schichten-Diagramm (gestapelt):
  - Ebene 1: Transport-Security (API-Key im Header)
  - Ebene 2: DB-Berechtigungen (Read-Only User)
  - Ebene 3: Tool-Level Authorization (Admin-Only für delete)

### Folie 22: SQL-Injection im MCP-Kontext (Code-Beispiel)
- Zwei Code-Blöcke:
  - ❌ UNSICHER: `SELECT * FROM ${tableName} WHERE ${whereClause}` — KI könnte `1=1; DROP TABLE` generieren
  - ✅ SICHER: Tabellen-Whitelist + Parameterized Queries + Regex-Validierung für WHERE
- Checklist:
  - Tabellen-Whitelist (nur erlaubte Tabellen)
  - Parameterized Queries (keine String-Interpolation)
  - Sensible Spalten filtern (PASSWORD, SECRET, API_KEY)
  - Rate Limiting: Max. 10 Tool-Calls/Minute
  - Logging: Tool-Calls loggen, KEINE Query-Parameter (Datenschutz)

### Folie 23: Security-Checklist für KI im Unternehmen (NEU)
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

### Folie 24: Playwright MCP — Live-Demo
- Was es kann: Selbstheilende Tests, Accessibility-Snapshots (kein Screenshot nötig)
- Vergleich: Klassisch (KI schreibt → du testest → Fehler → KI fixt) vs. MCP (KI schreibt → testet selbst → fixt direkt)

### Folie 25: Wann MCP einsetzen?
- Entscheidungstabelle:
  | Situation | Empfehlung |
  | DB-Schema verstehen | MCP |
  | Wiederholt auf Daten zugreifen | MCP |
  | Einmalige API-Abfrage | Direkt via URL |
  | Team nutzt gleichen Datenzugriff | MCP (einmal bauen, alle nutzen) |

### Folie 26: Übersicht Hands-on Aufgabe
- Architektur-Diagramm:
  Vue.js Frontend ↔ Spring Boot Backend ↔ Claude API (mit Tool Use) ↔ MCP Server (TypeScript) ↔ Oracle Database
- "Wir erweitern den Chatbot von Tag 1!"

### Folie 27: Schritt 1 — Starter verstehen (15 Min)
- Dateien: server.ts, db-connector.ts, auth-middleware.ts
- Aufgabe: MCP-Struktur erklären lassen

### Folie 28: Schritt 2 — Oracle DB Connector (30 Min)
- PostgreSQL → Oracle umbauen (oracledb Package)
- .env mit Oracle-Credentials füllen

### Folie 29: Schritt 3 — MCP Tools bauen (50 Min)
- 4 Tools implementieren:
  1. list-tables — Alle Tabellen auflisten
  2. describe-table — Spalten + Typen einer Tabelle
  3. query-table — SELECT mit Parametern
  4. count-rows — Anzahl Zeilen einer Tabelle

### Folie 30: Schritt 4 — API-Key Auth (20 Min)
- Security-Layer mit x-api-key Header
- Parameterized Queries, Table Whitelist

### Folie 31: Schritt 5 — Claude Code anbinden (20 Min)
- .mcp.json konfigurieren
- KI testet den MCP-Server direkt

### Folie 32: Schritt 6 — Backend Tool Use (30 Min)
- Spring Boot erweitern: Claude API mit tool_use Blocks
- Chatbot beantwortet jetzt echte DB-Fragen!

### Folie 33: Zusammenfassung Tag 2
- Key Takeaways:
  1. Kontext-Hierarchie: CLAUDE.md → Rules → Skills → Prompt
  2. lessons.md = Team-Gedächtnis gegen wiederkehrende Fehler
  3. /plan, /ralph, /swarm als Qualitäts-Workflow
  4. MCP = USB-Standard für KI-Tool-Interaktion
  5. Security first: 3-Ebenen-Auth + Unternehmens-Checklist
