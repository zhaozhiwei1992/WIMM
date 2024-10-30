import request from '@/config/axios'
import type { Dynamic, Asset, RadarData, Team, WorkplaceTotal } from './types'

export const getCountApi = (): Promise<IResponse<WorkplaceTotal>> => {
  return request.get({ url: '/system/workplace/total' })
}

export const getAssetsApi = (): Promise<Asset> => {
  return request.get({ url: '/acct/workplace/assets' })
}

export const getDynamicApi = (): Promise<IResponse<Dynamic[]>> => {
  return request.get({ url: '/system/workplace/dynamic' })
}

export const getTeamApi = (): Promise<IResponse<Team[]>> => {
  return request.get({ url: '/system/workplace/team' })
}

export const getRadarApi = (): Promise<IResponse<RadarData[]>> => {
  return request.get({ url: '/system/workplace/radar' })
}
