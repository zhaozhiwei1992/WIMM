<script name="UserIndex" setup lang="ts">
import { ContentWrap } from '@/components/ContentWrap'
import { Search } from '@/components/Search'
import { useI18n } from '@/hooks/web/useI18n'
import { ElButton } from 'element-plus'
import { Table } from '@/components/Table'
import { getTableListApi, delTableListApi } from '@/api/acct/account'
import { useTable } from '@/hooks/web/useTable'
import { AccountVO } from '@/api/acct/account/types'
import { ref, reactive } from 'vue'
import { CrudSchema, useCrudSchemas } from '@/hooks/web/useCrudSchemas'

const { register, tableObject, methods } = useTable<AccountVO>({
  getListApi: getTableListApi,
  delListApi: delTableListApi,
  response: {
    list: 'list',
    total: 'total'
  },
  defaultParams: {
    title: 's'
  }
})

const { getList, setSearchParams } = methods

getList()

const { t } = useI18n()

const crudSchemas = reactive<CrudSchema[]>([
  {
    field: 'id',
    label: '编号',
    type: 'index',
    form: {
      show: false
    },
    detail: {
      show: false
    }
  },
  {
    field: 'createdDate',
    label: '创建日期',
    search: {
      show: true
    }
  },
  {
    field: 'createdBy',
    label: '创建人',
    form: {
      show: false
    }
  },
  {
    field: 'creditAccount',
    label: '贷方科目',
    form: {
      formItemProps: {
        required: true
      }
    }
  },
  {
    field: 'debitAccount',
    label: '借方科目',
    form: {
      formItemProps: {
        required: true
      }
    }
  },
  {
    field: 'amt',
    label: '金额',
    form: {
      formItemProps: {
        required: true
      }
    }
  },
  {
    field: 'remark',
    label: '摘要',
    form: {
      formItemProps: {
        required: true
      }
    }
  },
  {
    field: 'action',
    width: '260px',
    label: t('tableDemo.action'),
    form: {
      show: false
    },
    detail: {
      show: false
    }
  }
])

const { allSchemas } = useCrudSchemas(crudSchemas)

const delLoading = ref(false)

const delData = async (row: AccountVO | null, multiple: boolean) => {
  tableObject.currentRow = row
  const { delList, getSelections } = methods
  const selections = await getSelections()
  delLoading.value = true
  await delList(
    multiple ? selections.map((v) => v.id) : [tableObject.currentRow?.id as string],
    multiple
  ).finally(() => {
    delLoading.value = false
  })
}
</script>

<template>
  <ContentWrap>
    <Search
      :model="{ title: 's' }"
      :schema="allSchemas.searchSchema"
      @search="setSearchParams"
      @reset="setSearchParams"
    />

    <div class="mb-10px">
      <ElButton :loading="delLoading" type="danger" @click="delData(null, true)">
        {{ t('exampleDemo.del') }}
      </ElButton>
    </div>

    <Table
      v-model:pageSize="tableObject.pageSize"
      v-model:currentPage="tableObject.currentPage"
      :columns="allSchemas.tableColumns"
      :data="tableObject.tableList"
      :loading="tableObject.loading"
      :pagination="{
        total: tableObject.total
      }"
      @register="register"
      row-key="id"
      border
    >
      <template #action="{ row }">
        <ElButton v-if="row.createdBy != 'system'" type="danger" @click="delData(row, false)">
          {{ t('exampleDemo.del') }}
        </ElButton>
      </template>
    </Table>
  </ContentWrap>
</template>
