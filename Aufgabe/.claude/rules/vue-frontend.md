---
description: Regeln fuer Vue.js 3 und TypeScript Entwicklung – automatisch aktiv bei .vue und .ts Dateien
globs: ["**/*.vue", "**/*.ts"]
alwaysApply: false
---

# Vue.js 3 + TypeScript – Pflichtregeln

## Komponenten-Struktur (Reihenfolge einhalten)
```vue
<script setup lang="ts">
// 1. Imports
// 2. Props (typisiert via Interface)
// 3. Emits (typisiert)
// 4. Store
// 5. State (ref/reactive)
// 6. Computed
// 7. Methods
// 8. Lifecycle Hooks
</script>
```

## Pflichten
- `<script setup lang="ts">` – immer TypeScript, immer Composition API
- Props mit Interface typisieren: `defineProps<Props>()`
- Emits explizit typisieren: `defineEmits<{ click: [MouseEvent] }>()`
- CSS: `<style scoped>` – keine globalen Overrides

## Verbote
- **Kein** direkter API-Call in Komponenten – immer via Pinia Store oder Composable
- **Keine** Options API in neuem Code (`data()`, `methods:`, `computed:`)
- **Kein** `v-html` mit User-Daten (XSS-Risiko)
- **Kein** `this` in Composition API
- **Keine** globalen CSS-Overrides

## Testing (Vitest)
Bei jeder neuen/geaenderten Komponente:
- Render-Test: Komponente rendert ohne Fehler
- Props-Test: Alle Pflicht-Props werden korrekt gerendert
- Emit-Test: Events werden bei Interaktion ausgeloest
- E2E-Flows (User-Journey ueber mehrere Seiten) → Test Agent delegieren
