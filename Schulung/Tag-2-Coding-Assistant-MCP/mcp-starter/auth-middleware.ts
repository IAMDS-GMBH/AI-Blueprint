/**
 * Authentifizierung & Autorisierung für den MCP-Server
 *
 * Ebene 1: API-Key Validierung (einfach, für Schulungszwecke)
 * Ebene 2: Tool-Level Berechtigungen (welcher Key darf was)
 *
 * Für Produktion: OAuth2/OIDC in Betracht ziehen
 */

// Erlaubte API-Keys mit zugehörigen Rollen
// In Produktion: Aus Datenbank oder Secret Manager laden
interface ApiKeyConfig {
  key: string;
  role: "reader" | "admin";
  description: string;
}

let apiKeys: ApiKeyConfig[] = [];

/**
 * API-Keys beim Start laden.
 * Muss einmal aufgerufen werden bevor validateApiKey verwendet wird.
 */
export function initializeAuth(): void {
  const masterKey = process.env.MCP_API_KEY;
  if (!masterKey) {
    throw new Error("MCP_API_KEY Umgebungsvariable fehlt");
  }

  apiKeys = [
    {
      key: masterKey,
      role: "reader",
      description: "Standard-Key für Lesezugriff",
    },
    // TODO: Aufgabe Schritt 3 – Admin-Key hinzufügen
    // {
    //   key: process.env.MCP_ADMIN_KEY || "",
    //   role: "admin",
    //   description: "Admin-Key für Schreibzugriff",
    // },
  ];
}

/**
 * Prüft ob ein API-Key gültig ist.
 *
 * TODO: Aufgabe Schritt 3 – Diese Funktion vollständig implementieren
 * - Key aus dem übergebenen Header lesen
 * - Gegen apiKeys-Liste prüfen
 * - Bei ungültigem Key: false zurückgeben
 * - Bei gültigem Key: Rolle zurückgeben
 */
export function validateApiKey(providedKey: string | undefined): {
  valid: boolean;
  role?: "reader" | "admin";
} {
  if (!providedKey) {
    return { valid: false };
  }

  // TODO: Implementierung hier
  // Hinweis: Einfacher String-Vergleich reicht für Schulung
  // Für Produktion: Timing-safe comparison (crypto.timingSafeEqual)
  throw new Error("Noch nicht implementiert – Aufgabe Schritt 3");
}

/**
 * Prüft ob eine Rolle ein bestimmtes Tool ausführen darf.
 */
export function isToolAllowed(
  role: "reader" | "admin",
  toolName: string
): boolean {
  const readerTools = ["get-schema", "query-users", "get-user-by-email", "health-check"];
  const adminTools = [...readerTools, "user-statistics"];

  if (role === "admin") return adminTools.includes(toolName);
  return readerTools.includes(toolName);
}

/**
 * Logt einen Tool-Aufruf (OHNE sensible Daten).
 */
export function logToolCall(
  toolName: string,
  success: boolean,
  role?: string
): void {
  const timestamp = new Date().toISOString();
  const status = success ? "OK" : "FEHLER";
  // Keine Parameter-Werte loggen – könnten sensible Daten enthalten
  console.error(`[${timestamp}] Tool: ${toolName} | Status: ${status} | Rolle: ${role ?? "unbekannt"}`);
}
