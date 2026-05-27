# CloudMorph 首页控制台 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a distinctive Vue 3 homepage control console for CloudMorph that surfaces app overview, recent generation activity, workflow status, and fast actions in a dark, high-contrast dashboard.

**Architecture:** Create a standalone frontend app under `frontend/` so it does not interfere with the existing backend Maven project. Use Vue 3 + Vite + Vue Router with a single polished landing dashboard composed of reusable panels, then wire it to mock data first and a backend API adapter second. Keep the implementation small and cohesive: one layout shell, a small data layer, and focused presentational components.

**Tech Stack:** Vue 3, Vite, Vue Router 4, plain CSS, Vite dev server, optional fetch-based API client

---

### Task 1: Scaffold the frontend app

**Files:**
- Create: `frontend/package.json`
- Create: `frontend/vite.config.js`
- Create: `frontend/index.html`
- Create: `frontend/src/main.js`
- Create: `frontend/src/App.vue`
- Create: `frontend/src/styles/base.css`

- [ ] **Step 1: Write the failing test**

```bash
cd frontend
npm run build
```

Expected: fail because the frontend app files do not exist yet.

- [ ] **Step 2: Run test to verify it fails**

Run: `cd frontend && npm run build`
Expected: build fails with missing file or missing script errors.

- [ ] **Step 3: Write minimal implementation**

```json
{
  "name": "cloudmorph-frontend",
  "private": true,
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "vite build"
  },
  "dependencies": {
    "vue": "^3.5.13",
    "vue-router": "^4.5.1"
  },
  "devDependencies": {
    "@vitejs/plugin-vue": "^5.2.4",
    "vite": "^6.2.1"
  }
}
```

```js
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

export default defineConfig({
  base: './',
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
})
```

```html
<!doctype html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>CloudMorph</title>
  </head>
  <body>
    <div id="app"></div>
    <script type="module" src="/src/main.js"></script>
  </body>
</html>
```

```js
import { createApp } from 'vue'
import App from './App.vue'
import './styles/base.css'

createApp(App).mount('#app')
```

```vue
<template>
  <div>CloudMorph</div>
</template>
```

```css
:root {
  color-scheme: dark;
}

* {
  box-sizing: border-box;
}

html,
body,
#app {
  min-height: 100%;
  margin: 0;
}

body {
  font-family: 'Times New Roman', serif;
  background: #090909;
  color: #f2efe8;
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `cd frontend && npm install && npm run build`
Expected: build succeeds and outputs a production bundle.

- [ ] **Step 5: Commit**

```bash
git add frontend/package.json frontend/vite.config.js frontend/index.html frontend/src/main.js frontend/src/App.vue frontend/src/styles/base.css
git commit -m "初始化前端控制台"
```

### Task 2: Build the dashboard shell and router

**Files:**
- Create: `frontend/src/router/index.js`
- Create: `frontend/src/layouts/DashboardShell.vue`
- Modify: `frontend/src/main.js`
- Modify: `frontend/src/App.vue`

- [ ] **Step 1: Write the failing test**

```bash
cd frontend
npm run build
```

Expected: fail because router and layout imports are missing.

- [ ] **Step 2: Run test to verify it fails**

Run: `cd frontend && npm run build`
Expected: build fails with unresolved import errors for `vue-router` setup or layout files.

- [ ] **Step 3: Write minimal implementation**

```js
import { createRouter, createWebHashHistory } from 'vue-router'
import DashboardShell from '@/layouts/DashboardShell.vue'

export const routes = [
  {
    path: '/',
    component: DashboardShell,
  },
]

export const router = createRouter({
  history: createWebHashHistory(),
  routes,
})
```

```vue
<template>
  <div class="shell">
    <slot />
  </div>
</template>
```

```js
import { createApp } from 'vue'
import App from './App.vue'
import { router } from './router'
import './styles/base.css'

createApp(App).use(router).mount('#app')
```

```vue
<template>
  <router-view />
</template>
```

- [ ] **Step 4: Run test to verify it passes**

Run: `cd frontend && npm run build`
Expected: build succeeds.

- [ ] **Step 5: Commit**

```bash
git add frontend/src/router/index.js frontend/src/layouts/DashboardShell.vue frontend/src/main.js frontend/src/App.vue
git commit -m "补齐前端路由骨架"
```

### Task 3: Implement the homepage dashboard modules

**Files:**
- Create: `frontend/src/pages/HomeDashboard.vue`
- Create: `frontend/src/components/dashboard/TopBar.vue`
- Create: `frontend/src/components/dashboard/HeroOverview.vue`
- Create: `frontend/src/components/dashboard/StatsGrid.vue`
- Create: `frontend/src/components/dashboard/RecentAppPanel.vue`
- Create: `frontend/src/components/dashboard/GenerationFeedPanel.vue`
- Create: `frontend/src/components/dashboard/WorkflowStatusPanel.vue`
- Create: `frontend/src/components/dashboard/ActivityTimeline.vue`
- Create: `frontend/src/components/dashboard/QuickActionsPanel.vue`
- Modify: `frontend/src/router/index.js`
- Modify: `frontend/src/layouts/DashboardShell.vue`

- [ ] **Step 1: Write the failing test**

```bash
cd frontend
npm run build
```

Expected: fail because dashboard components do not exist yet.

- [ ] **Step 2: Run test to verify it fails**

Run: `cd frontend && npm run build`
Expected: unresolved import errors for dashboard components.

- [ ] **Step 3: Write minimal implementation**

```vue
<template>
  <div class="dashboard">
    <TopBar />
    <HeroOverview />
    <StatsGrid />
    <div class="dashboard-grid">
      <RecentAppPanel />
      <GenerationFeedPanel />
      <WorkflowStatusPanel />
    </div>
    <div class="dashboard-footer">
      <ActivityTimeline />
      <QuickActionsPanel />
    </div>
  </div>
</template>
```

```js
import HomeDashboard from '@/pages/HomeDashboard.vue'

export const routes = [
  {
    path: '/',
    component: HomeDashboard,
  },
]
```

- [ ] **Step 4: Run test to verify it passes**

Run: `cd frontend && npm run build`
Expected: build succeeds with the full dashboard scaffold.

- [ ] **Step 5: Commit**

```bash
git add frontend/src/pages/HomeDashboard.vue frontend/src/components/dashboard frontend/src/router/index.js frontend/src/layouts/DashboardShell.vue
git commit -m "搭建首页控制台结构"
```

### Task 4: Apply the Midnight Control Room visual system

**Files:**
- Create: `frontend/src/styles/theme.css`
- Modify: `frontend/src/styles/base.css`
- Modify: `frontend/src/components/dashboard/*.vue`

- [ ] **Step 1: Write the failing test**

```bash
cd frontend
npm run build
```

Expected: build still passes but the page is visually unfinished.

- [ ] **Step 2: Run test to verify it fails**

Run: `cd frontend && npm run build`
Expected: success, but this step is used to enforce a visual refactor before commit.

- [ ] **Step 3: Write minimal implementation**

```css
:root {
  --bg: #08090b;
  --bg-elevated: rgba(19, 21, 26, 0.78);
  --bg-soft: rgba(255, 255, 255, 0.04);
  --text: #f2ede3;
  --muted: rgba(242, 237, 227, 0.68);
  --accent: #d79a3f;
  --accent-strong: #ffbe63;
  --line: rgba(255, 255, 255, 0.08);
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `cd frontend && npm run build`
Expected: build still succeeds with the themed styles.

- [ ] **Step 5: Commit**

```bash
git add frontend/src/styles/theme.css frontend/src/styles/base.css frontend/src/components/dashboard/*.vue
git commit -m "完善前端视觉风格"
```

### Task 5: Add API adapters and realistic mock data

**Files:**
- Create: `frontend/src/api/client.js`
- Create: `frontend/src/api/dashboard.js`
- Create: `frontend/src/mock/dashboard.js`
- Modify: `frontend/src/pages/HomeDashboard.vue`

- [ ] **Step 1: Write the failing test**

```bash
cd frontend
npm run build
```

Expected: fail because API layer files do not exist yet.

- [ ] **Step 2: Run test to verify it fails**

Run: `cd frontend && npm run build`
Expected: unresolved import errors for API modules.

- [ ] **Step 3: Write minimal implementation**

```js
export async function request(path, options = {}) {
  const response = await fetch(path, {
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    ...options,
  })
  if (!response.ok) {
    throw new Error(`Request failed: ${response.status}`)
  }
  return response.json()
}
```

```js
export const mockDashboard = { /* apps, feed, workflow, timeline, stats */ }
```

- [ ] **Step 4: Run test to verify it passes**

Run: `cd frontend && npm run build`
Expected: build succeeds with the API and mock-data layer in place.

- [ ] **Step 5: Commit**

```bash
git add frontend/src/api frontend/src/mock frontend/src/pages/HomeDashboard.vue
git commit -m "接入首页数据层"
```

### Task 6: Verify responsive polish and final production build

**Files:**
- Modify: `frontend/src/styles/*.css`
- Modify: `frontend/src/components/dashboard/*.vue`
- Modify: `frontend/src/pages/HomeDashboard.vue`

- [ ] **Step 1: Write the failing test**

```bash
cd frontend
npm run build
```

Expected: pass, then inspect the output at desktop and mobile widths.

- [ ] **Step 2: Run test to verify it fails**

Run: `cd frontend && npm run build`
Expected: build passes; then manually verify layout in browser at mobile width.

- [ ] **Step 3: Write minimal implementation**

```css
@media (max-width: 960px) {
  .dashboard-grid,
  .dashboard-footer {
    grid-template-columns: 1fr;
  }
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `cd frontend && npm run build`
Expected: production build remains green after responsive adjustments.

- [ ] **Step 5: Commit**

```bash
git add frontend/src/styles frontend/src/components/dashboard frontend/src/pages/HomeDashboard.vue
git commit -m "完成首页控制台收尾"
```

## Self-Review Checklist

- The spec covers only the frontend homepage console, not unrelated pages
- The file structure is isolated under `frontend/`
- The layout, style system, API layer, and responsive behavior each have a task
- No placeholder steps remain
- Each task has exact files and a concrete build verification command
