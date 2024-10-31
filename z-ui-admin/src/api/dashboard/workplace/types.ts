// 资产统计数
export type AssetVO = {
  totalAssets: number
  totalLiabilities: number
  netWorth: number
}

export type RadarData = {
  personal: number
  team: number
  max: number
  name: string
}

// 饼图
export type PieDataVO = {
  value: number
  name: string
}

// 折线图
export type LineDataVO = {
  name: string
  line1: number
  line2: number
}
