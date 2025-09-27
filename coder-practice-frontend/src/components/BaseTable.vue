<template>
  <div class="battle-table-container">
    <div v-if="title" class="battle-table-header">
      <h3 class="battle-table-title">
        <span v-if="icon" class="battle-table-icon">{{ icon }}</span>
        {{ title }}
      </h3>
      <div v-if="$slots.actions" class="battle-table-actions">
        <slot name="actions"></slot>
      </div>
    </div>

    <div class="battle-table-wrapper">
      <table class="battle-table">
        <thead>
          <tr>
            <th
              v-for="column in columns"
              :key="column.prop"
              :class="[
                'battle-table-th',
                `battle-table-align-${column.align || 'left'}`,
                { 'battle-table-sortable': column.sortable }
              ]"
              @click="handleSort(column)"
            >
              <span class="battle-table-th-content">
                {{ column.label }}
                <span v-if="column.sortable" class="battle-table-sort-icon">
                  <span v-if="sortProp === column.prop && sortOrder === 'asc'" class="sort-asc">▲</span>
                  <span v-else-if="sortProp === column.prop && sortOrder === 'desc'" class="sort-desc">▼</span>
                  <span v-else class="sort-default">◊</span>
                </span>
              </span>
            </th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(row, index) in displayData"
            :key="row[rowKey] || index"
            :class="[
              'battle-table-tr',
              { 'battle-table-tr-striped': striped && index % 2 === 1 },
              { 'battle-table-tr-hover': hoverable }
            ]"
            @click="handleRowClick(row, index)"
          >
            <td
              v-for="column in columns"
              :key="column.prop"
              :class="[
                'battle-table-td',
                `battle-table-align-${column.align || 'left'}`
              ]"
            >
              <slot
                :name="`cell-${column.prop}`"
                :row="row"
                :column="column"
                :index="index"
              >
                {{ formatCellContent(row, column) }}
              </slot>
            </td>
          </tr>
        </tbody>
      </table>

      <div v-if="displayData.length === 0" class="battle-table-empty">
        <div class="battle-table-empty-content">
          <div class="battle-table-empty-icon">📜</div>
          <div class="battle-table-empty-text">{{ emptyText }}</div>
        </div>
      </div>
    </div>

    <div v-if="pagination" class="battle-table-pagination">
      <div class="battle-table-pagination-info">
        共 {{ total }} 条记录
      </div>
      <div class="battle-table-pagination-controls">
        <button
          class="battle-table-pagination-btn"
          :disabled="currentPage === 1"
          @click="handlePageChange(currentPage - 1)"
        >
          上一页
        </button>
        <span class="battle-table-pagination-current">
          {{ currentPage }} / {{ totalPages }}
        </span>
        <button
          class="battle-table-pagination-btn"
          :disabled="currentPage === totalPages"
          @click="handlePageChange(currentPage + 1)"
        >
          下一页
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'

const props = defineProps({
  data: {
    type: Array,
    default: () => []
  },
  columns: {
    type: Array,
    required: true
  },
  rowKey: {
    type: String,
    default: 'id'
  },
  title: {
    type: String,
    default: ''
  },
  icon: {
    type: String,
    default: '📊'
  },
  striped: {
    type: Boolean,
    default: true
  },
  hoverable: {
    type: Boolean,
    default: true
  },
  emptyText: {
    type: String,
    default: '暂无数据'
  },
  pagination: {
    type: Boolean,
    default: false
  },
  pageSize: {
    type: Number,
    default: 10
  },
  currentPage: {
    type: Number,
    default: 1
  },
  total: {
    type: Number,
    default: 0
  }
})

const emit = defineEmits(['row-click', 'sort-change', 'page-change'])

const sortProp = ref('')
const sortOrder = ref('')

// 计算总页数
const totalPages = computed(() => {
  return props.pagination ? Math.ceil(props.total / props.pageSize) : 1
})

// 计算显示的数据
const displayData = computed(() => {
  let result = [...props.data]

  // 排序
  if (sortProp.value) {
    result.sort((a, b) => {
      const aVal = a[sortProp.value]
      const bVal = b[sortProp.value]

      if (sortOrder.value === 'asc') {
        return aVal > bVal ? 1 : -1
      } else {
        return aVal < bVal ? 1 : -1
      }
    })
  }

  // 分页
  if (props.pagination) {
    const start = (props.currentPage - 1) * props.pageSize
    const end = start + props.pageSize
    return result.slice(start, end)
  }

  return result
})

// 格式化单元格内容
const formatCellContent = (row, column) => {
  const value = row[column.prop]

  if (column.formatter) {
    return column.formatter(value, row)
  }

  if (column.format === 'date') {
    return new Date(value).toLocaleDateString()
  }

  if (column.format === 'currency') {
    return `💰 ${value}`
  }

  return value
}

// 处理排序
const handleSort = (column) => {
  if (!column.sortable) return

  if (sortProp.value === column.prop) {
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc'
  } else {
    sortProp.value = column.prop
    sortOrder.value = 'asc'
  }

  emit('sort-change', {
    prop: sortProp.value,
    order: sortOrder.value
  })
}

// 处理行点击
const handleRowClick = (row, index) => {
  emit('row-click', row, index)
}

// 处理分页
const handlePageChange = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    emit('page-change', page)
  }
}
</script>

<style scoped>
/* 引入战场主题样式 */
@import '@/styles/battlefield-theme.css';

/* 表格容器 */
.battle-table-container {
  background: var(--bg-card);
  border: 2px solid var(--border-primary);
  border-radius: 4px;
  overflow: hidden;
  position: relative;
  box-shadow:
    0 8px 16px var(--shadow-light),
    inset 0 0 30px rgba(218, 165, 32, 0.05);
}

.battle-table-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, var(--battle-gold), var(--sword-silver), var(--battle-gold));
  opacity: 0.6;
}

/* 表格头部 */
.battle-table-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  border-bottom: 1px solid var(--border-secondary);
  background: linear-gradient(135deg, rgba(139, 69, 19, 0.05) 0%, rgba(218, 165, 32, 0.05) 100%);
}

.battle-table-title {
  font-family: 'Noto Serif SC', serif;
  font-size: 1.125rem;
  font-weight: bold;
  color: var(--text-primary);
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;
  text-shadow: 1px 1px 2px rgba(139, 69, 19, 0.1);
}

.battle-table-icon {
  font-size: 1.2rem;
}

.battle-table-actions {
  display: flex;
  gap: 8px;
}

/* 表格包装器 */
.battle-table-wrapper {
  overflow-x: auto;
  max-height: 600px;
  overflow-y: auto;
}

/* 表格样式 */
.battle-table {
  width: 100%;
  border-collapse: collapse;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Noto Sans SC', sans-serif;
}

.battle-table th,
.battle-table td {
  padding: 16px;
  text-align: left;
  border-bottom: 1px solid var(--border-secondary);
}

.battle-table-th {
  background: linear-gradient(135deg, rgba(139, 69, 19, 0.1) 0%, rgba(218, 165, 32, 0.05) 100%);
  font-weight: bold;
  color: var(--text-primary);
  font-family: 'Noto Serif SC', serif;
  position: sticky;
  top: 0;
  z-index: 10;
  user-select: none;
}

.battle-table-th-content {
  display: flex;
  align-items: center;
  gap: 4px;
}

.battle-table-sortable {
  cursor: pointer;
  transition: all 0.3s ease;
}

.battle-table-sortable:hover {
  background: rgba(218, 165, 32, 0.1);
}

.battle-table-sort-icon {
  font-size: 0.8rem;
  color: var(--battle-gold);
}

.battle-table-tr {
  transition: all 0.3s ease;
}

.battle-table-tr-striped {
  background: rgba(139, 69, 19, 0.02);
}

.battle-table-tr-hover:hover {
  background: rgba(218, 165, 32, 0.1);
  transform: scale(1.01);
  box-shadow: 0 2px 8px var(--shadow-light);
}

.battle-table-td {
  color: var(--text-secondary);
  line-height: 1.6;
}

/* 对齐方式 */
.battle-table-align-left {
  text-align: left;
}

.battle-table-align-center {
  text-align: center;
}

.battle-table-align-right {
  text-align: right;
}

/* 空状态 */
.battle-table-empty {
  text-align: center;
  padding: 60px 20px;
  color: var(--text-secondary);
}

.battle-table-empty-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.battle-table-empty-icon {
  font-size: 3rem;
  opacity: 0.5;
}

.battle-table-empty-text {
  font-family: 'Noto Serif SC', serif;
  font-size: 1.1rem;
  font-style: italic;
}

/* 分页 */
.battle-table-pagination {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  border-top: 1px solid var(--border-secondary);
  background: linear-gradient(135deg, rgba(139, 69, 19, 0.02) 0%, rgba(218, 165, 32, 0.02) 100%);
}

.battle-table-pagination-info {
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.battle-table-pagination-controls {
  display: flex;
  align-items: center;
  gap: 12px;
}

.battle-table-pagination-btn {
  background: var(--bg-card);
  border: 1px solid var(--border-secondary);
  color: var(--text-secondary);
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-family: 'Noto Serif SC', serif;
}

.battle-table-pagination-btn:hover:not(:disabled) {
  background: var(--battle-gold);
  color: white;
  border-color: var(--battle-gold);
  transform: translateY(-1px);
}

.battle-table-pagination-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.battle-table-pagination-current {
  color: var(--text-primary);
  font-weight: bold;
  font-family: 'Noto Serif SC', serif;
}

/* 滚动条样式 */
.battle-table-wrapper::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.battle-table-wrapper::-webkit-scrollbar-track {
  background: var(--bg-secondary);
  border: 1px solid var(--border-secondary);
}

.battle-table-wrapper::-webkit-scrollbar-thumb {
  background: var(--primary-brown);
  border: 1px solid var(--border-secondary);
}

.battle-table-wrapper::-webkit-scrollbar-thumb:hover {
  background: var(--secondary-brown);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .battle-table-header {
    padding: 16px 20px;
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
  }

  .battle-table-actions {
    justify-content: center;
  }

  .battle-table th,
  .battle-table td {
    padding: 12px 8px;
    font-size: 0.875rem;
  }

  .battle-table-pagination {
    padding: 16px 20px;
    flex-direction: column;
    gap: 12px;
  }

  .battle-table-pagination-controls {
    justify-content: center;
  }
}

/* 动画效果 */
@keyframes tableRowGlow {
  0%, 100% {
    background: transparent;
  }
  50% {
    background: rgba(218, 165, 32, 0.05);
  }
}

.battle-table-tr:hover {
  animation: tableRowGlow 2s ease-in-out infinite;
}

/* 战场特殊效果 */
.battle-table-container::after {
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
</style>