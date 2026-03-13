# Hono + Bun + Drizzle — Patterns & Anti-Patterns

## Hono App Setup (DO)
```typescript
// src/index.ts
import { Hono } from "hono";
import { cors } from "hono/cors";
import { logger } from "hono/logger";
import { secureHeaders } from "hono/secure-headers";
import { orderRoutes } from "./routes/orders";
import { authRoutes } from "./routes/auth";
import { errorHandler } from "./middleware/error-handler";
import { env } from "./config/env";

const app = new Hono();

// Middleware
app.use("*", logger());
app.use("*", cors({ origin: env.CORS_ORIGIN }));
app.use("*", secureHeaders());

// Routes
app.route("/api/v1/orders", orderRoutes);
app.route("/api/v1/auth", authRoutes);

// Health Check
app.get("/health", (c) => c.json({ status: "ok" }));

// Error Handler
app.onError(errorHandler);

export default {
  port: env.PORT,
  fetch: app.fetch,
};
```

## Routes mit Zod Validierung (DO)
```typescript
// src/routes/orders.ts
import { Hono } from "hono";
import { zValidator } from "@hono/zod-validator";
import { z } from "zod";
import { OrderService } from "../services/order.service";
import { authMiddleware } from "../middleware/auth";

const app = new Hono();
const orderService = new OrderService();

const createOrderSchema = z.object({
  customerId: z.string().uuid(),
  total: z.number().positive(),
  items: z.array(z.object({
    productId: z.string().uuid(),
    quantity: z.number().int().positive(),
    unitPrice: z.number().positive(),
  })).min(1),
});

const orderParamsSchema = z.object({
  id: z.string().uuid(),
});

// Alle Routes geschuetzt
app.use("*", authMiddleware);

app.get("/", async (c) => {
  const page = Number(c.req.query("page")) || 1;
  const limit = Number(c.req.query("limit")) || 20;
  const orders = await orderService.findAll(page, limit);
  return c.json(orders);
});

app.get("/:id", zValidator("param", orderParamsSchema), async (c) => {
  const { id } = c.req.valid("param");
  const order = await orderService.findById(id);
  return c.json(order);
});

app.post("/", zValidator("json", createOrderSchema), async (c) => {
  const input = c.req.valid("json");
  const order = await orderService.create(input);
  return c.json(order, 201);
});

app.delete("/:id", zValidator("param", orderParamsSchema), async (c) => {
  const { id } = c.req.valid("param");
  await orderService.delete(id);
  return c.body(null, 204);
});

export { app as orderRoutes };
```

## Service Pattern (DO)
```typescript
// src/services/order.service.ts
import { db } from "../db";
import { orders } from "../db/schema";
import { eq } from "drizzle-orm";
import { HTTPException } from "hono/http-exception";
import type { z } from "zod";
import type { createOrderSchema } from "../routes/orders";

type CreateOrderInput = z.infer<typeof createOrderSchema>;

export class OrderService {
  async findAll(page: number, limit: number) {
    const offset = (page - 1) * limit;
    return db.select().from(orders).limit(limit).offset(offset);
  }

  async findById(id: string) {
    const [order] = await db.select().from(orders).where(eq(orders.id, id));
    if (!order) {
      throw new HTTPException(404, { message: "Order not found" });
    }
    return order;
  }

  async create(input: CreateOrderInput) {
    const [order] = await db.insert(orders).values(input).returning();
    return order;
  }

  async delete(id: string) {
    const result = await db.delete(orders).where(eq(orders.id, id)).returning();
    if (result.length === 0) {
      throw new HTTPException(404, { message: "Order not found" });
    }
  }
}
```

## Auth Middleware mit Jose (DO)
```typescript
// src/middleware/auth.ts
import { createMiddleware } from "hono/factory";
import { jwtVerify } from "jose";
import { HTTPException } from "hono/http-exception";
import { env } from "../config/env";

interface JWTPayload {
  sub: string;
  role: string;
}

export const authMiddleware = createMiddleware<{
  Variables: { userId: string; role: string };
}>(async (c, next) => {
  const header = c.req.header("Authorization");
  if (!header?.startsWith("Bearer ")) {
    throw new HTTPException(401, { message: "Missing authorization header" });
  }

  try {
    const token = header.slice(7);
    const secret = new TextEncoder().encode(env.JWT_SECRET);
    const { payload } = await jwtVerify(token, secret);
    const claims = payload as unknown as JWTPayload;

    c.set("userId", claims.sub);
    c.set("role", claims.role);
    await next();
  } catch {
    throw new HTTPException(401, { message: "Invalid or expired token" });
  }
});
```

## Token erstellen mit Jose (DO)
```typescript
// src/services/auth.service.ts
import { SignJWT } from "jose";
import { env } from "../config/env";

export async function createToken(userId: string, role: string): Promise<string> {
  const secret = new TextEncoder().encode(env.JWT_SECRET);

  return new SignJWT({ sub: userId, role })
    .setProtectedHeader({ alg: "HS256" })
    .setIssuedAt()
    .setExpirationTime("24h")
    .sign(secret);
}
```

## Error Handler (DO)
```typescript
// src/middleware/error-handler.ts
import type { ErrorHandler } from "hono";
import { HTTPException } from "hono/http-exception";
import { ZodError } from "zod";

export const errorHandler: ErrorHandler = (err, c) => {
  if (err instanceof HTTPException) {
    return c.json(
      { error: err.message, statusCode: err.status },
      err.status
    );
  }

  if (err instanceof ZodError) {
    return c.json(
      { error: "Validation failed", details: err.errors, statusCode: 400 },
      400
    );
  }

  console.error("Unhandled error:", err);
  return c.json(
    { error: "Internal server error", statusCode: 500 },
    500
  );
};
```

## Drizzle ORM Setup (DO)
```typescript
// src/db/index.ts
import { drizzle } from "drizzle-orm/node-postgres";
import { Pool } from "pg";
import { env } from "../config/env";
import * as schema from "./schema";

const pool = new Pool({
  connectionString: env.DATABASE_URL,
  max: 20,
});

export const db = drizzle(pool, { schema });
```

```typescript
// src/db/schema.ts
import { pgTable, uuid, varchar, numeric, timestamp } from "drizzle-orm/pg-core";

export const orders = pgTable("orders", {
  id: uuid("id").defaultRandom().primaryKey(),
  customerId: uuid("customer_id").notNull(),
  total: numeric("total", { precision: 19, scale: 4 }).notNull(),
  status: varchar("status", { length: 20 }).default("pending").notNull(),
  createdAt: timestamp("created_at", { withTimezone: true }).defaultNow().notNull(),
  updatedAt: timestamp("updated_at", { withTimezone: true }).defaultNow().notNull(),
});
```

## Typsichere Env Config (DO)
```typescript
// src/config/env.ts
import { z } from "zod";

const envSchema = z.object({
  PORT: z.coerce.number().default(3000),
  NODE_ENV: z.enum(["development", "production", "test"]).default("development"),
  DATABASE_URL: z.string().url(),
  JWT_SECRET: z.string().min(32),
  CORS_ORIGIN: z.string().default("http://localhost:5173"),
});

export const env = envSchema.parse(Bun.env);
```

## Hono Test mit testClient (DO)
```typescript
// src/routes/__tests__/orders.test.ts
import { describe, it, expect, mock } from "bun:test";
import { testClient } from "hono/testing";
import { orderRoutes } from "../orders";

// Auth Middleware mocken
mock.module("../middleware/auth", () => ({
  authMiddleware: createMiddleware(async (_c, next) => await next()),
}));

describe("Order Routes", () => {
  const client = testClient(orderRoutes);

  it("should return orders on GET /", async () => {
    const res = await client.index.$get();
    expect(res.status).toBe(200);

    const data = await res.json();
    expect(Array.isArray(data)).toBe(true);
  });

  it("should create order on POST /", async () => {
    const res = await client.index.$post({
      json: {
        customerId: "550e8400-e29b-41d4-a716-446655440000",
        total: 99.99,
        items: [{ productId: "...", quantity: 2, unitPrice: 49.99 }],
      },
    });

    expect(res.status).toBe(201);
    const order = await res.json();
    expect(order.total).toBe("99.99");
  });

  it("should return 404 for non-existent order", async () => {
    const res = await client[":id"].$get({
      param: { id: "00000000-0000-0000-0000-000000000000" },
    });
    expect(res.status).toBe(404);
  });
});
```

## Service Unit Test (DO)
```typescript
// src/services/__tests__/order.service.test.ts
import { describe, it, expect, mock, beforeEach } from "bun:test";
import { OrderService } from "../order.service";

describe("OrderService", () => {
  let service: OrderService;

  beforeEach(() => {
    service = new OrderService();
  });

  it("should throw 404 when order not found", () => {
    expect(service.findById("non-existent")).rejects.toThrow();
  });
});
```

## Drizzle Transaction (DO)
```typescript
// Atomare Multi-Table Operation
async function createOrderWithItems(input: CreateOrderInput) {
  return db.transaction(async (tx) => {
    const [order] = await tx.insert(orders).values({
      customerId: input.customerId,
      total: input.total.toString(),
    }).returning();

    const itemValues = input.items.map(item => ({
      orderId: order.id,
      ...item,
      unitPrice: item.unitPrice.toString(),
    }));

    await tx.insert(orderItems).values(itemValues);

    return order;
  });
}
```

## package.json Scripts (DO)
```json
{
  "scripts": {
    "dev": "bun --watch src/index.ts",
    "start": "bun src/index.ts",
    "test": "bun test",
    "test:watch": "bun test --watch",
    "lint": "bun x biome check src/",
    "lint:fix": "bun x biome check --write src/",
    "db:generate": "bun x drizzle-kit generate",
    "db:migrate": "bun x drizzle-kit migrate",
    "db:studio": "bun x drizzle-kit studio"
  },
  "dependencies": {
    "hono": "^4",
    "@hono/zod-validator": "^0.4",
    "drizzle-orm": "^0.35",
    "pg": "^8",
    "jose": "^5",
    "zod": "^3"
  },
  "devDependencies": {
    "@types/pg": "^8",
    "drizzle-kit": "^0.30"
  }
}
```

## Anti-Pattern: Express-Style in Hono (DON'T)
```typescript
// FALSCH — Express Patterns
app.get("/orders", (req, res, next) => {
  const orders = await db.query("SELECT * FROM orders");
  res.json(orders);
});

// RICHTIG — Hono Context
app.get("/orders", async (c) => {
  const orders = await orderService.findAll(1, 20);
  return c.json(orders);
});
```

## Anti-Pattern: jsonwebtoken statt Jose (DON'T)
```typescript
// FALSCH — nicht Edge-kompatibel
import jwt from "jsonwebtoken";
const token = jwt.sign({ sub: userId }, secret);

// RICHTIG — Web Standards
import { SignJWT } from "jose";
const token = await new SignJWT({ sub: userId })
  .setProtectedHeader({ alg: "HS256" })
  .setExpirationTime("24h")
  .sign(secret);
```
