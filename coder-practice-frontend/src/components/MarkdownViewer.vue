<template>
  <div class="markdown-viewer">
    <MdPreview
      :modelValue="content"
      :theme="theme"
      :previewTheme="previewTheme"
      :codeTheme="codeTheme"
      :language="language"
      :showCodeRowNumber="showCodeRowNumber"
      :mermaid="mermaidOptions"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { MdPreview } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import mermaid from 'mermaid'

// 定义 props
const props = defineProps({
  content: {
    type: String,
    default: ''
  },
  theme: {
    type: String,
    default: 'light'
  },
  previewTheme: {
    type: String,
    default: 'github'
  },
  codeTheme: {
    type: String,
    default: 'atom'
  },
  language: {
    type: String,
    default: 'zh-CN'
  },
  showCodeRowNumber: {
    type: Boolean,
    default: true
  }
})

// Mermaid 配置
const mermaidOptions = ref({
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
  // 修复文字渲染的关键配置
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

// 初始化 Mermaid
onMounted(() => {
  mermaid.initialize({
    startOnLoad: true,
    theme: 'default',
    themeVariables: mermaidOptions.value.themeVariables,
    fontFamily: 'Noto Serif SC, serif',
    flowchart: mermaidOptions.value.flowchart,
    sequence: mermaidOptions.value.sequence,
    gantt: mermaidOptions.value.gantt
  })
})
</script>

<style scoped>
.markdown-viewer {
  width: 100%;
  font-family: 'Noto Serif SC', serif;
}

/* 战场主题样式适配 */
:deep(.md-editor-preview) {
  background: var(--bg-primary) !important;
  color: var(--text-secondary) !important;
  font-family: 'Noto Serif SC', serif !important;
  line-height: 1.8 !important;
  padding: 0 !important;
}

:deep(.md-editor-preview h1),
:deep(.md-editor-preview h2),
:deep(.md-editor-preview h3),
:deep(.md-editor-preview h4),
:deep(.md-editor-preview h5),
:deep(.md-editor-preview h6) {
  color: var(--text-primary) !important;
  margin: 24px 0 16px 0 !important;
  font-weight: 600 !important;
}

:deep(.md-editor-preview h1) {
  font-size: 1.8rem !important;
  border-bottom: 2px solid var(--border-secondary) !important;
  padding-bottom: 8px !important;
}

:deep(.md-editor-preview h2) {
  font-size: 1.5rem !important;
  border-bottom: 1px solid var(--border-secondary) !important;
  padding-bottom: 6px !important;
}

:deep(.md-editor-preview h3) {
  font-size: 1.3rem !important;
}

:deep(.md-editor-preview p) {
  margin: 16px 0 !important;
}

:deep(.md-editor-preview ul),
:deep(.md-editor-preview ol) {
  margin: 16px 0 !important;
  padding-left: 32px !important;
}

:deep(.md-editor-preview li) {
  margin: 8px 0 !important;
}

:deep(.md-editor-preview blockquote) {
  border-left: 4px solid var(--battle-gold) !important;
  margin: 16px 0 !important;
  padding: 8px 16px !important;
  background: var(--bg-primary) !important;
  font-style: italic !important;
}

:deep(.md-editor-preview code) {
  background: var(--bg-primary) !important;
  padding: 2px 6px !important;
  border-radius: 4px !important;
  font-family: 'Consolas', 'Monaco', monospace !important;
  font-size: 0.9rem !important;
  color: var(--battle-gold) !important;
}

:deep(.md-editor-preview pre) {
  background: var(--bg-primary) !important;
  border: 1px solid var(--border-secondary) !important;
  border-radius: 4px !important;
  padding: 16px !important;
  overflow-x: auto !important;
  margin: 16px 0 !important;
}

:deep(.md-editor-preview pre code) {
  background: none !important;
  padding: 0 !important;
  color: var(--text-secondary) !important;
}

:deep(.md-editor-preview table) {
  border-collapse: collapse !important;
  width: 100% !important;
  margin: 16px 0 !important;
}

:deep(.md-editor-preview th),
:deep(.md-editor-preview td) {
  border: 1px solid var(--border-secondary) !important;
  padding: 8px 12px !important;
  text-align: left !important;
}

:deep(.md-editor-preview th) {
  background: var(--bg-primary) !important;
  font-weight: 600 !important;
  color: var(--text-primary) !important;
}

:deep(.md-editor-preview a) {
  color: var(--battle-gold) !important;
  text-decoration: none !important;
}

:deep(.md-editor-preview a:hover) {
  text-decoration: underline !important;
}

/* Mermaid 图表样式适配 */
:deep(.md-editor-preview .mermaid) {
  background: var(--bg-primary) !important;
  border: 2px solid var(--border-secondary) !important;
  border-radius: 8px !important;
  padding: 30px !important;
  margin: 30px 0 !important;
  text-align: center !important;
  overflow-x: auto !important;
  overflow-y: visible !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1) !important;
  min-height: 400px !important;
}

:deep(.md-editor-preview .mermaid svg) {
  max-width: 100% !important;
  height: auto !important;
  display: block !important;
  margin: 0 auto !important;
  min-height: 350px !important;
}

:deep(.md-editor-preview .mermaid svg text) {
  font-family: 'Noto Serif SC', serif !important;
  fill: #2c1810 !important;
  color: #2c1810 !important;
  font-size: 14px !important;
  font-weight: 500 !important;
  text-anchor: middle !important;
  dominant-baseline: middle !important;
}

/* Mermaid 节点样式 */
:deep(.md-editor-preview .mermaid .node rect),
:deep(.md-editor-preview .mermaid .node circle),
:deep(.md-editor-preview .mermaid .node ellipse),
:deep(.md-editor-preview .mermaid .node polygon),
:deep(.md-editor-preview .mermaid .node path) {
  fill: #daa520 !important;
  stroke: #8b4513 !important;
  stroke-width: 2px !important;
}

:deep(.md-editor-preview .mermaid .node text) {
  fill: #2c1810 !important;
  font-family: 'Noto Serif SC', serif !important;
  font-size: 14px !important;
  font-weight: 600 !important;
  text-anchor: middle !important;
  dominant-baseline: middle !important;
}

/* Mermaid 边缘标签样式 */
:deep(.md-editor-preview .mermaid .edgeLabel) {
  background-color: #fff8dc !important;
  fill: #2c1810 !important;
  color: #2c1810 !important;
  font-family: 'Noto Serif SC', serif !important;
  font-size: 12px !important;
}

:deep(.md-editor-preview .mermaid .edgeLabel text) {
  fill: #2c1810 !important;
  color: #2c1810 !important;
  font-family: 'Noto Serif SC', serif !important;
  font-size: 12px !important;
}

/* Mermaid 标签样式 */
:deep(.md-editor-preview .mermaid .label text) {
  fill: #2c1810 !important;
  font-family: 'Noto Serif SC', serif !important;
  font-size: 12px !important;
}

/* Mermaid 子图样式 */
:deep(.md-editor-preview .mermaid .cluster rect) {
  fill: rgba(245, 222, 179, 0.2) !important;
  stroke: #8b4513 !important;
  stroke-width: 1px !important;
}

:deep(.md-editor-preview .mermaid .cluster text) {
  fill: #2c1810 !important;
  font-family: 'Noto Serif SC', serif !important;
  font-size: 14px !important;
  font-weight: 600 !important;
}

/* 确保所有文本元素都使用正确的字体和颜色 */
:deep(.md-editor-preview .mermaid .label),
:deep(.md-editor-preview .mermaid .nodeLabel),
:deep(.md-editor-preview .mermaid .edgeLabel) {
  font-family: 'Noto Serif SC', serif !important;
  color: #2c1810 !important;
  fill: #2c1810 !important;
}

/* Mermaid 连接线样式 */
:deep(.md-editor-preview .mermaid .edgePath .path) {
  stroke: var(--primary-brown) !important;
  stroke-width: 2px !important;
}

:deep(.md-editor-preview .mermaid .edgeLabel) {
  background-color: var(--bg-primary) !important;
  fill: var(--text-primary) !important;
}

/* 响应式适配 */
@media (max-width: 768px) {
  :deep(.md-editor-preview .mermaid) {
    padding: 12px !important;
    margin: 12px 0 !important;
  }

  :deep(.md-editor-preview .mermaid svg) {
    transform: scale(0.9) !important;
  }
}
</style>