<script name="MenuIndex" setup lang="ts">
import { ContentWrap } from '@/components/ContentWrap'
import { Search } from '@/components/Search'
import { Dialog } from '@/components/Dialog'
import { useI18n } from '@/hooks/web/useI18n'
import { ElButton, ElTag } from 'element-plus'
import { Table } from '@/components/Table'
import { getTableListApi, saveTableApi, delTableListApi } from '@/api/acct/account-cls'
import { useTable } from '@/hooks/web/useTable'
import { AccountClsVO } from '@/api/acct/account-cls/types'
import { ref, unref, reactive, h } from 'vue'
import AddOrUpdate from './components/AddOrUpdate.vue'
import Detail from './components/Detail.vue'
import { CrudSchema, useCrudSchemas } from '@/hooks/web/useCrudSchemas'
import { TableColumn } from '@/types/table'

const { register, tableObject, methods } = useTable<AccountClsVO>({
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
    field: 'setYear',
    label: '核算年度',
    search: {
      show: true
    },
    form: {
      colProps: {
        span: 24
      },
      formItemProps: {
        required: true
      }
    },
    detail: {
      span: 24
    }
  },
  {
    field: 'code',
    label: '预算指标核算科目代码',
    search: {
      show: true
    },
    form: {
      colProps: {
        span: 24
      },
      formItemProps: {
        required: true
      }
    },
    detail: {
      span: 24
    }
  },
  {
    field: 'name',
    label: '预算指标核算科目名称',
    search: {
      show: true
    },
    form: {
      colProps: {
        span: 24
      },
      formItemProps: {
        required: true
      }
    },
    detail: {
      span: 24
    }
  },
  {
    field: 'remark',
    label: '备注',
    search: {
      show: false
    },
    form: {
      colProps: {
        span: 24
      }
    },
    detail: {
      span: 24
    }
  },
  {
    field: 'isStandard',
    label: '是否标准',
    search: {
      show: false
    },
    form: {
      show: false
    },
    detail: {
      span: 24
    },
    formatter: (_: Recordable, __: TableColumn, cellValue: number) => {
      let standard = cellValue === 1 ? '标准代码集' : '扩展代码集'
      return h(
        ElTag,
        {
          type: cellValue === 1 ? 'success' : 'info'
        },
        () => standard
      )
    }
  },
  {
    field: 'parentId',
    label: '上级科目唯一标识',
    search: {
      show: false
    },
    form: {
      show: false
    },
    detail: {
      span: 24
    }
  },
  {
    field: 'levelNo',
    label: '级次',
    search: {
      show: false
    },
    form: {
      show: true
    },
    detail: {
      span: 24
    }
  },
  {
    field: 'isLeaf',
    label: '是否末级',
    search: {
      show: false
    },
    form: {
      show: true
    },
    detail: {
      span: 24
    },
    formatter: (_: Recordable, __: TableColumn, cellValue: number) => {
      let leaf = cellValue === 1 ? '是' : '否'
      return h(
        ElTag,
        {
          type: cellValue === 1 ? 'success' : 'danger'
        },
        () => leaf
      )
    }
  },
  {
    field: 'icon',
    label: '图标',
    search: {
      show: false
    },
    form: {
      colProps: {
        span: 24
      }
    },
    detail: {
      span: 24
    }
  }
])

const { allSchemas } = useCrudSchemas(crudSchemas)

const dialogVisible = ref(false)

const dialogTitle = ref('')

const AddAction = (row: TableData | null) => {
  dialogTitle.value = t('exampleDemo.add')
  // 外部新增按钮只能增加1级科目 0: 目录, 1: 科目, 2: 按钮
  if (row) {
    row.menuType = 0
    row.parentId = 0
  }
  tableObject.currentRow = row
  dialogVisible.value = true
  actionType.value = ''
}

const delLoading = ref(false)

const delData = async (row: TableData | null, multiple: boolean) => {
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

const actionType = ref('')

const action = (row: AccountClsVO, type: string) => {
  dialogTitle.value = t(type === 'edit' ? 'exampleDemo.edit' : 'exampleDemo.detail')
  actionType.value = type
  if (row) {
    row.parentId = row.id
  } else if (!!!row.parentId) {
    row.parentId = 0
  }
  tableObject.currentRow = row
  dialogVisible.value = true
}

const writeRef = ref<ComponentRef<typeof AddOrUpdate>>()

const loading = ref(false)

const save = async () => {
  const write = unref(writeRef)
  await write?.elFormRef?.validate(async (isValid) => {
    if (isValid) {
      loading.value = true
      const data = (await write?.getFormData()) as AccountClsVO
      const res = await saveTableApi(data)
        .catch(() => {})
        .finally(() => {
          loading.value = false
        })
      if (res) {
        dialogVisible.value = false
        tableObject.currentPage = 1
        getList()
      }
    }
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
      <ElButton type="primary" @click="AddAction">{{ t('exampleDemo.add') }}</ElButton>
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
        <ElButton type="primary" @click="action(row, 'edit')">
          {{ t('exampleDemo.edit') }}
        </ElButton>
        <ElButton type="success" @click="AddAction(row)">
          {{ t('exampleDemo.add') }}
        </ElButton>
        <ElButton v-if="row.createdBy != 'system'" type="danger" @click="delData(row, false)">
          {{ t('exampleDemo.del') }}
        </ElButton>
      </template>
    </Table>
  </ContentWrap>

  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <AddOrUpdate
      v-if="actionType !== 'detail'"
      ref="writeRef"
      :form-schema="allSchemas.formSchema"
      :current-row="tableObject.currentRow"
    />

    <Detail
      v-if="actionType === 'detail'"
      :detail-schema="allSchemas.detailSchema"
      :current-row="tableObject.currentRow"
    />

    <template #footer>
      <ElButton v-if="actionType !== 'detail'" type="primary" :loading="loading" @click="save">
        {{ t('exampleDemo.save') }}
      </ElButton>
      <ElButton @click="dialogVisible = false">{{ t('dialogDemo.close') }}</ElButton>
    </template>
  </Dialog>
</template>
