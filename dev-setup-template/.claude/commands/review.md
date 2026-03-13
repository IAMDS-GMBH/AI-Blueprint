# /review – Code Review

Fuehre einen Code Review der geaenderten Dateien durch.
Keine Code-Aenderungen — nur reviewen und berichten.

## Ablauf
1. `git diff` — alle geaenderten Dateien identifizieren
   Basis: `git diff HEAD` (unstaged + staged) oder `git diff main...HEAD` (Branch-Review)
2. Jede Datei pruefen: Security, Qualitaet, Tests, Konventionen
3. Strukturierten Report ausgeben

## Output-Format
```
## Code Review

### BLOCKER (vor Merge beheben)
- [Datei:Zeile] Problem | Loesung

### Empfehlung
- [Datei:Zeile] Problem | Vorschlag

### Ergebnis
APPROVED / CHANGES REQUESTED — [1 Satz]
```

"Would a staff engineer approve this?" als Masterfrage.
