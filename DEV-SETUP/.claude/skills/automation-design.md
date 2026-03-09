---
description: Wird geladen wenn Automatisierungen entworfen, n8n-Workflows gebaut oder repetitive Prozesse analysiert werden sollen.
---

# Skill: Automation Design

## Automatisierungs-Maturity-Modell

```
Level 1: Manuell         → Mensch macht alles
Level 2: KI-unterstuetzt → KI schlaegt vor, Mensch entscheidet
Level 3: KI-orchestriert → KI fuehrt aus, Mensch verifiziert
Level 4: KI-autonom      → KI arbeitet selbstaendig, Mensch ueberwacht
Level 5: Multi-Agent     → Agent-Schwarm, Mensch setzt nur Ziele
```

**Ziel:** Level 3-4 fuer Routine-Tasks, Level 2-3 fuer kritische Aufgaben.

---

## n8n Workflow Design

### Grundprinzip
1. Trigger definieren (Webhook, Schedule, Event)
2. Datenquelle anbinden
3. Transformation/Logik via Claude Opus beschreiben
4. Ausgabe/Aktion definieren
5. Error-Handling und Retry-Logik

### Vorlage fuer Claude-generierte n8n-Workflows

```
Ich brauche einen n8n-Workflow der folgendes tut:

TRIGGER: [Was loest den Workflow aus? z.B. "taeglich um 08:00 Uhr"]
INPUT: [Woher kommen die Daten? z.B. "PostgreSQL-Tabelle orders"]
VERARBEITUNG: [Was soll passieren? z.B. "Alle offenen Bestellungen aelter als 48h identifizieren"]
OUTPUT: [Was soll das Ergebnis sein? z.B. "Slack-Nachricht an #operations"]
FEHLERFALL: [Was passiert wenn etwas schiefgeht? z.B. "E-Mail an admin@company.com"]
```

---

## Automatisierungsbausteine (Stack-spezifisch)

### Backend-Automatisierungen (Java Spring)
- **Scheduled Jobs:** `@Scheduled(cron = "0 0 8 * * *")` fuer regelmaessige Tasks
- **Event-driven:** Spring Events (`ApplicationEventPublisher`) fuer lose Kopplung
- **Batch-Processing:** Spring Batch fuer grosse Datenmengen
- **Webhook-Empfang:** `@PostMapping("/webhooks/...")` mit HMAC-Verifikation

### Datenbank-Automatisierungen
- Oracle: DBMS_SCHEDULER fuer DB-seitige Jobs
- PostgreSQL: pg_cron Extension fuer regelmaessige Queries
- Beide: Flyway fuer automatische Schema-Migrationen bei Deployment

### Frontend-Automatisierungen (Vue.js)
- Automatische Retry-Logik via Axios Interceptors
- Optimistic UI Updates mit Rollback bei Fehler
- Polling via `useIntervalFn` (VueUse)

---

## Haeufige Automatisierungs-Patterns

### Pattern 1: Daten-Sync
```
[Quelle A] → [n8n Trigger] → [Transformation] → [Ziel B]
Beispiel: Oracle → n8n → PostgreSQL (Data Warehouse)
```

### Pattern 2: Benachrichtigungen
```
[DB-Event / API-Event] → [Spring Event] → [Notification Service] → [Slack/E-Mail]
```

### Pattern 3: Report-Automatisierung
```
[Schedule: Montag 08:00] → [Daten aus DB] → [Claude generiert Analyse] → [PDF/Slack]
```

### Pattern 4: Code-Quality-Automatisierung
```
[PR erstellt] → [GitHub Action] → [Claude Code Review] → [Review-Comment auf PR]
```

---

## ROI-Berechnung Vorlage

```
Manueller Aufwand:    X Minuten × Y mal/Woche × 52 Wochen = Z Stunden/Jahr
Stundensatz:          [PLACEHOLDER] EUR/h
Jaehrliche Kosten:    Z × Stundensatz = EUR/Jahr

Automatisierung:
  Implementierung:    A Stunden × Stundensatz = EUR einmalig
  Wartung:            B Stunden/Jahr × Stundensatz = EUR/Jahr

Break-Even:           Implementierung / (Jaehrliche Einsparung - Wartungskosten) = X Monate
```
