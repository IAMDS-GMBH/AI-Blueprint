# 12 – MCP & Integrationen

> Model Context Protocol (MCP) Server und Tool-Integrationen für KI-Agents.

---

## Was ist MCP?

**Model Context Protocol (MCP)** ist ein offener Standard der es KI-Modellen ermöglicht, mit externen Tools und Systemen zu interagieren. Statt nur Text zu verarbeiten, kann die KI damit:
- Dateien lesen/schreiben
- APIs aufrufen
- Datenbanken abfragen
- Externe Services nutzen

---

## WEBMCP – Ersatz für REST APIs für LLMs? `⭐ HOCH`

### Was ist WEBMCP?
- **Konzept:** Ein Protokoll das REST APIs durch ein LLM-optimiertes Interface ersetzt
- **Problem mit REST APIs + LLMs:** 
  - REST APIs sind für maschinelle Clients designt, nicht für LLMs
  - LLMs brauchen Kontext, Beschreibungen, Beispiele
  - JSON-Schemas sind für LLMs schwer zu interpretieren
- **WEBMCP-Lösung:**
  - Wrapper um bestehende APIs
  - Fügt semantische Beschreibungen hinzu
  - Optimiert Request/Response-Format für LLM-Verständnis
  - Ermöglicht "Plug-and-Play" Anbindung von Services an LLMs
- **Vergleich:**

| Aspekt | REST API | WEBMCP |
|--------|----------|--------|
| Zielgruppe | Entwickler / Apps | LLMs / Agents |
| Dokumentation | OpenAPI/Swagger | Semantisch angereichert |
| Interaktion | Request/Response | Konversationell |
| Fehlerbehandlung | HTTP-Statuscodes | Natürlichsprachige Erklärungen |
| Discovery | Manuell | Automatisch via Protokoll |

- **Warum relevant:** Könnte der Standard werden, wie KI-Agents mit unseren Services kommunizieren
- **Aktion:** [ ] Evaluieren: Welche unserer APIs könnten von WEBMCP profitieren?

---

## Verfügbare MCP-Server

### Draw.io MCP Server `🔵 MITTEL`
- **Was:** KI kann Draw.io-Diagramme direkt erstellen und bearbeiten
- **Link:** https://mcpservers.org/servers/lgazo/drawio-mcp-server
- **Use Case:** Architektur-Diagramme, Flowcharts automatisch generieren lassen
- **Aktion:** [ ] Setup testen mit Claude Code / Copilot

### Figma MCP `🔵 MITTEL`
- **Was:** Verbindung zwischen Figma und KI-Agents
- **Use Case:** Design-to-Code, Design-Review, automatische Komponentenerkennung
- **Aktion:** [ ] Evaluieren für Frontend-Entwicklung

### Context7 MCP `⭐ HOCH`
- **Was:** Lädt aktuelle, versionsgenaue Library-Dokumentation direkt in den KI-Kontext
- **Problem ohne Context7:** KI kennt nur Trainingsdaten → halluziniert bei neuen Library-Versionen
- **Mit Context7:** Vor der Antwort sucht KI in der aktuellen Doku → korrekte API-Calls, keine veralteten Muster
- **Setup (.mcp.json / .vscode/mcp.json):**
```json
{
  "context7": {
    "command": "npx",
    "args": ["-y", "@upstash/context7-mcp@latest"]
  }
}
```
- **Kein API-Key nötig** – funktioniert out of the box
- **Für Claude Code:** Auch als Plugin verfügbar (`context7@claude-plugins-official`)
- **Für Copilot:** Nur via MCP-Server (kein Plugin-System)
- **Use Cases:** Spring Boot 3.x Änderungen, Vue 3.x API, neue Flyway-Syntax, etc.

---

### Playwright MCP (Microsoft) `⭐ HOCH`
- **Was:** Offizieller Microsoft MCP-Server der LLMs Browser-Automatisierung über strukturierte Accessibility-Daten ermöglicht – kein Vision-Modell nötig
- **Repo:** https://github.com/microsoft/playwright-mcp
- **Besonderheit:** Nutzt Accessibility-Snapshots statt Screenshots → token-effizient, kein Bild-Input nötig
- **Features:**
  - Chromium, Firefox, WebKit Support
  - Persistente Browser-Profile und isolierte Sessions
  - Deterministische, zuverlässige Web-Interaktionen
  - Selbstheilende Tests (KI analysiert DOM und repariert Tests eigenständig)
- **Setup (.mcp.json / .vscode/mcp.json):**
```json
{
  "mcpServers": {
    "playwright": {
      "command": "npx",
      "args": ["@playwright/mcp@latest"]
    }
  }
}
```
- **Use Cases:**
  - KI schreibt und führt E2E-Tests autonom aus
  - Web-Recherche ohne Screenshots (spart Token)
  - Formular-Ausfüllung und Web-Automatisierung
  - Ersatz für Claude Chrome Extension wenn kein Desktop vorhanden
- **Kompatibel mit:** Claude Code, GitHub Copilot (VS Code), Cursor, Claude Desktop

---

## Browser-Integrationen

### Claude Chrome Extension `🔵 MITTEL`
- **Was:** Chrome Extension die Claude mit dem Browser verbindet
- **Use Cases:**
  - Navigation und Recherche durch den Agent
  - **Verifikation** – Agent prüft seine eigene Arbeit im Browser
  - Web-Testing
- **Besonders wichtig:** Verifikationsprozess (→ siehe [06-Claude-Code-Workflow.md](06-Claude-Code-Workflow.md))
- **Aktion:** [ ] Setup für das Team, Verifikations-Workflows definieren

---

## MCP-Konfiguration

### .mcp.json (Claude Code)
```json
{
  "mcpServers": {
    "drawio": {
      "command": "npx",
      "args": ["-y", "drawio-mcp-server"]
    },
    "playwright": {
      "command": "npx",
      "args": ["-y", "@playwright/mcp-server"]
    },
    "figma": {
      "command": "npx",
      "args": ["-y", "figma-mcp-server"],
      "env": {
        "FIGMA_TOKEN": "${FIGMA_TOKEN}"
      }
    }
  }
}
```

### VS Code Settings (GitHub Copilot)
- MCP-Server können auch in den VS Code Settings konfiguriert werden
- Gleiche Funktionalität wie `.mcp.json`, aber über VS Code UI
- **Aktion:** [ ] Standard-MCP-Konfiguration für das Team erstellen

---

## Weitere Integrationen

### Claude Code MCP für Development
- **Playwright:** Browser-Testing und Web-Automatisierung
- **Figma:** Design-to-Code Pipeline
- **Aktion:** [ ] Priorisieren welche MCPs zuerst eingerichtet werden
