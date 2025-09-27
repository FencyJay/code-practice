<template>
  <div class="login-battlefield">
    <!-- 左侧装饰区 -->
    <div class="battlefield-decoration">
      <div class="ancient-text">
        <h1 class="battle-title">⚔️ 技术练兵场</h1>
        <p class="battle-subtitle">兵者，诡道也</p>
        <div class="battle-quotes">
          <p>"知彼知己，百战不殆"</p>
          <p>"上兵伐谋，其次伐交"</p>
          <p>"不战而屈人之兵，善之善者也"</p>
        </div>
      </div>
    </div>

    <!-- 右侧登录区 -->
    <div class="login-arena">
      <div class="battle-card">
        <div class="arena-header">
          <h2 class="arena-title">🛡️ 将军认证</h2>
          <p class="arena-subtitle">请出示你的兵符，确认身份</p>
        </div>

        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          label-width="0"
          size="large"
          class="battle-form"
        >
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="请输入将军名号"
              prefix-icon="User"
              class="battle-input"
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入兵符密码"
              prefix-icon="Lock"
              show-password
              @keyup.enter="handleLogin"
              class="battle-input"
            />
          </el-form-item>

          <el-form-item>
            <button
              class="battle-button"
              :disabled="userStore.isLoading"
              @click="handleLogin"
            >
              <span v-if="userStore.isLoading">🚩 验证中...</span>
              <span v-else>⚔️ 进入军营</span>
            </button>
          </el-form-item>
        </el-form>

        <div class="register-link">
          <span class="link-text">尚未加入军营？</span>
          <router-link to="/register" class="battle-link">
            📝 立即注册
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref()

const loginForm = reactive({
  username: '',
  password: '',
})

const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' },
  ],
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  try {
    await loginFormRef.value.validate()
    const success = await userStore.userLogin(loginForm)

    if (success) {
      // 获取重定向地址，如果没有则默认跳转到首页
      const redirect = router.currentRoute.value.query.redirect || '/home'
      router.push(redirect)
    }
  } catch (error) {
    console.error('登录失败:', error)
  }
}
</script>

<style scoped>
/* 引入战场主题样式 */
@import '@/styles/battlefield-theme.css';

.login-battlefield {
  display: flex;
  min-height: 100vh;
  position: relative;
}

/* 左侧装饰区 */
.battlefield-decoration {
  flex: 1;
  background: linear-gradient(135deg, var(--deep-brown) 0%, var(--primary-brown) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  position: relative;
  overflow: hidden;
}

.battlefield-decoration::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image:
    repeating-linear-gradient(
      45deg,
      transparent,
      transparent 35px,
      rgba(218, 165, 32, 0.1) 35px,
      rgba(218, 165, 32, 0.1) 70px
    );
  pointer-events: none;
}

.ancient-text {
  text-align: center;
  color: var(--warm-beige);
  max-width: 500px;
  z-index: 1;
}

.battle-title {
  font-family: 'Noto Serif SC', serif;
  font-size: 3rem;
  font-weight: bold;
  margin-bottom: 16px;
  text-shadow: 3px 3px 6px rgba(0, 0, 0, 0.5);
  color: var(--battle-gold);
  letter-spacing: 2px;
}

.battle-subtitle {
  font-family: 'Noto Serif SC', serif;
  font-size: 1.5rem;
  font-style: italic;
  margin-bottom: 40px;
  opacity: 0.9;
}

.battle-quotes p {
  font-family: 'Noto Serif SC', serif;
  font-size: 1.1rem;
  margin: 16px 0;
  opacity: 0.8;
  line-height: 1.6;
  border-left: 3px solid var(--battle-gold);
  padding-left: 16px;
  text-align: left;
}

/* 右侧登录区 */
.login-arena {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  background: var(--bg-primary);
}

.battle-card {
  width: 100%;
  max-width: 450px;
  background: var(--bg-card);
  border: 2px solid var(--border-primary);
  border-radius: 0;
  padding: 40px;
  box-shadow: 0 8px 32px var(--shadow-heavy);
  position: relative;
}

.battle-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, var(--battle-gold), var(--sword-silver), var(--battle-gold));
}

.arena-header {
  text-align: center;
  margin-bottom: 32px;
}

.arena-title {
  font-family: 'Noto Serif SC', serif;
  font-size: 2rem;
  font-weight: bold;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.arena-subtitle {
  color: var(--text-secondary);
  font-size: 1rem;
  margin: 0;
}

/* 表单样式 */
.battle-form {
  margin-top: 24px;
}

.battle-input {
  margin-bottom: 16px;
}

.battle-input :deep(.el-input__wrapper) {
  background: var(--bg-primary);
  border: 1px solid var(--border-primary);
  border-radius: 0;
  box-shadow: none !important;
  transition: all 0.3s ease;
}

.battle-input :deep(.el-input__wrapper:hover) {
  border-color: var(--battle-gold);
  box-shadow: 0 0 0 1px var(--battle-gold) !important;
}

.battle-input :deep(.el-input__wrapper.is-focus) {
  border-color: var(--battle-gold);
  box-shadow: 0 0 0 1px var(--battle-gold) !important;
}

.battle-input :deep(.el-input__inner) {
  color: var(--text-primary);
  font-family: 'Inter', sans-serif;
}

.battle-input :deep(.el-input__inner::placeholder) {
  color: var(--text-secondary);
}

/* 登录按钮 */
.battle-button {
  width: 100%;
  background: linear-gradient(135deg, var(--primary-brown) 0%, var(--secondary-brown) 100%);
  color: white;
  border: 2px solid var(--border-primary);
  border-radius: 0;
  padding: 16px;
  font-size: 1.1rem;
  font-weight: bold;
  text-transform: uppercase;
  letter-spacing: 1px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 8px var(--shadow-light);
  position: relative;
  overflow: hidden;
}

.battle-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(218, 165, 32, 0.4), transparent);
  transition: left 0.6s ease;
}

.battle-button:hover::before {
  left: 100%;
}

.battle-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px var(--shadow-heavy);
  background: linear-gradient(135deg, var(--secondary-brown) 0%, var(--light-brown) 100%);
}

.battle-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

/* 注册链接 */
.register-link {
  text-align: center;
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid var(--border-secondary);
}

.link-text {
  color: var(--text-secondary);
  margin-right: 8px;
}

.battle-link {
  color: var(--battle-gold);
  text-decoration: none;
  font-weight: bold;
  padding: 4px 8px;
  border: 1px solid transparent;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.battle-link:hover {
  color: var(--light-brown);
  border-color: var(--battle-gold);
  background: rgba(218, 165, 32, 0.1);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-battlefield {
    flex-direction: column;
  }

  .battlefield-decoration {
    flex: none;
    min-height: 300px;
    padding: 20px;
  }

  .battle-title {
    font-size: 2rem;
  }

  .battle-subtitle {
    font-size: 1.2rem;
  }

  .battle-quotes p {
    font-size: 1rem;
  }

  .login-arena {
    flex: none;
    padding: 20px;
  }

  .battle-card {
    padding: 24px;
  }

  .arena-title {
    font-size: 1.5rem;
  }
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.battle-card {
  animation: fadeInUp 0.8s ease-out;
}

.battlefield-decoration {
  animation: fadeInUp 0.8s ease-out 0.2s both;
}
</style>
