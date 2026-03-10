# Lokale PostgreSQL-Showcase-Datenbank (Abrechnung & Fakturierung)

Diese Datenbank laeuft lokal in Docker und stellt ein realistisches B2B-Beispiel fuer Abrechnung und Fakturierung bereit.

## Inhalt

- PostgreSQL 15 in Docker Compose
- Initialisierung per SQL-Skripten aus `init/`
- Datensatz mit 6 Tabellen und ca. 175 Datensaetzen
- Direkte lokale Verbindung fuer Lernende (z. B. DBeaver, `psql`, MCP-Server)

## Voraussetzungen

- Docker Desktop (oder Docker Engine + Compose Plugin)
- Freier lokaler Port `5432`

## Schnellstart

1. In den Ordner wechseln:
   ```bash
   cd Schulung/Tag-2-Coding-Assistant-MCP/database
   ```
2. Umgebungsdatei anlegen:
   ```bash
   cp .env.example .env
   ```
3. Datenbank starten:
   ```bash
   docker compose up -d
   ```
4. Optional Logs pruefen:
   ```bash
   docker compose logs -f postgres
   ```

Die Initialisierung laeuft beim ersten Start automatisch ueber:

- `init/01-schema.sql`
- `init/02-seed.sql`

## Verbindungsdaten (Default)

- Host: `localhost`
- Port: `5432`
- Datenbank: `abrechnung`
- User: `training`
- Passwort: `training`

Beispiel mit `psql`:

```bash
psql "postgresql://training:training@localhost:5432/abrechnung"
```

## Datenmodell

- `kunden`: Stammdaten von B2B-Kunden
- `produkte`: Abrechenbare Leistungen
- `rechnungen`: Kopfzeilen der Rechnungen
- `rechnungspositionen`: Positionen je Rechnung
- `zahlungen`: Zahlungseingaenge
- `mahnungen`: Mahnprozess fuer ueberfaellige Rechnungen

## Verifikation (SQL)

```sql
SELECT 'kunden' AS tabelle, COUNT(*) AS anzahl FROM kunden
UNION ALL
SELECT 'produkte', COUNT(*) FROM produkte
UNION ALL
SELECT 'rechnungen', COUNT(*) FROM rechnungen
UNION ALL
SELECT 'rechnungspositionen', COUNT(*) FROM rechnungspositionen
UNION ALL
SELECT 'zahlungen', COUNT(*) FROM zahlungen
UNION ALL
SELECT 'mahnungen', COUNT(*) FROM mahnungen;
```

```sql
SELECT status, COUNT(*)
FROM rechnungen
GROUP BY status
ORDER BY status;
```

```sql
SELECT
  r.rechnungsnummer,
  k.firmenname,
  r.gesamtsumme_brutto,
  COALESCE(SUM(z.betrag), 0) AS bezahlt,
  ROUND(r.gesamtsumme_brutto - COALESCE(SUM(z.betrag), 0), 2) AS restoffen
FROM rechnungen r
JOIN kunden k ON k.id = r.kunden_id
LEFT JOIN zahlungen z ON z.rechnung_id = r.id
GROUP BY r.id, r.rechnungsnummer, k.firmenname, r.gesamtsumme_brutto
ORDER BY r.rechnungsnummer
LIMIT 10;
```

```sql
SELECT
  m.mahnstufe,
  COUNT(*) AS anzahl_mahnungen,
  ROUND(AVG(m.gebuehr), 2) AS avg_gebuehr
FROM mahnungen m
GROUP BY m.mahnstufe
ORDER BY m.mahnstufe;
```

## Stoppen und Zuruecksetzen

Container stoppen:

```bash
docker compose down
```

Container + Volume (kompletter Reset mit neuer Initialisierung):

```bash
docker compose down -v
```
