import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { ChatMessage } from '@/types/chat'
import { sendMessage as sendChatMessage } from '@/services/api'

export const useChatStore = defineStore('chat', () => {
  const messages = ref<ChatMessage[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

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
    } catch (err) {
      error.value = 'Backend nicht erreichbar. Bitte versuche es erneut.'
      setTimeout(() => {
        error.value = null
      }, 5000)
    } finally {
      loading.value = false
    }
  }

  return { messages, loading, error, sendMessage }
})
