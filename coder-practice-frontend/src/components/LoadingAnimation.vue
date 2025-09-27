<template>
  <div class="loading-container">
    <div class="loading-box">
      <!-- 左右移动的图标 -->
      <div class="moving-icon-container">
        <img :src="iconPath" alt="加载中" class="moving-icon" />
      </div>

      <!-- 加载进度条 -->
      <div class="progress-container">
        <div class="progress-bar">
          <div class="progress-fill" :style="{ width: progress + '%' }"></div>
        </div>
        <div class="progress-text">{{ Math.floor(progress) }}%</div>
      </div>

      <!-- 加载文字 -->
      <div class="loading-text">{{ text }}</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  text: {
    type: String,
    default: 'AI正在生成中...'
  },
  iconPath: {
    type: String,
    default: '/src/assets/img.png'
  }
})

const progress = ref(0)
let progressInterval = null

// 模拟进度条动画 - 随机突进式加载，模拟不可预测的AI思考过程
const startProgress = () => {
  progress.value = 0
  const targetProgress = 99
  const maxDuration = 5000 // 5秒最大时长

  let currentProgress = 0
  let stageStartTime = Date.now()
  let isPaused = false
  let currentStage = 0

  // 生成随机加载阶段
  const generateRandomStage = () => {
    const progressJump = Math.random() * 25 + 10 // 随机跳跃10-35%
    const jumpDuration = Math.random() * 600 + 300 // 随机突进时间300-900ms
    const pauseDuration = Math.random() * 1500 + 500 // 随机停顿时间500-2000ms

    return {
      start: currentProgress,
      end: Math.min(currentProgress + progressJump, targetProgress),
      duration: jumpDuration,
      pause: pauseDuration
    }
  }

  let currentStageData = generateRandomStage()

  const executeRandomStage = () => {
    const elapsed = Date.now() - stageStartTime
    const totalTimeElapsed = Date.now() - startTime

    // 确保总时长不超过5秒
    if (totalTimeElapsed >= maxDuration || currentProgress >= targetProgress) {
      progress.value = targetProgress
      clearInterval(progressInterval)
      progressInterval = null
      return
    }

    if (!isPaused) {
      // 随机突进阶段
      if (elapsed <= currentStageData.duration) {
        // 使用随机缓动函数，每次都不同
        const progressRatio = elapsed / currentStageData.duration
        let easeProgress

        // 随机选择缓动类型
        const easingType = Math.floor(Math.random() * 4)
        switch (easingType) {
          case 0: // easeOutQuad
            easeProgress = 1 - (1 - progressRatio) * (1 - progressRatio)
            break
          case 1: // easeOutCubic
            easeProgress = 1 - Math.pow(1 - progressRatio, 3)
            break
          case 2: // easeOutQuart
            easeProgress = 1 - Math.pow(1 - progressRatio, 4)
            break
          case 3: // easeOutExpo
            easeProgress = progressRatio === 1 ? 1 : 1 - Math.pow(2, -10 * progressRatio)
            break
        }

        const calculatedProgress = currentStageData.start +
          (currentStageData.end - currentStageData.start) * easeProgress

        // 添加微小的正向随机波动，确保进度只增不减
        const positiveNoise = Math.random() * 2 // 0-2%的正向波动
        progress.value = Math.max(currentProgress, Math.min(targetProgress, calculatedProgress + positiveNoise))
      } else {
        // 突进完成，开始随机停顿
        currentProgress = currentStageData.end
        progress.value = currentProgress
        isPaused = true
        stageStartTime = Date.now()
      }
    } else {
      // 随机停顿阶段
      if (elapsed >= currentStageData.pause) {
        // 停顿结束，生成下一个随机阶段
        currentStage++
        currentStageData = generateRandomStage()
        isPaused = false
        stageStartTime = Date.now()
      } else {
        // 在停顿期间保持当前进度，偶尔显示微小正向假启动
        const fakeStart = Math.random() < 0.05 ? Math.random() * 1 : 0 // 5%概率假启动，最多1%
        progress.value = Math.max(currentProgress, Math.min(targetProgress, currentProgress + fakeStart))
      }
    }
  }

  const startTime = Date.now()
  progressInterval = setInterval(executeRandomStage, 30) // 30ms更新，更流畅
}

const stopProgress = () => {
  if (progressInterval) {
    clearInterval(progressInterval)
    progressInterval = null
  }
}

onMounted(() => {
  startProgress()
})

onUnmounted(() => {
  stopProgress()
})

defineExpose({
  stopProgress
})
</script>

<style scoped>
.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  margin-top: 20px;
}

.loading-box {
  background: var(--bg-card);
  border: 2px solid var(--border-primary);
  border-radius: 8px;
  padding: 20px;
  min-width: 300px;
  box-shadow: 0 4px 12px var(--shadow-light);
  text-align: center;
}

.moving-icon-container {
  height: 60px;
  overflow: hidden;
  margin-bottom: 16px;
  position: relative;
}

.moving-icon {
  height: 40px;
  width: auto;
  position: absolute;
  top: 10px;
  animation: moveLeftRight 2s ease-in-out infinite;
}

@keyframes moveLeftRight {
  0%, 100% {
    left: 20px;
  }
  50% {
    left: calc(100% - 60px);
  }
}

.progress-container {
  margin-bottom: 12px;
}

.progress-bar {
  width: 100%;
  height: 8px;
  background: var(--bg-primary);
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 8px;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, var(--battle-gold), var(--sword-silver));
  border-radius: 4px;
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 14px;
  color: var(--text-secondary);
  font-weight: 500;
}

.loading-text {
  font-size: 14px;
  color: var(--text-primary);
  font-weight: 500;
}
</style>