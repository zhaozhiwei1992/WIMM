<!-- https://limeui.qcoon.cn/#/echart-example -->
<template>
	<view style="height: 750rpx">
		<l-echart ref="chartRef"></l-echart>
	</view>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import * as echarts from 'echarts'
import { getExpPieDataApi } from '@/api/workplace';

const chartRef = ref(null)

const option = {
	title: {
		text: '支出统计图',
		subtext: '',
		left: 'center'
	},
	tooltip: {
		trigger: 'item'
	},
	legend: {
		orient: 'vertical',
		left: 'left',
	},
	series: [
		{
			name: '访问来源',
			type: 'pie',
			radius: '50%',
			data: [
				{ value: 1048, name: '搜索引擎' },
				{ value: 735, name: '直接访问' },
				{ value: 580, name: '邮件营销' },
				{ value: 484, name: '联盟广告' },
				{ value: 300, name: '视频广告' }
			],
			emphasis: {
				itemStyle: {
					shadowBlur: 10,
					shadowOffsetX: 0,
					shadowColor: 'rgba(0, 0, 0, 0.5)'
				}
			}
		}
	]
}

// 收入饼图
const getExpPieData = async () => {
  const res = await getExpPieDataApi().catch(() => {})
  if (res) {
    option.series[0].data = res.map((v) => {
      return {
        name: v.name,
        value: v.value
      }
    })
  }
}

onMounted(() => {
	getExpPieData()
	// 组件能被调用必须是组件的节点已经被渲染到页面上
	setTimeout(async () => {
		if (!chartRef.value) return
		const myChart = await chartRef.value.init(echarts)
		myChart.setOption(option)
	}, 300)
})
</script>
