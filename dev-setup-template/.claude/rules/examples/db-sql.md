---
description: "BEISPIEL: Datenbank & SQL — Vollstaendige Version in stacks/oracle-db/ und stacks/postgresql/"
globs: ["**/*.sql", "**/migrations/**"]
alwaysApply: false
---

<!--
  HINWEIS: Dies ist ein Stub. DB-spezifische Rules liegen in:
  stacks/oracle-db/rules.md     (Oracle-spezifisch)
  stacks/postgresql/rules.md    (PostgreSQL-spezifisch)

  setup.sh kopiert alle Stacks nach .claude/rules/stacks/.
  /configure aktiviert die passenden automatisch.
-->

# Datenbank & SQL (Kurzversion)

- Flyway Migrations: V001__create_table.sql
- NEVER bestehende Migration editieren
- NEVER SELECT * in Produktion
- NEVER DDL ohne Flyway-Migration
- NEVER DROP ohne explizite Bestaetigung

→ Oracle: `stacks/oracle-db/` | PostgreSQL: `stacks/postgresql/`
