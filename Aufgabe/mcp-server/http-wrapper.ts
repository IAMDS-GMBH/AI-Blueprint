import { config } from "dotenv";
config();

import express, { Request, Response, NextFunction } from "express";
import cors from "cors";
import { executeQuery, listTables, getTableSchema, closePool } from "./db-connector.js";
import { initializeAuth, validateApiKey } from "./auth-middleware.js";

const SENSITIVE_COLUMNS = ["password", "secret", "api_key", "token"];

const FORBIDDEN_SQL_PATTERNS: RegExp[] = [
  /;\s*/i,
  /--/,
  /\/\*/,
  /\bdrop\b/i,
  /\bdelete\b/i,
  /\binsert\b/i,
  /\bupdate\b/i,
  /\balter\b/i,
  /\bcreate\b/i,
  /\btruncate\b/i,
  /\bgrant\b/i,
  /\brevoke\b/i,
  /\bunion\b/i,
  /\binto\b/i,
  /\bcopy\b/i,
];

function validateWhereClause(clause: string): void {
  for (const pattern of FORBIDDEN_SQL_PATTERNS) {
    if (pattern.test(clause)) {
      throw new Error(
        "WHERE-Klausel enthaelt nicht erlaubtes SQL-Pattern. " +
        "Nur einfache Bedingungen (=, <, >, LIKE, AND, OR) sind erlaubt."
      );
    }
  }
}

// Tool-Definitionen fuer GET /api/tools
const TOOL_DEFINITIONS = [
  {
    name: "list-tables",
    description: "Listet alle Tabellen in der Datenbank auf.",
    parameters: {},
  },
  {
    name: "describe-table",
    description: "Gibt das Schema einer Tabelle zurueck (Spalten, Typen, Constraints).",
    parameters: {
      tableName: { type: "string", description: "Name der Tabelle", required: true },
    },
  },
  {
    name: "get-schema",
    description: "Gibt das Schema aller Tabellen in der Datenbank zurueck.",
    parameters: {},
  },
  {
    name: "count-rows",
    description: "Zaehlt die Anzahl der Eintraege in einer Tabelle. Optional mit WHERE-Bedingung.",
    parameters: {
      tableName: { type: "string", description: "Name der Tabelle", required: true },
      whereClause: { type: "string", description: "Optionale WHERE-Bedingung (ohne WHERE-Keyword)", required: false },
    },
  },
  {
    name: "query-table",
    description: "Fuehrt eine SELECT-Abfrage auf einer Tabelle aus. Sensible Spalten werden automatisch ausgeschlossen. Max 50 Zeilen.",
    parameters: {
      tableName: { type: "string", description: "Name der Tabelle", required: true },
      columns: { type: "array", description: "Spalten die zurueckgegeben werden sollen", required: false },
      whereClause: { type: "string", description: "Optionale WHERE-Bedingung (ohne WHERE-Keyword)", required: false },
      limit: { type: "number", description: "Max. Anzahl Zeilen (max 50)", required: false },
    },
  },
  {
    name: "query-users",
    description: "Gibt eine Liste von Usern zurueck. Maximal 50 Eintraege. Passwoerter werden NICHT zurueckgegeben.",
    parameters: {
      limit: { type: "number", description: "Anzahl der zurueckgegebenen User (max 50)", required: false },
      offset: { type: "number", description: "Startposition fuer Paginierung", required: false },
    },
  },
  {
    name: "get-user-by-email",
    description: "Sucht einen User anhand der E-Mail-Adresse.",
    parameters: {
      email: { type: "string", description: "E-Mail-Adresse des gesuchten Users", required: true },
    },
  },
];

// Tool-Handler: Jeder fuehrt die entsprechende DB-Operation aus
const toolHandlers: Record<string, (args: Record<string, unknown>) => Promise<unknown>> = {
  "list-tables": async () => {
    return await listTables();
  },

  "describe-table": async (args) => {
    const tableName = args.tableName as string;
    if (!tableName) throw new Error("tableName ist erforderlich");
    return await getTableSchema(tableName);
  },

  "get-schema": async () => {
    const tables = await listTables();
    const schema: Record<string, unknown> = {};
    for (const table of tables) {
      schema[table] = await getTableSchema(table);
    }
    return schema;
  },

  "count-rows": async (args) => {
    const tableName = args.tableName as string;
    const whereClause = args.whereClause as string | undefined;
    if (!tableName) throw new Error("tableName ist erforderlich");

    const allowedTables = await listTables();
    if (!allowedTables.includes(tableName)) {
      throw new Error(`Tabelle '${tableName}' nicht gefunden oder nicht erlaubt`);
    }

    if (whereClause) {
      validateWhereClause(whereClause);
    }

    let sql = `SELECT COUNT(*) as count FROM "${tableName}"`;
    if (whereClause) {
      sql += ` WHERE ${whereClause}`;
    }

    const rows = await executeQuery<{ count: string }>(sql);
    return { count: Number(rows[0].count) };
  },

  "query-table": async (args) => {
    const tableName = args.tableName as string;
    const columns = args.columns as string[] | undefined;
    const whereClause = args.whereClause as string | undefined;
    const limit = Math.min(Math.max((args.limit as number) || 50, 1), 50);

    if (!tableName) throw new Error("tableName ist erforderlich");

    const allowedTables = await listTables();
    if (!allowedTables.includes(tableName)) {
      throw new Error(`Tabelle '${tableName}' nicht gefunden oder nicht erlaubt`);
    }

    if (whereClause) {
      validateWhereClause(whereClause);
    }

    const schemaColumns = await getTableSchema(tableName);
    const validColumnNames = schemaColumns.map((c) => c.columnName);

    const safeColumns = (columns ?? ["*"])
      .filter((c) => c === "*" || !SENSITIVE_COLUMNS.includes(c.toLowerCase()));

    let selectPart: string;
    if (safeColumns.includes("*")) {
      selectPart = validColumnNames
        .filter((c) => !SENSITIVE_COLUMNS.includes(c.toLowerCase()))
        .map((c) => `"${c}"`).join(", ");
    } else {
      for (const col of safeColumns) {
        if (!validColumnNames.includes(col)) {
          throw new Error(`Spalte '${col}' existiert nicht in Tabelle '${tableName}'`);
        }
      }
      selectPart = safeColumns.map((c) => `"${c}"`).join(", ");
    }

    let sql = `SELECT ${selectPart} FROM "${tableName}"`;
    if (whereClause) {
      sql += ` WHERE ${whereClause}`;
    }
    sql += ` LIMIT ${limit}`;

    return await executeQuery(sql);
  },

  "query-users": async (args) => {
    const limit = Math.min(Math.max((args.limit as number) || 10, 1), 50);
    const offset = Math.max((args.offset as number) || 0, 0);

    return await executeQuery(
      `SELECT id, email, name, created_at FROM users ORDER BY id LIMIT $1 OFFSET $2`,
      [limit, offset]
    );
  },

  "get-user-by-email": async (args) => {
    const email = args.email as string;
    if (!email) throw new Error("email ist erforderlich");

    const rows = await executeQuery(
      `SELECT id, email, name, created_at FROM users WHERE email = $1`,
      [email]
    );
    return rows.length > 0 ? rows[0] : null;
  },
};

// Express-App erstellen
const app = express();
const PORT = 3001;

app.use(express.json());
app.use(cors({
  origin: ["http://localhost:8080", "http://localhost:5173"],
}));

// Health-Endpoint (kein Auth noetig)
app.get("/api/health", (_req: Request, res: Response) => {
  res.json({ status: "ok" });
});

// Auth-Middleware fuer alle weiteren /api/ Routen
function authMiddleware(req: Request, res: Response, next: NextFunction): void {
  const authHeader = req.headers.authorization;
  const token = authHeader?.startsWith("Bearer ") ? authHeader.slice(7) : undefined;

  const result = validateApiKey(token);
  if (!result.valid) {
    res.status(401).json({ error: "Unauthorized – API-Key fehlt oder ungueltig" });
    return;
  }

  next();
}

// Tool-Liste
app.get("/api/tools", authMiddleware, (_req: Request, res: Response) => {
  res.json(TOOL_DEFINITIONS);
});

// Tool ausfuehren
app.post("/api/tools/:toolName", authMiddleware, async (req: Request, res: Response) => {
  const { toolName } = req.params;
  const handler = toolHandlers[toolName];

  if (!handler) {
    res.status(404).json({ error: `Tool '${toolName}' nicht gefunden` });
    return;
  }

  try {
    const args = req.body?.arguments ?? {};
    const result = await handler(args);
    res.json({ result });
  } catch (error) {
    const message = error instanceof Error ? error.message : "Unbekannter Fehler";
    res.status(400).json({ error: message });
  }
});

// Server starten
function main(): void {
  if (!process.env.DATABASE_URL) {
    console.error("Fehler: DATABASE_URL Umgebungsvariable fehlt");
    process.exit(1);
  }
  if (!process.env.MCP_API_KEY) {
    console.error("Fehler: MCP_API_KEY Umgebungsvariable fehlt");
    process.exit(1);
  }

  initializeAuth();

  app.listen(PORT, () => {
    console.log(`HTTP-Wrapper laeuft auf http://localhost:${PORT}`);
  });
}

// Graceful Shutdown
process.on("SIGINT", async () => {
  console.log("Server wird beendet...");
  await closePool();
  process.exit(0);
});

main();
