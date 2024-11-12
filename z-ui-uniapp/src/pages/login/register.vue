<template>
	<view>
		<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
		<up-form
				labelPosition="left"
				:model="model1"
				:rules="rules"
				ref="form1"
		>
			<up-form-item
					label="姓名"
					prop="userInfo.name"
					borderBottom
					ref="item1"
			>
				<up-input
						v-model="userInfo.name"
						border="none"
				></up-input>
			</up-form-item>
			<up-form-item
					label="登录名"
					prop="userInfo.login"
					borderBottom
					ref="item1"
			>
				<up-input
						v-model="userInfo.login"
						border="none"
				></up-input>
			</up-form-item>
      <up-button @click="save" text="注册"></up-button>
      <up-button @click="toLogin" text="去登录"></up-button>
		</up-form>
	</view>
</template>

<script setup>  
import { ref, reactive } from 'vue';  

const userInfo = reactive({
  name: '',  
  login: '',
})

const rules =  reactive({  
    'userInfo.name': {  
      type: 'string',  
      required: true,  
      message: '请填写姓名',  
      trigger: ['blur', 'change'],  
    },
    'userInfo.login': {  
      type: 'string',  
      required: true,  
      message: '请填写登录名',  
      trigger: ['blur', 'change'],  
    }
})
  
// 使用 ref 创建响应式引用  
const formRef = ref(null);  
  
// 定义方法  
function sexSelect(e) {  
  state.model1.userInfo.sex = e.name;  
  if (formRef.value) {  
    formRef.value.validateField('userInfo.sex');  
  }  
}  

// 注册
const save = async () => {
    console.log('save', form)
}

const toLogin = () => {
  uni.navigateTo({
    url: '/pages/login'
  })
}
</script>