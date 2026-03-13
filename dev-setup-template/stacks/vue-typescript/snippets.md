# Vue.js 3 + TypeScript — Patterns & Anti-Patterns

## Komponente mit Props + Emits (DO)
```vue
<script setup lang="ts">
import { computed } from 'vue'
import { useOrderStore } from '@/stores/order'

interface Props {
  orderId: number
  showDetails?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  showDetails: false,
})

const emit = defineEmits<{
  select: [id: number]
  delete: [id: number]
}>()

const store = useOrderStore()
const order = computed(() => store.getById(props.orderId))

function handleSelect() {
  emit('select', props.orderId)
}
</script>

<template>
  <div class="order-card" @click="handleSelect">
    <h3>{{ order?.title }}</h3>
    <p v-if="showDetails">{{ order?.description }}</p>
  </div>
</template>

<style scoped>
.order-card {
  padding: 1rem;
  border: 1px solid var(--border-color);
  cursor: pointer;
}
</style>
```

## Pinia Store (DO)
```typescript
// stores/order.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { api } from '@/lib/api'
import type { Order } from '@/types'

export const useOrderStore = defineStore('order', () => {
  const orders = ref<Order[]>([])
  const isLoading = ref(false)

  const getById = computed(() => (id: number) =>
    orders.value.find(o => o.id === id)
  )

  async function fetchAll() {
    isLoading.value = true
    try {
      const { data } = await api.get<Order[]>('/api/v1/orders')
      orders.value = data
    } finally {
      isLoading.value = false
    }
  }

  return { orders, isLoading, getById, fetchAll }
})
```

## Composable (DO)
```typescript
// composables/useApi.ts
import { ref } from 'vue'

export function useApi<T>(fetcher: () => Promise<T>) {
  const data = ref<T | null>(null)
  const error = ref<Error | null>(null)
  const isLoading = ref(false)

  async function execute() {
    isLoading.value = true
    error.value = null
    try {
      data.value = await fetcher()
    } catch (e) {
      error.value = e instanceof Error ? e : new Error(String(e))
    } finally {
      isLoading.value = false
    }
  }

  return { data, error, isLoading, execute }
}
```

## Komponenten-Test (DO)
```typescript
// OrderCard.test.ts
import { describe, it, expect, vi } from "vitest";
import { mount } from "@vue/test-utils";
import { createTestingPinia } from "@pinia/testing";
import OrderCard from "./OrderCard.vue";

describe("OrderCard", () => {
  const defaultProps = {
    orderId: 1,
    showDetails: false,
  };

  it("should render order title", () => {
    const wrapper = mount(OrderCard, {
      props: defaultProps,
      global: {
        plugins: [createTestingPinia({
          initialState: {
            order: { orders: [{ id: 1, title: "Test Order" }] },
          },
        })],
      },
    });

    expect(wrapper.text()).toContain("Test Order");
  });

  it("should emit select event on click", async () => {
    const wrapper = mount(OrderCard, {
      props: defaultProps,
      global: { plugins: [createTestingPinia()] },
    });

    await wrapper.trigger("click");
    expect(wrapper.emitted("select")).toHaveLength(1);
    expect(wrapper.emitted("select")![0]).toEqual([1]);
  });

  it("should show details when showDetails is true", () => {
    const wrapper = mount(OrderCard, {
      props: { ...defaultProps, showDetails: true },
      global: { plugins: [createTestingPinia()] },
    });

    expect(wrapper.find("p").exists()).toBe(true);
  });
});
```

## Pinia Store Test (DO)
```typescript
// stores/__tests__/order.test.ts
import { describe, it, expect, vi, beforeEach } from "vitest";
import { setActivePinia, createPinia } from "pinia";
import { useOrderStore } from "../order";
import { api } from "@/lib/api";

vi.mock("@/lib/api");

describe("useOrderStore", () => {
  beforeEach(() => {
    setActivePinia(createPinia());
    vi.clearAllMocks();
  });

  it("should fetch orders and update state", async () => {
    const mockOrders = [{ id: 1, title: "Order 1" }];
    vi.mocked(api.get).mockResolvedValue({ data: mockOrders });

    const store = useOrderStore();
    await store.fetchAll();

    expect(store.orders).toEqual(mockOrders);
    expect(store.isLoading).toBe(false);
  });

  it("should set isLoading during fetch", async () => {
    vi.mocked(api.get).mockImplementation(() => new Promise(() => {}));

    const store = useOrderStore();
    store.fetchAll();

    expect(store.isLoading).toBe(true);
  });
});
```

## Composable Test (DO)
```typescript
// composables/__tests__/useApi.test.ts
import { describe, it, expect } from "vitest";
import { useApi } from "../useApi";

describe("useApi", () => {
  it("should set data on success", async () => {
    const fetcher = async () => ({ id: 1, name: "Test" });
    const { data, error, isLoading, execute } = useApi(fetcher);

    await execute();

    expect(data.value).toEqual({ id: 1, name: "Test" });
    expect(error.value).toBeNull();
    expect(isLoading.value).toBe(false);
  });

  it("should set error on failure", async () => {
    const fetcher = async () => { throw new Error("Network error"); };
    const { data, error, execute } = useApi(fetcher);

    await execute();

    expect(data.value).toBeNull();
    expect(error.value?.message).toBe("Network error");
  });
});
```

## Anti-Pattern: API-Call in Komponente (DON'T)
```vue
<script setup lang="ts">
import axios from 'axios'
import { ref, onMounted } from 'vue'

// FALSCH — direkter API-Call in Komponente
const orders = ref([])
onMounted(async () => {
  const res = await axios.get('/api/orders')
  orders.value = res.data
})
</script>
```
