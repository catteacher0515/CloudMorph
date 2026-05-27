<script setup>
import PanelCard from '@/components/dashboard/shared/PanelCard.vue'
import { useRouter } from 'vue-router'

const router = useRouter()
defineProps({
  items: {
    type: Array,
    default: () => [],
  },
  loading: Boolean,
})

const actionRouteMap = {
  create: '/apps',
  workflow: '/workflow',
  history: '/history',
  deploy: '/deploy',
}

function goTo(action) {
  router.push(actionRouteMap[action] || '/')
}
</script>

<template>
  <PanelCard eyebrow="Shortcuts" title="快捷入口" subtitle="把高频动作放在离你最近的地方。">
    <div class="action-grid">
      <button v-if="loading" class="action-card" type="button">快捷入口加载中</button>
      <button
        v-for="action in items"
        :key="action.title"
        class="action-card"
        type="button"
        @click="goTo(action.action)"
      >
        {{ action.title }}
      </button>
    </div>
  </PanelCard>
</template>
