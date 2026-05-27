<script setup>
import PanelCard from '@/components/dashboard/shared/PanelCard.vue'
defineProps({
  steps: {
    type: Array,
    default: () => [],
  },
  loading: Boolean,
})
</script>

<template>
  <PanelCard eyebrow="Workflow" title="工作流状态" subtitle="素材、提示词、路由、生成、构建、修复。">
    <div class="workflow-list">
      <article v-if="loading" class="workflow-step">
        <div class="workflow-meta">
          <span>工作流加载中</span>
          <strong>—</strong>
        </div>
        <div class="progress"><i style="width: 36%" /></div>
      </article>
      <article v-for="step in steps" :key="step.label" class="workflow-step">
        <div class="workflow-meta">
          <span>{{ step.label }}</span>
          <strong>{{ step.value }}%</strong>
        </div>
        <div class="progress">
          <i :style="{ width: `${step.value}%` }" />
        </div>
      </article>
    </div>
  </PanelCard>
</template>
