<script setup>
defineProps({
  stats: Object,
  loading: Boolean,
})

const statMap = [
  { key: 'todayGenerated', label: '今日生成', fallback: '0', suffix: '' },
  { key: 'deploySuccessRate', label: '部署成功率', fallback: '0%', suffix: '' },
  { key: 'activeAppCount', label: '活跃应用', fallback: '0', suffix: '' },
  { key: 'pendingTaskCount', label: '待处理任务', fallback: '0', suffix: '' },
]
</script>

<template>
  <section class="stats-grid">
    <article v-for="item in statMap" :key="item.label" class="stat-card">
      <span>{{ item.label }}</span>
      <strong>{{ loading ? '—' : stats?.[item.key] || item.fallback }}</strong>
      <small>{{ loading ? '加载中' : '实时数据' }}</small>
    </article>
  </section>
</template>
