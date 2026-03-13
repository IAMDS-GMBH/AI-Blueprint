---
description: "PostgreSQL Regeln"
globs: ["**/*.sql", "**/migrations/**"]
alwaysApply: false
---

# PostgreSQL

## Naming
- Alles snake_case: Tabellen, Spalten, Constraints, Indices
- Tabellen: Plural (customers, orders)
- Primary Keys: id oder tabelle_id
- Foreign Keys: fk_tabelle_referenz (fk_orders_customer)
- Indices: idx_tabelle_spalte (idx_orders_customer_id)

## Flyway Migrations
- Naming: V001__create_table.sql / R__repeatable.sql
- NEVER bestehende Migration editieren — immer neue Datei
- Jede Migration mit Rollback-SQL kommentieren

## Data Types
- IDs: BIGSERIAL (auto-increment) oder BIGINT + GENERATED ALWAYS AS IDENTITY
- Geld: NUMERIC(19,4) — NEVER FLOAT/DOUBLE PRECISION
- Timestamps: TIMESTAMPTZ (mit Zeitzone, immer)
- Strings: TEXT (unbegrenzt) oder VARCHAR(n)
- Booleans: BOOLEAN (nativ)
- JSON: JSONB (nicht JSON — JSONB ist indizierbar)

## Performance
- Indices auf alle Foreign Keys
- EXPLAIN ANALYZE vor komplexen Queries
- Prepared Statements / Parameterized Queries (SQL Injection!)
- Pagination: LIMIT n OFFSET m

## Testing
- Migrations testen: Flyway migrate + validate in CI Pipeline
- Testdaten: Separate V-Migrations oder Fixtures (nur in Test-Profil)
- NEVER Produktionsdaten in Tests verwenden
- Rollback-SQL zu jeder Migration testen
- Testcontainers mit PostgreSQL Image fuer Integration Tests
- pgTAP fuer DB-Unit-Tests (Stored Functions, Constraints)

## Verbote
- NEVER SELECT * in Produktion
- NEVER DDL ohne Flyway-Migration
- NEVER Stored Procedures fuer Business-Logik
- NEVER DROP ohne explizite Bestaetigung
- NEVER FLOAT fuer Geldbetraege
- NEVER JSON statt JSONB (ausser fuer exakte Ordnung noetig)
