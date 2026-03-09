# Tag 1 – Theorie: Greenfield-Entwicklung mit KI

## Lernziele
Nach diesem Block kannst du:
- Erklären wie ein LLM funktioniert und warum Halluzination passiert
- KI als vollwertigen Entwicklungspartner für neue Projekte einsetzen
- Prompts strukturiert formulieren (KERNEL-Framework)
- Ein komplettes Feature von 0 mit KI planen und umsetzen
- Zwischen Claude Code und GitHub Copilot situativ wählen
- KI-Outputs systematisch verifizieren und Fehler gezielt korrigieren
- Datenschutz und Kosten beider Tools einschätzen

---

## 0. Was ist ein LLM – das mentale Modell (10 Min)

Bevor wir anfangen: Was macht KI eigentlich unter der Haube? Ihr müsst kein Mathematiker sein – aber dieses mentale Modell hilft zu verstehen warum Prompts so wichtig sind.

**LLM = Large Language Model**

```
Vereinfacht gesagt:
Ein LLM hat extrem viel Text gelesen (das halbe Internet, Bücher, Code)
und gelernt: "Nach diesem Text kommt mit hoher Wahrscheinlichkeit dieser Text."

Wenn ihr einen Prompt schreibt → KI sagt voraus was als nächstes sinnvoll ist.
```

**Was das bedeutet in der Praxis:**

| Eigenschaft | Was das heißt für euch |
|---|---|
| KI ist kein Datenbank-Lookup | Sie erfindet Antworten – auch wenn sie falsch sind ("Halluzination") |
| KI hat ein Kontextfenster | Sie "sieht" nur was im aktuellen Chat steht, nicht euer ganzes Repo |
| Prompt-Qualität = Output-Qualität | Je präziser die Eingabe, desto besser das Ergebnis |
| KI kennt keine "Wahrheit" | Sie antwortet überzeugend auch wenn sie falsch liegt |

**Warum halluziniert KI?**
```
KI sagt nicht "ich weiß es nicht" – sie sagt die wahrscheinlichste Antwort.
Wenn sie keine gute Antwort kennt: erfindet sie eine die plausibel klingt.

→ Deshalb: Nie blind vertrauen. Immer verifizieren.
```

**Die wichtigste Konsequenz:**
KI ist kein Orakel das ihr glauben müsst – sie ist ein sehr schneller Erstentwurf-Generator den ihr prüfen und korrigieren müsst.

---

## 1. Der Mindset-Shift

**Früher:**
```
Entwickler = jemand der Code schreibt
Kernaufgabe: Programmiersprache beherrschen
Output: Lines of Code
```

**Heute:**
```
Entwickler = jemand der Probleme löst
Kernaufgabe: Problem präzise definieren + Architektur + Verifikation
Output: Funktionierendes System
```

> "The use of Software Engineers will be to solve problems.
> Coding was one task of that – and it will get replaced."

**Was das bedeutet:**
- KI schreibt den Code → du definierst das Problem, prüfst das Ergebnis
- Architektur-Denken wird wichtiger, nicht unwichtiger
- Prompt-Qualität = Code-Qualität

---

## 2. Vibe Coding – Was ist das?

**Vibe Coding** = ein komplettes Feature oder Projekt von Grund auf mit KI entwickeln,
ohne vorher eine einzige Zeile manuell zu schreiben.

**Ablauf:**
```
1. Idee beschreiben (natürliche Sprache)
2. KI plant Architektur
3. KI implementiert
4. Du verifizierst + korrigierst
5. KI iteriert
```

**Wichtig:** Vibe Coding bedeutet nicht "KI macht alles, du lehnst dich zurück".
Es bedeutet: Du bist Architekt und Verifikator, KI ist Umsetzer.

### Vibe Coding in der Praxis – Feature-Liste übergeben

Der nächste Level: Nicht ein Feature auf einmal, sondern **eine ganze Liste** abarbeiten lassen.

```
Schritt 1: Requirements klar definieren (je konkreter, desto autonomer)
Schritt 2: tasks/prd.md anlegen mit priorisierten Features
Schritt 3: "Lies prd.md. Baue alle Features nach Priorität. Halte todo.md aktuell."
Schritt 4: KI arbeitet autonom durch
Schritt 5: /ralph + /review am Ende
```

**Was den Unterschied macht:**
```
Vage:    "Baue eine Login-Funktion"
         → KI fragt 5x nach, stoppt, wartet

Konkret: POST /api/v1/auth/login
         Body: { email, password }
         Response: { token, expiresIn: 3600 }
         Fehler: 401 bei falschen Credentials
         → KI baut durch ohne eine Rückfrage
```

---

## 3. Das KERNEL-Framework

Das KERNEL-Framework macht Prompts systematisch besser.

### K – Keep it simple
Ein Prompt = ein Ziel. Nicht mehrere Sachen auf einmal fragen.

```
Schlecht: "Erstelle eine User-API mit Login, Registrierung, Passwort-Reset und Tests"
Gut:      "Erstelle einen POST /api/users Endpoint für User-Registrierung"
```

### E – Explicit constraints
Sag der KI was sie NICHT tun soll.

```
"Java Spring Boot. Keine externe Auth-Library. Keine Methoden über 30 Zeilen."
```

### R – Reproducible results
Keine vagen Referenzen wie "aktuelle Best Practices".
Konkrete Versionen und Anforderungen nennen.

```
"Java 17, Spring Boot 3.2, JPA/Hibernate, PostgreSQL 15"
```

### N – Narrow scope
Ein Prompt, ein Ziel.

```
Schlecht: Code + Tests + Dokumentation in einem Prompt
Gut:      Erst Code, dann separater Prompt für Tests
```

### E – Easy to verify
Klare Erfolgskriterien definieren.

```
"Schreib einen Test der prüft: POST /api/users mit gültigem Body → 201 Created"
```

### L – Logical structure
Jeder Prompt folgt diesem Schema:
```
Context:     [Was gibt es bereits / was ist der Stack?]
Task:        [Was soll gemacht werden?]
Constraints: [Was darf NICHT passieren?]
Format:      [Wie soll das Ergebnis aussehen?]
```

### KERNEL in der Praxis

**Vorher (vage):**
> "Hilf mir eine Spring-Applikation zu bauen"
→ Ergebnis: Generisches Hello-World, nicht nutzbar

**Nachher (KERNEL):**
```
Context:     Java Spring Boot 3.2 Projekt, PostgreSQL, Maven
Task:        POST /api/v1/users – User registrieren (Name, E-Mail, Passwort)
Constraints: Service-Interface Pattern, DTO für Input/Output, Passwort mit BCrypt hashen
Format:      Nur die nötigen Klassen, kein Boilerplate
```
→ Ergebnis: Direkt nutzbarer Code beim ersten Versuch

### Erweiterte Prompting-Techniken (Anthropic Best Practices)

**Kontext und Motivation erklären:**
```
Ohne: "Schreib Tests für UserService"
Mit:  "Wir hatten einen Produktionsbug: findById gab null zurück statt eine Exception.
       Schreib Tests die das absichern – Fokus auf null-handling und nicht-existente IDs."
```
→ Warum erklären schlägt Was beschreiben.

**Beispiele geben (Few-Shot):**
```
Formatiere alle Service-Methoden so:

<example>
Input:  getUserById(Long id)
Output: Gibt Optional<UserDTO> zurück, wirft ResourceNotFoundException wenn nicht gefunden
</example>
```
→ Zeigen ist schneller als erklären. 3–5 Beispiele im Prompt verdoppeln die Treffsicherheit.

**XML-Tags für komplexe Prompts:**
```xml
<context>
  Java Spring Boot 3.2, PostgreSQL
  Bestehender Code: [CODE HIER]
</context>

<task>Füge findByEmail() hinzu</task>

<constraints>Optional<User> Rückgabetyp, keine neuen Dependencies</constraints>
```

**Lange Dokumente: Kontext zuerst, Frage am Ende:**
```
[Log-File / Code / Dokumentation einfügen]

→ Erst jetzt die Frage stellen: "Was ist die Ursache dieses Fehlers?"
```

**Schrittweise denken lassen:**
```
"Analysiere erst Schritt für Schritt was dieser Code macht.
 Dann identifiziere potenzielle Bugs.
 Erst danach schlage Fixes vor."
```

---

## 4. Tool-Überblick: Claude Code vs. GitHub Copilot

| Merkmal | Claude Code | GitHub Copilot |
|---------|-------------|----------------|
| **Stärke** | Autonome Agents, Plan Mode, Swarms | Inline-Autocomplete, IDE-Integration |
| **Wann nutzen** | Große Features, Refactoring, Autonome Tasks | Tägliches Coding, schnelle Vervollständigung |
| **Modell-Wahl** | Nur Claude | Claude, GPT-4o, Gemini wählbar |
| **CLI-Native** | Ja (Terminal) | Nein (nur in IDE) |
| **Kosten** | Usage-based (API) | Flat Rate (Subscription) |

**Empfehlung für heute:**
- Team A nutzt **Claude Code** (Terminal)
- Team B nutzt **GitHub Copilot** (VS Code Agent Mode)
- Abend-Vergleich: Wer hatte welche Erfahrung?

**Was kostet das konkret?**

| Tool | Modell | Kosten |
|------|--------|--------|
| Claude Code | Pay-per-use (API-Token) | ~$5–20 / Entwickler / Monat bei normalem Einsatz |
| Claude Code | Claude Max Plan | $100/Monat flat – unlimitiert |
| GitHub Copilot | Individual | $10/Monat |
| GitHub Copilot | Business | $19/Monat pro Nutzer |
| GitHub Copilot | Enterprise | $39/Monat pro Nutzer (mit Unternehmens-Policies) |

**Claude Code – Setup in 3 Schritten:**
```bash
# 1. Installieren (Node.js 20+ vorausgesetzt)
npm install -g @anthropic-ai/claude-code

# 2. API Key setzen (einmalig)
export ANTHROPIC_API_KEY=sk-ant-...
# Dauerhaft: in ~/.zshrc oder ~/.bashrc eintragen

# 3. Starten
claude
```

API Keys bekommt ihr unter: console.anthropic.com → API Keys

**Datenschutz – was wird wohin geschickt?**

Das ist die Frage die immer kommt – und die richtige Antwort ist: **Es kommt drauf an.**

| Situation | Was passiert mit euren Daten |
|---|---|
| **Claude Code (API)** | Prompts gehen an Anthropic-Server. Anthropic nutzt sie standardmäßig **nicht** zum Training (Enterprise/API-Nutzung). |
| **GitHub Copilot Individual** | Code-Snippets können für Training genutzt werden (opt-out möglich) |
| **GitHub Copilot Business/Enterprise** | Code wird **nicht** für Training genutzt – vertraglich zugesichert |
| **Lokale Modelle (Ollama)** | Nichts verlässt den Rechner |

**Faustregel für Unternehmens-Code:**
```
Kein Kundencode, keine Passwörter, keine internen IPs in den Prompt.
Immer nur den Code-Kontext schicken der für die Aufgabe nötig ist.
Für maximale Kontrolle: GitHub Copilot Business oder lokale Modelle.
```

---

### 4b. Lokale KI mit Ollama – Claude Code + Copilot ohne Cloud

**Wann interessant:**
- Kein API-Key vorhanden (Schulung, neuer Entwickler)
- Datenschutz: Kein Code verlässt das Netzwerk
- Offline-Entwicklung oder Kostenreduktion

**Ollama** ist eine lokale LLM-Laufzeit – ihr ladet ein Open-Source-Modell herunter
und nutzt es wie eine Cloud-API, aber komplett auf eurer Hardware.

```bash
# Ollama installieren (macOS)
brew install ollama

# Modell laden (einmalig, ~4 GB)
ollama pull qwen2.5-coder:7b   # Empfehlung für Code

# Ollama starten
ollama serve
```

**Claude Code mit lokalem Ollama-Modell verbinden:**

```bash
# Umgebungsvariablen setzen → Claude Code nutzt jetzt Ollama
export ANTHROPIC_BASE_URL=http://localhost:11434/v1
export ANTHROPIC_API_KEY=ollama    # Beliebiger Wert

claude                             # → nutzt jetzt lokales Modell
```

**Zurück zur Cloud:**
```bash
unset ANTHROPIC_BASE_URL
export ANTHROPIC_API_KEY=sk-ant-...
claude
```

**Empfohlene Modelle für Entwickler:**

| Modell | Größe | Stärke |
|--------|-------|--------|
| `qwen2.5-coder:7b` | 4 GB | Code (Java, Vue, SQL) – empfohlen |
| `llama3.2:3b` | 2 GB | Allrounder, sehr schnell |
| `llama3.1:8b` | 5 GB | Besser bei komplexen Aufgaben |
| `codellama:7b` | 4 GB | Meta's Code-Modell |

**Was funktioniert lokal gut / was nicht:**

| Feature | Lokal (Ollama) |
|---------|---------------|
| Code generieren | ✅ Gut |
| KERNEL-Prompts | ✅ Wie gewohnt |
| /plan | ✅ Meist gut |
| /review + /ralph | ⚠️ Qualitätsabhängig |
| MCP Tool Use | ⚠️ Nur mit Function-Calling-Modellen |

**Für Copilot:** Lokale Modelle via VS Code Extension "Continue" + Ollama-Provider möglich.
Für reine Offline-Entwicklung ist Claude Code + Ollama die einfachere Option.

> Vollständige Anleitung → `KI-Wissensbasis/11-Lokale-KI.md`

---

## 5. CLAUDE.md – KI Projektkontext geben

Die wichtigste Datei im Projekt. Wird bei jeder Session automatisch geladen.

```markdown
# Projekt: [Name]
## Stack: Java 17, Spring Boot 3.2, Vue.js 3, PostgreSQL
## Konventionen: Services via Interface, DTOs, max. 30 Zeilen/Methode
## Was ich NICHT will: Over-Engineering, neue Dependencies ohne Absprache
```

**Live-Demo:**
1. Ohne CLAUDE.md → KI macht Annahmen
2. Mit CLAUDE.md → KI kennt den Kontext

---

## 5b. Rules & Plugins – die Kontext-Ebenen die automatisch arbeiten

### Rules – dateispezifische Standards ohne Copy-Paste

Rules sind Markdown-Dateien in `.claude/rules/` die **automatisch aktiv werden** wenn Claude bestimmte Dateitypen bearbeitet. Kein manueller Aufruf nötig.

```markdown
---
description: Java Spring Boot Regeln
globs: ["**/*.java"]
alwaysApply: false
---

# Java Pflichtregeln
- Services immer via Interface (UserService → UserServiceImpl)
- Keine Entities direkt in API-Response exposen
- Alle Endpoints mit @PreAuthorize absichern
```

**Was passiert:** Claude bearbeitet eine `.java`-Datei → Rule wird geladen → Regeln gelten automatisch

**Unser Setup hat 3 Rules:**
```
.claude/rules/java-spring.md    → aktiv bei *.java
.claude/rules/vue-frontend.md   → aktiv bei *.vue, *.ts
.claude/rules/db-migrations.md  → aktiv bei *.sql
```

**Wichtig:** Der korrekte Frontmatter-Key heißt `globs:` (nicht `paths:` – das wird ignoriert!)

---

### Plugin Marketplace – fertige KI-Erweiterungen projekt-lokal

Claude Code hat einen Plugin-Marketplace. Plugins erweitern das **Verhalten** von Claude Code selbst – sie müssen nicht global installiert werden.

```bash
# Im Projektverzeichnis installieren
/plugin install ralph-loop@claude-plugins-official
/plugin install security-guidance@claude-plugins-official

# → .claude/settings.json wird automatisch erweitert
```

**Ergebnis in settings.json:**
```json
{
  "enabledPlugins": {
    "ralph-loop@claude-plugins-official": true,
    "security-guidance@claude-plugins-official": true
  }
}
```

**Wichtige Plugins für uns:**
| Plugin | Effekt |
|--------|--------|
| `ralph-loop` | KI prüft ihren Output selbst bevor sie antwortet |
| `context7` | Lädt aktuelle Library-Docs – kein Halluzinieren bei Versionen |
| `security-guidance` | Warnt automatisch bei Security-Problemen im Code |
| `typescript-lsp` / `jdtls-lsp` | Language Server Integration für TS/Java |

**Plugin vs. MCP-Server:**
```
Plugin:     Verhaltens-Erweiterung von Claude Code selbst (nur Claude Code)
MCP-Server: Zugang zu externen Tools (Browser, DB, Figma) – auch für Copilot
```

---

## 6. MEMORY.md – Projektgedächtnis über Sessions hinaus

**Problem:** Claude Code vergisst nach jeder Session alles – Stack, Konventionen, gemachte Fehler.

**Lösung: MEMORY.md** – wird bei jedem neuen Chat automatisch geladen.

```
Ablageort (außerhalb des Repos, lokal auf dem Rechner):
~/.claude/projects/[projekt-pfad]/memory/MEMORY.md
```

**Was hineingehört:**
```markdown
## Projekt
Zweck, Stack, wichtige Dateipfade

## Konventionen
Was immer gilt (z.B. BigDecimal statt double)

## Präferenzen
Was nicht gewünscht ist (z.B. kein Over-Engineering)
```

**Wann wird MEMORY.md geladen?**
Nicht on-demand – sondern **automatisch beim Start jeder neuen Session**, bevor ihr die erste Nachricht schreibt. Die KI kennt das Projekt also sofort.

**Unterschied zu CLAUDE.md:**

| | CLAUDE.md | MEMORY.md |
|--|-----------|-----------|
| Zweck | Anweisungen und Standards | Projekt-Wissen und Kontext |
| Wer pflegt sie | Ihr (manuell) | KI schreibt sie selbst |
| Im Repo | Ja | Nein (lokal auf dem Rechner) |
| Für welches Tool | Alle (Claude, Copilot) | Nur Claude Code |

**Was ist das Copilot-Äquivalent zu MEMORY.md?**

Copilot hat kein automatisches Gedächtnis. Das nächstbeste ist:
- `.github/copilot-instructions.md` → wird bei jeder Anfrage mitgeladen, aber **ihr pflegt sie manuell**
- Copilot schreibt sie nicht selbst und aktualisiert sie nicht

```
Claude Code MEMORY.md  →  KI schreibt und aktualisiert selbst
Copilot instructions   →  Ihr schreibt, statisch, manuell gepflegt
```

**MEMORY.md anlegen:**
```
Prompt an Claude Code: "Erstelle eine MEMORY.md für dieses Projekt"
→ KI scannt das Repo und schreibt die Datei automatisch
```

**Live-Demo:**
1. MEMORY.md anlegen lassen
2. Session schließen, neu starten
3. KI kennt das Projekt sofort – ohne eine einzige Erklärung

---

## 7. Plan Mode – Erst denken, dann ausführen

**Problem:** KI ohne Plan fängt sofort an zu coden → oft falsche Richtung.

**Lösung: Plan Mode**
```
1. Prompt: "Erstelle zuerst nur einen Plan. Noch keinen Code."
2. Plan reviewen – stimmt die Richtung?
3. Freigeben: "Führe jetzt den Plan aus"
4. Auto-Accept: KI arbeitet den Plan selbstständig ab
```

In Claude Code: `/plan` Command
In Copilot: "Plan first, then implement" als Prompt-Prefix

---

## 8. Verifikation – Nie fertig ohne Beweis

KI meldet sich oft als fertig obwohl etwas nicht funktioniert.

**Verifikations-Workflow:**
1. Code laufen lassen (Tests, Build)
2. Diff reviewen: Was hat sich wirklich geändert?
3. Frage: "Would a staff engineer approve this?"
4. Bei Fehlern: `/review` Command nutzen

**Chain-of-Verification** (für kritische Logik):
```
Prompt: "[Deine Frage]

Now follow these steps:
1. Gib deine initiale Antwort
2. Erstelle 3-5 Verifikationsfragen die Fehler in deiner Antwort aufdecken könnten
3. Beantworte jede Frage separat
4. Gib deine korrigierte finale Antwort"
```

**Was tue ich wenn der KI-Output falsch ist?**

Das passiert. Regelmäßig. Hier wie ihr systematisch damit umgeht:

**Fall 1: Code kompiliert nicht / Tests schlagen fehl**
```
Fehlermeldung direkt in den Chat kopieren:
"Ich bekomme diesen Fehler: [FEHLERMELDUNG]. Analysiere die Ursache und behebe sie."

→ Nicht: "Das funktioniert nicht" (zu vage)
→ Ja:    Vollständige Fehlermeldung einfügen
```

**Fall 2: Code läuft aber ist logisch falsch**
```
"Das Ergebnis ist falsch. Erwartet: [X]. Tatsächlich: [Y].
Analysiere Schritt für Schritt warum das passiert."

→ KI zur Ursachenanalyse zwingen, nicht einfach neu generieren lassen
```

**Fall 3: KI dreht sich im Kreis (macht denselben Fehler wieder)**
```
Drei Optionen:
1. Neuen Chat starten – Kontext ist zu voll/verwirrt
2. Aufgabe anders formulieren (anderer Winkel, anderes Beispiel)
3. Manuell eingreifen und den korrekten Teil vorgeben:
   "Dieser Teil ist korrekt: [CODE]. Baue jetzt [FEHLENDEN TEIL] dazu."
```

**Fall 4: KI halluziniert (erfindet Libraries/APIs die nicht existieren)**
```
"Die Methode [X] existiert nicht in [Library Y].
Welche tatsächlich existierende Alternative erreichst du dasselbe Ziel?"

→ lessons.md updaten: "KI halluziniert bei [Thema] – explizit auf Existenz prüfen"
```

**Goldene Regel:** Jeder korrigierte Fehler gehört in `tasks/lessons.md` – damit die KI ihn beim nächsten Mal nicht wiederholt.

---

## Zusammenfassung

```
Guter Vibe-Coding-Workflow:
1. CLAUDE.md erstellen (Kontext geben)
2. MEMORY.md erstellen (Claude Code-Gedächtnis einrichten)
3. /plan → Plan reviewen → freigeben
4. KI implementiert (Auto-Accept)
5. Verifikation: Tests, Review, /review Command
6. Korrekturen → lessons.md updaten
```
