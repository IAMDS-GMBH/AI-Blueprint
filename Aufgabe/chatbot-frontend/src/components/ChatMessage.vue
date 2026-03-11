<script setup lang="ts">
import type { ChatMessage } from '@/types/chat'

interface Props {
  message: ChatMessage
}

defineProps<Props>()
</script>

<template>
  <div class="message-row" :class="message.role">
    <div class="message-block" :class="message.role">
      <div class="message-avatar" :class="message.role">
        {{ message.role === 'user' ? '🧑' : '🤖' }}
      </div>
      <div class="message-body">
        <span class="message-label">{{ message.role === 'user' ? 'Steve' : 'Crafting AI' }}</span>
        <div class="message-bubble" :class="message.role">
          <p class="message-content">{{ message.content }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.message-row {
  display: flex;
  margin-bottom: 8px;
  animation: block-place 0.15s steps(3) both;
}

@keyframes block-place {
  0% {
    opacity: 0;
    transform: scale(0.8);
  }
  100% {
    opacity: 1;
    transform: scale(1);
  }
}

.message-row.user {
  justify-content: flex-end;
}

.message-row.assistant {
  justify-content: flex-start;
}

.message-block {
  display: flex;
  gap: 8px;
  max-width: 75%;
  align-items: flex-start;
}

.message-block.user {
  flex-direction: row-reverse;
}

.message-avatar {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.1rem;
  flex-shrink: 0;
  border: 3px solid var(--mc-black);
  image-rendering: pixelated;
}

.message-avatar.user {
  background-color: var(--mc-blue);
  border-top-color: var(--mc-blue-light);
  border-left-color: var(--mc-blue-light);
}

.message-avatar.assistant {
  background-color: var(--mc-grass);
  border-top-color: var(--mc-grass-light);
  border-left-color: var(--mc-grass-light);
}

.message-body {
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.message-label {
  font-family: 'Silkscreen', monospace;
  font-size: 0.55rem;
  color: var(--mc-gold);
  text-shadow: 1px 1px 0 var(--mc-text-shadow);
  padding: 0 4px;
  letter-spacing: 1px;
}

.message-row.user .message-label {
  text-align: right;
}

.message-bubble {
  padding: 10px 14px;
  word-wrap: break-word;
  border: 3px solid var(--mc-black);
  position: relative;
}

.message-bubble.user {
  background-color: var(--mc-blue);
  border-top-color: var(--mc-blue-light);
  border-left-color: var(--mc-blue-light);
  color: var(--mc-text);
}

.message-bubble.assistant {
  background-color: var(--mc-stone);
  border-top-color: var(--mc-stone-light);
  border-left-color: var(--mc-stone-light);
  color: var(--mc-text);
}

.message-content {
  margin: 0;
  font-family: 'Silkscreen', monospace;
  font-size: 0.7rem;
  line-height: 1.8;
  text-shadow: 1px 1px 0 rgba(0,0,0,0.4);
  white-space: pre-wrap;
}
</style>
