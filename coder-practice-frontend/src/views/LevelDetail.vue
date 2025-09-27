<template>
  <div class="battlefield-level">
    <div class="battlefield-content">
      <!-- 返回按钮 -->
      <div class="back-button">
        <el-button @click="router.back()" icon="Back" class="battle-button-secondary">
          🛡️ 返回军营
        </el-button>
      </div>

      <!-- 战役信息 -->
      <div v-if="level" class="battle-container">
        <div class="battle-card">
          <div class="battle-level-header">
            <h2 class="battle-title">{{ level.levelName }}</h2>
            <div class="battle-meta">
              <span class="battle-difficulty" :class="level.difficulty">{{ level.difficulty }}</span>
              <span v-if="level.direction" class="direction-badge">
                {{ getDirectionIcon(level.direction) }} {{ level.direction }}
              </span>
              <span class="target-salary">💰 目标军饷: {{ level.targetSalary }} 军饷/月</span>
            </div>
          </div>

          <!-- 战役任务 -->
          <div class="mission-section">
            <h3>📋 将军令</h3>
            <div class="mission-content">
              {{ level.levelDesc }}
            </div>
          </div>

          <!-- 兵器库 -->
          <div class="weapons-section">
            <h3>⚔️ 兵器库</h3>
            <p class="weapons-hint">请选择你认为合适的兵器投入到战场</p>

            <div class="weapons-grid">
              <div
                v-for="(option, index) in levelOptions"
                :key="index"
                class="weapon-item"
                :class="{ selected: selectedOptions.includes(option.optionName) }"
                draggable="true"
                @dragstart="handleDragStart($event, option)"
                @dragend="handleDragEnd"
                @click="toggleOption(option.optionName)"
              >
                <span class="weapon-icon">🗡️</span>
                {{ option.optionName }}
              </div>
            </div>
          </div>

          <!-- 战场部署 -->
          <div class="deployment-section">
            <h3>🛡️ 战场部署</h3>
            <div
              class="deployment-area"
              :class="{ 'drag-over': isDragOver }"
              @dragover="handleDragOver"
              @dragleave="handleDragLeave"
              @drop="handleDrop"
            >
              <div v-if="selectedOptions.length === 0" class="empty-hint">
                将兵器拖拽到这里部署，或点击兵器进行选择
              </div>
              <div v-for="optionName in selectedOptions" :key="optionName" class="deployed-weapon">
                <span class="weapon-icon">🗡️</span>
                <span>{{ optionName }}</span>
                <el-icon @click="removeOption(optionName)" class="remove-icon">
                  <Close />
                </el-icon>
              </div>
            </div>

            <!-- 加载动画组件 -->
            <div v-if="isSubmitting" class="loading-section">
              <LoadingAnimation text="将军正在部署中..." />
            </div>

            <div class="deployment-actions">
              <el-button
                size="large"
                :disabled="selectedOptions.length === 0 || isSubmitting"
                @click="submitAnswer"
                class="battle-button"
              >
                {{ isSubmitting ? '🚩 部署中...' : '⚔️ 开始征战' }}
              </el-button>
              <el-button
                size="large"
                @click="clearSelection"
                :disabled="selectedOptions.length === 0"
                class="battle-button-secondary"
              >
                🔄 重新部署
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-else class="battle-loading">
        <div class="loading-animation">
          <div class="loading-icon">⚔️</div>
          <p>正在准备战场...</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { getLevelById } from '@/api/level'
import { submitAnswer as submitAnswerApi } from '@/api/userLevel'
import { ElMessage } from 'element-plus'
import { Close, Back } from '@element-plus/icons-vue'
import LoadingAnimation from '@/components/LoadingAnimation.vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const userInfo = userStore.userInfo

const level = ref(null)
const levelOptions = ref([])
const selectedOptions = ref([])
const isSubmitting = ref(false)
const isDragOver = ref(false)

// 加载关卡详情
const loadLevel = async () => {
  try {
    const levelData = await getLevelById(route.params.id)
    level.value = levelData
    levelOptions.value = JSON.parse(levelData.options)
  } catch (error) {
    ElMessage.error('加载关卡失败')
    router.push('/home')
  }
}

// 拖拽功能
const handleDragStart = (event, option) => {
  event.dataTransfer.setData('optionName', option.optionName)
}

const handleDragEnd = () => {
  // 拖拽结束
}

const handleDragOver = (event) => {
  event.preventDefault()
  isDragOver.value = true
}

const handleDragLeave = () => {
  isDragOver.value = false
}

const handleDrop = (event) => {
  event.preventDefault()
  isDragOver.value = false

  const optionName = event.dataTransfer.getData('optionName')
  if (optionName && !selectedOptions.value.includes(optionName)) {
    selectedOptions.value.push(optionName)
  }
}

// 点击选择选项
const toggleOption = (optionName) => {
  const index = selectedOptions.value.indexOf(optionName)
  if (index > -1) {
    selectedOptions.value.splice(index, 1)
  } else {
    selectedOptions.value.push(optionName)
  }
}

// 移除选项
const removeOption = (optionName) => {
  const index = selectedOptions.value.indexOf(optionName)
  if (index > -1) {
    selectedOptions.value.splice(index, 1)
  }
}

// 清空选择
const clearSelection = () => {
  selectedOptions.value = []
}

// 获取方向图标
const getDirectionIcon = (direction) => {
  const iconMap = {
    '前端开发': '🎨',
    '后端开发': '⚙️',
    'Java后端开发': '☕',
    '软件测试': '🔍',
    'AI算法': '🤖',
    'Unity游戏开发': '🎮',
    '网络运维': '🌐',
    '全栈开发': '🚀',
    '小程序': '📱'
  }
  return iconMap[direction] || '🎯'
}

// 提交答案
const submitAnswer = async () => {
  if (selectedOptions.value.length === 0) {
    ElMessage.warning('请至少选择一个选项')
    return
  }
  isSubmitting.value = true

  // 检查用户登录状态
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录后再征战')
    const currentPath = window.location.pathname + window.location.search
    router.push(`/login?redirect=${encodeURIComponent(currentPath)}`)
    isSubmitting.value = false
    return
  }

  // 检查用户薪资信息完整性，如果缺失则尝试重新获取
  if (!userInfo.value?.salary) {
    try {
      await userStore.fetchCurrentUser()
      userInfo.value = userStore.userInfo

      // 如果重新获取后仍然没有薪资信息，跳转到登录页
      if (!userInfo.value?.salary) {
        ElMessage.warning('用户信息不完整，请重新登录')
        const currentPath = window.location.pathname + window.location.search
        router.push(`/login?redirect=${encodeURIComponent(currentPath)}`)
        isSubmitting.value = false
        return
      }
    } catch (error) {
      ElMessage.warning('获取用户信息失败，请重新登录')
      const currentPath = window.location.pathname + window.location.search
      router.push(`/login?redirect=${encodeURIComponent(currentPath)}`)
      isSubmitting.value = false
      return
    }
  }

  try {
    const result = await submitAnswerApi({
      levelId: parseInt(route.params.id),
      selectedOptions: selectedOptions.value,
    })

    // 更新用户薪资
    userStore.updateUserSalary(result.salaryChange)

    // 存储结果数据并跳转到结果页面
    setTimeout(() => {
      sessionStorage.setItem('currentResult', JSON.stringify(result))
      router.push(`/result/${result.id}`)
    }, 1000)
  } catch (error) {
    console.error('提交答案错误:', error)

    // 登录态验证失败时，页面会自动跳转到登录页，这里不需要处理
    // 其他错误情况下显示错误消息并重置状态
    if (error?.message !== '登录态验证失败') {
      if (error.message === 'Failed to fetch') {
        ElMessage.error('网络连接失败，请检查网络后重试')
      } else {
        ElMessage.error(`提交失败: ${error.message || '请重试'}`)
      }
      isSubmitting.value = false
    }
  }
}

onMounted(() => {
  loadLevel()
})
</script>

<style scoped>
/* 引入战场主题样式 */
@import '@/styles/battlefield-theme.css';

.battlefield-level {
  min-height: 100vh;
  position: relative;
  overflow: hidden;
}

.battlefield-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
  position: relative;
  z-index: 1;
}

.back-button {
  margin-bottom: 30px;
}

.battle-card {
  background: var(--bg-card);
  border: 2px solid var(--border-primary);
  border-radius: 0;
  padding: 40px;
  box-shadow: 0 8px 32px var(--shadow-heavy);
  position: relative;
  overflow: hidden;
}

.battle-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, var(--battle-gold), var(--sword-silver), var(--battle-gold));
}

.battle-level-header {
  border-bottom: 2px solid var(--border-secondary);
  padding-bottom: 30px;
  margin-bottom: 40px;
  position: relative;
}

.battle-title {
  font-family: 'Noto Serif SC', serif;
  font-size: 2rem;
  font-weight: bold;
  color: var(--text-primary);
  margin-bottom: 20px;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
}

.battle-meta {
  display: flex;
  gap: 24px;
  align-items: center;
  flex-wrap: wrap;
}

.direction-badge {
  background: linear-gradient(135deg, rgba(139, 69, 19, 0.2) 0%, rgba(218, 165, 32, 0.2) 100%);
  color: var(--battle-gold);
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 600;
  border: 1px solid rgba(218, 165, 32, 0.3);
  white-space: nowrap;
}

.battle-difficulty {
  padding: 8px 16px;
  border-radius: 0;
  font-weight: 600;
  font-size: 14px;
  text-transform: uppercase;
  letter-spacing: 1px;
  border: 2px solid;
}

.battle-difficulty.简单 {
  background: var(--success-green);
  color: white;
  border-color: var(--success-green);
}

.battle-difficulty.中等 {
  background: var(--warning-orange);
  color: white;
  border-color: var(--warning-orange);
}

.battle-difficulty.困难 {
  background: var(--danger-red);
  color: white;
  border-color: var(--danger-red);
}

.target-salary {
  color: var(--battle-gold);
  font-weight: 600;
  font-family: 'Noto Serif SC', serif;
  font-size: 1.1rem;
  padding: 8px 16px;
  border: 1px solid var(--battle-gold);
  background: rgba(218, 165, 32, 0.1);
}

.mission-section {
  margin-bottom: 40px;
}

.mission-section h3 {
  font-family: 'Noto Serif SC', serif;
  color: var(--text-primary);
  margin-bottom: 20px;
  font-size: 1.3rem;
  font-weight: bold;
}

.mission-content {
  background: var(--bg-primary);
  border: 2px solid var(--border-secondary);
  border-radius: 0;
  padding: 30px;
  line-height: 1.8;
  color: var(--text-secondary);
  border-left: 4px solid var(--battle-gold);
  font-family: 'Noto Serif SC', serif;
  font-size: 1.1rem;
  position: relative;
}

.mission-content::before {
  content: '📜';
  position: absolute;
  top: -10px;
  left: 20px;
  background: var(--bg-primary);
  padding: 0 10px;
  font-size: 1.5rem;
}

.weapons-section {
  margin-bottom: 40px;
}

.weapons-section h3 {
  font-family: 'Noto Serif SC', serif;
  color: var(--text-primary);
  margin-bottom: 16px;
  font-size: 1.3rem;
  font-weight: bold;
}

.weapons-hint {
  color: var(--text-secondary);
  margin-bottom: 24px;
  font-size: 16px;
  font-style: italic;
}

.weapons-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 16px;
}

.weapon-item {
  padding: 20px;
  border: 2px solid var(--border-primary);
  border-radius: 0;
  cursor: pointer;
  transition: all 0.3s ease;
  user-select: none;
  text-align: center;
  background: var(--bg-primary);
  position: relative;
  overflow: hidden;
}

.weapon-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(218, 165, 32, 0.2), transparent);
  transition: left 0.6s ease;
}

.weapon-item:hover {
  border-color: var(--battle-gold);
  background: var(--bg-card);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px var(--shadow-light);
}

.weapon-item:hover::before {
  left: 100%;
}

.weapon-item.selected {
  border-color: var(--battle-gold);
  background: linear-gradient(135deg, var(--primary-brown), var(--secondary-brown));
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px var(--shadow-heavy);
}

.weapon-icon {
  display: block;
  font-size: 1.5rem;
  margin-bottom: 8px;
}

.deployment-section h3 {
  font-family: 'Noto Serif SC', serif;
  color: var(--text-primary);
  margin-bottom: 20px;
  font-size: 1.3rem;
  font-weight: bold;
}

.deployment-area {
  min-height: 120px;
  border: 2px dashed var(--border-secondary);
  border-radius: 0;
  padding: 20px;
  margin: 20px 0;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;
  background: var(--bg-primary);
  transition: all 0.3s ease;
  position: relative;
}

.deployment-area.drag-over {
  border-color: var(--battle-gold);
  background: rgba(218, 165, 32, 0.05);
  border-style: solid;
}

.deployment-area::before {
  content: '🛡️';
  position: absolute;
  top: -12px;
  left: 20px;
  background: var(--bg-primary);
  padding: 0 8px;
  font-size: 1.2rem;
}

.empty-hint {
  color: var(--text-secondary);
  font-style: italic;
  font-size: 16px;
  text-align: center;
}

.deployed-weapon {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: linear-gradient(135deg, var(--primary-brown), var(--secondary-brown));
  color: white;
  padding: 12px 16px;
  border-radius: 0;
  margin: 8px;
  font-size: 14px;
  font-weight: 500;
  border: 1px solid var(--border-primary);
  transition: all 0.3s ease;
}

.deployed-weapon:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px var(--shadow-light);
}

.loading-section {
  margin: 20px 0;
  display: flex;
  justify-content: center;
}

.deployment-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
  margin-top: 30px;
}

.remove-icon {
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 16px;
}

.remove-icon:hover {
  transform: scale(1.2);
  color: var(--danger-red);
}

.battle-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 400px;
  color: var(--text-secondary);
}

.loading-animation {
  text-align: center;
}

.loading-icon {
  font-size: 4rem;
  margin-bottom: 20px;
  animation: pulse 2s ease-in-out infinite;
}

.battle-loading p {
  font-family: 'Noto Serif SC', serif;
  font-size: 1.2rem;
  font-style: italic;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .battlefield-content {
    padding: 20px;
  }

  .battle-card {
    padding: 24px;
  }

  .battle-title {
    font-size: 1.5rem;
  }

  .battle-meta {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }

  .weapons-grid {
    grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
    gap: 12px;
  }

  .deployment-actions {
    flex-direction: column;
    gap: 12px;
  }

  .deployment-area {
    min-height: 100px;
    padding: 16px;
  }
}

/* 动画效果 */
@keyframes pulse {
  0%, 100% {
    transform: scale(1);
    opacity: 0.8;
  }
  50% {
    transform: scale(1.1);
    opacity: 1;
  }
}

.battle-card {
  animation: fadeInUp 0.8s ease-out;
}

.weapon-item {
  animation: fadeInUp 0.6s ease-out;
}

.weapon-item:nth-child(1) { animation-delay: 0.1s; }
.weapon-item:nth-child(2) { animation-delay: 0.2s; }
.weapon-item:nth-child(3) { animation-delay: 0.3s; }
.weapon-item:nth-child(4) { animation-delay: 0.4s; }
.weapon-item:nth-child(5) { animation-delay: 0.5s; }
.weapon-item:nth-child(6) { animation-delay: 0.6s; }

.deployment-section {
  animation: fadeInUp 0.6s ease-out 0.7s both;
}
</style>
