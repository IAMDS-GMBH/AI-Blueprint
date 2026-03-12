<script setup lang="ts">
import ChatView from '@/components/ChatView.vue'
</script>

<template>
  <div class="app-layout">
    <header class="app-header">
      <div class="header-left">
        <div class="logo-mark"></div>
        <h1>Chatbot<span class="logo-dot">.</span></h1>
      </div>
      <div class="header-right">
        <span class="status-badge">
          <span class="status-dot"></span>
          Online
        </span>
      </div>
    </header>
    <main class="app-main">
      <ChatView />
    </main>
  </div>
</template>

<style>
@import url('https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700&family=Source+Sans+3:ital,wght@0,300;0,400;0,500;0,600;1,400&display=swap');

*,
*::before,
*::after {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

:root {
  --obsidian: #0A0A0B;
  --obsidian-light: #131316;
  --obsidian-lighter: #1C1C21;
  --obsidian-border: #2A2A32;
  --obsidian-hover: #232329;
  --amber: #E8A84C;
  --amber-dim: #C48A35;
  --amber-glow: rgba(232, 168, 76, 0.12);
  --amber-glow-strong: rgba(232, 168, 76, 0.25);
  --text-primary: #EDEDEF;
  --text-secondary: #8E8E96;
  --text-tertiary: #5A5A63;
  --glass-bg: rgba(28, 28, 33, 0.65);
  --glass-border: rgba(255, 255, 255, 0.06);
  --glass-shine: rgba(255, 255, 255, 0.03);
  --error-red: #E85C5C;
  --error-bg: rgba(232, 92, 92, 0.1);
  --success-green: #5CE87C;
  --radius-sm: 8px;
  --radius-md: 12px;
  --radius-lg: 16px;
  --radius-xl: 20px;
  --font-display: 'Outfit', sans-serif;
  --font-body: 'Source Sans 3', sans-serif;
}

body {
  margin: 0;
  padding: 0;
  font-family: var(--font-body);
  background-color: var(--obsidian);
  color: var(--text-primary);
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>

<style scoped>
.app-layout {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: var(--obsidian);
  background-image:
    radial-gradient(ellipse 80% 50% at 50% -20%, var(--amber-glow), transparent),
    radial-gradient(circle at 100% 100%, rgba(232, 168, 76, 0.03), transparent 50%);
}

.app-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 24px;
  border-bottom: 1px solid var(--glass-border);
  background: var(--glass-bg);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  position: relative;
  z-index: 10;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-mark {
  width: 28px;
  height: 28px;
  border-radius: var(--radius-sm);
  background: linear-gradient(135deg, var(--amber), var(--amber-dim));
  box-shadow: 0 0 20px var(--amber-glow-strong);
  position: relative;
}

.logo-mark::after {
  content: '';
  position: absolute;
  inset: 3px;
  border-radius: 4px;
  border: 1.5px solid rgba(255, 255, 255, 0.3);
  border-bottom-color: transparent;
  border-right-color: transparent;
}

.app-header h1 {
  font-family: var(--font-display);
  font-size: 1.15rem;
  font-weight: 600;
  color: var(--text-primary);
  letter-spacing: -0.02em;
}

.logo-dot {
  color: var(--amber);
}

.header-right {
  display: flex;
  align-items: center;
}

.status-badge {
  display: flex;
  align-items: center;
  gap: 6px;
  font-family: var(--font-body);
  font-size: 0.75rem;
  font-weight: 500;
  color: var(--text-secondary);
  padding: 5px 12px;
  border-radius: 100px;
  background: var(--obsidian-lighter);
  border: 1px solid var(--obsidian-border);
}

.status-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background-color: var(--success-green);
  box-shadow: 0 0 8px rgba(92, 232, 124, 0.4);
  animation: pulse 2.5s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.app-main {
  flex: 1;
  overflow: hidden;
}
</style>
