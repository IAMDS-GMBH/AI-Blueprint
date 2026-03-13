---
description: "Systematisches Debugging — Logs, Reproduce, Isolate, Fix, Test"
---

# Debugging — Systematisches Vorgehen

## Schritt-fuer-Schritt
1. **Symptom verstehen:** Was genau passiert? Fehlermeldung? Unerwartetes Verhalten?
2. **Reproduzieren:** Minimalen Reproduktionsfall finden — wann tritt es auf, wann nicht?
3. **Logs lesen:** Stack Trace, Application Logs, Browser Console — von unten nach oben
4. **Isolieren:** Binaere Suche — welche Aenderung hat es ausgeloest? git bisect wenn noetig
5. **Root Cause:** NICHT das Symptom fixen — die eigentliche Ursache finden
6. **Fix:** Minimaler Fix, nur die betroffene Stelle
7. **Test:** Regressionstest schreiben der den Bug reproduziert und den Fix verifiziert
8. **Dokumentieren:** Eintrag in tasks/lessons.md

## Haeufige Ursachen
- Null/Undefined: Fehlende Null-Checks, optionale Felder
- Timing: Race Conditions, fehlende await, Event-Reihenfolge
- State: Stale State, fehlende Invalidation, Cache-Probleme
- Config: Falsche Umgebung, fehlende ENV-Variablen
- Types: Falscher Cast, Type Mismatch (besonders JSON ↔ Objekt)

## Anti-Patterns
- NEVER "try-and-error" ohne Hypothese
- NEVER mehrere Dinge gleichzeitig aendern
- NEVER Fix ohne Regressionstest
- NEVER Symptom fixen statt Root Cause
