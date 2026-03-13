# Prompt: KI-optimierte Tickets — Folien fuer Tag2.pptx

> Diesen Prompt an die Claude PowerPoint Extension geben, um die Folien in die bestehende Tag2.pptx einzufuegen.

## Anweisung

Oeffne die Datei `Tag2.pptx` im selben Ordner und fuege die folgenden **5 neuen Folien** ein — **nach dem Abschnitt "KI autonom arbeiten lassen"** (vor "Praktischer Alltags-Workflow"). Uebernimm das bestehende Design (Farben, Schriftarten, Layout) der Praesentation.

---

## Folie 1: Titelfolie

**Titel:** KI-optimierte Tickets schreiben
**Untertitel:** Warum gute Tickets = gute Prompts sind
**Design:** Gleicher Stil wie die anderen Abschnitts-Titelfolien in der Praesentation

---

## Folie 2: Das Problem — Implizites vs. Explizites Wissen

**Titel:** Warum normale Tickets fuer KI nicht reichen

**Linke Spalte — "Mensch liest Ticket":**
```
Ticket: "Performance der Suche verbessern"

Mensch denkt automatisch:
✓ Elasticsearch-Query im ProductService
✓ Max hat letzte Woche Indizes angelegt
✓ Soll unter 200ms bleiben
✓ Nur Backend, Frontend ist ok
```

**Rechte Spalte — "KI liest Ticket":**
```
Ticket: "Performance der Suche verbessern"

KI sieht:
✗ Welche Suche?
✗ Welches Modul? Welche Datei?
✗ Was ist "gut genug"?
✗ Was ist der Tech-Stack?
→ KI fragt nach oder raet falsch
```

**Fazit-Box am unteren Rand:**
"KI hat keinen Zugang zu muendlichen Absprachen, Sprint-Kontext oder Team-Erfahrung. Was ein Mensch implizit weiss, muss fuer KI explizit im Ticket stehen."

---

## Folie 3: KERNEL auf Tickets anwenden

**Titel:** KERNEL-Framework als Ticket-Checkliste

**Tabelle (6 Zeilen):**

| KERNEL | Prinzip | Anwendung auf Tickets |
|--------|---------|----------------------|
| **K** | Keep simple | Ein Ticket = eine klar abgegrenzte Aenderung |
| **E** | Explicit constraints | Was NICHT gemacht werden soll (Scope-Grenzen) |
| **R** | Reproducible | Versionen, Branch, Environment explizit nennen |
| **N** | Narrow scope | Kein "und ausserdem..." — separates Ticket |
| **E** | Easy to verify | Akzeptanzkriterien als Checkliste |
| **L** | Logical structure | Kontext → Anforderung → Constraints → Erfolg |

**Fazit-Box:**
"KERNEL gilt nicht nur fuer Prompts — es ist die ideale Checkliste fuer jedes Ticket."

---

## Folie 4: Ticket-Template

**Titel:** Template: KI-optimiertes Feature-Ticket

**Code-Block/Box mit dem Template:**
```markdown
## Titel: [Bereich] Aktion + Ziel
   Beispiel: "Auth: Password-Reset Token per E-Mail senden"

### Kontext
- Betroffene Dateien/Module: src/auth/...
- Tech-Stack: Spring Boot 3.2, PostgreSQL
- Abhaengigkeiten: Braucht Feature X (Ticket #123)

### Anforderung
- WAS gebaut werden soll (nicht WIE)
- Endpunkte, Felder, Verhalten

### Akzeptanzkriterien
- [ ] POST /api/v1/auth/forgot-password sendet E-Mail
- [ ] Token laeuft nach 24h ab
- [ ] Test: Happy Path + Token expired

### Scope-Grenzen
- NICHT: Frontend-Aenderungen (separates Ticket)
- NICHT: E-Mail-Template-Design
```

**Hinweis-Box:**
"Auch als Bug-Ticket und Refactoring-Ticket verfuegbar → siehe Wissensbasis Artikel 18"

---

## Folie 5: Vorher/Nachher — Konkretes Beispiel

**Titel:** Vorher/Nachher: Gleiches Feature, unterschiedliches Ergebnis

**Obere Haelfte — "Vorher (vages Ticket)":**
```
Titel: Login verbessern
Beschreibung: Der Login soll besser werden.
              Bitte auch an die Security denken.

→ KI fragt 5x nach
→ Baut OAuth2 statt JWT (geraten)
→ Ignoriert bestehende Patterns
→ Ergebnis passt nicht
```

**Untere Haelfte — "Nachher (KI-optimiert)":**
```
Titel: Auth: JWT Refresh-Token Rotation implementieren
Kontext: Spring Boot 3.2, Spring Security 6.x, jjwt 0.12
Anforderung: Access-Token (15min) + Refresh-Token (7d) mit Rotation
Akzeptanzkriterien:
- [ ] POST /api/v1/auth/refresh rotiert Token
- [ ] Altes Token wird invalidiert
- [ ] Test: Happy Path, Expired, Reuse Detection
Scope-Grenzen: NICHT Frontend, NICHT OAuth2

→ KI startet sofort
→ Nutzt korrekte Dependencies
→ Haelt Scope ein
→ Akzeptanzkriterien → Tests
```

**Fazit-Box:**
"Ticket schreiben = Prompt schreiben. KERNEL gilt. Akzeptanzkriterien werden 1:1 zu Tests."

---

## Hinweise fuer die Claude PowerPoint Extension

- Uebernimm exakt das Farbschema und die Schriftarten der bestehenden Tag2.pptx
- Verwende das gleiche Folienlayout (Titel + Content) wie die anderen Folien
- Code-Bloecke mit Monospace-Schrift darstellen
- Die Tabelle auf Folie 3 soll gut lesbar sein (nicht zu klein)
- Fuege die Folien NACH dem Abschnitt "KI autonom arbeiten lassen" ein
