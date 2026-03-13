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

  const SENSITIVE_COLUMNS = ["password", "secret", "api_key", "token"];

  const rows = await executeQuery<{
    column_name: string;
    data_type: string;
    is_nullable: string;
    is_primary_key: boolean;
  }>(
    `SELECT c.column_name, c.data_type, c.is_nullable,
            CASE WHEN kcu.column_name IS NOT NULL THEN true ELSE false END AS is_primary_key
     FROM information_schema.columns c
     LEFT JOIN information_schema.table_constraints tc
       ON tc.table_name = c.table_name AND tc.constraint_type = 'PRIMARY KEY'
       AND tc.table_schema = c.table_schema
     LEFT JOIN information_schema.key_column_usage kcu
       ON kcu.constraint_name = tc.constraint_name
       AND kcu.column_name = c.column_name
       AND kcu.table_schema = c.table_schema
     WHERE c.table_name = $1 AND c.table_schema = 'public'
     ORDER BY c.ordinal_position`,
    [tableName]
  );

  return rows
    .filter((r) => !SENSITIVE_COLUMNS.includes(r.column_name.toLowerCase()))
    .map((r) => ({
      columnName: r.column_name,
      dataType: r.data_type,
      isNullable: r.is_nullable === "YES",
      isPrimaryKey: r.is_primary_key,
    }));
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
