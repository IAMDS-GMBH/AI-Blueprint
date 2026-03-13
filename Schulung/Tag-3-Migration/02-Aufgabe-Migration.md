# Tag 3 – Aufgabe: COBOL-Migration zu Java

**Arbeitsverzeichnis:** `Aufgabe/` (Fortsetzung der Vortage)

---

## Ziel

COBOL-Code wird nach Java migriert — mit KI-Unterstuetzung.
Zuerst anhand synthetischer Beispiele, anschliessend optional mit echtem COBOL aus dem eigenen Unternehmen.

**Vorgehensmodell:**

```
1. Verstehen           → COBOL-Code vollstaendig analysieren
2. Pseudocode          → Analyse-Ergebnis als strukturierten Pseudocode festhalten
3. Tests schreiben     → TDD: Tests basierend auf Pseudocode (rot)
4. Implementieren      → Schrittweise umsetzen (gruen)
5. Verifizieren        → Tests die COBOL- und Java-Ergebnisse vergleichen
```

> Die Analyse-Phase ist entscheidend. Ohne vollstaendiges Verstaendnis des Quellcodes
> entstehen fehlerhafte Migrationen, die erst spaet auffallen.

---

## 1. Projekt aufsetzen

```bash
cd Aufgabe
mkdir cobol && cd cobol
```

> Das KI-Setup aus `Aufgabe/` wird weiterverwendet. Die Konfigurationsdatei (z.B. `CLAUDE.md`) um den Migrations-Kontext ergaenzen:

- Projekttyp: COBOL-zu-Java Migration
- Stack: Java 17, Maven (kein Spring Boot noetig — reine Fachlogik-Migration)
- Ziel: Fachlogik 1:1 migrieren mit identischen Ein- und Ausgaben
- Besondere Anforderungen: BigDecimal fuer Geldbetraege, korrekte Rundungslogik

---

## 2. Uebung: payroll.cbl — Gehaltsberechnung

Datei: `cobol-beispiele/payroll.cbl` (im Schulungs-Repo)

Die Datei in das Projektverzeichnis kopieren.

### Analyse

Den COBOL-Code von der KI analysieren lassen. Folgende Punkte muessen verstanden sein:
- Was berechnet das Programm?
- Welche Eingabefelder existieren (mit COBOL-Datentypen)?
- Welche Ausgabe wird erzeugt?
- Welche Business-Logik ist in der PROCEDURE DIVISION enthalten?
- Wo koennten bei der Java-Migration Probleme auftreten?

> **In diesem Schritt wird kein Java-Code erstellt.** Ausschliesslich Analyse.

### Pseudocode erstellen

Analyse-Ergebnis als `payroll-pseudocode.md` festhalten:
- **Datenstrukturen** als Tabelle (Feldname, COBOL-Typ, Java-Typ, Bemerkung)
- **Hauptlogik** als strukturierter Pseudocode (Schritt fuer Schritt)
- **Kritische Stellen** markieren (Rundung, Datentyp-Konvertierung, Edge Cases)

> Der Pseudocode dient als Spezifikation fuer die Tests.

### Tests schreiben (TDD)

Basierend auf dem Pseudocode Tests **vor** der Implementierung schreiben:
- JUnit 5 mit `@Nested`-Klassen fuer Gruppierung
- Workflow: Pseudocode → Tests (rot) → Implementierung (gruen) → Refactor

**Konkrete Testdaten:**

| Eingabe | Wert |
|---------|------|
| Brutto | 5000 |
| Steuersatz | 19% |
| Sozialabgaben | 20% |
| Bonus | 500 |

| Erwartung | Wert |
|-----------|------|
| Steuerbasis (Brutto + Bonus) | 5500 |
| Steuer (19% von 5500) | 1045 |
| Sozialabgaben (20% von 5000) | 1000 |
| Netto (5500 - 1045 - 1000) | 3455 |

### Migration

Im **Plan Mode** einen Migrationsplan erstellen lassen. Den Plan pruefen:
- Werden Geldbetraege als BigDecimal behandelt?
- Ist die Rundungslogik beruecksichtigt?
- Sind Tests eingeplant?

Plan umsetzen lassen und anschliessend verifizieren:
- Java-Ergebnis mit COBOL-Ergebnis abgleichen
- Edge Cases pruefen

**Akzeptanzkriterien:**
- [ ] Java-Code liefert identische Ergebnisse wie das COBOL-Programm
- [ ] BigDecimal fuer alle Geldbetraege
- [ ] JUnit 5 Tests vorhanden und bestanden
- [ ] Edge Cases abgedeckt

---

## 3. Uebung: inventory.cbl — Lagerverwaltung

Datei: `cobol-beispiele/inventory.cbl`

Gleicher Ablauf:
1. COBOL analysieren (Programmzweck, Geschaeftsregeln)
2. Pseudocode als `inventory-pseudocode.md` festhalten
3. Tests schreiben (TDD, basierend auf Pseudocode)
4. Migrationsplan erstellen (Java Records/POJOs, Service-Interface + Impl)
5. Implementieren (Stack: Java 17, Maven — Plain Java ohne Framework)
6. Verifizieren (Wareneingang, Warenausgang, Mindestbestand-Logik)

**Konkrete Testdaten:**

| Eingabe | Wert |
|---------|------|
| Bestand | 100 |
| Mindestbestand | 50 |
| Nachbestellmenge | 200 |
| Buchung | Ausgang, Menge 80 |

| Erwartung | Wert |
|-----------|------|
| Neuer Bestand | 20 |
| Nachbestellung noetig | JA |
| Nachbestellmenge | 200 |

---

## 4. Echtes COBOL migrieren

> **Optional — falls eigener COBOL-Code vorhanden.**
> Wenn kein eigener COBOL-Code vorliegt: Die bestehenden Uebungen (payroll, inventory) mit zusaetzlichen Edge Cases erweitern.

### Voraussetzungen
- [ ] COBOL-Datei anonymisiert (keine echten Kunden- oder Mitarbeiterdaten)
- [ ] Bekannte Test-Inputs und erwartete Outputs vorbereitet
- [ ] Grundlegendes Verstaendnis der Modul-Funktion vorhanden

### Vorgehen

Gleicher Ablauf wie bei den Uebungen — mit mehr Zeit fuer die Analyse.
Produktiver COBOL-Code ist in der Regel deutlich komplexer.

1. **Verstehen** — Besonders beachten:
   - Abhaengigkeiten zu externen Systemen (CICS, DB2, Dateien)
   - Kritische Berechnungen und Geschaeftsregeln
   - Aspekte die sich nicht 1:1 migrieren lassen

2. **Pseudocode** — Analyse-Ergebnis strukturiert festhalten (wie bei payroll/inventory)

3. **Tests** — TDD: Tests vor der Implementierung schreiben

4. **Implementieren** — Schrittweise, nicht den gesamten Code auf einmal.

5. **Verifizieren** — Tests die COBOL- und Java-Ergebnisse vergleichen.

> **Bei mehreren COBOL-Modulen:** Mit `/swarm` (Claude Code) koennen Module parallel migriert werden.
> Jedes Modul erhaelt einen eigenen Agent. Der Review-Agent prueft alle Ergebnisse.

---

## Abschluss-Retrospektive

### Erfahrungsaustausch
1. Welches COBOL-Modul wurde migriert?
2. Was hat die KI gut umgesetzt, was musste korrigiert werden?
3. Welche Bedeutung hatte die Analyse-Phase vor der Migration?
4. Wie hat der TDD-Ansatz (Tests vor Implementierung) die Qualitaet beeinflusst?
5. Was wuerde beim naechsten Migrations-Projekt anders gemacht werden?

### Gemeinsamer Abschluss
- `tasks/lessons.md` befuellen: Zentrale Erkenntnisse aus allen 3 Tagen
- Welche KI-Workflows werden in den Arbeitsalltag uebernommen?
- Naechste Schritte: Was wird als erstes im eigenen Team eingefuehrt?
