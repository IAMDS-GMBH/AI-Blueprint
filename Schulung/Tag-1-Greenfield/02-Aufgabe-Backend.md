# Tag 1 – Aufgabe Team A: KI-Chatbot Backend mit LLM API

**Tool:** Claude Code (Terminal)
**Ziel:** Einen Spring Boot Backend-Service bauen der Nachrichten an eine LLM API (Claude / OpenAI) weiterleitet und als Chatbot-Proxy dient
**Zeit:** 11:00–15:15 Uhr (3,5 Stunden – startet vor Mittag, weiter nach Mittag)

---

## Das Gesamtbild (beide Teams zusammen)

Am Ende des Tages entsteht ein vollständiger KI-Chatbot:

```
Team B: Vue.js Chat-UI
        ↓ POST /api/v1/chat
Team A: Spring Boot Backend
        ↓ Anthropic / OpenAI API
        LLM (Claude / GPT)
```

Team A baut heute die "Mitte" – das Backend das Nachrichten entgegennimmt,
an ein LLM weiterleitet und die Antwort zurückgibt.
Team B baut heute die Oberfläche (separate Aufgabe).

---

## KI-Tool-Ablauf: Wann greift welches Tool?

```
Schritt 1  Setup         → Claude Code (Direkter Prompt)  – Projekt-Skelett generieren
Schritt 2  Plan          → Claude Code + /plan Command    – Architektur planen, todo.md erstellen
Schritt 3  API-Modell    → Claude Code (Direkter Prompt)  – Request/Response DTOs
Schritt 4  LLM-Service   → Claude Code (Direkter Prompt)  – HTTP-Client zur LLM API
Schritt 5  Controller    → Claude Code (Direkter Prompt)  – REST Endpoint + CORS
Schritt 6  Config        → Claude Code (Direkter Prompt)  – API Key sicher speichern
Schritt 7  Tests         → Claude Code (Direkter Prompt)  – Mocked LLM API Tests
Ende       Review        → /review Command                – Sub-Agent prüft den Code
```

---

## Stack

```
Java 17 + Spring Boot 3.2
Maven
Spring Web (REST)
Spring WebFlux ODER RestTemplate / RestClient (HTTP-Client)
Lombok
```

**LLM API (eines davon):**
- **Claude (Anthropic):** `https://api.anthropic.com/v1/messages`
- **OpenAI:** `https://api.openai.com/v1/chat/completions`
- **Lokal via Ollama** (falls kein API-Key vorhanden): `http://localhost:11434/v1/chat/completions`

---

## Schritt 1: Projekt-Setup (20 Min)

### 1a – Spring Initializr via KI

Startet Claude Code im Terminal und gebt folgenden Prompt ein:

```
Erstelle ein neues Spring Boot 3.2 Projekt mit Maven.
Dependencies: Spring Web, Lombok, Validation, Spring Boot DevTools.
Package: com.schulung.chatbot
Noch keinen Feature-Code – nur das Projekt-Skelett mit pom.xml und ChatbotApplication.java.
```

### 1b – CLAUDE.md erstellen

Erstellt im Projektordner eine `CLAUDE.md`:

```markdown
# Chatbot Backend API

## Zweck
Spring Boot Backend das Nachrichten an eine LLM API (Claude/OpenAI) weiterleitet.
Wird von einem Vue.js Frontend (Team B, Port 5173) konsumiert.

## Stack
Java 17, Spring Boot 3.2, Maven, Lombok

## LLM API
Provider: Anthropic Claude API
Basis-URL: https://api.anthropic.com/v1/messages
API-Key: aus application.properties (niemals hardcodiert!)

## API-Endpoint
POST /api/v1/chat
Request:  { "message": "...", "history": [...] }
Response: { "reply": "...", "model": "..." }

## Konventionen
- DTOs für alle Request/Response-Objekte
- API-Key aus @Value, niemals hardcodiert
- CORS: localhost:5173 erlaubt (Team B Frontend)
- Keine Spring Security nötig (Schulungsumgebung)
- Methoden max. 30 Zeilen
```

---

## Schritt 2: Architektur planen (20 Min)

Nutzt `/plan` oder folgenden Prompt:

```
Erstelle zuerst nur einen Plan (noch keinen Code) für folgende Aufgabe:

Chatbot Backend mit einem Endpoint:
  POST /api/v1/chat
  Request:  { "message": "Hallo", "history": [{"role":"user","content":"..."},{"role":"assistant","content":"..."}] }
  Response: { "reply": "Antwort des LLM", "model": "claude-3-5-sonnet-20241022" }

Der Endpoint leitet die Nachricht + History an die Anthropic Claude API weiter.

Zeige welche Klassen erstellt werden (Controller, Service, DTOs, Config).
Welche Klasse macht was?
```

Reviewt den Plan. Dann: *"Führe jetzt den Plan Schritt für Schritt aus."*

---

## Schritt 3: Request/Response Modelle (20 Min)

```
Erstelle die DTOs für den Chat-Endpoint.

Context: Spring Boot 3.2, Lombok, Jackson
Task:
  - ChatRequest: Felder message (String, @NotBlank) + history (List<ChatMessage>)
  - ChatMessage: Felder role (String: "user" oder "assistant") + content (String)
  - ChatResponse: Felder reply (String) + model (String)
  - AnthropicRequest: Felder model, max_tokens, messages (für Claude API)
  - AnthropicResponse: Für die Claude API-Antwort (content[0].text parsen)
Format: Java Records oder Lombok @Data – konsistent bleiben
```

**Prüft:**
- Hat `ChatRequest` eine `@NotBlank`-Validierung auf `message`?
- Gibt es DTOs sowohl für euren Endpoint ALS AUCH für die externe Claude API?

---

## Schritt 4: LLM Service (50 Min)

Der Kern – hier ruft ihr die externe LLM API auf:

```
Implementiere den LlmService der Nachrichten an die Claude API sendet.

Context: Spring Boot 3.2, RestClient (oder RestTemplate), Jackson
Task: LlmService mit Methode:
  String sendMessage(String userMessage, List<ChatMessage> history)

Implementierung:
  1. ChatMessage-History in Anthropic-Format konvertieren
     (jede Nachricht: { "role": "user"/"assistant", "content": "..." })
  2. Aktuelle user-Nachricht hinzufügen
  3. POST an https://api.anthropic.com/v1/messages mit Headers:
     - x-api-key: ${anthropic.api-key}
     - anthropic-version: 2023-06-01
     - Content-Type: application/json
  4. Body: { "model": "claude-3-5-sonnet-20241022", "max_tokens": 1024, "messages": [...] }
  5. Antwort: content[0].text extrahieren und zurückgeben

Constraints:
  - API-Key aus @Value("${anthropic.api-key}"), niemals hardcodiert
  - Bei HTTP-Fehler (401, 429, 500): RuntimeException mit sinnvoller Meldung
  - Kein Logging von Message-Inhalten (Datenschutz)
Format: Interface LlmService + LlmServiceImpl
```

**⚠️ Wichtig: API-Key in `application.properties`:**
```properties
anthropic.api-key=${ANTHROPIC_API_KEY}
```

Dann als Umgebungsvariable setzen (nicht committen!):
```bash
export ANTHROPIC_API_KEY=sk-ant-...
```

---

## Schritt 5: Controller + CORS (30 Min)

```
Erstelle den ChatController für POST /api/v1/chat.

Context: LlmService vorhanden, ChatRequest/ChatResponse DTOs vorhanden
Task:
  - POST /api/v1/chat: ChatRequest entgegennehmen, LlmService aufrufen, ChatResponse zurückgeben
  - @Valid für Request-Validierung
  - @ControllerAdvice für Fehlerbehandlung:
    - Validierungsfehler → 400 Bad Request
    - API-Fehler (z.B. ungültiger Key) → 502 Bad Gateway mit Fehlermeldung
  - CORS: localhost:5173 (Team B) und localhost:3000 erlauben

Constraints:
  - Kein Logging von Nachrichteninhalten
  - HTTP 200 bei Erfolg
```

---

## Schritt 6: Konfiguration + Sicherheit (20 Min)

```
Erstelle die application.properties und eine WebMvcConfig für CORS.

Inhalte:
  - server.port=8080
  - anthropic.api-key=${ANTHROPIC_API_KEY} (aus Umgebungsvariable)
  - spring.application.name=chatbot-backend
  - Logging-Level: INFO für com.schulung.chatbot

Zusätzlich: WebMvcConfigurer Bean der CORS für http://localhost:5173 erlaubt.
```

---

## Schritt 7: Tests (30 Min)

```
Schreibe JUnit 5 Tests für den ChatController.

Context: ChatController mit LlmService-Dependency
Task:
  - Test: Gültige Anfrage → 200 + ChatResponse zurück
  - Test: Leere message → 400 Bad Request
  - Test: LlmService wirft Exception → 502 zurückgegeben

Constraints:
  - LlmService mocken (@MockBean) – kein echter API-Call in Tests
  - MockMvc für Controller-Tests
  - Kein Spring Context laden wenn nicht nötig (@WebMvcTest)
```

---

## Verifikation: So prüft ihr ob alles funktioniert

```bash
# Backend starten
mvn spring-boot:run

# Einfacher Chat-Test
curl -X POST http://localhost:8080/api/v1/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Sag mir Hallo auf Deutsch",
    "history": []
  }'
# Erwartete Antwort: { "reply": "Hallo! ...", "model": "claude-..." }

# Test mit History (Konversation)
curl -X POST http://localhost:8080/api/v1/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Wie heißt du?",
    "history": [
      {"role": "user", "content": "Sag mir Hallo"},
      {"role": "assistant", "content": "Hallo! Wie kann ich helfen?"}
    ]
  }'

# Fehlerfall testen (leere Nachricht)
curl -X POST http://localhost:8080/api/v1/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "", "history": []}'
# Erwartete Antwort: 400 Bad Request
```

---

## Vorbereitung für Tag 2 (WICHTIG)

Am Ende von Tag 1: Notiert euch für Tag 2

```
POST http://localhost:8080/api/v1/chat
Request-Format: { "message": "...", "history": [...] }
Response-Format: { "reply": "...", "model": "..." }
```

In Tag 2 wird dieses Backend erweitert, sodass der Chatbot Oracle-Datenbankfragen beantworten kann.

---

## Verbindung mit Team B herstellen (15:00 Uhr)

Team B hat ihre Vue.js App unter `http://localhost:5173`.
Wenn Team A fertig ist → kurzer Test ob Nachrichten aus dem Frontend ankommen.

Falls CORS-Probleme:
```
Prüfe und korrigiere die CORS-Konfiguration in WebMvcConfig.
Team B's Frontend läuft auf http://localhost:5173.
Aktuell bekomme ich: [Fehlermeldung aus Browser-Console einfügen]
```

---

## Retrospektive (15:15 Uhr)

Bereitet euch auf diese Fragen vor:
1. Welcher Prompt hat am besten funktioniert? (Zeigen!)
2. Was hat KI falsch gemacht? Was musstet ihr korrigieren?
3. Wie viele Zeilen Code habt ihr selbst geschrieben vs. KI-generiert?
4. Was würdet ihr beim nächsten Mal in der `CLAUDE.md` anders definieren?
