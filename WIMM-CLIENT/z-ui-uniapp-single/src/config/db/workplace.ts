import { dbService } from './index';
import type { AssetVO, PieDataVO, LineDataVO, RadarData } from '@/api/workplace/types';

/**
 * 工作台服务
 */
export class WorkplaceService {
  /**
   * 获取资产数据
   */
  async getAssets(): Promise<AssetVO> {
    try {
      const assets = await dbService.selectSql('SELECT * FROM assets LIMIT 1');
      
      if (assets && assets.length > 0) {
        return {
          totalAssets: assets[0].totalAssets,
          totalLiabilities: assets[0].totalLiabilities,
          netWorth: assets[0].netWorth
        };
      } else {
        // 如果没有数据，返回默认值
        return {
          totalAssets: 0,
          totalLiabilities: 0,
          netWorth: 0
        };
      }
    } catch (error) {
      console.error('获取资产数据失败', error);
      throw error;
    }
  }

  /**
   * 获取饼图数据
   */
  async getPieData(): Promise<PieDataVO[]> {
    try {
      const pieData = await dbService.selectSql('SELECT * FROM pie_data');
      
      return pieData.map(item => ({
        value: item.value,
        name: item.name
      }));
    } catch (error) {
      console.error('获取饼图数据失败', error);
      throw error;
    }
  }

  /**
   * 获取折线图数据
   */
  async getLineData(): Promise<LineDataVO[]> {
    try {
      const lineData = await dbService.selectSql('SELECT * FROM line_data');
      
      return lineData.map(item => ({
        name: item.name,
        line1: item.line1,
        line2: item.line2
      }));
    } catch (error) {
      console.error('获取折线图数据失败', error);
      throw error;
    }
  }

  /**
   * 获取雷达图数据
   */
  async getRadarData(): Promise<RadarData[]> {
    try {
      const radarData = await dbService.selectSql('SELECT * FROM radar_data');
      
      return radarData.map(item => ({
        personal: item.personal,
        team: item.team,
        max: item.max,
        name: item.name
      }));
    } catch (error) {
      console.error('获取雷达图数据失败', error);
      throw error;
    }
  }
}

// 导出单例
export const workplaceService = new WorkplaceService(); 