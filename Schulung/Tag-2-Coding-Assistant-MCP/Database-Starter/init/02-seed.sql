INSERT INTO kunden (kundennummer, firmenname, kontakt_name, email, telefon, ust_id, zahlungsziel_tage, status)
SELECT
  FORMAT('K%04s', gs),
  FORMAT('Musterfirma %s GmbH', gs),
  FORMAT('Kontakt %s', gs),
  FORMAT('kontakt%s@beispiel.de', gs),
  FORMAT('+49-30-555-%04s', gs),
  FORMAT('DE%09s', 100000000 + gs),
  CASE WHEN gs % 3 = 0 THEN 30 ELSE 14 END,
  'aktiv'
FROM generate_series(1, 20) AS gs
ON CONFLICT (kundennummer) DO NOTHING;

INSERT INTO produkte (sku, bezeichnung, einheit, nettopreis, steuersatz, aktiv)
SELECT
  FORMAT('SKU-%03s', gs),
  FORMAT('Leistungspaket %s', gs),
  'Stk',
  ROUND((35 + gs * 6.5)::NUMERIC, 2),
  CASE WHEN gs % 4 = 0 THEN 7.00 ELSE 19.00 END,
  TRUE
FROM generate_series(1, 15) AS gs
ON CONFLICT (sku) DO NOTHING;

INSERT INTO rechnungen (rechnungsnummer, kunden_id, rechnungsdatum, faellig_am, status, bemerkung)
SELECT
  FORMAT('R-2026-%04s', gs),
  ((gs - 1) % 20) + 1,
  DATE '2026-01-01' + (gs * 2),
  DATE '2026-01-01' + (gs * 2) + INTERVAL '14 day',
  'offen',
  FORMAT('Automatisch erzeugte Beispielrechnung %s', gs)
FROM generate_series(1, 35) AS gs
ON CONFLICT (rechnungsnummer) DO NOTHING;

INSERT INTO rechnungspositionen (
  rechnung_id,
  positionsnr,
  produkt_id,
  beschreibung,
  menge,
  einzelpreis_netto,
  steuersatz,
  rabatt_prozent,
  positionssumme_netto
)
SELECT
  r.id,
  pos.pos_nr,
  ((r.id + pos.pos_nr - 1) % 15) + 1,
  FORMAT('Position %s fuer %s', pos.pos_nr, r.rechnungsnummer),
  (1 + ((r.id + pos.pos_nr) % 5))::NUMERIC(12, 2),
  p.nettopreis,
  p.steuersatz,
  CASE WHEN (r.id + pos.pos_nr) % 6 = 0 THEN 10.00 ELSE 0.00 END,
  ROUND(
    (1 + ((r.id + pos.pos_nr) % 5)) * p.nettopreis *
    (1 - (CASE WHEN (r.id + pos.pos_nr) % 6 = 0 THEN 10.00 ELSE 0.00 END) / 100.0),
    2
  )
FROM rechnungen AS r
CROSS JOIN (VALUES (1), (2)) AS pos(pos_nr)
JOIN produkte AS p
  ON p.id = ((r.id + pos.pos_nr - 1) % 15) + 1
ON CONFLICT (rechnung_id, positionsnr) DO NOTHING;

WITH calc AS (
  SELECT
    rp.rechnung_id,
    ROUND(SUM(rp.positionssumme_netto), 2) AS netto,
    ROUND(SUM(rp.positionssumme_netto * rp.steuersatz / 100.0), 2) AS steuer
  FROM rechnungspositionen AS rp
  GROUP BY rp.rechnung_id
)
UPDATE rechnungen AS r
SET
  zwischensumme_netto = c.netto,
  steuerbetrag = c.steuer,
  gesamtsumme_brutto = ROUND(c.netto + c.steuer, 2)
FROM calc AS c
WHERE r.id = c.rechnung_id;

INSERT INTO zahlungen (rechnung_id, zahlungsdatum, betrag, zahlungsart, referenz)
SELECT
  r.id,
  r.rechnungsdatum + INTERVAL '7 day',
  ROUND(r.gesamtsumme_brutto * 0.40, 2),
  'ueberweisung',
  FORMAT('PAY-%s-1', r.rechnungsnummer)
FROM rechnungen AS r
WHERE r.id % 2 = 0
ON CONFLICT (rechnung_id, zahlungsdatum, betrag, zahlungsart) DO NOTHING;

INSERT INTO zahlungen (rechnung_id, zahlungsdatum, betrag, zahlungsart, referenz)
SELECT
  r.id,
  r.rechnungsdatum + INTERVAL '21 day',
  ROUND(r.gesamtsumme_brutto * 0.60, 2),
  'lastschrift',
  FORMAT('PAY-%s-2', r.rechnungsnummer)
FROM rechnungen AS r
WHERE r.id % 4 = 0
ON CONFLICT (rechnung_id, zahlungsdatum, betrag, zahlungsart) DO NOTHING;

WITH zahlungsstand AS (
  SELECT
    r.id AS rechnung_id,
    COALESCE(SUM(z.betrag), 0) AS bezahlt
  FROM rechnungen AS r
  LEFT JOIN zahlungen AS z ON z.rechnung_id = r.id
  GROUP BY r.id
)
UPDATE rechnungen AS r
SET status = CASE
  WHEN zs.bezahlt >= r.gesamtsumme_brutto THEN 'bezahlt'
  WHEN zs.bezahlt > 0 THEN 'teilbezahlt'
  WHEN r.faellig_am < CURRENT_DATE THEN 'ueberfaellig'
  ELSE 'offen'
END
FROM zahlungsstand AS zs
WHERE r.id = zs.rechnung_id;

INSERT INTO mahnungen (rechnung_id, mahnstufe, mahn_datum, gebuehr, gesendet_an, status)
SELECT
  r.id,
  CASE
    WHEN CURRENT_DATE - r.faellig_am > 45 THEN 3
    WHEN CURRENT_DATE - r.faellig_am > 30 THEN 2
    ELSE 1
  END AS mahnstufe,
  CURRENT_DATE - ((ROW_NUMBER() OVER (ORDER BY r.faellig_am))::INT % 5),
  CASE
    WHEN CURRENT_DATE - r.faellig_am > 45 THEN 15.00
    WHEN CURRENT_DATE - r.faellig_am > 30 THEN 8.00
    ELSE 3.00
  END,
  k.email,
  'versendet'
FROM rechnungen AS r
JOIN kunden AS k ON k.id = r.kunden_id
WHERE r.status = 'ueberfaellig'
ORDER BY r.faellig_am
LIMIT 10
ON CONFLICT (rechnung_id, mahnstufe) DO NOTHING;
