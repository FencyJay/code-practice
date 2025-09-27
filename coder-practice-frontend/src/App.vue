<template>
  <div id="app">
    <BattlefieldLayout>
      <router-view />
      <BaseFooter />
    </BattlefieldLayout>
  </div>
</template>

<script setup>
import BattlefieldLayout from '@/components/BattlefieldLayout.vue'
import BaseFooter from '@/components/BaseFooter.vue'
</script>

<style scoped>
#app {
  min-height: 100vh;
  width: 100%;
  display: flex;
  flex-direction: column;
  overflow-x: hidden;
}

/* 全局字体设置 */
#app {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Noto Sans SC', sans-serif;
  text-rendering: optimizeLegibility;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: var(--text-primary);
  line-height: 1.6;
}

#app h1, #app h2, #app h3, #app h4, #app h5, #app h6 {
  font-family: 'Noto Serif SC', serif;
  font-weight: 700;
  color: var(--text-primary);
  line-height: 1.3;
  margin-bottom: 1rem;
}

#app p, #app span, #app div {
  color: var(--text-secondary);
}

#app .battlefield-typography {
  color: var(--text-primary);
}

#app .vintage-title {
  font-family: 'Noto Serif SC', serif;
  color: var(--text-primary);
  text-shadow: 2px 2px 4px rgba(139, 69, 19, 0.1);
  position: relative;
  display: inline-block;
  font-weight: 700;
  letter-spacing: 1px;
  line-height: 1.3;
}
</style>
