import { Pool, PoolClient } from "pg";

// Connection Pool – wird einmal erstellt und wiederverwendet
let pool: Pool | null = null;

function getPool(): Pool {
  if (!pool) {
    pool = new Pool({
      connectionString: process.env.DATABASE_URL,
      max: 5,              // Maximale Anzahl Verbindungen
      idleTimeoutMillis: 30000,
      connectionTimeoutMillis: 5000,
    });

    pool.on("error", (err) => {
      console.error("Unerwarteter DB-Pool-Fehler:", err.message);
    });
  }
  return pool;
}

/**
 * Führt eine parametrisierte SQL-Query aus.
 * WICHTIG: Niemals String-Konkatenation für User-Eingaben verwenden!
 *
 * @param sql    SQL-Query mit $1, $2, ... Platzhaltern
 * @param params Parameter-Array (entspricht den Platzhaltern)
 */
export async function executeQuery<T = Record<string, unknown>>(
  sql: string,
  params: unknown[] = []
): Promise<T[]> {
  const client: PoolClient = await getPool().connect();
  try {
    const result = await client.query(sql, params);
    return result.rows as T[];
  } finally {
    client.release();
  }
}

/**
 * Gibt alle Tabellennamen im public-Schema zurück.
 */
export async function listTables(): Promise<string[]> {
  const rows = await executeQuery<{ tablename: string }>(
    "SELECT tablename FROM pg_tables WHERE schemaname = 'public' ORDER BY tablename"
  );
  return rows.map((r) => r.tablename);
}

/**
 * Gibt das Schema einer Tabelle zurück (Spalten, Typen, Constraints).
 *
 * TODO: Aufgabe Schritt 1 – Diese Funktion implementieren
 * Hinweis: information_schema.columns enthält alle nötigen Infos
 * WICHTIG: Tabellennamen können nicht parametrisiert werden →
 *          Whitelist-Validierung verwenden (nur erlaubte Tabellen zulassen)
 */
export async function getTableSchema(
  tableName: string
): Promise<ColumnInfo[]> {
  // Sicherheit: Tabellennamen gegen Whitelist prüfen
  const allowedTables = await listTables();
  if (!allowedTables.includes(tableName)) {
    throw new Error(`Tabelle '${tableName}' nicht gefunden oder nicht erlaubt`);
  }

  // TODO: SQL-Query implementieren die folgende Infos zurückgibt:
  // - Spaltenname (column_name)
  // - Datentyp (data_type)
  // - Nullable (is_nullable)
  // - Primary Key (ja/nein)
  throw new Error("Noch nicht implementiert – Aufgabe Schritt 1");
}

export interface ColumnInfo {
  columnName: string;
  dataType: string;
  isNullable: boolean;
  isPrimaryKey: boolean;
}

/**
 * Verbindung sauber beenden (bei Server-Shutdown)
 */
export async function closePool(): Promise<void> {
  if (pool) {
    await pool.end();
    pool = null;
  }
}
