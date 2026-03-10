# Tag 2 (Vormittag) – KI als Coding Assistant im Alltag

## Lernziele
- KI produktiv einsetzen auch ohne vollständigen Repo-Zugriff
- CLAUDE.md / Copilot-Instructions als Kontext-Werkzeug verstehen
- Sub-Agents und Lessons Learned im Team-Alltag etablieren
- Unterschied: Copilot Inline vs. Agent Mode situativ einsetzen

---

## 1. Das Problem: KI ohne Repo-Zugriff

In der Realität arbeiten Entwickler oft ohne vollständigen Kontext:
- Kein Zugriff auf das gesamte Repository
- Nur einzelne Dateien sichtbar
- Neues Team-Mitglied das sich einarbeitet

**Was passiert wenn KI keinen Kontext hat:**
```
Prompt: "Füge einen neuen Endpoint hinzu"
KI-Antwort: Generischer Code der nicht zum Projekt passt,
            falsche Dependencies, ignorierte Konventionen
```

**Lösung:** Kontext aktiv liefern – nicht darauf warten dass KI ihn findet.

---

## 2. Die Kontext-Hierarchie

```
CLAUDE.md / copilot-instructions.md    ← Immer geladen (Projekt-Basis)
         ↓
Rules / Instructions                   ← Automatisch bei bestimmten Dateitypen
         ↓
Skills / Reusable Prompts              ← Bei Bedarf laden
         ↓
Dein Prompt                            ← Spezifischer Task
```

### Was wohin gehört:

| Information | Wohin |
|-------------|-------|
| Tech Stack, Versions | CLAUDE.md |
| Coding-Konventionen | CLAUDE.md oder Rule |
| Dateityp-spezifische Standards | Rule (applyTo: *.java) |
| Domänen-Wissen, komplexe Prozesse | Skill |
| Spezifischer Task heute | Dein Prompt |

---

## 3. CLAUDE.md im Team-Alltag

**Das Geheimnis der produktiven KI-Nutzung:** Die Konfiguration wird einmal gut aufgebaut,
dann profitiert das ganze Team automatisch davon.

**Was in einer guten CLAUDE.md steht:**
```markdown
## Tech Stack (exact versions)
## Coding-Konventionen (mit Beispielen)
## Was wir NICHT wollen (explizite Verbote)
## Projektstruktur
## Häufige Patterns (unser Service-Pattern, DTO-Pattern)
```

**Live-Demo:**
1. CLAUDE.md aus dev-setup-template zeigen
2. Prompt ohne CLAUDE.md → unpassendes Ergebnis
3. Prompt mit CLAUDE.md → passt sofort

---

## 4. Lessons Learned Loop

**Problem:** KI macht denselben Fehler immer wieder.
**Lösung:** `tasks/lessons.md` – Team-Gedächtnis das jede Session geladen wird.

```markdown
## 2026-03-07 – KI nutzt Options API statt Composition API
Root Cause: Prompt hat nicht explizit Composition API gefordert
Regel: In CLAUDE.md steht "Keine Options API" – aber KI ignoriert es manchmal
Fix: Im Prompt zusätzlich: "Ausschließlich <script setup> Composition API"
```

**Workflow:**
1. KI macht Fehler → User korrigiert
2. Lesson in `tasks/lessons.md` aufschreiben
3. KI liest `lessons.md` zu Beginn jeder Session → Fehler wiederholt sich nicht

---

## 5. Copilot: Inline vs. Agent Mode

### Inline Autocomplete
```
Wann:     Während du tippst, bekannte Patterns
Context:  Nur aktuelle Datei + offene Tabs (~2-5 Dateien)
Latenz:   50-200ms (fühlt sich instant an)
Tokens:   Wenig (nur kleiner Ausschnitt wird gesendet)
Stärke:   Schnell, kein Prompt nötig, fühlt sich natürlich an
Grenze:   Kein Projekt-weiter Kontext, keine CLAUDE.md/Instructions
```

### Agent Mode (Chat)
```
Wann:     Neue Features, Refactoring, Analyse
Context:  Gesamtes Workspace-Verzeichnis (via @workspace), Instructions geladen
Latenz:   2-15 Sekunden (multi-file Analyse)
Tokens:   Hoch (ganzer Workspace-Kontext wird geladen)
Stärke:   Kann 10+ Dateien lesen und ändern, folgt Instructions
Grenze:   Langsamer, höherer Token-Verbrauch, braucht guten Prompt
```

### Entscheidungsbaum: Welchen Modus wann?
```
Tippe ich gerade Code und will Vervollständigung?
  → JA: Inline (Tab drücken)
Muss ich mehrere Dateien ändern oder verstehen?
  → JA: Agent Mode (@workspace oder @DevAgent)
Brauche ich Projekt-Konventionen (aus Instructions)?
  → JA: Agent Mode (Inline kennt Instructions nicht)
Will ich ein Feature autonom bauen lassen?
  → JA: Agent Mode mit klarem KERNEL-Prompt
```

### Kosten-Perspektive
```
Inline:     ~100-500 Tokens pro Completion → Centbruchteile pro Aufruf
Agent Mode: ~5.000-50.000 Tokens pro Anfrage → $0.05-$0.50 pro Feature
→ Inline ist 100x günstiger, aber Agent Mode spart euch Stunden
```

---

## 6. Sub-Agents: Hauptkontext sauber halten

**Problem:** Lange Konversationen werden ungenau – KI vergisst frühere Entscheidungen.

**Lösung:** Sub-Agents für Research und Exploration:

```
Hauptkontext (sauber, fokussiert)
    ↓
Sub-Agent: "Analysiere alle Service-Klassen und liste Duplikate"
    ↓ Ergebnis zurück (nur das Ergebnis, nicht der ganze Analyse-Kontext)
Hauptkontext weiterführen mit dem Ergebnis
```

**Was technisch passiert wenn ein Sub-Agent startet:**
```
Haupt-Chat:   [CLAUDE.md + bisherige Konversation] = 30% von 200k Tokens belegt
Sub-Agent:    [Neues 200k Window] = 0% belegt → volle Kapazität für die Aufgabe
              → Bekommt NUR: Task-Beschreibung + explizit übergebene Dateien
              → Gibt NUR das Ergebnis zurück (nicht seinen internen Kontext)
```

**Kosten-Rechnung:**
```
1 Sub-Agent  = 1 neues Context Window = 1x Token-Kosten für die Aufgabe
5 parallele  = 5 Windows = 5x Token-Kosten ABER: alle laufen gleichzeitig
→ Lohnt sich wenn die Alternative "3 Stunden sequenziell" ist
→ Lohnt sich NICHT für einfache Aufgaben die in 5 Minuten erledigt sind
```

**Wann Sub-Agent einsetzen:**
- Recherche und Analyse ("Was existiert bereits?")
- Parallele unabhängige Aufgaben
- Aufgaben die den Kontext "verschmutzen" würden (z.B. große Log-Analyse)

**Was passiert wenn der Kontext voll wird?**

Jedes KI-Modell hat ein Kontextfenster – eine maximale Menge an Text die es gleichzeitig "sehen" kann. Claude Code komprimiert automatisch wenn die Grenze naht, aber das hat Konsequenzen:

```
Kurze Konversation:   KI erinnert sich an alles
Lange Konversation:   KI beginnt frühere Entscheidungen zu "vergessen"
Sehr lange Session:   Widersprüche, vergessene Constraints, inkonsistenter Code
```

**Anzeichen dass der Kontext zu groß wird:**
- KI wiederholt Fragen die schon beantwortet wurden
- KI ignoriert Constraints aus dem CLAUDE.md
- Code-Stil weicht plötzlich ab
- KI schlägt Lösungen vor die ihr schon verworfen habt

**Die Lösung:** Sub-Agents für alles was nicht zum Kern-Task gehört.
Ein neuer Sub-Agent startet mit einem sauberen Kontext – er kennt nur was ihr ihm mitgebt.

```
Hauptkontext: nur aktueller Feature-Task
Sub-Agent:    bekommt nur was er braucht → gibt Ergebnis zurück → done
```

**Faustregel:** Wenn eine Konversation länger als 30 Minuten aktiv Coding macht → neuen Chat starten oder `/swarm` nutzen.

---

## 7. Wann startet Claude Code / Copilot automatisch einen Agent?

Eine Frage die immer kommt: **"Muss ich Agents manuell aufrufen oder passiert das automatisch?"**

Die Antwort hängt davon ab wie der Agent definiert ist – und welches Tool ihr verwendet.

### Claude Code: Automatisch vs. manuell

Claude Code entscheidet selbst ob es einen Sub-Agent startet – basierend auf zwei Dingen:

**A) `description:` Frontmatter in der Agent-Datei (automatischer Trigger)**
```markdown
---
name: ReviewAgent
description: Führt Code Reviews durch. Wird automatisch aufgerufen wenn
             Code vor einem PR-Merge geprüft werden soll.
tools:
  - read_file
  - grep_search
---
```
→ Claude liest die `description` und entscheidet: "Passt diese Aufgabe zum Agent?"
→ Bei Treffer: startet Agent automatisch ohne dass ihr explizit darum bittet

**B) Kein Frontmatter = manueller Trigger**
```markdown
# Agent: Dev Agent
## Rolle
...
```
→ Claude startet diesen Agent nur wenn ihr ihn explizit anfordert:
```
/review
@DevAgent implementiere Feature X
```

**Wichtig für unser Setup:** Die Dateien in `.claude/agents/` haben aktuell **kein `description:` Frontmatter** – sie werden also nur manuell über Commands (`/review`, `/swarm`) oder explizite Erwähnung aktiviert. Das ist bewusst so, damit ihr die Kontrolle behaltet.

### Copilot: Proaktive Agent-Vorschläge via copilot-instructions.md

Copilot kann zwar Agents nicht automatisch starten, aber wir können ihn trainieren **Situationen zu erkennen** und proaktiv einen Agent-Aufruf vorzuschlagen:

```markdown
# In copilot-instructions.md:
## Situationen erkennen – Agent proaktiv vorschlagen

| Situation | Agent vorschlagen |
|-----------|-----------------|
| Vue-Komponente geändert aber kein Test erwähnt | "Möchtest du @TestAgent für den Vitest-Test nutzen?" |
| Neuer User-Flow implementiert | "Möchtest du @TestAgent für einen Playwright E2E-Test?" |
| Feature fertig, PR-Merge steht an | "Möchtest du @ReviewAgent vor dem Merge aufrufen?" |
```

**Effekt:** Copilot erkennt diese Situationen und fragt von selbst – fast wie automatisch, nur mit einem Klick Bestätigung.

### Copilot: Immer über `@AgentName`

Copilot startet Agents **nie automatisch** – ihr ruft sie immer explizit auf:

```
@KnowledgeAgent Erkläre mir das KERNEL-Framework
@ReviewAgent Prüfe den aktuellen Code
```

Damit Copilot einen Agent kennt, braucht die Agent-Datei zwingend das Frontmatter:
```markdown
---
name: KnowledgeAgent
description: Beantwortet Fragen aus der internen KI-Wissensbasis
tools:
  - read_file
  - web_search
---
```

### Gegenüberstellung

| | Claude Code | Copilot |
|---|---|---|
| **Automatischer Trigger** | Ja (wenn `description:` im Frontmatter) | Nein |
| **Manueller Aufruf** | `/command` oder Erwähnung im Prompt | `@AgentName` |
| **Agent-Dateien** | `.claude/agents/` | `.github/agents/` |
| **Frontmatter nötig?** | Für Auto-Trigger ja, sonst nein | Immer ja |

### Faustregel für euer Projekt

```
Ihr wollt Kontrolle behalten?
  → Kein description:-Frontmatter → KI startet Agent nur wenn ihr es sagt

Ihr wollt maximale Automatisierung?
  → description: hinzufügen → KI entscheidet selbst wann der Agent passt
```

---

## 8. /swarm – Wie parallele Agents technisch funktionieren

`/swarm` ist die Antwort auf die Frage: **"Was wenn eine Aufgabe zu groß für einen Agent ist?"**

**Das Prinzip:**
```
Ohne Swarm:  Ein Agent → eine Aufgabe → sequenziell → langsam
Mit Swarm:   Mehrere Agents → parallele Aufgaben → gleichzeitig → schnell
```

**Wie /swarm abläuft (Schritt für Schritt):**

```
1. Ihr tippt: /swarm
2. Claude analysiert die aktuelle Aufgabe
3. Claude erstellt einen Swarm-Plan:
   ┌─────────────────────────────────────┐
   │ Runde 1 (parallel):                 │
   │   Agent 1 → Service A implementieren│
   │   Agent 2 → Service B implementieren│
   │                                     │
   │ Runde 2 (parallel):                 │
   │   Agent 3 → Tests für Service A     │
   │   Agent 4 → Tests für Service B     │
   │                                     │
   │ Runde 3:                            │
   │   Agent 5 → Review aller Ergebnisse │
   └─────────────────────────────────────┘
4. Ihr bestätigt den Plan
5. Claude startet die Agents – jeder im eigenen Kontextfenster
6. Ergebnisse werden zusammengeführt
```

**Wichtig zu verstehen:**
- Jeder Agent sieht nur seinen eigenen Task + was ihm explizit mitgegeben wird
- Agents kommunizieren nicht direkt miteinander – nur über euren Hauptkontext
- Der Review-Agent am Ende ist Pflicht – er sieht alle Ergebnisse und prüft Konsistenz
- Max. 5 parallele Agents (mehr → Qualität leidet, Überblick geht verloren)

**Wann lohnt sich /swarm?**
```
Lohnt sich:
  ✓ 3+ unabhängige Module implementieren
  ✓ COBOL-Migration: jedes Modul = ein Agent (Tag 3!)
  ✓ Große Refactorings über viele Dateien

Lohnt sich NICHT:
  ✗ Einfache Features (ein Agent reicht)
  ✗ Aufgaben die aufeinander aufbauen (sequenziell besser)
  ✗ Wenn ihr den Überblick nicht behalten könnt
```

---

## 9. Built-in Commands – Was Claude Code & Copilot out-of-the-box liefern

### Claude Code – Wichtigste Commands

| Command | Was es macht |
|---------|-------------|
| `/plan` | Plan erstellen vor der Umsetzung – keine Änderungen ohne Freigabe |
| `/review` | Code-Review des aktuellen Diffs |
| `/commit` | Schreibt Commit-Message und committet |
| `/swarm` | Startet parallele Sub-Agents (siehe Abschnitt 8) |
| `/loop 5m /review` | Alle 5 Minuten automatisch ein Review ausführen |
| `/compact` | Kontext komprimieren wenn er zu voll wird |

### Copilot-Äquivalente

| Claude Code | Copilot | Parität |
|---|---|---|
| `/plan` | `"Plan first, then implement"` als Prompt-Prefix | 🟡 Manuell |
| `/review` | GitHub PR Review / `@workspace review` | ✅ Gut |
| `/commit` | Source-Control-Panel | 🟡 Manuell |
| `/swarm` | Kein Äquivalent | 🔴 Nicht verfügbar |
| `/loop` | GitHub Actions | 🔴 Anderes Tool |
| `/ralph` | Ralph-Verhalten in copilot-instructions.md | 🟡 Manuell (kein Command) |
| MEMORY.md | `copilot-instructions.md` manuell pflegen | 🔴 Kein Auto-Update |

**Faustregel:** Claude Code hat deutlich mehr Automatisierung out-of-the-box. Copilot ist stärker bei IDE-Integration und Inline-Completion.

### Alternative: Mistral Vibe CLI + Continue.dev

Falls euer Team **Mistral Teams Lizenzen** hat, gibt es einen dritten Weg:

| Tool | Typ | Stärke | Config |
|------|-----|--------|--------|
| Claude Code | CLI-Agent | Reifstes Ecosystem, CLAUDE.md, Rules, Skills | `.claude/`, `CLAUDE.md` |
| Mistral Vibe | CLI-Agent | Open Source, Devstral 2 (72.2% SWE-bench), GRATIS | `.vibe/config.toml`, `AGENTS.md` |
| GitHub Copilot | IDE-Plugin | Inline-Completion, Chat in VS Code | `.github/copilot-instructions.md` |
| Continue.dev + Codestral | IDE-Plugin | FIM-Autocomplete, sehr günstig (€0.30/M Tokens) | Continue.dev Extension |

**Mistral Vibe installieren:**
```bash
curl -LsSf https://mistral.ai/vibe/install.sh | bash
export MISTRAL_API_KEY=your-key
vibe
```

Vibe nutzt `.vibe/config.toml` statt `CLAUDE.md` und `AGENTS.md` im Workspace-Root für Projektkontext. Das dev-setup-template liefert beides mit — Claude Code und Vibe können parallel im selben Repo arbeiten.

### /ralph – Iterativer Selbst-Verifikations-Loop

Der `/ralph` Command ist unser Qualitäts-Firewall vor dem Commit:

```
/ralph
→ Claude prüft seinen letzten Output gegen 6 Kriterien:
  1. Vollständig? (alle Requirements erfüllt)
  2. Korrekt? (syntaktisch + logisch)
  3. Konventionen? (Java/Vue/DB-Standards)
  4. Sicher? (keine Security-Lücken)
  5. Minimal? (nichts außerhalb des Scopes)
  6. Elegant? (Staff-Engineer-Standard)
→ Was fehlschlägt wird sofort gefixt, erst dann: fertig
```

**Was /ralph konkret ausgibt (Beispiel):**
```
✅ Vollständig: Alle 3 Endpoints implementiert (POST, GET, DELETE)
✅ Korrekt: Kompiliert, Tests grün (5/5)
⚠️ Konventionen: UserService fehlt Interface-Abstraktion → FIX: Interface erstellt
✅ Sicher: Kein SQL-Injection, @Valid auf allen Inputs
✅ Minimal: Keine ungefragten Extras
⚠️ Elegant: Duplicate Code in createUser/updateUser → FIX: Shared Validation-Methode

→ 2 Issues gefunden und automatisch gefixt. Ergebnis jetzt bereit für Review.
```

**Einschränkung:** /ralph kann selbst halluzinieren – z.B. "Tests grün" melden ohne sie tatsächlich ausgeführt zu haben. Deshalb: Nach /ralph immer noch einmal `mvn test` oder `npm test` selbst laufen lassen.

**Copilot-Äquivalent:** Durch den "Selbst-Verifikation"-Abschnitt in `copilot-instructions.md` wird dieses Verhalten auch Copilot eintrainiert — ohne expliziten Command, als feste Verhaltensregel.

---

## 10. KI autonom arbeiten lassen – Feature-Liste übergeben

Das mächtigste Pattern im täglichen Einsatz: Ihr beschreibt **was** gebaut werden soll — KI baut es komplett durch.

### Ein Feature – direkt beschreiben

```
Baue mir den kompletten Passwort-Reset-Flow:

Requirements:
- POST /api/v1/auth/forgot-password → sendet Reset-E-Mail mit Token
- POST /api/v1/auth/reset-password → Token validieren, neues Passwort setzen
- Token läuft nach 24h ab, einmalig verwendbar
- Frontend: ForgotPasswordView.vue + ResetPasswordView.vue

Stack aus CLAUDE.md gilt. Arbeite autonom bis fertig.
```

### Mehrere Features – als prd.md übergeben

1. `tasks/prd.md` anlegen mit priorisierten Features:
```markdown
## Feature 1: Passwort-Reset (HOCH)
## Feature 2: User-Profil (MITTEL)
## Feature 3: Audit-Log (NIEDRIG)
```

2. Claude beauftragen:
```
Lies tasks/prd.md. Baue alle Features nach Priorität.
Nach jedem Feature /ralph zur Qualitätsprüfung.
Halte tasks/todo.md aktuell. Fang an.
```

### Parallel – /swarm für unabhängige Features

```
/swarm
Feature 1 und 2 sind unabhängig → parallel.
Feature 3 braucht Feature 1 → danach.
Review-Agent prüft alle am Ende.
```

### Copilot – @DevAgent sequenziell

```
@DevAgent Implementiere Feature 1: [Requirements]
→ Fertig → @DevAgent für Feature 2
→ Am Ende @ReviewAgent
```

**Der Schlüssel:**
```
Vage:    "Baue eine Login-Funktion"       → KI fragt nach, stoppt
Konkret: Endpunkt + Body + Response +
         Fehler + Business Rules          → KI baut durch ohne Unterbrechung
```

---

## 11. Praktischer Alltags-Workflow

```
Morgens:
1. Claude Code / Copilot öffnen
2. lessons.md wird automatisch geladen (Session-Start-Regel in CLAUDE.md)
3. tasks/todo.md checken: Offene Aufgaben?

Feature entwickeln:
1. Aufgabe > 3 Schritte? → /plan erst
2. Klare Requirements? → Direkt beschreiben, autonom bauen lassen
3. Feature-Liste? → tasks/prd.md anlegen, dann übergeben
4. Fertig? → /ralph dann /review aufrufen
5. Merge-ready? → PR erstellen

Abends / nach Korrekturen:
6. Lessons learned in tasks/lessons.md
```

---

## Übung: Coding Assistant ohne Repo (→ Aufgabendatei)

Ihr erhaltet gleich eine Aufgabe die ihr lösen sollt,
aber NUR mit folgenden Informationen (kein Repository-Zugriff):
- API-Dokumentation (Markdown)
- Tech Stack (ohne Code)
- Eure CLAUDE.md

Ziel: Herausfinden wie viel ihr mit gutem Kontext trotzdem erreichen könnt.
