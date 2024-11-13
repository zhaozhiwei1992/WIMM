export class DatabaseHelper {
  private db: plus.sqlite.Database;

  constructor() {
    // #ifdef APP-PLUS
    this.db = plus.sqlite.openDatabase({
      name: 'WIMM',
      path: '_doc/',
      encoding: 'UTF-8'
    });
    // #endif
  }

  public async init(): Promise<void> {
    try {
        await this.createVouDetailTable();
        await this.createUserTable();
        // 添加更多表的初始化...
      } catch (error) {
        console.error('Database initialization failed', error);
      }
  }
    private createUserTable = async () => {
        const sql = 
        `
          CREATE TABLE IF NOT EXISTS sys_user (
            name TEXT,
            login Text,
            password TEXT
          )
        `;
        await this.db.executeSql(sql);
    }

    private async createVouDetailTable() {
        const sql = 
        `
          CREATE TABLE IF NOT EXISTS voucher_detail (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            voucher_no TEXT,
            acct_cls_code TEXT,
            acct_cls_name TEXT,
            amt REAL,
            dr_cr INTEGER,
            remark TEXT
          )
        `;
        await this.db.executeSql(sql);
    }

  public executeSql(sql: string, params: any[] = []): Promise<void> {
    return new Promise((resolve, reject) => {
      this.db.executeSql(sql, params, () => resolve(), (err) => reject(err));
    });
  }

  public insert(tableName: string, data: any): Promise<void> {
    return new Promise((resolve, reject) => {
      const keys = Object.keys(data);
      const values = Object.values(data);
      const placeholders = keys.map(() => '?').join(',');
      const sql = `INSERT INTO ${tableName} (${keys.join(',')}) VALUES (${placeholders})`;

      this.db.executeSql(sql, values, () => resolve(), (err) => reject(err));
    });
  }

  public query(tableName: string, conditions: string = '', params: any[] = []): Promise<any[]> {
    return new Promise((resolve, reject) => {
      let sql = `SELECT * FROM ${tableName}`;
      if (conditions) {
        sql += ` WHERE ${conditions}`;
      }
      this.db.executeSql(sql, params, (res) => {
        const results: any[] = [];
        for (let i = 0; i < res.rows.length; i++) {
          results.push(res.rows.item(i));
        }
        resolve(results);
      }, (err) => reject(err));
    });
  }

  public update(tableName: string, data: any, conditions: string = '', params: any[] = []): Promise<void> {
    return new Promise((resolve, reject) => {
      const sets = Object.keys(data).map(key => `${key} = ?`).join(',');
      const sql = `UPDATE ${tableName} SET ${sets} WHERE ${conditions}`;

      this.db.executeSql(sql, [...Object.values(data), ...params], () => resolve(), (err) => reject(err));
    });
  }

  public delete(tableName: string, conditions: string = '', params: any[] = []): Promise<void> {
    return new Promise((resolve, reject) => {
      let sql = `DELETE FROM ${tableName}`;
      if (conditions) {
        sql += ` WHERE ${conditions}`;
      }
      this.db.executeSql(sql, params, () => resolve(), (err) => reject(err));
    });
  }

  public close(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.db.close((err) => {
        if (err) {
          reject(err);
        } else {
          resolve();
        }
      });
    });
  }
}