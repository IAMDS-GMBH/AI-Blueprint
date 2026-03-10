# Tag 3 – Theorie: Code-Modernisierung & COBOL-Migration mit KI

> Quelle: "The Code Modernization Playbook – Transforming Legacy Systems with AI" (Anthropic)

## Lernziele
- Den Business Case für KI-gestützte Modernisierung argumentieren können
- Die drei Säulen der Code-Modernisierung verstehen
- Einen strukturierten Migrations-Workflow mit KI anwenden
- COBOL-Datenstrukturen in Java-Äquivalente übersetzen
- Reifegradmodell auf das eigene Unternehmen anwenden

---

## 1. Warum Modernisierung jetzt? (Der Business Case)

### Die drei Blocker der digitalen Transformation

**Blocker 1: Sinkende Entwicklerproduktivität**
> "Der durchschnittliche Entwickler verbringt **17,3 Stunden pro Woche** mit technischen Schulden,
> schlechtem Code und Wartungsaufgaben – statt neue Features zu bauen." (McKinsey)

Das ist fast die Hälfte der Arbeitswoche. Ergebnis: Ein Kreislauf aus dem es ohne Intervention kaum einen Ausweg gibt – und Entwickler die die Motivation verlieren.

**Blocker 2: Talent-Knappheit**
- Junge Entwickler lernen moderne Sprachen und Cloud-Architekturen
- Positionen die COBOL, VB6 oder proprietäre Systeme erfordern werden immer schwerer zu besetzen
- Recruiting dauert Monate, Einarbeitung weitere Monate

**Blocker 3: Technische Schulden und Security-Risiken**
- **70%** aller Unternehmen nennen technische Schulden als primären Innovationsblocker (Protiviti)
- Im Finanzwesen: **82%** nennen Legacy-Code als Blocker für neue Features
- Ungepatzte Sicherheitslücken in Legacy-Systemen = wachsendes Angriffsziel
- Compliance-Anforderungen wachsen, Legacy-Systeme bleiben statisch

### Was das bedeutet
```
Ohne Modernisierung:
  Finanzdienstleister → Kein Echtzeit-Betrugsschutz auf Batch-Systemen
  Healthcare → Interoperabilitäts-Mandate nicht erfüllbar mit isolierten DBs
  Handel → Kundendatenschutz nicht umsetzbar auf nicht-dafür-gebautem Legacy-Stack
```

**Die Lösung:** Nicht alles auf einmal. Nicht "Lift-and-Shift" (Probleme ins Cloud-Regal stellen). Sondern: **Agentic Code Modernization** – KI-Agents die systematisch modernisieren während der Betrieb weiterläuft.

---

## 2. Die drei Säulen der Code-Modernisierung

### Säule 1: Architektur-Transformation
- Monolith → Microservices (unabhängig deploybar, unabhängig skalierbar)
- On-Premise → Cloud-native (Resilience, Circuit Breaker, Graceful Degradation)
- Batch-Processing → Echtzeit-Verarbeitung

### Säule 2: Tech-Stack-Modernisierung
- Veraltete Sprachen (COBOL, C, VB6, Perl) → Java, Python, TypeScript
- Proprietäre Frameworks → Open-Source Ökosystem
- Passive Systeme → Aktiv gewartet, regelmäßig gepatcht

### Säule 3: Entwicklungspraxis-Evolution
- Wasserfall → CI/CD-Pipelines
- Manuelles Testing → Automatisierte Test-Suites
- Monatliche Releases → Mehrfache Deployments pro Tag
- Wissen im Kopf einzelner Experten → Dokumentiert im Code + KI-lesbar

---

## 3. Real-World Beispiele: Was KI-Modernisierung konkret leistet

### Beispiel A: C → Java (Versicherungs-Claims-Processing)

**Original C-Code (Legacy):**
```c
typedef struct {
    int    claim_id;
    char   policy_number[11];
    double claim_amount;      /* FEHLER: double für Geld! */
    double deductible;
    double coverage_limit;
    char   status;
} ClaimRecord;

int main() {
    read_claim(&claim);
    validate_claim(&claim);
    calculate_payout(&claim);
    update_status(&claim);
}
```

**Migriert zu Java (mit Claude Code):**
```java
@Service
@Transactional
public class ClaimProcessingService {

    @Data @Builder
    public static class ClaimRecord {
        private Long claimId;
        private String policyNumber;
        private BigDecimal claimAmount;    // BigDecimal statt double!
        private BigDecimal deductible;
        private BigDecimal coverageLimit;
        private ClaimStatus approvalStatus; // Enum statt char
    }

    public ClaimRecord processClaim(ClaimRecord claim) {
        validateClaim(claim);
        calculatePayout(claim);
        updateClaimStatus(claim);
        publishClaimEvent(claim);          // Modern: Event-driven
        return claimRepository.save(claim);
    }
}
```

**Was KI geleistet hat:**
- Implizite `double`-Fehler erkannt und auf `BigDecimal` umgestellt
- Status-`char` in typsicheres `Enum` umgewandelt
- Business-Logik 1:1 erhalten, aber mit modernen Patterns (Transactions, Events)

**Was ihr zusätzlich prüfen müsst (KI vergisst das oft):**
```java
// Exception-Handling: Was passiert bei ungültigen Daten?
public ClaimRecord processClaim(ClaimRecord claim) {
    try {
        validateClaim(claim);  // → kann ValidationException werfen
        calculatePayout(claim);
        updateClaimStatus(claim);
        publishClaimEvent(claim);
        return claimRepository.save(claim);
    } catch (ValidationException e) {
        claim.setApprovalStatus(ClaimStatus.REJECTED);
        auditService.logRejection(claim, e.getMessage());  // Audit-Trail!
        return claimRepository.save(claim);
    }
    // @Transactional sorgt für Rollback bei unerwarteten Exceptions
}
```
→ KI generiert den Happy Path gut. Error Handling, Audit-Logging und Transaktions-Grenzen müsst ihr explizit anfordern.

---

### Beispiel B: Shell-Script/FTP → REST API (Logistik)

**Original (Legacy):**
```bash
# Cron: täglich 02:00 Uhr
ftp -inv $FTP_SERVER << EOF
user $FTP_USER $FTP_PASS
cd /inbound
mget PARTNER_SHIPMENT_*.csv
bye
EOF

for file in PARTNER_SHIPMENT_*.csv; do
  ./process_shipment.pl $file  # Kein Error-Handling!
  mv $file ./processed/
done
```
Ergebnis: 24-Stunden-Verzögerung bei Bestandsupdates, keine Fehlersignalisierung.

**Migriert zu TypeScript REST API:**
```typescript
this.router.post('/api/v2/shipments',
    this.authenticate,
    this.validateRequest(ShipmentSchema), // Input-Validierung
    this.rateLimit,
    async (req, res) => {
        const shipment = await this.createShipment(req.body, req.partner);
        res.status(201).json({ id: shipment.id, status: 'CREATED' });

        // Echtzeit-Benachrichtigung statt nächster Cron-Lauf
        this.eventBus.broadcast(shipment.id, { type: 'SHIPMENT_CREATED' });
    }
);

// Rückwärtskompatibilität: CSV-Upload bleibt erhalten
this.router.post('/api/v2/shipments/batch', upload.single('file'), ...);
```
Ergebnis: Echtzeit statt Batch, 90% weniger Integrationsfehler, aber Rückwärtskompatibilität erhalten.

**Was in Produktion noch dazu muss:**
```typescript
// Retry-Logik bei temporären Fehlern
@Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
async createShipment(data: ShipmentData): Promise<Shipment> { ... }

// Idempotency-Key gegen Doppelverarbeitung
this.router.post('/api/v2/shipments',
    this.checkIdempotencyKey,  // Header: X-Idempotency-Key
    ...
);
```

---

### Beispiel C: Monolith → Microservices (Finanzhandel)

```java
// Vorher: Alles in einem Transactionsblock – Settlement-Fehler blockieren Orders
@Transactional
public TradeResult executeTrade(TradeRequest request) {
    if (!validateOrder(request)) return TradeResult.rejected("...");
    RiskMetrics risk = calculateRisk(request, currentPosition);
    if (risk.getVaR() > getAccountLimit(...)) return TradeResult.rejected("...");
    Order order = new Order(request);
    db.insert("INSERT INTO orders VALUES (?)", order);
    Settlement settlement = settlementProcessor.process(order); // Bottleneck!
    return TradeResult.success(order.getId());
}
```

```java
// Nachher: Order-Service nur noch für Orders zuständig
@PostMapping
@CircuitBreaker(name = "order-creation")
public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
    Order order = orderRepository.save(Order.builder()...build());
    eventPublisher.publish(new OrderCreatedEvent(order)); // Risk-Check async
    return ResponseEntity.accepted().body(OrderResponse.from(order));
}

// Risk-Service reagiert unabhängig auf Events
@EventListener @Async
public void handleOrderCreated(OrderCreatedEvent event) {
    RiskAssessment assessment = performRiskAssessment(event.getOrder());
    publishEvent(assessment.isApproved()
        ? new RiskApprovedEvent(event.getOrderId())
        : new RiskRejectedEvent(event.getOrderId()));
}
```

---

## 4. ROI der Code-Modernisierung

| Dimension | Konkret |
|-----------|---------|
| **Geschwindigkeit** | Multi-Monats-Projekte → Wochen |
| **Sicherheit** | Aktiv gewartete Stacks, regelmäßige Patches |
| **Wissenserhalt** | KI generiert Dokumentation aus Legacy-Code (kein Knowhow-Verlust bei Personalwechsel) |
| **Finanziell** | +14% höherer Marktwert bei Unternehmen die modernisieren (Deloitte-Studie) |
| **Entwickler-Retention** | Moderne Stacks = bessere Kandidaten, weniger Burnout |

### Alternative: Mistral Devstral fuer Migrationen

Mistrals **Devstral 2** (123B Parameter) unterstützt COBOL explizit als Quellsprache und erreicht 72.2% auf SWE-bench Verified. Die **Mistral Vibe CLI** kann ähnlich wie Claude Code als Terminal-basierter Coding-Agent für Migration-Tasks eingesetzt werden (`curl -LsSf https://mistral.ai/vibe/install.sh | bash`). Besonders interessant für datenschutzkritische Migrationen: **Devstral Small 2** (24B) ist Apache 2.0 und läuft lokal via OLLAMA — der Legacy-Code bleibt vollständig on-premise. In der Praxis lassen sich Claude Code und Vibe CLI komplementär nutzen, z.B. Claude Code für die Hauptmigration und Devstral lokal für vertrauliche Quellen.

---

## 5. Der Phasenplan: Von Pilot zu Enterprise-Modernisierung

```
Woche 1-2:  Code-Archäologie
  → Niedrig-Risiko-System identifizieren (Batch-Report, Kalkulations-Engine)
  → Programm-Flows analysieren, Abhängigkeiten mappen, Business Rules dokumentieren

Woche 3-4:  Proof of Concept
  → KI analysiert Legacy-Code, extrahiert Business-Logik
  → Erste Code-Übersetzungen, Datenfluss-Diagramme
  → Side-by-Side: Legacy und Java laufen parallel, Outputs vergleichen

Woche 5-8:  Erste vollständige Migration
  → KI übersetzt Code, Tests für alle Edge Cases, API-Wrapper für Übergang
  → Parallel-Run: Beide Systeme, Outputs werden verglichen
  → Woche 8: Cutover – Legacy wird abgeschaltet

Monat 3+:   Skalierung
  → Bewährte Patterns wiederverwenden
  → Parallele Teams, gemeinsame Konvertierungs-Utilities
  → KI mit gesammeltem Projekt-Wissen: Anti-Patterns früh erkennen
```

**Goldene Regel:** Mit einem nicht-kritischen System starten. Vertrauen aufbauen. Dann skalieren.

---

## 6. Das Modernisierungs-Reifegradmodell

Wo steht euer Unternehmen heute?

| Reifegrad | Kennzeichen |
|-----------|-------------|
| **Ad hoc** | Legacy nur bei Notfällen angefasst. Kein Dokumentation, keine Tests, Wissen in den Köpfen einzelner Experten. |
| **Geplant** | Jährliche Migrationsprojekte, aber politisch getrieben statt ROI-getrieben. Jedes Projekt erfindet das Rad neu. |
| **Systematisch** | Dedizierte Teams, standardisierte Prozesse. KI-Tools analysieren kontinuierlich Legacy-Code und schlagen Refactorings vor. |
| **Optimiert** | KI-Agents scannen proaktiv und erstellen Modernisierungsvorschläge mit ROI-Analyse. Kontinuierliche Hintergrund-Modernisierung. |

**Ziel für euer Unternehmen:** Mindestens "Systematisch" – mit diesem Schulungs-Setup habt ihr die Grundlage.

---

## 7. COBOL-Basics für Nicht-COBOL-Entwickler

Ihr müsst kein COBOL können – aber diese Konzepte helfen:

### Struktur eines COBOL-Programms
```cobol
IDENTIFICATION DIVISION.    ← Programm-Metadaten
  PROGRAM-ID. MEINPROGRAMM.

DATA DIVISION.              ← Variablen-Deklarationen
  WORKING-STORAGE SECTION.
    01 CUSTOMER-RECORD.     ← Struct-ähnlich (Level-Numbers!)
       05 CUST-ID     PIC 9(5).    ← 5-stellige Zahl
       05 CUST-NAME   PIC X(30).   ← 30-Zeichen-String
       05 CUST-SALARY PIC 9(7)V99. ← Zahl mit 2 Dezimalstellen

PROCEDURE DIVISION.         ← Business-Logik (wie main() + Methoden)
  MAIN-LOGIC.
    PERFORM CALCULATE-TAX
    STOP RUN.
```

### Die häufigsten Fallen bei der Migration

| COBOL-Konzept | Java-Äquivalent | Falle |
|---------------|-----------------|-------|
| `PIC 9(5)` | `int` | Führende Nullen gehen verloren (z.B. Postleitzahl "01234") |
| `PIC 9(7)V99` | `BigDecimal` | **Niemals `double`!** Rundungsfehler bei Geldbeträgen |
| `PIC X(30)` | `String.trim()` | COBOL paddet mit Leerzeichen → immer `.trim()` aufrufen |
| Level-Numbers (01, 05, 10) | Verschachtelte Klassen / Records | Oft mehrere Ebenen tief |
| `PERFORM UNTIL` | `while`-Schleife | **Nicht** `for-each`! COBOL prüft Bedingung am Anfang |
| `COMPUTE` | Arithmetik | Dezimalstellenpräzision beachten |
| `COMP-3` (Packed Decimal) | `BigDecimal` | BCD-Format: 2 Ziffern pro Byte + Sign-Nibble |
| `SIGN IS SEPARATE` | Vorzeichen-Handling | Vorzeichen als eigenes Zeichen (+/-) gespeichert |
| `REDEFINES` | Union-ähnlich | Selber Speicher, andere Interpretation → verschiedene DTOs |

### Erweiterte COBOL-Konzepte die KI oft falsch migriert

**COPYBOOKS (wie C-Header-Dateien):**
```cobol
*--- COPY CUSTOMER-RECORD.CPY ---
  01 CUSTOMER-RECORD.
     05 CUST-ID      PIC 9(5).
     05 CUST-NAME    PIC X(30).
     05 CUST-ADDR    PIC X(50).
```
→ Werden in mehreren Programmen per `COPY CUSTOMER-RECORD` eingebunden
→ Java: Gemeinsames Record/DTO das alle Services nutzen
→ **KI-Falle:** KI sieht nur das aktuelle Programm, nicht die Copybooks → explizit mitgeben!

**FILE SECTION (Sequential File I/O):**
```cobol
FILE SECTION.
  FD CUSTOMER-FILE.
  01 CUSTOMER-REC    PIC X(85).  ← Fixed-Length Record, 85 Bytes
```
→ Java: `BufferedReader` mit Fixed-Width-Parsing oder Apache Commons FixedLengthReader
→ **KI-Falle:** KI migriert auf CSV-Parsing → falsch! COBOL-Dateien haben KEINE Delimiter

**COMP-3 (Packed Decimal) – das unterschätzte Problem:**
```cobol
  05 SALARY    PIC 9(7)V99 COMP-3.
  * Speichert 1234567.89 als: 01 23 45 67 89 0C (BCD + positive sign)
  * 5 Bytes statt 9 Bytes im Display-Format
```
→ Java: `BigDecimal` mit korrekter Scale (immer `.setScale(2, RoundingMode.HALF_UP)`)
→ **Achtung:** COBOL `ROUNDED` ist nicht immer `HALF_UP` – prüft die COBOL-Doku!

**COBOL ROUNDED vs. Java RoundingMode:**
```
COBOL ROUNDED (Standard)     = Java RoundingMode.HALF_UP     (0.5 → 1)
COBOL ROUNDED MODE NEAREST-EVEN = Java RoundingMode.HALF_EVEN (Banker's Rounding)
→ Wenn ihr nicht wisst welches: Testet mit bekannten Werten!
```

---

## 8. Der COBOL-Migrations-Workflow mit KI

### Schritt 1: Analysieren (erst verstehen, dann migrieren!)
```
"Analysiere diesen COBOL-Code:
1. Zweck des Programms?
2. Eingabedaten (Felder, Typen)?
3. Ausgabedaten?
4. Business-Logik in der PROCEDURE DIVISION?
Noch kein Java – nur die Analyse."
```

### Schritt 2: Fachlogik extrahieren
```
"Extrahiere die reine Fachlogik als Pseudocode auf Deutsch.
Markiere kritische Stellen wo Rundung oder Datentypen wichtig sind."
```

### Schritt 3: Java-Struktur planen
```
"Plane die Java-Klassen für diese Migration.
Stack: Java 17, Spring Boot, JPA
Zeige: Klassen, Methoden, Datentypen. Noch kein Code."
```

### Schritt 4: Implementieren
```
"Implementiere [spezifischen Teil].
- BigDecimal für alle Geldbeträge (niemals double)
- RoundingMode.HALF_UP (wie COBOL ROUNDED)
- Gleiche Ausgabe bei diesen Test-Inputs: [Beispiele]"
```

### Schritt 5: Verifikation (Chain-of-Verification erzwingen)
```
"[Deine Frage]

Danach:
1. Gib initiale Java-Implementierung
2. Erstelle 3-5 Testfälle die Fehler aufdecken könnten
   (Grenzwerte, Rundung, Null-Werte)
3. Führe Tests mental durch
4. Gib korrigierte finale Implementierung"
```

---

### Verifikationsstrategie: Wie prüft man ob die Migration korrekt ist?

**1. Output-Vergleich (Golden File Testing):**
```
COBOL-Programm mit Test-Input laufen lassen → Output speichern (= Golden File)
Java-Programm mit identischem Input laufen lassen → Output vergleichen
→ Byte-für-Byte muss identisch sein (bei Geldbeträgen: auf 2 Dezimalstellen)
```

**2. Testdaten-Strategie:**
```java
// Aus COBOL-Testfällen ableiten:
@ParameterizedTest
@CsvSource({
    "10000.00, 0.19, 1900.00",        // Normalfall
    "0.01, 0.19, 0.00",               // Rundung bei Kleinstbeträgen
    "9999999.99, 0.19, 1899999.9981",  // Maximaler Wert (PIC 9(7)V99)
    "0.00, 0.19, 0.00",               // Null-Betrag
})
void testTaxCalculation(BigDecimal amount, BigDecimal rate, BigDecimal expected) {
    assertEquals(expected, service.calculateTax(amount, rate));
}
```

**3. Grenzwerte die COBOL anders handelt als Java:**
- Negative Zahlen bei `PIC 9(...)` (unsigned) → Java `int` erlaubt negativ!
- Überlauf: COBOL schneidet ab, Java wirft Exception
- Leerzeichen in `PIC X(...)` → Java `null` vs. leerer String vs. Leerzeichen

---

## 9. Strangler Fig Pattern + Phased Cutover

```
Phase 1: Legacy läuft, Java-Service parallel aufbauen
Phase 2: Traffic-Routing – neue Anfragen an Java, COBOL als Fallback
Phase 3: Parallel-Run – beide Systeme laufen, Outputs vergleichen
Phase 4: Cutover – COBOL abschalten sobald Tests 100% bestehen
```

---

## 10. Swarm für große Migrationen

```
/swarm → Jedes COBOL-Modul = ein Agent

Runde 1 (parallel): Analyse + Java-Plan je Modul
Runde 2 (parallel): Implementierung je Modul
Runde 3: Integration + Code Review aller Ergebnisse
```

## 11. Ralph – Vollautonome iterative Migration

> **Konzept:** Statt einem langen Migrations-Chat → iterative Loops mit frischem Kontext pro Iteration.

**Das Problem das Ralph löst:**
```
Langer Migrations-Chat → Kontext wird voll → KI verliert Überblick → Qualität sinkt
```

**Wie Ralph bei COBOL-Migration hilft:**
1. Migrationsziele in `prd.json` definieren (Modul für Modul)
2. Ralph wählt nächstes Modul, startet frische Agent-Session
3. Migriert, führt Tests aus, committet bei Erfolg
4. Bei Fehler: analysiert Root Cause, korrigiert, testet erneut
5. Nächstes Modul – ohne Kontext-Ballast aus dem letzten

**Installation:**
```bash
/plugin install ralph@anthropic-agent-skills
```

**Wann sinnvoll:**
```
✓ Große COBOL-Codebase mit vielen unabhängigen Modulen
✓ Wenn die Migration Tage/Wochen dauert (nicht Stunden)
✗ Für die heutige Aufgabe (2 Module, überschaubar) → /swarm reicht
```

Mehr Details → `KI-Wissensbasis/15-Agent-Einsatz-im-Entwicklungsalltag.md` (Abschnitt 7)

---

## Heute: Ablauf

```
09:00–09:30  Warm-up + Recap Tag 2
09:30–12:00  Theorie (diese Datei) + Live-Demo: payroll.cbl analysieren
13:00–14:30  Hands-on: Synthetische Beispiele (payroll.cbl + inventory.cbl)
14:30–16:30  Hauptübung: Echtes COBOL eures Unternehmens
16:30–17:00  Abschluss-Präsentation + Gesamt-Retrospektive 3 Tage
```

**Bitte mitbringen:**
- 1-2 COBOL-Module aus eurem Unternehmen (anonymisiert)
- Bekannte Test-Inputs und erwartete Outputs

---

## 12. Ausblick: Wie geht es nach der Schulung weiter?

### Nächste Schritte für euer Unternehmen

**RAG – KI mit Unternehmenswissen füttern**
```
Euer Chatbot von Tag 1 kann mit RAG (Retrieval Augmented Generation) erweitert werden:
→ Interne Dokumente, Wikis, Confluence-Seiten → Vektor-Datenbank
→ KI beantwortet Fragen basierend auf euren echten Daten
→ Tools: Google Notebook LM, CLaRa (Apple), GraphRAG
```

**Workflow-Automatisierung mit n8n + KI**
```
Wiederkehrende Prozesse automatisieren:
→ Claude Opus beschreibt den Workflow in natürlicher Sprache
→ n8n führt ihn aus (JSON-Import)
→ Beispiel: Ticket erstellt → KI analysiert → Branch erstellt → Tests laufen
```

**Automatisierungs-Reifegrad:**

| Level | Beschreibung | Beispiel |
|-------|-------------|----------|
| 1 – Manuell | Alles von Hand | Developer schreibt, testet, deployed manuell |
| 2 – KI-unterstützt | KI schlägt vor, Mensch entscheidet | Copilot Autocomplete, Code Review |
| 3 – KI-orchestriert | KI führt aus, Mensch verifiziert | Claude Code mit /plan + Auto-Accept |
| 4 – KI-autonom | KI arbeitet selbstständig (nicht-kritische Tasks) | Ralph-Loop, Swarm für Migrations |
| 5 – Multi-Agent | Mehrere KI-Agents koordinieren sich | Enterprise-Modernisierung im Factory-Modus |

**Ziel:** Level 3 für den Alltag, Level 4 für Routine-Tasks, Level 5 für große Modernisierungen.

> Weiterführende Themen in der `KI-Wissensbasis/`: RAG (13), Automatisierung (09), Strategie (14)
