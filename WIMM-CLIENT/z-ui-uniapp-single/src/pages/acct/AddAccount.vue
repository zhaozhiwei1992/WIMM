<template>
    <!-- 估计是这里表单有问题，参考plus官方页面重新搞下 -->
    <up-form labelPosition="left" :model="form" :rules="rules" ref="uForm">
        <up-form-item label="贷" prop="form.creditAccount" borderBottom ref="item1"
            @click="show = true; hideKeyboard(); flag = -1">
            <up-input v-model="form.creditAccountName" disabled disabledColor="#ffffff" placeholder="贷方科目"
                border="none"></up-input>
            <template #right>
                <up-icon name="arrow-right"></up-icon>
            </template>
        </up-form-item>
        <up-form-item label="借" prop="form.debitAccount" borderBottom ref="item1" @click="show = true; hideKeyboard(); flag = 1">
            <up-input v-model="form.debitAccountName" disabled disabledColor="#ffffff" placeholder="借方科目"
                border="none"></up-input>
            <template #right>
                <up-icon name="arrow-right"></up-icon>
            </template>
        </up-form-item>
        <up-form-item label="金额" prop="form.amt" borderBottom ref="item1">
            <up-input placeholder="请输入金额" border="surround" v-model="form.amt"></up-input>
        </up-form-item>
        <up-form-item label="备注" prop="form.remark" borderBottom ref="item1">
            <up-textarea v-model="form.remark" placeholder="请输入备注"></up-textarea>
        </up-form-item>
        <up-button @click="save" text="记账"></up-button>
    </up-form>

    <up-picker :show="show" :columns="columns" ref="uPickerRef" @cancel="cancel" @confirm="confirm" keyName="label"
        @change="changeHandler"></up-picker>
</template>

<script setup lang="ts">
// 使用unplugin-auto-import自动引入vue这些, https://blog.csdn.net/qq_18798149/article/details/134321097
import { onMounted, reactive, ref } from "vue";
import type { AccountVO } from "@/api/acct/account/types";
import { onLoad } from "@dcloudio/uni-app";
// import {onLoad,onReady} from "@dcloudio/uni-app";
import { getAccountClsSelect } from '@/api/acct/account-cls';
import type { ComponentOptions } from '@/api/acct/common-types'
import { saveApi } from "@/api/acct/account";

const show = ref(false);
const flag = ref(0);

const columns = reactive([
    [{id:'001', label: '资产'}, {id: '002', label: '负债'}, {id: '003', label: '收入'}, {id: '004', label: '支出'}]
]);

const columnData = reactive([]);

const uPickerRef = ref(null)
const changeHandler = (e) => {
    const {
        columnIndex,
        value,
        values,
        index,
    } = e;

    if (columnIndex === 0) {
        uPickerRef.value.setColumnValues(1, columnData[index]);
    }

    console.log('changeHandler', e, columnData[index]);
};

const form = reactive<AccountVO>({
    id: 0,
    createdBy: '',
    createdDate: new Date(),
    creditAccount: '',
    debitAccount: '',
    creditAccountName: '',
    debitAccountName: '',
    amt: 0,
    remkark: '',
    type: 1
})

const hideKeyboard = () => {
    uni.hideKeyboard()
}

const confirm = (e) => {
    if(flag.value === -1){
        form.creditAccount = e.value[1].id
        form.creditAccountName = e.value[1].label
    }else if (flag.value === 1){
        form.debitAccount = e.value[1].id
        form.debitAccountName = e.value[1].label
    }
    console.log('confirm', e, flag.value)
    show.value = false
}
const cancel = (e) => {
    // console.log('cancel');
    show.value = false
}

const save = async () => {
    console.log('save', form)
    await saveApi(form)
    // 提示保存成功
    uni.showToast({
        title: '保存成功',
        icon: 'success'
    })
}

onMounted(async () => {
    const res = await getAccountClsSelect()
    res.forEach(item => {
        const tmpOptions: ComponentOptions = reactive([])
        tmpOptions.push(...item.children)
        columnData.push(tmpOptions)
    })
});

onLoad(() => {
    console.log('onload')
})
</script>
