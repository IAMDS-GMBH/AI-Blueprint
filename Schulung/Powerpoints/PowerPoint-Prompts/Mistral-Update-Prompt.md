# Update-Prompt: Mistral-Ergänzungen für alle 3 Präsentationen

## Anweisung
Die Präsentationen für Tag 1, Tag 2 und Tag 3 existieren bereits. Führe die folgenden Mistral-spezifischen Änderungen durch. Du darfst Folien **anpassen, erweitern, zusammenführen und auch Inhalte entfernen oder ersetzen** wenn sie durch die neuen Informationen überflüssig werden. Ziel: Am Ende sollen die Präsentationen sauber und konsistent sein — keine Redundanzen, keine veralteten Infos, keine "angeklebten" Ergänzungen. Design, Farben und Layout bleiben unverändert.

**Kontext:** Das Schulungsteam hat **Mistral Teams Lizenzen** (25 EUR/User/Monat). Damit haben die Teilnehmer Zugang zu Mistral-Modellen, Le Chat und dem CLI-Tool **Mistral Vibe**. Mistral wird als **ergänzende Option** neben Claude vorgestellt — nicht als Ersatz. Claude bleibt das primäre Tool der Schulung.

**Wichtige Fakten:**
- **Mistral Vibe CLI** = Open-Source CLI-Agent (MIT/Apache 2.0), vergleichbar mit Claude Code, powered by Devstral 2
- **Devstral 2** = 123B Parameter, 256K Context, 72.2% SWE-bench Verified, aktuell GRATIS via Mistral API
- **Devstral Small 2** = 24B Parameter, lokal via OLLAMA, Apache 2.0, 68% SWE-bench
- **Codestral** = Code-Generierung + FIM-Autocomplete für IDEs (Continue.dev), 80+ Sprachen inkl. COBOL
- **Le Chat** = Mistrals Chat-Interface (chat.mistral.ai), MCP nur via Remote SSE (kein stdio)
- **Config:** `.vibe/config.toml` (Projekt) + `AGENTS.md` (Workspace-Root) — kein VIBE.md
- **MCP:** Voller Support via `[[mcp_servers]]` in config.toml (stdio + HTTP)
- **Installation:** `curl -LsSf https://mistral.ai/vibe/install.sh | bash` (Python 3.12+)

---

# TAG 1: Greenfield-Entwicklung mit KI

## Folie "Token-Kosten" — Kosten-Tabelle ERWEITERN

Ergänze die bestehende Kosten-Tabelle um 3 Mistral-Zeilen. Falls die Tabelle dadurch zu voll wird, entferne weniger relevante Modelle (z.B. ältere GPT-Varianten) oder gruppiere sie:

| Modell | Input/1M Tokens | Output/1M Tokens | Context Window | ~Kosten pro Feature |
|--------|----------------|-----------------|----------------|---------------------|
| Claude Sonnet 4 | $3 | $15 | 200k | ~$0.18 |
| Claude Opus 4 | $15 | $75 | 200k | ~$0.90 |
| GPT-4o | $2.50 | $10 | 128k | ~$0.13 |
| **Devstral 2 (Mistral)** | **GRATIS** | **GRATIS** | **256k** | **Aktuell kostenlos** |
| Codestral (Mistral) | €0.30 | €0.90 | 256k | ~€0.01 |
| Mistral Large | €2 | €6 | 128k | ~€0.08 |

- Devstral 2 visuell hervorheben (Golden Yellow Akzent) mit Label "Aktuell GRATIS"
- Kernaussage: "200k-256k Tokens ≈ 500+ Seiten Code. Devstral 2 ist aktuell komplett kostenlos — ideal zum Experimentieren."

## Folie "Tools im Überblick" — Tool-Vergleich ERSETZEN durch 3-Spalten-Tabelle

Falls die Folie bisher nur Claude Code vs. GitHub Copilot zeigt, **ersetze** die 2-Spalten-Tabelle komplett durch diese 3-Spalten-Version:

| Aspekt | Claude Code | Mistral Vibe | GitHub Copilot |
|--------|------------|--------------|----------------|
| Typ | CLI-Agent | CLI-Agent | IDE-Extension |
| Modell | Claude Sonnet/Opus | Devstral 2 (123B) | GPT-4o / Claude |
| Projekt-Config | CLAUDE.md | .vibe/config.toml + AGENTS.md | copilot-instructions.md |
| MCP-Support | Ja (.mcp.json) | Ja (config.toml) | Ja (.vscode/mcp.json) |
| Open Source | Nein | Ja (MIT/Apache 2.0) | Nein |
| Preis | Anthropic API | Mistral API (Devstral 2 GRATIS) | $19/Monat |

- Kernaussage: "Alle 3 Tools können parallel im selben Projekt laufen — .claude/ und .vibe/ stören sich nicht."
- Falls vorher separate Punkte zu "Claude Code" und "Copilot" auf verschiedenen Folien verteilt waren, **zusammenführen** in diese eine Vergleichsfolie.

## Folie "Zusammenfassung Tag 1" — ANPASSEN

Die Zusammenfassung soll diese Key Takeaways enthalten (bestehende Punkte anpassen, überflüssige streichen):
1. LLMs sind Wahrscheinlichkeits-Maschinen — immer verifizieren
2. Entwickler = Problem Solver, KI = Umsetzer
3. KERNEL-Framework für systematisch bessere Prompts
4. CLAUDE.md + MEMORY.md = KI kennt euer Projekt (Mistral Vibe: .vibe/config.toml + AGENTS.md)
5. Plan Mode → Implementierung → Verifikation → Lessons Learned
6. Claude Code + Mistral Vibe + Copilot können parallel im selben Projekt laufen

Falls vorher ein Punkt nur "CLAUDE.md" erwähnte ohne Mistral-Pendant: ersetzen durch Punkt 4. Falls Punkt 6 inhaltlich redundant zu etwas Bestehendem ist: das Bestehende ersetzen.

---

# TAG 2: KI als Coding-Partner + MCP

## Folie "Was ist MCP?" / MCP-Clients — Client-Liste ERSETZEN

Die MCP-Client-Liste soll vollständig und aktuell sein. **Ersetze** die bestehende Liste komplett durch:

Claude Code, **Mistral Vibe**, GitHub Copilot, Cursor, Windsurf, Claude Desktop, **Le Chat** (nur Remote SSE)

- Hinweis bei Le Chat: "(nur Remote SSE-Transport, kein stdio — d.h. lokale MCP-Server nicht direkt nutzbar)"
- Falls vorher einzelne Clients fehlten oder veraltete dabei waren: bereinigen.
- Kernaussage: "MCP = offener Standard — einmal bauen, überall nutzen. Euer MCP-Server funktioniert mit Claude Code UND Mistral Vibe."

## Folie "MCP-Konfiguration" — ERSETZEN durch 3-Config-Vergleich

Falls die Folie bisher nur 1-2 Config-Formate zeigt, **ersetze komplett** durch alle 3 nebeneinander:

**Claude Code (.mcp.json) — JSON:**
```json
{
  "mcpServers": {
    "postgres": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-postgres"]
    }
  }
}
```

**Mistral Vibe (.vibe/config.toml) — TOML:**
```toml
[[mcp_servers]]
name = "postgres"
transport = "stdio"
command = "npx"
args = ["-y", "@modelcontextprotocol/server-postgres"]
```

**GitHub Copilot (.vscode/mcp.json) — JSON:**
```json
{
  "servers": {
    "postgres": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-postgres"]
    }
  }
}
```

- Kernaussage: "Gleicher MCP-Server, 3 Config-Formate. Der Server-Code bleibt identisch — nur die Konfiguration unterscheidet sich."

## NEUE Folie: "Coding-Assistants im Vergleich" — EINFÜGEN nach Kontext-Hierarchie, vor MCP-Teil

Falls noch keine umfassende Tool-Vergleichsfolie im Tag-2-Deck existiert, **neue Folie einfügen**. Falls bereits eine existiert die nur 2 Tools vergleicht, **ersetzen**:

| Aspekt | Claude Code | Mistral Vibe | GitHub Copilot | Continue.dev + Codestral |
|--------|------------|--------------|----------------|--------------------------|
| Typ | CLI-Agent | CLI-Agent | IDE-Extension | IDE-Extension |
| Stärke | Plan Mode, Sub-Agents, Rules | Open Source, GRATIS | Inline-Suggestions, Agent Mode | FIM-Autocomplete, günstig |
| Config | CLAUDE.md + .claude/ | .vibe/config.toml + AGENTS.md | copilot-instructions.md | settings.json |
| MCP | .mcp.json | config.toml [[mcp_servers]] | .vscode/mcp.json | Nein |

- Kernaussage: "CLI-basiert (Terminal) vs. IDE-basiert — nicht entweder/oder, sondern beides nutzen."
- Falls diese Info vorher auf mehrere Folien verteilt war: **konsolidieren** in diese eine Folie und die alten Einzel-Folien entfernen oder kürzen.

## Folie "Zusammenfassung Tag 2" — ANPASSEN

Key Takeaways (bestehende Punkte anpassen, nichts doppelt):
1. Kontext-Hierarchie: CLAUDE.md → Rules → Skills → Prompt (Vibe: .vibe/config.toml + AGENTS.md)
2. lessons.md = Team-Gedächtnis gegen wiederkehrende Fehler
3. /plan, /ralph, /swarm als Qualitäts-Workflow
4. MCP = USB-Standard für KI — einmal bauen, mit Claude Code UND Mistral Vibe nutzen
5. Security first: 3-Ebenen-Auth + Unternehmens-Checklist
6. Mistral Vibe CLI als Open-Source-Alternative (Devstral 2 GRATIS, .vibe/config.toml)

Falls vorher Punkte existierten die jetzt in 1, 4 oder 6 aufgegangen sind: **entfernen** statt doppelt lassen.

---

# TAG 3: Code-Modernisierung & COBOL-Migration

## Folie "KI-Tool pro Migrationsschritt" — Tabelle ERWEITERN + BEREINIGEN

Bestehende Tabelle anpassen:
- Bei allen Zeilen wo "Claude Code" als Tool steht: **ersetzen** durch "Claude Code / Mistral Vibe" (nicht doppelte Zeilen anlegen)
- **Neue Zeile** am Ende: "Datenschutzkritisch / Offline" → **Devstral Small 2 lokal via OLLAMA** (24B, Apache 2.0, 68% SWE-bench)
- Falls redundante oder zu detaillierte Zeilen existieren: zusammenfassen

**Hinweis-Box** unter der Tabelle (Golden Yellow Akzent):
> Devstral 2 (Mistral) unterstützt **COBOL explizit** als eine von 80+ Sprachen.
> 72.2% SWE-bench Verified — aktuell **GRATIS** via Mistral API.

## Folie "Ausblick — Wie geht es weiter?" — ERWEITERN

Folgende Punkte ergänzen (falls ähnliche Punkte existieren, **ersetzen** statt verdoppeln):
- **Mistral Vibe CLI:** Open-Source CLI-Agent (github.com/mistralai/mistral-vibe) — Devstral 2 aktuell GRATIS
- **Vibe Kanban:** Kanban-Board für KI-Agents — Tasks zuweisen, parallel ausführen (`npx vibe-kanban`)

Falls der Ausblick zu viele Punkte hat (>6): die am wenigsten relevanten kürzen oder streichen.

## Folie "Gesamtzusammenfassung 3 Tage" — ERSETZEN

Die Zusammenfassung der 3 Tage komplett überarbeiten damit Mistral organisch integriert ist (nicht "angeklebt"):

- **Tag 1:** LLM-Grundlagen, KERNEL-Framework, erste App mit KI gebaut. Claude Code + Mistral Vibe als CLI-Agents kennengelernt.
- **Tag 2:** Kontext-Hierarchie (CLAUDE.md / .vibe/config.toml), MCP-Server für PostgreSQL gebaut — funktioniert mit allen Tools. Security-Checklist.
- **Tag 3:** COBOL → Java Migration mit KI. Devstral 2 unterstützt COBOL explizit. Devstral Small 2 für datenschutzkritische Offline-Szenarien.
- **Fazit:** Nicht dogmatisch — Claude + Mistral + Copilot parallel nutzen. MCP-Server wiederverwenden. Das beste Tool für die jeweilige Aufgabe wählen.

Alte Zusammenfassungs-Punkte die jetzt integriert sind: **entfernen**.

---

# Qualitäts-Check nach allen Änderungen

Gehe nach allen Änderungen die gesamte Präsentation durch und prüfe:

1. **Keine Redundanzen:** Wird Mistral Vibe oder Devstral 2 auf mehreren Folien erklärt? → Nur einmal erklären, danach nur referenzieren.
2. **Konsistente Begriffe:** Immer "Mistral Vibe" (nicht "Vibe CLI" oder "Mistral CLI"), immer "Devstral 2" (nicht "Devstral" oder "Devstral-2").
3. **Keine verwaisten Folien:** Falls durch Zusammenführung Folien leer oder redundant werden → löschen.
4. **Folien-Anzahl:** Die Gesamtzahl darf sich leicht ändern (±2 Folien pro Tag sind ok). Lieber eine Folie weniger als eine zu viel.
5. **Überflüssige Einzel-Vergleiche entfernen:** Falls vorher separate "Claude Code Features" und "Copilot Features" Folien existierten, die jetzt in der Vergleichstabelle aufgegangen sind → einzelne Folien entfernen oder kürzen.
6. **Ton prüfen:** Mistral wird als "zusätzliche Option" präsentiert, nie als "besser als Claude". Der Fokus bleibt auf Claude Code als primärem Tool.
