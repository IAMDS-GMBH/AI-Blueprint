<script setup lang="ts">
import { ref, computed } from 'vue'
import { useChatStore } from '@/stores/chat'
import type { TraceEntry } from '@/types/chat'

const chatStore = useChatStore()
const expandedEntries = ref<Set<number>>(new Set())

const activeTrace = computed<TraceEntry[]>(() => {
  if (chatStore.activeTraceIndex === null) return []
  return chatStore.traceLog[chatStore.activeTraceIndex] ?? []
})

const sourceConfig: Record<TraceEntry['source'], { color: string; label: string }> = {
  USER_INPUT: { color: '#6B8AFF', label: 'USER' },
  LLM_REQUEST: { color: '#5CE87C', label: 'LLM' },
  LLM_RESPONSE: { color: '#5CE87C', label: 'LLM' },
  MCP_TOOL_CALL: { color: 'var(--amber)', label: 'MCP' },
  MCP_TOOL_RESULT: { color: '#C084FC', label: 'MCP' }
}

function getRelativeTime(entry: TraceEntry, index: number): string {
  if (index === 0 || activeTrace.value.length === 0) return '+0ms'
  const first = new Date(activeTrace.value[0].timestamp).getTime()
  const current = new Date(entry.timestamp).getTime()
  const diff = current - first
  if (diff >= 1000) return `+${(diff / 1000).toFixed(1)}s`
  return `+${diff}ms`
}

function formatDuration(ms: number): string {
  if (ms >= 1000) return `${(ms / 1000).toFixed(1)}s`
  return `${ms}ms`
}

function toggleEntry(index: number) {
  if (expandedEntries.value.has(index)) {
    expandedEntries.value.delete(index)
  } else {
    expandedEntries.value.add(index)
  }
}

function formatDetail(detail: string): string {
  try {
    return JSON.stringify(JSON.parse(detail), null, 2)
  } catch {
    return detail
  }
}

function handleClose() {
  chatStore.toggleTrace()
}
</script>

<template>
  <div class="trace-panel">
    <div class="trace-header">
      <h2 class="trace-title">Trace Log</h2>
      <button class="trace-close" @click="handleClose">
        <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
          <path d="M4 4L12 12M12 4L4 12" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
        </svg>
      </button>
    </div>

    <div v-if="activeTrace.length === 0" class="trace-empty">
      <div class="trace-empty-icon">
        <svg width="40" height="40" viewBox="0 0 40 40" fill="none">
          <path d="M20 8V32M8 20H32" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" opacity="0.2"/>
          <circle cx="20" cy="20" r="14" stroke="currentColor" stroke-width="1.5" opacity="0.15"/>
        </svg>
      </div>
      <p class="trace-empty-text">Sende eine Nachricht um den Trace zu sehen</p>
    </div>

    <div v-else class="trace-timeline">
      <div class="trace-index-bar">
        <button
          v-for="(_, idx) in chatStore.traceLog"
          :key="idx"
          class="trace-index-btn"
          :class="{ active: idx === chatStore.activeTraceIndex }"
          @click="chatStore.selectTrace(idx)"
        >
          #{{ idx + 1 }}
        </button>
      </div>
      <div
        v-for="(entry, index) in activeTrace"
        :key="index"
        class="trace-entry"
        :style="{ animationDelay: `${index * 0.05}s` }"
        @click="entry.detail ? toggleEntry(index) : undefined"
      >
        <div class="trace-line-container">
          <div class="trace-line" :class="{ 'trace-line-last': index === activeTrace.length - 1 }"></div>
          <div
            class="trace-dot"
            :style="{ backgroundColor: sourceConfig[entry.source].color, boxShadow: `0 0 8px ${sourceConfig[entry.source].color}40` }"
          ></div>
        </div>
        <div class="trace-content">
          <div class="trace-meta">
            <span
              class="trace-badge"
              :style="{ color: sourceConfig[entry.source].color, borderColor: sourceConfig[entry.source].color + '40', backgroundColor: sourceConfig[entry.source].color + '15' }"
            >
              {{ sourceConfig[entry.source].label }}
            </span>
            <span class="trace-time">{{ getRelativeTime(entry, index) }}</span>
            <span v-if="entry.durationMs !== null" class="trace-duration">
              {{ formatDuration(entry.durationMs) }}
            </span>
          </div>
          <p class="trace-action">{{ entry.action }}</p>
          <div
            v-if="entry.detail && expandedEntries.has(index)"
            class="trace-detail"
          >
            <pre>{{ formatDetail(entry.detail) }}</pre>
          </div>
          <button
            v-if="entry.detail"
            class="trace-expand-hint"
          >
            {{ expandedEntries.has(index) ? 'Weniger' : 'Details' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.trace-panel {
  width: 380px;
  min-width: 380px;
  height: 100%;
  display: flex;
  flex-direction: column;
  background: var(--obsidian-light);
  border-left: 1px solid var(--obsidian-border);
  animation: panel-in 0.3s cubic-bezier(0.16, 1, 0.3, 1) both;
}

@keyframes panel-in {
  0% {
    opacity: 0;
    transform: translateX(20px);
  }
  100% {
    opacity: 1;
    transform: translateX(0);
  }
}

.trace-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  border-bottom: 1px solid var(--obsidian-border);
  flex-shrink: 0;
}

.trace-title {
  font-family: var(--font-display);
  font-size: 0.95rem;
  font-weight: 600;
  color: var(--text-primary);
  letter-spacing: -0.02em;
}

.trace-close {
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
  background: transparent;
  color: var(--text-tertiary);
  cursor: pointer;
  border-radius: var(--radius-sm);
  transition: color 0.15s ease, background 0.15s ease;
}

.trace-close:hover {
  color: var(--text-primary);
  background: var(--obsidian-lighter);
}

/* Empty State */
.trace-empty {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 20px;
}

.trace-empty-icon {
  color: var(--text-tertiary);
  opacity: 0.5;
}

.trace-empty-text {
  font-family: var(--font-body);
  font-size: 0.8rem;
  color: var(--text-tertiary);
  text-align: center;
  line-height: 1.5;
}

/* Index Bar */
.trace-index-bar {
  display: flex;
  gap: 4px;
  padding: 12px 20px;
  flex-wrap: wrap;
  border-bottom: 1px solid var(--obsidian-border);
  flex-shrink: 0;
}

.trace-index-btn {
  padding: 3px 10px;
  font-family: var(--font-body);
  font-size: 0.7rem;
  font-weight: 500;
  color: var(--text-tertiary);
  background: transparent;
  border: 1px solid var(--obsidian-border);
  border-radius: 100px;
  cursor: pointer;
  transition: all 0.15s ease;
}

.trace-index-btn:hover {
  color: var(--text-secondary);
  border-color: var(--text-tertiary);
}

.trace-index-btn.active {
  color: var(--amber);
  border-color: var(--amber-dim);
  background: var(--amber-glow);
}

/* Timeline */
.trace-timeline {
  flex: 1;
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: var(--obsidian-border) transparent;
}

.trace-timeline::-webkit-scrollbar {
  width: 5px;
}

.trace-timeline::-webkit-scrollbar-track {
  background: transparent;
}

.trace-timeline::-webkit-scrollbar-thumb {
  background: var(--obsidian-border);
  border-radius: 3px;
}

.trace-entry {
  display: flex;
  gap: 12px;
  padding: 0 20px;
  cursor: pointer;
  animation: entry-in 0.35s cubic-bezier(0.16, 1, 0.3, 1) both;
}

@keyframes entry-in {
  0% {
    opacity: 0;
    transform: translateY(8px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

.trace-entry:hover .trace-content {
  background: var(--obsidian-lighter);
}

.trace-line-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 10px;
  flex-shrink: 0;
  padding-top: 14px;
}

.trace-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  flex-shrink: 0;
  z-index: 1;
}

.trace-line {
  width: 2px;
  flex: 1;
  background: var(--obsidian-border);
  margin-top: 4px;
}

.trace-line.trace-line-last {
  background: transparent;
}

.trace-content {
  flex: 1;
  padding: 10px 12px;
  border-radius: var(--radius-sm);
  transition: background 0.15s ease;
  min-width: 0;
}

.trace-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.trace-badge {
  font-family: var(--font-display);
  font-size: 0.6rem;
  font-weight: 600;
  letter-spacing: 0.05em;
  padding: 2px 7px;
  border-radius: 100px;
  border: 1px solid;
}

.trace-time {
  font-family: var(--font-body);
  font-size: 0.65rem;
  color: var(--text-tertiary);
}

.trace-duration {
  font-family: var(--font-body);
  font-size: 0.6rem;
  color: var(--text-secondary);
  padding: 1px 7px;
  background: var(--obsidian);
  border: 1px solid var(--obsidian-border);
  border-radius: 100px;
  margin-left: auto;
}

.trace-action {
  font-family: var(--font-body);
  font-size: 0.8rem;
  color: var(--text-primary);
  margin: 6px 0 0;
  line-height: 1.4;
}

.trace-expand-hint {
  font-family: var(--font-body);
  font-size: 0.65rem;
  color: var(--amber-dim);
  background: none;
  border: none;
  padding: 0;
  margin-top: 4px;
  cursor: pointer;
  transition: color 0.15s ease;
}

.trace-expand-hint:hover {
  color: var(--amber);
}

.trace-detail {
  margin-top: 8px;
  padding: 10px 12px;
  background: var(--obsidian);
  border: 1px solid var(--obsidian-border);
  border-radius: var(--radius-sm);
  max-height: 200px;
  overflow: auto;
  scrollbar-width: thin;
  scrollbar-color: var(--obsidian-border) transparent;
}

.trace-detail pre {
  font-family: 'SF Mono', 'Fira Code', 'Cascadia Code', monospace;
  font-size: 0.7rem;
  color: var(--text-secondary);
  line-height: 1.5;
  white-space: pre-wrap;
  word-break: break-all;
  margin: 0;
}
</style>
