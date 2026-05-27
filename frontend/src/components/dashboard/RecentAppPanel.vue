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
  <PanelCard eyebrow="Workspace" title="最近应用" subtitle="直接进入应用详情、生成和部署。">
    <div class="list-stack">
      <article v-if="loading" class="list-row">
        <div>
          <h3>正在加载应用...</h3>
          <p>请稍候</p>
        </div>
      </article>
      <article v-for="app in items" :key="app.id || app.name" class="list-row">
        <div>
          <h3>{{ app.appName }}</h3>
          <p>{{ app.codeGenType }} · {{ app.updateTime || app.updatedAt || '刚刚' }}</p>
        </div>
        <span class="status-pill">{{ app.deployKey ? '已部署' : '未部署' }}</span>
      </article>
    </div>
  </PanelCard>
</template>
