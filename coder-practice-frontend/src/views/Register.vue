<template>
  <div class="register-battlefield">
    <!-- 右侧装饰区 -->
    <div class="battlefield-decoration">
      <div class="ancient-text">
        <h1 class="battle-title">⚔️ 加入军营</h1>
        <p class="battle-subtitle">养兵千日，用兵一时</p>
        <div class="battle-quotes">
          <p>"工欲善其事，必先利其器"</p>
          <p>"不积跬步，无以至千里"</p>
          <p>"天将降大任于斯人也，必先苦其心志"</p>
        </div>
      </div>
    </div>

    <!-- 左侧注册区 -->
    <div class="register-arena">
      <div class="battle-card">
        <div class="arena-header">
          <h2 class="arena-title">📝 将军登记</h2>
          <p class="arena-subtitle">填写你的信息，获得兵符认证</p>
        </div>

        <el-form
          ref="registerFormRef"
          :model="registerForm"
          :rules="registerRules"
          label-width="0"
          size="large"
          class="battle-form"
        >
          <el-form-item prop="username">
            <el-input
              v-model="registerForm.username"
              placeholder="请输入将军名号"
              prefix-icon="User"
              class="battle-input"
            />
          </el-form-item>

          <el-form-item prop="nickname">
            <el-input
              v-model="registerForm.nickname"
              placeholder="请输入战名（可选）"
              prefix-icon="UserFilled"
              class="battle-input"
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="请设置兵符密码"
              prefix-icon="Lock"
              show-password
              class="battle-input"
            />
          </el-form-item>

          <el-form-item prop="confirmPassword">
            <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="请确认兵符密码"
              prefix-icon="Lock"
              show-password
              @keyup.enter="handleRegister"
              class="battle-input"
            />
          </el-form-item>

          <el-form-item>
            <button
              class="battle-button"
              :disabled="userStore.isLoading"
              @click="handleRegister"
            >
              <span v-if="userStore.isLoading">🚩 登记中...</span>
              <span v-else>⚔️ 领取兵符</span>
            </button>
          </el-form-item>
        </el-form>

        <div class="login-link">
          <span class="link-text">已有兵符？</span>
          <router-link to="/login" class="battle-link">
            🛡️ 立即登录
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

const registerFormRef = ref()

const registerForm = reactive({
  username: '',
  nickname: '',
  password: '',
  confirmPassword: '',
})

const validatePassword = (rule, value, callback) => {
  if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' },
  ],
  nickname: [{ max: 20, message: '昵称长度不能超过 20 个字符', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validatePassword, trigger: 'blur' },
  ],
}

const handleRegister = async () => {
  if (!registerFormRef.value) return

  try {
    await registerFormRef.value.validate()
    const success = await userStore.userRegister(registerForm)

    if (success) {
      router.push('/login')
    }
  } catch (error) {
    console.error('注册失败:', error)
  }
}
</script>

<style scoped>
/* 引入战场主题样式 */
@import '@/styles/battlefield-theme.css';

.register-battlefield {
  display: flex;
  min-height: 100vh;
  position: relative;
}

/* 右侧装饰区 */
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
      -45deg,
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
  border-right: 3px solid var(--battle-gold);
  padding-right: 16px;
  text-align: right;
}

/* 左侧注册区 */
.register-arena {
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

/* 注册按钮 */
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

/* 登录链接 */
.login-link {
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
  .register-battlefield {
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

  .register-arena {
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
