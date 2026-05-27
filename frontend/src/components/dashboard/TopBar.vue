<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
const links = ['应用', '工作流', '部署', '历史']

const routeMap = {
  应用: '/apps',
  工作流: '/workflow',
  部署: '/deploy',
  历史: '/history',
}

function goTo(path) {
  router.push(path)
}
</script>

<template>
  <header class="topbar">
    <div class="brand">
      <div class="brand-mark">CM</div>
      <div>
        <p class="eyebrow">AI Application Forge</p>
        <h1>CloudMorph</h1>
      </div>
    </div>
    <label class="search">
      <span>⌕</span>
      <input placeholder="搜索应用、工作流、部署记录" />
    </label>
    <nav class="topnav">
      <button
        v-for="link in links"
        :key="link"
        class="nav-link"
        type="button"
        @click="goTo(routeMap[link])"
      >
        {{ link }}
      </button>
    </nav>
    <div class="topbar-actions">
      <button class="ghost-button" type="button" @click="goTo('/history')">通知</button>
      <button class="primary-button" type="button" @click="goTo('/apps')">新建应用</button>
      <div class="avatar">PY</div>
    </div>
  </header>
</template>
