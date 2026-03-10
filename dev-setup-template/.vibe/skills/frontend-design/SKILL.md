---
name: frontend-design
description: Wird geladen wenn Vue.js Komponenten aus Design-Vorgaben, Screenshots, Figma-Beschreibungen oder UI-Anforderungen generiert werden sollen.
license: MIT
compatibility: Python 3.12+
user-invocable: true
allowed-tools:
  - read_file
  - write_file
  - patch_file
  - grep
---

# Skill: Frontend Design (Vue.js 3)

## Wann verwenden
- "Erstell eine Komponente die so aussieht wie..."
- "Baue ein UI fuer [Beschreibung/Screenshot/Figma-Link]"
- "Mach eine moderne Card-Komponente mit..."
- Wenn Figma MCP aktiv ist: Design direkt aus Figma in Vue umwandeln

## Prinzipien
1. Mobile-First Design
2. Konsistentes Design-System (Farben, Spacing, Typography)
3. Accessibility (WCAG 2.1 AA)
4. Performance (keine unnoetige Abhaengigkeiten)

## Workflow

### Schritt 1: Design analysieren
- Was sind die sichtbaren UI-Elemente?
- Welche Interaktionen gibt es (Clicks, Hover, Loading-States)?
- Welche Props braucht die Komponente?
- Welche Events emittiert sie?

### Schritt 2: Komponenten-Struktur planen
```
Komponente:   PascalCase, Single File Component
Props:        TypeScript Interface definieren
Emits:        Explizit typisiert
Slots:        Fuer flexible Inhalte
Composables:  Fuer wiederverwendbare Logik
```

### Schritt 3: Implementieren

**Pflicht-Template fuer jede neue Komponente:**
```vue
<script setup lang="ts">
// 1. Imports
import { ref, computed } from 'vue'

// 2. Props (immer typisiert)
interface Props {
  title: string
  variant?: 'primary' | 'secondary' | 'danger'
  disabled?: boolean
  loading?: boolean
}
const props = withDefaults(defineProps<Props>(), {
  variant: 'primary',
  disabled: false,
  loading: false
})

// 3. Emits (immer typisiert)
const emit = defineEmits<{
  click: [event: MouseEvent]
  submit: [data: FormData]
}>()

// 4. State
const isExpanded = ref(false)

// 5. Computed
const classes = computed(() => ({
  [`btn-${props.variant}`]: true,
  'btn-disabled': props.disabled,
  'btn-loading': props.loading
}))
</script>

<template>
  <button
    :class="classes"
    :disabled="disabled || loading"
    @click="emit('click', $event)"
  >
    <span v-if="loading" class="spinner" aria-label="Laden..." />
    <slot />
  </button>
</template>

<style scoped>
/* Scoped CSS – keine globalen Overrides */
.btn-primary { /* ... */ }
.btn-secondary { /* ... */ }
.btn-danger { /* ... */ }
</style>
```

## Design-Qualitaets-Checkliste

- [ ] Responsive Design (Mobile-First)
- [ ] Loading-State vorhanden (wo sinnvoll)
- [ ] Empty-State vorhanden (fuer Listen/Tabellen)
- [ ] Error-State vorhanden
- [ ] Keyboard-Navigation funktioniert (tabindex, Enter/Escape)
- [ ] ARIA-Labels fuer Screen Reader
- [ ] Props haben sinnvolle Default-Werte
- [ ] Keine direkten API-Calls in der Komponente (immer Pinia Store/Composable)

## Mit Figma MCP (wenn konfiguriert)

Wenn der Figma MCP Server aktiv ist:
```
"Konvertiere dieses Figma-Design in eine Vue-Komponente: [Figma-Link oder Node-ID]"
→ Figma MCP liest das Design
→ Dieser Skill generiert die Vue-Komponente nach unseren Standards
```

## Output-Format

```
## Komponente: [Name].vue

### Props
| Prop | Typ | Default | Beschreibung |
|------|-----|---------|--------------|
| ... | ... | ... | ... |

### Events
| Event | Payload | Wann |
|-------|---------|------|
| ... | ... | ... |

### Verwendung
[Code-Beispiel wie die Komponente eingebunden wird]
```
