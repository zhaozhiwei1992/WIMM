import request from '@/config/axios'
import type { AssetVO, PieDataVO, LineDataVO } from './types'

export const getAssetsApi = (): Promise<AssetVO> => {
  return request.get({ url: '/acct/workplace/asset' })
}

// 按月获取收入支出数据
export const getMonthlyIncExpDataApi = (): Promise<LineDataVO[]> => {
  return request.get({ url: '/acct/workplace/monthlyIncExpData' })
}

// 获取支出类别饼图
export const getExpPieDataApi = (): Promise<PieDataVO[]> => {
  return request.get({ url: '/acct/workplace/expPieData' })
}

// 获取收入类别饼图
export const getIncPieDataApi = (): Promise<PieDataVO[]> => {
  return request.get({ url: '/acct/workplace/incPieData' })
}
