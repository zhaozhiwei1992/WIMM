<script setup lang="ts">
import { ElRow, ElCol, ElSkeleton, ElCard, ElDivider, ElLink } from 'element-plus'
import { useI18n } from '@/hooks/web/useI18n'
import { useAppStore } from '@/store/modules/app'
import { ref, reactive, onMounted } from 'vue'
import { CountTo } from '@/components/CountTo'
import { Echart } from '@/components/Echart'
import { EChartsOption } from 'echarts'
import { lineOptions, pieOptions } from './echarts-data'
import { getAssetsApi } from '@/api/dashboard/workplace'
import { set } from 'lodash-es'
import { useCache } from '@/hooks/web/useCache'
import {
  getIncPieDataApi,
  getExpPieDataApi,
  getMonthlyIncExpDataApi
} from '@/api/dashboard/workplace'
import { cloneDeep } from 'lodash-es'

const loading = ref(true)

// 总资产：Total Assets
let totalAssets = ref(0)
// 总负债：Total Liabilities
let totalLiabilities = ref(0)
//净资产：Net Worth
let netWorth = ref(0)
const getAsset = async () => {
  const res = await getAssetsApi().catch(() => {})
  totalAssets.value = res?.totalAssets || 0
  totalLiabilities.value = res?.totalLiabilities || 0
  netWorth.value = res?.netWorth || 0
}

// 收入支出类别统计
let incPieData = reactive<EChartsOption>(cloneDeep(pieOptions)) as EChartsOption

// 收入饼图
const getIncPieData = async () => {
  const res = await getIncPieDataApi().catch(() => {})
  if (res) {
    set(
      incPieData,
      'legend.data',
      res.map((v) => t(v.name))
    )
    incPieData!.title!.text = '收入统计图'
    incPieData!.series![0].data = res.map((v) => {
      return {
        name: t(v.name),
        value: v.value
      }
    })
  }
}

let expPieData = reactive<EChartsOption>(cloneDeep(pieOptions)) as EChartsOption
const getExpPieData = async () => {
  const res = await getExpPieDataApi().catch(() => {})
  if (res) {
    set(
      expPieData,
      'legend.data',
      res.map((v) => t(v.name))
    )
    expPieData!.title!.text = '支出统计图'
    expPieData!.series![0].data = res.map((v) => {
      return {
        name: t(v.name),
        value: v.value
      }
    })
  }
}

// 收入支出趋势统计, 按月
let incExpMonthLineData = reactive<EChartsOption>(lineOptions) as EChartsOption

// 每月收入支出统计折线图
const getMonthlyIncExpData = async () => {
  const res = await getMonthlyIncExpDataApi().catch(() => {})
  if (res) {
    set(
      incExpMonthLineData,
      'xAxis.data',
      res.map((v) => t(v.name))
    )
    set(incExpMonthLineData, 'title.text', '收入支出趋势统计(月)')
    set(incExpMonthLineData, 'series', [
      {
        name: t('workspace.inc'),
        smooth: true,
        type: 'line',
        data: res.map((v) => v.line1),
        animationDuration: 2800,
        animationEasing: 'cubicInOut'
      },
      {
        name: t('workspace.exp'),
        smooth: true,
        type: 'line',
        itemStyle: {},
        data: res.map((v) => v.line2),
        animationDuration: 2800,
        animationEasing: 'quadraticOut'
      }
    ])
  }
}

const getAllApi = async () => {
  await Promise.all([getMonthlyIncExpData(), getAsset(), getIncPieData(), getExpPieData()])
  loading.value = false
}

getAllApi()

const { t } = useI18n()

const { wsCache } = useCache()

const appStore = useAppStore()

// 头像
const avatarImageUrl = ref('@/assets/imgs/avatar.jpg')

// 用户名
const username = ref('Archer')

onMounted(() => {
  // captchaImageUrl.value = 'data:image/png;base64,' + response.data
  avatarImageUrl.value = 'data:image/png;base64,' + wsCache.get(appStore.getUserInfo).avatar
  username.value = wsCache.get(appStore.getUserInfo).username
})
</script>

<template>
  <div>
    <ElCard shadow="never">
      <ElSkeleton :loading="loading" animated>
        <ElRow :gutter="20" justify="space-between">
          <!-- 个人信息问候部分 -->
          <ElCol :xl="12" :lg="12" :md="12" :sm="24" :xs="24">
            <div class="flex items-center">
              <img :src="avatarImageUrl" alt="" class="w-70px h-70px rounded-[50%] mr-20px" />
              <div>
                <div class="text-20px text-700">
                  {{ t('workplace.goodMorning') }}，{{ username }}，{{ t('workplace.happyDay') }}
                </div>
                <div class="mt-10px text-14px text-gray-500">
                  {{ t('workplace.toady') }}，0℃ - 100℃！
                </div>
              </div>
            </div>
          </ElCol>
          <!-- 资产负债统计 -->
          <ElCol :xl="12" :lg="12" :md="12" :sm="24" :xs="24">
            <div class="flex h-70px items-center justify-end <sm:mt-20px">
              <div class="px-8px text-right">
                <div class="text-14px text-gray-400 mb-20px">{{ t('workplace.totalAssets') }}</div>
                <CountTo class="text-20px" :start-val="0" :end-val="totalAssets" :duration="2600" />
              </div>
              <ElDivider direction="vertical" />
              <div class="px-8px text-right">
                <div class="text-14px text-gray-400 mb-20px">{{
                  t('workplace.totalLabilities')
                }}</div>
                <CountTo
                  class="text-20px"
                  :start-val="0"
                  :end-val="totalLiabilities"
                  :duration="2600"
                />
              </div>
              <ElDivider direction="vertical" border-style="dashed" />
              <div class="px-8px text-right">
                <div class="text-14px text-gray-400 mb-20px">{{ t('workplace.netWorth') }}</div>
                <CountTo class="text-20px" :start-val="0" :end-val="netWorth" :duration="2600" />
              </div>
            </div>
          </ElCol>
        </ElRow>
      </ElSkeleton>
    </ElCard>
  </div>

  <ElRow class="mt-20px" :gutter="20" justify="space-between">
    <ElCol :xl="8" :lg="8" :md="24" :sm="24" :xs="24" class="mb-20px">
      <!-- 收入分布饼图 -->
      <ElCard shadow="hover" class="mb-20px">
        <template #header>
          <span>收入统计</span>
        </template>
        <ElSkeleton :loading="loading" animated :rows="4">
          <Echart :options="incPieData" :height="350" />
        </ElSkeleton>
      </ElCard>
    </ElCol>
    <ElCol :xl="8" :lg="8" :md="24" :sm="24" :xs="24" class="mb-20px">
      <!-- 支出分布饼图 -->
      <ElCard shadow="hover" class="mb-20px">
        <template #header>
          <span>支出统计</span>
        </template>
        <ElSkeleton :loading="loading" animated :rows="4">
          <Echart :options="expPieData" :height="350" />
        </ElSkeleton>
      </ElCard>
    </ElCol>
    <ElCol :xl="8" :lg="8" :md="24" :sm="24" :xs="24" class="mb-20px">
      <ElCard shadow="never">
        <template #header>
          <span>{{ t('workplace.shortcutOperation') }}</span>
        </template>
        <!-- 快捷操作 快速记账、报表查看 -->
        <ElSkeleton :loading="loading" animated>
          <ElCol :xl="12" :lg="12" :md="12" :sm="24" :xs="24" class="mb-10px">
            <ElLink type="default" :underline="false">
              <!-- 记账 -->
            </ElLink>
          </ElCol>
        </ElSkeleton>
      </ElCard>
    </ElCol>
  </ElRow>

  <ElRow class="mt-20px" :gutter="20" justify="space-between">
    <ElCol :xl="24" :lg="24" :md="24" :sm="24" :xs="24" class="mb-20px">
      <!-- 收入和支出趋势图 -->
      <ElCard shadow="hover" class="mb-20px">
        <template #header>
          <span>收入{{ t('workplace.index') }}</span>
        </template>
        <ElSkeleton :loading="loading" animated :rows="4">
          <Echart :options="incExpMonthLineData" :height="350" />
        </ElSkeleton>
      </ElCard>
    </ElCol>
  </ElRow>
</template>
