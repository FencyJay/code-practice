import request from './request'

// 生成关卡
export function generateLevel(salary, direction = null) {
  return request({
    url: '/api/level/generate',
    method: 'POST',
    params: { salary, direction },
  })
}

// 根据ID获取关卡详情
export function getLevelById(id) {
  return request({
    url: `/api/level/${id}`,
    method: 'GET',
  })
}

// 获取关卡列表
export function getLevelList(params) {
  return request({
    url: '/api/level/list',
    method: 'GET',
    params,
  })
}
