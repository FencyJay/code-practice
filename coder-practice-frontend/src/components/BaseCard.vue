<template>
  <div :class="['battle-card', `battle-card-${variant}`, { 'battle-card-hoverable': hoverable }]">
    <div v-if="$slots.header || title" class="battle-card-header">
      <slot name="header">
        <h3 class="battle-card-title">{{ title }}</h3>
        <div v-if="subtitle" class="battle-card-subtitle">{{ subtitle }}</div>
      </slot>
    </div>

    <div v-if="$slots.default" class="battle-card-body">
      <slot></slot>
    </div>

    <div v-if="$slots.footer" class="battle-card-footer">
      <slot name="footer"></slot>
    </div>

    <div v-if="decoration" class="battle-card-decoration">
      {{ decoration }}
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  variant: {
    type: String,
    default: 'default',
    validator: (value) => ['default', 'battle', 'vintage', 'minimal'].includes(value)
  },
  title: {
    type: String,
    default: ''
  },
  subtitle: {
    type: String,
    default: ''
  },
  hoverable: {
    type: Boolean,
    default: true
  },
  decoration: {
    type: String,
    default: ''
  }
})
</script>

<style scoped>
/* 引入战场主题样式 */
@import '@/styles/battlefield-theme.css';

.battle-card {
  background: var(--bg-card);
  border: 2px solid var(--border-primary);
  border-radius: 4px;
  padding: 24px;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
  box-shadow:
    0 8px 16px var(--shadow-light),
    inset 0 0 30px rgba(218, 165, 32, 0.05);
}

.battle-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, var(--battle-gold), var(--sword-silver), var(--battle-gold));
  opacity: 0.6;
}

/* 默认卡片 */
.battle-card-default {
  background: var(--bg-card);
  border-color: var(--border-primary);
}

.battle-card-default:hover {
  transform: translateY(-4px);
  box-shadow:
    0 12px 24px var(--shadow-heavy),
    inset 0 0 40px rgba(218, 165, 32, 0.1);
  border-color: var(--battle-gold);
}

/* 战斗卡片 */
.battle-card-battle {
  background: linear-gradient(135deg, rgba(62, 39, 35, 0.05) 0%, rgba(139, 69, 19, 0.1) 100%);
  border-color: var(--primary-brown);
}

.battle-card-battle::before {
  background: linear-gradient(90deg, var(--blood-red), var(--battle-gold), var(--blood-red));
  height: 4px;
}

.battle-card-battle:hover {
  transform: translateY(-4px) scale(1.02);
  box-shadow:
    0 16px 32px rgba(139, 69, 19, 0.3),
    inset 0 0 50px rgba(218, 165, 32, 0.15);
  border-color: var(--blood-red);
}

/* 复古卡片 */
.battle-card-vintage {
  background: linear-gradient(135deg, var(--parchment) 0%, var(--warm-beige) 100%);
  border-color: var(--secondary-brown);
  position: relative;
}

.battle-card-vintage::before {
  background: linear-gradient(90deg, var(--dark-brown), var(--light-brown), var(--dark-brown));
  height: 2px;
}

.battle-card-vintage::after {
  content: '';
  position: absolute;
  top: 4px;
  left: 4px;
  right: 4px;
  bottom: 4px;
  border: 1px solid var(--border-secondary);
  pointer-events: none;
  opacity: 0.5;
}

.battle-card-vintage:hover {
  transform: translateY(-2px);
  box-shadow:
    0 8px 16px rgba(160, 82, 45, 0.3),
    inset 0 0 20px rgba(139, 69, 19, 0.1);
}

/* 极简卡片 */
.battle-card-minimal {
  background: transparent;
  border: 1px solid var(--border-tertiary);
  box-shadow: none;
}

.battle-card-minimal::before {
  display: none;
}

.battle-card-minimal:hover {
  border-color: var(--battle-gold);
  box-shadow: 0 4px 8px var(--shadow-light);
  transform: translateY(-2px);
}

/* 可悬停效果 */
.battle-card-hoverable {
  cursor: pointer;
}

.battle-card-hoverable:hover {
  transform: translateY(-4px);
}

/* 卡片头部 */
.battle-card-header {
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--border-secondary);
  position: relative;
}

.battle-card-header::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  width: 60px;
  height: 2px;
  background: var(--battle-gold);
}

.battle-card-title {
  font-family: 'Noto Serif SC', serif;
  font-size: 1.25rem;
  font-weight: bold;
  color: var(--text-primary);
  margin: 0 0 8px 0;
  text-shadow: 1px 1px 2px rgba(139, 69, 19, 0.1);
}

.battle-card-subtitle {
  color: var(--text-secondary);
  font-size: 0.875rem;
  font-style: italic;
  margin: 0;
}

/* 卡片内容 */
.battle-card-body {
  color: var(--text-secondary);
  line-height: 1.6;
}

.battle-card-body p {
  margin-bottom: 16px;
}

.battle-card-body p:last-child {
  margin-bottom: 0;
}

/* 卡片底部 */
.battle-card-footer {
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid var(--border-secondary);
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}

/* 装饰元素 */
.battle-card-decoration {
  position: absolute;
  top: 12px;
  right: 12px;
  font-size: 1.5rem;
  opacity: 0.2;
  transform: rotate(15deg);
  pointer-events: none;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .battle-card {
    padding: 20px;
    margin: 0;
  }

  .battle-card-title {
    font-size: 1.1rem;
  }

  .battle-card-subtitle {
    font-size: 0.8rem;
  }

  .battle-card-header {
    margin-bottom: 16px;
    padding-bottom: 12px;
  }

  .battle-card-body {
    font-size: 0.9rem;
  }

  .battle-card-footer {
    margin-top: 16px;
    padding-top: 12px;
    flex-direction: column;
    align-items: stretch;
    gap: 8px;
  }

  .battle-card-decoration {
    font-size: 1.2rem;
    top: 8px;
    right: 8px;
  }
}

/* 动画效果 */
@keyframes cardGlow {
  0%, 100% {
    box-shadow:
      0 8px 16px var(--shadow-light),
      inset 0 0 30px rgba(218, 165, 32, 0.05);
  }
  50% {
    box-shadow:
      0 12px 24px rgba(218, 165, 32, 0.3),
      inset 0 0 40px rgba(218, 165, 32, 0.1);
  }
}

.battle-card-hoverable:hover {
  animation: cardGlow 2s ease-in-out infinite;
}

/* 战场特殊效果 */
.battle-card-battle .battle-card-decoration {
  opacity: 0.3;
  color: var(--blood-red);
}

.battle-card-vintage .battle-card-decoration {
  opacity: 0.4;
  color: var(--dark-brown);
}
</style>