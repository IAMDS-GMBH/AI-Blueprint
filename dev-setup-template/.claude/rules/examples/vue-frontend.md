---
description: "BEISPIEL: Vue.js 3 + TypeScript — Vollstaendige Version in stacks/vue-typescript/"
globs: ["src/**/*.vue", "src/**/*.ts"]
alwaysApply: false
---

<!--
  HINWEIS: Dies ist ein Stub. Die vollstaendige Rule liegt in:
  stacks/vue-typescript/rules.md    (Rule mit Frontmatter)
  stacks/vue-typescript/snippets.md (Code-Patterns)

  setup.sh kopiert alle Stacks nach .claude/rules/stacks/.
  /configure aktiviert die passenden automatisch.
-->

# Vue.js 3 + TypeScript (Kurzversion)

- Immer `<script setup lang="ts">` — Composition API + TypeScript
- Props typisieren: defineProps<Props>()
- CSS: `<style scoped>`
- NEVER direkter API-Call in Komponenten
- NEVER Options API in neuem Code

→ Vollstaendige Version: `stacks/vue-typescript/`
