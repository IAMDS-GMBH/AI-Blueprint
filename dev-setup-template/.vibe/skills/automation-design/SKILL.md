---
name: automation-design
description: Wird geladen wenn Automatisierungen entworfen, n8n-Workflows gebaut oder repetitive Prozesse analysiert werden sollen.
license: MIT
compatibility: Python 3.12+
user-invocable: true
allowed-tools:
  - read_file
  - write_file
  - grep
  - bash
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

### Vorlage fuer n8n-Workflows

```
Ich brauche einen n8n-Workflow der folgendes tut:

TRIGGER: [Was loest den Workflow aus?]
INPUT: [Woher kommen die Daten?]
VERARBEITUNG: [Was soll passieren?]
OUTPUT: [Was soll das Ergebnis sein?]
FEHLERFALL: [Was passiert wenn etwas schiefgeht?]
```

---

## Automatisierungsbausteine (Stack-spezifisch)

### Backend (Java Spring)
- **Scheduled Jobs:** `@Scheduled(cron = "0 0 8 * * *")`
- **Event-driven:** Spring Events (`ApplicationEventPublisher`)
- **Batch-Processing:** Spring Batch fuer grosse Datenmengen
- **Webhook-Empfang:** `@PostMapping("/webhooks/...")` mit HMAC-Verifikation

### Datenbank
- Oracle: DBMS_SCHEDULER fuer DB-seitige Jobs
- PostgreSQL: pg_cron Extension
- Beide: Flyway fuer automatische Schema-Migrationen

### Frontend (Vue.js)
- Automatische Retry-Logik via Axios Interceptors
- Optimistic UI Updates mit Rollback bei Fehler
- Polling via `useIntervalFn` (VueUse)

---

## Haeufige Automatisierungs-Patterns

### Pattern 1: Daten-Sync
```
[Quelle A] → [n8n Trigger] → [Transformation] → [Ziel B]
```

### Pattern 2: Benachrichtigungen
```
[DB-Event / API-Event] → [Spring Event] → [Notification Service] → [Slack/E-Mail]
```

### Pattern 3: Report-Automatisierung
```
[Schedule: Montag 08:00] → [Daten aus DB] → [KI generiert Analyse] → [PDF/Slack]
```

### Pattern 4: Code-Quality-Automatisierung
```
[PR erstellt] → [CI Action] → [KI Code Review] → [Review-Comment auf PR]
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
