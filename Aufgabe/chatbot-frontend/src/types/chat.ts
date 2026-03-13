export interface ChatMessage {
  role: 'user' | 'assistant'
  content: string
}

export interface TraceEntry {
  timestamp: string
  source: 'USER_INPUT' | 'LLM_REQUEST' | 'LLM_RESPONSE' | 'MCP_TOOL_CALL' | 'MCP_TOOL_RESULT'
  action: string
  detail: string | null
  durationMs: number | null
}

export interface ChatRequest {
  message: string
  history: ChatMessage[]
}

export interface ChatResponse {
  response: string
  role: string
  trace?: TraceEntry[]
}
