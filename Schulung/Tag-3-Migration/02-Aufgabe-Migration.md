# Tag 3 – Aufgabe: COBOL-Migration zu Java Spring

**Zeit:** 13:00–16:30 Uhr
**Warm-up:** Synthetische Beispiele aus `cobol-beispiele/`
**Hauptübung:** Euer eigenes COBOL

---

## KI-Tool-Ablauf: Wann greift welches Tool?

```
Analyse     COBOL lesen    → Claude Code / Copilot Chat     – KI liest COBOL, erklaert auf Deutsch
Kritisch    Stellen finden → Claude Code / Copilot Chat     – KI identifiziert Migrationsrisiken
Plan        Struktur        → Claude Code /plan oder Chat    – Java-Klassen planen (noch kein Code)
Impl.       Java schreiben → Claude Code (Dev Agent)         – Code generieren, Schritt fuer Schritt
Tests       JUnit 5        → Claude Code (Test Agent)        – Tests die COBOL und Java vergleichen
Verif.      Chain-of-Verif → Claude Code                    – KI prueft sich selbst durch Testfaelle
Grosse Mig. Swarm          → /swarm                         – Mehrere COBOL-Module parallel migrieren
```

**Besonderheit Tag 3: KI als Uebersetzer**
```
Schritt 1: COBOL → Erklaerung auf Deutsch   (KI als Dolmetscher)
Schritt 2: COBOL → Pseudocode               (KI als Analytiker)
Schritt 3: Pseudocode → Java-Struktur       (KI als Architekt)
Schritt 4: Java-Struktur → Java-Code        (KI als Entwickler)
Schritt 5: Java-Code → JUnit 5 Tests        (KI als Tester)
```

**Wann /swarm?** Bei mehreren Modulen parallel:
```
/swarm: Modul A → Agent 1 | Modul B → Agent 2 | Modul C → Agent 3
        → Review Agent prueft alle Ergebnisse → 3x schneller
```

---

## Warm-up (13:00–14:30): Synthetische Beispiele

### Team A: payroll.cbl – Gehaltsberechnung
Datei: `cobol-beispiele/payroll.cbl`

**Schritt 1: Analysieren (20 Min)**

```
Analysiere diesen COBOL-Code aus payroll.cbl.

Erkläre:
1. Was berechnet dieses Programm?
2. Welche Eingabefelder gibt es (mit Datentypen)?
3. Welche Ausgabe wird erzeugt?
4. Welche Business-Logik steckt in der PROCEDURE DIVISION?
   Erkläre jeden Berechnungsschritt in Deutsch.

Noch kein Java. Nur die Analyse.
```

**Schritt 2: Kritische Stellen identifizieren (10 Min)**

```
Welche Stellen in diesem COBOL-Code sind bei der Java-Migration kritisch?
- Welche Datentypen brauchen besondere Aufmerksamkeit?
- Wo könnte es Rundungsprobleme geben?
- Was ist die COBOL-Semantik von [spezifische Stelle]?
```

**Schritt 3: Java-Struktur planen (10 Min)**

```
Plane die Java-Klassen für die Migration von payroll.cbl.
Stack: Java 17, Spring Boot, kein Framework-Overhead nötig (Plain Java Service reicht)

Zeige:
- Welche Datenklassen (Records oder POJOs)?
- Welche Service-Methoden?
- Welche Datentypen für Geldbeträge?

Noch kein Code.
```

**Schritt 4: Implementieren (30 Min)**

```
Implementiere die Java-Klassen für die Gehaltsberechnung aus payroll.cbl.

Besondere Anforderungen:
- BigDecimal für ALLE Geldbeträge und Prozentsätze
- RoundingMode.HALF_UP für Rundung (wie COBOL)
- Gleiche Ausgabe bei diesen Test-Inputs:
  - Bruttogehalt: 3000.00, Steuersatz: 19%, Sozialabgaben: 20%
  - Bruttogehalt: 5000.00, Steuersatz: 25%, Sozialabgaben: 20%

Format: PayrollInput Record, PayrollResult Record, PayrollService Klasse
```

**Schritt 5: Tests (20 Min)**

```
Schreibe JUnit 5 Tests die beweisen dass der Java PayrollService
dasselbe Ergebnis liefert wie das COBOL-Programm.

Nutze die Test-Inputs aus Schritt 4.
Teste zusätzlich: Bruttogehalt 0, negativer Betrag (sollte Exception werfen).
```

---

### Team B: inventory.cbl – Lagerverwaltung
Datei: `cobol-beispiele/inventory.cbl`

**Schritt 1: Analysieren (20 Min)**

```
Analysiere diesen COBOL-Code aus inventory.cbl.

Erkläre:
1. Was verwaltet dieses Programm?
2. Welche Datenstrukturen gibt es (mit Level-Numbers)?
3. Welche Operationen sind möglich?
4. Was sind die Geschäftsregeln (Mindestbestand, Bestellung, etc.)?
```

**Schritt 2 + 3: Kritische Stellen + Plan (20 Min)**

Gleicher Ansatz wie Team A, angepasst auf Lagerverwaltung.

**Schritt 4: Implementieren (30 Min)**

```
Implementiere die Java-Klassen für die Lagerverwaltung aus inventory.cbl.

Stack: Java 17, Spring Boot, JPA/Hibernate, PostgreSQL

Besondere Anforderungen:
- JPA-Entity für das Lager-Item (entspricht der COBOL-Datenstruktur)
- InventoryService mit Methoden für alle COBOL-PERFORM-Paragraphen
- Bestelllogik: Wenn Bestand < Mindestbestand → automatisch nachbestellen

Format: Entity, DTO, Service-Interface + Impl, Flyway-Migration für die Tabelle
```

**Schritt 5: Tests + Review (20 Min)**

```
Schreibe Tests für den InventoryService.
Test-Cases:
- Wareneingang: Bestand erhöht sich korrekt
- Warenausgang: Bestand sinkt, Exception wenn nicht genug
- Mindestbestand unterschritten: Bestellung wird ausgelöst
```

---

## Hauptübung (14:30–16:30): Echtes COBOL

Jetzt bringt ihr euer eigenes COBOL-Modul ein.

### Vorbereitungs-Checkliste (vor dem Start)
- [ ] COBOL-Datei anonymisiert (keine echten Kunden-/Mitarbeiterdaten)
- [ ] Bekannte Test-Inputs und erwartete Outputs bereit
- [ ] Verständnis was das Modul tut (auch grob reicht)

### Empfohlener Workflow

**1. KI gibt Kontext (15 Min)**

```
Ich habe ein COBOL-Programm aus unserem Unternehmen.
Es ist Teil unseres [System-Name] Systems.

[COBOL-CODE EINFÜGEN]

Erkläre mir:
1. Was macht dieses Programm in einfachen Worten?
2. Welche Daten werden ein- und ausgegeben?
3. Gibt es Abhängigkeiten zu externen Systemen (CICS, DB2, Dateien)?
4. Was sind die kritischsten Berechnungen / Geschäftsregeln?
```

**2. Migrationsplan erstellen (10 Min)**

```
Erstelle einen Migrationsplan für dieses COBOL-Programm nach Java Spring Boot.

Berücksichtige:
- Externe Abhängigkeiten: [was ihr aus Schritt 1 erfahren habt]
- Priorität: Fachlogik korrekt migrieren, Infrastruktur kann Placeholder sein

Format: Welche Klassen, welche Schritte, was zuerst?
Noch kein Code.
```

**3. Schrittweise implementieren**

Nutzt den Plan aus Schritt 2. Implementiert Abschnitt für Abschnitt.
Nutzt Chain-of-Verification bei kritischer Berechnungslogik.

**4. Verifikation**

```
Schreibe Tests die COBOL- und Java-Ergebnisse vergleichen.

Bekannte Test-Inputs und erwartete Outputs:
[Eure bekannten Testfälle einfügen]

Wenn die COBOL-Ausgabe nicht bekannt ist: Erkläre wie wir die COBOL-Ausgabe
manuell ermitteln können um die Tests zu definieren.
```

---

## Abschluss-Retrospektive (16:30–17:00)

### Jedes Team präsentiert (je 10 Min):
1. Welches COBOL-Modul habt ihr migriert?
2. Was hat KI gut gemacht, was musste korrigiert werden?
3. Welchen Prompt habt ihr am meisten genutzt?
4. Was würdet ihr beim nächsten Migrations-Projekt anders machen?

### Gemeinsam (10 Min):
- `tasks/lessons.md` befüllen: Was haben wir in 3 Tagen gelernt?
- Welche KI-Workflows nehmt ihr mit in den Arbeitsalltag?
- Nächste Schritte: Was wollt ihr als erstes in eurem Team einführen?
