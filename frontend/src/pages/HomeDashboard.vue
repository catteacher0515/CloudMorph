<script setup>
import { onMounted, ref, computed } from 'vue'
import TopBar from '@/components/dashboard/TopBar.vue'
import HeroOverview from '@/components/dashboard/HeroOverview.vue'
import StatsGrid from '@/components/dashboard/StatsGrid.vue'
import RecentAppPanel from '@/components/dashboard/RecentAppPanel.vue'
import GenerationFeedPanel from '@/components/dashboard/GenerationFeedPanel.vue'
import WorkflowStatusPanel from '@/components/dashboard/WorkflowStatusPanel.vue'
import ActivityTimeline from '@/components/dashboard/ActivityTimeline.vue'
import QuickActionsPanel from '@/components/dashboard/QuickActionsPanel.vue'
import { fetchDashboardOverview } from '@/api/dashboard'

const overview = ref(null)
const loading = ref(true)
const error = ref('')

const stats = computed(() => overview.value?.stats || null)
const recentApps = computed(() => overview.value?.recentApps || [])
const generationFeed = computed(() => overview.value?.generationFeed || [])
const workflowSteps = computed(() => overview.value?.workflowSteps || [])
const activityTimeline = computed(() => overview.value?.activityTimeline || [])
const quickActions = computed(() => overview.value?.quickActions || [])

async function loadOverview() {
  loading.value = true
  error.value = ''
  try {
    overview.value = await fetchDashboardOverview()
  } catch (err) {
    error.value = err?.message || '加载首页数据失败'
  } finally {
    loading.value = false
  }
}

onMounted(loadOverview)
</script>

<template>
  <main class="dashboard">
    <TopBar />
    <div v-if="error" class="error-banner">{{ error }}</div>
    <HeroOverview :stats="stats" :loading="loading" />
    <StatsGrid :stats="stats" :loading="loading" />
    <section class="dashboard-grid">
      <RecentAppPanel :items="recentApps" :loading="loading" />
      <GenerationFeedPanel :items="generationFeed" :loading="loading" />
      <WorkflowStatusPanel :steps="workflowSteps" :loading="loading" />
    </section>
    <section class="dashboard-footer">
      <ActivityTimeline :items="activityTimeline" :loading="loading" />
      <QuickActionsPanel :items="quickActions" :loading="loading" />
    </section>
  </main>
</template>
