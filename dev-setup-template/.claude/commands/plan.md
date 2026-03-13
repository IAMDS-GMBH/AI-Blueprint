# /plan – Aufgabe planen bevor sie ausgefuehrt wird

Aufgabe: $ARGUMENTS

Wenn keine Aufgabe angegeben wurde, frage den User was geplant werden soll.

Erstelle einen Ausfuehrungsplan und speichere ihn in tasks/todo.md.
IMPORTANT: NICHT automatisch ausfuehren — auf Freigabe warten.

## Ablauf
1. Aufgabe in Teilschritte zerlegen
2. Abhaengigkeiten identifizieren
3. Risiken benennen
4. Plan in tasks/todo.md schreiben

## Output-Format (in tasks/todo.md)
```markdown
# Plan: [Aufgabenname]
Datum: [DATUM] | Status: WARTET AUF FREIGABE

## Ziel
[Was soll erreicht werden?]

## Schritte
- [ ] Schritt 1: [Beschreibung] – [Dateien]
- [ ] Verifikation: [Wie wird Erfolg bewiesen?]

## Risiken
- [Risiko]: [Gegenmassnahme]

## Nicht im Scope
- [Was wird bewusst NICHT angefasst]
```
