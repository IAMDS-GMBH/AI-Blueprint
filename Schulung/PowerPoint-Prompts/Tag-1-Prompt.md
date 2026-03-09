# PowerPoint-Prompt: Tag 1 — Greenfield-Entwicklung mit KI

## Anweisung
Erstelle eine professionelle PowerPoint-Präsentation für einen Schulungstag zum Thema "Greenfield-Entwicklung mit KI". Die Präsentation begleitet einen ganzen Trainingstag (Theorie 9:00-11:00, Praxis 11:00-15:15) für 6 Software-Entwickler.

## Design-Vorgaben
- Professionell, modern, clean
- Wenig Text pro Folie (max. 5-6 Bulletpoints, kurze Sätze)
- Große Schrift (min. 24pt für Body, 36pt für Titel)
- Diagramme und Tabellen wo möglich statt Fließtext
- Farbschema: Dunkelblau (#1a365d) als Primärfarbe, helles Grau (#f7fafc) als Hintergrund, Akzentfarbe Orange (#ed8936)
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

### Folie 3: Was ist ein LLM?
- Vereinfachtes Diagramm: Text rein → Token-Vorhersage → Text raus
- Kernaussage: "Ein LLM hat extrem viel Text gelesen und gelernt: Nach diesem Text kommt mit hoher Wahrscheinlichkeit dieser Text."
- KI ist ein schneller Erstentwurf-Generator, kein Orakel

### Folie 4: Was das in der Praxis heißt
- Tabelle mit 4 Zeilen:
  | Eigenschaft | Was das für euch heißt |
  | KI ist kein Datenbank-Lookup | Sie erfindet Antworten – auch wenn falsch ("Halluzination") |
  | KI hat ein Kontextfenster | Sieht nur den aktuellen Chat, nicht das ganze Repo |
  | Prompt-Qualität = Output-Qualität | Je präziser die Eingabe, desto besser |
  | KI kennt keine "Wahrheit" | Antwortet überzeugend auch wenn sie falsch liegt |

### Folie 5: Warum halluziniert KI?
- Grafik: Wahrscheinlichkeitsverteilung → KI wählt die wahrscheinlichste Antwort
- Kernaussage: "KI sagt nicht 'ich weiß es nicht' – sie sagt die wahrscheinlichste Antwort."
- Große Schrift unten: "Nie blind vertrauen. Immer verifizieren."

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

### Folie 17: L — Logical structure
- 4 Blöcke (Context → Task → Constraints → Format) als Schablone
- Komplettes Beispiel mit Java Spring Boot

### Folie 18: Erweiterte Prompting-Techniken
- 4 Techniken als Kacheln:
  1. Kontext + Motivation erklären (Warum > Was)
  2. Few-Shot Beispiele (3-5 Beispiele verdoppeln Treffsicherheit)
  3. XML-Tags für komplexe Prompts
  4. Schrittweise denken lassen

### Folie 19: Tool-Vergleich — Claude Code vs. GitHub Copilot
- Vergleichstabelle:
  | Merkmal | Claude Code | GitHub Copilot |
  | Stärke | Autonome Agents, Plan Mode, Swarms | Inline-Autocomplete, IDE-Integration |
  | Wann nutzen | Große Features, Refactoring | Tägliches Coding, Vervollständigung |
  | CLI-Native | Ja (Terminal) | Nein (nur IDE) |
  | Kosten | Usage-based oder $100/Mo flat | $10-39/Mo |

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

### Folie 28: Zusammenfassung Tag 1
- Key Takeaways (5 Bulletpoints):
  1. LLMs sind Wahrscheinlichkeits-Maschinen — immer verifizieren
  2. Entwickler = Problem Solver, KI = Umsetzer
  3. KERNEL-Framework für systematisch bessere Prompts
  4. CLAUDE.md + MEMORY.md = KI kennt euer Projekt
  5. Plan Mode → Implementierung → Verifikation → Lessons Learned
