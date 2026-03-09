import { McpServer } from "@modelcontextprotocol/sdk/server/mcp.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";
import { z } from "zod";
import { executeQuery, listTables, getTableSchema } from "./db-connector.js";
import { validateApiKey } from "./auth-middleware.js";

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
    // TODO: Aufgabe Schritt 2 – Tool hier implementieren
    // Hinweis: Nutze executeQuery() aus db-connector.ts
    // Wichtig: NIEMALS das password-Feld zurückgeben!
    throw new Error("Noch nicht implementiert – Aufgabe Schritt 2");
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
    // TODO: Aufgabe Schritt 2 – Tool hier implementieren
    throw new Error("Noch nicht implementiert – Aufgabe Schritt 2");
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

  const transport = new StdioServerTransport();
  await server.connect(transport);
  console.error("MCP-Server gestartet und bereit");
}

main().catch((error) => {
  console.error("Server-Fehler:", error);
  process.exit(1);
});
