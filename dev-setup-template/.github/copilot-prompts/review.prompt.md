---
description: Code Review aller geänderten Dateien – Security, Qualität, Konventionen
---

Führe einen vollständigen Code Review der geänderten Dateien durch (`git diff`).

Prüfe auf:
1. Security-Probleme (Credentials, SQL-Injection, fehlende Auth) → BLOCKER
2. Code-Qualität (Methoden > 30 Zeilen, fehlende DTOs, fehlende Fehlerbehandlung)
3. Tests vorhanden für neue Features / Bug Fixes
4. Konventionen (Java Spring, Vue.js, DB-Standards aus copilot-instructions.md)

Output-Format:
```
## Code Review
### BLOCKER | ### Empfehlung | ### Ergebnis: APPROVED / CHANGES REQUESTED
```
Masterfrage: "Would a staff engineer approve this?"
