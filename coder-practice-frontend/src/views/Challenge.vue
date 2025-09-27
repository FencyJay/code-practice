<template>
  <div class="battlefield-bg">
    <!-- 顶部导航栏 - 城池壁垒 -->
    <div class="fortress-header">
      <div class="header-content">
        <div class="logo">
          <h1 class="vintage-title flag-wave">⚔️ 技术练兵场</h1>
        </div>
        <div class="user-info">
          <div v-if="userInfo" class="battlefield-commander">
            <div class="commander-dropdown" @click="toggleDropdown">
              <div class="commander-avatar">
                <div class="avatar-badge">
                  <img
                    v-if="userInfo?.avatar"
                    :src="userInfo.avatar"
                    :alt="userInfo?.nickname || userInfo?.username"
                    class="avatar-image"
                  />
                  <span v-else class="avatar-text">
                    {{ userInfo?.username?.charAt(0).toUpperCase() }}
                  </span>
                </div>
                <div class="avatar-decoration">⚔️</div>
              </div>
              <div class="commander-info">
                <div class="commander-rank">{{ userInfo?.nickname || userInfo?.username }}</div>
                <div class="commander-salary">💰 {{ userInfo?.salary }} 文</div>
              </div>
              <div class="dropdown-arrow" :class="{ 'dropdown-arrow-open': isDropdownOpen }">▼</div>
            </div>

            <!-- 下拉菜单 -->
            <transition name="dropdown-fade">
              <div v-if="isDropdownOpen" class="battlefield-dropdown">
                <div class="dropdown-header">
                  <div class="dropdown-title">将军指挥所</div>
                </div>
                <div class="dropdown-menu">
                  <div class="menu-item" @click="handleCommand('history')">
                    <span class="menu-icon">📊</span>
                    <span class="menu-text">征战史料</span>
                    <span class="menu-hint">查看战绩</span>
                  </div>
                  <div
                    v-if="userInfo?.userRole === 'admin'"
                    class="menu-item"
                    @click="handleCommand('admin')"
                  >
                    <span class="menu-icon">👑</span>
                    <span class="menu-text">将军帐</span>
                    <span class="menu-hint">管理系统</span>
                  </div>
                  <div class="menu-divider"></div>
                  <div class="menu-item" @click="handleCommand('logout')">
                    <span class="menu-icon">🚪</span>
                    <span class="menu-text">卸甲归田</span>
                    <span class="menu-hint">退出军营</span>
                  </div>
                </div>
              </div>
            </transition>
          </div>
          <div v-else class="battlefield-auth">
            <router-link to="/login" class="auth-shield">
              <span class="shield-icon">🛡️</span>
              <span class="shield-text">登录</span>
            </router-link>
            <router-link to="/register" class="auth-enlist">
              <span class="enlist-icon">📝</span>
              <span class="enlist-text">注册</span>
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- 挑战页面主体内容 -->
    <div class="challenge-container battlefield-container battlefield-typography">
      <!-- 古战场标题 -->
      <div class="ancient-battle-title">
        <div class="title-scroll">
          <h1 class="battle-emperor-title">
            <span class="title-decoration">⚔️</span>
            选择你的技术方向
            <span class="title-decoration">⚔️</span>
          </h1>
        </div>
        <div class="imperial-seal">
          <p class="emperor-decree">
            将军，请选择您要专精的技术领域<br>
            <span class="ai-advisor">AI参谋长将为您定制专属的技术挑战</span>
          </p>
        </div>

        <!-- 当前选择的方向 -->
        <div v-if="selectedDirection" class="chosen-path">
          <div class="path-indicator">
            <span class="path-emoji">🏹</span>
            <span class="path-text">已择征途：</span>
            <span class="path-name">{{ selectedDirection }}</span>
            <span class="path-icon">{{ getDirectionIcon(selectedDirection) }}</span>
          </div>
        </div>
      </div>

      <!-- 方向选择网格 -->
      <div class="directions-grid">
        <div
          v-for="direction in directions"
          :key="direction.value"
          class="direction-card battle-card soldier-enter"
          :class="{
            'selected': selectedDirection === direction.value,
            'hover-effect': !selectedDirection || selectedDirection === direction.value
          }"
          @click="selectDirection(direction.value)"
          :data-animation="getAnimationClass(direction.value)"
        >
          <div class="direction-icon-large">
            {{ direction.icon }}
          </div>
          <h3 class="direction-title">{{ direction.label }}</h3>
          <p class="direction-description">{{ direction.description }}</p>

          <!-- 技术栈标签 -->
          <div class="tech-tags">
            <span
              v-for="tech in direction.techStack"
              :key="tech"
              class="tech-tag"
            >
              {{ tech }}
            </span>
          </div>

          <!-- 选中指示器 -->
          <div v-if="selectedDirection === direction.value" class="selected-indicator">
            <span class="selected-check">✓</span>
            <span class="selected-text">已选择</span>
          </div>
        </div>
      </div>

      <!-- 开始挑战按钮区域 -->
      <div class="challenge-action battle-card soldier-enter">
        <div class="action-info">
          <h3 class="action-title">⚔️ 准备开始挑战</h3>
          <p class="action-description">
            <span v-if="selectedDirection">
              已选择 {{ selectedDirection }} 方向，点击下方按钮开始您的技术挑战之旅
            </span>
            <span v-else class="hint-text">
              请先选择您要挑战的技术方向
            </span>
          </p>

          <!-- 用户信息显示 -->
          <div v-if="userInfo" class="user-stats">
            <div class="stat-item">
              <span class="stat-label">当前俸禄：</span>
              <span class="stat-value salary-highlight">{{ userInfo.salary }}</span>
              <span class="stat-unit">文/月</span>
            </div>
          </div>
        </div>

        <div class="action-buttons">
          <button
            class="challenge-button"
            :disabled="!selectedDirection || isGenerating"
            @click="startChallenge"
          >
            <span v-if="isGenerating">🚩 战役部署中...</span>
            <span v-else>⚔️ 开始挑战</span>
          </button>

          <button
            class="back-button"
            @click="goBack"
          >
            🏠 返回主页
          </button>
        </div>

        <!-- 加载动画 -->
        <div v-if="isGenerating" class="challenge-loading">
          <LoadingAnimation text="参谋长正在制定战术..." />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { generateLevel } from '@/api/level'
import { ElMessage } from 'element-plus'
import LoadingAnimation from '@/components/LoadingAnimation.vue'
import { initBattlefieldAnimations, injectAnimationStyles } from '@/utils/battlefield-animations'

const router = useRouter()
const userStore = useUserStore()

const isGenerating = ref(false)
const selectedDirection = ref(null)
const userInfo = ref(userStore.userInfo)
const isDropdownOpen = ref(false)

// 技术方向选项
const directions = ref([
  {
    value: '前端开发',
    label: '前端开发',
    icon: '🎨',
    description: '专注于用户界面设计和交互体验，构建现代化的Web应用',
    techStack: ['React', 'Vue', 'Angular', 'TypeScript', 'Webpack', 'Sass']
  },
  {
    value: '后端开发',
    label: '后端开发',
    icon: '⚙️',
    description: '专注于服务器端逻辑、数据处理和API设计，构建稳定可靠的后端系统',
    techStack: ['Java', 'Python', 'Go', 'Spring', 'Django', 'MySQL', 'Redis']
  },
  {
    value: 'Java后端开发',
    label: 'Java后端开发',
    icon: '☕',
    description: '深度专精Java生态系统，构建企业级应用和微服务架构',
    techStack: ['Spring Boot', 'Spring Cloud', 'MyBatis', 'JVM', 'MySQL', 'Redis', 'RabbitMQ']
  },
  {
    value: '软件测试',
    label: '软件测试',
    icon: '🔍',
    description: '专注于质量保证和测试策略，确保软件产品的稳定性和可靠性',
    techStack: ['Selenium', 'JUnit', 'Postman', 'JMeter', 'Cypress', 'Jenkins']
  },
  {
    value: 'AI算法',
    label: 'AI算法',
    icon: '🤖',
    description: '专注于人工智能和机器学习技术，构建智能化的解决方案',
    techStack: ['Python', 'TensorFlow', 'PyTorch', 'Scikit-learn', 'NLP', 'Deep Learning']
  },
  {
    value: 'Unity游戏开发',
    label: 'Unity游戏开发',
    icon: '🎮',
    description: '专注于游戏开发，使用Unity引擎创造精彩的游戏体验',
    techStack: ['Unity', 'C#', '游戏设计', '3D建模', 'Shader', '物理引擎']
  },
  {
    value: '网络运维',
    label: '网络运维',
    icon: '🌐',
    description: '专注于系统运维和网络架构，保障服务的稳定运行和高可用性',
    techStack: ['Linux', 'Docker', 'Kubernetes', '监控', 'CI/CD', '云服务']
  },
  {
    value: '全栈开发',
    label: '全栈开发',
    icon: '🚀',
    description: '精通前后端技术，能够独立完成完整的Web应用开发',
    techStack: ['React/Vue', 'Node.js', 'Python', '数据库', 'DevOps', '云服务']
  },
  {
    value: '小程序',
    label: '小程序',
    icon: '📱',
    description: '专注于移动端小程序开发，构建轻量级的移动应用',
    techStack: ['微信小程序', '支付宝小程序', 'uni-app', '跨端开发', '云开发']
  }
])

// 选择技术方向
const selectDirection = (direction) => {
  selectedDirection.value = direction
}

// 获取方向图标
const getDirectionIcon = (direction) => {
  const found = directions.value.find(d => d.value === direction)
  return found ? found.icon : '🎯'
}

// 获取动画类
const getAnimationClass = (direction) => {
  const index = directions.value.findIndex(d => d.value === direction)
  const animations = ['slideInLeft', 'fadeInUp', 'slideInRight', 'fadeInUp', 'slideInLeft', 'fadeInUp', 'slideInRight', 'fadeInUp']
  return animations[index] || 'fadeInUp'
}

// 开始挑战
const startChallenge = async () => {
  if (!selectedDirection.value || isGenerating.value) return

  // 检查用户登录信息和薪资
  if (!userInfo.value || !userInfo.value.salary) {
    try {
      await userStore.fetchCurrentUser()
      userInfo.value = userStore.userInfo

      if (!userInfo.value || !userInfo.value.salary) {
        const currentPath = window.location.pathname + window.location.search
        router.push(`/login?redirect=${encodeURIComponent(currentPath)}`)
        return
      }
    } catch (error) {
      const currentPath = window.location.pathname + window.location.search
      router.push(`/login?redirect=${encodeURIComponent(currentPath)}`)
      return
    }
  }

  isGenerating.value = true
  try {
    const level = await generateLevel(userInfo.value.salary, selectedDirection.value)
    ElMessage.success('关卡生成成功！')
    router.push(`/level/${level.id}`)
  } catch (error) {
    if (error?.message !== '登录态验证失败') {
      ElMessage.error('关卡生成失败，请重试')
      isGenerating.value = false
    }
  }
}

// 返回主页
const goBack = () => {
  router.push('/home')
}

// 下拉菜单相关方法
const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value
}

const closeDropdown = () => {
  isDropdownOpen.value = false
}

const handleCommand = async (command) => {
  closeDropdown()
  switch (command) {
    case 'history':
      if (!userStore.isLoggedIn) {
        ElMessage.warning('请先登录后再查看历史档案')
        router.push('/login')
        return
      }
      router.push('/history')
      break
    case 'admin':
      router.push('/admin')
      break
    case 'logout':
      await userStore.userLogout()
      router.push('/login')
      break
  }
}

const handleClickOutside = (event) => {
  const dropdown = document.querySelector('.battlefield-commander')
  if (dropdown && !dropdown.contains(event.target)) {
    closeDropdown()
  }
}

let animations = null

onMounted(() => {
  userStore.fetchCurrentUser()
  userInfo.value = userStore.userInfo

  // 注入动画样式
  injectAnimationStyles()

  // 初始化战场动画
  animations = initBattlefieldAnimations()

  // 点击外部关闭下拉菜单
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  // 清理动画
  if (animations) {
    animations.destroy()
  }

  // 移除事件监听
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
/* 引入战场主题样式 */
@import '@/styles/battlefield-theme.css';



/* 挑战页面容器 */
.challenge-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
  width: 100%;
  box-sizing: border-box;
}

/* 古战场标题样式 */
.ancient-battle-title {
  text-align: center;
  margin-bottom: 50px;
  position: relative;
}

.title-scroll {
  position: relative;
  margin-bottom: 30px;
}

.title-scroll::before {
  content: '';
  position: absolute;
  top: 50%;
  left: -50px;
  right: -50px;
  height: 2px;
  background: linear-gradient(90deg, transparent, var(--battle-gold), transparent);
  transform: translateY(-50%);
}

.battle-emperor-title {
  font-family: 'Noto Serif SC', serif;
  font-size: 2.8rem;
  font-weight: bold;
  color: var(--battle-gold);
  text-shadow:
    2px 2px 4px rgba(0, 0, 0, 0.5),
    0 0 20px rgba(218, 165, 32, 0.3);
  margin: 0;
  position: relative;
  display: inline-block;
  letter-spacing: 3px;
}

.title-decoration {
  font-size: 1.5rem;
  margin: 0 15px;
  animation: swordGlow 2s ease-in-out infinite alternate;
}

@keyframes swordGlow {
  0% { opacity: 0.7; transform: scale(1); }
  100% { opacity: 1; transform: scale(1.1); }
}

.imperial-seal {
  position: relative;
  display: inline-block;
}

.imperial-seal::before {
  content: '';
  position: absolute;
  top: -10px;
  left: 50%;
  transform: translateX(-50%);
  width: 100px;
  height: 1px;
  background: linear-gradient(90deg, transparent, var(--battle-gold), transparent);
}

.imperial-seal::after {
  content: '';
  position: absolute;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%);
  width: 100px;
  height: 1px;
  background: linear-gradient(90deg, transparent, var(--battle-gold), transparent);
}

.emperor-decree {
  font-family: 'Noto Serif SC', serif;
  font-size: 1.2rem;
  color: var(--text-secondary);
  line-height: 1.8;
  margin: 0;
  font-weight: 500;
}

.ai-advisor {
  color: var(--battle-gold);
  font-weight: 600;
  font-size: 1rem;
  opacity: 0.9;
}

.chosen-path {
  margin-top: 30px;
  position: relative;
}

.path-indicator {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 15px 30px;
  background: linear-gradient(135deg, rgba(139, 69, 19, 0.2) 0%, rgba(218, 165, 32, 0.2) 100%);
  border: 1px solid rgba(218, 165, 32, 0.3);
  border-radius: 30px;
  font-family: 'Noto Serif SC', serif;
  position: relative;
}

.path-indicator::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(90deg, transparent, rgba(218, 165, 32, 0.1), transparent);
  border-radius: 30px;
  animation: shimmer 3s ease-in-out infinite;
}

@keyframes shimmer {
  0% { opacity: 0; }
  50% { opacity: 1; }
  100% { opacity: 0; }
}

.path-emoji {
  font-size: 1.3rem;
  animation: bounce 2s ease-in-out infinite;
}

.path-text {
  color: var(--text-primary);
  font-weight: 600;
  font-size: 1rem;
}

.path-name {
  color: var(--battle-gold);
  font-weight: bold;
  font-size: 1.1rem;
}

.path-icon {
  font-size: 1.2rem;
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-3px); }
}



/* 方向选择网格 */
.directions-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 30px;
}

/* 响应式设计 - 在较小屏幕上自动调整 */
@media (max-width: 1024px) {
  .directions-grid {
    grid-template-columns: repeat(3, 1fr);
    gap: 12px;
  }
}

@media (max-width: 768px) {
  .directions-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 12px;
  }
}

@media (max-width: 480px) {
  .directions-grid {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .direction-card {
    padding: 16px;
    min-height: 110px;
  }

  .direction-icon-large {
    font-size: 2rem;
    margin-bottom: 6px;
  }

  .tech-tags {
    gap: 2px;
  }

  .tech-tag {
    font-size: 0.6rem;
    padding: 1px 3px;
  }
}

.direction-card {
  padding: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  border: 2px solid var(--border-primary);
  background: var(--bg-card);
  min-height: 140px;
  display: flex;
  flex-direction: column;
}

.direction-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px var(--shadow-light);
  border-color: var(--battle-gold);
}

.direction-card.selected {
  background: linear-gradient(135deg, rgba(218, 165, 32, 0.15) 0%, rgba(218, 165, 32, 0.08) 100%);
  border-color: var(--battle-gold);
  box-shadow: 0 8px 24px rgba(218, 165, 32, 0.3);
}

.direction-icon-large {
  font-size: 2.2rem;
  margin-bottom: 8px;
  text-align: center;
}

.direction-title {
  color: var(--text-primary);
  font-size: 1.1rem;
  font-weight: bold;
  margin: 0 0 6px 0;
  text-align: center;
}

.direction-description {
  color: var(--text-secondary);
  font-size: 0.8rem;
  line-height: 1.3;
  margin-bottom: 12px;
  text-align: center;
  flex: 1;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 技术栈标签 */
.tech-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  justify-content: center;
  margin-top: auto;
}

.tech-tag {
  background: rgba(139, 69, 19, 0.1);
  color: var(--text-secondary);
  padding: 2px 6px;
  border-radius: 8px;
  font-size: 0.7rem;
  font-weight: 500;
  border: 1px solid rgba(139, 69, 19, 0.2);
  white-space: nowrap;
}

.direction-card.selected .tech-tag {
  background: rgba(218, 165, 32, 0.2);
  color: var(--battle-gold);
  border-color: var(--battle-gold);
}

/* 选中指示器 */
.selected-indicator {
  position: absolute;
  top: 12px;
  right: 12px;
  background: linear-gradient(135deg, var(--battle-gold) 0%, #ffd700 100%);
  color: var(--primary-brown);
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 6px;
  box-shadow: 0 2px 8px rgba(218, 165, 32, 0.4);
}

.selected-check {
  font-size: 1rem;
}

/* 挑战操作区域 */
.challenge-action {
  padding: 30px;
  text-align: center;
}

.action-info {
  margin-bottom: 30px;
}

.action-title {
  color: var(--text-primary);
  font-size: 1.5rem;
  margin: 0 0 16px 0;
}

.action-description {
  color: var(--text-secondary);
  font-size: 1rem;
  line-height: 1.6;
  margin-bottom: 20px;
}

.hint-text {
  color: var(--text-muted);
  font-style: italic;
}

/* 用户信息显示 */
.user-stats {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin-top: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.stat-label {
  color: var(--text-secondary);
  font-weight: 600;
}

.stat-value {
  color: var(--text-primary);
  font-weight: bold;
  font-size: 1.1rem;
}

.stat-unit {
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.salary-highlight {
  color: var(--battle-gold);
  font-weight: bold;
  font-size: 1.2rem;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  gap: 16px;
  justify-content: center;
  align-items: center;
}

.challenge-button {
  background: linear-gradient(135deg, var(--battle-gold) 0%, var(--light-brown) 100%);
  color: white;
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-radius: 0;
  padding: 16px 48px;
  font-size: 1.2rem;
  font-weight: bold;
  text-transform: uppercase;
  letter-spacing: 2px;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
  box-shadow:
    0 6px 12px rgba(0, 0, 0, 0.4),
    inset 0 1px 0 rgba(255, 255, 255, 0.2);
  cursor: pointer;
  min-width: 200px;
}

.challenge-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transition: left 0.6s ease;
}

.challenge-button:hover::before {
  left: 100%;
}

.challenge-button:hover:not(:disabled) {
  transform: translateY(-4px) scale(1.05);
  box-shadow:
    0 12px 24px rgba(0, 0, 0, 0.6),
    inset 0 1px 0 rgba(255, 255, 255, 0.3);
  border-color: rgba(255, 255, 255, 0.5);
}

.challenge-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
  animation: challengePulse 2s infinite;
}

.back-button {
  background: transparent;
  color: var(--text-secondary);
  border: 2px solid var(--border-primary);
  border-radius: 0;
  padding: 14px 32px;
  font-size: 1rem;
  font-weight: 600;
  transition: all 0.3s ease;
  cursor: pointer;
}

.back-button:hover {
  background: rgba(139, 69, 19, 0.1);
  border-color: var(--battle-gold);
  color: var(--battle-gold);
  transform: translateY(-2px);
}

@keyframes challengePulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
}

/* 加载动画 */
.challenge-loading {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

/* 城池导航栏样式（与主页保持一致） */
.fortress-header {
  padding: 16px 24px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
}

.logo h1 {
  margin: 0;
  font-size: 1.8rem;
}

/* 战场指挥官信息区 */
.battlefield-commander {
  position: relative;
  z-index: 100;
}

.commander-dropdown {
  background: linear-gradient(135deg, var(--bg-card) 0%, rgba(139, 69, 19, 0.1) 100%);
  border: 2px solid var(--border-primary);
  border-radius: 0;
  padding: 6px 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 160px;
}

.commander-dropdown::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, var(--battle-gold), var(--sword-silver), var(--battle-gold));
}

.commander-dropdown:hover {
  border-color: var(--battle-gold);
  box-shadow: 0 8px 16px var(--shadow-heavy);
  transform: translateY(-2px);
}

/* 指挥官头像 */
.commander-avatar {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-badge {
  width: 32px;
  height: 32px;
  background: linear-gradient(135deg, var(--primary-brown) 0%, var(--secondary-brown) 100%);
  border: 2px solid var(--battle-gold);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  font-family: 'Noto Serif SC', serif;
  font-size: 1rem;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
  overflow: hidden;
}

.avatar-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
}

.avatar-text {
  color: white;
  font-weight: bold;
  font-family: 'Noto Serif SC', serif;
  font-size: 1rem;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
}

.avatar-decoration {
  position: absolute;
  top: -5px;
  right: -5px;
  font-size: 0.8rem;
  filter: drop-shadow(1px 1px 2px rgba(0, 0, 0, 0.3));
}

/* 指挥官信息 */
.commander-info {
  flex: 1;
}

.commander-rank {
  color: var(--text-primary);
  font-weight: bold;
  font-family: 'Noto Serif SC', serif;
  font-size: 0.8rem;
  text-shadow: 1px 1px 2px rgba(139, 69, 19, 0.1);
}

.commander-salary {
  color: var(--battle-gold);
  font-size: 0.7rem;
  font-weight: 600;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
}

/* 下拉箭头 */
.dropdown-arrow {
  color: var(--text-secondary);
  font-size: 0.75rem;
  transition: transform 0.3s ease;
}

.dropdown-arrow-open {
  transform: rotate(180deg);
}

/* 战场下拉菜单 - 优化尺寸 */
.battlefield-dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: 4px;
  width: 160px;
  max-width: 200px;
  background: linear-gradient(135deg, var(--bg-card) 0%, rgba(139, 69, 19, 0.05) 100%);
  border: 1px solid var(--border-primary);
  border-radius: 4px;
  box-shadow: 0 6px 12px var(--shadow-heavy);
  overflow: hidden;
}

.battlefield-dropdown::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, var(--battle-gold), var(--sword-silver), var(--battle-gold));
}

.dropdown-header {
  background: linear-gradient(135deg, rgba(139, 69, 19, 0.08) 0%, rgba(218, 165, 32, 0.08) 100%);
  padding: 6px 10px;
  border-bottom: 1px solid var(--border-secondary);
}

.dropdown-title {
  color: var(--text-primary);
  font-weight: bold;
  font-family: 'Noto Serif SC', serif;
  text-align: center;
  font-size: 0.75rem;
  text-shadow: 1px 1px 2px rgba(139, 69, 19, 0.1);
}

.dropdown-menu {
  padding: 2px 0;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  line-height: 1.2;
}

.menu-item:hover {
  background: rgba(218, 165, 32, 0.12);
  color: var(--battle-gold);
  transform: translateX(2px);
}

.menu-item::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 2px;
  background: linear-gradient(180deg, var(--battle-gold), var(--sword-silver));
  transform: scaleY(0);
  transition: transform 0.3s ease;
}

.menu-item:hover::before {
  transform: scaleY(1);
}

.menu-icon {
  font-size: 0.9rem;
  width: 16px;
  text-align: center;
}

.menu-text {
  flex: 1;
  font-weight: 600;
  color: var(--text-primary);
  font-size: 0.8rem;
}

.menu-hint {
  font-size: 0.6rem;
  color: var(--text-secondary);
  font-style: italic;
  opacity: 0.8;
}

.menu-divider {
  height: 1px;
  background: linear-gradient(90deg, transparent, var(--border-secondary), transparent);
  margin: 4px 12px;
}

/* 登录注册按钮 */
.auth-shield, .auth-enlist {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  text-decoration: none;
  border-radius: 0;
  transition: all 0.3s ease;
  font-weight: 600;
  font-family: 'Noto Serif SC', serif;
  position: relative;
}

.auth-shield::before,
.auth-enlist::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, var(--battle-gold), var(--sword-silver), var(--battle-gold));
}

.auth-shield {
  background: linear-gradient(135deg, var(--primary-brown) 0%, var(--secondary-brown) 100%);
  color: white;
  border: 2px solid var(--border-primary);
}

.auth-shield:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px var(--shadow-heavy);
  border-color: var(--battle-gold);
}

.auth-enlist {
  background: transparent;
  color: var(--battle-gold);
  border: 2px solid var(--battle-gold);
}

.auth-enlist:hover {
  background: rgba(218, 165, 32, 0.1);
  transform: translateY(-2px);
  box-shadow: 0 6px 12px var(--shadow-light);
}

.shield-icon, .enlist-icon {
  font-size: 1rem;
}

/* 下拉动画 */
.dropdown-fade-enter-active,
.dropdown-fade-leave-active {
  transition: all 0.3s ease;
}

.dropdown-fade-enter-from,
.dropdown-fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .challenge-container {
    padding: 20px 16px;
  }

  .battle-emperor-title {
    font-size: 2.2rem;
    letter-spacing: 2px;
  }

  .title-decoration {
    font-size: 1.2rem;
    margin: 0 10px;
  }

  .emperor-decree {
    font-size: 1rem;
  }

  .path-indicator {
    padding: 12px 20px;
  }

  .path-text {
    font-size: 0.9rem;
  }

  .path-name {
    font-size: 1rem;
  }

  .battlefield-dropdown {
    width: 200px;
    right: -20px;
  }

  .commander-dropdown {
    min-width: 160px;
    padding: 6px 12px;
  }

  .avatar-badge {
    width: 36px;
    height: 36px;
    font-size: 1rem;
  }

  .commander-rank {
    font-size: 0.8rem;
  }

  .commander-salary {
    font-size: 0.7rem;
  }

  .directions-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  
  .action-buttons {
    flex-direction: column;
    gap: 12px;
  }

  .challenge-button {
    width: 100%;
    padding: 14px 32px;
    font-size: 1rem;
  }

  .back-button {
    width: 100%;
  }

  .user-stats {
    flex-direction: column;
    gap: 12px;
  }

  .tech-tags {
    gap: 3px;
  }

  .tech-tag {
    font-size: 0.65rem;
    padding: 2px 4px;
  }
}

@media (max-width: 480px) {
  .challenge-container {
    padding: 16px 12px;
  }

  .battle-emperor-title {
    font-size: 1.8rem;
    letter-spacing: 1px;
  }

  .title-decoration {
    font-size: 1rem;
    margin: 0 8px;
  }

  .emperor-decree {
    font-size: 0.9rem;
  }

  .path-indicator {
    padding: 10px 16px;
    gap: 8px;
  }

  .path-emoji {
    font-size: 1.1rem;
  }

  .path-text {
    font-size: 0.8rem;
  }

  .path-name {
    font-size: 0.9rem;
  }

  .path-icon {
    font-size: 1rem;
  }

  .direction-card {
    padding: 12px;
    min-height: 120px;
  }

  .direction-icon-large {
    font-size: 1.8rem;
    margin-bottom: 6px;
  }

  .direction-title {
    font-size: 1.1rem;
  }

  .fortress-header {
    padding: 12px 16px;
  }

  .header-content {
    padding: 0 8px;
  }

  .logo h1 {
    font-size: 1.4rem;
  }
}

/* 战场主题动画效果 */
.battlefield-bg {
  position: relative;
  overflow-x: hidden;
}

.battlefield-bg::before {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image:
    radial-gradient(circle at 20% 30%, rgba(139, 69, 19, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 80% 70%, rgba(218, 165, 32, 0.05) 0%, transparent 50%);
  z-index: -1;
  pointer-events: none;
}
</style>