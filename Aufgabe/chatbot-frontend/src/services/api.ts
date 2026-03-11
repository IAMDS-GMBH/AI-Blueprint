import axios from 'axios'
import type { ChatRequest, ChatResponse } from '@/types/chat'

const apiClient = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json'
  }
})

export async function sendMessage(request: ChatRequest): Promise<ChatResponse> {
  const { data } = await apiClient.post<ChatResponse>('/api/v1/chat', request)
  return data
}
