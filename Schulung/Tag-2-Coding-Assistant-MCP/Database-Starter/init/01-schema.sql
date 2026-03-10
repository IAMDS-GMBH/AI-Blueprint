CREATE TABLE IF NOT EXISTS kunden (
  id BIGSERIAL PRIMARY KEY,
  kundennummer TEXT UNIQUE NOT NULL,
  firmenname TEXT NOT NULL,
  kontakt_name TEXT NOT NULL,
  email TEXT UNIQUE,
  telefon TEXT,
  ust_id TEXT,
  zahlungsziel_tage INTEGER NOT NULL DEFAULT 14,
  status TEXT NOT NULL DEFAULT 'aktiv' CHECK (status IN ('aktiv', 'inaktiv')),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS produkte (
  id BIGSERIAL PRIMARY KEY,
  sku TEXT UNIQUE NOT NULL,
  bezeichnung TEXT NOT NULL,
  einheit TEXT NOT NULL DEFAULT 'Stk',
  nettopreis NUMERIC(12, 2) NOT NULL,
  steuersatz NUMERIC(5, 2) NOT NULL DEFAULT 19.00,
  aktiv BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS rechnungen (
  id BIGSERIAL PRIMARY KEY,
  rechnungsnummer TEXT UNIQUE NOT NULL,
  kunden_id BIGINT NOT NULL REFERENCES kunden(id),
  rechnungsdatum DATE NOT NULL,
  faellig_am DATE NOT NULL,
  status TEXT NOT NULL DEFAULT 'offen' CHECK (status IN ('offen', 'teilbezahlt', 'bezahlt', 'storniert', 'ueberfaellig')),
  waehrung CHAR(3) NOT NULL DEFAULT 'EUR',
  zwischensumme_netto NUMERIC(14, 2) NOT NULL DEFAULT 0,
  steuerbetrag NUMERIC(14, 2) NOT NULL DEFAULT 0,
  gesamtsumme_brutto NUMERIC(14, 2) NOT NULL DEFAULT 0,
  bemerkung TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS rechnungspositionen (
  id BIGSERIAL PRIMARY KEY,
  rechnung_id BIGINT NOT NULL REFERENCES rechnungen(id) ON DELETE CASCADE,
  positionsnr INTEGER NOT NULL,
  produkt_id BIGINT NOT NULL REFERENCES produkte(id),
  beschreibung TEXT NOT NULL,
  menge NUMERIC(12, 2) NOT NULL,
  einzelpreis_netto NUMERIC(12, 2) NOT NULL,
  steuersatz NUMERIC(5, 2) NOT NULL,
  rabatt_prozent NUMERIC(5, 2) NOT NULL DEFAULT 0,
  positionssumme_netto NUMERIC(14, 2) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  UNIQUE (rechnung_id, positionsnr)
);

CREATE TABLE IF NOT EXISTS zahlungen (
  id BIGSERIAL PRIMARY KEY,
  rechnung_id BIGINT NOT NULL REFERENCES rechnungen(id) ON DELETE CASCADE,
  zahlungsdatum DATE NOT NULL,
  betrag NUMERIC(14, 2) NOT NULL,
  zahlungsart TEXT NOT NULL CHECK (zahlungsart IN ('ueberweisung', 'lastschrift', 'karte', 'bar')),
  referenz TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  UNIQUE (rechnung_id, zahlungsdatum, betrag, zahlungsart)
);

CREATE TABLE IF NOT EXISTS mahnungen (
  id BIGSERIAL PRIMARY KEY,
  rechnung_id BIGINT NOT NULL REFERENCES rechnungen(id) ON DELETE CASCADE,
  mahnstufe SMALLINT NOT NULL CHECK (mahnstufe BETWEEN 1 AND 3),
  mahn_datum DATE NOT NULL,
  gebuehr NUMERIC(10, 2) NOT NULL DEFAULT 0,
  gesendet_an TEXT NOT NULL,
  status TEXT NOT NULL DEFAULT 'versendet' CHECK (status IN ('versendet', 'zugestellt', 'offen')),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  UNIQUE (rechnung_id, mahnstufe)
);

CREATE INDEX IF NOT EXISTS idx_rechnungen_kunden_id ON rechnungen(kunden_id);
CREATE INDEX IF NOT EXISTS idx_rechnungen_status ON rechnungen(status);
CREATE INDEX IF NOT EXISTS idx_rechnungen_faellig_am ON rechnungen(faellig_am);
CREATE INDEX IF NOT EXISTS idx_positionen_rechnung_id ON rechnungspositionen(rechnung_id);
CREATE INDEX IF NOT EXISTS idx_zahlungen_rechnung_id ON zahlungen(rechnung_id);
CREATE INDEX IF NOT EXISTS idx_mahnungen_rechnung_id ON mahnungen(rechnung_id);
