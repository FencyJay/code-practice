<template>
  <div class="battlefield-result">
    <div class="battlefield-content">
      <!-- 返回按钮 -->
      <div class="back-button">
        <el-button @click="router.back()" icon="Back" class="battle-button-secondary">
          🛡️ 返回军营
        </el-button>
      </div>

      <!-- 战报内容 -->
      <div v-if="loading" class="battle-loading">
        <div class="loading-animation">
          <div class="loading-icon">📜</div>
          <p>正在整理战报...</p>
        </div>
      </div>

      <div v-else-if="result" class="battle-result-container">
        <!-- 战果展示 -->
        <div class="battle-result-card battle-score-card">
          <div class="battle-result-score" :class="getScoreClass(result.score)">{{ result.score }}</div>
          <div class="battle-score-label">征战得分</div>
          <div v-if="result.direction" class="battle-direction-badge">
            {{ getDirectionIcon(result.direction) }} {{ result.direction }}
          </div>
          <div class="battle-decoration">⚔️</div>
        </div>

        <!-- 将军评价和军饷变化 -->
        <div class="battle-result-card battle-feedback-card">
          <div class="battle-feedback-content">
            <div class="battle-comment">
              <h3>🎭 将军评价</h3>
              <p class="battle-comment-text">{{ result.comment }}</p>
            </div>

            <div class="battle-salary-change" :class="getSalaryChangeClass(result.salaryChange)">
              {{ result.salaryChange > 0 ? '+' : '' }}{{ result.salaryChange }} 军饷
            </div>
            <div class="battle-salary-label">军饷变化</div>

            <div class="battle-current-salary">
              当前军饷：<span class="battle-salary-amount">{{ userStore.userInfo?.salary }}</span> 军饷/月
            </div>
          </div>
        </div>

        <!-- 军团建议 -->
        <div v-if="result.suggest" class="battle-result-card battle-suggest-card">
          <h3>🏰 军团建议</h3>
          <p class="battle-suggest-text">{{ result.suggest }}</p>
        </div>

        <!-- 评分原因 -->
        <div v-if="result.reason" class="battle-result-card battle-reason-card">
          <h3>📊 战果分析</h3>
          <div class="battle-reason-text">{{ result.reason }}</div>
        </div>

        <!-- 正确策略 -->
        <div v-if="result.trueOptions" class="battle-result-card battle-correct-strategy-card">
          <h3>✅ 正确策略</h3>
          <div class="battle-correct-strategies">
            <el-tag
              v-for="option in correctOptionsArray"
              :key="option"
              class="battle-correct-tag"
            >
              <span class="strategy-icon">🗡️</span>
              {{ option }}
            </el-tag>
          </div>
        </div>

        <!-- 标准答案解析 -->
        <div v-if="result.standardAnswer" class="battle-result-card battle-standard-answer-card">
          <h3>📜 兵法解析</h3>
          <div class="battle-standard-answer-content">
            <SimpleMermaidViewer :content="result.standardAnswer" />
          </div>
        </div>

        <!-- 将军选择 -->
        <div v-if="result.userOptions" class="battle-result-card battle-user-strategies-card">
          <h3>👤 将军选择</h3>
          <div class="battle-user-strategies">
            <el-tag
              v-for="option in userOptionsArray"
              :key="option"
              :class="isCorrectOption(option) ? 'battle-correct-strategy-tag' : 'battle-incorrect-strategy-tag'"
              class="battle-user-strategy-tag"
            >
              <span class="strategy-icon">⚔️</span>
              {{ option }}
              <el-icon v-if="isCorrectOption(option)" class="battle-correct-icon"><Check /></el-icon>
              <el-icon v-else class="battle-incorrect-icon"><Close /></el-icon>
            </el-tag>
          </div>
        </div>

        <!-- 推荐题目 -->
        <div v-if="recommendedQuestionsArray.length > 0" class="battle-result-card battle-recommended-questions-card">
          <h3>📚 智能推荐相关面试题</h3>
          <div class="battle-recommended-questions">
            <div
              v-for="question in recommendedQuestionsArray"
              :key="question.title"
              class="battle-question-item"
              @click="openQuestionLink(question.url)"
            >
              <div class="battle-question-header">
                <span class="battle-question-title">{{ question.title }}</span>
                <el-tag
                  :type="getDifficultyType(question.difficulty)"
                  size="small"
                  class="battle-question-difficulty"
                >
                  {{ question.difficulty }}
                </el-tag>
              </div>
              <div class="battle-question-footer">
                <span v-if="question.company" class="battle-question-company">
                  🏢 {{ question.company }}
                </span>
                <span v-if="question.tags" class="battle-question-tags">
                  🏷️ {{ question.tags }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- 下一步行动 -->
        <div class="battle-actions-card">
          <el-button @click="goToHome" icon="House" class="battle-button">
            🏰 返回大本营
          </el-button>
          <el-button @click="goToHistory" icon="Document" class="battle-button-secondary">
            📜 查看战史
          </el-button>
          <el-button @click="startNewChallenge" class="battle-button">
            ⚔️ 再次出征
          </el-button>
        </div>
      </div>

      <!-- 数据不存在或已过期 -->
      <div v-else class="battle-empty-state">
        <div class="empty-content">
          <div class="empty-icon">🏰</div>
          <h3>战报数据不存在</h3>
          <p>可能是数据已过期或链接错误</p>
          <div class="empty-actions">
            <el-button @click="router.push('/history')" class="battle-button">
              📜 返回历史记录
            </el-button>
            <el-button @click="router.push('/home')" class="battle-button-secondary">
              🏰 返回大本营
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { getUserLevelResult } from '@/api/userLevel'
import { ElMessage } from 'element-plus'
import SimpleMermaidViewer from '@/components/SimpleMermaidViewer.vue'
import { Check, Close, Back, House, Document, Plus } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const result = ref(null)
const loading = ref(true)

// 计算属性
const correctOptionsArray = computed(() => {
  if (!result.value?.trueOptions) return []
  try {
    return JSON.parse(result.value.trueOptions)
  } catch {
    return []
  }
})

const userOptionsArray = computed(() => {
  if (!result.value?.userOptions) return []
  try {
    return JSON.parse(result.value.userOptions)
  } catch {
    return []
  }
})

const recommendedQuestionsArray = computed(() => {
  if (!result.value?.recommendedQuestions) return []
  try {
    return JSON.parse(result.value.recommendedQuestions)
  } catch {
    return []
  }
})

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

// 判断选项是否正确
const isCorrectOption = (option) => {
  return correctOptionsArray.value.includes(option)
}

// 获取难度标签类型
const getDifficultyType = (difficulty) => {
  const typeMap = {
    '简单': 'success',
    '中等': 'warning',
    '困难': 'danger'
  }
  return typeMap[difficulty] || 'info'
}

// 打开题目链接
const openQuestionLink = (url) => {
  if (url) {
    window.open(url, '_blank')
  }
}

// 加载结果数据
const loadResult = async () => {
  loading.value = true
  try {
    const resultId = route.params.id

    // 直接从后端 API 获取数据
    const data = await getUserLevelResult(resultId)
    result.value = data

  } catch (error) {
    console.error('加载结果数据失败:', error)

    if (error.response?.status === 404) {
      ElMessage.warning('战报数据不存在或已删除')
    } else if (error.response?.status === 403) {
      ElMessage.warning('无权访问此战报数据')
    } else {
      ElMessage.error('加载战报数据失败')
    }

    // 延迟重定向到历史页面
    setTimeout(() => {
      router.push('/history')
    }, 2000)
  } finally {
    loading.value = false
  }
}

// 导航函数
const goToHome = () => {
  router.push('/home')
}

const goToHistory = () => {
  router.push('/history')
}

const startNewChallenge = () => {
  if (result.value?.levelId) {
    // 跳转到当前关卡重新答题
    router.push(`/level/${result.value.levelId}`)
  } else {
    // 如果没有关卡ID，回退到首页
    router.push('/home')
  }
}


onMounted(() => {
  loadResult()
})
</script>

<style scoped>
/* 引入战场主题样式 */
@import '@/styles/battlefield-theme.css';

.battlefield-result {
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

.battle-result-container {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.battle-result-card {
  background: var(--bg-card);
  border: 2px solid var(--border-primary);
  border-radius: 0;
  padding: 40px;
  box-shadow: 0 8px 32px var(--shadow-heavy);
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
}

.battle-result-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, var(--battle-gold), var(--sword-silver), var(--battle-gold));
}

.battle-result-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 40px var(--shadow-heavy);
}

/* 战果卡片 */
.battle-score-card {
  text-align: center;
  background: linear-gradient(135deg, var(--deep-brown) 0%, var(--primary-brown) 100%);
  color: white;
  position: relative;
}

.battle-score-card::before {
  background: linear-gradient(90deg, var(--battle-gold), var(--light-brown), var(--battle-gold));
}

.battle-result-score {
  font-size: 4rem;
  font-weight: bold;
  margin-bottom: 16px;
  font-family: 'Noto Serif SC', serif;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
  position: relative;
  z-index: 1;
}

.battle-result-score.excellent {
  color: var(--success-green);
}

.battle-result-score.good {
  color: var(--battle-gold);
}

.battle-result-score.poor {
  color: var(--danger-red);
}

.battle-score-label {
  font-size: 1.5rem;
  opacity: 0.9;
  font-family: 'Noto Serif SC', serif;
  font-weight: 500;
  position: relative;
  z-index: 1;
}

.battle-direction-badge {
  position: absolute;
  top: 20px;
  left: 20px;
  background: linear-gradient(135deg, rgba(139, 69, 19, 0.2) 0%, rgba(218, 165, 32, 0.2) 100%);
  color: var(--battle-gold);
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 600;
  border: 1px solid rgba(218, 165, 32, 0.3);
  white-space: nowrap;
  backdrop-filter: blur(5px);
}

.battle-decoration {
  position: absolute;
  top: 20px;
  right: 20px;
  font-size: 3rem;
  opacity: 0.3;
  transform: rotate(15deg);
}

/* 反馈卡片 */
.battle-feedback-card {
  text-align: center;
}

.battle-feedback-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.battle-comment h3 {
  font-family: 'Noto Serif SC', serif;
  color: var(--text-primary);
  margin-bottom: 16px;
  font-size: 1.3rem;
  font-weight: bold;
}

.battle-comment-text {
  font-size: 1.1rem;
  color: var(--text-secondary);
  line-height: 1.8;
  font-family: 'Noto Serif SC', serif;
  max-width: 600px;
}

.battle-salary-change {
  font-size: 2.5rem;
  font-weight: bold;
  font-family: 'Noto Serif SC', serif;
}

.battle-salary-change.positive {
  color: var(--success-green);
}

.battle-salary-change.negative {
  color: var(--danger-red);
}

.battle-salary-label {
  color: var(--text-secondary);
  font-size: 1rem;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.battle-current-salary {
  font-size: 1.1rem;
  color: var(--text-secondary);
  font-family: 'Noto Serif SC', serif;
}

.battle-salary-amount {
  font-weight: bold;
  color: var(--battle-gold);
}

/* 建议卡片 */
.battle-suggest-card h3,
.battle-reason-card h3,
.battle-correct-strategy-card h3,
.battle-standard-answer-card h3,
.battle-user-strategies-card h3,
.battle-recommended-questions-card h3 {
  font-family: 'Noto Serif SC', serif;
  color: var(--text-primary);
  margin-bottom: 20px;
  font-size: 1.3rem;
  font-weight: bold;
}

.battle-suggest-text,
.battle-reason-text {
  line-height: 1.8;
  color: var(--text-secondary);
  font-family: 'Noto Serif SC', serif;
  font-size: 1.1rem;
}

.battle-correct-strategies {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.battle-correct-tag {
  font-size: 16px;
  padding: 12px 16px;
  background: var(--success-green);
  color: white;
  border: 2px solid var(--success-green);
  border-radius: 0;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
}

.strategy-icon {
  font-size: 1.2rem;
}

.battle-user-strategies {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.battle-user-strategy-tag {
  font-size: 16px;
  padding: 12px 16px;
  display: flex;
  align-items: center;
  gap: 8px;
  border-radius: 0;
  font-weight: 500;
  border: 2px solid;
}

.battle-correct-strategy-tag {
  background: var(--success-green);
  color: white;
  border-color: var(--success-green);
}

.battle-incorrect-strategy-tag {
  background: var(--danger-red);
  color: white;
  border-color: var(--danger-red);
}

.battle-correct-icon,
.battle-incorrect-icon {
  color: white;
  font-size: 1.2rem;
}

.battle-standard-answer-content {
  background: var(--bg-primary);
  border: 2px solid var(--border-secondary);
  border-radius: 0;
  padding: 30px;
  border-left: 4px solid var(--battle-gold);
  position: relative;
}

.battle-standard-answer-content::before {
  content: '📜';
  position: absolute;
  top: -12px;
  left: 20px;
  background: var(--bg-primary);
  padding: 0 10px;
  font-size: 1.5rem;
}

/* SimpleMermaidViewer 组件样式覆盖 */
.battle-standard-answer-content :deep(.mermaid-viewer) {
  font-family: 'Noto Serif SC', serif;
  line-height: 1.8;
  color: var(--text-secondary);
  font-size: 1rem;
}

.battle-standard-answer-content :deep(.markdown-content h1),
.battle-standard-answer-content :deep(.markdown-content h2),
.battle-standard-answer-content :deep(.markdown-content h3),
.battle-standard-answer-content :deep(.markdown-content h4),
.battle-standard-answer-content :deep(.markdown-content h5),
.battle-standard-answer-content :deep(.markdown-content h6) {
  color: var(--text-primary);
  margin: 24px 0 16px 0;
  font-weight: 600;
}

.battle-standard-answer-content :deep(.markdown-content h1) {
  font-size: 1.8rem;
  border-bottom: 2px solid var(--border-secondary);
  padding-bottom: 8px;
}

.battle-standard-answer-content :deep(.markdown-content h2) {
  font-size: 1.5rem;
  border-bottom: 1px solid var(--border-secondary);
  padding-bottom: 6px;
}

.battle-standard-answer-content :deep(.markdown-content h3) {
  font-size: 1.3rem;
}

.battle-standard-answer-content :deep(.markdown-content p) {
  margin: 16px 0;
}

.battle-standard-answer-content :deep(.markdown-content ul),
.battle-standard-answer-content :deep(.markdown-content ol) {
  margin: 16px 0;
  padding-left: 32px;
}

.battle-standard-answer-content :deep(.markdown-content li) {
  margin: 8px 0;
}

.battle-standard-answer-content :deep(.markdown-content blockquote) {
  border-left: 4px solid var(--battle-gold);
  margin: 16px 0;
  padding: 8px 16px;
  background: var(--bg-primary);
  font-style: italic;
}

.battle-standard-answer-content :deep(.markdown-content code) {
  background: var(--bg-primary);
  padding: 2px 6px;
  border-radius: 4px;
  font-family: 'Consolas', 'Monaco', monospace;
  font-size: 0.9rem;
  color: var(--battle-gold);
}

.battle-standard-answer-content :deep(.markdown-content pre) {
  background: var(--bg-primary);
  border: 1px solid var(--border-secondary);
  border-radius: 4px;
  padding: 16px;
  overflow-x: auto;
  margin: 16px 0;
}

.battle-standard-answer-content :deep(.markdown-content pre code) {
  background: none;
  padding: 0;
  color: var(--text-secondary);
}

.battle-standard-answer-content :deep(.markdown-content table) {
  border-collapse: collapse;
  width: 100%;
  margin: 16px 0;
}

.battle-standard-answer-content :deep(.markdown-content th),
.battle-standard-answer-content :deep(.markdown-content td) {
  border: 1px solid var(--border-secondary);
  padding: 8px 12px;
  text-align: left;
}

.battle-standard-answer-content :deep(.markdown-content th) {
  background: var(--bg-primary);
  font-weight: 600;
  color: var(--text-primary);
}

.battle-standard-answer-content :deep(.markdown-content a) {
  color: var(--battle-gold);
  text-decoration: none;
}

.battle-standard-answer-content :deep(.markdown-content a:hover) {
  text-decoration: underline;
}

/* 推荐题目卡片 */
.battle-recommended-questions {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.battle-question-item {
  background: var(--bg-primary);
  border: 1px solid var(--border-secondary);
  border-radius: 8px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.battle-question-item::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background: var(--battle-gold);
  transform: scaleY(0);
  transition: transform 0.3s ease;
}

.battle-question-item:hover {
  border-color: var(--battle-gold);
  transform: translateX(4px);
  box-shadow: 0 4px 12px var(--shadow-light);
}

.battle-question-item:hover::before {
  transform: scaleY(1);
}

.battle-question-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
  gap: 12px;
}

.battle-question-title {
  flex: 1;
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--text-primary);
  font-family: 'Noto Serif SC', serif;
  line-height: 1.4;
}

.battle-question-difficulty {
  flex-shrink: 0;
  font-weight: 500;
}

.battle-question-footer {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.battle-question-company,
.battle-question-tags {
  font-size: 0.9rem;
  color: var(--text-secondary);
  font-family: 'Noto Serif SC', serif;
}

.battle-actions-card {
  display: flex;
  justify-content: center;
  gap: 16px;
  padding: 40px;
  background: var(--bg-card);
  border: 2px solid var(--border-primary);
  border-radius: 0;
  box-shadow: 0 8px 32px var(--shadow-heavy);
  flex-wrap: wrap;
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

.battle-empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 500px;
  text-align: center;
  background: var(--bg-card);
  border: 2px solid var(--border-primary);
  border-radius: 0;
  margin: 40px 20px;
  padding: 60px 40px;
}

.empty-content {
  max-width: 400px;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 24px;
  opacity: 0.7;
}

.empty-content h3 {
  font-family: 'Noto Serif SC', serif;
  color: var(--text-primary);
  margin-bottom: 16px;
  font-size: 1.8rem;
  font-weight: bold;
}

.empty-content p {
  color: var(--text-secondary);
  margin-bottom: 32px;
  font-size: 1.1rem;
  line-height: 1.6;
}

.empty-actions {
  display: flex;
  flex-direction: column;
  gap: 16px;
  align-items: center;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .battlefield-content {
    padding: 20px;
  }

  .battle-result-card {
    padding: 24px;
  }

  .battle-result-score {
    font-size: 3rem;
  }

  .battle-score-label {
    font-size: 1.2rem;
  }

  .battle-actions-card {
    flex-direction: column;
    gap: 12px;
    padding: 24px;
  }

  .battle-correct-strategies,
  .battle-user-strategies {
    gap: 8px;
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

.battle-result-card {
  animation: fadeInUp 0.8s ease-out;
}

.battle-result-card:nth-child(1) { animation-delay: 0.1s; }
.battle-result-card:nth-child(2) { animation-delay: 0.2s; }
.battle-result-card:nth-child(3) { animation-delay: 0.3s; }
.battle-result-card:nth-child(4) { animation-delay: 0.4s; }
.battle-result-card:nth-child(5) { animation-delay: 0.5s; }
.battle-result-card:nth-child(6) { animation-delay: 0.6s; }
.battle-result-card:nth-child(7) { animation-delay: 0.7s; }
.battle-result-card:nth-child(8) { animation-delay: 0.8s; }
</style>
