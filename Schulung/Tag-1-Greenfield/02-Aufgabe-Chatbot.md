# Tag 1 – Aufgabe: KI-Chatbot bauen

**Arbeitsverzeichnis:** `Aufgabe/` (vorbereitet, wird ueber alle 3 Tage weiterverwendet)

---

## Ziel

Ein funktionsfaehiger KI-Chatbot — Backend und Frontend — der mit einem LLM kommuniziert.

```
Frontend (Chat-UI)
    ↓ POST /api/v1/chat
Backend (Spring Boot)
    ↓
LLM API (Mistral, alternativ Claude)
    ↓
Antwort erscheint im Chat
```

**Hinweis:** Das Backend wird an Tag 2 um einen PostgreSQL-Zugriff via MCP erweitert.

---

## 1. Projekt anlegen und KI-Setup einrichten

### Projektstruktur (Zielzustand)

```
Aufgabe/
  .claude/          # KI-Setup (einmal einrichten)
  chatbot-backend/  # Java Spring Boot
  chatbot-frontend/ # Vue.js 3
  tasks/            # Planung + Lessons
  start.sh          # Orchestrierung (alle Services)
  .env              # API-Keys
  .gitignore
```

> Das KI-Setup wird einmal im Root `Aufgabe/` ausgefuehrt — nicht pro Unterordner.

### KI-Setup ausfuehren

```bash
cd Aufgabe
bash <pfad-zum>/dev-setup-template/setup.sh
```

Das Script fragt, welches Tool eingerichtet werden soll (Claude Code, GitHub Copilot oder Mistral Vibe).
Alternativ direkt als Argument: `setup.sh claude`, `setup.sh copilot` oder `setup.sh mistral`.

### Backend-Projekt erstellen

```bash
cd Aufgabe
mkdir chatbot-backend && cd chatbot-backend
```

### Projekt-Konfiguration anpassen

Das Setup legt eine Konfigurationsdatei an (je nach Tool: `CLAUDE.md`, `.github/copilot-instructions.md` oder `AGENTS.md`).
Diese Datei muss fuer das aktuelle Projekt angepasst werden — sie ist der wichtigste Kontext fuer die KI.

Relevante Informationen:
- Was ist dieses Projekt?
- Welche LLM-API wird angesprochen?
- Wie ist die Infrastruktur aufgebaut (Ports, CORS, API-Key-Handling)?

> Je praeziser der Kontext, desto bessere Ergebnisse.

> **LLM-API: Mistral**
>
> | Eigenschaft | Wert |
> |-------------|------|
> | Endpoint | `https://api.mistral.ai/v1/chat/completions` |
> | Model | `mistral-large-latest` |
> | Environment Variable | `MISTRAL_API_KEY` |
> | API-Format | OpenAI-kompatibel (Messages-Array, roles, etc.) |
>
> Mistral verwendet dasselbe Request-Format wie die OpenAI API.
> Alternativ kann auch die Claude API verwendet werden — dann Endpoint und Model entsprechend anpassen.

---

## 2. Backend planen und implementieren

### Plan Mode verwenden

Vor der Implementierung im **Plan Mode** einen Umsetzungsplan erstellen lassen:

| Tool | Plan Mode |
|------|-----------|
| Claude Code | `/plan` oder `Shift+Tab` |
| GitHub Copilot | Im Chat: "Erstelle einen Plan fuer..." |
| Mistral Vibe | `vibe --agent plan` |

### Anforderungen

- Stack: Java 17, Spring Boot 3.2, Maven
- Endpoint: `POST /api/v1/chat`
- Request nimmt eine Nachricht und den bisherigen Chat-Verlauf entgegen
- Backend leitet die Anfrage an die LLM-API weiter und gibt die Antwort zurueck
- API-Key aus Environment Variable
- CORS fuer das Frontend aktivieren

Den generierten Plan pruefen, dann umsetzen lassen.

### Bekannte Stolpersteine

> Diese Punkte stammen aus den Lessons Learned und sparen Zeit bei der Implementierung:

- **Lombok-Version:** Spring Boot 3.2 liefert Lombok 1.18.30 — das ist inkompatibel mit Java 21+. Lombok-Version explizit auf **1.18.44+** setzen und `maven-compiler-plugin` mit Lombok Annotation Processor konfigurieren.
- **Maven Wrapper:** Immer `./mvnw` statt `mvn` verwenden. Das macht das Projekt portabel und vermeidet Probleme mit fehlender Maven-Installation.
- **`.gitignore` von Anfang an anlegen:** `target/`, `node_modules/`, `.env`, `.DS_Store`, `.idea/`, `.vscode/` — sonst landen Artefakte und Secrets im Repository.
- **Kein `set -e` in Start-Scripts:** Bei Scripts die Hintergrundprozesse managen bricht `set -e` bei harmlosen Fehlern ab (z.B. `kill` auf bereits beendete Prozesse).
- **Port-Kill braucht Polling:** Nach `kill -9` ist der Port nicht sofort frei. Immer aktiv warten bis `lsof` den Port als frei meldet (`free_port`-Funktion mit Retry-Loop).

### Akzeptanzkriterien

- [ ] `POST /api/v1/chat` liefert eine LLM-Antwort zurueck
- [ ] Chat-Verlauf (history) wird korrekt mitgesendet
- [ ] API-Key ist nicht im Code enthalten
- [ ] Fehler werden sauber behandelt (kein Stacktrace an den Client)
- [ ] CORS erlaubt `localhost:5173`

---

## 3. Frontend aufsetzen und implementieren

```bash
cd Aufgabe
mkdir chatbot-frontend && cd chatbot-frontend
```

KI-Konfigurationsdatei fuer das Frontend-Projekt anpassen (die Konfiguration im Root `Aufgabe/` wird weiterverwendet).

### Anforderungen

- Stack: Vue.js 3 (Composition API), TypeScript, Vite, Pinia, Axios
- Chat-Ansicht mit Nachrichten-Liste, Eingabefeld und Senden-Button
- Pinia Store fuer State Management
- Kommunikation mit `POST http://localhost:8080/api/v1/chat`
- Chat-Verlauf wird bei jeder Anfrage mitgesendet

Auch hier: Plan Mode verwenden, Plan pruefen, dann umsetzen lassen.

### Akzeptanzkriterien

- [ ] Chat-UI rendert und ist benutzbar
- [ ] Nachrichten werden an das Backend gesendet
- [ ] Antworten erscheinen im Chat
- [ ] Loading-Indikator waehrend der Verarbeitung
- [ ] Fehler werden angezeigt (z.B. Backend nicht erreichbar)
- [ ] Auto-Scroll zu neuen Nachrichten
- [ ] Enter sendet, Shift+Enter fuer neue Zeile

---

## 4. Bonus: TracePanel (optional)

> Dieses Feature ist **optional** und dient der Transparenz beim Debugging.

Ein Debug-Panel das API-Requests, Tool-Calls und Timings anzeigt:
- Toggle-Button in der Toolbar zum Ein-/Ausblenden
- Zeigt pro Nachricht: Request/Response, Dauer, verwendetes Model
- Spaeter (Tag 2): auch Tool-Call-Details und MCP-Interaktionen

---

## 5. start.sh — Orchestrierung

Ein Start-Script das Backend und Frontend gemeinsam startet:

### Anforderungen

- API-Key als Argument oder aus `.env` lesen
- `free_port`-Funktion: Port freigeben mit Polling bis Port tatsaechlich frei ist
- Backend starten (`./mvnw spring-boot:run`), warten bis Port 8080 erreichbar
- Frontend starten (`npm run dev`), warten bis Port 5173 erreichbar
- Cleanup via `trap` bei SIGINT (Ctrl+C beendet alle Prozesse sauber)
- Kein `set -e` (siehe Stolpersteine oben)

---

## 6. Integration testen

Backend laeuft auf `:8080`, Frontend auf `:5173`.

> **Hinweis:** Integration-Tests sind manuell — kein automatisiertes E2E-Framework noetig.

**Test-Szenarien:**
1. Einfache Frage stellen — LLM antwortet
2. Folgefrage stellen — LLM antwortet mit Bezug auf die vorherige Nachricht
3. Backend stoppen, Nachricht senden — Fehlermeldung erscheint, UI friert nicht ein

---

## Retrospektive

1. Welcher Prompt hat am besten funktioniert?
2. Was musste korrigiert werden und warum?
3. Wie hat der Plan Mode die Ergebnisse beeinflusst?
4. Was wurde in die Konfigurationsdatei eingetragen — was wuerde ergaenzt werden?
5. Erkenntnisse in `tasks/lessons.md` eintragen
