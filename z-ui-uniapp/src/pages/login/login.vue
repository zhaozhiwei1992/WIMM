<script setup lang="ts">
import { loginByPhoneApi } from "@/api/login";
import type { SmsCodeVO } from "@/api/login/types";
import { ref } from "vue";

const title = ref("欢迎回来！");
const second = ref(60);
const showText = ref(true);
const phone = ref("");
const yzm = ref("");

const login = async () => {
  if (!phone.value) {
    uni.showToast({ title: "请输入手机号", icon: "none" });
    return;
  }
  if (!/^[1][3,4,5,7,8,9][0-9]{9}$/.test(phone.value)) {
    uni.showToast({ title: "请输入正确手机号", icon: "none" });
    return;
  }
  if (!yzm.value) {
    uni.showToast({ title: "请输入验证码", icon: "none" });
    return;
  }
  //....此处省略，这里需要调用后台验证一下验证码是否正确，根据您的需求来
  const obj: SmsCodeVO = {
    mobile: phone.value,
    scene: yzm.value,
  };
  try {
    const res = await loginByPhoneApi(obj);
    console.log(res);
    uni.setStorageSync("token", res.token);
    uni.setStorageSync("username", res.username);
    uni.showToast({ title: "登录成功！", icon: "none" });
    // 跳转首页, 首页是tabBar需要用switchTab
    uni.switchTab({ url: "/pages/acct/AddAccount" });
  } catch (err) {
    uni.showToast({ title: "验证码错误", icon: "none" });
  }
};

const getCode = () => {
  let interval = setInterval(() => {
    showText.value = false;
    let times = second.value - 1;
    second.value = times;
    console.log(times);
  }, 1000);
  setTimeout(() => {
    clearInterval(interval);
    second.value = 60;
    showText.value = true;
  }, 60000);
  //这里请求后台获取短信验证码
  // request({
  //   success: function (res) {
  //     showText.value = false;
  //   },
  // });
};

const wxLogin = () => {
  uni.showToast({ title: "微信登录", icon: "none" });
};

const zfbLogin = () => {
  uni.showToast({ title: "支付宝登录", icon: "none" });
};
</script>
<!-- 蓝色简洁登录页面 -->
<template>
  <view class="t-login">
    <!-- 页面装饰图片 -->
    <image
      class="img-a"
      src="https://zhoukaiwen.com/img/loginImg/2.png"
    ></image>
    <image
      class="img-b"
      src="https://zhoukaiwen.com/img/loginImg/3.png"
    ></image>
    <!-- 标题 -->
    <view class="t-b">{{ title }}</view>
    <view class="t-b2">欢迎使用，钱呢小程序</view>
    <form class="cl">
      <view class="t-a">
        <image src="https://zhoukaiwen.com/img/loginImg/sj.png"></image>
        <view class="line"></view>
        <input
          type="number"
          name="phone"
          placeholder="请输入手机号"
          maxlength="11"
          v-model="phone"
        />
      </view>
      <view class="t-a">
        <image src="https://zhoukaiwen.com/img/loginImg/yz.png"></image>
        <view class="line"></view>
        <input
          type="number"
          name="code"
          maxlength="6"
          placeholder="请输入验证码"
          v-model="yzm"
        />
        <view v-if="showText" class="t-c" @tap="getCode()">发送短信</view>
        <view v-else class="t-c" style="background-color: #a7a7a7"
          >重新发送({{ second }})</view
        >
      </view>
      <button @tap="login()">登 录</button>
    </form>
    <view class="t-f"><text>————— 第三方账号登录 —————</text></view>
    <view class="t-e cl">
      <view class="t-g" @tap="wxLogin()"
        ><image src="https://zhoukaiwen.com/img/loginImg/wx.png"></image
      ></view>
      <view class="t-g" @tap="zfbLogin()"
        ><image src="https://zhoukaiwen.com/img/loginImg/qq.png"></image
      ></view>
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
  /* margin-bottom: -200rpx; */
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

.t-login .t-a image {
  width: 40rpx;
  height: 40rpx;
  position: absolute;
  left: 40rpx;
  top: 28rpx;
  /* border-right: 2rpx solid #dedede; */
  margin-right: 20rpx;
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

.t-login .t-c {
  position: absolute;
  right: 22rpx;
  top: 22rpx;
  background: #5677fc;
  color: #fff;
  font-size: 24rpx;
  border-radius: 50rpx;
  height: 50rpx;
  line-height: 50rpx;
  padding: 0 25rpx;
}

.t-login .t-d {
  text-align: center;
  color: #999;
  margin: 80rpx 0;
}

.t-login .t-e {
  text-align: center;
  width: 250rpx;
  margin: 80rpx auto 0;
}

.t-login .t-g {
  float: left;
  width: 50%;
}

.t-login .t-e image {
  width: 50rpx;
  height: 50rpx;
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
  color: #000;
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
