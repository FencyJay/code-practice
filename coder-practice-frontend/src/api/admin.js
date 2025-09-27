import request from './request'

/**
 * 管理员相关API
 */

// 分页获取关卡列表（支持筛选）
export const getLevelsByPage = (params) => {
  return request({
    url: '/api/admin/levels',
    method: 'get',
    params
  })
}

// 获取所有关卡（按优先级排序）- 保持兼容性
export const getAllLevels = () => {
  return request({
    url: '/api/admin/levels/all',
    method: 'get'
  })
}

// 添加关卡
export const addLevel = (data) => {
  return request({
    url: '/api/admin/levels',
    method: 'post',
    data
  })
}

// 更新关卡
export const updateLevel = (data) => {
  return request({
    url: '/api/admin/levels',
    method: 'put',
    data
  })
}

// 删除关卡
export const deleteLevel = (levelId) => {
  return request({
    url: `/api/admin/levels/${levelId}`,
    method: 'delete'
  })
}

// 设置精选关卡（优先级自动99）
export const setFeaturedLevel = (levelId) => {
  return request({
    url: `/api/admin/levels/${levelId}/featured`,
    method: 'post'
  })
}

// 取消精选关卡
export const cancelFeaturedLevel = (levelId) => {
  return request({
    url: `/api/admin/levels/${levelId}/cancel-featured`,
    method: 'post'
  })
}

// 设置用户角色
export const setUserRole = (userId, userRole) => {
  return request({
    url: `/api/admin/users/${userId}/role`,
    method: 'post',
    params: { userRole }
  })
}