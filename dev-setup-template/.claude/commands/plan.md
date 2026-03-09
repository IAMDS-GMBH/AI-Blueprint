# /plan – Aufgabe planen bevor sie ausgefuehrt wird

Erstelle einen detaillierten Ausfuehrungsplan fuer die beschriebene Aufgabe.
Dieser Plan wird in tasks/todo.md gespeichert und erst nach Freigabe ausgefuehrt.

## Ablauf

1. Aufgabe verstehen und in Teilschritte zerlegen
2. Abhaengigkeiten zwischen Schritten identifizieren
3. Risiken und Fallstricke benennen
4. Plan in tasks/todo.md schreiben
5. Auf Freigabe warten – NICHT automatisch mit der Ausfuehrung beginnen

## Output-Format (in tasks/todo.md schreiben)

```markdown
# Plan: [Aufgabenname]
Datum: [DATUM]
Status: WARTET AUF FREIGABE

## Ziel
[Was soll erreicht werden?]

## Schritte
- [ ] Schritt 1: [Beschreibung] – [Betroffene Dateien/Systeme]
- [ ] Schritt 2: ...
- [ ] Schritt N: Verifikation – [Wie wird Erfolg bewiesen?]

## Risiken
- [Risiko 1]: [Gegenmassnahme]

## Nicht im Scope
- [Was wird bewusst NICHT angefasst]

## Freigabe
[ ] Plan freigegeben von: ___________
```

## Constraints
- Mindestens 1 Verifikationsschritt am Ende
- "Nicht im Scope" explizit benennen um Scope Creep zu verhindern
- Immer den minimalen Code-Impact anstreben
