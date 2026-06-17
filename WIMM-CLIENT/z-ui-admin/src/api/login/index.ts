import request from '@/config/axios'
import type { RegisterVO, UserType } from './types'

interface RoleParams {
  roleName: string
}

// 请求时得看下是否自动增加了
// 无状态验证码方案：getImgCodeApi 内部缓存后端下发的 captchaToken，
// loginApi 提交时自动带上，组件层无感（跨域/反代不再依赖 session cookie）。
let captchaToken = ''

export const loginApi = (data: UserType): Promise<UserType> => {
  return request.post({ url: '/system/login', data: { ...data, captchaToken } })
  // 等价
  // const options = { data: data }
  // return request.post({ url: '/system/login', ...options })
}

export const loginOutApi = (): Promise<string> => {
  return request.get({ url: '/system/loginOut' })
}

export const getUserListApi = ({ params }: AxiosConfig) => {
  return request.get<{
    code: string
    data: {
      list: UserType[]
      total: number
    }
  }>({ url: '/system/users', params })
}

export const getAdminRoleApi = (params: RoleParams): Promise<AppCustomRouteRecordRaw[]> => {
  return request.get({ url: '/system/role/list', params })
}

export const getTestRoleApi = (params: RoleParams): Promise<string[]> => {
  return request.get({ url: '/system/role/list', params })
}

export const getMenuRouteListApi = (params: RoleParams): Promise<AppCustomRouteRecordRaw[]> => {
  return request.get({ url: '/system/menus/route', params })
}

// 获取验证码，返回 base64 图片串（后端实际返回 {img, captchaToken}，此处缓存 token 并只透出 img）
export const getImgCodeApi = async (): Promise<string> => {
  const res = await request.get({ url: '/captcha/numCode' })
  captchaToken = res?.captchaToken || ''
  return res?.img || ''
}

export const registerApi = (data: RegisterVO): Promise<RegisterVO> => {
  return request.post({ url: '/system/register', data })
}
