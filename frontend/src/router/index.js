import { createRouter, createWebHashHistory } from 'vue-router'
import DashboardShell from '@/layouts/DashboardShell.vue'
import HomeDashboard from '@/pages/HomeDashboard.vue'
import AppPlaceholderPage from '@/pages/AppPlaceholderPage.vue'
import WorkflowPlaceholderPage from '@/pages/WorkflowPlaceholderPage.vue'
import DeployPlaceholderPage from '@/pages/DeployPlaceholderPage.vue'
import HistoryPlaceholderPage from '@/pages/HistoryPlaceholderPage.vue'

export const routes = [
  {
    path: '/',
    component: DashboardShell,
    children: [
      {
        path: '',
        name: 'home',
        component: HomeDashboard,
      },
      {
        path: 'apps',
        name: 'apps',
        component: AppPlaceholderPage,
      },
      {
        path: 'workflow',
        name: 'workflow',
        component: WorkflowPlaceholderPage,
      },
      {
        path: 'deploy',
        name: 'deploy',
        component: DeployPlaceholderPage,
      },
      {
        path: 'history',
        name: 'history',
        component: HistoryPlaceholderPage,
      },
    ],
  },
]

export const router = createRouter({
  history: createWebHashHistory(),
  routes,
})
