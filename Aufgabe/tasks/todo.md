# Plan: KI-Chatbot (Backend + Frontend)
Datum: 2026-03-11
Status: ABGESCHLOSSEN

## Ziel
Funktionsfaehiger KI-Chatbot: Spring Boot Backend + Vue.js Frontend, der mit einer LLM-API kommuniziert.
Der Plan ist fuer **vollautomatische Ausfuehrung im Swarm-Modus** optimiert — keine Rueckfragen.

## Architektur
```
Frontend (Vue.js 3, :5173)
    ↓ POST /api/v1/chat
Backend (Spring Boot 3.2, :8080)
    ↓ Mistral API (REST via WebClient)
LLM-Antwort → zurueck an Chat-UI
```

## Start-Script
```bash
# Aufruf: ./start.sh <MISTRAL_API_KEY>
# 1. Prueft ob Ports 8080/5173 belegt sind → killt belegende Prozesse
# 2. Startet Backend (:8080) und Frontend (:5173) parallel
# 3. API-Key wird als Environment Variable an das Backend uebergeben
# 4. Ctrl+C beendet beide Prozesse sauber
```

---

## Swarm-Aufteilung

### Agent 1: Backend (Dev Agent)
- [ ] 1.1 Projektstruktur anlegen: `chatbot-backend/` mit Spring Boot 3.2, Java 17, Maven
- [ ] 1.2 Maven Dependencies: `spring-boot-starter-web`, `spring-boot-starter-webflux` (WebClient fuer Mistral API), `lombok`, `spring-boot-starter-validation`
- [ ] 1.3 CORS-Konfiguration: `localhost:5173` erlauben
- [ ] 1.4 DTOs erstellen: `ChatRequest` (message, history), `ChatResponse` (response, role)
- [ ] 1.5 Service-Interface + Impl: `ChatService` → Mistral API (`https://api.mistral.ai/v1/chat/completions`) via WebClient mit Chat-History, Model: `mistral-small-latest`
- [ ] 1.6 REST Controller: `POST /api/v1/chat` → 200 mit Antwort, Fehler via @RestControllerAdvice
- [ ] 1.7 API-Key aus Environment Variable (`MISTRAL_API_KEY` via `@Value`)
- [ ] 1.8 `application.yml` mit Port 8080, API-Config
- [ ] 1.9 Error Handling: GlobalExceptionHandler, keine Stacktraces an Client
- [ ] 1.10 Start-Script: `start.sh` im Projekt-Root — nimmt `MISTRAL_API_KEY` als Argument, prueft ob Ports 8080/5173 belegt sind und killt belegende Prozesse automatisch, startet Backend + Frontend parallel, Cleanup bei Ctrl+C

### Agent 2: Frontend (Dev Agent)
- [ ] 2.1 Projektstruktur anlegen: `chatbot-frontend/` mit Vue.js 3, TypeScript, Vite, Pinia, Axios
- [ ] 2.2 Pinia Store: `useChatStore` — messages[], sendMessage(), loading, error
- [ ] 2.3 Chat-Komponente: Nachrichten-Liste, Eingabefeld, Senden-Button
- [ ] 2.4 Axios-Composable/Service: `POST http://localhost:8080/api/v1/chat` mit History
- [ ] 2.5 Loading-Indikator waehrend API-Call
- [ ] 2.6 Error-Handling: Fehlermeldung wenn Backend nicht erreichbar
- [ ] 2.7 Auto-Scroll zu neuester Nachricht
- [ ] 2.8 Styling: Sauberes Chat-Layout (User vs. Bot Bubbles)

### Agent 3: Verification Chain (Test/Review Agent)
Wird **sequenziell nach** Agent 1 + 2 ausgefuehrt.

- [ ] 3.1 Backend kompiliert: `cd chatbot-backend && mvn compile` → Exit 0
- [ ] 3.2 Backend-Struktur pruefen: Controller, Service Interface+Impl, DTOs, ExceptionHandler vorhanden
- [ ] 3.3 API-Key NICHT im Code hardcoded (Grep nach Schluesselwoertern)
- [ ] 3.4 CORS-Config vorhanden und auf localhost:5173 gesetzt
- [ ] 3.5 Start-Script vorhanden, ausfuehrbar, prueft auf API-Key-Argument und Port-Freigabe-Logik
- [ ] 3.6 Frontend kompiliert: `cd chatbot-frontend && npm install && npm run build` → Exit 0
- [ ] 3.7 Frontend-Struktur pruefen: Pinia Store, Chat-Komponente, kein direkter API-Call in Komponente
- [ ] 3.8 TypeScript-Fehler: `npm run type-check` oder Build ohne TS-Errors
- [ ] 3.9 Akzeptanzkriterien-Checkliste (aus Aufgabe) abgleichen und Ergebnis dokumentieren

---

## Abhaengigkeiten
```
Agent 1 (Backend) ──┐
                     ├──→ Agent 3 (Verification) ──→ Ergebnis in todo.md
Agent 2 (Frontend) ──┘
```
- Agent 1 + 2 laufen **parallel** (keine Abhaengigkeit)
- Agent 3 laeuft **nach** Abschluss von 1 + 2

## Risiken
- **Mistral API nicht erreichbar**: Gegenmassnahme → Fehler wird sauber gefangen, User bekommt Fehlermeldung statt Stacktrace
- **Maven/npm nicht installiert**: Gegenmassnahme → Agents pruefen Verfuegbarkeit zuerst
- **API-Key fehlt zur Laufzeit**: Gegenmassnahme → Code laeuft, Laufzeit-Fehler wird sauber behandelt (kein Kompilierfehler)

## Nicht im Scope
- Unit Tests (werden bei Bedarf separat gemacht)
- Deployment / Docker
- PostgreSQL / MCP-Server (kommt an Tag 2)
- setup.sh / dev-setup-template ausfuehren (wird manuell gemacht)
- Authentifizierung / Security (kein JWT)
- Streaming / SSE (einfacher Request-Response)

## Swarm-Konfiguration
```
Modus:    Parallel (Agent 1 + 2), dann Sequential (Agent 3)
Agents:   2x Dev Agent (Backend + Frontend), 1x Review Agent (Verification)
Autonomie: Voll — keine Rueckfragen, keine Freigabe zwischen Schritten
Worktree:  Nein (alles im gleichen Verzeichnis)
```

## Akzeptanzkriterien (aus Aufgabe)
### Start-Script
- [ ] `./start.sh <API_KEY>` startet Backend + Frontend
- [ ] Ohne API-Key gibt es eine Fehlermeldung
- [ ] Belegte Ports (8080/5173) werden automatisch freigegeben
- [ ] Ctrl+C beendet beide Prozesse sauber

### Backend
- [ ] `POST /api/v1/chat` liefert eine LLM-Antwort zurueck (Mistral API)
- [ ] Chat-Verlauf (history) wird korrekt mitgesendet
- [ ] API-Key ist nicht im Code enthalten
- [ ] Fehler werden sauber behandelt (kein Stacktrace an den Client)
- [ ] CORS erlaubt `localhost:5173`

### Frontend
- [ ] Chat-UI rendert und ist benutzbar
- [ ] Nachrichten werden an das Backend gesendet
- [ ] Antworten erscheinen im Chat
- [ ] Loading-Indikator waehrend der Verarbeitung
- [ ] Fehler werden angezeigt (z.B. Backend nicht erreichbar)

---

## Freigabe
- [ ] Plan freigegeben von: ___________
