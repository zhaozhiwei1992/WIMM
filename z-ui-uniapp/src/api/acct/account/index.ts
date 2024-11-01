import request from '@/config/axios'
import { AccountVO } from './types'

export const saveApi = (data: Partial<AccountVO>): Promise<IResponse> => {
  return request.post({ url: '/acct/account', data })
}

export const getTableListApi = (params: any): Promise<IResponse> => {
  params = { ...params, page: params.pageIndex, size: params.pageSize }
  return request.get({ url: '/acct/voucher/detail', params })
}

// 批量删除
export const delTableListApi = (ids: string[] | number[]): Promise<string> => {
  return request.delete({ url: '/acct/voucher/detail', data: ids })
}
