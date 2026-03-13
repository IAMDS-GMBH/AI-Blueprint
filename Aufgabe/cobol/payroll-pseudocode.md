# Pseudo-Code: PAYROLL
**Zweck:** Berechnet Nettogehalt aus Bruttogehalt abzueglich Steuer und Sozialabgaben
**Quelle:** payroll.cbl

---

## Datenstrukturen

### Eingabedaten (Input)
| Feld | Typ | Beschreibung |
|------|-----|-------------|
| Mitarbeiter-ID (WS-EMPLOYEE-ID) | Ganzzahl (6 Stellen) | Eindeutige Mitarbeiternummer |
| Bruttogehalt (WS-GROSS-SALARY) | Dezimalzahl (7 Vorkomma, 2 Nachkomma) | Monatliches Bruttogehalt |
| Steuersatz (WS-TAX-RATE) | Dezimalzahl (2 Vorkomma, 2 Nachkomma) | Steuersatz in Prozent |
| Sozialabgabensatz (WS-SOCIAL-RATE) | Dezimalzahl (2 Vorkomma, 2 Nachkomma) | Sozialabgabensatz in Prozent |
| Bonus (WS-BONUS) | Dezimalzahl (5 Vorkomma, 2 Nachkomma) | Zusaetzlicher Bonus |

### Berechnungsvariablen (intern)
| Feld | Typ | Beschreibung |
|------|-----|-------------|
| Steuerbasis (WS-TAXABLE-BASE) | Dezimalzahl (7 Vorkomma, 2 Nachkomma) | Zu versteuernder Betrag |
| Steuerbetrag (WS-TAX-AMOUNT) | Dezimalzahl (7 Vorkomma, 2 Nachkomma) | Berechnete Steuer |
| Sozialabgabenbetrag (WS-SOCIAL-AMOUNT) | Dezimalzahl (7 Vorkomma, 2 Nachkomma) | Berechnete Sozialabgaben |
| Gesamtabzuege (WS-TOTAL-DEDUCT) | Dezimalzahl (7 Vorkomma, 2 Nachkomma) | Summe aller Abzuege |

### Ausgabedaten (Output)
| Feld | Typ | Beschreibung |
|------|-----|-------------|
| Nettogehalt (WS-NET-SALARY) | Dezimalzahl (7 Vorkomma, 2 Nachkomma) | Berechnetes Nettogehalt |
| Gezahlte Steuer (WS-TAX-PAID) | Dezimalzahl (7 Vorkomma, 2 Nachkomma) | Tatsaechlich abgefuehrte Steuer |
| Gezahlte Sozialabgaben (WS-SOCIAL-PAID) | Dezimalzahl (7 Vorkomma, 2 Nachkomma) | Tatsaechlich abgefuehrte Sozialabgaben |
| Statuscode (WS-STATUS-CODE) | Text (2 Zeichen) | Ergebnisstatus der Berechnung |

Benannte Werte:
- STATUS.ERFOLG = "OK"
- STATUS.FEHLER = "ER"

---

## Hauptlogik

```
PROGRAMM PAYROLL:
  1. rufe EINGABE-VALIDIEREN auf
  2. WENN Status = ERFOLG:
       a. rufe STEUERBASIS-BERECHNEN auf
       b. rufe ABZUEGE-BERECHNEN auf
       c. rufe NETTOGEHALT-BERECHNEN auf
       d. Status = ERFOLG
  3. Programm beenden
```

---

## Unterprogramme

### VALIDATE-INPUT — Eingabedaten pruefen
```
FUNKTION EINGABE-VALIDIEREN:
  WENN Bruttogehalt <= 0:
      Status = FEHLER
  SONST WENN Steuersatz > 50:
      Status = FEHLER
  SONST WENN Sozialabgabensatz > 30:
      Status = FEHLER
  SONST:
      Status = ERFOLG
```

### CALCULATE-TAXABLE-BASE — Steuerbasis ermitteln
```
FUNKTION STEUERBASIS-BERECHNEN:
  Steuerbasis = Bruttogehalt + Bonus
```

### CALCULATE-DEDUCTIONS — Abzuege berechnen
```
FUNKTION ABZUEGE-BERECHNEN:
  Steuerbetrag     = Steuerbasis * Steuersatz / 100       (gerundet)
  Sozialabgaben    = Bruttogehalt * Sozialabgabensatz / 100 (gerundet)
  Gesamtabzuege    = Steuerbetrag + Sozialabgaben
```

### CALCULATE-NET-SALARY — Nettogehalt ermitteln
```
FUNKTION NETTOGEHALT-BERECHNEN:
  Nettogehalt        = Steuerbasis - Gesamtabzuege
  Gezahlte Steuer    = Steuerbetrag
  Gezahlte Sozialab. = Sozialabgaben
```

---

## Zusammenfassung

Dieses Programm berechnet das Nettogehalt eines Mitarbeiters. Zunaechst wird die Eingabe validiert (Bruttogehalt muss positiv sein, Steuersatz maximal 50%, Sozialabgabensatz maximal 30%). Die Steuerbasis ergibt sich aus Bruttogehalt plus Bonus. Davon werden Steuer (auf die Steuerbasis) und Sozialabgaben (nur auf das Bruttogehalt, ohne Bonus) abgezogen, um das Nettogehalt zu ermitteln.
