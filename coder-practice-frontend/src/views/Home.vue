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

    <!-- 战场横幅 -->
    <div class="battle-banner">
      <img src="@/assets/banner.png" alt="古战场" class="banner-image">
      <div class="banner-overlay">
        <div class="banner-content">
          <h1 class="banner-title">技术练兵如沙场点兵</h1>
          <p class="banner-subtitle">每一段代码都是你的武器，每一次调试都是你的战役</p>
          <button
            class="banner-battle-button"
            @click="goToChallenge"
          >
            ⚔️ 开始攻坚
          </button>
        </div>
      </div>
    </div>

    <!-- 横幅加载动画 -->
    <div v-if="isGenerating" class="banner-loading-section">
      <LoadingAnimation text="参谋长正在制定战术..." />
    </div>

    <!-- 主要内容区域 - 兵马阵列 -->
    <div class="battlefield-container battlefield-typography">
      <!-- 将军问候 -->
      <div class="general-welcome battle-card soldier-enter">
        <h2 class="vintage-title">欢迎归来，{{ userInfo?.nickname || userInfo?.username }}将军！</h2>
        <div class="battle-motto motto">
          "兵者，国之大事，死生之地，存亡之道，不可不察也。" - 孙子兵法
        </div>
        <div class="status-report">
          <div class="status-item">
            <span class="status-label">当前俸禄：</span>
            <span class="status-value salary-highlight">{{ userInfo?.salary }}</span>
            <span class="status-unit">文/月</span>
          </div>
          <div class="status-item">
            <span class="status-label">军中地位：</span>
            <span class="status-value">技术先锋</span>
          </div>
        </div>
      </div>

      <!-- 章节分隔 -->
      <div class="chapter-divider"></div>

      <!-- 排行榜 -->
      <RankingList />

      <!-- 章节分隔 -->
      <div class="chapter-divider"></div>

      <!-- 战场阵列 -->
      <div class="battle-formation">
        <!-- 攻坚营 -->
        <div class="battle-card soldier-enter" data-animation="slideInLeft">
          <div class="card-header">
            <h3 class="vintage-title">🎯 攻坚营</h3>
            <div class="card-badge">新挑战</div>
          </div>
          <p class="card-description">基于当前俸禄，AI参谋长为你定制技术攻坚战役</p>
          <button
            class="battle-button"
            @click="goToChallenge"
          >
            ⚔️ 开始攻坚
          </button>

          <!-- 加载动画组件 -->
          <div v-if="isGenerating" class="battle-button-loading">
            <LoadingAnimation text="参谋长正在制定战术..." />
          </div>
        </div>

        <!-- 史料馆 -->
        <div class="battle-card soldier-enter" data-animation="slideInRight">
          <div class="card-header">
            <h3 class="vintage-title">📜 史料馆</h3>
            <div class="card-badge">回顾</div>
          </div>
          <p class="card-description">查阅征战档案，回顾将军的成长轨迹</p>
          <button class="battle-button" @click="router.push('/history')">
            📖 查阅史料
          </button>
        </div>
      </div>

      <!-- 章节分隔 -->
      <div class="chapter-divider"></div>

      <!-- 精选关卡 -->
      <div v-if="recentLevels.length > 0" class="recent-battles battle-card soldier-enter" data-animation="fadeInUp">
        <h3 class="vintage-title">🏆 精选关卡</h3>
        <div class="battle-grid">
          <div
            v-for="level in recentLevels"
            :key="level.id"
            class="battle-record"
            @click="goToLevel(level.id)"
          >
            <div class="record-header">
              <h4>{{ level.levelName }}</h4>
              <div class="record-meta">
                <span class="difficulty-vintage" :class="getDifficultyClass(level.difficulty)">
                  {{ level.difficulty }}
                </span>
                <span v-if="level.direction" class="direction-badge">
                  {{ getDirectionIcon(level.direction) }} {{ level.direction }}
                </span>
              </div>
            </div>
            <p class="record-desc">{{ level.levelDesc?.substring(0, 80) }}...</p>
            <div class="record-footer">
              <span class="reward">💰 {{ level.targetSalary }} 文</span>
              <span class="action">⚔️ 开始征战</span>
            </div>
          </div>
        </div>

        <!-- 分页组件 -->
        <div v-if="totalPages > 1" class="battle-pagination">
          <div class="pagination-container">
            <button
              class="pagination-btn pagination-prev"
              :disabled="featuredPagination.current === 1 || featuredPagination.loading"
              @click="handlePrevPage"
            >
              ‹ 上一页
            </button>

            <div class="pagination-pages">
              <button
                v-for="page in Math.min(totalPages, 5)"
                :key="page"
                class="pagination-page"
                :class="{ active: featuredPagination.current === page }"
                :disabled="featuredPagination.loading"
                @click="handlePageChange(page)"
              >
                {{ page }}
              </button>
              <span v-if="totalPages > 5" class="pagination-ellipsis">...</span>
            </div>

            <button
              class="pagination-btn pagination-next"
              :disabled="featuredPagination.current === totalPages || featuredPagination.loading"
              @click="handleNextPage"
            >
              下一页 ›
            </button>
          </div>

          <div class="pagination-info">
            第 {{ featuredPagination.current }} 页 / 共 {{ totalPages }} 页
            (总计 {{ featuredPagination.total }} 个关卡)
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { generateLevel, getLevelList } from '@/api/level'
import { ElMessage } from 'element-plus'
import { ArrowDown } from '@element-plus/icons-vue'
import { initBattlefieldAnimations, injectAnimationStyles } from '@/utils/battlefield-animations'
import LoadingAnimation from '@/components/LoadingAnimation.vue'
import RankingList from '@/components/RankingList.vue'
import { storeToRefs } from 'pinia'

const router = useRouter()
const userStore = useUserStore()
const { userInfo } = storeToRefs(userStore)  // userInfo 是 ref，始终响应 store 变化

const isGenerating = ref(false)
const recentLevels = ref([])
const userAvatar = ref('')
const isDropdownOpen = ref(false)

// 精选关卡分页相关数据
const featuredPagination = ref({
  current: 1,
  pageSize: 6, // 每页显示6个关卡
  total: 0,
  loading: false
})

const generateNewLevel = async () => {
  if (isGenerating.value) return

  // 检查用户登录信息和薪资
  if (!userInfo.value || !userInfo.value.salary) {
    // 尝试重新获取用户信息，如果失败则跳转到登录页
    // 注意：这里不显示提示信息，因为请求拦截器会处理
    try {
      await userStore.fetchCurrentUser()
      userInfo.value = userStore.userInfo

      // 如果获取后仍然没有薪资信息，跳转到登录页
      if (!userInfo.value || !userInfo.value.salary) {
        const currentPath = window.location.pathname + window.location.search
        router.push(`/login?redirect=${encodeURIComponent(currentPath)}`)
        return
      }
    } catch (error) {
      // 获取用户信息失败，跳转到登录页
      // 注意：这里不显示提示信息，因为请求拦截器已经显示了
      const currentPath = window.location.pathname + window.location.search
      router.push(`/login?redirect=${encodeURIComponent(currentPath)}`)
      return
    }
  }

  isGenerating.value = true
  try {
    const level = await generateLevel(userInfo.value.salary)
    ElMessage.success('关卡生成成功！')
    router.push(`/level/${level.id}`)
  } catch (error) {
    // 登录态验证失败时，页面会自动跳转到登录页，这里不需要处理
    // 其他错误情况下显示错误消息并重置状态
    if (error?.message !== '登录态验证失败') {
      ElMessage.error('关卡生成失败，请重试')
      isGenerating.value = false
    }
  }
}

const goToLevel = (levelId) => {
  router.push(`/level/${levelId}`)
}

const goToChallenge = () => {
  router.push('/challenge')
}

const getDifficultyClass = (difficulty) => {
  const classMap = {
    '简单': 'easy',
    '中等': 'medium',
    '困难': 'hard'
  }
  return classMap[difficulty] || 'medium'
}

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
      // 检查是否登录，如果未登录则跳转到登录页
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

const loadRecentLevels = async () => {
  try {
    featuredPagination.value.loading = true
    const result = await getLevelList({
      current: featuredPagination.value.current,
      pageSize: featuredPagination.value.pageSize,
      isFeatured: true, // 查询精选关卡
    })
    recentLevels.value = result.records || []
    featuredPagination.value.total = result.total || 0
  } catch (error) {
    // 未登录时，不显示关卡列表
    if (error.message === '登录态验证失败') {
      recentLevels.value = []
      featuredPagination.value.total = 0
      return
    }
    console.error('加载关卡列表失败:', error)
  } finally {
    featuredPagination.value.loading = false
  }
}

// 分页相关方法
const handlePageChange = (page) => {
  featuredPagination.value.current = page
  loadRecentLevels()
}

const handlePrevPage = () => {
  if (featuredPagination.value.current > 1) {
    featuredPagination.value.current--
    loadRecentLevels()
  }
}

const handleNextPage = () => {
  const totalPages = Math.ceil(featuredPagination.value.total / featuredPagination.value.pageSize)
  if (featuredPagination.value.current < totalPages) {
    featuredPagination.value.current++
    loadRecentLevels()
  }
}

// 计算总页数
const totalPages = computed(() => {
  return Math.ceil(featuredPagination.value.total / featuredPagination.value.pageSize)
})

let animations = null

onMounted(async () => {

  // 先尝试获取当前用户信息
  try {
    await userStore.fetchCurrentUser()
    userInfo.value = userStore.userInfo
  } catch (error) {
    console.log('获取用户信息失败:', error)
  }

  loadRecentLevels()

  // 注入动画样式
  injectAnimationStyles()

  // 初始化战场动画
  animations = initBattlefieldAnimations()


  // 点击外部关闭下拉菜单
  document.addEventListener('click', handleClickOutside)

})

const handleClickOutside = (event) => {
  const dropdown = document.querySelector('.battlefield-commander')
  if (dropdown && !dropdown.contains(event.target)) {
    closeDropdown()
  }
}

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

/* 战场横幅 */
.battle-banner {
  position: relative;
  width: 100%;
  height: 400px;
  overflow: hidden;
}

.banner-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  filter: sepia(0.3) contrast(1.1);
}

.banner-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(62, 39, 35, 0.7), rgba(139, 69, 19, 0.4));
  display: flex;
  align-items: center;
  justify-content: center;
}

.banner-content {
  text-align: center;
  color: white;
  max-width: 800px;
  padding: 0 20px;
}

.banner-title {
  font-family: 'Noto Serif SC', serif;
  font-size: 3rem;
  font-weight: bold;
  margin-bottom: 16px;
  text-shadow: 3px 3px 6px rgba(0, 0, 0, 0.8);
  letter-spacing: 2px;
}

.banner-subtitle {
  font-family: 'Noto Serif SC', serif;
  font-size: 1.2rem;
  font-style: italic;
  opacity: 0.9;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.6);
  margin-bottom: 32px;
}

/* Banner按钮 */
.banner-battle-button {
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

.banner-battle-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transition: left 0.6s ease;
}

.banner-battle-button:hover::before {
  left: 100%;
}

.banner-battle-button:hover {
  transform: translateY(-4px) scale(1.05);
  box-shadow:
    0 12px 24px rgba(0, 0, 0, 0.6),
    inset 0 1px 0 rgba(255, 255, 255, 0.3);
  border-color: rgba(255, 255, 255, 0.5);
}

.banner-battle-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
  animation: bannerPulse 2s infinite;
}

@keyframes bannerPulse {
  0%, 100% {
    transform: scale(1);
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.4);
  }
  50% {
    transform: scale(1.02);
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.6);
  }
}

/* 战场容器 */
.battlefield-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
  width: 100%;
  box-sizing: border-box;
  overflow-x: hidden;
}

/* 将军欢迎区 */
.general-welcome {
  text-align: center;
  margin-bottom: 40px;
}

.status-report {
  display: flex;
  justify-content: center;
  gap: 40px;
  margin-top: 24px;
}

.status-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.status-label {
  color: var(--text-secondary);
  font-weight: 600;
}

.status-value {
  color: var(--text-primary);
  font-weight: bold;
  font-size: 1.1rem;
}

.status-unit {
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.salary-highlight {
  color: var(--battle-gold);
  font-weight: bold;
  font-size: 1.2rem;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
}

/* 卡片头部 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.card-badge {
  background: var(--battle-gold);
  color: white;
  padding: 4px 12px;
  font-size: 0.8rem;
  font-weight: bold;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.card-description {
  color: var(--text-secondary);
  line-height: 1.6;
  margin-bottom: 24px;
}

/* 战斗记录 */
.battle-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 20px;
}

.battle-record {
  background: var(--bg-card);
  border: 1px solid var(--border-primary);
  border-radius: 4px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
}

.battle-record:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px var(--shadow-light);
  border-color: var(--battle-gold);
}

.record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.record-header h4 {
  color: var(--text-primary);
  font-weight: bold;
  margin: 0;
}

.record-desc {
  color: var(--text-secondary);
  font-size: 0.9rem;
  margin-bottom: 16px;
  line-height: 1.5;
}

.record-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.reward {
  color: var(--battle-gold);
  font-weight: bold;
}

.action {
  color: var(--text-accent);
  font-size: 0.9rem;
  font-weight: 600;
}

/* 复古按钮效果 */
.battle-button {
  width: 100%;
  margin-top: 16px;
}

/* 记录元信息 */
.record-meta {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 4px;
  flex-wrap: wrap;
}

/* 难度标签样式 */
.difficulty-vintage {
  min-width: 80px;
  text-align: center;
  white-space: nowrap;
  display: inline-block;
}

.direction-badge {
  background: linear-gradient(135deg, rgba(139, 69, 19, 0.2) 0%, rgba(218, 165, 32, 0.2) 100%);
  color: var(--battle-gold);
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 600;
  border: 1px solid rgba(218, 165, 32, 0.3);
  white-space: nowrap;
}

.battle-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

/* 加载动画区域样式 */
.banner-loading-section {
  display: flex;
  justify-content: center;
  margin: 30px 0;
}

.battle-button-loading {
  margin-top: 16px;
  display: flex;
  justify-content: center;
}

/* 城池导航栏 */
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

/* 响应式优化下拉菜单 */
@media (max-width: 768px) {
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

/* 未登录状态 */
.battlefield-auth {
  display: flex;
  gap: 12px;
  align-items: center;
}

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
  overflow: hidden;
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

.dropdown-fade-enter-from {
  opacity: 0;
  transform: translateY(-10px);
}

.dropdown-fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* 动画延迟 */
.soldier-enter:nth-child(1) {
  animation-delay: 0.2s;
}

.soldier-enter:nth-child(2) {
  animation-delay: 0.4s;
}

.soldier-enter:nth-child(3) {
  animation-delay: 0.6s;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .battle-banner {
    height: 300px;
  }

  .banner-title {
    font-size: 2rem;
    padding: 0 16px;
  }

  .banner-subtitle {
    font-size: 1rem;
    padding: 0 16px;
  }

  .banner-battle-button {
    padding: 12px 32px;
    font-size: 1rem;
    min-width: 160px;
  }

  .status-report {
    flex-direction: column;
    gap: 16px;
  }

  .battlefield-container {
    padding: 20px 16px;
    max-width: 100%;
  }

  .battle-formation {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .battle-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .battle-card {
    padding: 20px;
    margin: 0;
  }
}

/* 防止在小屏幕上出现横向滚动条 */
@media (max-width: 480px) {
  .battle-banner {
    height: 200px;
  }

  .banner-title {
    font-size: 1.5rem;
  }

  .banner-battle-button {
    padding: 10px 24px;
    font-size: 0.9rem;
    min-width: 140px;
  }

  .battlefield-container {
    padding: 16px 12px;
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

  .user-dropdown {
    padding: 6px 12px;
  }
}

/* 滚动视差效果 */
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

/* 加载动画优化 */
@keyframes battlePulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
}

.battle-button:disabled {
  animation: battlePulse 2s infinite;
}

/* 分页组件样式 */
.battle-pagination {
  margin-top: 24px;
  padding: 20px;
  background: linear-gradient(135deg, rgba(139, 69, 19, 0.05) 0%, rgba(218, 165, 32, 0.05) 100%);
  border: 1px solid var(--primary-brown);
  border-radius: 8px;
  backdrop-filter: blur(5px);
}

.pagination-container {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  margin-bottom: 12px;
}

.pagination-btn {
  padding: 8px 16px;
  background: linear-gradient(135deg, var(--primary-brown) 0%, var(--secondary-brown) 100%);
  border: 1px solid var(--battle-gold);
  border-radius: 6px;
  color: var(--light-sand);
  font-family: 'Noto Serif SC', serif;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.pagination-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, var(--battle-gold) 0%, #ffd700 100%);
  color: var(--primary-brown);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(218, 165, 32, 0.3);
}

.pagination-btn:disabled {
  background: linear-gradient(135deg, var(--text-secondary) 0%, var(--text-muted) 100%);
  border-color: var(--text-muted);
  color: var(--text-muted);
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.pagination-pages {
  display: flex;
  align-items: center;
  gap: 8px;
}

.pagination-page {
  width: 36px;
  height: 36px;
  border: 1px solid var(--primary-brown);
  border-radius: 6px;
  background: var(--light-sand);
  color: var(--primary-brown);
  font-family: 'Noto Serif SC', serif;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.pagination-page:hover:not(:disabled) {
  background: var(--battle-gold);
  color: var(--primary-brown);
  transform: translateY(-2px);
  box-shadow: 0 2px 4px rgba(218, 165, 32, 0.3);
}

.pagination-page.active {
  background: linear-gradient(135deg, var(--battle-gold) 0%, #ffd700 100%);
  border-color: var(--battle-gold);
  color: var(--primary-brown);
  font-weight: bold;
  box-shadow: 0 2px 4px rgba(218, 165, 32, 0.3);
}

.pagination-page:disabled {
  background: var(--text-secondary);
  border-color: var(--text-muted);
  color: var(--text-muted);
  cursor: not-allowed;
}

.pagination-ellipsis {
  color: var(--text-secondary);
  font-family: 'Noto Serif SC', serif;
  font-size: 0.9rem;
  padding: 0 4px;
}

.pagination-info {
  text-align: center;
  color: var(--text-secondary);
  font-family: 'Noto Serif SC', serif;
  font-size: 0.85rem;
  margin-top: 8px;
  opacity: 0.8;
}

/* 响应式分页样式 */
@media (max-width: 768px) {
  .battle-pagination {
    padding: 16px;
    margin-top: 20px;
  }

  .pagination-container {
    flex-direction: column;
    gap: 12px;
  }

  .pagination-pages {
    flex-wrap: wrap;
    justify-content: center;
  }

  .pagination-btn {
    padding: 6px 12px;
    font-size: 0.8rem;
  }

  .pagination-page {
    width: 32px;
    height: 32px;
    font-size: 0.8rem;
  }

  .pagination-info {
    font-size: 0.75rem;
  }
}

@media (max-width: 480px) {
  .pagination-container {
    gap: 8px;
  }

  .pagination-btn {
    padding: 4px 8px;
    font-size: 0.75rem;
  }

  .pagination-page {
    width: 28px;
    height: 28px;
    font-size: 0.75rem;
  }
}
</style>
