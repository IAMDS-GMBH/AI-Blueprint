# Lessons Learned – [PROJEKTNAME / TEAM]

> Dieses Dokument wird nach jeder Korrektur durch den User oder nach einem Fehler aktualisiert.
> Ziel: Denselben Fehler nie zweimal machen.
> Die KI liest diese Datei zu Beginn jeder neuen Session.

---

## Format

```
### [DATUM] – [Kurztitel des Fehlers]
**Was passierte:** [Beschreibung was schiefging]
**Root Cause:** [Eigentliche Ursache]
**Regel fuer die Zukunft:** [Konkrete Regel die diesen Fehler verhindert]
```

---

## Eintragen wenn:
- Der User eine Korrektur vornimmt
- Ein Deployment-Fehler aufgetreten ist
- Ein Missverstaendnis zu unnoetigem Aufwand gefuehrt hat
- Eine Annahme falsch war

---

## Lessons

### [DATUM] – Beispieleintrag (loeschen wenn erste echte Lesson eingetragen)
**Was passierte:** KI hat ein Feature implementiert das nicht im Scope war
**Root Cause:** Aufgabenbeschreibung war zu vage, KI hat eigene Annahmen getroffen
**Regel:** Immer zuerst /plan ausfuehren und Scope explizit definieren bevor Code geschrieben wird
