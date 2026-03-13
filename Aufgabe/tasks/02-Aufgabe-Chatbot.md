# Tag 1 – Aufgabe: KI-Chatbot bauen

**Arbeitsverzeichnis:** `Aufgaben/` (vorbereitet, wird ueber alle 3 Tage weiterverwendet)

---

## Ziel

Ein funktionsfaehiger KI-Chatbot — Backend und Frontend — der mit einem LLM kommuniziert.

```
Frontend (Chat-UI)
    ↓ POST /api/v1/chat
Backend (Spring Boot)
    ↓
LLM API (Claude / Mistral)
    ↓
Antwort erscheint im Chat
```

**Hinweis:** Das Backend wird an Tag 2 um einen PostgreSQL-Zugriff via MCP erweitert.

---

## 1. Projekt anlegen und KI-Setup einrichten

### Backend-Projekt erstellen

```bash
cd Aufgaben
mkdir chatbot-backend && cd chatbot-backend
```

### KI-Setup ausfuehren

```bash
bash <pfad-zum>/dev-setup-template/setup.sh
```

Das Script fragt, welches Tool eingerichtet werden soll (Claude Code, GitHub Copilot oder Mistral Vibe).
Alternativ direkt als Argument: `setup.sh claude`, `setup.sh copilot` oder `setup.sh mistral`.

### Projekt-Konfiguration anpassen

Das Setup legt eine Konfigurationsdatei an (je nach Tool: `CLAUDE.md`, `.github/copilot-instructions.md` oder `AGENTS.md`).
Diese Datei muss fuer das aktuelle Projekt angepasst werden — sie ist der wichtigste Kontext fuer die KI.

Relevante Informationen:
- Was ist dieses Projekt?
- Welche LLM-API wird angesprochen?
- Wie ist die Infrastruktur aufgebaut (Ports, CORS, API-Key-Handling)?

> Je praeziser der Kontext, desto bessere Ergebnisse.

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

### Akzeptanzkriterien

- [ ] `POST /api/v1/chat` liefert eine LLM-Antwort zurueck
- [ ] Chat-Verlauf (history) wird korrekt mitgesendet
- [ ] API-Key ist nicht im Code enthalten
- [ ] Fehler werden sauber behandelt (kein Stacktrace an den Client)
- [ ] CORS erlaubt `localhost:5173`

---

## 3. Frontend aufsetzen und implementieren

```bash
cd Aufgaben
mkdir chatbot-frontend && cd chatbot-frontend
```

KI-Setup erneut ausfuehren und Konfigurationsdatei fuer das Frontend-Projekt anpassen.

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

---

## 4. Integration testen

Backend laeuft auf `:8080`, Frontend auf `:5173`.

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
