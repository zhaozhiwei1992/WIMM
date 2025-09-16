import request from '@/config/axios'
import { ComponentOptions } from '@/types/components'
import type { AccountClsVO } from './types'

export const getTableListApi = (params: any): Promise<IResponse> => {
  params = { ...params, page: params.pageIndex, size: params.pageSize }
  // 这里直接写 params等价与 params: {page:xx, size:xx}
  return request.get({ url: '/acct/account-cls', params })
}

export const saveTableApi = (data: Partial<AccountClsVO>): Promise<IResponse> => {
  return request.post({ url: '/acct/account-cls', data })
}

// 获取指定数据详情
export const getMenuDetApi = (id: string): Promise<AccountClsVO> => {
  // url?id=xx
  return request.get({ url: '/acct/account-cls/' + id })
}

// 批量删除
export const delTableListApi = (ids: string[] | number[]): Promise<IResponse> => {
  // 适配后端, 直接使用RequestBody接收数据, 不能使用{ids}, 这种表示data里的是个map对象,key为ids
  return request.delete({ url: '/acct/account-cls', data: ids })
}

// 查询支出科目树
export const getAccountClsSelect = async (): Promise<ComponentOptions[]> => {
  return await request.get({ url: '/acct/account-cls/select' })
}
