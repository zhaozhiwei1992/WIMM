<script setup lang="ts">
import { getMenuRouteListApi, loginApi } from '@/api/login'
import { UserType } from '@/api/login/types'
import { Form } from '@/components/Form'
import { useCache } from '@/hooks/web/useCache'
import { useForm } from '@/hooks/web/useForm'
import { useI18n } from '@/hooks/web/useI18n'
import { useValidator } from '@/hooks/web/useValidator'
import { useAppStore } from '@/store/modules/app'
import { usePermissionStore } from '@/store/modules/permission'
import { FormSchema } from '@/types/form'
import * as authUtil from '@/utils/auth'
import { ElButton, ElCheckbox, ElLink } from 'element-plus'
import { reactive, ref, unref, watch } from 'vue'
import type { RouteLocationNormalizedLoaded, RouteRecordRaw } from 'vue-router'
import { useRouter } from 'vue-router'

const { required } = useValidator()

const emit = defineEmits(['to-register'])

const appStore = useAppStore()

const permissionStore = usePermissionStore()

const { currentRoute, addRoute, push } = useRouter()

const { wsCache } = useCache()

const { t } = useI18n()

const rules = {
  username: [required()],
  password: [required()]
}

const schema = reactive<FormSchema[]>([
  {
    field: 'title',
    colProps: {
      span: 24
    }
  },
  {
    field: 'username',
    label: '',
    // 测试填默认用户
    // value: 'admin',
    component: 'Input',
    colProps: {
      span: 24
    },
    componentProps: {
      placeholder: t('login.username')
    }
  },
  {
    field: 'password',
    label: '',
    // value: 'admin',
    component: 'InputPassword',
    colProps: {
      span: 24
    },
    componentProps: {
      style: {
        width: '100%'
      },
      placeholder: t('login.password')
    }
  },
  {
    field: 'captcha',
    label: '',
    component: 'ImageCaptcha',
    colProps: {
      span: 24
    },
    componentProps: {
      style: {
        width: '100%'
      },
      placeholder: '验证码'
    }
  },
  {
    field: 'tool',
    colProps: {
      span: 24
    }
  },
  {
    field: 'login',
    colProps: {
      span: 24
    }
  },
  {
    field: 'other',
    component: 'Divider',
    label: t('login.otherLogin'),
    componentProps: {
      contentPosition: 'center'
    }
  },
  {
    field: 'otherIcon',
    colProps: {
      span: 24
    }
  }
])

const remember = ref(false)

const { register, elFormRef, methods } = useForm()

const loading = ref(false)

const redirect = ref<string>('')

watch(
  () => currentRoute.value,
  (route: RouteLocationNormalizedLoaded) => {
    redirect.value = route?.query?.redirect as string
  },
  {
    immediate: true
  }
)

// 登录
const signIn = async () => {
  const formRef = unref(elFormRef)
  await formRef?.validate(async (isValid) => {
    if (isValid) {
      loading.value = true
      const { getFormData } = methods
      const formData = await getFormData<UserType>()

      try {
        const res = await loginApi(formData)

        if (res) {
          // 返回用户信息, 带token
          wsCache.set(appStore.getUserInfo, res)
          // 设置token id
          // wsCache.set('token', res.data.token)
          authUtil.setToken(res)
          // 设置租户id
          wsCache.set('TENANT_ID', res.tenantId)
          // 使用后端动态路由只需要把这里放开
          appStore.setDynamicRouter(true)
          if (appStore.getDynamicRouter) {
            getRole()
          } else {
            // 根据传入不同参数生成不同路由表, none是所有的静态路由
            await permissionStore.generateRoutes('none').catch(() => {})
            permissionStore.getAddRouters.forEach((route) => {
              addRoute(route as RouteRecordRaw) // 动态添加可访问路由表
            })
            permissionStore.setIsAddRouters(true)

            // 登录后跳转指定地址, 或者路由第一个地址, 后端权限控制, 管理员是分析页, 普通用户是工作页
            push({ path: redirect.value || permissionStore.addRouters[0].path })
          }
        }
      } finally {
        loading.value = false
      }
    }
  })
}

// 获取角色信息
const getRole = async () => {
  const { getFormData } = methods
  const formData = await getFormData<UserType>()
  const params = {
    roleName: formData.username
  }
  // admin - 模拟后端过滤菜单
  // test - 模拟前端过滤菜单
  // const res =
  //   formData.username === 'admin' ? await getAdminRoleApi(params) : await getTestRoleApi(params)
  // console.log('params', params)
  const res = await getMenuRouteListApi(params)
  if (res) {
    const { wsCache } = useCache()
    const routers = res || []
    // console.log('返回routers信息', res)
    wsCache.set('roleRouters', routers)

    // 全部都走后端路由
    await permissionStore.generateRoutes('admin', routers).catch(() => {})
    // formData.username === 'admin'
    //   ? await permissionStore.generateRoutes('admin', routers).catch(() => {})
    //   : await permissionStore.generateRoutes('test', routers).catch(() => {})

    permissionStore.getAddRouters.forEach((route) => {
      addRoute(route as RouteRecordRaw) // 动态添加可访问路由表
    })
    permissionStore.setIsAddRouters(true)
    // 登录后跳转指定地址, 或者路由第一个地址, 后端权限控制, 管理员是分析页, 普通用户是工作页
    console.log(redirect.value)
    console.log(permissionStore.addRouters[0].path)
    push({ path: '/dashboard/workplace' })
  }
}

// 去注册页面
const toRegister = () => {
  emit('to-register')
}
</script>

<template>
  <Form
    :schema="schema"
    :rules="rules"
    label-position="top"
    hide-required-asterisk
    size="large"
    class="dark:(border-1 border-[var(--el-border-color)] border-solid)"
    @register="register"
  >
    <template #title>
      <h2 class="text-2xl font-bold text-center w-[100%]">{{ t('login.login') }}</h2>
    </template>

    <template #tool>
      <div class="flex justify-between items-center w-[100%]">
        <ElCheckbox v-model="remember" :label="t('login.remember')" size="small" />
        <ElLink type="primary" :underline="false">{{ t('login.forgetPassword') }}</ElLink>
      </div>
    </template>

    <template #login>
      <div class="w-[100%]">
        <ElButton :loading="loading" type="primary" class="w-[100%]" @click="signIn">
          {{ t('login.login') }}
        </ElButton>
      </div>
    </template>
  </Form>
</template>

<style lang="less" scoped>
:deep(.anticon) {
  &:hover {
    color: var(--el-color-primary) !important;
  }
}
</style>
