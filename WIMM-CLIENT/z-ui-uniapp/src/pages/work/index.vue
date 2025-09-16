<script setup lang="ts">
import { onMounted, reactive } from "vue";

const dynamicGrid = reactive([
  {
    type: "person-filled",
    text: "记个账",
  },
  {
    type: "download-filled",
    text: "记账导出",
  },
  {
    type: "upload-filled",
    text: "记账导入",
  },
  {
    type: "wallet-filled",
    text: "收入分析",
  },
  {
    type: "wallet-filled",
    text: "支出分析",
  },
  {
    type: "locked-filled",
    text: "预算管理",
  },
  {
    type: "notification-filled",
    text: "账单提醒",
  },
  {
    type: "wallet-filled",
    text: "资产负债表",
  },
]);

function changeGrid(obj) {
  const i = obj.detail.index
  if (i === 0) {
    uni.switchTab({ url: "/pages/acct/AddAccount" });
  } else if (i === 1) {
    // 调用后台下载记账明细数据
    exportData()
  } else if (i === 2) {
    importData()
  } else if (i === 3) {
    uni.navigateTo({ url: "/pages/report/CreditAnalyze" });
  } else if (i === 4) {
    uni.navigateTo({ url: "/pages/report/DebitAnalyze" });
  }
  // uni.showToast({
  //   title: "模块建设中~",
  //   image: "https://cdn.uviewui.com/uview/demo/toast/error.png",
  //   duration: 2000,
  // });
}

const importData = () => {
  // 导入数据
}

const exportData = () => {
  // 导出
  let header = {
    'X-Access-Token': uni.getStorageSync(SET_TOKEN) 
  }

  uni.request({
      url: 'http://......', // 后端请求地址
      method: 'GET',
      header: header,
      responseType: 'arraybuffer',
      data: {}
    })
    .then((res) => {
      const fileName = new Date().getTime() + '.xlsx'
      const arrayBuffer = res[1].data // utf-8编码的文件数据
      const base64String = uni.arrayBufferToBase64(arrayBuffer)
      const buffer = uni.base64ToArrayBuffer(base64String)
      let fs = uni.getFileSystemManager()
      const filePath = wx.env.USER_DATA_PATH + '/' + fileName
      fs.writeFile({
        filePath: filePath,
        data: buffer,
        encoding: 'binary',
        success: (res) => {
          console.log('文件保存成功')
          uni.openDocument({
            filePath: filePath,
            fileType: 'xlsx',
            showMenu: true,
            success: () => {
              console.log('文件预览成功')
              // 构建分享内容
            },
            fail: (error) => {
              console.error('文件保存失败', error)
            }
          })
        },
        fail: (err) => {
          console.log('文件保存失败', err)
        }
      })
    })
}

// 如果需要在组件挂载后执行某些操作，可以使用 onMounted 钩子
onMounted(() => {
  // 组件挂载后的代码
});
</script>

<template>
  <view class="work-container">
    <!-- 宫格组件 -->
    <uni-section title="工作空间" type="line"></uni-section>
    <view class="grid-body">
      <uni-grid :column="4" :showBorder="false" :horizontal="false" :square="true"  use-virtual :threshold="8"  @change="changeGrid">
        <uni-grid-item
          v-for="(item, index) in dynamicGrid"
          :index="index"
          :key="index"
        >
          <view class="grid-item-box">
            <uni-icons :type="item.type" size="30"></uni-icons>
            <text class="text">{{ item.text }}</text>
          </view>
        </uni-grid-item>
      </uni-grid>
    </view>
  </view>
</template>

<style lang="scss">
.grid-item-box {
  flex: 1;
  /* #ifndef APP-NVUE */
  display: flex;
  /* #endif */
  flex-direction: column;
  align-items: center;
  justify-content: center;
  // padding: 15px 0;
  // 修改后
  padding: 8px 0;  // 减少垂直间距
  @media screen and (min-width: 500px) {
    padding: 12px 0;  // 大屏适当增加
  }
  uni-icons {
    margin-bottom: 4px;  // 原默认8px
  }
  .text {
    font-size: 12px;     // 缩小字体
    line-height: 1.2;    // 紧凑行高
  }
}

.swiper {
  height: 300rpx;
}

.swiper-box {
  height: 150px;
}

.swiper-item {
  /* #ifndef APP-NVUE */
  display: flex;
  /* #endif */
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #fff;
  height: 300rpx;
  line-height: 300rpx;
}

@media screen and (min-width: 500px) {
  .uni-swiper-dot-box {
    width: 400px;
    /* #ifndef APP-NVUE */
    margin: 0 auto;
    /* #endif */
    margin-top: 8px;
  }

  .image {
    width: 100%;
  }
}

.uni-grid-item {
  &:active {
    background-color: #f5f5f5;
    opacity: 0.8;
  }
}

.uni-grid {
  position: relative;
  &::after {
    content: '';
    position: absolute;
    left: 0;
    right: 0;
    bottom: 0;
    height: 1px;
    background: #eee;
  }
}
</style>
