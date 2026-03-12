# Prompt-Protokoll – KI-Chatbot Projekt
Datum: 2026-03-11

---

## Prompt 1: Plan erstellen
**Prompt:**
> /plan Verwende die Datei "02-Aufgabe-Chatbot.md" als TODOS ab dem Punkt 1. Erstelle einen Plan mit Verification Chain. Ich will den Plan im Swarm Modus mit Subagents ohne Interruption ausführen.

**Ergebnis:**
- `tasks/todo.md` erstellt mit vollstaendigem Swarm-Plan
- 3 Agents geplant: Agent 1 (Backend), Agent 2 (Frontend), Agent 3 (Verification)
- 10 Schritte Backend, 8 Schritte Frontend, 9 Verification-Checks
- Akzeptanzkriterien aus der Aufgabe uebernommen
- Abhaengigkeiten: Agent 1+2 parallel, Agent 3 sequenziell danach

---

## Prompt 2: Start-Script erweitern
**Prompt:**
> Erstelle ein Script für den Systemstart, dasfrontend und backend starten und einen Mistral API Key engegennimmt um die LLM API aufzurufen.

**Ergebnis:**
- Plan angepasst: LLM von Claude/Anthropic auf **Mistral API** umgestellt
- `ANTHROPIC_API_KEY` → `MISTRAL_API_KEY`
- Neuer Schritt 1.10: `start.sh` mit API-Key-Argument, parallel Start, Cleanup
- Verification-Schritt 3.5 hinzugefuegt

---

## Prompt 3: Port-Pruefung im Start-Script
**Prompt:**
> Beim Startscript pruefen ob die Ports belegt sind und ggf. die Services stoppen damit frontend und backend nur gemeinsam gestartet werden koennen.

**Ergebnis:**
- Schritt 1.10 erweitert: Port 8080/5173 pruefen, belegende Prozesse killen
- Verification 3.5 erweitert: Port-Freigabe-Logik pruefen
- Neues Akzeptanzkriterium: "Belegte Ports werden automatisch freigegeben"

---

## Prompt 4: Swarm starten
**Prompt:**
> ja

**Ergebnis:**
- **Agent 1 (Backend):** 12 Dateien erstellt
  - `chatbot-backend/pom.xml` — Spring Boot 3.2.5, Java 17
  - `ChatbotApplication.java` — Main-Klasse
  - `WebConfig.java` — CORS fuer localhost:5173
  - `ChatRequest.java`, `ChatMessage.java`, `ChatResponse.java` — DTOs
  - `ChatService.java` + `ChatServiceImpl.java` — Interface + Mistral API via WebClient
  - `ChatController.java` — POST /api/v1/chat
  - `GlobalExceptionHandler.java` + `ErrorResponse.java` — Error Handling
  - `application.yml` — Port 8080, Mistral-Config
  - `start.sh` — Start-Script mit Port-Check und Cleanup

- **Agent 2 (Frontend):** 11 Dateien erstellt
  - `chatbot-frontend/package.json` — Vue 3.4, Pinia, Axios, Vite
  - `tsconfig.json`, `tsconfig.app.json`, `tsconfig.node.json` — TypeScript Config
  - `vite.config.ts` — Vue Plugin, Port 5173
  - `src/types/chat.ts` — Interfaces
  - `src/services/api.ts` — Axios Service
  - `src/stores/chat.ts` — Pinia Store
  - `src/components/ChatMessage.vue` — Message Bubbles
  - `src/components/ChatView.vue` — Chat-UI mit Loading, Error, Auto-Scroll
  - `src/App.vue` — Layout mit Header

- **Agent 3 (Verification):** Alle 9 Checks bestanden
  - 1 Fix noetig: Lombok 1.18.44 fuer Java 25 Kompatibilitaet
  - Maven Wrapper hinzugefuegt (mvn war nicht installiert)
  - start.sh auf ./mvnw umgestellt
  - 15/15 Akzeptanzkriterien erfuellt

---

## Prompt 5: Minecraft Design
**Prompt:**
> /frontend-design Bitte erstelle einen Plan für die Designoptimierng des Chatbot im Stil von Minecraft

**Ergebnis:**
- Design-Plan erstellt mit Minecraft-Aesthetic:
  - Pixel-Font: Silkscreen (Google Fonts)
  - Farbpalette: Dirt, Stone, Grass, Gold (MC-typisch)
  - UI-Elemente: Block-Bubbles, 3D-Borders, keine border-radius
  - Animationen: Block-Place Effekt, Pixel-Bounce Loading
  - Labels: "Steve" (User), "Crafting AI" (Bot)

---

## Prompt 6: Minecraft Design umsetzen
**Prompt:**
> ja

**Ergebnis:**
- 3 Dateien komplett ueberarbeitet (nur CSS/Template, keine Logik):
  - `App.vue` — Dirt-Textur-Hintergrund, Stein-Header, Gold-Titel "CRAFTING AI", Silkscreen Font
  - `ChatMessage.vue` — Pixelige Block-Bubbles, 3D-Borders, Block-Place Animation, Avatare
  - `ChatView.vue` — Minecraft-Button "Craft!", Terminal-Input, Block-Loading, Buch-Empty-State, Custom Scrollbar, TNT-Error
- Frontend Build erfolgreich (0 Fehler)

---

## Prompt 7: Start-Script Fix
**Prompt:**
> Beim ausfuehren des Scripts mit der start.sh habe ich folgenden Fehler bekommen: [Build Failure Exit 137]

**Ergebnis:**
- Root Cause: Port-Kill ohne Warten — neuer Prozess startete auf noch belegtem Port
- Fix: `free_port` Funktion mit aktivem Polling (max 10s warten bis Port frei)
- `set -e` entfernt (verursachte Abbrueche bei harmlosen Fehlern)
- Cleanup verbessert mit `wait` auf Kind-Prozesse

---

## Prompt 8: .gitignore
**Prompt:**
> Best Practice Gitignore file in das Projekt legen

**Ergebnis:**
- `.gitignore` erstellt im Aufgabe-Root
- Abdeckung: Java/Maven, Node/npm, IDE, Secrets, OS, Logs

---

## Prompt 9: Lessons aktualisieren
**Prompt:**
> Update der lessons.md mit den Learnings aus der Implementation

**Ergebnis:**
- 5 Lessons in `tasks/lessons.md` eingetragen:
  1. Lombok + Java 25 Inkompatibilitaet
  2. start.sh Port-Kill ohne Warten
  3. set -e in Start-Scripts gefaehrlich
  4. Maven nicht installiert → immer ./mvnw
  5. .gitignore fehlte beim Setup

---

## Prompt 10: Review Agent soll lessons.md automatisch updaten
**Prompt:**
> Das naechste mal die lessons.md automatisch updaten. Anpassen des Test und Review Agent

**Ergebnis:**
- Neue Lesson eingetragen: "lessons.md wurde nicht automatisch aktualisiert"
- `MEMORY.md` erweitert: Swarm-Regel "Review Agent muss IMMER lessons.md updaten"

---

## Prompt 11: Test Agent als fester Bestandteil
**Prompt:**
> Tests sollen automatisch geschrieben werden fuer jedes Feature. Automtishes ausführen des Test Agents.

**Ergebnis:**
- `MEMORY.md` aktualisiert: Swarm-Reihenfolge Dev → Test → Review
- Neue Lesson: "Tests wurden nicht automatisch geschrieben"

---

## Prompt 12: Agent-Konfigurationen anpassen
**Prompt:**
> Kann dieses Verhalten auch in die Agent Konfigurationen uebernommen werden damit das immer greift egal ob Swarm?

**Ergebnis:**
- `dev-agent.md` erweitert: Pflicht nach jeder Implementierung Test Agent zu starten (Ausnahme: dedizierter Test Agent im Swarm)
- `review-agent.md` erweitert: Pflicht als letzten Schritt immer lessons.md zu aktualisieren (gilt bei jedem Review-Typ)
