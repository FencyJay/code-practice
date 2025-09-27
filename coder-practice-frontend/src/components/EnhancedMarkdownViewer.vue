<template>
  <div class="markdown-viewer" ref="markdownContainer">
    <div v-html="renderedContent"></div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch, nextTick } from 'vue'
import { marked } from 'marked'
import mermaid from 'mermaid'

// 定义 props
const props = defineProps({
  content: {
    type: String,
    default: ''
  }
})

const markdownContainer = ref(null)
const renderedContent = ref('')

// 配置 marked
marked.setOptions({
  breaks: true,
  gfm: true,
  headerIds: true,
  headerPrefix: 'heading-',
  highlight: function(code, lang) {
    if (lang === 'mermaid') {
      // 为 mermaid 代码块添加特殊标记
      return `<div class="mermaid">${code}</div>`
    }
    return code
  }
})

// 初始化 Mermaid
const initMermaid = () => {
  mermaid.initialize({
    startOnLoad: false,
    theme: 'default',
    themeVariables: {
      primaryColor: '#daa520',
      primaryTextColor: '#2c1810',
      primaryBorderColor: '#8b4513',
      lineColor: '#8b4513',
      secondaryColor: '#f5deb3',
      tertiaryColor: '#fff',
      clusterBkg: 'rgba(245, 222, 179, 0.2)',
      clusterBorder: '#8b4513',
      defaultLinkColor: '#8b4513',
      titleColor: '#2c1810',
      edgeLabelBackground: '#fff8dc',
      fontSize: '16px',
      fontFamily: 'Noto Serif SC, serif',
      // 确保文字颜色设置正确
      altFontFamily: 'Noto Serif SC, serif',
      labelFontFamily: 'Noto Serif SC, serif',
      nodeTextColor: '#2c1810',
      edgeLabelFontFamily: 'Noto Serif SC, serif',
      edgeLabelFontSize: '14px',
      edgeLabelTextColor: '#2c1810',
      // 关键文字渲染配置
      mainBkg: '#daa520',
      secondBkg: '#f5deb3',
      lineColor: '#8b4513',
      textColor: '#2c1810',
      fontColor: '#2c1810',
      clusterTextColor: '#2c1810',
      actorTextColor: '#2c1810',
      actorBkg: '#daa520',
      actorBorder: '#8b4513',
      actorLineColor: '#8b4513',
      signalColor: '#8b4513',
      signalTextColor: '#2c1810',
      labelBoxBkgColor: '#fff8dc',
      labelBoxBorderColor: '#8b4513',
      loopTextColor: '#2c1810',
      noteTextColor: '#2c1810',
      noteBorderColor: '#8b4513',
      activationTextColor: '#2c1810',
      activationBkgColor: '#f5deb3',
      gridColor: 'rgba(139, 69, 19, 0.3)',
      sectionBkgColor: 'rgba(245, 222, 179, 0.1)',
      altSectionBkgColor: 'rgba(245, 222, 179, 0.1)',
      sectionTextColor: '#2c1810',
      message0TextColor: '#2c1810',
      message1TextColor: '#2c1810'
    },
    fontFamily: 'Noto Serif SC, serif',
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
      showSequenceNumbers: false
    },
    gantt: {
      useMaxWidth: false,
      fontSize: '16px',
      fontFamily: 'Noto Serif SC, serif'
    }
  })
}

// 渲染 Mermaid 图表
const renderMermaid = async () => {
  if (!markdownContainer.value) return

  const mermaidElements = markdownContainer.value.querySelectorAll('.mermaid')

  for (const element of mermaidElements) {
    try {
      const code = element.textContent.trim()
      const { svg } = await mermaid.render(`mermaid-${Date.now()}-${Math.random()}`, code)
      element.innerHTML = svg
    } catch (error) {
      console.error('Mermaid 渲染错误:', error)
      element.innerHTML = `<div class="mermaid-error">Mermaid 渲染错误: ${error.message}</div>`
    }
  }
}

// 渲染内容
const renderContent = async () => {
  if (!props.content) {
    renderedContent.value = ''
    return
  }

  try {
    // 使用 marked 渲染 markdown
    const html = marked(props.content)
    renderedContent.value = html

    // 使用 nextTick 确保 DOM 更新完成
    await nextTick()

    // 等待一小段时间确保 DOM 完全渲染
    setTimeout(async () => {
      await renderMermaid()
    }, 50)
  } catch (error) {
    console.error('Markdown 渲染错误:', error)
    renderedContent.value = `<div class="render-error">渲染错误: ${error.message}</div>`
  }
}

// 监听内容变化
watch(() => props.content, renderContent, { immediate: true })

// 组件挂载时初始化
onMounted(async () => {
  initMermaid()
  await renderContent()
})
</script>

<style scoped>
.markdown-viewer {
  width: 100%;
  font-family: 'Noto Serif SC', serif;
  line-height: 1.8;
}

/* 基础样式 */
:deep(h1),
:deep(h2),
:deep(h3),
:deep(h4),
:deep(h5),
:deep(h6) {
  color: var(--text-primary);
  margin: 24px 0 16px 0;
  font-weight: 600;
}

:deep(h1) {
  font-size: 1.8rem;
  border-bottom: 2px solid var(--border-secondary);
  padding-bottom: 8px;
}

:deep(h2) {
  font-size: 1.5rem;
  border-bottom: 1px solid var(--border-secondary);
  padding-bottom: 6px;
}

:deep(h3) {
  font-size: 1.3rem;
}

:deep(p) {
  margin: 16px 0;
}

:deep(ul),
:deep(ol) {
  margin: 16px 0;
  padding-left: 32px;
}

:deep(li) {
  margin: 8px 0;
}

:deep(blockquote) {
  border-left: 4px solid var(--battle-gold);
  margin: 16px 0;
  padding: 8px 16px;
  background: var(--bg-primary);
  font-style: italic;
}

:deep(code) {
  background: var(--bg-primary);
  padding: 2px 6px;
  border-radius: 4px;
  font-family: 'Consolas', 'Monaco', monospace;
  font-size: 0.9rem;
  color: var(--battle-gold);
}

:deep(pre) {
  background: var(--bg-primary);
  border: 1px solid var(--border-secondary);
  border-radius: 4px;
  padding: 16px;
  overflow-x: auto;
  margin: 16px 0;
}

:deep(pre code) {
  background: none;
  padding: 0;
  color: var(--text-secondary);
}

:deep(table) {
  border-collapse: collapse;
  width: 100%;
  margin: 16px 0;
}

:deep(th),
:deep(td) {
  border: 1px solid var(--border-secondary);
  padding: 8px 12px;
  text-align: left;
}

:deep(th) {
  background: var(--bg-primary);
  font-weight: 600;
  color: var(--text-primary);
}

:deep(a) {
  color: var(--battle-gold);
  text-decoration: none;
}

:deep(a:hover) {
  text-decoration: underline;
}

/* Mermaid 图表样式 */
:deep(.mermaid) {
  background: var(--bg-primary);
  border: 2px solid var(--border-secondary);
  border-radius: 8px;
  padding: 30px;
  margin: 30px 0;
  text-align: center;
  overflow-x: auto;
  overflow-y: visible;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  min-height: 400px;
}

:deep(.mermaid svg) {
  max-width: 100%;
  height: auto;
  display: block;
  margin: 0 auto;
  min-height: 350px;
}

:deep(.mermaid svg text) {
  font-family: 'Noto Serif SC', serif;
  fill: #2c1810;
  color: #2c1810;
  font-size: 14px;
  font-weight: 500;
  text-anchor: middle;
  dominant-baseline: middle;
}

/* Mermaid 节点样式 */
:deep(.mermaid .node rect),
:deep(.mermaid .node circle),
:deep(.mermaid .node ellipse),
:deep(.mermaid .node polygon),
:deep(.mermaid .node path) {
  fill: #daa520;
  stroke: #8b4513;
  stroke-width: 2px;
}

:deep(.mermaid .node text) {
  fill: #2c1810;
  font-family: 'Noto Serif SC', serif;
  font-size: 14px;
  font-weight: 600;
  text-anchor: middle;
  dominant-baseline: middle;
}

/* Mermaid 边缘标签样式 */
:deep(.mermaid .edgeLabel) {
  background-color: #fff8dc;
  fill: #2c1810;
  color: #2c1810;
  font-family: 'Noto Serif SC', serif;
  font-size: 12px;
}

:deep(.mermaid .edgeLabel text) {
  fill: #2c1810;
  color: #2c1810;
  font-family: 'Noto Serif SC', serif;
  font-size: 12px;
}

/* Mermaid 标签样式 */
:deep(.mermaid .label text) {
  fill: #2c1810;
  font-family: 'Noto Serif SC', serif;
  font-size: 12px;
}

/* Mermaid 子图样式 */
:deep(.mermaid .cluster rect) {
  fill: rgba(245, 222, 179, 0.2);
  stroke: #8b4513;
  stroke-width: 1px;
}

:deep(.mermaid .cluster text) {
  fill: #2c1810;
  font-family: 'Noto Serif SC', serif;
  font-size: 14px;
  font-weight: 600;
}

/* 确保所有文本元素都使用正确的字体和颜色 */
:deep(.mermaid .label),
:deep(.mermaid .nodeLabel),
:deep(.mermaid .edgeLabel) {
  font-family: 'Noto Serif SC', serif;
  color: #2c1810;
  fill: #2c1810;
}

/* Mermaid 连接线样式 */
:deep(.mermaid .edgePath .path) {
  stroke: var(--primary-brown);
  stroke-width: 2px;
}

:deep(.mermaid .edgeLabel) {
  background-color: var(--bg-primary);
  fill: var(--text-primary);
}

/* 错误样式 */
:deep(.mermaid-error) {
  color: #ff4444;
  background: #ffeeee;
  border: 1px solid #ffcccc;
  border-radius: 4px;
  padding: 8px;
  margin: 8px 0;
}

:deep(.render-error) {
  color: #ff4444;
  background: #ffeeee;
  border: 1px solid #ffcccc;
  border-radius: 4px;
  padding: 8px;
  margin: 8px 0;
}

/* 响应式适配 */
@media (max-width: 768px) {
  :deep(.mermaid) {
    padding: 12px;
    margin: 12px 0;
  }

  :deep(.mermaid svg) {
    transform: scale(0.9);
  }
}
</style>