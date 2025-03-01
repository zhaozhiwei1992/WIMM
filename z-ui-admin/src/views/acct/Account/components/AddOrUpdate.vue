<script name="MenuIndex" setup lang="ts">
import { saveApi } from '@/api/acct/account'
import { getAccountClsSelect } from '@/api/acct/account-cls'
import { AccountVO } from '@/api/acct/account/types'
import { ContentWrap } from '@/components/ContentWrap'
import { useI18n } from '@/hooks/web/useI18n'
import { ComponentOptions } from '@/types/components'
import { onMounted, reactive, ref } from 'vue'

const { t } = useI18n()

const loading = ref(false)

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

const rules = reactive<FormRules<AccountVO>>({
  debitAccount: [{ required: true, message: t('rules.required'), trigger: 'blur' }],
  creditAccount: [{ required: true, message: t('rules.required'), trigger: 'blur' }],
  amt: [{ required: true, message: t('rules.required'), trigger: 'blur' }]
})

const elFormRefExp = ref<FormInstance>()

const save = async () => {
  await elFormRefExp.value?.validate(async (isValid) => {
    if (isValid) {
      loading.value = true
      const res = await saveApi(form)
      if (res) {
        ElMessage.success('保存成功')
        // 清空表单
        resetForm(elFormRefExp.value)
      }
    }
  })
}

const resetForm = (formEl: FormInstance | undefined) => {
  // 重置表单的逻辑
  if (!formEl) return
  formEl.resetFields()
}

const acctClsOptions = reactive<ComponentOptions[] | any>([])

onMounted(async () => {
  const res = await getAccountClsSelect()
  acctClsOptions.push(...res)
})
</script>

<template>
  <ContentWrap>
    <ElForm
      ref="elFormRefExp"
      :model="form"
      :rules="rules"
      label-width="auto"
      style="max-width: 600px"
    >
      <ElFormItem label="贷" prop="creditAccount">
        <ElCascader
          placeholder="支出"
          v-model="form.creditAccount"
          :options="acctClsOptions"
          :props="{ emitPath: false }"
          filterable
        />
      </ElFormItem>
      <ElFormItem label="借" prop="debitAccount">
        <ElCascader
          placeholder="收入"
          v-model="form.debitAccount"
          :options="acctClsOptions"
          :props="{ emitPath: false }"
          filterable
        />
      </ElFormItem>
      <ElFormItem label="金额" prop="amt">
        <ElInput v-model="form.amt" style="width: 240px" placeholder="支出金额，支持运算符" />
      </ElFormItem>
      <ElFormItem label="备注">
        <ElInput v-model="form.remark" type="textarea" prop="remark" />
      </ElFormItem>
      <ElFormItem>
        <ElButton type="primary" @click="save">记账</ElButton>
        <ElButton @click="resetForm(elFormRefExp)">重置</ElButton>
      </ElFormItem>
    </ElForm>
  </ContentWrap>
</template>
