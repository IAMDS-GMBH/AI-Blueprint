<script setup lang="ts">
import { ref, nextTick, watch } from 'vue'
import { useChatStore } from '@/stores/chat'
import ChatMessage from '@/components/ChatMessage.vue'

const chatStore = useChatStore()

const inputText = ref('')
const messagesContainer = ref<HTMLElement | null>(null)

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

watch(
  () => chatStore.messages.length,
  () => {
    nextTick(scrollToBottom)
  }
)
</script>

<template>
  <div class="chat-container">
    <div ref="messagesContainer" class="messages-list">
      <div v-if="chatStore.messages.length === 0" class="empty-state">
        <div class="empty-icon">📖</div>
        <p class="empty-title">&lt;Leeres Buch&gt;</p>
        <p class="empty-hint">Druecke Enter um zu craften...</p>
      </div>
      <ChatMessage
        v-for="(msg, index) in chatStore.messages"
        :key="index"
        :message="msg"
      />
      <div v-if="chatStore.loading" class="loading-indicator">
        <span class="block block-1"></span>
        <span class="block block-2"></span>
        <span class="block block-3"></span>
      </div>
    </div>

    <div v-if="chatStore.error" class="error-banner">
      <span class="error-icon">💥</span>
      {{ chatStore.error }}
    </div>

    <div class="input-area">
      <div class="input-wrapper">
        <textarea
          v-model="inputText"
          placeholder="> Nachricht eingeben..."
          rows="1"
          :disabled="chatStore.loading"
          @keydown="handleKeydown"
        ></textarea>
      </div>
      <button
        :disabled="chatStore.loading || !inputText.trim()"
        @click="handleSend"
      >
        Craft!
      </button>
    </div>
  </div>
</template>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  max-width: 850px;
  margin: 0 auto;
}

.messages-list {
  flex: 1;
  overflow-y: auto;
  padding: 20px 16px;
  scrollbar-width: thin;
  scrollbar-color: var(--mc-stone-dark) var(--mc-dirt-dark);
}

.messages-list::-webkit-scrollbar {
  width: 12px;
}

.messages-list::-webkit-scrollbar-track {
  background: var(--mc-dirt-dark);
  border-left: 2px solid var(--mc-black);
}

.messages-list::-webkit-scrollbar-thumb {
  background: var(--mc-stone);
  border: 2px solid var(--mc-black);
  border-top-color: var(--mc-stone-light);
  border-left-color: var(--mc-stone-light);
}

.messages-list::-webkit-scrollbar-thumb:hover {
  background: var(--mc-stone-light);
}

/* Empty State */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  gap: 8px;
}

.empty-icon {
  font-size: 3rem;
  animation: float 2s steps(4) infinite alternate;
}

@keyframes float {
  0% { transform: translateY(0); }
  100% { transform: translateY(-8px); }
}

.empty-title {
  font-family: 'Silkscreen', monospace;
  color: var(--mc-gold);
  font-size: 0.9rem;
  text-shadow: 2px 2px 0 var(--mc-text-shadow);
  margin: 0;
}

.empty-hint {
  font-family: 'Silkscreen', monospace;
  color: var(--mc-stone-light);
  font-size: 0.6rem;
  text-shadow: 1px 1px 0 var(--mc-text-shadow);
  margin: 0;
  animation: blink 1s steps(1) infinite;
}

@keyframes blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0; }
}

/* Loading */
.loading-indicator {
  display: flex;
  gap: 6px;
  padding: 12px 16px;
  align-items: flex-end;
}

.block {
  width: 14px;
  height: 14px;
  border: 2px solid var(--mc-black);
  animation: block-bounce 1s steps(2) infinite;
}

.block-1 {
  background-color: var(--mc-grass);
  border-top-color: var(--mc-grass-light);
  border-left-color: var(--mc-grass-light);
  animation-delay: 0s;
}

.block-2 {
  background-color: var(--mc-dirt);
  border-top-color: var(--mc-dirt-light);
  border-left-color: var(--mc-dirt-light);
  animation-delay: 0.2s;
}

.block-3 {
  background-color: var(--mc-stone);
  border-top-color: var(--mc-stone-light);
  border-left-color: var(--mc-stone-light);
  animation-delay: 0.4s;
}

@keyframes block-bounce {
  0%, 100% {
    transform: translateY(0) scale(1);
  }
  50% {
    transform: translateY(-10px) scale(1.1);
  }
}

/* Error */
.error-banner {
  background-color: #4A1010;
  color: var(--mc-red);
  padding: 10px 16px;
  font-family: 'Silkscreen', monospace;
  font-size: 0.6rem;
  text-align: center;
  text-shadow: 1px 1px 0 var(--mc-black);
  border-top: 3px solid var(--mc-red);
  border-bottom: 3px solid var(--mc-black);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.error-icon {
  font-size: 1rem;
}

/* Input Area */
.input-area {
  display: flex;
  gap: 8px;
  padding: 12px 16px;
  background-color: var(--mc-stone-dark);
  border-top: 4px solid var(--mc-stone-light);
  box-shadow: 0 -2px 0 var(--mc-black);
  position: relative;
}

.input-area::before {
  content: '';
  position: absolute;
  inset: 0;
  background-image:
    repeating-linear-gradient(
      90deg,
      transparent,
      transparent 8px,
      rgba(0,0,0,0.04) 8px,
      rgba(0,0,0,0.04) 16px
    );
  pointer-events: none;
}

.input-wrapper {
  flex: 1;
  position: relative;
}

.input-area textarea {
  width: 100%;
  padding: 10px 14px;
  background-color: var(--mc-black);
  color: var(--mc-text);
  border: 3px solid var(--mc-stone);
  border-top-color: var(--mc-inventory-border);
  border-left-color: var(--mc-inventory-border);
  border-bottom-color: var(--mc-stone-light);
  border-right-color: var(--mc-stone-light);
  font-family: 'Silkscreen', monospace;
  font-size: 0.7rem;
  resize: none;
  outline: none;
  line-height: 1.8;
  caret-color: var(--mc-gold);
  text-shadow: 1px 1px 0 rgba(255,255,255,0.1);
}

.input-area textarea::placeholder {
  color: var(--mc-stone);
  text-shadow: none;
}

.input-area textarea:focus {
  border-color: var(--mc-gold);
  border-top-color: var(--mc-dirt-dark);
  border-left-color: var(--mc-dirt-dark);
}

.input-area textarea:disabled {
  background-color: #2A2A2A;
  color: var(--mc-stone);
}

/* Minecraft Button */
.input-area button {
  padding: 10px 22px;
  background-color: var(--mc-grass);
  color: var(--mc-text);
  font-family: 'Silkscreen', monospace;
  font-size: 0.7rem;
  font-weight: 700;
  cursor: pointer;
  white-space: nowrap;
  text-shadow: 2px 2px 0 var(--mc-text-shadow);
  letter-spacing: 1px;
  border: 3px solid var(--mc-black);
  border-top-color: var(--mc-grass-light);
  border-left-color: var(--mc-grass-light);
  position: relative;
  transition: none;
}

.input-area button:hover:not(:disabled) {
  background-color: var(--mc-grass-light);
  border-top-color: #96E86E;
  border-left-color: #96E86E;
}

.input-area button:active:not(:disabled) {
  background-color: var(--mc-grass-dark);
  border-top-color: var(--mc-black);
  border-left-color: var(--mc-black);
  border-bottom-color: var(--mc-grass-light);
  border-right-color: var(--mc-grass-light);
  transform: translateY(1px);
}

.input-area button:disabled {
  background-color: var(--mc-stone-dark);
  border-top-color: var(--mc-stone);
  border-left-color: var(--mc-stone);
  color: var(--mc-stone);
  cursor: not-allowed;
  text-shadow: 1px 1px 0 var(--mc-black);
}
</style>
