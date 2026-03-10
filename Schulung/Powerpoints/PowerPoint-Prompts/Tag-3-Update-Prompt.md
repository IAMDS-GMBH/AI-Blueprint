# Update-Prompt: Tag 3 Präsentation anpassen

## Anweisung
Die Tag-3-Präsentation existiert bereits mit dem alten Design. Führe folgende Änderungen durch — bestehende Folien anpassen, keine löschen.

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

- Schriftart auf **Open Sans** ändern (Calibri als Fallback)
- Mehr Weißraum zwischen Elementen — clean, modern, tech-professional (inspiriert von iamds.com)

---

## 2. Oracle → PostgreSQL (ALLE betroffenen Folien)

Überall wo "Oracle" vorkommt, ersetze durch "PostgreSQL":
- Gesamtzusammenfassung: "MCP-Server für PostgreSQL" (statt Oracle DB)
- Falls in Hands-on-Folien Oracle erwähnt wird → PostgreSQL

---

## 3. Folien-Änderungen

### Folie "Praxis-Beispiel A — C → Java" (PRÜFEN)
Stelle sicher, dass der Java-Code produktionsreif ist:
- Rechte Seite: Java mit BigDecimal, @Transactional, Exception-Handling, Audit-Logging
- Box: "KI generiert Happy Path gut. Error Handling + Audit müsst ihr explizit anfordern."

### Folie "COBOL → Java Fallen" (PRÜFEN/ERWEITERN)
Stelle sicher, dass diese erweiterte Tabelle vorhanden ist:
| COBOL | Java | Falle! |
| PIC 9(5) | int | Führende Nullen verloren (PLZ "01234") |
| PIC 9(7)V99 | BigDecimal | NIEMALS double! |
| PIC X(30) | String.trim() | COBOL paddet mit Leerzeichen |
| COMP-3 (Packed Decimal) | BigDecimal | BCD-Format: 2 Ziffern/Byte + Sign-Nibble |
| COPYBOOKS (.CPY) | Shared DTO/Record | Wie C-Header: in mehreren Programmen eingebunden |
| FILE SECTION | BufferedReader | Fixed-Width! KEINE Delimiter (kein CSV!) |
| SIGN IS SEPARATE | Vorzeichen-Handling | +/- als eigenes Zeichen gespeichert |

### Folie nach COBOL-Fallen: "COBOL ROUNDED vs. Java RoundingMode" (NEU falls nicht vorhanden)
- COBOL ROUNDED (Standard) = Java RoundingMode.HALF_UP (0.5 → 1)
- COBOL ROUNDED MODE NEAREST-EVEN = Java RoundingMode.HALF_EVEN (Banker's Rounding)
- Kernaussage: "Wenn ihr nicht wisst welches → Testet mit bekannten Werten!"

### Folie nach KI-Tool pro Migrationsschritt: "Verifikationsstrategie" (NEU falls nicht vorhanden)
- 3 Schritte:
  1. **Golden File Testing:** COBOL mit Test-Input → Output speichern → Java mit gleichem Input → vergleichen
  2. **Testdaten-Strategie:** @ParameterizedTest mit Normalfall, Rundung, Maximalwert, Null
  3. **Grenzwerte die COBOL anders handelt:** Negative Zahlen (unsigned PIC), Überlauf (COBOL schneidet ab, Java Exception), Leerzeichen vs. null
- Kernaussage: "Byte-für-Byte identisch bei Geldbeträgen"

### Folie nach KI-Tool pro Migrationsschritt: Mistral als Alternative (ERGÄNZEN)
Falls eine Folie "KI-Tool pro Migrationsschritt" existiert, ergänze:
- Zeile: "Datenschutzkritisch → Devstral Small 2 lokal via OLLAMA"
- Hinweis-Box: "Devstral 2 (Mistral) unterstützt COBOL explizit, 72.2% SWE-bench Verified, aktuell GRATIS via API"
- Kurzer Absatz: "Mistral Vibe CLI kann als Alternative zu Claude Code für Migration-Tasks eingesetzt werden"

### Folie "Ausblick — Wie geht es weiter?" (ANPASSEN)
Füge hinzu:
- **Mistral Vibe CLI:** Open-Source CLI-Agent von Mistral (github.com/mistralai/mistral-vibe), Devstral 2 aktuell GRATIS
- **Vibe Kanban:** Kanban-Board für KI-Agents — Tasks zuweisen, parallel ausführen, Diffs reviewen (`npx vibe-kanban`)

### Folie "Gesamtzusammenfassung 3 Tage" (ANPASSEN)
- Tag 2: "Kontext-Hierarchie, MCP-Server für PostgreSQL, Security" (statt Oracle)
