<template>
  <button
    :class="[
      'battle-button',
      `battle-button-${type}`,
      `battle-button-${size}`,
      { 'battle-button-disabled': disabled },
      { 'battle-button-loading': loading }
    ]"
    :disabled="disabled || loading"
    @click="handleClick"
  >
    <span v-if="loading" class="battle-button-spinner">
      <span class="spinner-icon"></span>
    </span>
    <span v-else-if="icon" class="battle-button-icon">
      {{ icon }}
    </span>
    <span class="battle-button-text">
      <slot></slot>
    </span>
  </button>
</template>

<script setup>
const props = defineProps({
  type: {
    type: String,
    default: 'primary',
    validator: (value) => ['primary', 'secondary', 'success', 'danger', 'warning'].includes(value)
  },
  size: {
    type: String,
    default: 'medium',
    validator: (value) => ['small', 'medium', 'large'].includes(value)
  },
  disabled: {
    type: Boolean,
    default: false
  },
  loading: {
    type: Boolean,
    default: false
  },
  icon: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['click'])

const handleClick = (event) => {
  if (!props.disabled && !props.loading) {
    emit('click', event)
  }
}
</script>

<style scoped>
/* 引入战场主题样式 */
@import '@/styles/battlefield-theme.css';

.battle-button {
  position: relative;
  border: 2px solid;
  border-radius: 0;
  font-weight: bold;
  text-transform: uppercase;
  letter-spacing: 1px;
  cursor: pointer;
  transition: all 0.3s ease;
  overflow: hidden;
  font-family: 'Noto Serif SC', serif;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  box-shadow:
    0 4px 8px var(--shadow-light),
    inset 0 1px 0 rgba(255, 255, 255, 0.2);
}

.battle-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transition: left 0.6s ease;
}

.battle-button:hover::before {
  left: 100%;
}

/* 主要按钮 */
.battle-button-primary {
  background: linear-gradient(135deg, var(--primary-brown) 0%, var(--secondary-brown) 100%);
  color: white;
  border-color: var(--border-primary);
}

.battle-button-primary:hover {
  background: linear-gradient(135deg, var(--secondary-brown) 0%, var(--light-brown) 100%);
  transform: translateY(-2px);
  box-shadow:
    0 6px 12px var(--shadow-heavy),
    inset 0 1px 0 rgba(255, 255, 255, 0.3);
  border-color: var(--battle-gold);
}

/* 次要按钮 */
.battle-button-secondary {
  background: transparent;
  color: var(--battle-gold);
  border-color: var(--battle-gold);
}

.battle-button-secondary:hover {
  background: rgba(218, 165, 32, 0.1);
  color: var(--light-brown);
  border-color: var(--light-brown);
  transform: translateY(-2px);
  box-shadow:
    0 6px 12px var(--shadow-light),
    inset 0 1px 0 rgba(218, 165, 32, 0.2);
}

/* 成功按钮 */
.battle-button-success {
  background: linear-gradient(135deg, var(--success-green) 0%, #2E8B57 100%);
  color: white;
  border-color: var(--success-green);
}

.battle-button-success:hover {
  background: linear-gradient(135deg, #2E8B57 0%, #3CB371 100%);
  transform: translateY(-2px);
  box-shadow:
    0 6px 12px rgba(34, 139, 34, 0.4),
    inset 0 1px 0 rgba(255, 255, 255, 0.3);
}

/* 危险按钮 */
.battle-button-danger {
  background: linear-gradient(135deg, var(--danger-red) 0%, #B22222 100%);
  color: white;
  border-color: var(--danger-red);
}

.battle-button-danger:hover {
  background: linear-gradient(135deg, #B22222 0%, #CD5C5C 100%);
  transform: translateY(-2px);
  box-shadow:
    0 6px 12px rgba(220, 20, 60, 0.4),
    inset 0 1px 0 rgba(255, 255, 255, 0.3);
}

/* 警告按钮 */
.battle-button-warning {
  background: linear-gradient(135deg, var(--warning-orange) 0%, #FF8C00 100%);
  color: white;
  border-color: var(--warning-orange);
}

.battle-button-warning:hover {
  background: linear-gradient(135deg, #FF8C00 0%, #FFA500 100%);
  transform: translateY(-2px);
  box-shadow:
    0 6px 12px rgba(255, 140, 0, 0.4),
    inset 0 1px 0 rgba(255, 255, 255, 0.3);
}

/* 尺寸变体 */
.battle-button-small {
  padding: 8px 16px;
  font-size: 0.875rem;
  min-height: 36px;
}

.battle-button-medium {
  padding: 12px 24px;
  font-size: 1rem;
  min-height: 44px;
}

.battle-button-large {
  padding: 16px 32px;
  font-size: 1.125rem;
  min-height: 52px;
}

/* 禁用状态 */
.battle-button-disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.battle-button-disabled:hover {
  transform: none;
  box-shadow:
    0 4px 8px var(--shadow-light),
    inset 0 1px 0 rgba(255, 255, 255, 0.2);
}

.battle-button-disabled::before {
  display: none;
}

/* 加载状态 */
.battle-button-loading {
  cursor: not-allowed;
}

.battle-button-spinner {
  display: inline-block;
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: battle-spin 1s linear infinite;
}

.battle-button-loading .battle-button-text {
  opacity: 0.8;
}

/* 图标样式 */
.battle-button-icon {
  font-size: 1.2em;
}

/* 动画 */
@keyframes battle-spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .battle-button-large {
    padding: 14px 28px;
    font-size: 1rem;
    min-height: 48px;
  }

  .battle-button-medium {
    padding: 10px 20px;
    font-size: 0.875rem;
    min-height: 40px;
  }

  .battle-button-small {
    padding: 6px 12px;
    font-size: 0.75rem;
    min-height: 32px;
  }
}

/* 战场特殊效果 */
.battle-button::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, var(--battle-gold), var(--sword-silver), var(--battle-gold));
  opacity: 0.6;
}

.battle-button:hover::after {
  opacity: 1;
  height: 3px;
}
</style>