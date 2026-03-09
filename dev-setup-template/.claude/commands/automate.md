# /automate – Automatisierungspotenzial analysieren

Analysiere einen beschriebenen Prozess oder Workflow und erstelle einen konkreten Automatisierungsplan.

## Ablauf

1. Den Prozess vollstaendig verstehen (ggf. nachfragen)
2. Jeden manuellen Schritt identifizieren
3. Automatisierungspotenzial bewerten (HOCH / MITTEL / NIEDRIG)
4. Konkrete Implementierungsvorschlaege machen
5. Aufwand vs. Nutzen abschaetzen

## Output-Format

```
## Automatisierungsanalyse: [PROZESSNAME]

### Aktueller Prozess (manuell)
| Schritt | Haeufigkeit | Dauer | Fehleranfaellig? |
|---------|-------------|-------|-----------------|
| ...     | ...         | ...   | ...             |

### Automatisierungspotenzial
| Schritt | Potenzial | Tool/Ansatz | Aufwand |
|---------|-----------|-------------|---------|
| ...     | HOCH      | n8n + Claude| 2h      |

### Empfohlene Implementierung (Prioritaet)
1. [Schritt mit hoechstem ROI]: [Konkrete Umsetzung]
2. ...

### Zeitersparnis (geschaetzt)
- Aktuell: X Stunden/Woche manuell
- Nach Automatisierung: Y Minuten/Woche
- ROI-Zeitpunkt: [nach X Wochen amortisiert]

### Naechster Schritt
[Konkrete erste Aktion um anzufangen]
```

## Constraints
- Immer mit dem hoechsten ROI anfangen, nicht mit dem technisch Interessantesten
- Automatisierungslevel-Ziel: Level 3-4 (KI fuehrt aus, Mensch verifiziert)
- Kosten/Komplexitaet realistisch einschaetzen
