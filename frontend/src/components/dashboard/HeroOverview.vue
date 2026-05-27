<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
defineProps({
  stats: Object,
  loading: Boolean,
})

const chips = ['生成工坊', '工作流编排', '可视化修改']

function goTo(path) {
  router.push(path)
}
</script>

<template>
  <section class="hero">
    <div class="hero-copy">
      <p class="eyebrow">Midnight Control Room</p>
      <h2>把想法变成可部署的应用</h2>
      <p class="hero-text">
        统一管理生成、修复、部署和工作流状态。你在这里看到的不是一个普通后台，而是一台持续运行的 AI 生成控制台。
      </p>
      <div class="hero-actions">
        <button class="primary-button" type="button" @click="goTo('/apps')">创建应用</button>
        <button class="ghost-button" type="button" @click="goTo('/workflow')">进入工作流</button>
      </div>
      <div class="hero-chips">
        <span v-for="chip in chips" :key="chip">{{ chip }}</span>
      </div>
    </div>
      <div class="hero-side">
        <div class="hero-stat">
          <span>今日生成</span>
          <strong>{{ loading ? '—' : stats?.todayGenerated || '0' }}</strong>
        </div>
        <div class="hero-stat">
          <span>部署成功率</span>
          <strong>{{ loading ? '—' : stats?.deploySuccessRate || '0%' }}</strong>
        </div>
        <div class="hero-stat">
          <span>待处理任务</span>
          <strong>{{ loading ? '—' : stats?.pendingTaskCount || '0' }}</strong>
        </div>
      </div>
  </section>
</template>
