export type WorkplaceTotal = {
  project: number
  access: number
  todo: number
}

// 资产统计数
export type Asset = {
  totalAssets: number
  totalLiabilities: number
  netWorth: number
}

export type Dynamic = {
  msg: string
  time: Date | number | string
}

export type Team = {
  name: string
  icon: string
}

export type RadarData = {
  personal: number
  team: number
  max: number
  name: string
}
