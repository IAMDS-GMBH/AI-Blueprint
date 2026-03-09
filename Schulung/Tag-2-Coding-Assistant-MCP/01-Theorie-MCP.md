# Tag 2 (Nachmittag) – MCP-Server: KI mit der Außenwelt verbinden

## Lernziele
- Verstehen was MCP ist und warum KI ein eigenes Protokoll braucht
- Einen MCP-Server von Grund auf aufbauen
- PostgreSQL-Datenbank über MCP an KI anbinden
- Authentifizierung und Autorisierung im MCP-Kontext implementieren

---

## 1. Das Problem: KI spricht kein REST

Standard REST-APIs sind für Menschen und Apps gemacht – nicht für KI-Modelle.

| Aspekt | REST API | Was KI braucht |
|--------|----------|----------------|
| Dokumentation | OpenAPI/Swagger (formal) | Natürlichsprachige Beschreibung |
| Fehler | HTTP-Statuscodes | Erklärung was schiefging |
| Discovery | Manuell lesen | Automatisch verstehen |
| Kontext | Request/Response | "Was bedeutet dieses Ergebnis?" |

**Beispiel:**
```
KI fragt REST API: GET /api/v1/orders?status=PENDING
API antwortet: [{"id": 1, "status": "PENDING", "customerId": 42, ...}]

KI weiß nicht: Was bedeutet "PENDING"? Was kann sie damit tun?
```

---

## 2. Was ist MCP?

**MCP = Model Context Protocol**

Ein offener Standard (von Anthropic) der KI-Modellen ermöglicht,
mit externen Tools und Systemen zu interagieren – auf eine Art die KI versteht.

```
Ohne MCP:           KI ← Text-Prompt → Du ← API → System
Mit MCP:            KI ← MCP → System (direkt, mit Kontext)
```

**MCP ist wie ein USB-Standard für KI-Tools:**
- Einmal einen MCP-Server schreiben
- In Claude Code, Copilot, und jedem anderen MCP-Client nutzen

---

## 3. Anatomie eines MCP-Servers

Ein MCP-Server stellt drei Dinge bereit:

### Tools (Aktionen)
Was die KI *tun* kann – mit Parametern und Rückgabewerten.
```typescript
{
  name: "get-user",
  description: "Lädt einen User aus der Datenbank anhand der ID",
  parameters: {
    userId: { type: "string", description: "UUID des Users" }
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

---

## 4. MCP in der Praxis: Konfiguration

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

## 5. MCP + PostgreSQL

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

## 6. Authentifizierung & Autorisierung

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

### Wichtige Sicherheitsregeln:
- [ ] Keine Datenbankpasswörter im Code – nur Umgebungsvariablen
- [ ] Minimale DB-Rechte (Principle of Least Privilege)
- [ ] SQL-Injection-Schutz: Parameterized Queries, kein String-Concat
- [ ] Sensible Daten (Passwort-Hashes) nie über MCP exponieren
- [ ] Rate Limiting für Tool-Calls

---

## 7. Live-Demo: Playwright MCP (Microsoft, offiziell)

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
