<style lang="scss">
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
	@import "uview-plus/index.scss";
</style>

<script setup lang="ts">
import { onLaunch, onShow, onHide } from "@dcloudio/uni-app";
import { DatabaseHelper } from "./plugins/sqlite/db";
onLaunch(() => {
  console.log("App Launch");

  // 初始化数据库
  const db = new DatabaseHelper();
  async () => {
    try {
      await db.init();
      console.log('数据库初始化成功');
    } catch (err) {
      console.error('数据库初始化失败', err);
    }
  };
});
onShow(() => {
  console.log("App Show");
});
onHide(() => {
  console.log("App Hide");
});
</script>
<style></style>
