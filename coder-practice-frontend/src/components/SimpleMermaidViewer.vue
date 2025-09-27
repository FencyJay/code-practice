<template>
  <div class="mermaid-viewer" ref="mermaidContainer">
    <!-- 渲染的内容将在这里显示 -->
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { marked } from 'marked'
import mermaid from 'mermaid'

const props = defineProps({
  content: {
    type: String,
    default: ''
  },
  theme: {
    type: String,
    default: 'base'
  }
})

const mermaidContainer = ref(null)

// 获取主题配置
const getThemeConfig = (theme) => {
  const themes = {
    base: {
      theme: 'base',
      themeVariables: {
        primaryColor: '#e1f5fe',
        primaryTextColor: '#01579b',
        primaryBorderColor: '#81d4fa',
        lineColor: '#546e7a',
        secondaryColor: '#f3e5f5',
        tertiaryColor: '#fff3e0',
        textColor: '#263238',
        mainBkg: '#e3f2fd',
        secondBkg: '#f3e5f5',
        clusterBkg: 'rgba(227, 242, 253, 0.3)',
        clusterBorder: '#90caf9',
        edgeLabelBackground: '#ffffff',
        noteTextColor: '#263238',
        noteBorderColor: '#90caf9',
        taskTextColor: '#01579b',
        taskBkgColor: '#e1f5fe',
        taskBorderColor: '#81d4fa'
      }
    },
    forest: {
      theme: 'base',
      themeVariables: {
        primaryColor: '#e8f5e8',
        primaryTextColor: '#1b5e20',
        primaryBorderColor: '#a5d6a7',
        lineColor: '#546e7a',
        secondaryColor: '#e3f2fd',
        tertiaryColor: '#fff8e1',
        textColor: '#263238',
        mainBkg: '#e8f5e8',
        secondBkg: '#e3f2fd',
        clusterBkg: 'rgba(232, 245, 232, 0.3)',
        clusterBorder: '#a5d6a7',
        edgeLabelBackground: '#ffffff',
        noteTextColor: '#263238',
        noteBorderColor: '#a5d6a7',
        taskTextColor: '#1b5e20',
        taskBkgColor: '#e8f5e8',
        taskBorderColor: '#a5d6a7'
      }
    },
    default: {
      theme: 'default',
      themeVariables: {
        primaryColor: '#ffffff',
        primaryTextColor: '#333333',
        primaryBorderColor: '#e0e0e0',
        lineColor: '#666666',
        secondaryColor: '#f5f5f5',
        tertiaryColor: '#fafafa',
        textColor: '#333333',
        mainBkg: '#ffffff',
        secondBkg: '#f5f5f5',
        clusterBkg: 'rgba(245, 245, 245, 0.3)',
        clusterBorder: '#e0e0e0',
        edgeLabelBackground: '#ffffff',
        noteTextColor: '#333333',
        noteBorderColor: '#e0e0e0',
        taskTextColor: '#333333',
        taskBkgColor: '#ffffff',
        taskBorderColor: '#e0e0e0'
      }
    },
    clean: {
      theme: 'base',
      themeVariables: {
        primaryColor: '#f8f9fa',
        primaryTextColor: '#495057',
        primaryBorderColor: '#dee2e6',
        lineColor: '#6c757d',
        secondaryColor: '#e9ecef',
        tertiaryColor: '#ffffff',
        textColor: '#212529',
        mainBkg: '#f8f9fa',
        secondBkg: '#e9ecef',
        clusterBkg: 'rgba(248, 249, 250, 0.3)',
        clusterBorder: '#dee2e6',
        edgeLabelBackground: '#ffffff',
        noteTextColor: '#212529',
        noteBorderColor: '#dee2e6',
        taskTextColor: '#495057',
        taskBkgColor: '#f8f9fa',
        taskBorderColor: '#dee2e6'
      }
    }
  }

  return themes[theme] || themes.default
}

// 初始化 Mermaid
const initMermaid = () => {
  const config = getThemeConfig(props.theme)

  mermaid.initialize({
    startOnLoad: false,
    theme: config.theme,
    themeVariables: {
      ...config.themeVariables,
      fontFamily: 'Arial, sans-serif',
      altFontFamily: 'Arial, sans-serif',
      labelFontFamily: 'Arial, sans-serif',
      nodeTextColor: '#ffffff',
      edgeLabelFontFamily: 'Arial, sans-serif',
      edgeLabelFontSize: '12px',
      edgeLabelTextColor: '#333333'
    },
    fontFamily: 'Arial, sans-serif',
    flowchart: {
      useMaxWidth: false,
      htmlLabels: true,
      curve: 'basis'
    },
    sequence: {
      diagramMarginX: 50,
      diagramMarginY: 10,
      actorMargin: 50,
      width: 150,
      height: 65,
      boxMargin: 10,
      boxTextMargin: 5,
      noteMargin: 10,
      messageMargin: 35,
      mirrorActors: true,
      bottomMarginAdj: 1,
      useMaxWidth: false,
      rightAngles: false,
      showSequenceNumbers: false,
      actorFontFamily: 'Arial, sans-serif',
      actorTextColor: '#333333',
      actorBkg: config.themeVariables.primaryColor,
      actorBorder: config.themeVariables.primaryBorderColor,
      actorLineColor: '#333333'
    },
    gantt: {
      useMaxWidth: false,
      fontSize: '14px',
      fontFamily: 'Arial, sans-serif',
      sectionBkgColor: config.themeVariables.clusterBkg,
      altSectionBkgColor: 'rgba(0, 153, 51, 0.1)',
      taskTextColor: '#ffffff',
      taskBkgColor: config.themeVariables.primaryColor,
      taskBorderColor: config.themeVariables.primaryBorderColor,
      gridColor: 'rgba(51, 51, 51, 0.2)',
      todayLineColor: '#ff6600'
    }
  })
}

// 渲染内容
const renderContent = async () => {
  if (!mermaidContainer.value || !props.content) return

  try {
    // 清空容器
    mermaidContainer.value.innerHTML = ''

    // 分割内容，识别 mermaid 代码块
    const parts = props.content.split(/```(?:mermaid)?\n/)

    for (let i = 0; i < parts.length; i++) {
      const part = parts[i]

      if (i % 2 === 0) {
        // 这是普通 markdown 内容
        if (part.trim()) {
          const html = marked(part)
          const div = document.createElement('div')
          div.innerHTML = html
          div.className = 'markdown-content'
          mermaidContainer.value.appendChild(div)
        }
      } else {
        // 这是 mermaid 代码
        const mermaidCode = part.split('```')[0].trim()
        if (mermaidCode) {
          try {
            // 清理代码，移除可能的语法问题
            const cleanCode = mermaidCode.trim()
            if (!cleanCode) continue

            // 生成安全的 ID，避免特殊字符
            const safeId = `mermaid-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`

            // 尝试渲染，如果失败则重试一次
            let svg
            try {
              svg = (await mermaid.render(safeId, cleanCode)).svg
            } catch (firstError) {
              console.warn('第一次渲染失败，尝试重试:', firstError)
              // 等待一小段时间后重试
              await new Promise(resolve => setTimeout(resolve, 100))
              svg = (await mermaid.render(safeId + '-retry', cleanCode)).svg
            }

            const div = document.createElement('div')
            div.innerHTML = svg
            div.className = 'mermaid-container'
            mermaidContainer.value.appendChild(div)
          } catch (error) {
            console.error('Mermaid 渲染错误:', error)
            const errorDiv = document.createElement('div')
            errorDiv.className = 'mermaid-error'
            errorDiv.innerHTML = `
              <div class="error-title">🔍 Mermaid 渲染错误</div>
              <div class="error-message">${error.message}</div>
              <div class="error-code">问题代码:</div>
              <pre>${mermaidCode}</pre>
            `
            mermaidContainer.value.appendChild(errorDiv)
          }
        }
      }
    }
  } catch (error) {
    console.error('渲染错误:', error)
    mermaidContainer.value.innerHTML = `<div class="render-error">渲染错误: ${error.message}</div>`
  }
}

// 监听内容变化
watch(() => props.content, renderContent, { immediate: true })

// 组件挂载时初始化
onMounted(() => {
  initMermaid()
  renderContent()
})
</script>

<style scoped>
.mermaid-viewer {
  width: 100%;
  font-family: 'Noto Serif SC', serif;
  line-height: 1.8;
}

/* Markdown 内容样式 */
:deep(.markdown-content) {
  margin-bottom: 20px;
}

:deep(.markdown-content h1),
:deep(.markdown-content h2),
:deep(.markdown-content h3),
:deep(.markdown-content h4),
:deep(.markdown-content h5),
:deep(.markdown-content h6) {
  color: var(--text-primary);
  margin: 24px 0 16px 0;
  font-weight: 600;
}

:deep(.markdown-content h1) {
  font-size: 1.8rem;
  border-bottom: 2px solid var(--border-secondary);
  padding-bottom: 8px;
}

:deep(.markdown-content h2) {
  font-size: 1.5rem;
  border-bottom: 1px solid var(--border-secondary);
  padding-bottom: 6px;
}

:deep(.markdown-content p) {
  margin: 16px 0;
}

:deep(.markdown-content ul),
:deep(.markdown-content ol) {
  margin: 16px 0;
  padding-left: 32px;
}

:deep(.markdown-content li) {
  margin: 8px 0;
}

:deep(.markdown-content blockquote) {
  border-left: 4px solid var(--battle-gold);
  margin: 16px 0;
  padding: 8px 16px;
  background: var(--bg-primary);
  font-style: italic;
}

:deep(.markdown-content code) {
  background: var(--bg-primary);
  padding: 2px 6px;
  border-radius: 4px;
  font-family: 'Consolas', 'Monaco', monospace;
  font-size: 0.9rem;
  color: var(--battle-gold);
}

:deep(.markdown-content pre) {
  background: var(--bg-primary);
  border: 1px solid var(--border-secondary);
  border-radius: 4px;
  padding: 16px;
  overflow-x: auto;
  margin: 16px 0;
}

:deep(.markdown-content table) {
  border-collapse: collapse;
  width: 100%;
  margin: 16px 0;
}

:deep(.markdown-content th),
:deep(.markdown-content td) {
  border: 1px solid var(--border-secondary);
  padding: 8px 12px;
  text-align: left;
}

:deep(.markdown-content th) {
  background: var(--bg-primary);
  font-weight: 600;
  color: var(--text-primary);
}

:deep(.markdown-content a) {
  color: var(--battle-gold);
  text-decoration: none;
}

:deep(.markdown-content a:hover) {
  text-decoration: underline;
}

/* Mermaid 容器样式 */
:deep(.mermaid-container) {
  background: #ffffff;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 20px;
  margin: 20px 0;
  text-align: center;
  overflow-x: auto;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

:deep(.mermaid-container svg) {
  max-width: 100%;
  height: auto;
  display: block;
  margin: 0 auto;
}

:deep(.mermaid-container svg text) {
  font-family: 'Arial, sans-serif';
  font-size: 14px;
  font-weight: 500;
}

/* 错误样式 */
:deep(.mermaid-error) {
  color: #ff4444;
  background: #ffeeee;
  border: 1px solid #ffcccc;
  border-radius: 4px;
  padding: 12px;
  margin: 16px 0;
}

:deep(.mermaid-error .error-title) {
  font-weight: bold;
  margin-bottom: 8px;
}

:deep(.mermaid-error .error-message) {
  margin-bottom: 8px;
}

:deep(.mermaid-error .error-code) {
  font-weight: bold;
  margin-bottom: 4px;
}

:deep(.mermaid-error pre) {
  background: #f8f8f8;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 8px;
  margin: 0;
  font-size: 12px;
  overflow-x: auto;
}

:deep(.render-error) {
  color: #ff4444;
  background: #ffeeee;
  border: 1px solid #ffcccc;
  border-radius: 4px;
  padding: 12px;
  margin: 16px 0;
}

/* 响应式适配 */
@media (max-width: 768px) {
  :deep(.mermaid-container) {
    padding: 12px;
    margin: 12px 0;
  }

  :deep(.mermaid-container svg) {
    transform: scale(0.9);
  }
}
</style>