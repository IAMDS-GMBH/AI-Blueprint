# Pseudo-Code: INVENTORY
**Zweck:** Lagerverwaltung — Verwaltet Lagerbestand, prueft Mindestbestand und loest Nachbestellung aus wenn noetig
**Quelle:** inventory.cbl

---

## Datenstrukturen

### Artikel-Datensatz (entspricht einer DB-Zeile / Entity)
| Feld | Typ | Beschreibung |
|------|-----|-------------|
| ArtikelNr (WS-ARTICLE-ID) | Ganzzahl (8 Stellen) | Eindeutige Artikel-ID |
| ArtikelName (WS-ARTICLE-NAME) | Text (50 Zeichen) | Bezeichnung des Artikels |
| AktuellerBestand (WS-CURRENT-STOCK) | Ganzzahl (6 Stellen) | Aktueller Lagerbestand |
| Mindestbestand (WS-MIN-STOCK) | Ganzzahl (6 Stellen) | Minimaler Lagerbestand bevor nachbestellt wird |
| Nachbestellmenge (WS-REORDER-QTY) | Ganzzahl (6 Stellen) | Standard-Bestellmenge bei Nachbestellung |
| Stueckpreis (WS-UNIT-PRICE) | Dezimalzahl (5 Vorkomma, 2 Nachkomma) | Preis pro Einheit |

### Buchungs-Eingabe (Lagerbewegung)
| Feld | Typ | Beschreibung |
|------|-----|-------------|
| Buchungstyp (WS-TRANS-TYPE) | Text (1 Zeichen) | Art der Lagerbewegung |
| Buchungsmenge (WS-TRANS-QTY) | Ganzzahl (6 Stellen) | Anzahl der bewegten Einheiten |

Benannte Werte:
- Buchungstyp.WARENEINGANG = "I"
- Buchungstyp.WARENAUSGANG = "O"

### Ergebnis (Verarbeitungsresultat)
| Feld | Typ | Beschreibung |
|------|-----|-------------|
| NeuerBestand (WS-NEW-STOCK) | Ganzzahl (6 Stellen) | Bestand nach der Buchung |
| NachbestellungNoetig (WS-ORDER-NEEDED) | Text (1 Zeichen) | Flag ob nachbestellt werden muss |
| Bestellmenge (WS-ORDER-QTY) | Ganzzahl (6 Stellen) | Zu bestellende Menge |
| ErgebnisCode (WS-RESULT-CODE) | Text (2 Zeichen) | Status der Verarbeitung |

Benannte Werte:
- NachbestellungNoetig.JA = "Y"
- NachbestellungNoetig.NEIN = "N"
- ErgebnisCode.ERFOLG = "OK"
- ErgebnisCode.BESTAND_UNZUREICHEND = "IS"
- ErgebnisCode.UNGUELTIGE_BUCHUNG = "IT"

---

## Hauptlogik

```
PROGRAMM INVENTORY:
  1. rufe BuchungValidieren auf
  2. WENN ErgebnisCode = ERFOLG:
       PRUEFE:
         FALL Buchungstyp = WARENEINGANG → rufe WareneingangVerarbeiten auf
         FALL Buchungstyp = WARENAUSGANG → rufe WarenausgangVerarbeiten auf
       rufe NachbestellungPruefen auf
  3. Programm beenden
```

---

## Unterprogramme

### BuchungValidieren (VALIDATE-TRANSACTION) — Prueft ob die Lagerbewegung gueltig ist
```
FUNKTION BuchungValidieren:
  WENN Buchungsmenge <= 0:
    ErgebnisCode = UNGUELTIGE_BUCHUNG
  SONST WENN Buchungstyp weder WARENEINGANG noch WARENAUSGANG:
    ErgebnisCode = UNGUELTIGE_BUCHUNG
  SONST:
    ErgebnisCode = ERFOLG
```

### WareneingangVerarbeiten (PROCESS-INBOUND) — Erhoeht den Lagerbestand
```
FUNKTION WareneingangVerarbeiten:
  NeuerBestand = AktuellerBestand + Buchungsmenge
  AktuellerBestand = NeuerBestand
  ErgebnisCode = ERFOLG
```

### WarenausgangVerarbeiten (PROCESS-OUTBOUND) — Reduziert den Lagerbestand wenn genug vorhanden
```
FUNKTION WarenausgangVerarbeiten:
  WENN Buchungsmenge > AktuellerBestand:
    ErgebnisCode = BESTAND_UNZUREICHEND
  SONST:
    NeuerBestand = AktuellerBestand - Buchungsmenge
    AktuellerBestand = NeuerBestand
    ErgebnisCode = ERFOLG
```

### NachbestellungPruefen (CHECK-REORDER) — Loest Nachbestellung aus wenn Bestand unter Minimum
```
FUNKTION NachbestellungPruefen:
  WENN AktuellerBestand < Mindestbestand:
    NachbestellungNoetig = JA
    Bestellmenge = Nachbestellmenge - AktuellerBestand
    WENN Bestellmenge < Nachbestellmenge:
      Bestellmenge = Nachbestellmenge
  SONST:
    NachbestellungNoetig = NEIN
    Bestellmenge = 0
```

---

## Zusammenfassung

Dieses Programm implementiert eine einfache Lagerverwaltung mit Warenein- und -ausgang. Bei jedem Warenausgang wird geprueft, ob genuegend Bestand vorhanden ist — falls nicht, wird die Buchung mit dem Status BESTAND_UNZUREICHEND abgelehnt. Nach jeder erfolgreichen Buchung prueft das Programm, ob der aktuelle Bestand unter den Mindestbestand gefallen ist, und setzt in diesem Fall ein Nachbestellungs-Flag mit der entsprechenden Bestellmenge (mindestens die konfigurierte Standard-Nachbestellmenge).
