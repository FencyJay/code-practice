import request from './request'

// 提交答案
export function submitAnswer(data) {
  return request({
    url: '/api/user-level/submit',
    method: 'POST',
    data,
  })
}

// 获取用户闯关历史
export function getUserLevelHistory(params) {
  return request({
    url: '/api/user-level/history',
    method: 'GET',
    params,
  })
}

// 根据ID获取用户闯关结果详情
export function getUserLevelResult(id) {
  return request({
    url: `/api/user-level/${id}`,
    method: 'GET',
  })
}
