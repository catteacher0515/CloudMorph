<script setup>
import PanelCard from '@/components/dashboard/shared/PanelCard.vue'
defineProps({
  items: {
    type: Array,
    default: () => [],
  },
  loading: Boolean,
})
</script>

<template>
  <PanelCard eyebrow="Log" title="最近活动" subtitle="按时间快速追踪你最近的操作。">
    <ol class="timeline">
      <li v-if="loading">
        <span>--:--</span>
        <p>活动记录加载中</p>
      </li>
      <li v-for="item in items" :key="item.time">
        <span>{{ item.time }}</span>
        <p>{{ item.text }}</p>
      </li>
    </ol>
  </PanelCard>
</template>
