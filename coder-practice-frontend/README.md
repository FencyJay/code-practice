# 程序员技术练兵场 - 前端

一个基于 Vue 3 + Element Plus 的程序员技术闯关游戏前端项目。

## 🚀 快速开始

### 环境要求

- Node.js >= 16
- npm >= 8

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

项目将在 http://localhost:5174 启动

### 构建生产版本

```bash
npm run build
```

### 代码检查和格式化

```bash
npm run lint
npm run format
```

## 🎯 功能特性

### 用户功能
- ✅ 用户注册和登录
- ✅ 薪资管理
- ✅ 个人信息展示

### 关卡系统
- ✅ AI 生成个性化关卡
- ✅ 基于薪资的难度匹配
- ✅ 拖拽式答题交互
- ✅ 实时反馈和评分

### 结果展示
- ✅ 详细的成绩分析
- ✅ 薪资变化反馈
- ✅ AI 评价和建议
- ✅ 标准答案解析

### 历史记录
- ✅ 完整的闯关历史
- ✅ 数据统计和图表
- ✅ 筛选和搜索功能

## 🛠️ 技术栈

- **前端框架**: Vue 3 (Composition API)
- **UI 组件**: Element Plus
- **状态管理**: Pinia
- **路由管理**: Vue Router 4
- **HTTP 请求**: Axios
- **构建工具**: Vite
- **代码规范**: ESLint + Prettier

## 📁 项目结构

```
src/
├── api/           # API 接口封装
├── assets/        # 静态资源
├── components/    # 公共组件
├── composables/   # 组合式函数
├── router/        # 路由配置
├── store/         # 状态管理
├── styles/        # 全局样式
├── utils/         # 工具函数
├── views/         # 页面组件
├── App.vue        # 根组件
└── main.js        # 入口文件
```

## 🔧 开发规范

### 命名规范
- 组件文件名：PascalCase (如 `UserLogin.vue`)
- 路由路径：kebab-case (如 `/user-profile`)
- API 函数：camelCase (如 `getUserInfo`)

### 代码风格
- 使用 ESLint + Prettier 进行代码格式化
- 统一使用单引号
- 不使用分号
- 缩进使用 2 个空格

### 组件开发
- 必须使用 `<script setup>` 语法
- 业务逻辑抽离到 composables
- 公共样式抽离到 styles 目录

## 🎮 使用说明

1. **注册/登录**: 首次使用需要注册账号并登录
2. **生成关卡**: 基于当前薪资生成个性化技术关卡
3. **答题挑战**: 通过拖拽或点击选择技术选项
4. **查看结果**: 获得详细的评分和反馈
5. **历史记录**: 查看成长轨迹和薪资变化

## 🔄 API 接口

后端 API 文档请参考 `.cladue/default.md`

### 主要接口
- 用户认证: `/api/user/*`
- 关卡管理: `/api/level/*`
- 答题系统: `/api/user-level/*`

## 🐛 问题排查

### 常见问题
1. **端口占用**: 修改 `vite.config.js` 中的端口号
2. **API 请求失败**: 检查后端服务是否在 8001 端口运行
3. **依赖安装失败**: 尝试使用 `npm install --registry https://registry.npmmirror.com`

### 开发环境配置
确保后端服务运行在 http://localhost:8001

## 📄 许可证

MIT License