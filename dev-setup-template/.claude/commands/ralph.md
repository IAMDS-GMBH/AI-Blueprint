# /ralph – Iterativer Verbesserungs-Loop

Nimm die zuletzt generierte Ausgabe (Code, Dokument, Plan) und verbessere sie iterativ,
bis alle Qualitaetskriterien erfuellt sind. Nicht vorher fertig melden.

## Loop-Ablauf

### Runde 1 – Erstausgabe analysieren
Pruefe die aktuelle Ausgabe gegen diese Kriterien:

1. **Vollstaendig** – Alle Anforderungen aus der Aufgabe abgedeckt?
2. **Korrekt** – Syntaktisch und logisch fehlerfrei?
3. **Konventionen** – Entspricht Java/Vue/DB-Standards aus CLAUDE.md?
4. **Sicher** – Keine Security-Luecken (Credentials, SQL-Injection, XSS)?
5. **Minimal** – Keine unnötigen Änderungen ausserhalb des Scopes?
6. **Elegant** – Wuerde ein Staff Engineer das abnehmen?

### Runde 2 – Verbesserungen umsetzen
Fuer jedes fehlgeschlagene Kriterium: Fix umsetzen, nicht nur beschreiben.

### Runde 3 – Finale Verifikation
Alle Kriterien nochmals pruefen. Erst wenn alle gruen: fertig melden.

## Output-Format

```
## Ralph-Loop – Verifikation

### Runde 1
| Kriterium | Status | Anmerkung |
|-----------|--------|-----------|
| Vollstaendig | ✅ / ❌ | ... |
| Korrekt | ✅ / ❌ | ... |
| Konventionen | ✅ / ❌ | ... |
| Sicher | ✅ / ❌ | ... |
| Minimal | ✅ / ❌ | ... |
| Elegant | ✅ / ❌ | ... |

### Verbesserungen
[Falls Runde 1 Fehler hatte: was wurde geaendert]

### Ergebnis
[Kurze Zusammenfassung – bereit fuer Review / was noch offen ist]
```

## Wann einsetzen
- Nach komplexen Feature-Implementierungen vor dem Commit
- Wenn eine Loesung "irgendwie funktioniert" aber noch nicht sauber wirkt
- Vor PR-Erstellung als letzter Selbst-Check
- Als Alternative zu `/review` wenn kein externer Review gewuenscht ist
