# Tag 1 – Aufgabe: KI-Chatbot bauen (Backend + Frontend)

**Jeder Teilnehmer baut beides** – Backend und Frontend alleine
**Tools:** Claude Code (Backend) + GitHub Copilot (Frontend)
**Zeit:** 11:00–15:15 Uhr (3,5 Stunden – vor und nach Mittag)

---

## Was entsteht heute?

Ein vollständiger KI-Chatbot – von 0:

```
Vue.js Chat-UI (http://localhost:5173)
    ↓ POST /api/v1/chat
Spring Boot Backend (http://localhost:8080)
    ↓
Claude / OpenAI API
    ↓
Antwort erscheint im Chat
```

Das Backend ruft das LLM auf. Das Frontend zeigt den Chat an.
Beides baut ihr heute selbst – mit KI als Entwicklungspartner.

**Für Tag 2 wichtig:** In Tag 2 wird das Backend so erweitert,
dass der Chatbot PostgreSQL-Datenbankfragen beantworten kann.

---

## Zeitplan

```
11:00–11:20  Projekt anlegen + dev-setup-template einrichten (setup.sh)
11:20–11:40  Plan mit KI erstellen
11:40–12:00  DTOs + LLM Service (Teil 1)
── Mittagspause ──
12:45–13:15  LLM Service (Teil 2) + Controller
13:15–13:30  Backend testen (curl)
13:30–13:50  Frontend-Projekt anlegen + setup.sh
13:50–14:20  Chat Store + Komponente
14:20–14:50  App zusammenbauen + testen
14:50–15:10  Frontend ↔ Backend verbinden
15:10–15:15  Vorbereitung Präsentation
```

---

## Teil 1: Backend (Claude Code)

### Stack
```
Java 17 + Spring Boot 3.2
Maven
Spring Web, Lombok
```

---

### Schritt 1: Projekt anlegen + dev-setup-template einrichten (20 Min)

**Projektordner anlegen:**
```bash
mkdir chatbot-backend
cd chatbot-backend
```

**KI-Setup aus dem Schulungs-Repo kopieren:**
```bash
# setup.sh aus dev-setup-template einmalig ausführen
# (Pfad anpassen je nachdem wo ihr das Schulungs-Repo geklont habt)
bash ../ai-knowledgebase/dev-setup-template/setup.sh
```

Das Script kopiert automatisch:
- `.claude/agents/`, `.claude/commands/`, `.claude/rules/`, `.claude/skills/`
- `.github/agents/`, `.github/copilot-instructions.md`
- `.mcp.json`, `.vscode/mcp.json`
- `CLAUDE.md`, `MEMORY.md`, `tasks/lessons.md`, `tasks/todo.md`

**CLAUDE.md für dieses Projekt anpassen** (nur den oberen Teil):
```markdown
# CLAUDE.md – Chatbot Backend

## Dieses Projekt
Spring Boot Proxy-Service zwischen Vue.js Frontend und Claude LLM API.
POST /api/v1/chat nimmt Nachrichten entgegen und ruft die Claude API auf.
Wird von Vue.js Frontend auf http://localhost:5173 konsumiert.

## Externe LLM API
Provider: Anthropic Claude
URL: https://api.anthropic.com/v1/messages
API-Key: aus @Value("${anthropic.api-key}"), NIE hardcodiert

## CORS
localhost:5173 erlaubt (Vue.js Frontend)

## Keine Spring Security
(Schulungsumgebung – kein Auth nötig)
```
> Den Rest von CLAUDE.md (Tech Stack, Konventionen, etc.) lasst ihr – der kommt vom dev-setup-template und passt bereits.

> **Variante B: Mistral API (falls euer Team Mistral Teams hat)**
>
> Wenn ihr Mistral Teams Lizenzen habt, könnt ihr statt der Claude API auch die Mistral API verwenden:
> ```markdown
> ## Externe LLM API
> Provider: Mistral AI
> URL: https://api.mistral.ai/v1/chat/completions
> API-Key: aus @Value("${mistral.api-key}"), NIE hardcodiert
> ```
> Wichtig: Mistral nutzt das OpenAI-kompatible Format (messages array).
> In `application.properties`:
> ```
> mistral.api-key=${MISTRAL_API_KEY}
> ```
> Die API-Aufrufe unterscheiden sich leicht (OpenAI-Format statt Anthropic-Format).
> Fragt bei der Implementierung den KI-Assistenten nach dem korrekten Mistral-API-Format.

**Spring Boot Projekt erstellen (Claude Code):**
```bash
claude
```

```
Erstelle ein neues Spring Boot 3.2 Projekt mit Maven.
Dependencies: Spring Web, Lombok, Validation, Spring Boot DevTools.
Package: com.schulung.chatbot
Nur das Skelett – pom.xml und ChatbotApplication.java. Noch kein Feature-Code.
```

---

### Schritt 2: Planen (15 Min)

```
Erstelle nur einen Plan (keinen Code) für folgendes:

POST /api/v1/chat
  Request:  { "message": "...", "history": [{role, content},...] }
  Response: { "reply": "...", "model": "..." }

Der Endpoint leitet message + history an die Claude API weiter.

Zeige: welche Klassen werden erstellt, was macht jede Klasse?
Stack: Java 17, Spring Boot 3.2, Lombok
```

Plan prüfen → dann: *"Führe den Plan Schritt für Schritt aus."*

---

### Schritt 3: DTOs (15 Min)

```
Erstelle die DTOs für den Chat-Endpoint.

Task:
  - ChatRequest: message (String, @NotBlank) + history (List<ChatMessage>)
  - ChatMessage: role (String) + content (String)
  - ChatResponse: reply (String) + model (String)
  - AnthropicRequest + AnthropicResponse: für die externe Claude API

Format: Java Records oder Lombok @Data – einheitlich
```

---

### Schritt 4: LLM Service (40 Min)

```
Implementiere LlmService der Nachrichten an die Claude API sendet.

Interface LlmService + LlmServiceImpl:
  Methode: String sendMessage(String userMessage, List<ChatMessage> history)

Implementierung:
  1. history + neue user-Nachricht in Anthropic-Format bringen
     (jede Nachricht: { "role": "user"/"assistant", "content": "..." })
  2. POST an https://api.anthropic.com/v1/messages
     Headers: x-api-key, anthropic-version: 2023-06-01, Content-Type: application/json
     Body: { "model": "claude-3-5-sonnet-20241022", "max_tokens": 1024, "messages": [...] }
  3. content[0].text aus Antwort extrahieren und zurückgeben

Constraints:
  - API-Key aus @Value("${anthropic.api-key}")
  - Bei Fehler (401, 429, 500): RuntimeException mit Meldung
  - Kein Logging von Nachrichten-Inhalten
```

**API-Key in `src/main/resources/application.properties`:**
```properties
anthropic.api-key=${ANTHROPIC_API_KEY}
server.port=8080
```

Terminal:
```bash
export ANTHROPIC_API_KEY=sk-ant-...
```

---

### Schritt 5: Controller + CORS (25 Min)

```
Erstelle ChatController für POST /api/v1/chat.

Task:
  - Endpoint POST /api/v1/chat: ChatRequest → LlmService → ChatResponse
  - @Valid für Validierung
  - @ControllerAdvice: Validierungsfehler → 400, API-Fehler → 502
  - CORS: http://localhost:5173 erlauben

Constraints: Kein Logging von Nachrichten-Inhalten
```

**Backend testen:**
```bash
mvn spring-boot:run

curl -X POST http://localhost:8080/api/v1/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Sag Hallo auf Deutsch", "history": []}'
# Erwartet: { "reply": "Hallo! ...", "model": "claude-..." }
```

---

## Teil 2: Frontend (GitHub Copilot)

### Stack
```
Vue.js 3 (Composition API, <script setup lang="ts">)
TypeScript, Vite, Pinia, Axios
```

---

### Schritt 6: Frontend-Projekt anlegen + dev-setup-template (20 Min)

**Projektordner anlegen:**
```bash
mkdir chatbot-frontend
cd chatbot-frontend
```

**KI-Setup einrichten:**
```bash
bash ../ai-knowledgebase/dev-setup-template/setup.sh
```

**`copilot-instructions.md` für dieses Projekt anpassen** (`.github/copilot-instructions.md`):

Fügt oben einen Projekt-Abschnitt ein:
```markdown
## Dieses Projekt
Vue.js 3 Chat-Interface für einen KI-Chatbot.
Kommuniziert mit Spring Boot Backend auf http://localhost:8080.

## Chat API
POST http://localhost:8080/api/v1/chat
Request:  { "message": "...", "history": [{"role":"user"|"assistant","content":"..."},...] }
Response: { "reply": "...", "model": "..." }

## Design
- Chat: eigene Nachrichten rechts (blau #0084ff), Bot-Antworten links (grau #f0f0f0)
- Typing-Indikator ("..." animiert) während API-Call
- Eingabe unten: Textarea + Senden-Button, Enter = Senden
```
> Den Rest der `copilot-instructions.md` lasst ihr – kommt vom dev-setup-template.

**Vue.js Projekt erstellen (Copilot Agent Mode, Ctrl+Shift+I):**
```
Erstelle ein neues Vue.js 3 Projekt mit Vite.
Dependencies: Pinia, Axios.
Package-Name: chatbot-frontend
TypeScript, kein Router nötig.
Nur Grundgerüst – App.vue, main.ts. Keine Beispiel-Komponenten.
```

---

### Schritt 7: Chat Pinia Store (25 Min)

```
Erstelle stores/chat.ts – Pinia Store für den Chat.

State:
  - messages: {role: "user"|"assistant", content: string}[]
  - isLoading: boolean
  - error: string | null

Action sendMessage(userMessage: string):
  1. user-Nachricht zu messages hinzufügen
  2. isLoading = true, error = null
  3. POST http://localhost:8080/api/v1/chat:
     { message: userMessage, history: alle bisherigen messages }
  4. Bei Erfolg: reply zu messages hinzufügen (role: "assistant")
  5. Bei Fehler: error mit lesbarer Meldung setzen
  6. isLoading = false (immer, auch bei Fehler)

Action clearChat(): messages + error leeren
Getter hasMessages: boolean
```

---

### Schritt 8: Chat-Komponente + App.vue (30 Min)

```
Erstelle components/ChatWindow.vue.

Nachrichten-Liste (v-for über messages):
  - User: rechts, blauer Hintergrund, weißer Text
  - Bot: links, grauer Hintergrund, dunkler Text
  - isLoading: animierter "..." Typing-Indikator
  - error: rote Fehlermeldung

Eingabebereich unten:
  - Textarea (wächst mit Inhalt)
  - Senden-Button (disabled wenn isLoading oder leer)
  - Enter = Senden, Shift+Enter = neue Zeile
  - Auto-Scroll nach unten bei neuer Nachricht

Constraints: <script setup lang="ts">, useChat Store, scoped CSS
```

```
Passe App.vue an:
  - ChatWindow einbinden
  - Header "KI-Chatbot" + "Chat leeren"-Button
  - Layout: zentriert, max-width 800px
```

```bash
npm run dev
# → http://localhost:5173
```

---

## Verbindung testen (14:50 Uhr)

Backend läuft auf `:8080`, Frontend auf `:5173`.

**Test-Szenarien:**

```
1. "Erkläre mir kurz was Spring Boot ist"
   → KI antwortet

2. "Und was ist der Unterschied zu normalen Spring?"
   → KI antwortet MIT Kontext der vorherigen Frage (Konversation!)

3. Backend stoppen → Nachricht senden
   → Fehlermeldung erscheint, App friert nicht ein
```

---

## Für Tag 2 notieren

```
Backend-Endpoint: POST http://localhost:8080/api/v1/chat
Request: { "message": "...", "history": [...] }
Response: { "reply": "...", "model": "..." }
```

In Tag 2 wird das Backend erweitert: Der Chatbot bekommt Zugriff auf PostgreSQL
und kann Datenbankfragen mit echten Daten beantworten.

---

## Retrospektive (15:15 Uhr)

1. Welcher Prompt hat am besten funktioniert? (Zeigen!)
2. Was musste korrigiert werden? Warum hat KI das falsch gemacht?
3. Unterschied: Claude Code (Backend) vs. Copilot (Frontend) – was fiel wo auf?
4. Was würdet ihr in CLAUDE.md / copilot-instructions.md ergänzen?
