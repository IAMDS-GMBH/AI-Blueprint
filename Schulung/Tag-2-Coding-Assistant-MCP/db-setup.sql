-- =============================================================================
-- Tag 2 Schulung: DB-Setup Skript
-- =============================================================================
-- Zweck: Erstellt die users-Tabelle + Beispieldaten fuer Tag 2 MCP-Uebung
-- Wird benoetigt falls Tag 1 (Backend-Aufbau) nicht vollstaendig abgeschlossen wurde
--
-- Ausfuehren:
--   docker exec -i schulung-db psql -U postgres -d userdb < db-setup.sql
--
-- Oder in psql:
--   psql -h localhost -U postgres -d userdb -f db-setup.sql
-- =============================================================================

-- UUID Extension aktivieren (PostgreSQL)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tabelle erstellen (idempotent)
CREATE TABLE IF NOT EXISTS users (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email       VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    first_name  VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100) NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Beispieldaten (Passwort: "Test1234!" bcrypt-gehasht)
INSERT INTO users (email, password, first_name, last_name, created_at)
VALUES
    ('max.muster@example.com',
     '$2a$10$example.hash.placeholder.1',
     'Max', 'Muster',
     NOW() - INTERVAL '5 days'),
    ('anna.schmidt@example.com',
     '$2a$10$example.hash.placeholder.2',
     'Anna', 'Schmidt',
     NOW() - INTERVAL '3 days'),
    ('thomas.meyer@example.com',
     '$2a$10$example.hash.placeholder.3',
     'Thomas', 'Meyer',
     NOW() - INTERVAL '1 day'),
    ('sarah.klein@example.com',
     '$2a$10$example.hash.placeholder.4',
     'Sarah', 'Klein',
     NOW() - INTERVAL '2 hours'),
    ('peter.braun@example.com',
     '$2a$10$example.hash.placeholder.5',
     'Peter', 'Braun',
     NOW() - INTERVAL '30 minutes')
ON CONFLICT (email) DO NOTHING;

-- Ergebnis pruefen
SELECT id, email, first_name, last_name, created_at FROM users ORDER BY created_at DESC;
