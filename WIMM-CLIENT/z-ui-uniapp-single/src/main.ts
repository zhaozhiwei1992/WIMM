import { createSSRApp } from "vue";
import App from "./App.vue";
import { setupUView } from "./plugins/uview"
import { dbService } from "./config/db";

export function createApp() {
  const app = createSSRApp(App);
  setupUView(app);
  
  // 初始化数据库
  initDatabase();
  
  return {
    app,
  };
}

/**
 * 初始化数据库
 */
async function initDatabase() {
  try {
    // 打开数据库
    await dbService.openDatabase();
    // 初始化表结构
    await dbService.initTables();
    // 初始化示例数据
    await dbService.initSampleData();
    console.log('数据库初始化完成');
  } catch (error) {
    console.error('数据库初始化失败', error);
  }
}
