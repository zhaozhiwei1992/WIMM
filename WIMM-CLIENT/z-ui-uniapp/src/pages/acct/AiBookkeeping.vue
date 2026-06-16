<template>
    <view class="ai-book">
        <!-- 输入区 -->
        <view class="input-section">
            <up-textarea v-model="text" placeholder="说点什么... 如：午饭花了35，微信付的 / 收到工资8000 / 微信转500到银行卡"
                count border="surround" :height="120"></up-textarea>
            <up-button type="primary" @click="parse" :loading="loading" text="AI 解析"></up-button>
        </view>

        <!-- 解析预览 -->
        <view v-if="result" class="preview">
            <view class="type-tag">
                <up-tag :type="typeTagType" :text="typeTagText" mode="dark" shape="circle"></up-tag>
                <up-tag v-if="result.fallback" type="warning" text="部分项未识别，请核对" mode="plain" shape="circle"></up-tag>
            </view>

            <up-form labelPosition="left" :model="result">
                <up-form-item :label="bizLabel" borderBottom @click="openPicker('biz')">
                    <up-input v-model="result.bizAccountName" disabled disabledColor="#ffffff" border="none"></up-input>
                    <template #right><up-icon name="arrow-right"></up-icon></template>
                </up-form-item>
                <up-form-item :label="payLabel" borderBottom @click="openPicker('pay')">
                    <up-input v-model="result.payAccountName" disabled disabledColor="#ffffff" border="none"></up-input>
                    <template #right><up-icon name="arrow-right"></up-icon></template>
                </up-form-item>
                <up-form-item label="金额" borderBottom>
                    <up-input placeholder="金额" border="surround" v-model="result.amt" type="digit"></up-input>
                </up-form-item>
                <up-form-item label="备注" borderBottom>
                    <up-input placeholder="备注" border="surround" v-model="result.remark"></up-input>
                </up-form-item>
            </up-form>

            <up-button type="success" @click="confirm" :loading="saving" text="确认记账" style="margin-top: 20px"></up-button>
        </view>

        <up-picker :show="show" :columns="columns" ref="uPickerRef" @cancel="show = false" @confirm="confirmPicker"
            keyName="label" @change="changeHandler"></up-picker>
    </view>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from "vue";
import { onLoad } from "@dcloudio/uni-app";
import { getAccountClsSelect } from '@/api/acct/account-cls';
import type { ComponentOptions } from '@/api/acct/common-types'
import { aiParseApi, saveApi } from "@/api/acct/account";
import type { AiParseResultVO } from "@/api/acct/account/types";

const text = ref('');
const loading = ref(false);
const saving = ref(false);
const result = ref<AiParseResultVO | null>(null);

// 科目 picker(复用 AddAccount 的两列联动)
const show = ref(false);
const pickTarget = ref<'biz' | 'pay'>('biz');
const columns = reactive([
    [{ id: '001', label: '资产' }, { id: '002', label: '负债' }, { id: '003', label: '收入' }, { id: '004', label: '支出' }]
]);
const columnData = reactive([]);
const uPickerRef = ref(null);

const changeHandler = (e: any) => {
    const { columnIndex, index } = e;
    if (columnIndex === 0) {
        uPickerRef.value.setColumnValues(1, columnData[index]);
    }
};

// 类型展示
const typeTagText = computed(() => {
    const map: Record<string, string> = { income: '收入', expense: '支出', transfer: '转账' };
    return map[result.value?.type || ''] || '未知';
});
const typeTagType = computed(() => {
    const map: Record<string, string> = { income: 'success', expense: 'error', transfer: 'primary' };
    return map[result.value?.type || ''] || 'info';
});
// 业务科目/账户的标签随类型变化
const bizLabel = computed(() => result.value?.type === 'transfer' ? '转出账户' : (result.value?.type === 'income' ? '收入类型' : '支出类型'));
const payLabel = computed(() => result.value?.type === 'transfer' ? '转入账户' : (result.value?.type === 'income' ? '收到账户' : '付款账户'));

// 调后端解析
const parse = async () => {
    if (!text.value.trim()) {
        uni.showToast({ title: '请输入记账描述', icon: 'none' });
        return;
    }
    loading.value = true;
    try {
        const res = await aiParseApi(text.value);
        // axios 封装的返回结构: res.data 为业务数据
        result.value = (res as any).data || (res as any);
    } catch (e) {
        uni.showToast({ title: '解析失败, 请重试', icon: 'error' });
    } finally {
        loading.value = false;
    }
};

// 打开科目选择
const openPicker = (target: 'biz' | 'pay') => {
    pickTarget.value = target;
    show.value = true;
    uni.hideKeyboard();
};
const confirmPicker = (e: any) => {
    const picked = e.value[1]; // 二级科目 { value: code, label: name }
    if (result.value && picked) {
        if (pickTarget.value === 'biz') {
            result.value.bizAccountCode = picked.value;
            result.value.bizAccountName = picked.label;
        } else {
            result.value.payAccountCode = picked.value;
            result.value.payAccountName = picked.label;
        }
        // 用户手动改了科目, 撤销兜底标记
        result.value.fallback = false;
    }
    show.value = false;
};

// 确认记账: 复用 saveApi, 后端按借/贷科目拆分录
const confirm = async () => {
    if (!result.value) return;
    saving.value = true;
    try {
        await saveApi({
            debitAccount: result.value.debitAccount,
            creditAccount: result.value.creditAccount,
            amt: Number(result.value.amt),
            remark: result.value.remark,
            type: 1
        } as any);
        uni.showToast({ title: '记账成功', icon: 'success' });
        setTimeout(() => uni.navigateBack(), 800);
    } catch (e) {
        uni.showToast({ title: '记账失败', icon: 'error' });
    } finally {
        saving.value = false;
    }
};

onMounted(async () => {
    // 加载科目两列数据(供预览阶段手动改科目)
    const res = await getAccountClsSelect();
    res.forEach((item: any) => {
        const tmpOptions: ComponentOptions = reactive([]);
        tmpOptions.push(...item.children);
        columnData.push(tmpOptions);
    });
});

onLoad(() => { });
</script>

<style lang="scss" scoped>
.ai-book {
    padding: 16px;

    .input-section {
        display: flex;
        flex-direction: column;
        gap: 12px;
    }

    .preview {
        margin-top: 24px;

        .type-tag {
            display: flex;
            gap: 8px;
            margin-bottom: 16px;
        }
    }
}
</style>
