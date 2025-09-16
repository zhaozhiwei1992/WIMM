<template>
  <view class="container">
    <u-navbar title="数据库管理" :autoBack="true"></u-navbar>
    
    <view class="content">
      <u-cell-group title="数据库操作">
        <u-cell title="初始化数据库" @click="handleInitDB" icon="reload" :isLink="true"></u-cell>
        <u-cell title="备份数据库" @click="handleBackupDB" icon="download" :isLink="true"></u-cell>
        <u-cell title="恢复数据库" @click="handleRestoreDB" icon="upload" :isLink="true"></u-cell>
      </u-cell-group>
      
      <u-cell-group title="数据表信息" margin-top="20">
        <u-cell v-for="(table, index) in tables" :key="index" :title="table.name" :value="`${table.count}条记录`" @click="showTableDetail(table.name)" :isLink="true"></u-cell>
      </u-cell-group>
      
      <u-gap height="20"></u-gap>
      
      <u-button type="primary" text="刷新数据" @click="loadTableInfo"></u-button>
    </view>
    
    <u-toast ref="uToast"></u-toast>
    <u-modal v-model="showModal" :title="modalTitle" :content="modalContent" @confirm="confirmModal"></u-modal>
  </view>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { dbService, DBUtils } from '@/config/db';

// 表信息
const tables = ref<{ name: string; count: number }[]>([]);
// 弹窗控制
const showModal = ref(false);
const modalTitle = ref('');
const modalContent = ref('');
const modalCallback = ref<() => void>(() => {});

// 初始化
onMounted(async () => {
  await loadTableInfo();
});

// 加载表信息
async function loadTableInfo() {
  try {
    // 获取所有表
    const result = await dbService.selectSql(
      "SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%'"
    );
    
    // 获取每个表的记录数
    const tableInfos = [];
    for (const item of result) {
      const count = await DBUtils.getTableCount(item.name);
      tableInfos.push({
        name: item.name,
        count: count
      });
    }
    
    tables.value = tableInfos;
  } catch (error) {
    console.error('加载表信息失败', error);
    uni.showToast({
      title: '加载表信息失败',
      icon: 'none'
    });
  }
}

// 初始化数据库
async function handleInitDB() {
  showConfirmModal(
    '初始化数据库',
    '确定要初始化数据库吗？这将清空所有数据并重新创建表结构和示例数据。',
    async () => {
      try {
        // 关闭数据库
        await dbService.closeDatabase();
        
        // 删除数据库文件
        try {
          await new Promise<void>((resolve, reject) => {
            plus.io.resolveLocalFileSystemURL('_doc/', entry => {
              entry.getFile('wimm.db', { create: false }, fileEntry => {
                fileEntry.remove(
                  () => resolve(),
                  e => reject(e)
                );
              }, e => resolve()); // 如果文件不存在，直接继续
            }, e => reject(e));
          });
        } catch (e) {
          console.log('删除数据库文件失败或文件不存在', e);
        }
        
        // 重新打开数据库
        await dbService.openDatabase();
        // 初始化表结构
        await dbService.initTables();
        // 初始化示例数据
        await dbService.initSampleData();
        
        // 刷新表信息
        await loadTableInfo();
        
        uni.showToast({
          title: '数据库初始化成功',
          icon: 'success'
        });
      } catch (error) {
        console.error('数据库初始化失败', error);
        uni.showToast({
          title: '数据库初始化失败',
          icon: 'none'
        });
      }
    }
  );
}

// 备份数据库
async function handleBackupDB() {
  try {
    const backupPath = '_doc/backup/';
    
    // 创建备份目录
    try {
      await new Promise<void>((resolve, reject) => {
        plus.io.resolveLocalFileSystemURL('_doc/', entry => {
          entry.getDirectory('backup', { create: true }, () => resolve(), e => reject(e));
        }, e => reject(e));
      });
    } catch (e) {
      console.log('创建备份目录失败', e);
    }
    
    // 备份数据库
    const success = await DBUtils.backupDatabase(backupPath);
    
    if (success) {
      uni.showToast({
        title: '数据库备份成功',
        icon: 'success'
      });
    } else {
      uni.showToast({
        title: '数据库备份失败',
        icon: 'none'
      });
    }
  } catch (error) {
    console.error('数据库备份失败', error);
    uni.showToast({
      title: '数据库备份失败',
      icon: 'none'
    });
  }
}

// 恢复数据库
async function handleRestoreDB() {
  showConfirmModal(
    '恢复数据库',
    '确定要恢复数据库吗？这将覆盖当前的所有数据。',
    async () => {
      try {
        const backupPath = '_doc/backup/wimm_backup.db';
        
        // 检查备份文件是否存在
        try {
          await new Promise<void>((resolve, reject) => {
            plus.io.resolveLocalFileSystemURL(backupPath, () => resolve(), e => reject(e));
          });
        } catch (e) {
          uni.showToast({
            title: '备份文件不存在',
            icon: 'none'
          });
          return;
        }
        
        // 恢复数据库
        const success = await DBUtils.restoreDatabase(backupPath);
        
        if (success) {
          // 刷新表信息
          await loadTableInfo();
          
          uni.showToast({
            title: '数据库恢复成功',
            icon: 'success'
          });
        } else {
          uni.showToast({
            title: '数据库恢复失败',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error('数据库恢复失败', error);
        uni.showToast({
          title: '数据库恢复失败',
          icon: 'none'
        });
      }
    }
  );
}

// 显示表详情
async function showTableDetail(tableName: string) {
  try {
    // 获取表的列信息
    const columns = await DBUtils.getTableColumns(tableName);
    
    // 获取表的前10条记录
    const records = await dbService.selectSql(`SELECT * FROM ${tableName} LIMIT 10`);
    
    // 跳转到表详情页
    uni.navigateTo({
      url: `/pages/system/db-manage/table-detail?tableName=${tableName}`,
      success: (res) => {
        // 传递数据给打开的页面
        res.eventChannel.emit('tableData', {
          tableName,
          columns,
          records
        });
      }
    });
  } catch (error) {
    console.error('获取表详情失败', error);
    uni.showToast({
      title: '获取表详情失败',
      icon: 'none'
    });
  }
}

// 显示确认弹窗
function showConfirmModal(title: string, content: string, callback: () => void) {
  modalTitle.value = title;
  modalContent.value = content;
  modalCallback.value = callback;
  showModal.value = true;
}

// 确认弹窗
function confirmModal() {
  modalCallback.value();
}
</script>

<style lang="scss" scoped>
.container {
  padding-bottom: 30rpx;
}

.content {
  padding: 20rpx;
}
</style> 