import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/home',
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('@/views/Home.vue'),
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
  },
  {
    path: '/level/:id',
    name: 'LevelDetail',
    component: () => import('@/views/LevelDetail.vue'),
  },
  {
    path: '/result/:id',
    name: 'ResultDetail',
    component: () => import('@/views/ResultDetail.vue'),
  },
  {
    path: '/history',
    name: 'History',
    component: () => import('@/views/History.vue'),
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('@/views/Admin.vue'),
  },
  {
    path: '/challenge',
    name: 'Challenge',
    component: () => import('@/views/Challenge.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 移除路由守卫，允许用户自由浏览
// 用户访问需要登录的接口时，由 Axios 拦截器处理登录验证

export default router
