<style lang="scss">
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
@import "uview-plus/index.scss";
</style>

<script setup lang="ts">
import { onHide, onLaunch, onShow } from "@dcloudio/uni-app";

// 不需要登录的白名单页面
const LOGIN_PAGE = '/pages/login/login';
const WHITE_LIST = [LOGIN_PAGE];

onLaunch(() => {
  console.log("App Launch");
  // 启动时检查登录状态
  checkLogin();
});

onShow(() => {
  console.log("App Show");
});

onHide(() => {
  console.log("App Hide");
});

function checkLogin() {
  const token = uni.getStorageSync('token');
  if (!token) {
    // 未登录，跳转登录页
    const pages = getCurrentPages();
    if (pages.length > 0) {
      const currentPage = pages[pages.length - 1];
      const path = '/' + currentPage.route;
      if (!WHITE_LIST.includes(path)) {
        uni.redirectTo({ url: LOGIN_PAGE });
      }
    }
  }
}
</script>
<style></style>
