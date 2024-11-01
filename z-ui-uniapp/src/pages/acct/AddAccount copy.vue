<template>
    <!-- 
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
    </up-form> -->

    <up-button @click="save" text="记账"></up-button>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import { AccountVO } from "@/api/acct/account/types";
import { saveApi } from "@/api/acct/account";
import { getAccountClsSelect } from '@/api/acct/account-cls';

const creditPicker = ref();
const debitPicker = ref();

const creditChangeHandler = (e) => {
    const {
        columnIndex,
        value,
        values, // values为当前变化列的数组内容
        index,
    } = e
    // 当第一列值发生变化时，变化第二列(后一列)对应的选项
    if (columnIndex === 0) {
        // picker为选择器this实例，变化第二列对应的选项
        creditPicker.setColumnValues(1, acctClsOptions[columnIndex].children)
    }
}

const debitChangeHandler = (e) => {
    const {
        columnIndex,
        value,
        values, // values为当前变化列的数组内容
        index,
    } = e
    // 当第一列值发生变化时，变化第二列(后一列)对应的选项
    if (columnIndex === 0) {
        // picker为选择器this实例，变化第二列对应的选项
        debitPicker.setColumnValues(1, acctClsOptions[columnIndex].children)
    }
}

const acctClsOptions = reactive([])

const success = ref(false);

const form = reactive<AccountVO>({
    id: 0,
    createdBy: '',
    createdDate: new Date(),
    creditAccount: '',
    debitAccount: '',
    amt: 0,
    remkark: '',
    type: 1
})

const rules = reactive({
    amt: {
        rules:
        {
            required: true,
            message: "请填写用户名",
        },
    },
});

const save = async () => {
    const res = await saveApi(form)
    if (res) {
        success.value = true
        // 清空表单
        resetForm()
    }
}

const resetForm = async () => {
}

onMounted(async () => {
    console.log('mounted')
    // const res = await getAccountClsSelect()
    // acctClsOptions.push(...res)
    console.log('mounted')
});
</script>
