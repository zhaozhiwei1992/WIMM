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

export const exportVoucherDetailApi = (params: any) => {
  request
    .get({
      url: '/acct/voucher/export',
      responseType: 'blob' // 设置响应类型为blob
    })
    .then((response) => {
      console.log('response', response)
      const blob = new Blob([response.data], {
        type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      })
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = '记账明细.xlsx' // 设置下载文件名
      link.click()
      window.URL.revokeObjectURL(url)
    })
    .catch((error) => {
      console.error('导出失败:', error)
    })
}
