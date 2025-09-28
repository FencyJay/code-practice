import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'

// 创建axios实例
const request = axios.create({
  // 开发环境和生产环境都使用完整URL
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 60000,
  withCredentials: true, // 跨域请求时携带 cookie
  headers: {
    'Content-Type': 'application/json',
  },
})

// 需要验证登录态的接口
const authRequiredUrls = [
  '/api/level/generate',
  '/api/user-level/submit',
  '/api/user-level/history',
  '/api/admin/',
]

// 检查URL是否需要登录验证
function needsAuthCheck(url) {
  return authRequiredUrls.some((authUrl) => url.includes(authUrl))
}

// 请求拦截器
request.interceptors.request.use(
  async (config) => {
    // Cookie/Session 认证方式，浏览器会自动携带 cookie
    // 无需手动添加 token

    // 如果是需要登录验证的接口，先验证登录态
    if (needsAuthCheck(config.url)) {
      const userStore = useUserStore()

      // 检查是否有用户信息，如果没有再验证
      if (!userStore.userInfo) {
        try {
          // 验证当前用户登录态
          await userStore.fetchCurrentUser()
        } catch (error) {
          // Session失效，清除用户状态并跳转到登录页
          userStore.clearUserState()

          // 显示提示信息
          const operation = getOperationName(config.url)
          ElMessage.warning(`请先登录后再${operation}`)

          // 跳转到登录页，并携带当前页面路径作为redirect参数
          if (!window.location.pathname.includes('/login')) {
            const currentPath = window.location.pathname + window.location.search
            // 使用 setTimeout 确保 Message 显示后再跳转
            setTimeout(() => {
              window.location.href = `/login?redirect=${encodeURIComponent(currentPath)}`
            }, 100)
          }

          // 中止当前请求
          return Promise.reject(new Error('登录态验证失败'))
        }
      }
    }

    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 根据URL获取操作名称
function getOperationName(url) {
  if (url.includes('/api/level/generate')) return '生成关卡'
  if (url.includes('/api/user-level/submit')) return '提交答案'
  if (url.includes('/api/user-level/history')) return '查看历史记录'
  if (url.includes('/api/user/current')) return '获取用户信息'
  if (url.includes('/api/admin/')) return '管理操作'
  return '进行操作'
}

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    const { data } = response
    if (data.code === 0) {
      return data.data
    } else if (data.code === 40300) {
      // 无管理员权限
      ElMessage.error('无管理员权限')
      return Promise.reject(new Error('无管理员权限'))
    } else if (data.code === 40100) {
      useUserStore().clearUserState()
      // 未登录
      ElMessage.error('请先登录')
      return Promise.reject(new Error('请先登录'))
    }else if (data.code === 40000){
      ElMessage.error(data.message || '操作失败')
      return Promise.reject(new Error(data.message || '操作失败'))
    }else {

      return Promise.reject(new Error(data.message || '操作失败'))
    }
  },
  (error) => {
    if (error.response?.status === 403) {
      ElMessage.error('无管理员权限')
    } else if (error.response?.status === 401) {
      ElMessage.error('请先登录')
    } else {
      ElMessage.error('网络错误，请稍后重试')
    }
    return Promise.reject(error)
  }
)


export default request
