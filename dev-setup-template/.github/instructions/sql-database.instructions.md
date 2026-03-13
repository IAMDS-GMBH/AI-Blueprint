---
applyTo: "**/*.sql,**/migrations/**"
---

<!-- Kurzfassung — vollstaendige Rules in .claude/rules/examples/db-sql.md -->

# Datenbank & SQL

- Alle Schema-Aenderungen via Flyway Migration — NEVER manuell
- Migration-Naming: V001__create_table.sql
- NEVER SELECT * in Produktion
- NEVER bestehende Migration editieren
- Indizes fuer alle Foreign Keys
