---
description: "Node.js + Hono + Bun Regeln"
globs: ["src/**/*.ts", "src/**/*.js", "package.json", "bunfig.toml"]
alwaysApply: false
---

# Hono + Bun + Drizzle

## Runtime & Tooling
- Bun als Runtime, Package Manager und Test Runner
- `bun run` statt `npm run` / `npx`
- `bun install` fuer Dependencies (bun.lockb)
- TypeScript nativ — kein tsc zum Ausfuehren noetig
- Scripts: `"dev": "bun --watch src/index.ts"`
- Hot Reload: `bun --watch` (kein nodemon noetig)

## Hono (Web-Framework)
- Hono statt Express — schneller, typsicher, Web Standards API
- App-Instanz: `new Hono()` — Routing via app.get(), app.post() etc.
- Middleware: app.use() — eigene Middleware als HonoMiddleware
- Context-Objekt `c` statt req/res: c.json(), c.text(), c.status()
- Route Groups: app.route("/api/v1/orders", orderRoutes)
- Error Handling: app.onError() global + HTTPException fuer spezifische Fehler
- Validator Middleware: @hono/zod-validator fuer typsichere Validierung
- NEVER Express-Patterns (req, res, next) — Hono nutzt Web Standards

## Architektur
- Routes → Handler → Service → Repository Schichtung
- Routes in eigenen Dateien: routes/orders.ts, routes/users.ts
- Services als Klassen oder Module
- Middleware fuer Cross-Cutting Concerns (Auth, Logging, CORS)
- NEVER Business-Logik in Route-Handlern — immer in Services

## API-Konventionen
- Route Prefix: app.route("/api/v1/orders", orderRoutes)
- GET → 200 | POST → 201 | PUT → 200 | PATCH → 200 | DELETE → 204
- Error Responses: c.json({ error, message, statusCode }, statusCode)
- Input-Validierung: Zod Schemas via @hono/zod-validator
- Content-Type: application/json via c.json()

## Validierung (Zod)
- Zod Schemas fuer alle Inputs (Body, Params, Query)
- @hono/zod-validator Middleware: zValidator("json", schema)
- Typen aus Schemas ableiten: z.infer<typeof schema>
- Custom Error Messages in Deutsch wo noetig
- NEVER manuelle Validierung — immer Zod

## Auth (Jose)
- Jose statt jsonwebtoken — Web Standards, Edge-kompatibel
- SignJWT fuer Token-Erstellung, jwtVerify fuer Validierung
- Middleware: JWT aus Authorization Header extrahieren + verifizieren
- Payload typisieren: JWTPayload erweitern mit eigenen Claims
- NEVER Secrets hardcoden — aus Bun.env laden
- NEVER eigene Crypto — Jose nutzt Web Crypto API

## Datenbank (Drizzle ORM + PostgreSQL)
- Drizzle ORM fuer Type-Safe DB Access
- Schema-Definition in src/db/schema.ts (pgTable)
- Migrations: drizzle-kit generate + migrate
- Connection: drizzle(pool) mit pg Pool
- Queries: db.select().from(table).where(eq(...))
- Prepared Statements automatisch — NEVER String Concatenation
- Transactions: db.transaction(async (tx) => { ... })

## Testing (Bun Test)
- `bun test` als Test Runner (Jest-kompatible API)
- describe/it/expect nativ verfuegbar
- Test-Dateien: *.test.ts oder *.spec.ts
- Hono testClient() fuer HTTP Tests (kein Supertest noetig)
- app.request() fuer einfache Request-Tests
- Test-Naming: it("should create order when valid input")
- Mocking: mock() von bun:test
- DB Tests: Testcontainers oder separate Test-DB
- NEVER echte externe APIs in Tests — immer mocken

## Configuration
- Environment Variables via Bun.env (NEVER process.env)
- .env Dateien: .env, .env.local, .env.production
- Typsichere Config: Zod Schema fuer env Validation beim Start
- NEVER hardcodierte Secrets

## Middleware-Reihenfolge
1. logger() — Request Logging
2. cors() — CORS Headers
3. secureHeaders() — Security Headers (wie Helmet)
4. Auth Middleware — JWT Validierung (auf geschuetzten Routes)
5. Error Handler — app.onError()

## Verbote
- NEVER console.log in Production — hono/logger oder pino
- NEVER `any` Type — immer konkrete Typen oder unknown
- NEVER Express-Patterns (req, res, next) — Hono Context nutzen
- NEVER `require()` — immer ES Module `import`
- NEVER synchrone I/O in Handlern (blockiert Event Loop)
- NEVER leere catch-Bloecke — Errors loggen oder weiterwerfen
- NEVER npm/yarn wenn Bun verfuegbar
- NEVER `var` — immer `const` oder `let`
- NEVER Callback-Style — async/await verwenden
- NEVER jsonwebtoken — jose verwenden (Web Standards)
