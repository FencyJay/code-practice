import request from './request'

// 用户注册
export function register(data) {
  return request({
    url: '/api/user/register',
    method: 'POST',
    data,
  })
}

// 用户登录
export function login(data) {
  return request({
    url: '/api/user/login',
    method: 'POST',
    data,
  })
}

// 用户注销
export function logout() {
  return request({
    url: '/api/user/logout',
    method: 'POST',
  })
}

// 获取当前用户信息
export function getCurrentUser() {
  return request({
    url: '/api/user/current',
    method: 'GET',
  })
}

// 获取用户排行榜
export function getUserRanking(limit = 30) {
  return request({
    url: '/api/user/ranking',
    method: 'GET',
    params: {
      limit
    }
  })
}
