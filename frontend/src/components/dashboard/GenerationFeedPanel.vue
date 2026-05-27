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
  <PanelCard eyebrow="Activity" title="最近生成" subtitle="生成链路、可视化修改和构建修复都在这里回放。">
    <div class="feed">
      <article v-if="loading" class="feed-item">
        <strong>正在读取生成历史</strong>
        <p>请稍候</p>
      </article>
      <article v-for="item in items" :key="item.title || item.id" class="feed-item">
        <strong>{{ item.title }}</strong>
        <p>{{ item.meta }}</p>
      </article>
    </div>
  </PanelCard>
</template>
