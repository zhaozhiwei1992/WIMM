<script setup lang="ts">
import { loginApi, loginByWxApi } from "@/api/login";
import type { UserLoginType } from "@/api/login/types";
import { ref } from "vue";

const title = ref("欢迎回来！");
// 登录方式切换: 'account' | 'wx'
const loginType = ref("account");
const username = ref("");
const password = ref("");

// 账号密码登录
const login = async () => {
  if (!username.value) {
    uni.showToast({ title: "请输入用户名", icon: "none" });
    return;
  }
  if (!password.value) {
    uni.showToast({ title: "请输入密码", icon: "none" });
    return;
  }
  const data: UserLoginType = {
    username: username.value,
    password: password.value,
  };
  try {
    const res = await loginApi(data);
    uni.setStorageSync("token", res.token);
    uni.setStorageSync("username", res.username || username.value);
    uni.showToast({ title: "登录成功！", icon: "none" });
    uni.switchTab({ url: "/pages/acct/AddAccount" });
  } catch (err: any) {
    uni.showToast({
      title: err?.message || "登录失败，请检查用户名和密码",
      icon: "none",
    });
  }
};

// 微信小程序登录
const wxLogin = () => {
  // #ifdef MP-WEIXIN
  uni.login({
    provider: "weixin",
    success: async (loginRes) => {
      try {
        const res = await loginByWxApi(loginRes.code);
        uni.setStorageSync("token", res.token);
        uni.setStorageSync("username", res.username || "微信用户");
        uni.showToast({ title: "登录成功！", icon: "none" });
        uni.switchTab({ url: "/pages/acct/AddAccount" });
      } catch (err: any) {
        uni.showToast({
          title: err?.message || "微信登录失败",
          icon: "none",
        });
      }
    },
    fail: () => {
      uni.showToast({ title: "获取微信授权失败", icon: "none" });
    },
  });
  // #endif
  // #ifndef MP-WEIXIN
  uni.showToast({ title: "仅在微信小程序中支持微信登录", icon: "none" });
  // #endif
};
</script>

<!-- 简洁登录页面 -->
<template>
  <view class="t-login">
    <!-- 页面装饰图片 -->
    <image class="img-a" src="/static/images/login-top.png"></image>
    <image class="img-b" src="/static/images/login-bottom.png"></image>
    <!-- 标题 -->
    <view class="t-b">{{ title }}</view>
    <view class="t-b2">欢迎使用，钱呢</view>

    <!-- 账号密码登录表单 -->
    <form class="cl">
      <view class="t-a">
        <view class="input-icon">👤</view>
        <view class="line"></view>
        <input
          type="text"
          name="username"
          placeholder="请输入用户名"
          v-model="username"
        />
      </view>
      <view class="t-a">
        <view class="input-icon">🔒</view>
        <view class="line"></view>
        <input
          type="safe-password"
          name="password"
          placeholder="请输入密码"
          v-model="password"
        />
      </view>
      <button @tap="login()">登 录</button>
    </form>

    <!-- 第三方登录 -->
    <view class="t-f"><text>————— 其他登录方式 —————</text></view>
    <view class="t-e cl">
      <view class="t-g" @tap="wxLogin()">
        <view class="wx-btn">
          <text>微信登录</text>
        </view>
      </view>
    </view>
  </view>
</template>

<style>
.img-a {
  position: absolute;
  width: 100%;
  top: -150rpx;
  right: 0;
}
.img-b {
  position: absolute;
  width: 50%;
  bottom: 0;
  left: -50rpx;
}
.t-login {
  width: 650rpx;
  margin: 0 auto;
  font-size: 28rpx;
  color: #000;
}

.t-login button {
  font-size: 28rpx;
  background: #5677fc;
  color: #fff;
  height: 90rpx;
  line-height: 90rpx;
  border-radius: 50rpx;
  box-shadow: 0 5px 7px 0 rgba(86, 119, 252, 0.2);
}

.t-login input {
  padding: 0 20rpx 0 120rpx;
  height: 90rpx;
  line-height: 90rpx;
  margin-bottom: 50rpx;
  background: #f8f7fc;
  border: 1px solid #e9e9e9;
  font-size: 28rpx;
  border-radius: 50rpx;
}

.t-login .t-a {
  position: relative;
}

.t-login .t-a .input-icon {
  width: 40rpx;
  height: 40rpx;
  position: absolute;
  left: 40rpx;
  top: 28rpx;
  font-size: 32rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.t-login .t-a .line {
  width: 2rpx;
  height: 40rpx;
  background-color: #dedede;
  position: absolute;
  top: 28rpx;
  left: 98rpx;
}

.t-login .t-b {
  text-align: left;
  font-size: 46rpx;
  color: #000;
  padding: 300rpx 0 30rpx 0;
  font-weight: bold;
}

.t-login .t-b2 {
  text-align: left;
  font-size: 32rpx;
  color: #aaaaaa;
  padding: 0rpx 0 120rpx 0;
}

.t-login .t-e {
  text-align: center;
  width: 400rpx;
  margin: 80rpx auto 0;
}

.t-login .t-g {
  width: 100%;
}

.t-login .wx-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 90rpx;
  line-height: 90rpx;
  background: #07c160;
  color: #fff;
  font-size: 28rpx;
  border-radius: 50rpx;
  box-shadow: 0 5px 7px 0 rgba(7, 193, 96, 0.2);
}

.t-login .t-f {
  text-align: center;
  margin: 200rpx 0 0 0;
  color: #666;
}

.t-login .t-f text {
  margin-left: 20rpx;
  color: #aaaaaa;
  font-size: 27rpx;
}

.t-login .uni-input-placeholder {
  color: #999;
}

.cl {
  zoom: 1;
}

.cl:after {
  clear: both;
  display: block;
  visibility: hidden;
  height: 0;
  content: "\20";
}
</style>
