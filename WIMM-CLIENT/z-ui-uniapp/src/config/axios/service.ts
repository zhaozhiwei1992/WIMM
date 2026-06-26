import qs from 'qs'

import { config } from './config'

const { result_code, default_headers, request_timeout } = config

const VITE_SERVER_URL = import.meta.env.VITE_SERVER_URL || 'https://19921514.xyz/wimm'

export const PATH_URL = `${VITE_SERVER_URL}/api`

// AxiosRequestConfig 风格的入参, 业务层沿用 { url, method, params, data, headersType, responseType }
export interface UniRequestConfig {
  url: string
  method?: AxiosMethod
  params?: Record<string, any>
  data?: any
  headersType?: string
  responseType?: AxiosResponseType
}

// 统一请求函数: 底层基于 uni.request
// - header 是普通对象, 小程序 wx.request 认, 解决 "wx.request header must be an object"
// - token 从本地存储取, 挂到 Authorization
// - 响应返回 HTTP body 本身(对齐原 axios 封装的 return response.data)
export const request = (option: UniRequestConfig): Promise<any> => {
  const { url, method = 'get', params, data, headersType, responseType } = option

  // header 普通对象
  const header: Record<string, string> = {
    'Content-Type': headersType || default_headers
  }
  const token = uni.getStorageSync('token')
  if (token) {
    header['Authorization'] = token
  }

  // GET 参数拼到 url query 上(对齐原实现)
  let finalUrl = `${PATH_URL}${url}`
  if (method.toLowerCase() === 'get' && params) {
    const query = qs.stringify(params)
    if (query) {
      finalUrl += (url.includes('?') ? '&' : '?') + query
    }
  }

  // 表单提交: qs 序列化 data(对齐原实现)
  let finalData = data
  if (
    method.toLowerCase() === 'post' &&
    header['Content-Type'] === 'application/x-www-form-urlencoded' &&
    data !== undefined && data !== null
  ) {
    finalData = qs.stringify(data)
  }

  // 文件流场景标记
  const isFileStream =
    responseType === 'blob' || responseType === 'arraybuffer'

  return new Promise((resolve, reject) => {
    uni.request({
      url: finalUrl,
      method: method.toUpperCase() as
        | 'GET' | 'POST' | 'PUT' | 'DELETE',
      header,
      data: finalData,
      timeout: request_timeout,
      responseType: responseType as any,
      success: (res) => {
        const statusCode = res.statusCode
        // 401 未授权, 清 token 并跳登录页(对齐原拦截器)
        if (statusCode === 401) {
          uni.removeStorageSync('token')
          uni.removeStorageSync('username')
          uni.redirectTo({ url: '/pages/login/login' })
          reject(new Error('未授权'))
          return
        }
        // 文件流直接返回整对象
        if (isFileStream) {
          resolve(res)
          return
        }
        // 成功返回 HTTP body 本身
        if (statusCode === result_code) {
          resolve(res.data)
          return
        }
        reject(new Error(`请求失败, 状态码: ${statusCode}`))
      },
      fail: (err) => {
        console.log('[uni.request] fail', err)
        reject(new Error(err.errMsg || '网络请求失败'))
      }
    })
  })
}
