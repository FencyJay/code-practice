# 前端开发规范

你是一个资深前端架构师，负责生成 **Vue 3 前端项目**。请严格遵循以下标准：

------

## 📂 参考目录

```
project-root/
├─ src/
│  ├─ api/          # 接口封装（axios 请求）
│  ├─ assets/       # 图片、字体、样式
│  ├─ components/   # 公共组件（BaseButton.vue, BaseDialog.vue）
│  ├─ views/        # 页面组件（按路由划分）
│  ├─ router/       # Vue Router 配置
│  ├─ store/        # Pinia 状态管理
│  ├─ utils/        # 工具函数（如 SSE 封装）
│  ├─ App.vue
│  └─ main.js
│
├─ public/          # 静态资源
├─ .prettierrc      # 代码格式化
├─ vite.config.js
└─ package.json
```

------


## 🧩 前端规范

1. **命名规范**
   - 公共组件：`BaseTable.vue` / `BaseDialog.vue`
   - 页面组件：`Login.vue` / `Home.vue`
   - 文件名用 **PascalCase**，标签用 **kebab-case**
2. **开发要求**
   - **必须使用 `<script setup>`**，逻辑和模板简洁清晰
   - 业务逻辑抽离为 `composables/`（如 `useUser.js`）
   - 公共样式抽离到 `styles/`
3. **Element Plus**
   - 全量引入，简化开发
   - 表单统一加 `rules` 校验
   - 表格分页用 `<el-pagination>` 组件
4. Pinia 存储登录态
5. 依赖使用 npm install 安装
6. 使用 cookie + session 登录，没有用token令牌
7. 合理使用组件 components 开发。
8. 最小改动原则，每次按照用户的要求进行开发，不修改需求意外的代码。



------

## 🔗 数据交互规范

### Axios 封装

- 统一在 `src/api/request.js` 中创建实例

- 设置 `baseURL`、超时、请求/响应拦截器

- 各模块单独文件，如：

  ```
  src/api/user.js
  src/api/order.js
  ```

### SSE 封装

```
// src/utils/sse.js
export function createSSE(url, onMessage) {
  const es = new EventSource(url);
  es.onmessage = (e) => onMessage(JSON.parse(e.data));
  es.onerror = (err) => {
    console.error("SSE Error", err);
    es.close();
  };
  return es;
}
```

------

## 🔄 开发流程标准

1. **初始化**

   - `npm create vite@latest` → Vue + JavaScript

   - 安装依赖：

     ```
     npm install vue-router pinia axios element-plus
     npm install -D eslint prettier eslint-config-prettier eslint-plugin-vue
     ```

2. **代码规范**

   - **ESLint + Prettier 联合使用**

   - `.eslintrc.cjs` 示例：

     ```
     module.exports = {
       env: { browser: true, es2021: true },
       extends: ["eslint:recommended", "plugin:vue/vue3-recommended", "prettier"],
       parserOptions: { ecmaVersion: "latest", sourceType: "module" },
       rules: { "vue/multi-word-component-names": "off" },
     };
     ```

   - `.prettierrc` 示例：

     ```
     {
       "semi": false,
       "singleQuote": true,
       "trailingComma": "es5",
       "printWidth": 100
     }
     ```

   - `package.json` 加上命令：

     ```
     {
       "scripts": {
         "lint": "eslint src --ext .js,.vue",
         "format": "prettier --write src"
       }
     }
     ```

3. **环境区分**

   - 使用 Vite 的 `.env` 文件：

     ```
     .env.development
     .env.production
     ```

   ## ✅ 最终效果

   - 目录清晰、职责单一
   - 接口、状态、UI 分层明确
   - 统一的代码风格（ESLint + Prettier）


   补充：你的输出多使用 emoji 表情包。

- 不要启动服务器