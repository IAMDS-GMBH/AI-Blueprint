---
description: "Vue.js 3 + TypeScript Regeln"
globs: ["src/**/*.vue", "src/**/*.ts", "vite.config.*"]
alwaysApply: false
---

# Vue.js 3 + TypeScript

## Komponenten-Struktur (Reihenfolge)
1. Imports → 2. Props → 3. Emits → 4. Store → 5. State (ref/reactive) → 6. Computed → 7. Methods → 8. Lifecycle Hooks

## Pflichten
- Immer `<script setup lang="ts">` — Composition API + TypeScript
- Props typisieren: defineProps<Props>()
- Emits typisieren: defineEmits<{ click: [MouseEvent] }>()
- CSS: `<style scoped>` — keine globalen Overrides
- Komponenten: PascalCase, Single Responsibility
- Routing: Lazy Loading via () => import()

## State Management
- Pinia fuer globalen State — defineStore mit Setup-Syntax
- Composables fuer geteilte Logik zwischen Komponenten
- ref() fuer primitive Werte, reactive() fuer Objekte
- Keine direkte Store-Mutation ausserhalb von Actions

## Vite (Build)
- `vite.config.ts` fuer Konfiguration
- Path Aliases via resolve.alias: `'@': path.resolve(__dirname, './src')`
- Environment Variables: `import.meta.env.VITE_*` — NEVER process.env
- Proxy fuer API: `server.proxy` in vite.config.ts (kein CORS in Dev)

## API-Calls
- NEVER direkt in Komponenten — immer via Pinia Store oder Composable
- Axios mit zentraler Instanz (baseURL, Interceptors)
- Error Handling: Global via Axios Interceptor + lokale Fallbacks
- Loading States: isLoading ref in jedem API-Composable

## Testing (Vitest + Vue Test Utils)
- Vitest als Test Runner — gleiche Config wie Vite (vite.config.ts)
- Vue Test Utils fuer Komponenten: mount() fuer vollstaendig, shallowMount() fuer isoliert
- Test-Typen: Unit (Services/Composables) → Component (Render/Props/Emits) → E2E (Playwright)
- Test-Dateien: `ComponentName.test.ts` neben der Komponente oder in `__tests__/`
- Test-Naming: it("should render order list when orders are loaded")
- Mocking: vi.mock() fuer Module, vi.fn() fuer Funktionen — NEVER echte API-Calls in Unit Tests
- Stores testen: Pinia createTestingPinia() mit initialState
- Coverage: Mindestens Render + Props + Emits pro Komponente
- NEVER Snapshot Tests als einzigen Test — immer Behavior Tests
- NEVER Implementation Details testen (interne State-Variablen, private Methoden)

## Verbote
- NEVER Options API in neuem Code (data(), methods:, computed:)
- NEVER v-html mit User-Daten (XSS)
- NEVER `this` in Composition API
- NEVER globale CSS-Overrides
- NEVER any-Type — immer konkrete Typen oder unknown
