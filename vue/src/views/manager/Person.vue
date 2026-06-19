<template>
  <div class="front-container" style="width: 100%; display: flex; justify-content: center">
    <div class="card" style="width: 1000px;height: 500px">
      <h2 style="margin-bottom: 30px; color: #333; text-align: center">个人信息</h2>
      <el-form :model="data.form" label-width="100px" style="padding-right: 30px">
        <el-form-item label="头像" prop="avatar">
          <div style="display: flex; align-items: center; gap: 20px">
            <el-upload :action="uploadUrl" list-type="picture" :on-success="handleImgSuccess" :show-file-list="false">
              <el-button type="primary">更换头像</el-button>
            </el-upload>
            <el-image v-if="data.form.avatar" :src="data.form.avatar"
                      style="width: 80px; height: 80px; border-radius: 50%"
                      fit="cover">
            </el-image>
            <el-image v-else src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
                      style="width: 80px; height: 80px; border-radius: 50%"
                      fit="cover">
            </el-image>
          </div>
        </el-form-item>
        <el-form-item label="账号" prop="username">
          <el-input disabled v-model="data.form.username" autocomplete="off" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item>
          <div style="display: flex; justify-content: center; gap: 20px">
            <el-button type="primary" @click="save" style="width: 120px">保存</el-button>
            <el-button @click="reset" style="width: 120px">重置</el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {reactive, onMounted, getCurrentInstance} from "vue";
import request from "@/utils/request";
import {ElMessage} from "element-plus";

const { proxy } = getCurrentInstance()

const uploadUrl = import.meta.env.VITE_BASE_URL + '/files/upload'

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
  form: {}
})

onMounted(() => {
  data.form = JSON.parse(JSON.stringify(data.user))
})

const save = () => {
  request.put('/admin/update', data.form).then(res => {
    if (res.code === '200') {
      const updatedUser = JSON.parse(JSON.stringify(data.form))
      localStorage.setItem('system-user', JSON.stringify(updatedUser))
      data.user = updatedUser
      proxy.$emit('updateUser', updatedUser)
      ElMessage.success('保存成功')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const reset = () => {
  data.form = JSON.parse(JSON.stringify(data.user))
  ElMessage.info('已重置')
}

const handleImgSuccess = (res) => {
  data.form.avatar = res.data
}
</script>

<style scoped>
.card {
  background-color: #fff;
  border-radius: 10px;
  padding: 30px;
  margin: 20px 0;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>