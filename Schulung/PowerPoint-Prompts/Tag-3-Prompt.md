# PowerPoint-Prompt: Tag 3 — Code-Modernisierung & COBOL-Migration

## Anweisung
Erstelle eine professionelle PowerPoint-Präsentation für Tag 3 einer KI-Schulung. Thema: Legacy-Code-Modernisierung und COBOL-Migration mit KI. Der Tag besteht aus Theorie (Vormittag), Warm-up mit synthetischen COBOL-Beispielen und einer Hauptübung mit echtem Unternehmens-COBOL. Zielgruppe: 6 Software-Entwickler.

## Design-Vorgaben
- Professionell, modern, clean — gleiches Design wie Tag 1 und 2
- Wenig Text pro Folie (max. 5-6 Bulletpoints)
- Große Schrift (min. 24pt Body, 36pt Titel)
- Diagramme und Tabellen bevorzugen
- Farbschema: Dunkelblau (#1a365d), helles Grau (#f7fafc), Akzent Orange (#ed8936)
- Sprache: Deutsch

## Folienstruktur (30 Folien)

### TEIL 1: Theorie — Warum und Wie (Folien 1-19)

### Folie 1: Titelfolie
- Titel: "Tag 3: Legacy-Code modernisieren mit KI"
- Untertitel: "AI-Blueprint Schulung"
- Quelle: "Basierend auf: The Code Modernization Playbook (Anthropic)"

### Folie 2: Agenda
- Zeitplan:
  - 09:00-09:30 — Recap Tag 2 + Warm-up
  - 09:30-12:00 — Theorie: Business Case, Methodik, COBOL-Grundlagen
  - 13:00-14:30 — Warm-up: Synthetische Beispiele (payroll.cbl, inventory.cbl)
  - 14:30-16:30 — Hauptübung: Echtes COBOL eures Unternehmens
  - 16:30-17:00 — Retrospektive + Gesamtfazit 3 Tage

### Folie 3: Blocker 1 — Sinkende Produktivität
- Große Zahl: "17,3 Stunden pro Woche"
- Untertitel: "...verbringt der durchschnittliche Entwickler mit Tech Debt und Wartung"
- "Das ist fast die Hälfte der Arbeitswoche." (McKinsey)
- Grafik: Kreislauf aus Wartung → weniger Innovation → mehr Tech Debt

### Folie 4: Blocker 2 — Talent-Knappheit
- 3 Bulletpoints:
  - Junge Entwickler lernen moderne Sprachen (nicht COBOL)
  - Legacy-Positionen: Monate Recruiting + Monate Einarbeitung
  - Wissen in den Köpfen einzelner Experten (Bus-Faktor!)

### Folie 5: Blocker 3 — Security & Compliance
- Zahlen: 70% nennen Tech Debt als Innovationsblocker (Protiviti), 82% im Finanzwesen
- Ungepatche Sicherheitslücken + wachsende Compliance-Anforderungen
- "Legacy-Systeme bleiben statisch, Anforderungen wachsen"

### Folie 6: Die Lösung — Agentic Code Modernization
- Kernaussage: "Nicht Lift-and-Shift. Sondern: KI-Agents die systematisch modernisieren während der Betrieb weiterläuft."
- Grafik: Altes System → KI analysiert → Neues System (parallel)

### Folie 7: Die 3 Säulen der Modernisierung
- 3-Spalten-Grafik:
  1. Architektur-Transformation: Monolith → Microservices, Batch → Echtzeit
  2. Tech-Stack-Modernisierung: COBOL/C/VB6 → Java/Python/TypeScript
  3. Entwicklungspraxis-Evolution: Wasserfall → CI/CD, manuell → automatisiert

### Folie 8: Praxis-Beispiel A — C → Java
- Zwei Code-Blöcke nebeneinander (vereinfacht):
  - Links: C-Struct mit double claim_amount (FEHLER markiert)
  - Rechts: Java mit BigDecimal claimAmount (FIX markiert)
- Was KI geleistet hat: double→BigDecimal erkannt, char→Enum, Events ergänzt

### Folie 9: Praxis-Beispiel B — Shell/FTP → REST API
- Zwei Boxen:
  - Vorher: Cron + FTP + Perl-Script (24h Verzögerung, kein Error-Handling)
  - Nachher: REST API + Echtzeit-Events + Rückwärtskompatibilität
- Ergebnis: Echtzeit statt Batch, 90% weniger Integrationsfehler

### Folie 10: Praxis-Beispiel C — Monolith → Microservices
- Diagramm: Ein großer Block (Monolith) → 3 unabhängige Services (Order, Risk, Settlement)
- "Settlement-Fehler blockieren nicht mehr Orders"
- Event-driven Architecture

### Folie 11: ROI der Modernisierung
- Tabelle:
  | Dimension | Konkret |
  | Geschwindigkeit | Multi-Monats-Projekte → Wochen |
  | Sicherheit | Aktiv gewartete Stacks, regelmäßige Patches |
  | Wissenserhalt | KI generiert Doku aus Legacy-Code |
  | Finanziell | +14% Marktwert (Deloitte) |
  | Entwickler-Retention | Moderne Stacks = bessere Kandidaten |

### Folie 12: Modernisierungs-Reifegradmodell
- 4-Stufen-Treppe (aufsteigend):
  1. Ad hoc: Legacy nur bei Notfällen, keine Doku
  2. Geplant: Jährliche Projekte, politisch getrieben
  3. Systematisch: Dedizierte Teams, KI-Tools, standardisiert
  4. Optimiert: KI-Agents scannen proaktiv, ROI-getrieben
- Pfeil: "Ziel: Mindestens Stufe 3"

### Folie 13: Der Phasenplan
- Timeline-Grafik (horizontal):
  - Woche 1-2: Code-Archäologie (System identifizieren, analysieren)
  - Woche 3-4: Proof of Concept (KI analysiert, erste Übersetzungen)
  - Woche 5-8: Vollständige Migration (Parallel-Run, Cutover)
  - Monat 3+: Skalierung (Factory-Modus, parallele Teams)
- Goldene Regel: "Mit nicht-kritischem System starten"

### Folie 14: COBOL-Grundlagen für Java-Entwickler
- Struktur eines COBOL-Programms (3 Divisions):
  - IDENTIFICATION DIVISION → Programm-Metadaten
  - DATA DIVISION → Variablen (wie Struct)
  - PROCEDURE DIVISION → Business-Logik (wie main())
- Kleines Code-Beispiel (5 Zeilen COBOL)

### Folie 15: COBOL → Java Fallen
- Tabelle:
  | COBOL | Java | Falle! |
  | PIC 9(5) | int | Führende Nullen verloren |
  | PIC 9(7)V99 | BigDecimal | NIEMALS double! |
  | PIC X(30) | String | COBOL paddet mit Leerzeichen |
  | Level-Numbers | Verschachtelte Klassen | Oft mehrere Ebenen tief |
  | PERFORM UNTIL | while-Schleife | NICHT for-each |

### Folie 16: Migrations-Workflow mit KI
- 5-Schritte-Diagramm (horizontal):
  1. Analysieren (erst verstehen!)
  2. Fachlogik extrahieren (Pseudocode)
  3. Java-Struktur planen (Klassen, Methoden, Typen)
  4. Implementieren (BigDecimal, RoundingMode.HALF_UP)
  5. Verifizieren (Chain-of-Verification)

### Folie 17: KI-Tool pro Migrationsschritt
- Tabelle:
  | Schritt | Tool |
  | Analyse | Claude Code Chat |
  | Kritische Stellen | Claude Code Chat |
  | Planung | /plan oder Chat |
  | Implementierung | Claude Code Dev Agent |
  | Tests | Claude Code Test Agent |
  | Verifikation | Chain-of-Verification |
  | Große Migration | /swarm |

### Folie 18: Strangler Fig Pattern
- Diagramm (4 Phasen):
  - Phase 1: Legacy läuft, Java parallel aufbauen
  - Phase 2: Traffic-Routing (neue Anfragen → Java, Fallback → COBOL)
  - Phase 3: Parallel-Run (beide Systeme, Outputs vergleichen)
  - Phase 4: Cutover (COBOL abschalten)

### Folie 19: Swarm + Ralph für große Migrationen
- Swarm: Jedes COBOL-Modul = ein Agent (parallel)
  - Runde 1: Analyse + Plan | Runde 2: Implementierung | Runde 3: Review
- Ralph: Iterative Loops mit frischem Kontext pro Modul
  - Löst das Problem: "Langer Chat → Kontext voll → Qualität sinkt"

---

### TEIL 2: Übungen (Folien 20-30)

### Folie 20: Überleitung — Jetzt seid ihr dran!
- "Von der Theorie in die Praxis"
- Warm-up: Synthetische Beispiele (13:00-14:30)
- Hauptübung: Euer echtes COBOL (14:30-16:30)

### Folie 21: Warm-up — Team-Aufteilung
- Team A: payroll.cbl (Gehaltsabrechnung)
- Team B: inventory.cbl (Lagerverwaltung)
- Zeitrahmen: 90 Minuten

### Folie 22: Team A — payroll.cbl
- 5 Schritte:
  1. COBOL-Programm analysieren lassen
  2. Kritische Stellen identifizieren (Rundung, Datentypen)
  3. Java-Klassenstruktur planen
  4. Implementieren mit BigDecimal + RoundingMode.HALF_UP
  5. JUnit 5 Tests mit bekannten Test-Inputs schreiben

### Folie 23: Team B — inventory.cbl
- 5 Schritte:
  1. Datenstrukturen und Business Rules analysieren
  2. Java/JPA Entity-Struktur planen
  3. InventoryService mit Auto-Reorder-Logik implementieren
  4. Tests für Warehouse-Operationen schreiben
  5. Edge Cases: Negativbestand, Grenzwerte

### Folie 24: Hauptübung — Euer echtes COBOL
- "Bringt euer anonymisiertes COBOL-Modul mit"
- Vorgehensweise:
  1. KI erklärt was das Programm macht
  2. Migrationsplan erstellen
  3. Schritt für Schritt implementieren
  4. Mit bekannten Test-Inputs verifizieren

### Folie 25: Tipps für die Hauptübung
- "Wenn das Modul zu groß ist: /swarm verwenden"
- Chain-of-Verification für kritische Berechnungen
- Jeden Schritt committen (nicht alles auf einmal)
- Bei Problemen: Neuen Chat starten (frischer Kontext)

### Folie 26: Retrospektive — 3 Fragen
- Was hat die KI gut gemacht?
- Wo musste ich eingreifen / korrigieren?
- Was nehme ich für mein Team mit?

### Folie 27: Ausblick — Wie geht es weiter?
- RAG: Chatbot mit Unternehmenswissen (Confluence, Wikis)
- n8n + KI: Workflow-Automatisierung (Ticket → Analyse → Branch → Tests)
- Maturity-Levels: Von "KI schlägt vor" zu "KI arbeitet autonom"

### Folie 28: Automatisierungs-Reifegrad (NEU)
- Stufenmodell:
  | Level | Beschreibung |
  | 1 – Manuell | Alles von Hand |
  | 2 – KI-unterstützt | KI schlägt vor, Mensch entscheidet |
  | 3 – KI-orchestriert | KI führt aus, Mensch verifiziert (Ziel Alltag) |
  | 4 – KI-autonom | KI arbeitet selbstständig (Routine-Tasks) |
  | 5 – Multi-Agent | Mehrere KI-Agents koordinieren sich (Enterprise) |

### Folie 29: Gesamtzusammenfassung 3 Tage
- Tag 1: Greenfield — Von der Idee zur App mit KI
  - LLM-Grundlagen, KERNEL-Framework, Chatbot gebaut
- Tag 2: Coding Assistant + MCP
  - Kontext-Hierarchie, MCP-Server für Oracle DB, Security
- Tag 3: Code-Modernisierung
  - Business Case, COBOL → Java, Swarm & Ralph
- "Ihr habt jetzt die Werkzeuge — nutzt sie!"

### Folie 30: Nächste Schritte für euer Team
- 5 konkrete Actions:
  1. CLAUDE.md + dev-setup-template ins eigene Projekt übernehmen (setup.sh)
  2. lessons.md im Team starten
  3. KERNEL-Framework als Prompting-Standard etablieren
  4. Erstes Legacy-Modul mit KI modernisieren (Pilot)
  5. Weiterführend: KI-Wissensbasis im Repo als Referenz nutzen
- Kontakt-Platzhalter für Trainer
