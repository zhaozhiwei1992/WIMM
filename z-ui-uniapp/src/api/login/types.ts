export type UserLoginType = {
  username: string
  password: string
}

export type UserType = {
  username: string
  password: string
  role: string
  roleId: string
  permissions: string | string[]
  token: string
}

export interface SmsCodeVO {
  mobile: string
  scene: string
}
