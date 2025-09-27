<template>
  <div class="markdown-test-container">
    <div class="test-header">
      <h1>🎯 Markdown + Mermaid 渲染测试</h1>
      <p>使用 SimpleMermaidViewer 组件，专门处理 markdown 和 mermaid 混合内容渲染</p>
    </div>

    <div class="test-content">
      <div class="test-section">
        <h2>📝 测试内容</h2>
        <div class="content-selector">
          <el-button
            v-for="(test, index) in testCases"
            :key="index"
            :type="currentTest === index ? 'primary' : 'default'"
            @click="selectTest(index)"
          >
            {{ test.name }}
          </el-button>
        </div>
      </div>

      <div class="test-section">
        <h2>📊 渲染结果</h2>
        <div class="markdown-renderer">
          <div class="theme-selector">
            <span class="theme-label">🎨 主题选择：</span>
            <el-radio-group v-model="currentTheme" size="small">
              <el-radio-button label="default">Default (默认白)</el-radio-button>
              <el-radio-button label="base">Base (浅蓝)</el-radio-button>
              <el-radio-button label="forest">Forest (浅绿)</el-radio-button>
              <el-radio-button label="clean">Clean (清爽)</el-radio-button>
            </el-radio-group>
          </div>
          <SimpleMermaidViewer
            :content="currentContent"
            :theme="currentTheme"
          />
        </div>
      </div>

      <div class="test-section">
        <h2>🔧 原始 Markdown</h2>
        <div class="markdown-source">
          <el-input
            v-model="currentContent"
            type="textarea"
            :rows="10"
            placeholder="Markdown 内容..."
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import SimpleMermaidViewer from '@/components/SimpleMermaidViewer.vue'

// 测试用例
const testCases = [
  {
    name: '基础 Markdown',
    content: `# 基础 Markdown 测试

这是一个 **粗体** 文本 和 *斜体* 文本的测试。

## 列表测试

### 无序列表
- 项目 1
- 项目 2
- 项目 3

### 有序列表
1. 第一步
2. 第二步
3. 第三步

## 代码块测试

\`\`\`javascript
function greet(name) {
  return \`Hello, \${name}!\`;
}

console.log(greet('World'));
\`\`\`

## 引用测试

> 这是一个引用块
>
> 可以包含多行内容

## 表格测试

| 列1 | 列2 | 列3 |
|-----|-----|-----|
| 数据1 | 数据2 | 数据3 |
| 数据4 | 数据5 | 数据6 |

---

*测试完成*`
  },
  {
    name: 'Mermaid 流程图',
    content: `# Mermaid 流程图测试

## 算法流程图

\`\`\`mermaid
graph TD
    A[开始] --> B[初始化sum为0]
    B --> C{还有元素吗?}
    C -->|是| D[将当前元素加到sum]
    D --> C
    C -->|否| E[返回sum]
    E --> F[结束]
\`\`\`

这个流程图展示了数组求和算法的执行过程。

## 判断流程图

\`\`\`mermaid
flowchart TD
    A[开始] --> B{用户是否登录}
    B -->|已登录| C[显示主页]
    B -->|未登录| D[跳转登录页]
    C --> E[用户操作]
    D --> F[用户登录]
    F --> B
    E --> G[结束]
\`\`\``
  },
  {
    name: 'Mermaid 序列图',
    content: `# Mermaid 序列图测试

## 用户登录序列图

\`\`\`mermaid
sequenceDiagram
    participant 用户
    participant 前端
    participant 后端
    participant 数据库

    用户->>前端: 输入用户名密码
    前端->>后端: 发送登录请求
    后端->>数据库: 验证用户信息
    数据库-->>后端: 返回用户数据
    后端-->>前端: 返回token
    前端->>前端: 存储token
    前端-->>用户: 显示登录成功
\`\`\`

## API 调用序列图

\`\`\`mermaid
sequenceDiagram
    participant Client
    participant API Gateway
    participant Auth Service
    participant User Service

    Client->>API Gateway: POST /login
    API Gateway->>Auth Service: 验证请求
    Auth Service->>User Service: 获取用户信息
    User Service-->>Auth Service: 返回用户数据
    Auth Service-->>API Gateway: 生成JWT
    API Gateway-->>Client: 返回token和用户信息
\`\`\``
  },
  {
    name: 'Mermaid 类图',
    content: `# Mermaid 类图测试

## 用户系统类图

\`\`\`mermaid
classDiagram
    class User {
        +String id
        +String username
        +String email
        +String password
        +DateTime createdAt
        +DateTime updatedAt
        +login()
        +logout()
        +updateProfile()
    }

    class UserProfile {
        +String userId
        +String avatar
        +String bio
        +String location
        +String website
        +updateAvatar()
        +updateBio()
    }

    class Role {
        +String id
        +String name
        +String description
        +getPermissions()
    }

    class Permission {
        +String id
        +String name
        +String resource
        +String action
    }

    User "1" -- "1" UserProfile
    User "1" -- "*" Role
    Role "1" -- "*" Permission
\`\`\`

## 订单系统类图

\`\`\`mermaid
classDiagram
    class Order {
        +String id
        +String userId
        +Decimal totalAmount
        +String status
        +DateTime createdAt
        +addItem()
        +removeItem()
        +calculateTotal()
        +updateStatus()
    }

    class OrderItem {
        +String id
        +String orderId
        +String productId
        +Integer quantity
        +Decimal price
        +getSubtotal()
    }

    class Product {
        +String id
        +String name
        +Decimal price
        +Integer stock
        +String description
        +updateStock()
        +isAvailable()
    }

    Order "1" -- "*" OrderItem
    OrderItem "*" -- "1" Product
\`\`\`
  },
  {
    name: '微服务架构图',
    content: `# 微服务架构图

📈 架构图示意（简化版）

\`\`\`mermaid
graph TD
    A[前端 UI - React/Vue] --> B{API Gateway - Nginx}
    B --> C1[User Service - Spring Boot]
    B --> C2[Order Service - Spring Boot]
    B --> C3[Payment Service - Spring Boot]
    C1 <--> D[(PostgreSQL)]
    C2 <--> E[(MongoDB)]
    C3 <--> F[(Redis)]
    C1 -- 消息通知 --> G[Kafka]
    G --> H[Data Analysis Module]
    H --> I[(Elasticsearch)]
    J[CI/CD Pipeline - Jenkins] --> K[Docker Build]
    K --> L[Kubernetes Deployment]
    M[Prometheus] --> N[Grafana Dashboard]
\`\`\`

这套架构才算是把微服务那套玩法玩明白了。`
  }
]

const currentTest = ref(0)
const currentContent = computed(() => testCases[currentTest.value].content)

const selectTest = (index) => {
  currentTest.value = index
}
</script>

<style scoped>
.markdown-test-container {
  min-height: 100vh;
  background: var(--bg-primary);
  padding: 40px 20px;
}

.test-header {
  text-align: center;
  margin-bottom: 40px;
}

.test-header h1 {
  color: var(--text-primary);
  font-size: 2.5rem;
  margin-bottom: 16px;
  font-family: 'Noto Serif SC', serif;
}

.test-header p {
  color: var(--text-secondary);
  font-size: 1.1rem;
  font-family: 'Noto Serif SC', serif;
}

.test-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.test-section {
  background: var(--bg-card);
  border: 2px solid var(--border-primary);
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 8px 32px var(--shadow-heavy);
}

.test-section h2 {
  color: var(--text-primary);
  font-size: 1.8rem;
  margin-bottom: 20px;
  font-family: 'Noto Serif SC', serif;
  border-bottom: 2px solid var(--border-secondary);
  padding-bottom: 10px;
}

.content-selector {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.markdown-renderer {
  background: var(--bg-primary);
  border: 1px solid var(--border-secondary);
  border-radius: 8px;
  padding: 30px;
  min-height: 400px;
}

.markdown-source {
  background: var(--bg-primary);
  border: 1px solid var(--border-secondary);
  border-radius: 8px;
  padding: 20px;
}

.markdown-source .el-textarea {
  font-family: 'Consolas', 'Monaco', monospace;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .test-content {
    padding: 0 10px;
  }

  .test-section {
    padding: 20px;
  }

  .content-selector {
    flex-direction: column;
  }

  .content-selector .el-button {
    width: 100%;
  }
}
</style>