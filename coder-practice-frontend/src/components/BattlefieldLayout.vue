<template>
  <div class="battlefield-layout">
    <!-- 全局背景 -->
    <div class="global-background"></div>

    <!-- 内容区域 -->
    <div class="layout-content">
      <slot></slot>
    </div>
  </div>
</template>

<script setup>
// 战场布局组件，为所有页面提供统一的战场风格背景
</script>

<style scoped>
.battlefield-layout {
  min-height: 100vh;
  position: relative;
  overflow: hidden;
  background: var(--bg-primary);
}

.global-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background:
    radial-gradient(circle at 20% 30%, rgba(139, 69, 19, 0.08) 0%, transparent 50%),
    radial-gradient(circle at 80% 70%, rgba(218, 165, 32, 0.05) 0%, transparent 50%),
    linear-gradient(135deg, var(--bg-primary) 0%, var(--bg-secondary) 100%);
  z-index: -2;
}

.global-background::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image:
    repeating-linear-gradient(
      45deg,
      transparent,
      transparent 35px,
      rgba(139, 69, 19, 0.02) 35px,
      rgba(139, 69, 19, 0.02) 70px
    );
  pointer-events: none;
}

.layout-content {
  position: relative;
  z-index: 1;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 动态背景效果 */
@keyframes backgroundShift {
  0%, 100% {
    background-position: 0% 0%;
  }
  50% {
    background-position: 100% 100%;
  }
}

.global-background {
  animation: backgroundShift 30s ease-in-out infinite;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .global-background::before {
    background-size: 50px 50px;
  }
}
</style>