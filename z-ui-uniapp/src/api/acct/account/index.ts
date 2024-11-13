import request from '@/config/axios'
import type { AccountVO } from './types'
import DatabaseHelper from '@/plugins/sqlite'

export const saveApi = (data: Partial<AccountVO>): Promise<IResponse> => {
  // #ifdef APP-PLUS
  console.log('保存到数据库')
  DatabaseHelper.insert('voucher_detail', data)
  // #endif
  //#ifndef APP-PLUS
  console.log('保存到服务器')
  return request.post({ url: '/acct/account', data })
  // #endif
}

export const getTableListApi = (params: any): Promise<IResponse> => {
  params = { ...params, page: params.pageIndex, size: params.pageSize }
  return request.get({ url: '/acct/voucher/detail', params })
}

// 批量删除
export const delTableListApi = (ids: string[] | number[]): Promise<string> => {
  return request.delete({ url: '/acct/voucher/detail', data: ids })
}
