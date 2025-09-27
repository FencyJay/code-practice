<template>
  <div class="ranking-container battle-card soldier-enter">
    <div class="ranking-header">
      <div class="ranking-title-section">
        <h3 class="vintage-title">⚔️ 群雄逐鹿 ⚔️</h3>
        <div class="ranking-subtitle">天下英雄，尽在此榜</div>
      </div>
      <div class="ranking-badge">财富榜</div>
    </div>

    <div class="ranking-columns">
      <!-- 第一列 - 天下英豪 -->
      <div class="ranking-column">
        <div class="column-header">
          <span class="column-title">🏛️ 天下英豪</span>
          <span class="column-subtitle">第1-10名</span>
        </div>
        <div
          v-for="user in firstColumn"
          :key="user.userId"
          class="ranking-item"
          :class="{ 'top-three': user.rank <= 3, 'champion': user.rank === 1 }"
        >
          <div class="rank-medal" :class="getMedalClass(user.rank)">
            <span v-if="user.rank === 1" class="crown">👑</span>
            <span v-else>{{ user.rank }}</span>
          </div>
          <div class="user-avatar" :class="{ 'avatar-glow': user.rank <= 3 }">
            <img
              :src="user.avatar"
              :alt="user.nickname"
              class="avatar-image"
            />
          </div>
          <div class="user-info">
            <div class="user-name">
              {{ user.nickname }}
              <span v-if="user.rank === 1" class="champion-title">· 武状元</span>
              <span v-else-if="user.rank === 2" class="noble-title">· 榜眼</span>
              <span v-else-if="user.rank === 3" class="noble-title">· 探花</span>
            </div>
            <div class="user-salary">
              <span class="salary-amount">{{ formatSalary(user.salary) }}</span>
              <span class="salary-unit">文</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 第二列 - 江湖豪杰 -->
      <div class="ranking-column">
        <div class="column-header">
          <span class="column-title">⚔️ 江湖豪杰</span>
          <span class="column-subtitle">第11-20名</span>
        </div>
        <div
          v-for="user in secondColumn"
          :key="user.userId"
          class="ranking-item"
          :class="{ 'top-three': user.rank <= 3 }"
        >
          <div class="rank-medal" :class="getMedalClass(user.rank)">
            {{ user.rank }}
          </div>
          <div class="user-avatar">
            <img
              :src="user.avatar"
              :alt="user.nickname"
              class="avatar-image"
            />
          </div>
          <div class="user-info">
            <div class="user-name">{{ user.nickname }}</div>
            <div class="user-salary">
              <span class="salary-amount">{{ formatSalary(user.salary) }}</span>
              <span class="salary-unit">文</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 第三列 - 后起之秀 -->
      <div class="ranking-column">
        <div class="column-header">
          <span class="column-title">🌟 后起之秀</span>
          <span class="column-subtitle">第21-30名</span>
        </div>
        <div
          v-for="user in thirdColumn"
          :key="user.userId"
          class="ranking-item"
          :class="{ 'top-three': user.rank <= 3 }"
        >
          <div class="rank-medal" :class="getMedalClass(user.rank)">
            {{ user.rank }}
          </div>
          <div class="user-avatar">
            <img
              :src="user.avatar"
              :alt="user.nickname"
              class="avatar-image"
            />
          </div>
          <div class="user-info">
            <div class="user-name">{{ user.nickname }}</div>
            <div class="user-salary">
              <span class="salary-amount">{{ formatSalary(user.salary) }}</span>
              <span class="salary-unit">文</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="ranking-loading">
      <LoadingAnimation text="正在加载排行榜..." />
    </div>

    <!-- 空状态 -->
    <div v-if="!loading && rankingList.length === 0" class="ranking-empty">
      <div class="empty-icon">📊</div>
      <div class="empty-text">暂无排行榜数据</div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getUserRanking } from '@/api/user'
import LoadingAnimation from '@/components/LoadingAnimation.vue'

const loading = ref(false)
const rankingList = ref([])

// 计算三列数据
const firstColumn = computed(() => rankingList.value.slice(0, 10))
const secondColumn = computed(() => rankingList.value.slice(10, 20))
const thirdColumn = computed(() => rankingList.value.slice(20, 30))

// 获取奖牌样式
const getMedalClass = (rank) => {
  if (rank === 1) return 'gold-medal'
  if (rank === 2) return 'silver-medal'
  if (rank === 3) return 'bronze-medal'
  return ''
}

// 格式化薪资显示
const formatSalary = (salary) => {
  if (salary >= 10000) {
    return (salary / 10000).toFixed(1) + '万'
  }
  return salary.toLocaleString()
}

// 加载排行榜数据
const loadRanking = async () => {
  try {
    loading.value = true
    const result = await getUserRanking(30)
    rankingList.value = result || []
  } catch (error) {
    console.error('加载排行榜失败:', error)
    rankingList.value = []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadRanking()
})
</script>

<style scoped>
/* 排行榜容器 */
.ranking-container {
  margin-bottom: 40px;
  position: relative;
  overflow: hidden;
}

.ranking-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 2px;
  background: linear-gradient(90deg, transparent, var(--battle-gold), transparent);
  animation: shimmer 3s infinite;
}

.ranking-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
  padding: 20px 0;
  border-bottom: 2px solid rgba(218, 165, 32, 0.3);
}

.ranking-title-section {
  flex: 1;
}

.ranking-subtitle {
  color: var(--text-secondary);
  font-size: 0.9rem;
  margin-top: 4px;
  font-style: italic;
  opacity: 0.8;
}

.ranking-badge {
  background: linear-gradient(135deg, var(--battle-gold), #ffd700);
  color: var(--primary-brown);
  padding: 6px 16px;
  font-size: 0.85rem;
  font-weight: bold;
  text-transform: uppercase;
  letter-spacing: 2px;
  border-radius: 20px;
  box-shadow: 0 2px 8px rgba(218, 165, 32, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

@keyframes shimmer {
  0% { left: -100%; }
  100% { left: 100%; }
}

/* 三列布局 */
.ranking-columns {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.ranking-column {
  display: flex;
  flex-direction: column;
  gap: 16px;
  background: linear-gradient(135deg, rgba(139, 69, 19, 0.05) 0%, rgba(218, 165, 32, 0.05) 100%);
  border: 1px solid rgba(218, 165, 32, 0.2);
  border-radius: 12px;
  padding: 20px;
  position: relative;
}

.ranking-column::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, var(--battle-gold), var(--sword-silver), var(--battle-gold));
  border-radius: 12px 12px 0 0;
}

.column-header {
  text-align: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid rgba(218, 165, 32, 0.3);
}

.column-title {
  display: block;
  font-size: 1.1rem;
  font-weight: bold;
  color: var(--battle-gold);
  margin-bottom: 4px;
}

.column-subtitle {
  font-size: 0.8rem;
  color: var(--text-secondary);
  opacity: 0.7;
}

/* 排行榜项目 */
.ranking-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px;
  background: var(--bg-card);
  border: 1px solid var(--border-primary);
  border-radius: 10px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.ranking-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(218, 165, 32, 0.1), transparent);
  transition: left 0.6s ease;
}

.ranking-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 24px rgba(218, 165, 32, 0.2);
  border-color: var(--battle-gold);
}

.ranking-item:hover::before {
  left: 100%;
}

.ranking-item.top-three {
  background: linear-gradient(135deg, rgba(255, 215, 0, 0.15), rgba(255, 215, 0, 0.08));
  border: 2px solid var(--battle-gold);
  box-shadow: 0 4px 16px rgba(218, 165, 32, 0.3);
}

.ranking-item.champion {
  background: linear-gradient(135deg, rgba(255, 215, 0, 0.2), rgba(255, 215, 0, 0.1));
  border: 2px solid #ffd700;
  box-shadow: 0 6px 20px rgba(255, 215, 0, 0.4);
  animation: championGlow 2s ease-in-out infinite alternate;
}

@keyframes championGlow {
  0% { box-shadow: 0 6px 20px rgba(255, 215, 0, 0.4); }
  100% { box-shadow: 0 6px 30px rgba(255, 215, 0, 0.6); }
}

/* 奖牌样式 */
.rank-medal {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  font-weight: bold;
  font-size: 0.9rem;
  background: var(--bg-secondary);
  color: var(--text-primary);
  flex-shrink: 0;
  position: relative;
  border: 2px solid var(--border-primary);
}

.rank-medal.gold-medal {
  background: radial-gradient(circle, #ffd700, #ffed4e);
  color: var(--primary-brown);
  box-shadow: 0 4px 12px rgba(255, 215, 0, 0.5);
  border-color: #ffd700;
  animation: medalShine 3s ease-in-out infinite;
}

.rank-medal.silver-medal {
  background: radial-gradient(circle, #c0c0c0, #e8e8e8);
  color: var(--primary-brown);
  box-shadow: 0 4px 8px rgba(192, 192, 192, 0.4);
  border-color: #c0c0c0;
}

.rank-medal.bronze-medal {
  background: radial-gradient(circle, #cd7f32, #e4a460);
  color: white;
  box-shadow: 0 4px 8px rgba(205, 127, 50, 0.4);
  border-color: #cd7f32;
}

.crown {
  font-size: 1.2rem;
  animation: crownFloat 2s ease-in-out infinite;
}

@keyframes medalShine {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}

@keyframes crownFloat {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-2px); }
}

/* 用户头像 */
.user-avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
  border: 2px solid var(--border-primary);
  transition: all 0.3s ease;
}

.avatar-glow {
  border-color: var(--battle-gold);
  box-shadow: 0 0 16px rgba(218, 165, 32, 0.4);
  animation: avatarPulse 2s ease-in-out infinite;
}

.avatar-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 用户信息 */
.user-info {
  flex: 1;
  min-width: 0;
}

.user-name {
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  display: flex;
  align-items: center;
  gap: 4px;
}

.champion-title {
  color: #ffd700;
  font-weight: bold;
  font-size: 0.85rem;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
}

.noble-title {
  color: #c0c0c0;
  font-weight: 600;
  font-size: 0.8rem;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
}

.user-salary {
  display: flex;
  align-items: baseline;
  gap: 2px;
  color: var(--battle-gold);
  font-size: 0.9rem;
  font-weight: 500;
}

.salary-amount {
  font-weight: 700;
  font-size: 1rem;
}

.salary-unit {
  font-size: 0.8rem;
  opacity: 0.8;
}

@keyframes avatarPulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.05); }
}

/* 加载状态 */
.ranking-loading {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px;
}

/* 空状态 */
.ranking-empty {
  text-align: center;
  padding: 40px;
  color: var(--text-secondary);
}

.empty-icon {
  font-size: 3rem;
  margin-bottom: 16px;
  opacity: 0.6;
}

.empty-text {
  font-size: 1rem;
  opacity: 0.8;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .ranking-columns {
    grid-template-columns: repeat(2, 1fr);
  }

  .third-column {
    grid-column: 1 / -1;
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 12px;
  }
}

@media (max-width: 768px) {
  .ranking-columns {
    grid-template-columns: 1fr;
  }

  .ranking-column {
    width: 100%;
  }

  .third-column {
    grid-template-columns: 1fr;
  }

  .ranking-item {
    padding: 10px;
  }

  .rank-medal {
    width: 28px;
    height: 28px;
    font-size: 0.8rem;
  }

  .user-avatar {
    width: 36px;
    height: 36px;
  }
}
</style>