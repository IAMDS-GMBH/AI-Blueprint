# PowerPoint-Prompt: Tag 1 — Greenfield-Entwicklung mit KI

## Anweisung
Erstelle eine professionelle PowerPoint-Präsentation für einen Schulungstag zum Thema "Greenfield-Entwicklung mit KI". Die Präsentation begleitet einen ganzen Trainingstag (Theorie 9:00-11:00, Praxis 11:00-15:15) für 6 erfahrene Software-Entwickler (Java, Spring Boot, Vue.js).

**WICHTIG — Zielgruppe:** Die Teilnehmer sind erfahrene Entwickler. Keine Grundlagen erklären die sie bereits kennen (z.B. was REST ist, was eine Datenbank ist). Stattdessen technische Tiefe: Token-Kosten, Context-Window-Management, Code-Vergleiche (vorher/nachher). Mindestens 40% der Folien sollen Code-Beispiele oder technische Diagramme enthalten.

## Design-Vorgaben
- Stil inspiriert von iamds.com: modern, clean, viel Weißraum, tech-professional
- Wenig Text pro Folie (max. 5-6 Bulletpoints, kurze Sätze)
- Große Schrift (min. 24pt für Body, 36pt für Titel)
- Schriftart: Open Sans (oder Calibri als Fallback)
- Diagramme und Tabellen wo möglich statt Fließtext
- Farbschema:
  - Primärfarbe: Deep Blue (#3f59ff) — für Überschriften, Buttons, Highlights
  - Sekundärfarbe: Navy (#212b80) — für Titel, dunkle Hintergründe
  - Akzentfarbe: Golden Yellow (#ffd440) — für Hervorhebungen, Call-to-Actions
  - Hintergrund: Weiß (#ffffff) mit viel Weißraum
  - Text: Dunkelgrau (#212121)
- Jede Folie sollte eine klare Kernaussage haben
- Sprache: Deutsch

## Folienstruktur (28 Folien)

### Folie 1: Titelfolie
- Titel: "Tag 1: Greenfield — Von der Idee zur App mit KI"
- Untertitel: "AI-Blueprint Schulung"
- Datum und Trainer-Name als Platzhalter

### Folie 2: Agenda
- Zeitplan als Tabelle:
  - 09:00-09:30 — Was ist ein LLM? Das mentale Modell
  - 09:30-10:00 — Mindset-Shift & Paradigmenwechsel
  - 10:00-10:30 — KERNEL-Framework: Prompts die funktionieren
  - 10:30-11:00 — Tools, Plan Mode & Verifikation
  - 11:00-15:15 — Hands-on: KI-Chatbot bauen (mit Mittagspause)

### Folie 3: Wie ein LLM technisch arbeitet
- 3 Schritte als Diagramm:
  1. Tokenization (BPE): "getUserById" = 3 Tokens ["get", "User", "ById"] — CamelCase kostet mehr Tokens als snake_case
  2. Transformer + Attention: Berechnet Wahrscheinlichkeit des nächsten Tokens basierend auf Kontext
  3. Sampling: Temperature 0.0 = deterministisch (Code) vs. 0.7 = kreativ (Architektur-Alternativen)
- Keine vereinfachte "Text gelesen"-Erklärung — stattdessen technische Darstellung

### Folie 4: Token-Kosten — Was das konkret bedeutet
- Kosten-Tabelle:
  | Modell | Input/1M Tokens | Output/1M Tokens | Context Window | ~Kosten pro Feature |
  | Claude Sonnet 4 | $3 | $15 | 200k | ~$0.18 |
  | Claude Opus 4 | $15 | $75 | 200k | ~$0.90 |
  | GPT-4o | $2.50 | $10 | 128k | ~$0.13 |
  | Devstral 2 (Mistral) | GRATIS | GRATIS | 256k | Aktuell kostenlos |
  | Codestral (Mistral) | €0.30 | €0.90 | 256k | ~€0.01 |
  | Mistral Large | €2 | €6 | 128k | ~€0.08 |
- Kernaussage: "200k Tokens ≈ 500 Seiten Code. Aber: Je voller, desto schlechter (Context Rot)."

### Folie 5: 3 typische Code-Halluzinationen
- 3 Code-Beispiele:
  1. Nicht-existierende API: `repository.findByNameContainingIgnoreCase()` — existiert nur mit korrekter Spring Data Config
  2. Falsche Version: `WebSecurityConfigurerAdapter` — in Spring Boot 3 entfernt
  3. Erfundene CLI-Flags: `mvn spring-boot:run --debug-port=5005` — existiert nicht so
- Kernaussage: "KI kennt das Pattern, aber nicht EUER Projekt. Output immer gegen Compiler + Docs prüfen."

### Folie 6: Der Mindset-Shift
- Zwei Boxen nebeneinander:
  - FRÜHER: Entwickler = jemand der Code schreibt | Kernaufgabe: Programmiersprache beherrschen | Output: Lines of Code
  - HEUTE: Entwickler = jemand der Probleme löst | Kernaufgabe: Problem definieren + Architektur + Verifikation | Output: Funktionierendes System
- Zitat: "The use of Software Engineers will be to solve problems."

### Folie 7: Der Paradigmenwechsel im Detail
- Tabelle mit 6 Zeilen:
  | Aspekt | BISHER | ZUKUNFT |
  | Rolle | Coder | Problem Solver |
  | Kernaufgabe | Code schreiben | Probleme definieren + Architektur |
  | Skill | Programmiersprachen | Architektur + Prompt Engineering |
  | Output | Lines of Code | Funktionierende Systeme |
  | Tools | IDE + Compiler | IDE + KI-Agents |
  | Produktivität | 1 Entwickler | 1 Dev + KI = 3-5x Output |

### Folie 8: Was das fürs Unternehmen heißt
- 4 Bulletpoints mit Icons:
  - Hiring: Problemlöser suchen, nicht nur Programmierer
  - Training: Prompt Engineering als Pflicht-Skill
  - Reviews: Lösungs-Qualität statt nur Code-Qualität
  - Kultur: Safe-to-Fail-Umgebung für KI-Experimente

### Folie 9: Vibe Coding — Was ist das?
- Ablauf-Diagramm (5 Schritte, horizontal):
  1. Idee beschreiben → 2. KI plant Architektur → 3. KI implementiert → 4. Du verifizierst → 5. KI iteriert
- Kernaussage: "Du bist Architekt und Verifikator, KI ist Umsetzer."

### Folie 10: Vibe Coding — Der Unterschied
- Zwei Code-Blöcke nebeneinander:
  - VAGE: "Baue eine Login-Funktion" → KI fragt 5x nach, stoppt
  - KONKRET: POST /api/v1/auth/login, Body: { email, password }, Response: { token, expiresIn }, Fehler: 401 → KI baut durch ohne Rückfrage

### Folie 11: KERNEL-Framework — Übersicht
- Große visuelle Grafik mit den 6 Buchstaben als Akronym:
  - K = Keep it simple
  - E = Explicit constraints
  - R = Reproducible results
  - N = Narrow scope
  - E = Easy to verify
  - L = Logical structure
- Untertitel: "6 Prinzipien für Prompts die beim ersten Versuch funktionieren"

### Folie 12: K — Keep it simple
- Regel: "Ein Prompt = ein Ziel"
- Beispiel Schlecht: "Erstelle User-API mit Login, Registrierung, Passwort-Reset und Tests"
- Beispiel Gut: "Erstelle POST /api/users Endpoint für User-Registrierung"

### Folie 13: E — Explicit constraints
- Regel: "Sag der KI was sie NICHT tun soll"
- Beispiel: "Java Spring Boot. Keine externe Auth-Library. Keine Methoden über 30 Zeilen."

### Folie 14: R — Reproducible results
- Regel: "Keine vagen Referenzen — konkrete Versionen"
- Beispiel Schlecht: "Nutze aktuelle Best Practices"
- Beispiel Gut: "Java 17, Spring Boot 3.2, JPA/Hibernate, PostgreSQL 15"

### Folie 15: N — Narrow scope
- Regel: "Ein Prompt, ein Ziel"
- Beispiel Schlecht: Code + Tests + Dokumentation in einem Prompt
- Beispiel Gut: Erst Code, dann separater Prompt für Tests

### Folie 16: E — Easy to verify
- Regel: "Klare Erfolgskriterien definieren"
- Beispiel: "Schreib einen Test: POST /api/users mit gültigem Body → 201 Created"

### Folie 17: L — Logical structure + KERNEL-Gesamtbeispiel
- 4 Blöcke (Context → Task → Constraints → Format) als Schablone
- Komplettes KERNEL-Beispiel links, generierter Java-Code rechts (Vorher/Nachher):
  - OHNE KERNEL: "Hilf mir eine Spring-App zu bauen" → generisches Hello-World (5 Zeilen Code)
  - MIT KERNEL: Context/Task/Constraints/Format → vollständiger UserController + Service mit BCrypt, @Valid, Interface-Pattern (15 Zeilen Code)

### Folie 18: Erweiterte Prompting-Techniken
- 4 Techniken als Kacheln:
  1. Kontext + Motivation erklären (Warum > Was)
  2. Few-Shot Beispiele (3-5 Beispiele verdoppeln Treffsicherheit)
  3. XML-Tags für komplexe Prompts
  4. Schrittweise denken lassen

### Folie 19: Tool-Vergleich — Claude Code vs. Mistral Vibe vs. Copilot
- Vergleichstabelle:
  | Merkmal | Claude Code | Mistral Vibe | GitHub Copilot |
  | Typ | CLI-Agent | CLI-Agent | IDE-Plugin |
  | Stärke | CLAUDE.md, Rules, Skills, Swarms | Open Source, Devstral 2 GRATIS | Inline-Autocomplete |
  | Wann nutzen | Große Features, Refactoring | Kostenbewusst, Open-Source-Präferenz | Tägliches Coding |
  | Config | CLAUDE.md + .claude/ | .vibe/config.toml + AGENTS.md | copilot-instructions.md |
  | Kosten | Usage-based oder $100/Mo flat | GRATIS (Devstral 2) / Teams €25/Mo | $10-39/Mo |
  | MCP | .mcp.json | [[mcp_servers]] in config.toml | .vscode/mcp.json |
- Kernaussage: "Nicht dogmatisch — je nach Task das bessere Tool nehmen. Alle drei können parallel laufen."

### Folie 20: CLAUDE.md — Projektkontext geben
- Beispiel einer CLAUDE.md (vereinfacht, 5 Zeilen)
- Zwei Screenshots/Mockups nebeneinander: "Ohne CLAUDE.md" vs. "Mit CLAUDE.md"

### Folie 21: Rules & Plugins
- Rules: Automatisch aktiv bei bestimmten Dateitypen
  - .claude/rules/java-spring.md → bei *.java
  - .claude/rules/vue-frontend.md → bei *.vue, *.ts
- Plugins: ralph-loop, context7, security-guidance

### Folie 22: MEMORY.md — Projektgedächtnis
- Unterschiedstabelle:
  | | CLAUDE.md | MEMORY.md |
  | Zweck | Anweisungen | Projekt-Wissen |
  | Wer pflegt | Ihr (manuell) | KI (automatisch) |
  | Im Repo | Ja | Nein (lokal) |

### Folie 23: Plan Mode & Verifikation
- Plan Mode Workflow: Prompt → Plan reviewen → Freigeben → KI arbeitet ab
- Chain-of-Verification: 4 Schritte (Antwort → Verifikationsfragen → Beantworten → Korrektur)

### Folie 24: Ollama — Lokale KI
- Wann lokal: Datenschutz, offline, Kosten
- 3 Zeilen Setup: brew install ollama → ollama pull qwen2.5-coder:7b → ollama serve
- Empfohlene Modelle als kleine Tabelle (qwen2.5-coder:7b = empfohlen)

### Folie 25: Datenschutz-Regeln
- Tabelle: Was passiert mit euren Daten?
  | Situation | Daten-Handling |
  | Claude Code (API) | Nicht für Training (Enterprise) |
  | Copilot Business | Nicht für Training (vertraglich) |
  | Lokale Modelle | Nichts verlässt den Rechner |
- Faustregel: Kein Kundencode, keine Passwörter, keine internen IPs

### Folie 26: Hands-on Aufgabe — Architektur
- Architektur-Diagramm:
  Vue.js Chat-UI (localhost:5173) ↔ POST /api/v1/chat ↔ Spring Boot Backend (localhost:8080) ↔ Claude API

### Folie 27: Hands-on — Ablauf
- Teil 1 (Claude Code): Spring Boot Backend
  - Setup → Plan → DTOs → LlmService → ChatController → Test
- Teil 2 (GitHub Copilot): Vue.js Frontend
  - Vite-Projekt → Pinia Store → ChatWindow → Typing Indicator → Verbinden

### Folie 28: Context-Window-Management
- Grafik: Neuer Chat (5% belegt, 95% für Aufgabe) → Nach 30 Min (40%) → Nach 2h (70%, Qualität sinkt) → Voll (Komprimierung, Details verloren)
- Warnsignale: KI wiederholt Fehler, vergisst Konventionen, generischer Code
- Strategien-Tabelle:
  | Situation | Strategie |
  | Feature fertig | Neuen Chat starten |
  | Context voll | /compact |
  | Großes Feature | Sub-Agents von Anfang an |
- Faustregel: "Ein Chat = ein Feature"

### Folie 29: Zusammenfassung Tag 1
- Key Takeaways (5 Bulletpoints):
  1. LLMs sind Wahrscheinlichkeits-Maschinen — immer verifizieren
  2. Entwickler = Problem Solver, KI = Umsetzer
  3. KERNEL-Framework für systematisch bessere Prompts
  4. CLAUDE.md + MEMORY.md = KI kennt euer Projekt
  5. Plan Mode → Implementierung → Verifikation → Lessons Learned
