# Update-Prompt: Tag 1 Präsentation anpassen

## Anweisung
Die Tag-1-Präsentation existiert bereits. Führe folgende Änderungen durch — **keine Folien löschen**, nur anpassen und ergänzen.

---

## 1. Design-Update (ALLE Folien)

Ändere das Farbschema der gesamten Präsentation:

| Element | ALT | NEU |
|---------|-----|-----|
| Primärfarbe (Überschriften, Highlights) | Dunkelblau #1a365d | Deep Blue #3f59ff |
| Titel, dunkle Flächen | Dunkelblau #1a365d | Navy #212b80 |
| Akzentfarbe (Hervorhebungen) | Orange #ed8936 | Golden Yellow #ffd440 |
| Hintergrund | Grau #f7fafc | Weiß #ffffff |
| Text | Schwarz | Dunkelgrau #212121 |

Schriftart auf **Open Sans** ändern (oder Calibri als Fallback).
Mehr Weißraum zwischen Elementen — clean, modern, tech-professional (inspiriert von iamds.com).

---

## 2. Folien-Änderungen

### Folie 3: "Wie ein LLM technisch arbeitet" (ERSETZEN falls noch alte Version)
Falls die Folie noch generisch ist ("Ein LLM hat viel Text gelesen"), ersetze sie durch:
- 3 Schritte als Diagramm:
  1. **Tokenization (BPE):** "getUserById" = 3 Tokens ["get", "User", "ById"] — CamelCase kostet mehr Tokens als snake_case
  2. **Transformer + Attention:** Berechnet Wahrscheinlichkeit des nächsten Tokens basierend auf Kontext
  3. **Sampling:** Temperature 0.0 = deterministisch (Code) vs. 0.7 = kreativ (Architektur-Alternativen)
- Keine vereinfachte "Text gelesen"-Erklärung — stattdessen technische Darstellung

### Folie 4: "Token-Kosten" (ERSETZEN falls noch alte Version)
Falls die Folie noch generische LLM-Eigenschaften zeigt, ersetze sie durch:
- **Kosten-Tabelle:**
  | Modell | Input/1M Tokens | Output/1M Tokens | Context Window | ~Kosten pro Feature |
  |--------|----------------|-----------------|----------------|---------------------|
  | Claude Sonnet 4 | $3 | $15 | 200k | ~$0.18 |
  | Claude Opus 4 | $15 | $75 | 200k | ~$0.90 |
  | GPT-4o | $2.50 | $10 | 128k | ~$0.13 |
- Kernaussage: "200k Tokens ≈ 500 Seiten Code. Aber: Je voller, desto schlechter (Context Rot)."

### Folie 5: "3 typische Code-Halluzinationen" (ERSETZEN falls noch alte Version)
Falls die Folie nur allgemein über Halluzinationen spricht, ersetze durch:
- 3 Code-Beispiele:
  1. **Nicht-existierende API:** `repository.findByNameContainingIgnoreCase()` — existiert nur mit korrekter Spring Data Config
  2. **Falsche Version:** `WebSecurityConfigurerAdapter` — in Spring Boot 3 entfernt
  3. **Erfundene CLI-Flags:** `mvn spring-boot:run --debug-port=5005` — existiert nicht so
- Kernaussage: "KI kennt das Pattern, aber nicht EUER Projekt. Output immer gegen Compiler + Docs prüfen."

### Folie 17: "L — Logical structure + KERNEL-Gesamtbeispiel" (ANPASSEN)
Stelle sicher, dass diese Folie einen echten Code-Vergleich zeigt:
- **OHNE KERNEL:** "Hilf mir eine Spring-App zu bauen" → generisches Hello-World (5 Zeilen Code)
- **MIT KERNEL:** Context/Task/Constraints/Format → vollständiger UserController + Service mit BCrypt, @Valid, Interface-Pattern (15 Zeilen Code)
- Zwei Code-Blöcke nebeneinander (Vorher/Nachher)

### Folie 28: "Context-Window-Management" (NEU EINFÜGEN vor Zusammenfassung)
Falls diese Folie noch nicht existiert, füge sie vor der Zusammenfassung ein:
- Grafik: Neuer Chat (5% belegt) → Nach 30 Min (40%) → Nach 2h (70%, Qualität sinkt) → Voll (Komprimierung)
- Warnsignale: KI wiederholt Fehler, vergisst Konventionen, generischer Code
- Strategien-Tabelle:
  | Situation | Strategie |
  |-----------|-----------|
  | Feature fertig | Neuen Chat starten |
  | Context voll | /compact |
  | Großes Feature | Sub-Agents von Anfang an |
- Faustregel: "Ein Chat = ein Feature"

### Folie 29 (letzte Folie): "Zusammenfassung Tag 1" (ANPASSEN)
- Key Takeaways:
  1. LLMs sind Wahrscheinlichkeits-Maschinen — immer verifizieren
  2. Entwickler = Problem Solver, KI = Umsetzer
  3. KERNEL-Framework für systematisch bessere Prompts
  4. CLAUDE.md + MEMORY.md = KI kennt euer Projekt
  5. Plan Mode → Implementierung → Verifikation → Lessons Learned
