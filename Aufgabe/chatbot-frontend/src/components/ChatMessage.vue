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
      <div class="avatar" :class="message.role">
        <span v-if="message.role === 'user'">Du</span>
        <span v-else class="avatar-ai">AI</span>
      </div>
      <div class="message-body">
        <div class="message-meta">
          <span class="message-sender">{{ message.role === 'user' ? 'Du' : 'Chatbot' }}</span>
          <span class="message-time">gerade</span>
        </div>
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
  margin-bottom: 4px;
  animation: message-in 0.35s cubic-bezier(0.16, 1, 0.3, 1) both;
}

@keyframes message-in {
  0% {
    opacity: 0;
    transform: translateY(12px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
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
  gap: 10px;
  max-width: 72%;
  align-items: flex-start;
}

.message-block.user {
  flex-direction: row-reverse;
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: var(--font-display);
  font-size: 0.65rem;
  font-weight: 600;
  flex-shrink: 0;
  margin-top: 20px;
}

.avatar.user {
  background: var(--obsidian-lighter);
  color: var(--text-secondary);
  border: 1px solid var(--obsidian-border);
}

.avatar.assistant {
  background: linear-gradient(135deg, var(--amber), var(--amber-dim));
  color: var(--obsidian);
  box-shadow: 0 0 16px var(--amber-glow);
}

.avatar-ai {
  letter-spacing: 0.05em;
}

.message-body {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.message-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 0 2px;
}

.message-sender {
  font-family: var(--font-display);
  font-size: 0.75rem;
  font-weight: 500;
  color: var(--text-secondary);
}

.message-time {
  font-size: 0.65rem;
  color: var(--text-tertiary);
}

.message-row.user .message-meta {
  flex-direction: row-reverse;
}

.message-bubble {
  padding: 12px 16px;
  word-wrap: break-word;
  border-radius: var(--radius-md);
  position: relative;
}

.message-bubble.user {
  background: linear-gradient(135deg, var(--amber), var(--amber-dim));
  color: var(--obsidian);
  border-bottom-right-radius: 4px;
}

.message-bubble.assistant {
  background: var(--obsidian-lighter);
  border: 1px solid var(--obsidian-border);
  color: var(--text-primary);
  border-bottom-left-radius: 4px;
}

.message-content {
  margin: 0;
  font-family: var(--font-body);
  font-size: 0.9rem;
  line-height: 1.6;
  white-space: pre-wrap;
}

.message-bubble.user .message-content {
  font-weight: 500;
}
</style>
