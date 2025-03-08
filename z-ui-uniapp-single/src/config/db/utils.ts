import { dbService } from './index';

/**
 * 数据库工具类
 */
export class DBUtils {
  /**
   * 检查表是否存在
   * @param tableName 表名
   */
  static async isTableExist(tableName: string): Promise<boolean> {
    try {
      const result = await dbService.selectSql(
        "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
        [tableName]
      );
      return result && result.length > 0;
    } catch (error) {
      console.error('检查表是否存在失败', error);
      return false;
    }
  }

  /**
   * 获取表的所有列名
   * @param tableName 表名
   */
  static async getTableColumns(tableName: string): Promise<string[]> {
    try {
      const result = await dbService.selectSql(`PRAGMA table_info(${tableName})`);
      return result.map(item => item.name);
    } catch (error) {
      console.error('获取表列名失败', error);
      return [];
    }
  }

  /**
   * 备份数据库
   * @param backupPath 备份路径
   */
  static async backupDatabase(backupPath: string): Promise<boolean> {
    try {
      // 使用plus.io API复制数据库文件
      const entry = await new Promise<any>((resolve, reject) => {
        plus.io.resolveLocalFileSystemURL('_doc/wimm.db', resolve, reject);
      });

      await new Promise<void>((resolve, reject) => {
        entry.copyTo(
          plus.io.resolveLocalFileSystemURL(backupPath, () => {}, () => {}),
          'wimm_backup.db',
          () => resolve(),
          (e) => reject(e)
        );
      });

      return true;
    } catch (error) {
      console.error('备份数据库失败', error);
      return false;
    }
  }

  /**
   * 恢复数据库
   * @param backupPath 备份文件路径
   */
  static async restoreDatabase(backupPath: string): Promise<boolean> {
    try {
      // 关闭当前数据库连接
      await dbService.closeDatabase();

      // 使用plus.io API复制备份文件到数据库位置
      const entry = await new Promise<any>((resolve, reject) => {
        plus.io.resolveLocalFileSystemURL(backupPath, resolve, reject);
      });

      await new Promise<void>((resolve, reject) => {
        entry.copyTo(
          plus.io.resolveLocalFileSystemURL('_doc/', () => {}, () => {}),
          'wimm.db',
          () => resolve(),
          (e) => reject(e)
        );
      });

      // 重新打开数据库
      await dbService.openDatabase();
      return true;
    } catch (error) {
      console.error('恢复数据库失败', error);
      return false;
    }
  }

  /**
   * 清空表数据
   * @param tableName 表名
   */
  static async clearTable(tableName: string): Promise<boolean> {
    try {
      await dbService.executeSql(`DELETE FROM ${tableName}`);
      return true;
    } catch (error) {
      console.error('清空表数据失败', error);
      return false;
    }
  }

  /**
   * 获取表中的记录数
   * @param tableName 表名
   */
  static async getTableCount(tableName: string): Promise<number> {
    try {
      const result = await dbService.selectSql(`SELECT COUNT(*) as count FROM ${tableName}`);
      return result[0].count;
    } catch (error) {
      console.error('获取表记录数失败', error);
      return 0;
    }
  }
}

export default DBUtils; 