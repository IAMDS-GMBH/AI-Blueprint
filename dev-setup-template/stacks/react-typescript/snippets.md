# React + TypeScript — Patterns & Anti-Patterns

## shadcn/ui + cn() Helper (DO)
```typescript
// lib/utils.ts
import { clsx, type ClassValue } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}
```

```tsx
// Verwendung in Komponenten
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

interface OrderCardProps {
  order: Order;
  isSelected?: boolean;
  onSelect: (id: number) => void;
}

export function OrderCard({ order, isSelected, onSelect }: OrderCardProps) {
  return (
    <Card
      className={cn(
        "cursor-pointer transition-colors hover:bg-accent",
        isSelected && "border-primary bg-accent"
      )}
      onClick={() => onSelect(order.id)}
    >
      <CardHeader>
        <CardTitle>{order.title}</CardTitle>
      </CardHeader>
      <CardContent>
        <p className="text-2xl font-bold">{formatCurrency(order.total)}</p>
      </CardContent>
    </Card>
  );
}
```

## TanStack Query mit Query Keys (DO)
```typescript
// src/lib/query-keys.ts
export const queryKeys = {
  orders: {
    all: ["orders"] as const,
    detail: (id: string) => ["orders", id] as const,
    byCustomer: (customerId: string) => ["orders", "customer", customerId] as const,
  },
  customers: {
    all: ["customers"] as const,
    detail: (id: string) => ["customers", id] as const,
  },
} as const;
```

```tsx
// src/hooks/useOrders.ts
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { api } from "@/lib/api";
import { queryKeys } from "@/lib/query-keys";
import type { Order, CreateOrderRequest } from "@/types";

export function useOrders() {
  return useQuery({
    queryKey: queryKeys.orders.all,
    queryFn: () => api.get<Order[]>("/api/v1/orders").then((r) => r.data),
  });
}

export function useOrder(id: string) {
  return useQuery({
    queryKey: queryKeys.orders.detail(id),
    queryFn: () => api.get<Order>(`/api/v1/orders/${id}`).then((r) => r.data),
    enabled: !!id,
  });
}

export function useCreateOrder() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: CreateOrderRequest) =>
      api.post<Order>("/api/v1/orders", data).then((r) => r.data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.orders.all });
    },
  });
}
```

## Custom Hook (DO)
```tsx
export function useAuth() {
  const [user, setUser] = useState<User | null>(null)

  useEffect(() => {
    const token = localStorage.getItem('token')
    if (token) {
      api.get<User>('/api/v1/auth/me')
        .then(r => setUser(r.data))
        .catch(() => localStorage.removeItem('token'))
    }
  }, [])

  const login = async (credentials: LoginRequest) => {
    const { data } = await api.post<AuthResponse>('/api/v1/auth/login', credentials)
    localStorage.setItem('token', data.token)
    setUser(data.user)
  }

  return { user, login, isAuthenticated: !!user }
}
```

## Vite Config mit SWC (DO)
```typescript
// vite.config.ts
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";
import path from "path";

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
  server: {
    port: 3000,
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
      },
    },
  },
});
```

## Environment Variables mit Vite (DO)
```typescript
// RICHTIG — Vite env variables
const apiUrl = import.meta.env.VITE_API_URL

// FALSCH — funktioniert nicht mit Vite
const apiUrl = process.env.REACT_APP_API_URL
```

## React Router DOM Setup (DO)
```tsx
// src/routes/index.tsx
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { lazy, Suspense } from "react";
import { RootLayout } from "@/layouts/RootLayout";
import { ProtectedRoute } from "@/components/ProtectedRoute";

const DashboardPage = lazy(() => import("@/pages/Dashboard"));
const OrdersPage = lazy(() => import("@/pages/Orders"));
const OrderDetailPage = lazy(() => import("@/pages/OrderDetail"));
const LoginPage = lazy(() => import("@/pages/Login"));

const router = createBrowserRouter([
  {
    path: "/",
    element: <RootLayout />,
    children: [
      {
        element: <ProtectedRoute />,
        children: [
          { index: true, element: <DashboardPage /> },
          { path: "orders", element: <OrdersPage /> },
          { path: "orders/:id", element: <OrderDetailPage /> },
        ],
      },
      { path: "login", element: <LoginPage /> },
    ],
  },
]);

export function AppRouter() {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <RouterProvider router={router} />
    </Suspense>
  );
}
```

```tsx
// src/components/ProtectedRoute.tsx
import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "@/hooks/useAuth";

export function ProtectedRoute() {
  const { isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
}
```

## React Hook Form + Zod (DO)
```tsx
// src/components/CreateOrderForm.tsx
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { useCreateOrder } from "@/hooks/useOrders";

const createOrderSchema = z.object({
  customerName: z.string().min(2, "Name muss mindestens 2 Zeichen lang sein"),
  total: z.coerce.number().positive("Betrag muss positiv sein"),
  email: z.string().email("Ungueltige E-Mail-Adresse"),
});

type CreateOrderValues = z.infer<typeof createOrderSchema>;

export function CreateOrderForm() {
  const createOrder = useCreateOrder();

  const form = useForm<CreateOrderValues>({
    resolver: zodResolver(createOrderSchema),
    defaultValues: { customerName: "", total: 0, email: "" },
  });

  async function onSubmit(values: CreateOrderValues) {
    await createOrder.mutateAsync(values);
    form.reset();
  }

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
        <FormField
          control={form.control}
          name="customerName"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Kundenname</FormLabel>
              <FormControl>
                <Input placeholder="Max Mustermann" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="total"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Betrag</FormLabel>
              <FormControl>
                <Input type="number" step="0.01" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <Button type="submit" disabled={createOrder.isPending}>
          {createOrder.isPending ? "Erstelle..." : "Bestellung anlegen"}
        </Button>
      </form>
    </Form>
  );
}
```

## Recharts Dashboard (DO)
```tsx
// src/components/OrderChart.tsx
import {
  ResponsiveContainer,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
} from "recharts";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

interface ChartData {
  month: string;
  orders: number;
  revenue: number;
}

interface OrderChartProps {
  data: ChartData[];
}

export function OrderChart({ data }: OrderChartProps) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Bestellungen pro Monat</CardTitle>
      </CardHeader>
      <CardContent>
        <ResponsiveContainer width="100%" height={350}>
          <BarChart data={data}>
            <CartesianGrid strokeDasharray="3 3" className="stroke-muted" />
            <XAxis dataKey="month" className="text-sm" />
            <YAxis className="text-sm" />
            <Tooltip />
            <Legend />
            <Bar dataKey="orders" fill="hsl(var(--primary))" name="Bestellungen" />
            <Bar dataKey="revenue" fill="hsl(var(--secondary))" name="Umsatz (€)" />
          </BarChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
}
```

## ESLint Flat Config (DO)
```javascript
// eslint.config.js
import js from "@eslint/js";
import tsPlugin from "@typescript-eslint/eslint-plugin";
import tsParser from "@typescript-eslint/parser";
import reactHooks from "eslint-plugin-react-hooks";
import reactRefresh from "eslint-plugin-react-refresh";
import tailwindcss from "eslint-plugin-tailwindcss";

export default [
  js.configs.recommended,
  {
    files: ["src/**/*.{ts,tsx}"],
    languageOptions: {
      parser: tsParser,
      parserOptions: { project: "./tsconfig.json" },
    },
    plugins: {
      "@typescript-eslint": tsPlugin,
      "react-hooks": reactHooks,
      "react-refresh": reactRefresh,
      tailwindcss,
    },
    rules: {
      ...reactHooks.configs.recommended.rules,
      "react-refresh/only-export-components": "warn",
      "@typescript-eslint/no-explicit-any": "error",
      "tailwindcss/classnames-order": "warn",
    },
  },
];
```

## Komponenten-Test (DO)
```tsx
// OrderCard.test.tsx
import { describe, it, expect, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { OrderCard } from "./OrderCard";

describe("OrderCard", () => {
  const defaultOrder = { id: 1, title: "Test Order", total: 99.99 };

  it("should render order title and total", () => {
    render(<OrderCard order={defaultOrder} onSelect={vi.fn()} />);

    expect(screen.getByText("Test Order")).toBeInTheDocument();
    expect(screen.getByText("$99.99")).toBeInTheDocument();
  });

  it("should call onSelect when clicked", async () => {
    const onSelect = vi.fn();
    render(<OrderCard order={defaultOrder} onSelect={onSelect} />);

    await userEvent.click(screen.getByText("Test Order"));
    expect(onSelect).toHaveBeenCalledWith(1);
  });
});
```

## Hook Test mit TanStack Query (DO)
```tsx
// hooks/__tests__/useOrders.test.tsx
import { describe, it, expect } from "vitest";
import { renderHook, waitFor } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { http, HttpResponse } from "msw";
import { setupServer } from "msw/node";
import { useOrders } from "../useOrders";

const server = setupServer(
  http.get("/api/v1/orders", () =>
    HttpResponse.json([{ id: 1, title: "Order 1" }])
  )
);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

function createWrapper() {
  const queryClient = new QueryClient({
    defaultOptions: { queries: { retry: false } },
  });
  return ({ children }: { children: React.ReactNode }) => (
    <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  );
}

describe("useOrders", () => {
  it("should fetch orders", async () => {
    const { result } = renderHook(() => useOrders(), {
      wrapper: createWrapper(),
    });

    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    expect(result.current.data).toHaveLength(1);
    expect(result.current.data![0].title).toBe("Order 1");
  });
});
```

## Custom Hook Test (DO)
```tsx
// hooks/__tests__/useAuth.test.tsx
import { describe, it, expect, vi, beforeEach } from "vitest";
import { renderHook, act } from "@testing-library/react";
import { useAuth } from "../useAuth";

describe("useAuth", () => {
  beforeEach(() => {
    localStorage.clear();
    vi.clearAllMocks();
  });

  it("should start unauthenticated", () => {
    const { result } = renderHook(() => useAuth());
    expect(result.current.isAuthenticated).toBe(false);
    expect(result.current.user).toBeNull();
  });
});
```

## Anti-Pattern: useEffect fuer abgeleiteten State (DON'T)
```tsx
// FALSCH
const [items, setItems] = useState<Item[]>([])
const [total, setTotal] = useState(0)
useEffect(() => {
  setTotal(items.reduce((sum, i) => sum + i.price, 0))
}, [items])

// RICHTIG
const total = useMemo(
  () => items.reduce((sum, i) => sum + i.price, 0),
  [items]
)
```
