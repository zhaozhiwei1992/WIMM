<script setup lang="ts">
import { propTypes } from '@/utils/propTypes'
import { ElSwitch } from 'element-plus'
import { ref } from 'vue'
import { useAppStore } from '@/store/modules/app'

defineProps({
  color: propTypes.string.def('')
})

const appStore = useAppStore()

const value6 = ref(true)

const handleChange = () => {
  appStore.setAcctCate(value6.value)
}
</script>

<template>
  <div>
    <el-switch
      v-model="value6"
      class="ml-2"
      width="60"
      inline-prompt
      active-text="家庭"
      inactive-text="个人"
      @change="handleChange"
    />
  </div>
</template>
