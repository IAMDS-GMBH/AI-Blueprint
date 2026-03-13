---
applyTo: "**/*.vue,**/*.ts"
---

<!-- Kurzfassung — vollstaendige Rules in .claude/rules/examples/vue-frontend.md -->

# Vue.js 3 + TypeScript

- Composition API (`<script setup lang="ts">`) — NEVER Options API
- Props typisieren: defineProps<Props>()
- NEVER direkter API-Call in Komponenten — via Store/Composable
- CSS: `<style scoped>` — keine globalen Overrides
- NEVER v-html mit User-Daten (XSS)
