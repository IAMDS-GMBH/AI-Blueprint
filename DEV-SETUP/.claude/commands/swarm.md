# /swarm – Grosse Aufgabe auf mehrere Agents verteilen

Zerlege eine komplexe Aufgabe in unabhaengige Teilaufgaben und verteile sie auf spezialisierte Agents.

## Ablauf

1. Aufgabe analysieren und in parallele, unabhaengige Teilaufgaben zerlegen
2. Fuer jede Teilaufgabe den passenden Agent bestimmen
3. Swarm-Plan ausgeben (Uebersicht was welcher Agent macht)
4. Auf Freigabe warten
5. Nach Freigabe: Jeden Agent mit seiner Aufgabe und dem passenden Kontext starten
6. Ergebnisse zusammenfuehren
7. /review ausfuehren um das Gesamtergebnis zu pruefen

## Swarm-Plan Format

```
## Swarm-Plan: [AUFGABENNAME]

### Aufgaben-Aufteilung

| Agent | Typ | Aufgabe | Abhaengig von |
|-------|-----|---------|---------------|
| Agent 1 | Dev | [Beschreibung] | – |
| Agent 2 | Dev | [Beschreibung] | – |
| Agent 3 | Test | [Beschreibung] | Agent 1, Agent 2 |
| Agent 4 | Docs | [Beschreibung] | Agent 1 |
| Agent 5 | Review | Gesamtreview | Alle |

### Reihenfolge
Runde 1 (parallel): Agent 1, Agent 2
Runde 2 (parallel): Agent 3, Agent 4
Runde 3: Agent 5 (Review)

### Geschaetzter Gesamtaufwand
[X Agents × Y Minuten = Z Minuten]

---
[ ] Swarm freigegeben
```

## Constraints
- Jede Teilaufgabe muss unabhaengig ausfuehrbar sein
- Abhaengigkeiten explizit dokumentieren
- Review-Agent immer am Ende einsetzen
- Nie mehr als 5 parallele Agents (Qualitaet leidet)
