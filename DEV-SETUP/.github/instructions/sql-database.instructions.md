---
applyTo: "**/*.sql,**/migration/*.java,**/migrations/**"
---

# Datenbank-Standards

## Oracle
- Tabellennamen: UPPER_CASE, Plural (`CUSTOMER_ORDERS`)
- Spaltennamen: UPPER_CASE
- Keine SQL-reserved-words als Bezeichner
- Constraints benennen: `CONSTRAINT FK_ORDER_CUSTOMER FOREIGN KEY ...`

## PostgreSQL
- snake_case für alle Bezeichner (Tabellen, Spalten, Indizes)
- Timestamps: `TIMESTAMPTZ` statt `TIMESTAMP`

## Beide
- Kein `SELECT *` in Produktion
- Alle Schema-Änderungen via Flyway Migration – niemals manuell in DB
- Migration-Naming: `V001__create_table.sql`, `V002__add_column.sql`
- Indizes für alle Foreign Keys und häufig gefilterten Spalten
