<script setup lang="ts">
import ChatView from '@/components/ChatView.vue'
</script>

<template>
  <div class="app-layout">
    <header class="app-header">
      <div class="header-inner">
        <span class="header-icon">⛏</span>
        <h1>Crafting AI</h1>
      </div>
    </header>
    <main class="app-main">
      <ChatView />
    </main>
  </div>
</template>

<style>
@import url('https://fonts.googleapis.com/css2?family=Silkscreen:wght@400;700&display=swap');

*,
*::before,
*::after {
  box-sizing: border-box;
}

:root {
  --mc-dirt: #8B6D4E;
  --mc-dirt-dark: #6B5240;
  --mc-dirt-light: #A0815C;
  --mc-stone: #7F7F7F;
  --mc-stone-dark: #5A5A5A;
  --mc-stone-light: #9E9E9E;
  --mc-grass: #5D9B3A;
  --mc-grass-dark: #4A7D2E;
  --mc-grass-light: #7DC456;
  --mc-text: #FFFFFF;
  --mc-text-shadow: #3F3F3F;
  --mc-gold: #FFAA00;
  --mc-red: #FF5555;
  --mc-black: #1D1D1D;
  --mc-blue: #5555FF;
  --mc-blue-light: #7777FF;
  --mc-inventory-bg: #C6C6C6;
  --mc-inventory-border: #555555;
  --mc-inventory-border-light: #FFFFFF;
}

body {
  margin: 0;
  padding: 0;
  font-family: 'Silkscreen', monospace;
  -webkit-font-smoothing: none;
  -moz-osx-font-smoothing: unset;
  image-rendering: pixelated;
  background-color: var(--mc-black);
}
</style>

<style scoped>
.app-layout {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: var(--mc-dirt-dark);
  background-image:
    repeating-linear-gradient(
      0deg,
      transparent,
      transparent 16px,
      rgba(0,0,0,0.06) 16px,
      rgba(0,0,0,0.06) 32px
    ),
    repeating-linear-gradient(
      90deg,
      transparent,
      transparent 16px,
      rgba(0,0,0,0.06) 16px,
      rgba(0,0,0,0.06) 32px
    );
}

.app-header {
  padding: 0;
  background-color: var(--mc-stone-dark);
  border-bottom: 4px solid var(--mc-black);
  border-top: 4px solid var(--mc-stone-light);
  box-shadow: 0 4px 0 rgba(0,0,0,0.3);
  position: relative;
}

.app-header::before {
  content: '';
  position: absolute;
  inset: 0;
  background-image:
    repeating-linear-gradient(
      90deg,
      transparent,
      transparent 8px,
      rgba(0,0,0,0.05) 8px,
      rgba(0,0,0,0.05) 16px
    );
  pointer-events: none;
}

.header-inner {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 14px 20px;
}

.header-icon {
  font-size: 1.4rem;
  filter: drop-shadow(2px 2px 0 var(--mc-text-shadow));
}

.app-header h1 {
  margin: 0;
  font-family: 'Silkscreen', monospace;
  font-size: 1.2rem;
  font-weight: 700;
  color: var(--mc-gold);
  text-shadow:
    2px 2px 0 var(--mc-text-shadow),
    -1px -1px 0 rgba(0,0,0,0.2);
  letter-spacing: 2px;
  text-transform: uppercase;
}

.app-main {
  flex: 1;
  overflow: hidden;
}
</style>
