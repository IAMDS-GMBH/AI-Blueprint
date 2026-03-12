import { config } from "dotenv";
config();

import { McpServer } from "@modelcontextprotocol/sdk/server/mcp.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";
import { z } from "zod";
import { executeQuery, listTables, getTableSchema } from "./db-connector.js";
import { initializeAuth, validateApiKey } from "./auth-middleware.js";

const SENSITIVE_COLUMNS = ["password", "secret", "api_key", "token"];

// SQL-Injection-Schutz fuer WHERE-Klauseln
// Blockt gefaehrliche Schluesselwoerter die in einem reinen Lese-Kontext nicht noetig sind
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

// MCP-Server Instanz erstellen
const server = new McpServer({
  name: "schulung-db",
  version: "1.0.0",
});

// -------------------------------------------------------
// TOOL: get-schema
// Gibt das Datenbankschema zurück (alle Tabellen + Spalten)
// -------------------------------------------------------
server.tool(
  "get-schema",
  "Gibt das Schema aller Tabellen in der Datenbank zurück. " +
    "Nützlich um zu verstehen welche Daten verfügbar sind.",
  {},
  async () => {
    const tables = await listTables();
    const schema: Record<string, unknown> = {};

    for (const table of tables) {
      schema[table] = await getTableSchema(table);
    }

    return {
      content: [
        {
          type: "text",
          text: JSON.stringify(schema, null, 2),
        },
      ],
    };
  }
);

// -------------------------------------------------------
// TOOL: query-users
// Gibt User aus der Datenbank zurück (OHNE Passwort-Hash)
// -------------------------------------------------------
server.tool(
  "query-users",
  "Gibt eine Liste von Usern zurück. Maximal 50 Einträge. " +
    "Passwörter werden NICHT zurückgegeben.",
  {
    limit: z
      .number()
      .min(1)
      .max(50)
      .optional()
      .default(10)
      .describe("Anzahl der zurückgegebenen User (max. 50)"),
    offset: z
      .number()
      .min(0)
      .optional()
      .default(0)
      .describe("Startposition für Paginierung"),
  },
  async ({ limit, offset }) => {
    const rows = await executeQuery(
      `SELECT id, email, name, created_at FROM users ORDER BY id LIMIT $1 OFFSET $2`,
      [limit, offset]
    );
    return {
      content: [{ type: "text", text: JSON.stringify(rows, null, 2) }],
    };
  }
);

// -------------------------------------------------------
// TOOL: get-user-by-email
// Sucht einen User anhand der E-Mail-Adresse
// -------------------------------------------------------
server.tool(
  "get-user-by-email",
  "Sucht einen User anhand der E-Mail-Adresse. " +
    "Gibt null zurück wenn kein User gefunden wurde.",
  {
    email: z.string().email().describe("E-Mail-Adresse des gesuchten Users"),
  },
  async ({ email }) => {
    const rows = await executeQuery(
      `SELECT id, email, name, created_at FROM users WHERE email = $1`,
      [email]
    );
    const user = rows.length > 0 ? rows[0] : null;
    return {
      content: [{ type: "text", text: JSON.stringify(user, null, 2) }],
    };
  }
);

// -------------------------------------------------------
// TOOL: list-tables
// Alle Tabellen im public-Schema auflisten
// -------------------------------------------------------
server.tool(
  "list-tables",
  "Listet alle Tabellen in der Datenbank auf.",
  {},
  async () => {
    const tables = await listTables();
    return {
      content: [{ type: "text", text: JSON.stringify(tables, null, 2) }],
    };
  }
);

// -------------------------------------------------------
// TOOL: describe-table
// Schema einer Tabelle anzeigen
// -------------------------------------------------------
server.tool(
  "describe-table",
  "Gibt das Schema einer Tabelle zurück (Spalten, Typen, Constraints).",
  {
    tableName: z.string().describe("Name der Tabelle"),
  },
  async ({ tableName }) => {
    const schema = await getTableSchema(tableName);
    return {
      content: [{ type: "text", text: JSON.stringify(schema, null, 2) }],
    };
  }
);

// -------------------------------------------------------
// TOOL: count-rows
// Anzahl Einträge in einer Tabelle zählen
// -------------------------------------------------------
server.tool(
  "count-rows",
  "Zählt die Anzahl der Einträge in einer Tabelle. " +
    "Optional mit WHERE-Bedingung.",
  {
    tableName: z.string().describe("Name der Tabelle"),
    whereClause: z
      .string()
      .optional()
      .describe("Optionale WHERE-Bedingung (ohne WHERE-Keyword). Nur einfache Bedingungen erlaubt."),
  },
  async ({ tableName, whereClause }) => {
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
    return {
      content: [{ type: "text", text: JSON.stringify({ count: Number(rows[0].count) }, null, 2) }],
    };
  }
);

// -------------------------------------------------------
// TOOL: query-table
// SELECT-Abfrage auf eine Tabelle (max 50 Zeilen)
// -------------------------------------------------------
server.tool(
  "query-table",
  "Führt eine SELECT-Abfrage auf einer Tabelle aus. " +
    "Sensible Spalten werden automatisch ausgeschlossen. Max 50 Zeilen.",
  {
    tableName: z.string().describe("Name der Tabelle"),
    columns: z
      .array(z.string())
      .optional()
      .describe("Spalten die zurückgegeben werden sollen (Standard: alle nicht-sensiblen)"),
    whereClause: z
      .string()
      .optional()
      .describe("Optionale WHERE-Bedingung (ohne WHERE-Keyword). Nur einfache Bedingungen erlaubt."),
    limit: z
      .number()
      .min(1)
      .max(50)
      .optional()
      .default(50)
      .describe("Max. Anzahl Zeilen (max 50)"),
  },
  async ({ tableName, columns, whereClause, limit }) => {
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
      selectPart = validColumnNames.map((c) => `"${c}"`).join(", ");
    } else {
      // Validate that requested columns actually exist in the table
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

    const rows = await executeQuery(sql);
    return {
      content: [{ type: "text", text: JSON.stringify(rows, null, 2) }],
    };
  }
);

// -------------------------------------------------------
// Server starten
// -------------------------------------------------------
async function main() {
  // Pflicht-Umgebungsvariablen prüfen
  if (!process.env.DATABASE_URL) {
    console.error("Fehler: DATABASE_URL Umgebungsvariable fehlt");
    process.exit(1);
  }
  if (!process.env.MCP_API_KEY) {
    console.error("Fehler: MCP_API_KEY Umgebungsvariable fehlt");
    process.exit(1);
  }

  initializeAuth();

  const transport = new StdioServerTransport();
  await server.connect(transport);
  console.error("MCP-Server gestartet und bereit");
}

main().catch((error) => {
  console.error("Server-Fehler:", error);
  process.exit(1);
});
