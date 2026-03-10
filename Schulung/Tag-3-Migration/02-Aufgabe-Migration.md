# Tag 3 – Aufgabe: COBOL-Migration zu Java

**Arbeitsverzeichnis:** `Aufgaben/` (Fortsetzung der Vortage)

---

## Ziel

COBOL-Code wird nach Java migriert — mit KI-Unterstuetzung.
Zuerst anhand synthetischer Beispiele, anschliessend mit echtem COBOL aus dem eigenen Unternehmen.

**Vorgehensmodell:**

```
1. Verstehen           → COBOL-Code vollstaendig analysieren
2. Kritische Stellen   → Migrationsrisiken identifizieren
3. Plan erstellen       → Java-Architektur und Reihenfolge festlegen
4. Implementieren       → Schrittweise umsetzen
5. Verifizieren         → Tests die COBOL- und Java-Ergebnisse vergleichen
```

> Die Analyse-Phase ist entscheidend. Ohne vollstaendiges Verstaendnis des Quellcodes
> entstehen fehlerhafte Migrationen, die erst spaet auffallen.

---

## 1. Projekt aufsetzen

```bash
cd Aufgaben
mkdir cobol-migration && cd cobol-migration
bash <pfad-zum>/dev-setup-template/setup.sh
```

Konfigurationsdatei fuer das Projekt anpassen:
- Projekttyp: COBOL-zu-Java Migration
- Stack: Java 17, Spring Boot 3.2, Maven
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
2. Migrationsplan erstellen (Java-Klassen, JPA Entity, Service)
3. Implementieren (Stack: Java 17, Spring Boot, JPA/Hibernate, PostgreSQL)
4. Verifizieren (Wareneingang, Warenausgang, Mindestbestand-Logik)

---

## 4. Echtes COBOL migrieren

Jetzt wird eigener COBOL-Code aus dem Unternehmen migriert.

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

2. **Plan** — Plan Mode verwenden. Plan gruendlich pruefen.

3. **Implementieren** — Schrittweise, nicht den gesamten Code auf einmal.

4. **Verifizieren** — Tests die COBOL- und Java-Ergebnisse vergleichen.

> **Bei mehreren COBOL-Modulen:** Mit `/swarm` (Claude Code) koennen Module parallel migriert werden.
> Jedes Modul erhaelt einen eigenen Agent. Der Review-Agent prueft alle Ergebnisse.

---

## Abschluss-Retrospektive

### Erfahrungsaustausch
1. Welches COBOL-Modul wurde migriert?
2. Was hat die KI gut umgesetzt, was musste korrigiert werden?
3. Welche Bedeutung hatte die Analyse-Phase vor der Migration?
4. Was wuerde beim naechsten Migrations-Projekt anders gemacht werden?

### Gemeinsamer Abschluss
- `tasks/lessons.md` befuellen: Zentrale Erkenntnisse aus allen 3 Tagen
- Welche KI-Workflows werden in den Arbeitsalltag uebernommen?
- Naechste Schritte: Was wird als erstes im eigenen Team eingefuehrt?
