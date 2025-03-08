import type { AssetVO, PieDataVO, LineDataVO } from './types'
import { workplaceService } from '@/config/db/workplace'

export const getAssetsApi = (): Promise<AssetVO> => {
  return workplaceService.getAssets()
}

// 按月获取收入支出数据
export const getMonthlyIncExpDataApi = (): Promise<LineDataVO[]> => {
  return workplaceService.getLineData()
}

// 获取支出类别饼图
export const getExpPieDataApi = (): Promise<PieDataVO[]> => {
  return workplaceService.getPieData()
}

// 获取收入类别饼图
export const getIncPieDataApi = (): Promise<PieDataVO[]> => {
  return workplaceService.getPieData()
}
