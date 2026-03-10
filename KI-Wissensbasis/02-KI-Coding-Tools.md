# 02 – KI Coding & Entwicklungstools

> IDE-Integrationen, Code-Assistenten und automatisierte Entwickler-Agents.

---

## Claude Code Ökosystem

### Claude Code CLI `⭐ HOCH`
- **Was:** CLI-basierter KI-Coding-Assistent von Anthropic
- **Features:** Code-Generierung, Refactoring, Debugging, Security Scans, MCP-Support
- **Betrieb:** Cloud (Anthropic API) oder lokal via OLLAMA
- **Aktion:** [ ] Standard-Setup für das Team dokumentieren

### Claude Code Security Scans `⭐ HOCH`
- **Was:** Integrierte Security-Analyse direkt in Claude Code
- **Warum relevant:** Automatisierte Sicherheitsprüfung im Entwicklungsprozess
- **Aktion:** [ ] In CI/CD-Pipeline integrieren

### Claude Code Mobile App (Remote Control) `🔵 MITTEL`
- **Was:** Claude Code von unterwegs per Handy-App steuern
- **Use Case:** Input an laufende Sessions senden, Status prüfen
- **Aktion:** [ ] Testen für unterwegs/mobiles Arbeiten

### Excel & Word Extension (Claude Code) `🔵 MITTEL`
- **Was:** Claude Code Extensions für Microsoft Office
- **Warum relevant:** KI-gestützte Datenanalyse und Textverarbeitung direkt in Office
- **Aktion:** [ ] Evaluieren für Business-Analysten im Team

---

## Mistral Vibe Ecosystem

### Mistral Vibe CLI `⭐ HOCH`
- **Was:** CLI-basierter Coding-Agent von Mistral, Open Source (MIT/Apache 2.0)
- **Features:** Code-Generierung, Multi-File Orchestration, MCP-Support, Subagents, Skills
- **Modell:** Devstral 2 (123B, 72.2% SWE-bench)
- **Config:** `.vibe/config.toml` (Projekt-Level), Agents als TOML, Skills als SKILL.md
- **Install:** `curl -LsSf https://mistral.ai/vibe/install.sh | bash`
- **Aktion:** [ ] Vibe installieren und Parallelbetrieb mit Claude Code testen

### Codestral + Continue.dev (IDE Autocomplete) `🔵 MITTEL`
- **Was:** Codestral als Autocomplete-Provider in VS Code / JetBrains via Continue.dev
- **Warum relevant:** FIM-Support (Fill-in-the-Middle), sehr schnell, guenstig (0.30/0.90 EUR/M Tokens)
- **Aktion:** [ ] Continue.dev Extension installieren + Codestral konfigurieren

### Le Chat Canvas `🔵 MITTEL`
- **Was:** Mistrals Chat-Interface mit kollaborativem Code-Editing
- **Features:** Web-Suche, Custom Agents, Python Sandbox, MCP-Support (Remote SSE)
- **Aktion:** [ ] Le Chat fuer Team-Collaboration evaluieren

→ Details: siehe [17-Mistral-Teams-und-Ecosystem.md](17-Mistral-Teams-und-Ecosystem.md)

---

## Open-Source Coding Agents

### OpenClaw `⭐ HOCH`
- **Was:** Automatisierter Entwickler-Agent (Open Source)
- **Warum relevant:** Kann Entwicklungsaufgaben autonom ausführen
- **Aktion:** [ ] Vergleich mit Claude Code für Routine-Tasks

### Picoclaw `🔵 MITTEL`
- **Was:** Minimalistische Version eines Coding-Agents
- **Warum relevant:** Leichtgewichtig, für einfache Automatisierungen
- **Aktion:** [ ] Testen für kleinere Scripting-Aufgaben

### Moltot + OLLAMA `🔵 MITTEL`
- **Was:** Vollautomatisierter Agent, der lokal mit OLLAMA läuft
- **Warum relevant:** Kompletter autonomer Dev-Agent ohne Cloud-Abhängigkeit
- **Aktion:** [ ] Proof of Concept für lokale Automatisierung

---

## IDE-Integrationen

### Cursor Visual Editor `🔵 MITTEL`
- **Was:** Visueller Editor im Browser der IDE (Cursor)
- **Warum relevant:** Erweiterte visuelle Bearbeitungsmöglichkeiten in der IDE
- **Aktion:** [ ] Feature-Vergleich Cursor vs. VS Code + Copilot

### Gemini Code Assistant für VS Code `🔵 MITTEL`
- **Was:** Googles Code-Assistent als VS-Code-Extension
- **Warum relevant:** Alternative/Ergänzung zu GitHub Copilot mit Google-Modellen
- **Aktion:** [ ] Side-by-Side-Test mit Copilot

### Chrome Dev Tools für Agent-Unterstützung `🔵 MITTEL`
- **Was:** Chrome Dev Tools aktivieren, um Agents bei Web-Entwicklung zu unterstützen
- **Warum relevant:** Agents können Browserverhalten analysieren, debuggen, testen
- **Aktion:** [ ] Best Practices dokumentieren

---

## Sonstige Coding-Tools

### GSD – "Get Shit Done" (GitHub) `⚪ NIEDRIG`
- **Was:** GitHub-Tool/Framework
- **Link:** https://github.com/gsd-build/get-shit-done
- **Aktion:** [ ] Prüfen was das Projekt genau bietet

### Remotion Skills (npmx) `⚪ NIEDRIG`
- **Was:** Skills-Framework von Remotion
- **Link:** https://github.com/remotion-dev/skills
- **Aktion:** [ ] Relevanz für unser Setup prüfen

---

## Strategische Einordnung 📸

> *„The use of Software Engineers will be to solve problems. Coding was one task of that → will get replaced."*

- Die Rolle des Entwicklers verschiebt sich: **Von Code schreiben → Probleme lösen**
- Coding wird zunehmend von Agents übernommen
- Kern-Skills der Zukunft: **Architektur, Problemdefinition, Verifizierung, Prompt-Kompetenz**
