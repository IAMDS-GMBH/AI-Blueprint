---
applyTo: "**/*.vue,**/*.ts,**/*.js"
---

# Vue.js 3 + TypeScript Standards

- Composition API (`<script setup>`) – keine Options API in neuem Code
- Komponenten: PascalCase, Single Responsibility
- Props immer mit TypeScript-Types definieren (`defineProps<{...}>()`)
- Kein direkter API-Call in Komponenten – immer via Pinia Store oder Composable
- CSS: Scoped styles (`<style scoped>`) – keine globalen Overrides
- Kein `any` in TypeScript ohne explizite Begründung
- Axios-Calls immer in eigenem Service/Composable kapseln
