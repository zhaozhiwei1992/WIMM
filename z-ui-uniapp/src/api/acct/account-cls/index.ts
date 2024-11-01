import request from '@/config/axios'
import { ComponentOptions } from '../common-types'
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
  // 单机版直接返回数据
  const accounts: ComponentOptions[] = [
    {
      id: '001',
      label: '资产',
      children: [
        { id: '001001', label: '现金' },
        { id: '001002', label: '银行' },
        { id: '001003', label: '投资' },
        { id: '001004', label: '固定资产' },
        { id: '001005', label: '其他资产' },
      ],
    },
    {
      id: '002',
      label: '负债',
      children: [
        { id: '002001', label: '贷款' },
        { id: '002002', label: '信用卡' },
        { id: '002003', label: '抵押' },
        { id: '002004', label: '其他负债' },
      ],
    },
    {
      id: '003',
      label: '收入',
      children: [
        { id: '003001', label: '工资' },
        { id: '003002', label: '投资' },
        { id: '003003', label: '其他收入' },
      ],
    },
    {
      id: '004',
      label: '支出',
      children: [
        { id: '004001', label: '衣服' },
        { id: '004002', label: '食品' },
        { id: '004003', label: '住房' },
        { id: '004004', label: '交通' },
        { id: '004005', label: '娱乐' },
        { id: '004006', label: '健身' },
        { id: '004007', label: '保险' },
        { id: '004008', label: '学习提升' },
        { id: '004009', label: '日常用品' },
        { id: '004010', label: '其他费用' },
      ],
    },
  ];
  return accounts
  // return await request.get({ url: '/acct/account-cls/select' })
}
