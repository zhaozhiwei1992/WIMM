<template>

    <up-input placeholder="请输入内容" border="surround" v-model="value" @change="change"></up-input>
    <!-- 估计是这里表单有问题，参考plus官方页面重新搞下 -->
    <up-form labelPosition="left" :model="form" :rules="rules" ref="uForm">
        <up-form-item label="贷" prop="form.creditAccount" borderBottom ref="item1">
            <up-picker ref="creditPicker" :columns="acctClsOptions" @change="creditChangeHandler"></up-picker>
        </up-form-item>
        <up-form-item label="借" prop="form.debigAccount" borderBottom ref="item1">
            <up-picker ref="debitPicker" :columns="acctClsOptions" @change="debitChangeHandler"></up-picker>
        </up-form-item>
        <up-form-item label="金额" prop="form.amt" borderBottom ref="item1">
            <up-input placeholder="请输入金额" border="surround" v-model="form.amt"></up-input>
        </up-form-item>
        <up-form-item label="备注" prop="form.remark" borderBottom ref="item1">
            <up-textarea v-model="form.remark" placeholder="请输入备注"></up-textarea>
        </up-form-item>
        <up-button @click="save" text="记账"></up-button>
    </up-form>
</template>

<script setup lang="ts">
// 使用unplugin-auto-import自动引入vue这些, https://blog.csdn.net/qq_18798149/article/details/134321097
// import {onMounted, onUpdated, ref,computed} from "vue";
// import {onLoad,onReady} from "@dcloudio/uni-app";

onMounted(async () => {
    console.log('mounted')
    // const res = await getAccountClsSelect()
    // acctClsOptions.push(...res)
    console.log('mounted')
});

onLoad(() => {
    console.log('onload')
})
</script>
