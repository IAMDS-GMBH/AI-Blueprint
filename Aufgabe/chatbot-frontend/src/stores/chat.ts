import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { ChatMessage, TraceEntry } from '@/types/chat'
import { sendMessage as sendChatMessage } from '@/services/api'

export const useChatStore = defineStore('chat', () => {
  const messages = ref<ChatMessage[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)
  const traceLog = ref<TraceEntry[][]>([])
  const showTrace = ref(false)
  const activeTraceIndex = ref<number | null>(null)

  async function sendMessage(content: string) {
    const userMessage: ChatMessage = { role: 'user', content }
    messages.value.push(userMessage)

    loading.value = true
    error.value = null

    try {
      const response = await sendChatMessage({
        message: content,
        history: messages.value.slice(0, -1)
      })

      const assistantMessage: ChatMessage = {
        role: 'assistant',
        content: response.response
      }
      messages.value.push(assistantMessage)
      traceLog.value.push(response.trace ?? [])
      activeTraceIndex.value = traceLog.value.length - 1
    } catch (err) {
      error.value = 'Backend nicht erreichbar. Bitte versuche es erneut.'
      setTimeout(() => {
        error.value = null
      }, 5000)
    } finally {
      loading.value = false
    }
  }

  function toggleTrace() {
    showTrace.value = !showTrace.value
  }

  function selectTrace(index: number) {
    activeTraceIndex.value = index
    if (!showTrace.value) {
      showTrace.value = true
    }
  }

  return { messages, loading, error, traceLog, showTrace, activeTraceIndex, sendMessage, toggleTrace, selectTrace }
})
