<template>
  <section class="panel-card">
    <header v-if="title || subtitle" class="panel-head">
      <div>
        <p v-if="eyebrow" class="eyebrow">{{ eyebrow }}</p>
        <h2 v-if="title">{{ title }}</h2>
        <p v-if="subtitle" class="subtitle">{{ subtitle }}</p>
      </div>
      <slot name="action" />
    </header>
    <div class="panel-body">
      <slot />
    </div>
  </section>
</template>

<script setup>
defineProps({
  eyebrow: String,
  title: String,
  subtitle: String,
})
</script>
