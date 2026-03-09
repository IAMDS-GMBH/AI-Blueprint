# /review – Code Review

Fuehre einen vollstaendigen Code Review der geaenderten Dateien durch.

## Ablauf

1. Alle geaenderten Dateien mit `git diff` identifizieren
2. Fuer jede Datei pruefen:
   - Entspricht der Code den Konventionen aus CLAUDE.md?
   - Gibt es Security-Probleme (keine Credentials, keine SQL-Injection, keine XSS)?
   - Sind neue Methoden/Funktionen laenger als 30 Zeilen? Falls ja: Refactoring vorschlagen.
   - Sind Tests vorhanden fuer neue Features/Bugfixes?
   - Gibt es unnoetige Abhaengigkeiten oder Over-Engineering?
3. Ergebnis als strukturierten Report ausgeben

## Output-Format

```
## Code Review

### Kritisch (muss vor Merge behoben werden)
- [Datei:Zeile] Problem + Vorschlag

### Empfehlung (sollte behoben werden)
- [Datei:Zeile] Problem + Vorschlag

### OK
- Alle weiteren geprueften Bereiche sind sauber

### Zusammenfassung
[1-2 Saetze: Merge-ready? Was muss noch passieren?]
```

## Constraints
- Keine Code-Aenderungen vornehmen, nur reviewen und berichten
- Bei kritischen Security-Problemen: Sofort als BLOCKER markieren
- "Would a staff engineer approve this?" als Masterfrage
