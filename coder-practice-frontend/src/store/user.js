import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login, register, logout, getCurrentUser } from '@/api/user'
import { ElMessage } from 'element-plus'

export const useUserStore = defineStore('user', () => {
  // 安全地从localStorage读取用户信息
  const getUserInfoFromStorage = () => {
    try {
      const stored = localStorage.getItem('userInfo')
      if (stored) {
        return JSON.parse(stored)
      }
      return null
    } catch (error) {
      console.error('解析用户信息失败:', error)
      localStorage.removeItem('userInfo')
      return null
    }
  }

  const userInfo = ref(getUserInfoFromStorage())
  const isLoading = ref(false)

  // 基于用户信息判断登录状态
  const isLoggedIn = computed(() => !!userInfo.value)

  // 用户登录
  const userLogin = async (loginData) => {
    isLoading.value = true
    try {
      const result = await login(loginData)
      userInfo.value = result
      localStorage.setItem('userInfo', JSON.stringify(result))
      return true
    } catch (error) {
     //  ElMessage.error(error.message || '登录失败')
      return false
    } finally {
      isLoading.value = false
    }
  }

  // 用户注册
  const userRegister = async (registerData) => {
    isLoading.value = true
    try {
      await register(registerData)
      ElMessage.success('注册成功，请登录')
      return true
    } catch (error) {
       ElMessage.error(error.message || '注册失败')
      return false
    } finally {
      isLoading.value = false
    }
  }

  // 用户注销
  const userLogout = async () => {
    try {
      await logout()
      clearUserState()
      ElMessage.success('已退出登录')
    } catch (error) {
      // 即使后端注销失败，也清空本地状态
      clearUserState()
      ElMessage.success('已退出登录')
    }
  }

  // 清除用户状态（用于登录态失效时）
  const clearUserState = () => {
    userInfo.value = null
    localStorage.removeItem('userInfo')
  }

  // 获取当前用户信息
  const fetchCurrentUser = async () => {
    try {
      const result = await getCurrentUser()
      if (result) {
        userInfo.value = result
        localStorage.setItem('userInfo', JSON.stringify(result))
      } else {
        clearUserState()
      }
    } catch (error) {
      // Session失效，清除登录状态
      // 注意：这里不调用 userLogout()，因为会显示成功消息
      clearUserState()
      throw error // 重新抛出错误，让调用者处理
    }
  }

  // 更新用户薪资
  const updateUserSalary = (salaryChange) => {
    if (userInfo.value) {
      userInfo.value.salary += salaryChange
      localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
    }
  }

  return {
    userInfo,
    isLoading,
    isLoggedIn,
    userLogin,
    userRegister,
    userLogout,
    clearUserState,
    fetchCurrentUser,
    updateUserSalary,
  }
})
