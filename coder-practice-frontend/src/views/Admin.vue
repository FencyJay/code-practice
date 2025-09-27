<template>
  <div class="admin-container">
    <!-- 管理员控制台 -->
    <div class="admin-header">
      <h1>🎮 管理员控制台</h1>
      <p>管理关卡设置和用户权限</p>
    </div>

    <!-- 操作栏 -->
    <div class="action-bar">
      <el-button
        type="primary"
        @click="showAddDialog"
        :icon="Plus"
      >
        添加关卡
      </el-button>
      <el-button
        @click="fetchLevels"
        :icon="Refresh"
      >
        刷新列表
      </el-button>
    </div>

    <!-- 搜索筛选区域 -->
    <div class="search-section">
      <el-form :model="queryParams" label-width="80px" class="search-form">
        <el-row :gutter="20" align="middle">
          <el-col :span="8">
            <el-form-item label="关卡名称">
              <el-input
                v-model="queryParams.levelName"
                placeholder="请输入关卡名称"
                clearable
                @keyup.enter="fetchLevels"
              />
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="难度">
              <el-select
                v-model="queryParams.difficulty"
                placeholder="请选择难度"
                clearable
                style="width: 100%"
              >
                <el-option label="初级" value="初级" />
                <el-option label="初级+" value="初级+" />
                <el-option label="中级" value="中级" />
                <el-option label="高级" value="高级" />
                <el-option label="专家级" value="专家级" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="精选状态">
              <el-select
                v-model="queryParams.isFeatured"
                placeholder="请选择精选状态"
                clearable
                style="width: 100%"
              >
                <el-option label="精选" :value="true" />
                <el-option label="普通" :value="false" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="0" class="button-group">
              <el-button
                type="primary"
                @click="fetchLevels"
                :icon="Refresh"
              >
                搜索
              </el-button>
              <el-button
                @click="resetQuery"
              >
                重置
              </el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>

    <!-- 关卡列表 -->
    <div class="level-list">
      <el-table
        :data="levels"
        v-loading="loading"
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="levelName" label="关卡名称" width="200" />
        <el-table-column prop="difficulty" label="难度" width="100">
          <template #default="scope">
            <el-tag
              :type="getDifficultyType(scope.row.difficulty)"
            >
              {{ scope.row.difficulty }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="targetSalary" label="目标薪资" width="120">
          <template #default="scope">
            ¥{{ scope.row.targetSalary.toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="100">
          <template #default="scope">
            <el-tag
              v-if="scope.row.priority > 0"
              type="success"
            >
              {{ scope.row.priority }}
            </el-tag>
            <span v-else class="text-gray">普通</span>
          </template>
        </el-table-column>
        <el-table-column prop="direction" label="岗位方向" width="120">
          <template #default="scope">
            <el-tag
              v-if="scope.row.direction"
              type="info"
            >
              {{ getDirectionIcon(scope.row.direction) }} {{ scope.row.direction }}
            </el-tag>
            <span v-else class="text-gray">未设置</span>
          </template>
        </el-table-column>
        <el-table-column label="精选状态" width="100">
          <template #default="scope">
            <el-tag
              v-if="scope.row.priority > 0"
              type="success"
            >
              精选
            </el-tag>
            <el-tag
              v-else
              type="info"
            >
              普通
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="320">
          <template #default="scope">
            <el-button
              v-if="scope.row.priority === 0"
              size="small"
              type="success"
              @click="setFeatured(scope.row)"
            >
              设为精选
            </el-button>
            <el-button
              v-else
              size="small"
              type="warning"
              @click="cancelFeatured(scope.row)"
            >
              取消精选
            </el-button>
            <el-button
              size="small"
              @click="editLevel(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              size="small"
              type="danger"
              @click="handleDeleteLevel(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.current"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 添加/编辑关卡对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑关卡' : '添加关卡'"
      width="600px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="关卡名称" prop="levelName">
          <el-input v-model="form.levelName" placeholder="请输入关卡名称" />
        </el-form-item>
        <el-form-item label="关卡描述" prop="levelDesc">
          <el-input
            v-model="form.levelDesc"
            type="textarea"
            :rows="4"
            placeholder="请输入关卡描述"
          />
        </el-form-item>
        <el-form-item label="难度等级" prop="difficulty">
          <el-select v-model="form.difficulty" placeholder="请选择难度等级">
            <el-option label="初级" value="初级" />
            <el-option label="初级+" value="初级+" />
            <el-option label="中级" value="中级" />
            <el-option label="高级" value="高级" />
            <el-option label="专家级" value="专家级" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标薪资" prop="targetSalary">
          <el-input-number
            v-model="form.targetSalary"
            :min="1000"
            :max="100000"
            :step="1000"
          />
        </el-form-item>
        <el-form-item label="岗位方向" prop="direction">
          <el-select v-model="form.direction" placeholder="请选择岗位方向" clearable>
            <el-option label="前端开发" value="前端开发" />
            <el-option label="后端开发" value="后端开发" />
            <el-option label="Java后端开发" value="Java后端开发" />
            <el-option label="软件测试" value="软件测试" />
            <el-option label="AI算法" value="AI算法" />
            <el-option label="Unity游戏开发" value="Unity游戏开发" />
            <el-option label="网络运维" value="网络运维" />
            <el-option label="全栈开发" value="全栈开发" />
            <el-option label="小程序" value="小程序" />
          </el-select>
        </el-form-item>
        <el-form-item label="关卡选项" prop="options">
          <el-input
            v-model="form.options"
            type="textarea"
            :rows="6"
            placeholder="请输入关卡选项（JSON格式）"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus,
  Refresh,
  Edit,
  Delete,
  Star,
  StarFilled
} from '@element-plus/icons-vue'
import {
  getLevelsByPage,
  getAllLevels,
  addLevel,
  updateLevel,
  deleteLevel,
  setFeaturedLevel,
  cancelFeaturedLevel
} from '@/api/admin'

// 数据
const levels = ref([])
const loading = ref(false)
const total = ref(0)
const queryParams = reactive({
  current: 1,
  pageSize: 20,
  levelName: '',
  difficulty: '',
  isFeatured: null
})
const dialogVisible = ref(false)
const isEdit = ref(false)
const currentLevel = ref(null)

// 表单数据
const form = reactive({
  levelName: '',
  levelDesc: '',
  difficulty: '',
  targetSalary: 10000,
  direction: '',
  options: ''
})


// 表单校验规则
const rules = {
  levelName: [
    { required: true, message: '请输入关卡名称', trigger: 'blur' }
  ],
  levelDesc: [
    { required: true, message: '请输入关卡描述', trigger: 'blur' }
  ],
  difficulty: [
    { required: true, message: '请选择难度等级', trigger: 'change' }
  ],
  targetSalary: [
    { required: true, message: '请输入目标薪资', trigger: 'blur' }
  ],
  options: [
    { required: true, message: '请输入关卡选项', trigger: 'blur' }
  ]
}

const formRef = ref()

// 获取关卡列表
const fetchLevels = async () => {
  try {
    loading.value = true
    const response = await getLevelsByPage(queryParams)
    console.log('API Response:', response)
    console.log('Records:', response.records)
    console.log('Total:', response.total)
    levels.value = response.records || []
    total.value = response.total || 0
  } catch (error) {
    console.error('获取关卡列表失败:', error)
    ElMessage.error('获取关卡列表失败')
  } finally {
    loading.value = false
  }
}

// 重置查询条件
const resetQuery = () => {
  Object.assign(queryParams, {
    current: 1,
    pageSize: 20,
    levelName: '',
    difficulty: '',
    isFeatured: null
  })
  fetchLevels()
}

// 处理页码变化
const handleCurrentChange = (page) => {
  queryParams.current = page
  fetchLevels()
}

// 处理每页条数变化
const handleSizeChange = (size) => {
  queryParams.pageSize = size
  queryParams.current = 1
  fetchLevels()
}

// 显示添加对话框
const showAddDialog = () => {
  isEdit.value = false
  Object.assign(form, {
    levelName: '',
    levelDesc: '',
    difficulty: '',
    targetSalary: 10000,
    direction: '',
    options: ''
  })
  dialogVisible.value = true
}

// 编辑关卡
const editLevel = (level) => {
  isEdit.value = true
  currentLevel.value = level
  Object.assign(form, level)
  dialogVisible.value = true
}

// 提交表单
const submitForm = async () => {
  try {
    await formRef.value.validate()

    if (isEdit.value) {
      await updateLevel(form)
      ElMessage.success('关卡更新成功')
    } else {
      await addLevel(form)
      ElMessage.success('关卡添加成功')
    }

    dialogVisible.value = false
    fetchLevels()
  } catch (error) {
    ElMessage.error(isEdit.value ? '关卡更新失败' : '关卡添加失败')
  }
}

// 删除关卡
const handleDeleteLevel = async (level) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除关卡"${level.levelName}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await deleteLevel(level.id)
    ElMessage.success('关卡删除成功')
    fetchLevels()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('关卡删除失败')
    }
  }
}

// 设置精选
const setFeatured = async (level) => {
  try {
    await ElMessageBox.confirm(
      `确定要将关卡"${level.levelName}"设为精选吗？`,
      '设置精选',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await setFeaturedLevel(level.id)
    ElMessage.success('设置精选成功')
    fetchLevels()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('设置精选失败')
    }
  }
}

// 取消精选
const cancelFeatured = async (level) => {
  try {
    await cancelFeaturedLevel(level.id)
    ElMessage.success('取消精选成功')
    fetchLevels()
  } catch (error) {
    ElMessage.error('取消精选失败')
  }
}

// 获取难度标签类型
const getDifficultyType = (difficulty) => {
  const typeMap = {
    '初级': 'success',
    '初级+': 'info',
    '中级': 'warning',
    '高级': 'danger',
    '专家级': 'danger'
  }
  return typeMap[difficulty] || 'info'
}

// 获取岗位方向图标
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

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleString()
}

// 初始化
onMounted(() => {
  fetchLevels()
})
</script>

<style scoped>
/* 引入战场主题样式 */
@import '@/styles/battlefield-theme.css';

/* 容器布局 */
.admin-container {
  padding: 20px;
  max-width: 90%;
  margin: 0 auto;
  background: linear-gradient(135deg, var(--bg-primary) 0%, var(--bg-secondary) 100%);
  min-height: 100vh;
  font-family: 'Noto Serif SC', serif;
}

/* 头部样式 */
.admin-header {
  text-align: center;
  margin-bottom: 30px;
  padding: 24px;
  background: linear-gradient(135deg, var(--primary-brown) 0%, var(--secondary-brown) 100%);
  border-radius: 0;
  border: 2px solid var(--border-primary);
  box-shadow:
    0 8px 16px var(--shadow-light),
    inset 0 0 30px rgba(218, 165, 32, 0.05);
  position: relative;
}

.admin-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, var(--battle-gold), var(--sword-silver), var(--battle-gold));
  opacity: 0.8;
}

.admin-header h1 {
  color: var(--text-primary);
  margin-bottom: 10px;
  font-weight: 700;
  font-family: 'Noto Serif SC', serif;
  text-shadow: 2px 2px 4px var(--shadow-light);
  letter-spacing: 1px;
}

.admin-header p {
  color: #2C1810;
  font-size: 18px;
  font-family: 'Noto Serif SC', serif;
  font-weight: 600;
}

/* 操作栏 */
.action-bar {
  margin-bottom: 20px;
  text-align: center;
}

/* 操作栏按钮特殊样式 */
.action-bar .el-button--primary {
  background: linear-gradient(135deg, var(--battle-gold) 0%, var(--light-brown) 100%);
  border-color: var(--battle-gold);
  color: white;
  font-weight: bold;
  text-transform: uppercase;
  letter-spacing: 1px;
  box-shadow:
    0 4px 8px var(--shadow-light),
    inset 0 1px 0 rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.action-bar .el-button--primary:hover {
  background: linear-gradient(135deg, var(--light-brown) 0%, var(--battle-gold) 100%);
  border-color: var(--battle-gold);
  transform: translateY(-2px);
  box-shadow:
    0 6px 12px var(--shadow-heavy),
    inset 0 1px 0 rgba(255, 255, 255, 0.3);
}

/* 搜索区域 */
.search-section {
  background: var(--bg-card);
  padding: 24px;
  border-radius: 0;
  border: 2px solid var(--border-primary);
  box-shadow:
    0 8px 16px var(--shadow-light),
    inset 0 0 30px rgba(218, 165, 32, 0.05);
  margin-bottom: 20px;
  position: relative;
}

.search-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, var(--battle-gold), var(--sword-silver), var(--battle-gold));
  opacity: 0.6;
}

/* 搜索表单按钮组 - 复古风格 */
.search-form .el-button {
  margin-right: 8px;
  background: linear-gradient(135deg, var(--primary-brown) 0%, var(--secondary-brown) 100%);
  color: white;
  border: 2px solid var(--border-primary);
  border-radius: 0;
  font-weight: bold;
  text-transform: uppercase;
  letter-spacing: 1px;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
  box-shadow:
    0 4px 8px var(--shadow-light),
    inset 0 1px 0 rgba(255, 255, 255, 0.2);
}

.search-form .el-button:last-child {
  margin-right: 0;
}

.search-form .el-button:hover {
  background: linear-gradient(135deg, var(--secondary-brown) 0%, var(--light-brown) 100%);
  transform: translateY(-2px);
  box-shadow:
    0 6px 12px var(--shadow-heavy),
    inset 0 1px 0 rgba(255, 255, 255, 0.3);
  border-color: var(--battle-gold);
}

.search-form .el-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(218, 165, 32, 0.4), transparent);
  transition: left 0.6s ease;
}

.search-form .el-button:hover::before {
  left: 100%;
}

/* 主按钮特殊效果 */
.search-form .el-button--primary {
  background: linear-gradient(135deg, var(--battle-gold) 0%, var(--light-brown) 100%);
  border-color: var(--battle-gold);
}

.search-form .el-button--primary:hover {
  background: linear-gradient(135deg, var(--light-brown) 0%, var(--battle-gold) 100%);
  border-color: var(--battle-gold);
}

/* 按钮组样式 */
.button-group {
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

/* 列表区域 */
.level-list {
  background: var(--bg-card);
  padding: 24px;
  border-radius: 0;
  border: 2px solid var(--border-primary);
  box-shadow:
    0 8px 16px var(--shadow-light),
    inset 0 0 30px rgba(218, 165, 32, 0.05);
  position: relative;
}

.level-list::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, var(--battle-gold), var(--sword-silver), var(--battle-gold));
  opacity: 0.6;
}

/* 分页容器 */
.pagination-container {
  margin-top: 24px;
  text-align: center;
  padding-top: 24px;
  border-top: 1px solid var(--border-secondary);
}

/* 对话框底部 */
.dialog-footer {
  text-align: right;
}

/* 灰色文本 */
.text-gray {
  color: var(--text-muted);
  font-weight: 600;
  font-family: 'Noto Serif SC', serif;
}
</style>