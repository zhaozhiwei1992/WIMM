<template>
  <view class="container">
    <u-navbar :title="tableName + ' 表详情'" :autoBack="true"></u-navbar>
    
    <view class="content">
      <view class="table-info">
        <text class="table-title">表名: {{ tableName }}</text>
        <text class="table-count">记录数: {{ recordCount }}</text>
      </view>
      
      <u-gap height="20"></u-gap>
      
      <view class="action-buttons">
        <u-button type="primary" text="刷新数据" @click="loadTableData"></u-button>
        <u-button type="error" text="清空表数据" @click="handleClearTable"></u-button>
      </view>
      
      <u-gap height="20"></u-gap>
      
      <scroll-view scroll-x class="table-container">
        <view class="table">
          <!-- 表头 -->
          <view class="table-header">
            <view class="table-cell" v-for="(column, index) in columns" :key="index">
              {{ column }}
            </view>
          </view>
          
          <!-- 表数据 -->
          <view class="table-row" v-for="(record, rowIndex) in records" :key="rowIndex">
            <view class="table-cell" v-for="(column, colIndex) in columns" :key="colIndex">
              {{ record[column] }}
            </view>
          </view>
          
          <!-- 无数据提示 -->
          <view class="no-data" v-if="records.length === 0">
            暂无数据
          </view>
        </view>
      </scroll-view>
      
      <u-gap height="20"></u-gap>
      
      <view class="pagination">
        <text>第 {{ currentPage }} 页，共 {{ totalPages }} 页</text>
        <view class="pagination-buttons">
          <u-button size="mini" :disabled="currentPage <= 1" @click="prevPage">上一页</u-button>
          <u-button size="mini" :disabled="currentPage >= totalPages" @click="nextPage">下一页</u-button>
        </view>
      </view>
    </view>
    
    <u-toast ref="uToast"></u-toast>
    <u-modal v-model="showModal" title="清空表数据" content="确定要清空表中的所有数据吗？此操作不可恢复！" @confirm="confirmClearTable"></u-modal>
  </view>
</template>

<script lang="ts" setup>
import { ref, onMounted, computed } from 'vue';
import { dbService, DBUtils } from '@/config/db';

// 表信息
const tableName = ref('');
const columns = ref<string[]>([]);
const records = ref<any[]>([]);
const recordCount = ref(0);

// 分页
const currentPage = ref(1);
const pageSize = 10;
const totalPages = computed(() => Math.ceil(recordCount.value / pageSize));

// 弹窗控制
const showModal = ref(false);

// 初始化
onMounted(() => {
  // 获取页面参数
  const eventChannel = getOpenerEventChannel();
  eventChannel.on('tableData', (data: any) => {
    tableName.value = data.tableName;
    columns.value = data.columns;
    records.value = data.records;
    
    // 获取记录总数
    loadRecordCount();
  });
});

// 加载记录总数
async function loadRecordCount() {
  try {
    recordCount.value = await DBUtils.getTableCount(tableName.value);
  } catch (error) {
    console.error('获取记录总数失败', error);
  }
}

// 加载表数据
async function loadTableData() {
  try {
    // 计算偏移量
    const offset = (currentPage.value - 1) * pageSize;
    
    // 查询数据
    const data = await dbService.selectSql(
      `SELECT * FROM ${tableName.value} LIMIT ${pageSize} OFFSET ${offset}`
    );
    
    records.value = data;
    
    // 刷新记录总数
    await loadRecordCount();
    
    uni.showToast({
      title: '数据刷新成功',
      icon: 'success'
    });
  } catch (error) {
    console.error('加载表数据失败', error);
    uni.showToast({
      title: '加载表数据失败',
      icon: 'none'
    });
  }
}

// 上一页
function prevPage() {
  if (currentPage.value > 1) {
    currentPage.value--;
    loadTableData();
  }
}

// 下一页
function nextPage() {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
    loadTableData();
  }
}

// 清空表
function handleClearTable() {
  showModal.value = true;
}

// 确认清空表
async function confirmClearTable() {
  try {
    await DBUtils.clearTable(tableName.value);
    
    // 重新加载数据
    currentPage.value = 1;
    await loadTableData();
    
    uni.showToast({
      title: '表数据已清空',
      icon: 'success'
    });
  } catch (error) {
    console.error('清空表数据失败', error);
    uni.showToast({
      title: '清空表数据失败',
      icon: 'none'
    });
  }
}
</script>

<style lang="scss" scoped>
.container {
  padding-bottom: 30rpx;
}

.content {
  padding: 20rpx;
}

.table-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10rpx 0;
}

.table-title {
  font-size: 32rpx;
  font-weight: bold;
}

.table-count {
  font-size: 28rpx;
  color: #666;
}

.action-buttons {
  display: flex;
  justify-content: space-between;
  gap: 20rpx;
}

.table-container {
  width: 100%;
  overflow-x: auto;
}

.table {
  min-width: 100%;
  border-collapse: collapse;
}

.table-header {
  display: flex;
  background-color: #f2f2f2;
  font-weight: bold;
}

.table-row {
  display: flex;
  border-bottom: 1px solid #eee;
}

.table-cell {
  flex: 1;
  min-width: 150rpx;
  padding: 20rpx;
  word-break: break-all;
  text-align: center;
}

.no-data {
  padding: 40rpx;
  text-align: center;
  color: #999;
}

.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
}

.pagination-buttons {
  display: flex;
  gap: 20rpx;
}
</style> 