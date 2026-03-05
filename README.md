# 程序员练兵场

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen)
![Vue](https://img.shields.io/badge/Vue-3.3.4-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)
![License](https://img.shields.io/badge/License-MIT-green)

**一个基于AI技术的编程练习平台，采用古战场主题，让编程学习充满乐趣**

</div>

## 📖 项目简介

程序员练兵场将编程练习融入古战场主题。用户扮演将军，通过解题提升“俸禄”，AI 智能出题，提供个性化挑战。

👉 [线上体验地址](https://coder.fency.asia)

## ✨ 功能特性

- ⚔️ 古战场主题化学习体验
- 🤖 AI 智能出题（基于 LangChain4j + 通义千问）
- 💰 俸禄系统 & 排行榜
- 📜 征战史料（学习历史）
- 🎯 多技术方向（前端、后端、AI、游戏开发等）

## 📂 项目结构

```
coder-practice
├── coder-practice-backend   # 后端 (Spring Boot)
├   └── sql                   # 数据库建表语句
├── coder-practice-frontend  # 前端 (Vue 3 + Vite)

```

## 🚀 快速开始

### 环境要求

- **JDK**: 21+
- **Node.js**: 16+
- **MySQL**: 8.0+
- **Maven**: 3.6+

### 1. 克隆项目

```
git clone https://github.com/FencyJay/coder-practice.git
cd coder-practice
```

### 2. 数据库配置

导入 `sql` 目录下的脚本，创建数据库。

### 3. 启动后端

```
cd coder-practice-backend

# 编辑配置文件 application-local.yml

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/coder_practice?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: coder_user
    password: your_password

langchain4j:
  community:
    dashscope:
      chat-model:
        api-key: your_ai_api_key
        model-name: qwen3-coder-plus
        temperature: 0.7
        max-tokens: 4096
        log-requests: true
        log-responses: true

sentry:
  dsn: your_sentry_dsn
  send-default-pii: true

# 启动服务
mvn spring-boot:run
```

后端服务运行在 http://localhost:8001。

### 4. 启动前端

```
cd coder-practice-frontend

# 安装依赖并启动
npm install
npm run dev
```

前端运行在 http://localhost:5173。

## 📦 部署方式

支持 **Docker Compose 一键部署** 或 **传统 Nginx + Jar 包部署**（详见 docs/deploy.md）。

## 📏 开发规范

- 后端：分层架构（Controller → Service → Mapper），统一异常处理，MyBatis Plus
- 前端：Vue 3 + Pinia，组件单一职责，ESLint + Prettier 格式化

## 🔁 CI（持续集成）说明

本项目使用 GitHub Actions 做自动化检查，工作流文件位于：`\.github/workflows/ci.yml`。

触发时机：

- push 到 `master` 或 `work` 分支
- 提交面向 `master` 或 `work` 的 Pull Request
- 在 GitHub Actions 页面手动触发（workflow_dispatch）

执行内容：

- 后端：Java 21 环境下执行 `mvn -B test -Dspring.profiles.active=test`（使用测试配置与Mock AI服务）
- 前端：Node 20 环境下执行 `npm ci`、`npm run lint`、`npm run build`

CI 的价值：

- 提交后自动验证，降低“本地可跑、线上失败”的风险
- PR 合并前提前发现问题，保障主分支稳定
- 让团队协作更标准化，减少重复手工检查
- CI 触发测试：仅修改文档并 push 到 `work` 分支，也会触发该工作流（当前未配置 paths 过滤）
