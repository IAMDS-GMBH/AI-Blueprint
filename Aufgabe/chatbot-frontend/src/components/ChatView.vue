<script setup lang="ts">
import { ref, nextTick, watch, computed } from 'vue'
import { useChatStore } from '@/stores/chat'
import ChatMessage from '@/components/ChatMessage.vue'
import TracePanel from '@/components/TracePanel.vue'

const chatStore = useChatStore()

const inputText = ref('')
const messagesContainer = ref<HTMLElement | null>(null)

const hasTraces = computed(() => chatStore.traceLog.length > 0)

async function handleSend() {
  const text = inputText.value.trim()
  if (!text || chatStore.loading) return

  inputText.value = ''
  await chatStore.sendMessage(text)
}

function handleKeydown(event: KeyboardEvent) {
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault()
    handleSend()
  }
}

function scrollToBottom() {
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

function handleMessageClick(index: number) {
  const traceIndex = Math.floor(index / 2)
  if (traceIndex < chatStore.traceLog.length) {
    chatStore.selectTrace(traceIndex)
  }
}

watch(
  () => chatStore.messages.length,
  () => {
    nextTick(scrollToBottom)
  }
)
</script>

<template>
  <div class="chat-layout">
    <div class="chat-container">
      <div class="chat-toolbar">
        <button
          class="trace-toggle"
          :class="{ active: chatStore.showTrace }"
          @click="chatStore.toggleTrace()"
        >
          <span v-if="hasTraces" class="trace-dot-indicator"></span>
          <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
            <path d="M2 3H12M2 7H9M2 11H6" stroke="currentColor" stroke-width="1.4" stroke-linecap="round"/>
          </svg>
          Trace
        </button>
      </div>

      <div ref="messagesContainer" class="messages-list">
        <div v-if="chatStore.messages.length === 0" class="empty-state">
          <div class="empty-glow"></div>
          <div class="empty-icon">
            <svg width="48" height="48" viewBox="0 0 48 48" fill="none">
              <rect x="4" y="8" width="40" height="28" rx="6" stroke="currentColor" stroke-width="2" opacity="0.3"/>
              <path d="M4 18H44" stroke="currentColor" stroke-width="2" opacity="0.15"/>
              <circle cx="14" cy="26" r="2" fill="currentColor" opacity="0.2"/>
              <circle cx="22" cy="26" r="2" fill="currentColor" opacity="0.3"/>
              <circle cx="30" cy="26" r="2" fill="currentColor" opacity="0.2"/>
              <path d="M18 40L24 34L30 40" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" opacity="0.2"/>
            </svg>
          </div>
          <p class="empty-title">Stelle eine Frage</p>
          <p class="empty-hint">Schreibe eine Nachricht um den Chat zu starten</p>
        </div>
        <ChatMessage
          v-for="(msg, index) in chatStore.messages"
          :key="index"
          :message="msg"
          @click="handleMessageClick(index)"
        />
        <div v-if="chatStore.loading" class="loading-indicator">
          <div class="loading-avatar">AI</div>
          <div class="loading-content">
            <span class="loading-dots">
              <span></span>
              <span></span>
              <span></span>
            </span>
          </div>
        </div>
      </div>

      <div v-if="chatStore.error" class="error-banner">
        <svg width="16" height="16" viewBox="0 0 16 16" fill="none" class="error-icon">
          <circle cx="8" cy="8" r="7" stroke="currentColor" stroke-width="1.5"/>
          <path d="M8 5V9M8 11V11.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
        </svg>
        <span>{{ chatStore.error }}</span>
      </div>

      <div class="input-area">
        <div class="input-wrapper">
          <textarea
            v-model="inputText"
            placeholder="Nachricht schreiben..."
            rows="1"
            :disabled="chatStore.loading"
            @keydown="handleKeydown"
          ></textarea>
          <button
            class="send-button"
            :disabled="chatStore.loading || !inputText.trim()"
            @click="handleSend"
          >
            <svg width="18" height="18" viewBox="0 0 18 18" fill="none">
              <path d="M3 9H15M15 9L10 4M15 9L10 14" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </button>
        </div>
        <p class="input-hint">Enter zum Senden · Shift+Enter fuer neue Zeile</p>
      </div>
    </div>

    <Transition name="trace-slide">
      <TracePanel v-if="chatStore.showTrace" />
    </Transition>
  </div>
</template>

<style scoped>
.chat-layout {
  display: flex;
  height: 100%;
  overflow: hidden;
}

.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  flex: 1;
  min-width: 0;
  max-width: 820px;
  margin: 0 auto;
  padding: 0 8px;
}

.chat-layout:has(.trace-panel) .chat-container {
  margin: 0;
  margin-left: auto;
}

/* Trace Toggle */
.chat-toolbar {
  display: flex;
  justify-content: flex-end;
  padding: 8px 12px 0;
  flex-shrink: 0;
}

.trace-toggle {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 5px 12px;
  font-family: var(--font-body);
  font-size: 0.7rem;
  font-weight: 500;
  color: var(--text-tertiary);
  background: var(--obsidian-lighter);
  border: 1px solid var(--obsidian-border);
  border-radius: 100px;
  cursor: pointer;
  transition: all 0.15s ease;
  position: relative;
}

.trace-toggle:hover {
  color: var(--text-secondary);
  border-color: var(--text-tertiary);
}

.trace-toggle.active {
  color: var(--amber);
  border-color: var(--amber-dim);
  background: var(--amber-glow);
}

.trace-dot-indicator {
  width: 5px;
  height: 5px;
  border-radius: 50%;
  background: var(--amber);
  box-shadow: 0 0 6px var(--amber-glow-strong);
}

/* Trace Transition */
.trace-slide-enter-active {
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}

.trace-slide-leave-active {
  transition: all 0.2s ease-in;
}

.trace-slide-enter-from {
  opacity: 0;
  transform: translateX(20px);
}

.trace-slide-leave-to {
  opacity: 0;
  transform: translateX(20px);
}

/* Scrollbar */
.messages-list {
  flex: 1;
  overflow-y: auto;
  padding: 20px 12px 12px;
  scroll-behavior: smooth;
  scrollbar-width: thin;
  scrollbar-color: var(--obsidian-border) transparent;
}

.messages-list::-webkit-scrollbar {
  width: 6px;
}

.messages-list::-webkit-scrollbar-track {
  background: transparent;
}

.messages-list::-webkit-scrollbar-thumb {
  background: var(--obsidian-border);
  border-radius: 3px;
}

.messages-list::-webkit-scrollbar-thumb:hover {
  background: var(--text-tertiary);
}

/* Empty State */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  gap: 12px;
  position: relative;
}

.empty-glow {
  position: absolute;
  width: 200px;
  height: 200px;
  border-radius: 50%;
  background: var(--amber-glow);
  filter: blur(80px);
  pointer-events: none;
}

.empty-icon {
  color: var(--amber);
  opacity: 0.8;
  animation: float 4s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-6px); }
}

.empty-title {
  font-family: var(--font-display);
  font-size: 1.2rem;
  font-weight: 600;
  color: var(--text-primary);
  letter-spacing: -0.02em;
}

.empty-hint {
  font-family: var(--font-body);
  font-size: 0.85rem;
  color: var(--text-tertiary);
}

/* Loading */
.loading-indicator {
  display: flex;
  gap: 10px;
  padding: 4px 0 12px;
  align-items: flex-start;
  animation: message-in 0.35s cubic-bezier(0.16, 1, 0.3, 1) both;
}

@keyframes message-in {
  0% { opacity: 0; transform: translateY(12px); }
  100% { opacity: 1; transform: translateY(0); }
}

.loading-avatar {
  width: 32px;
  height: 32px;
  border-radius: var(--radius-sm);
  background: linear-gradient(135deg, var(--amber), var(--amber-dim));
  color: var(--obsidian);
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: var(--font-display);
  font-size: 0.65rem;
  font-weight: 600;
  letter-spacing: 0.05em;
  flex-shrink: 0;
  margin-top: 20px;
}

.loading-content {
  padding: 14px 18px;
  background: var(--obsidian-lighter);
  border: 1px solid var(--obsidian-border);
  border-radius: var(--radius-md);
  border-bottom-left-radius: 4px;
  margin-top: 20px;
}

.loading-dots {
  display: flex;
  gap: 5px;
  align-items: center;
}

.loading-dots span {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background-color: var(--amber);
  animation: dot-pulse 1.4s ease-in-out infinite;
}

.loading-dots span:nth-child(2) {
  animation-delay: 0.15s;
}

.loading-dots span:nth-child(3) {
  animation-delay: 0.3s;
}

@keyframes dot-pulse {
  0%, 80%, 100% {
    opacity: 0.2;
    transform: scale(0.8);
  }
  40% {
    opacity: 1;
    transform: scale(1);
  }
}

/* Error */
.error-banner {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 10px 16px;
  margin: 0 12px;
  background: var(--error-bg);
  border: 1px solid rgba(232, 92, 92, 0.15);
  border-radius: var(--radius-sm);
  color: var(--error-red);
  font-family: var(--font-body);
  font-size: 0.8rem;
  font-weight: 500;
  animation: message-in 0.3s ease both;
}

.error-icon {
  flex-shrink: 0;
}

/* Input Area */
.input-area {
  padding: 12px 12px 16px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.input-wrapper {
  display: flex;
  align-items: flex-end;
  gap: 0;
  background: var(--obsidian-lighter);
  border: 1px solid var(--obsidian-border);
  border-radius: var(--radius-lg);
  padding: 4px 4px 4px 18px;
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
}

.input-wrapper:focus-within {
  border-color: var(--amber-dim);
  box-shadow: 0 0 0 3px var(--amber-glow), 0 4px 24px rgba(0, 0, 0, 0.3);
}

.input-wrapper textarea {
  flex: 1;
  padding: 10px 0;
  background: transparent;
  color: var(--text-primary);
  border: none;
  font-family: var(--font-body);
  font-size: 0.9rem;
  resize: none;
  outline: none;
  line-height: 1.5;
  caret-color: var(--amber);
}

.input-wrapper textarea::placeholder {
  color: var(--text-tertiary);
}

.input-wrapper textarea:disabled {
  color: var(--text-tertiary);
}

.send-button {
  width: 38px;
  height: 38px;
  border-radius: var(--radius-md);
  background: linear-gradient(135deg, var(--amber), var(--amber-dim));
  color: var(--obsidian);
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: transform 0.15s ease, opacity 0.15s ease, box-shadow 0.15s ease;
}

.send-button:hover:not(:disabled) {
  transform: scale(1.05);
  box-shadow: 0 0 20px var(--amber-glow-strong);
}

.send-button:active:not(:disabled) {
  transform: scale(0.95);
}

.send-button:disabled {
  opacity: 0.25;
  cursor: not-allowed;
}

.input-hint {
  font-family: var(--font-body);
  font-size: 0.7rem;
  color: var(--text-tertiary);
  text-align: center;
  padding: 0 4px;
}
</style>
