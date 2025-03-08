/**
 * SQLite数据库服务
 */

// 数据库名称
const DB_NAME = 'wimm.db';
// 数据库路径，只能是相对路径
const DB_PATH = '_doc/wimm.db';

class DBService {
  private db: any = null;
  private isOpen: boolean = false;

  /**
   * 打开数据库
   */
  openDatabase(): Promise<boolean> {
    return new Promise((resolve, reject) => {
      // 如果已经打开，直接返回
      if (this.isOpen) {
        resolve(true);
        return;
      }

      plus.sqlite.openDatabase({
        name: DB_NAME,
        path: DB_PATH,
        success: (e) => {
          this.isOpen = true;
          this.db = e;
          console.log('数据库打开成功');
          resolve(true);
        },
        fail: (e) => {
          console.error('数据库打开失败', e);
          reject(e);
        }
      });
    });
  }

  /**
   * 关闭数据库
   */
  closeDatabase(): Promise<boolean> {
    return new Promise((resolve, reject) => {
      if (!this.isOpen) {
        resolve(true);
        return;
      }

      plus.sqlite.closeDatabase({
        name: DB_NAME,
        success: () => {
          this.isOpen = false;
          console.log('数据库关闭成功');
          resolve(true);
        },
        fail: (e) => {
          console.error('数据库关闭失败', e);
          reject(e);
        }
      });
    });
  }

  /**
   * 执行SQL语句
   * @param sql SQL语句
   * @param values 参数值
   */
  executeSql(sql: string, values: any[] = []): Promise<any> {
    return new Promise(async (resolve, reject) => {
      try {
        // 确保数据库已打开
        if (!this.isOpen) {
          await this.openDatabase();
        }

        plus.sqlite.executeSql({
          name: DB_NAME,
          sql: sql,
          values: values,
          success: (data) => {
            resolve(data);
          },
          fail: (e) => {
            console.error('SQL执行失败', sql, values, e);
            reject(e);
          }
        });
      } catch (error) {
        reject(error);
      }
    });
  }

  /**
   * 查询数据
   * @param sql SQL语句
   * @param values 参数值
   */
  selectSql(sql: string, values: any[] = []): Promise<any[]> {
    return new Promise(async (resolve, reject) => {
      try {
        // 确保数据库已打开
        if (!this.isOpen) {
          await this.openDatabase();
        }

        plus.sqlite.selectSql({
          name: DB_NAME,
          sql: sql,
          values: values,
          success: (data) => {
            resolve(data);
          },
          fail: (e) => {
            console.error('查询失败', sql, values, e);
            reject(e);
          }
        });
      } catch (error) {
        reject(error);
      }
    });
  }

  /**
   * 事务执行多条SQL
   * @param sqlList SQL语句列表
   */
  transaction(sqlList: { sql: string; values: any[] }[]): Promise<boolean> {
    return new Promise(async (resolve, reject) => {
      try {
        // 确保数据库已打开
        if (!this.isOpen) {
          await this.openDatabase();
        }

        plus.sqlite.transaction({
          name: DB_NAME,
          operation: sqlList.map(item => ({
            sql: item.sql,
            values: item.values
          })),
          success: () => {
            resolve(true);
          },
          fail: (e) => {
            console.error('事务执行失败', e);
            reject(e);
          }
        });
      } catch (error) {
        reject(error);
      }
    });
  }

  /**
   * 初始化数据库表
   */
  async initTables(): Promise<boolean> {
    try {
      // 用户表
      await this.executeSql(`
        CREATE TABLE IF NOT EXISTS users (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          username TEXT NOT NULL,
          password TEXT NOT NULL,
          role TEXT,
          roleId TEXT,
          permissions TEXT,
          token TEXT
        )
      `);

      // 资产表
      await this.executeSql(`
        CREATE TABLE IF NOT EXISTS assets (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          totalAssets REAL DEFAULT 0,
          totalLiabilities REAL DEFAULT 0,
          netWorth REAL DEFAULT 0
        )
      `);

      // 饼图数据表
      await this.executeSql(`
        CREATE TABLE IF NOT EXISTS pie_data (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          value REAL NOT NULL,
          name TEXT NOT NULL
        )
      `);

      // 折线图数据表
      await this.executeSql(`
        CREATE TABLE IF NOT EXISTS line_data (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          name TEXT NOT NULL,
          line1 REAL NOT NULL,
          line2 REAL NOT NULL
        )
      `);

      // 雷达图数据表
      await this.executeSql(`
        CREATE TABLE IF NOT EXISTS radar_data (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          personal REAL NOT NULL,
          team REAL NOT NULL,
          max REAL NOT NULL,
          name TEXT NOT NULL
        )
      `);

      console.log('数据库表初始化成功');
      return true;
    } catch (error) {
      console.error('数据库表初始化失败', error);
      return false;
    }
  }

  /**
   * 初始化示例数据
   */
  async initSampleData(): Promise<boolean> {
    try {
      // 检查是否已有数据
      const users = await this.selectSql('SELECT * FROM users LIMIT 1');
      if (users && users.length > 0) {
        console.log('已有示例数据，跳过初始化');
        return true;
      }

      // 添加示例用户
      await this.executeSql(
        'INSERT INTO users (username, password, role, roleId, permissions, token) VALUES (?, ?, ?, ?, ?, ?)',
        ['admin', '123456', 'admin', '1', JSON.stringify(['admin']), 'sample-token']
      );

      // 添加示例资产数据
      await this.executeSql(
        'INSERT INTO assets (totalAssets, totalLiabilities, netWorth) VALUES (?, ?, ?)',
        [10000, 2000, 8000]
      );

      // 添加示例饼图数据
      const pieData = [
        { value: 1048, name: '搜索引擎' },
        { value: 735, name: '直接访问' },
        { value: 580, name: '邮件营销' },
        { value: 484, name: '联盟广告' },
        { value: 300, name: '视频广告' }
      ];

      for (const item of pieData) {
        await this.executeSql(
          'INSERT INTO pie_data (value, name) VALUES (?, ?)',
          [item.value, item.name]
        );
      }

      // 添加示例折线图数据
      const lineData = [
        { name: '1月', line1: 100, line2: 120 },
        { name: '2月', line1: 120, line2: 140 },
        { name: '3月', line1: 140, line2: 160 },
        { name: '4月', line1: 160, line2: 180 },
        { name: '5月', line1: 180, line2: 200 },
        { name: '6月', line1: 200, line2: 220 }
      ];

      for (const item of lineData) {
        await this.executeSql(
          'INSERT INTO line_data (name, line1, line2) VALUES (?, ?, ?)',
          [item.name, item.line1, item.line2]
        );
      }

      // 添加示例雷达图数据
      const radarData = [
        { personal: 80, team: 90, max: 100, name: '销售' },
        { personal: 70, team: 80, max: 100, name: '管理' },
        { personal: 60, team: 85, max: 100, name: '信息技术' },
        { personal: 50, team: 70, max: 100, name: '客服' },
        { personal: 40, team: 60, max: 100, name: '研发' }
      ];

      for (const item of radarData) {
        await this.executeSql(
          'INSERT INTO radar_data (personal, team, max, name) VALUES (?, ?, ?, ?)',
          [item.personal, item.team, item.max, item.name]
        );
      }

      console.log('示例数据初始化成功');
      return true;
    } catch (error) {
      console.error('示例数据初始化失败', error);
      return false;
    }
  }
}

// 导出单例
export const dbService = new DBService();

// 导出所有数据库服务
export * from './login';
export * from './workplace';
export * from './utils'; 