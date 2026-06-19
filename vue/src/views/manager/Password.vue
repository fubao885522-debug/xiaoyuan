<template>
  <div class="front-container" style="width: 100%; display: flex; justify-content: center">
  <div class="card" style="width: 700px;height: 500px">
    <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px">
      <el-form-item prop="password" label="原密码">
        <el-input v-model="data.form.password" show-password autocomplete="off" />
      </el-form-item>
      <el-form-item prop="newPassword" label="新密码">
        <el-input v-model="data.form.newPassword" show-password autocomplete="off" />
      </el-form-item>
      <el-form-item prop="confirmPassword" label="新密码">
        <el-input v-model="data.form.confirmPassword" show-password autocomplete="off" />
      </el-form-item>
      <div style="text-align: center; padding-bottom: 10px">
        <el-button type="primary" @click="save">保 存</el-button>
      </div>
    </el-form>
  </div>
</div>
</template>

<script setup>
import { reactive, getCurrentInstance, onMounted } from "vue";
import request from "@/utils/request";
import { ElMessage } from "element-plus";
import router from "@/router";

const { proxy } = getCurrentInstance()

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
  form: {
    password: '',
    newPassword: '',
    confirmPassword: ''
  },
  rules: {
    password: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
    newPassword: [
      { required: true, message: '请输入新密码', trigger: 'blur' },
      { min: 2, max: 20, message: '长度在 2到 20 个字符', trigger: 'blur' }
    ],
    confirmPassword: [
      { required: true, message: '请再次输入新密码', trigger: 'blur' },
      {
        validator: (rule, value, callback) => {
          if (value !== data.form.newPassword) {
            callback(new Error('两次输入的密码不一致'))
          } else {
            callback()
          }
        },
        trigger: 'blur'
      }
    ]
  }
})

// 页面加载时自动获取原密码并回填
onMounted(() => {
  const url = data.user.role === '管理员' ? '/admin/selectById/' : '/user/selectById/'
  request.get(url + data.user.id).then(res => {
    if (res.code === '200') {
      data.form.password = res.data.password
    }
  })
})

function save() {
  proxy.$refs.formRef.validate((valid) => {
    if (valid) {
      request.put('/updatePassword', {
        id: data.user.id,
        username: data.user.username,
        password: data.form.password,
        newPassword: data.form.newPassword,
        role: data.user.role
      }).then(res => {
        if (res.code === '200') {
          ElMessage.success('修改成功，请重新登录')
          localStorage.removeItem('system-user')
          router.push('/login')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
.card {
  margin: 10px;
  background: #fff;
  border-radius: 4px;
}
</style>
