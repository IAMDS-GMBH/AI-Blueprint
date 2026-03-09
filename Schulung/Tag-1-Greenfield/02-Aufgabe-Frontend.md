# Tag 1 – Aufgabe Team B: KI-Chatbot Frontend (Vue.js)

**Tool:** GitHub Copilot (VS Code – Agent Mode)
**Ziel:** Eine Vue.js 3 Chat-Oberfläche bauen die mit dem Chatbot-Backend von Team A kommuniziert
**Zeit:** 11:00–15:15 Uhr (3,5 Stunden – startet vor Mittag, weiter nach Mittag)

---

## Das Gesamtbild (beide Teams zusammen)

```
Team B: Vue.js Chat-UI  ← ihr baut das heute
        ↓ POST /api/v1/chat
Team A: Spring Boot Backend  ← sendet Nachrichten an Claude API
        ↓
        Claude / OpenAI / Ollama
```

Team B baut heute die Chat-Oberfläche. Am Ende des Tages verbindet sich beides
zu einem funktionierenden KI-Chatbot.

---

## KI-Tool-Ablauf: Wann greift welches Tool?

```
Schritt 1  Setup         → Copilot Agent Mode (Chat Panel)   – Projekt-Skelett generieren
Schritt 2  Plan          → Copilot Agent Mode                – Dateistruktur planen
Schritt 3  Chat Store    → Copilot Agent Mode                – Pinia Store für Chat-State
Schritt 4  API-Service   → Copilot Inline oder Agent Mode    – Axios HTTP-Call
Schritt 5  Chat-UI       → Copilot Agent Mode                – Haupt-Chat-Komponente
Schritt 6  Styles        → Copilot Inline                    – CSS für Chat-Layout
Ende       Review        → @ReviewAgent pruefe...            – Code Review
```

| Copilot-Modus | Wann einsetzen |
|---------------|---------------|
| **Inline Completion** (automatisch) | Einzelne Zeilen, CSS-Properties, Import-Statements |
| **Inline Chat** (Ctrl+I) | Bestehende Komponenten anpassen, Fehler erklären |
| **Chat Panel** (Ctrl+Shift+I) | Neue Komponenten/Views erstellen, Planung |
| **@ReviewAgent** | Code vor der Präsentation prüfen |

---

## Stack

```
Vue.js 3 (Composition API, <script setup>)
TypeScript
Vite
Pinia (Chat-State verwalten)
Axios (HTTP-Calls zum Backend)
```

---

## Schritt 1: Projekt-Setup (20 Min)

### 1a – Projekt erstellen

Öffnet VS Code Terminal und gebt Copilot (Agent Mode) folgenden Prompt:

```
Erstelle ein neues Vue.js 3 Projekt mit Vite.
Dependencies: Pinia, Axios.
Package-Name: chatbot-frontend
TypeScript verwenden.
Nur das Grundgerüst – App.vue, main.ts, keine Beispiel-Components.
```

### 1b – `.github/copilot-instructions.md` erstellen

```markdown
# Chatbot Frontend

## Zweck
Vue.js 3 Chat-Interface für einen KI-Chatbot.
Kommuniziert mit Spring Boot Backend (Team A) auf http://localhost:8080.

## Stack
Vue.js 3 (Composition API, <script setup>), TypeScript, Vite, Pinia, Axios

## API
POST http://localhost:8080/api/v1/chat
Request:  { "message": "...", "history": [{"role":"user","content":"..."},...] }
Response: { "reply": "...", "model": "..." }

## Konventionen
- Nur Composition API mit <script setup lang="ts">
- Props und Emits immer typisiert
- Kein direkter API-Call in Komponenten – immer via Pinia Store
- CSS: Scoped styles

## Design
- Minimalistisches Chat-Interface (kein UI-Framework, Plain CSS)
- Eigene Nachrichten: rechts ausgerichtet, blauer Hintergrund
- Bot-Antworten: links ausgerichtet, grauer Hintergrund
- Während API-Call: Typing-Indikator ("..." animiert)
```

---

## Schritt 2: Planen (15 Min)

```
Erstelle einen Plan (noch keinen Code) für ein Chat-Frontend.

Features:
- Chat-Fenster mit Nachrichtenverlauf (User + KI abwechselnd)
- Texteingabe unten + Senden-Button (oder Enter)
- Typing-Indikator während die KI antwortet
- Fehlermeldung wenn Backend nicht erreichbar

State (Pinia):
- messages: Array aus {role: "user"|"assistant", content: string}
- isLoading: boolean (während API-Call)
- error: string | null

Zeige welche Dateien erstellt werden.
```

---

## Schritt 3: Chat Pinia Store (30 Min)

```
Erstelle den Chat Pinia Store in stores/chat.ts.

Context: Vue.js 3, Pinia, Axios, Backend auf http://localhost:8080
Task: stores/chat.ts mit:
  - State:
    - messages: Array<{role: "user"|"assistant", content: string}>
    - isLoading: boolean
    - error: string | null
  - Action sendMessage(userMessage: string):
    1. User-Nachricht zu messages hinzufügen (role: "user")
    2. isLoading = true, error = null
    3. POST http://localhost:8080/api/v1/chat senden:
       { message: userMessage, history: messages (alle bisherigen) }
    4. Bei Erfolg: reply zu messages hinzufügen (role: "assistant")
    5. Bei Fehler: error setzen mit lesbarer Fehlermeldung
    6. isLoading = false
  - Action clearChat(): messages + error leeren
  - Getter hasMessages: boolean
Constraints:
  - Axios für HTTP (kein fetch)
  - Fehlerbehandlung: Network-Error vs. API-Error unterscheiden
```

**Prüft:**
- Wird die history korrekt mitgesendet (alle bisherigen Nachrichten)?
- Wird `isLoading` sicher auf `false` gesetzt (auch bei Fehler)?

---

## Schritt 4: Axios API-Service (15 Min)

```
Erstelle src/api/chat.ts – eine Axios-Instanz für den Chat-Backend.

Context: Backend auf http://localhost:8080
Task:
  - Axios-Instanz mit baseURL: http://localhost:8080
  - Timeout: 30 Sekunden (LLM kann langsam sein)
  - Response-Interceptor: Bei Netzwerk-Fehler → lesbarer Fehlertext
Format: Exportiere die Axios-Instanz + eine sendChat-Funktion als Wrapper
```

---

## Schritt 5: Chat-Komponente (60 Min)

```
Erstelle die Haupt-Chat-Komponente als components/ChatWindow.vue.

Context: chat Store vorhanden (messages, isLoading, error, sendMessage)
Task: Vollständige Chat-Komponente mit:
  - Nachrichten-Liste (v-for über messages):
    - User-Nachrichten: rechts, blauer Hintergrund (#0084ff), weißer Text
    - Bot-Nachrichten: links, grauer Hintergrund (#f0f0f0), dunkler Text
    - Absender-Label (optional: "Du" / "KI")
  - Typing-Indikator: Wenn isLoading=true → animierter "..." anzeigen
  - Fehlermeldung: Wenn error → rote Box mit Fehlertext
  - Eingabebereich unten:
    - Textarea (wächst mit Inhalt bis max 5 Zeilen)
    - Senden-Button (deaktiviert wenn isLoading oder leere Eingabe)
    - Enter = Senden, Shift+Enter = neue Zeile
  - Beim Laden einer neuen Nachricht: Auto-Scroll nach unten
Constraints:
  - <script setup lang="ts">
  - useChat Store nutzen
  - CSS: scoped, kein Framework
  - Responsive (funktioniert auf 1200px wie auf 800px)
```

---

## Schritt 6: App.vue zusammenbauen (20 Min)

```
Passe App.vue an.

Task:
  - ChatWindow Komponente einbinden
  - Einfacher Header mit Titel "KI-Chatbot"
  - Button "Chat leeren" der store.clearChat() aufruft
  - App-Layout: zentriert, max-width 800px
Constraints: Scoped CSS, kein Framework
```

**Vite Dev Server starten:**
```bash
npm run dev
# → http://localhost:5173
```

---

## Schritt 7: Testing & Feinschliff (20 Min)

Testet euren Chat mit folgenden Szenarien:

**Szenario 1: Normaler Chat**
```
User: "Erkläre mir kurz was Spring Boot ist"
→ KI antwortet
User: "Und was ist der Unterschied zu normalen Spring?"
→ KI antwortet mit Kontext der vorherigen Frage (Konversation funktioniert!)
```

**Szenario 2: Fehlerfall**
```
# Backend stoppen → Nachrichten senden
→ Fehlermeldung muss erscheinen, App darf nicht einfrieren
```

Wenn etwas nicht stimmt – Fehler an KI weitergeben:
```
Die Chat-Komponente hat folgendes Problem: [Beschreibung / Fehlermeldung]
Zeige mir den Fix.
```

---

## Verbindung mit Team A herstellen (15:00 Uhr)

Team A hat ihr Backend unter `http://localhost:8080`.

Falls Team A noch nicht fertig → Mock nutzen:
```
Erstelle eine einfache Mock-Funktion in stores/chat.ts die
sendMessage() simuliert ohne echten API-Call.
Nach 1,5 Sekunden soll folgende Antwort zurückkommen:
"[Mock-Antwort] Du hast gefragt: {userMessage}"
```

---

## Retrospektive (15:15 Uhr)

Bereitet euch auf diese Fragen vor:
1. Welcher Copilot-Prompt hat am besten funktioniert? (Zeigen!)
2. Wann war Inline Completion hilfreich, wann Agent Mode?
3. Was hat Copilot bei Vue.js falsch gemacht? (Options API? Kein TypeScript?)
4. Was würdet ihr in der `copilot-instructions.md` ergänzen?
