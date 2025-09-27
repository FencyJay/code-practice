<template>
  <Teleport to="body">
    <Transition name="battle-dialog">
      <div v-if="visible" class="battle-dialog-overlay" @click="handleOverlayClick">
        <div class="battle-dialog" @click.stop>
          <div class="battle-dialog-header">
            <h3 class="battle-dialog-title">
              <span v-if="icon" class="battle-dialog-icon">{{ icon }}</span>
              {{ title }}
            </h3>
            <button
              v-if="closable"
              class="battle-dialog-close"
              @click="handleClose"
            >
              ✕
            </button>
          </div>

          <div class="battle-dialog-body">
            <slot></slot>
          </div>

          <div v-if="showFooter" class="battle-dialog-footer">
            <slot name="footer">
              <BaseButton
                v-if="showCancel"
                type="secondary"
                @click="handleCancel"
              >
                {{ cancelText }}
              </BaseButton>
              <BaseButton
                type="primary"
                @click="handleConfirm"
                :loading="confirmLoading"
              >
                {{ confirmText }}
              </BaseButton>
            </slot>
          </div>

          <div class="battle-dialog-decoration">
            {{ decoration }}
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
import { watch, nextTick } from 'vue'
import BaseButton from './BaseButton.vue'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  title: {
    type: String,
    default: '确认'
  },
  icon: {
    type: String,
    default: ''
  },
  decoration: {
    type: String,
    default: '⚔️'
  },
  closable: {
    type: Boolean,
    default: true
  },
  showFooter: {
    type: Boolean,
    default: true
  },
  showCancel: {
    type: Boolean,
    default: true
  },
  cancelText: {
    type: String,
    default: '取消'
  },
  confirmText: {
    type: String,
    default: '确认'
  },
  confirmLoading: {
    type: Boolean,
    default: false
  },
  closeOnClickOverlay: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits(['update:visible', 'confirm', 'cancel', 'close'])

const handleClose = () => {
  emit('update:visible', false)
  emit('close')
}

const handleCancel = () => {
  emit('cancel')
  handleClose()
}

const handleConfirm = () => {
  emit('confirm')
}

const handleOverlayClick = () => {
  if (props.closeOnClickOverlay) {
    handleClose()
  }
}

// 处理 ESC 键关闭
watch(() => props.visible, (newVal) => {
  if (newVal) {
    nextTick(() => {
      const handleEsc = (e) => {
        if (e.key === 'Escape' && props.closable) {
          handleClose()
        }
      }
      document.addEventListener('keydown', handleEsc)

      // 清理事件监听
      return () => {
        document.removeEventListener('keydown', handleEsc)
      }
    })
  }
})
</script>

<style scoped>
/* 引入战场主题样式 */
@import '@/styles/battlefield-theme.css';

/* 对话框遮罩层 */
.battle-dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  backdrop-filter: blur(4px);
}

/* 对话框主体 */
.battle-dialog {
  background: var(--bg-card);
  border: 3px solid var(--border-primary);
  border-radius: 4px;
  min-width: 400px;
  max-width: 90vw;
  max-height: 90vh;
  position: relative;
  overflow: hidden;
  box-shadow:
    0 20px 40px var(--shadow-heavy),
    inset 0 0 50px rgba(218, 165, 32, 0.1);
}

.battle-dialog::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, var(--battle-gold), var(--sword-silver), var(--battle-gold));
}

/* 对话框头部 */
.battle-dialog-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24px 24px 16px;
  border-bottom: 1px solid var(--border-secondary);
  background: linear-gradient(135deg, rgba(139, 69, 19, 0.05) 0%, rgba(218, 165, 32, 0.05) 100%);
}

.battle-dialog-title {
  font-family: 'Noto Serif SC', serif;
  font-size: 1.25rem;
  font-weight: bold;
  color: var(--text-primary);
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;
  text-shadow: 1px 1px 2px rgba(139, 69, 19, 0.1);
}

.battle-dialog-icon {
  font-size: 1.5rem;
}

.battle-dialog-close {
  background: transparent;
  border: 1px solid var(--border-secondary);
  color: var(--text-secondary);
  width: 32px;
  height: 32px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 1.2rem;
  font-weight: bold;
}

.battle-dialog-close:hover {
  background: var(--danger-red);
  color: white;
  border-color: var(--danger-red);
  transform: scale(1.1);
}

/* 对话框内容 */
.battle-dialog-body {
  padding: 24px;
  max-height: 60vh;
  overflow-y: auto;
  color: var(--text-secondary);
  line-height: 1.6;
}

.battle-dialog-body::-webkit-scrollbar {
  width: 8px;
}

.battle-dialog-body::-webkit-scrollbar-track {
  background: var(--bg-secondary);
  border: 1px solid var(--border-secondary);
}

.battle-dialog-body::-webkit-scrollbar-thumb {
  background: var(--primary-brown);
  border: 1px solid var(--border-secondary);
}

.battle-dialog-body::-webkit-scrollbar-thumb:hover {
  background: var(--secondary-brown);
}

/* 对话框底部 */
.battle-dialog-footer {
  padding: 16px 24px 24px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  border-top: 1px solid var(--border-secondary);
  background: linear-gradient(135deg, rgba(139, 69, 19, 0.02) 0%, rgba(218, 165, 32, 0.02) 100%);
}

/* 装饰元素 */
.battle-dialog-decoration {
  position: absolute;
  top: 12px;
  right: 12px;
  font-size: 1.5rem;
  opacity: 0.2;
  transform: rotate(15deg);
  pointer-events: none;
  z-index: 1;
}

/* 过渡动画 */
.battle-dialog-enter-active,
.battle-dialog-leave-active {
  transition: all 0.3s ease;
}

.battle-dialog-enter-active .battle-dialog,
.battle-dialog-leave-active .battle-dialog {
  transition: all 0.3s ease;
}

.battle-dialog-enter-from .battle-dialog-overlay {
  opacity: 0;
}

.battle-dialog-leave-to .battle-dialog-overlay {
  opacity: 0;
}

.battle-dialog-enter-from .battle-dialog {
  opacity: 0;
  transform: scale(0.8) translateY(-20px);
}

.battle-dialog-leave-to .battle-dialog {
  opacity: 0;
  transform: scale(0.8) translateY(-20px);
}

.battle-dialog-enter-active .battle-dialog {
  opacity: 1;
  transform: scale(1) translateY(0);
}

.battle-dialog-leave-active .battle-dialog {
  opacity: 1;
  transform: scale(1) translateY(0);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .battle-dialog {
    min-width: 90vw;
    margin: 20px;
    max-height: calc(90vh - 40px);
  }

  .battle-dialog-header {
    padding: 20px 20px 12px;
  }

  .battle-dialog-title {
    font-size: 1.1rem;
  }

  .battle-dialog-body {
    padding: 20px;
    max-height: 50vh;
  }

  .battle-dialog-footer {
    padding: 12px 20px 20px;
    flex-direction: column;
    gap: 8px;
  }

  .battle-dialog-footer .battle-button {
    width: 100%;
  }

  .battle-dialog-decoration {
    font-size: 1.2rem;
    top: 8px;
    right: 8px;
  }
}

/* 战场特殊效果 */
.battle-dialog::after {
  content: '';
  position: absolute;
  top: 4px;
  left: 4px;
  right: 4px;
  bottom: 4px;
  border: 1px solid var(--border-secondary);
  pointer-events: none;
  opacity: 0.3;
}

/* 动画效果 */
@keyframes dialogPulse {
  0%, 100% {
    box-shadow:
      0 20px 40px var(--shadow-heavy),
      inset 0 0 50px rgba(218, 165, 32, 0.1);
  }
  50% {
    box-shadow:
      0 25px 50px rgba(218, 165, 32, 0.3),
      inset 0 0 60px rgba(218, 165, 32, 0.15);
  }
}

.battle-dialog:hover {
  animation: dialogPulse 3s ease-in-out infinite;
}
</style>