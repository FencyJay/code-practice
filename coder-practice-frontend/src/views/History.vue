<template>
  <div class="history-battlefield">
    <div class="battlefield-content">
      <!-- 返回按钮 -->
      <div class="back-button">
        <el-button @click="router.back()" icon="Back" class="battle-button-secondary">
          🛡️ 返回军营
        </el-button>
      </div>

      <!-- 页面标题 -->
      <div class="battlefield-header">
        <div class="campaign-title">
          <h1 class="vintage-main-title flag-wave">📊 战役档案室</h1>
          <p class="campaign-subtitle">查阅将军的征战记录与军功变化</p>
        </div>
      </div>

      <!-- 军功统计 -->
      <div class="military-stats">
        <div class="stats-grid">
          <div class="battle-stat-card">
            <div class="stat-icon">⚔️</div>
            <div class="stat-number">{{ totalChallenges }}</div>
            <div class="stat-label">征战次数</div>
          </div>
          <div class="battle-stat-card">
            <div class="stat-icon">🎯</div>
            <div class="stat-number">{{ averageScore }}%</div>
            <div class="stat-label">命中率</div>
          </div>
          <div class="battle-stat-card">
            <div class="stat-icon">💰</div>
            <div class="stat-number" :class="totalSalaryChange >= 0 ? 'positive' : 'negative'">
              {{ totalSalaryChange > 0 ? '+' : '' }}{{ totalSalaryChange }}
            </div>
            <div class="stat-label">军饷变化</div>
          </div>
          <div class="battle-stat-card">
            <div class="stat-icon">👑</div>
            <div class="stat-number">{{ currentSalary }}</div>
            <div class="stat-label">当前军饷</div>
          </div>
        </div>
      </div>

      <!-- 筛选和搜索 -->
      <div class="filter-section">
        <div class="battle-filter-card">
          <div class="filter-content">
            <div class="filter-item">
              <label>🎯 命中率范围</label>
              <el-slider
                v-model="scoreRange"
                :min="0"
                :max="100"
                range
                show-stops
                @change="handleFilterChange"
                class="battle-slider"
              />
              <div class="range-labels">
                <span>{{ scoreRange[0] }}%</span>
                <span>{{ scoreRange[1] }}%</span>
              </div>
            </div>

            <div class="filter-item">
              <label>📅 征战时间</label>
              <el-date-picker
                v-model="dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                @change="handleFilterChange"
                class="battle-date-picker"
              />
            </div>

            <div class="filter-actions">
              <el-button @click="resetFilters" class="battle-button-secondary">🔄 重置</el-button>
              <el-button @click="loadHistory" class="battle-button">🔍 查询档案</el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 征战记录列表 -->
      <div class="history-section">
        <div class="battle-history-card">
          <div class="battle-history-header">
            <h3>📜 征战记录</h3>
            <div class="history-actions">
              <el-button @click="exportHistory" icon="Download" class="battle-button-secondary">
                📄 导出兵册
              </el-button>
            </div>
          </div>

          <div v-if="historyRecords.length > 0">
            <div class="battle-history-list">
              <div
                v-for="record in historyRecords"
                :key="record.id"
                class="battle-history-item"
                @click="viewResult(record)"
              >
                <div class="battle-item-header">
                  <div class="campaign-info">
                    <h4>{{ record.levelName || `关卡 #${record.levelId}` }}</h4>
                    <div class="campaign-meta">
                      <span v-if="record.difficulty" class="battle-difficulty" :class="record.difficulty">
                        {{ record.difficulty }}
                      </span>
                      <span v-if="record.direction" class="direction-badge">
                        {{ getDirectionIcon(record.direction) }} {{ record.direction }}
                      </span>
                      <span class="battle-date">{{ formatDate(record.createTime) }}</span>
                    </div>
                  </div>
                  <div class="battle-results">
                    <div class="battle-score" :class="getScoreClass(record.score || 0)">
                      {{ record.score || 0 }}%
                    </div>
                    <div class="battle-salary" :class="getSalaryChangeClass(record.salaryChange || 0)">
                      {{ record.salaryChange > 0 ? '+' : '' }}{{ record.salaryChange || 0 }}军饷
                    </div>
                  </div>
                </div>

                <div class="battle-item-content">
                  <div class="battle-comment">
                    <el-text>📝 {{ record.comment || '暂无评价' }}</el-text>
                  </div>
                  <div class="strategy-count">
                    采用了 {{ getSelectedOptionsCount(record.userOptions) }} 种策略
                  </div>
                </div>

                <div class="battle-item-footer">
                  <div class="view-icon">→</div>
                </div>
              </div>
            </div>

            <!-- 分页 -->
            <div class="battle-pagination">
              <el-pagination
                v-model:current-page="currentPage"
                v-model:page-size="pageSize"
                :page-sizes="[10, 20, 50, 100]"
                :total="total"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                class="battle-pagination-component"
              />
            </div>
          </div>

          <!-- 空状态 -->
          <div v-else class="battle-empty-state">
            <div class="empty-message">
              <div class="empty-icon">🏰</div>
              <h3>暂无征战记录</h3>
              <p>将军尚未开始征战沙场</p>
              <el-button @click="router.push('/home')" class="battle-button">
                ⚔️ 开始征战
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 军饷变化图表 -->
      <div v-if="historyRecords.length > 0" class="chart-section">
        <div class="battle-chart-card">
          <h3>💰 军饷变化趋势</h3>
          <div class="chart-placeholder">
            <p>📈 军饷变化图表将在这里显示</p>
            <el-text type="info" size="small"> 显示从初始军饷到当前军饷的变化趋势 </el-text>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { getUserLevelHistory } from '@/api/userLevel'
import { ElMessage } from 'element-plus'
import { Back, ArrowRight, Download } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

// 数据状态
const historyRecords = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

// 筛选状态
const scoreRange = ref([0, 100])
const dateRange = ref([])

// 计算属性
const totalChallenges = computed(() => total.value)
const averageScore = computed(() => {
  if (historyRecords.value.length === 0) return 0
  const totalScore = historyRecords.value.reduce((sum, record) => sum + record.score, 0)
  return Math.round(totalScore / historyRecords.value.length)
})

const totalSalaryChange = computed(() => {
  if (historyRecords.value.length === 0) return 0
  return historyRecords.value.reduce((sum, record) => sum + record.salaryChange, 0)
})

const currentSalary = computed(() => userStore.userInfo?.salary || 0)

// 加载历史记录
const loadHistory = async () => {
  try {
    const params = {
      current: currentPage.value,
      pageSize: pageSize.value,
    }

    if (dateRange.value && dateRange.value.length === 2) {
      params.startTime = dateRange.value[0]
      params.endTime = dateRange.value[1]
    }

    const result = await getUserLevelHistory(params)
    historyRecords.value = result.records || []
    total.value = result.total || 0
  } catch (error) {
    ElMessage.error('加载历史记录失败，请登录')
  }
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '未知时间'
  return new Date(dateString).toLocaleDateString('zh-CN')
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

// 获取分数样式类
const getScoreClass = (score) => {
  if (score >= 80) return 'excellent'
  if (score >= 60) return 'good'
  return 'poor'
}

// 获取薪资变化样式类
const getSalaryChangeClass = (change) => {
  return change > 0 ? 'positive' : 'negative'
}

// 获取用户选择的选项数量
const getSelectedOptionsCount = (userOptions) => {
  if (!userOptions) return 0
  try {
    const options = JSON.parse(userOptions)
    return Array.isArray(options) ? options.length : 0
  } catch {
    return 0
  }
}

// 截断文本
const truncateText = (text, length) => {
  if (!text) return ''
  return text.length > length ? text.substring(0, length) + '...' : text
}

// 查看结果详情
const viewResult = (record) => {
  // 直接跳转到结果页面，由后端 API 获取数据
  router.push(`/result/${record.id}`)
}

// 处理筛选变化
const handleFilterChange = () => {
  currentPage.value = 1
  loadHistory()
}

// 重置筛选
const resetFilters = () => {
  scoreRange.value = [0, 100]
  dateRange.value = []
  currentPage.value = 1
  loadHistory()
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  loadHistory()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadHistory()
}

// 导出征兵记录
const exportHistory = () => {
  ElMessage.info('兵册导出功能开发中...')
}

onMounted(() => {
  loadHistory()
})
</script>

<style scoped>
/* 引入战场主题样式 */
@import '@/styles/battlefield-theme.css';

.history-battlefield {
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

.battlefield-header {
  text-align: center;
  margin-bottom: 50px;
  position: relative;
}

.campaign-title h1 {
  font-family: 'Noto Serif SC', serif;
  font-size: 2.5rem;
  font-weight: bold;
  color: var(--battle-gold);
  margin-bottom: 16px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
  letter-spacing: 2px;
}

.campaign-subtitle {
  font-family: 'Noto Serif SC', serif;
  font-size: 1.2rem;
  color: var(--text-secondary);
  font-style: italic;
}

.military-stats {
  margin-bottom: 40px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 24px;
}

.battle-stat-card {
  background: var(--bg-card);
  border: 2px solid var(--border-primary);
  border-radius: 0;
  padding: 30px 20px;
  text-align: center;
  box-shadow: 0 8px 32px var(--shadow-heavy);
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
}

.battle-stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, var(--battle-gold), var(--sword-silver), var(--battle-gold));
}

.battle-stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 40px var(--shadow-heavy);
}

.stat-icon {
  font-size: 2rem;
  margin-bottom: 12px;
  opacity: 0.8;
}

.stat-number {
  font-size: 2.5rem;
  font-weight: bold;
  margin-bottom: 8px;
  font-family: 'Noto Serif SC', serif;
}

.stat-number.positive {
  color: var(--success-green);
}

.stat-number.negative {
  color: var(--danger-red);
}

.stat-label {
  color: var(--text-secondary);
  font-size: 1rem;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.filter-section {
  margin-bottom: 40px;
}

.battle-filter-card {
  background: var(--bg-card);
  border: 2px solid var(--border-primary);
  border-radius: 0;
  padding: 30px;
  box-shadow: 0 8px 32px var(--shadow-heavy);
}

.filter-content {
  display: grid;
  grid-template-columns: 1fr 1fr auto;
  gap: 24px;
  align-items: end;
}

.filter-item label {
  display: block;
  margin-bottom: 12px;
  color: var(--text-primary);
  font-weight: 600;
  font-family: 'Noto Serif SC', serif;
}

.range-labels {
  display: flex;
  justify-content: space-between;
  margin-top: 8px;
  font-size: 14px;
  color: var(--text-secondary);
  font-weight: 500;
}

.filter-actions {
  display: flex;
  gap: 12px;
}

.battle-slider :deep(.el-slider__runway) {
  background: var(--border-secondary);
}

.battle-slider :deep(.el-slider__bar) {
  background: linear-gradient(90deg, var(--primary-brown), var(--battle-gold));
}

.battle-slider :deep(.el-slider__button) {
  background: var(--battle-gold);
  border: 2px solid var(--sword-silver);
}

.battle-date-picker :deep(.el-input__wrapper) {
  background: var(--bg-primary);
  border: 1px solid var(--border-primary);
  border-radius: 0;
}

.battle-date-picker :deep(.el-input__inner) {
  color: var(--text-primary);
}

.battle-history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.battle-history-header h3 {
  font-family: 'Noto Serif SC', serif;
  font-size: 1.5rem;
  color: var(--text-primary);
  margin: 0;
}

.battle-history-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.battle-history-item {
  background: var(--bg-card);
  border: 2px solid var(--border-primary);
  border-radius: 0;
  padding: 24px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.battle-history-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, transparent, var(--battle-gold), transparent);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.battle-history-item:hover {
  border-color: var(--battle-gold);
  transform: translateY(-4px);
  box-shadow: 0 12px 40px var(--shadow-heavy);
}

.battle-history-item:hover::before {
  opacity: 1;
}

.battle-item-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}

.campaign-info h4 {
  font-family: 'Noto Serif SC', serif;
  color: var(--text-primary);
  margin-bottom: 8px;
  font-size: 1.2rem;
  font-weight: bold;
}

.campaign-meta {
  display: flex;
  gap: 16px;
  align-items: center;
  flex-wrap: wrap;
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

.battle-difficulty {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  border: 1px solid;
  text-transform: none;
  letter-spacing: 0.5px;
}

.battle-difficulty.简单 {
  background: rgba(76, 175, 80, 0.1);
  color: #4caf50;
  border-color: #4caf50;
}

.battle-difficulty.中等 {
  background: rgba(255, 152, 0, 0.1);
  color: #ff9800;
  border-color: #ff9800;
}

.battle-difficulty.困难 {
  background: rgba(244, 67, 54, 0.1);
  color: #f44336;
  border-color: #f44336;
}

.battle-date {
  color: var(--text-secondary);
  font-size: 14px;
  font-weight: 500;
}

.battle-results {
  text-align: right;
}

.battle-score {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 8px;
  font-family: 'Noto Serif SC', serif;
}

.battle-score.excellent {
  color: var(--success-green);
}

.battle-score.good {
  color: var(--battle-gold);
}

.battle-score.poor {
  color: var(--danger-red);
}

.battle-salary {
  font-size: 1.1rem;
  font-weight: 600;
}

.battle-salary.positive {
  color: var(--success-green);
}

.battle-salary.negative {
  color: var(--danger-red);
}

.battle-item-content {
  margin-bottom: 16px;
  padding: 16px;
  background: var(--bg-primary);
  border: 1px solid var(--border-secondary);
  border-radius: 4px;
}

.battle-comment {
  color: var(--text-secondary);
  margin-bottom: 8px;
  font-size: 14px;
  line-height: 1.5;
}

.strategy-count {
  color: var(--text-secondary);
  font-size: 13px;
  font-weight: 500;
  opacity: 0.8;
}

.battle-item-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.battle-tag {
  background: rgba(218, 165, 32, 0.1);
  border: 1px solid var(--battle-gold);
  color: var(--battle-gold);
  border-radius: 0;
}

.view-icon {
  color: var(--battle-gold);
  font-size: 1.2rem;
  font-weight: bold;
  transition: transform 0.3s ease;
}

.battle-history-item:hover .view-icon {
  transform: translateX(8px);
}

.battle-pagination {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}

.battle-pagination-component :deep(.el-pagination) {
  background: var(--bg-card);
  border: 2px solid var(--border-primary);
  border-radius: 0;
  padding: 16px;
}

.battle-empty-state {
  text-align: center;
  padding: 80px 20px;
  background: var(--bg-card);
  border: 2px solid var(--border-primary);
  border-radius: 0;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 20px;
  opacity: 0.6;
}

.empty-message h3 {
  font-family: 'Noto Serif SC', serif;
  color: var(--text-primary);
  margin-bottom: 12px;
  font-size: 1.5rem;
}

.empty-message p {
  color: var(--text-secondary);
  margin-bottom: 30px;
}

.chart-section {
  margin-top: 40px;
}

.battle-chart-card {
  background: var(--bg-card);
  border: 2px solid var(--border-primary);
  border-radius: 0;
  padding: 30px;
  box-shadow: 0 8px 32px var(--shadow-heavy);
}

.battle-chart-card h3 {
  font-family: 'Noto Serif SC', serif;
  color: var(--text-primary);
  margin-bottom: 24px;
  font-size: 1.3rem;
}

.chart-placeholder {
  text-align: center;
  padding: 40px;
  background: var(--bg-primary);
  border: 2px dashed var(--border-secondary);
  border-radius: 0;
}

.chart-placeholder p {
  margin-bottom: 12px;
  color: var(--text-secondary);
  font-family: 'Noto Serif SC', serif;
  font-size: 1.1rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .battlefield-content {
    padding: 20px;
  }

  .campaign-title h1 {
    font-size: 2rem;
  }

  .stats-grid {
    grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
    gap: 16px;
  }

  .filter-content {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .battle-item-header {
    flex-direction: column;
    gap: 16px;
  }

  .battle-results {
    text-align: left;
  }

  .battle-item-content {
    flex-direction: column;
    gap: 12px;
    text-align: center;
  }

  .battle-item-footer {
    flex-direction: column;
    gap: 12px;
  }
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.battle-stat-card {
  animation: fadeInUp 0.6s ease-out;
}

.battle-history-item {
  animation: fadeInUp 0.6s ease-out;
}

.battle-filter-card {
  animation: fadeInUp 0.6s ease-out 0.2s both;
}

.battle-chart-card {
  animation: fadeInUp 0.6s ease-out 0.4s both;
}
</style>
