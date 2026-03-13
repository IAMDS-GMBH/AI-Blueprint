---
description: "Universelle Code-Qualitaet und Security — gilt IMMER"
alwaysApply: true
---

# Code-Qualitaet & Security

## Qualitaet
- Methoden/Funktionen max. 30 Zeilen
- Kommentare nur wenn Logik nicht selbsterklaerend
- Tests sind Pflicht fuer neue Features und Bug Fixes
- Keine Magic Numbers — benannte Konstanten
- Keine neuen Abhaengigkeiten ohne Freigabe

## Security — vor jedem Commit pruefen
- NEVER Credentials, Tokens, API-Keys im Code oder Git
- NEVER SQL-String-Konkatenation — nur PreparedStatements
- NEVER User-Daten unescaped rendern (XSS)
- NEVER sensible Daten in Logs (Passwoerter, PII)
- NEVER Wildcard-CORS in Produktion
- Input IMMER validieren an API-Grenzen

Unsicheren Code sofort melden und Fix vorschlagen.
