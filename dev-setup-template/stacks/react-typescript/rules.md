---
description: "React + TypeScript Regeln"
globs: ["src/**/*.tsx", "src/**/*.ts", "src/**/*.jsx", "vite.config.*"]
alwaysApply: false
---

# React + TypeScript

## Komponenten
- Functional Components mit TypeScript (React.FC vermeiden — direkt typisieren)
- Props als Interface definieren: interface Props { ... }
- Destructuring in Funktionssignatur: function Button({ label, onClick }: Props)
- Named Exports (kein default export fuer Komponenten)

## State Management
- Lokaler State: useState, useReducer
- Server State: TanStack React Query — NEVER manuelles fetch + useEffect fuer API-Daten
- Globaler Client State: Zustand (leichtgewichtig) oder React Context
- Query Keys: Konstanten in query-keys.ts — NEVER Strings inline
- Mutations: useMutation + onSuccess → invalidateQueries
- Optimistic Updates: via onMutate + rollback in onError
- NEVER prop drilling ueber 2+ Ebenen — Context oder Zustand nutzen
- NEVER Redux fuer neue Projekte — Zustand oder TanStack Query decken 95% ab

## Routing (React Router DOM)
- createBrowserRouter + RouterProvider (NEVER BrowserRouter + Routes in neuem Code)
- Route-Definitionen in src/routes/ — zentral, nicht verstreut
- Lazy Loading: lazy(() => import("@/pages/Orders"))
- Protected Routes: Wrapper-Komponente mit Auth-Check
- URL Params typisieren: useParams<{ id: string }>()
- Loader/Action Pattern fuer Data-Fetching (optional, alternativ TanStack Query)
- NEVER window.location fuer Navigation — useNavigate() nutzen

## Formulare (React Hook Form + Zod)
- React Hook Form fuer alle Formulare — NEVER unkontrollierte Inputs manuell
- Zod Schemas fuer Validierung: zodResolver(schema) im useForm()
- FormField + Controller Pattern mit shadcn/ui Form Components
- Error Messages direkt am Feld — formState.errors
- Submit Handler: handleSubmit(onSubmit) — typsicher aus Schema
- NEVER onChange-Handler pro Feld manuell — useForm erledigt das
- NEVER eigene Validierungslogik — Zod Schemas wiederverwenden (API + Form)

## Hooks
- Custom Hooks fuer wiederverwendbare Logik: useAuth(), useOrders()
- Hooks immer mit "use" prefixen
- Dependencies in useEffect/useMemo/useCallback korrekt angeben
- NEVER useEffect fuer abgeleiteten State — useMemo verwenden

## Vite + SWC (Build)
- `vite.config.ts` mit @vitejs/plugin-react-swc (SWC statt Babel — 20x schneller)
- Path Aliases via resolve.alias: `'@': path.resolve(__dirname, './src')`
- Environment Variables: `import.meta.env.VITE_*` — NEVER process.env
- Proxy fuer API: `server.proxy` in vite.config.ts (kein CORS in Dev)
- Lazy Loading: `React.lazy(() => import('./pages/Orders'))` + Suspense
- NEVER Create React App (CRA) — deprecated, Vite ist der Standard

## Styling (Tailwind CSS + shadcn/ui)
- Tailwind CSS als Utility-First Framework — NEVER eigene CSS-Klassen fuer Standard-Layouts
- shadcn/ui als Component Library — kopiert in src/components/ui/ (nicht als npm Dependency)
- `cn()` Helper (clsx + tailwind-merge) fuer bedingte Klassen
- Responsive: Mobile-First (sm:, md:, lg: Breakpoints)
- Dark Mode: class-basiert (`dark:` Prefix) via ThemeProvider
- NEVER globale CSS-Overrides — Tailwind @apply nur in Ausnahmefaellen
- NEVER eigene Button/Input/Dialog Komponenten — shadcn/ui nutzen
- Farben: CSS Variables in globals.css (--primary, --secondary etc.)

## Testing (Vitest + Testing Library)
- Vitest als Test Runner — gleiche Config wie Vite
- @testing-library/react + @testing-library/user-event fuer Komponenten
- Test-Typen: Unit (Hooks/Utils) → Component (Render/Interaction) → E2E (Playwright)
- Test-Dateien: `ComponentName.test.tsx` neben der Komponente oder in `__tests__/`
- Test-Naming: it("should render order details when order is loaded")
- Mocking: vi.mock() fuer Module — API-Calls via MSW (Mock Service Worker) mocken
- TanStack Query: QueryClientProvider mit frischem Client pro Test
- User-Events: userEvent.click() statt fireEvent.click() (realistischer)
- NEVER Implementation Details testen — Verhalten testen (was der User sieht)
- NEVER Snapshot Tests als einzigen Test
- NEVER getByTestId als erste Wahl — getByRole, getByText, getByLabelText bevorzugen

## Charts (Recharts)
- Recharts fuer Diagramme — deklarativ, React-nativ
- ResponsiveContainer als Wrapper (100% Breite/Hoehe)
- Daten-Format: Array of Objects mit konsistenten Keys
- Tooltips + Legends immer einbinden fuer Barrierefreiheit
- Farben: Tailwind CSS Variables oder Design-System Palette
- NEVER Chart.js oder D3 direkt — Recharts abstrahiert React-konform

## Linting (ESLint)
- ESLint 9+ mit Flat Config (eslint.config.js)
- Plugins: @typescript-eslint, eslint-plugin-react-hooks, eslint-plugin-react-refresh
- Tailwind: eslint-plugin-tailwindcss fuer Klassen-Sortierung
- NEVER eslint-disable ohne Kommentar warum
- Pre-Commit: lint-staged + husky fuer automatisches Linting

## Verbote
- NEVER `any` Type — unknown oder konkrete Typen
- NEVER Class Components in neuem Code
- NEVER index als key bei dynamischen Listen
- NEVER direkte DOM-Manipulation (document.getElementById)
- NEVER fetch in Komponenten ohne Abstraction Layer
