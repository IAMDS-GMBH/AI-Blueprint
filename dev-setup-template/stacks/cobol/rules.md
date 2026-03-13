---
description: "COBOL Migration Regeln"
globs: ["**/*.cob", "**/*.cbl", "**/*.cpy", "**/*.CBL"]
alwaysApply: false
---

# COBOL Migration

## Analyse-Vorgehen
1. COBOL-Programm Struktur verstehen (IDENTIFICATION, ENVIRONMENT, DATA, PROCEDURE DIVISION)
2. COPYBOOK-Abhaengigkeiten identifizieren
3. Datenstrukturen (01-Level, REDEFINES, OCCURS) → Java-Klassen mappen
4. Business-Logik in PROCEDURE DIVISION → Java-Services extrahieren
5. PERFORM-Hierarchie → Methodenaufrufe

## Mapping COBOL → Java
- WORKING-STORAGE → Klassen-Felder oder lokale Variablen
- COPYBOOK → Shared DTOs oder Enums
- SECTION/PARAGRAPH → Methoden
- PERFORM → Methodenaufruf
- PIC 9(n) → int/long/BigDecimal (je nach Groesse)
- PIC X(n) → String
- PIC 9(n)V9(m) → BigDecimal (NEVER double fuer Dezimalzahlen)
- REDEFINES → Vererbung oder Union-Type
- OCCURS → List oder Array
- 88-Level → Enum oder boolean

## Qualitaet
- 1:1-Transliteration vermeiden — Java-idiomatisch umschreiben
- Business-Regeln als eigene Service-Methoden extrahieren
- COBOL-Kommentare als Javadoc uebernehmen wo sinnvoll
- Testfaelle aus COBOL-Testdaten ableiten

## Testing der Migration
- Testfaelle aus COBOL-Testdaten ableiten (ACCEPT/DISPLAY Werte, JCL-Eingaben)
- Aequivalenzklassen: Gleiche Eingabe in COBOL und Java → gleiche Ausgabe
- Grenzwerte testen: PIC 9(n) Maximalwerte, COMP-3 Randbereiche
- Regressionstests: Vor und nach Migration identische Ergebnisse
- NEVER Migration ohne begleitende Tests abschliessen

## Verbote
- NEVER COBOL-Struktur 1:1 in Java abbilden (God Classes)
- NEVER Goto-Logik nachbauen — strukturierte Kontrollfluss nutzen
- NEVER COMP-3 packed decimal ignorieren — korrekt nach BigDecimal konvertieren
- NEVER Batch-JCL ignorieren — Scheduling-Logik dokumentieren
