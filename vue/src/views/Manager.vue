<template>
  <div>
    <div style="height: 60px; background-color: #75a9e8; display: flex; align-items: center; border-bottom: 1px solid #ddd">
      <div style="flex: 1">
        <div style="padding-left: 20px; display: flex; align-items: center">
          <img src="@/assets/imgs/logo.png" alt="" style="width: 40px">
          <div style="font-weight: bold; font-size: 24px; margin-left: 5px; color: #fff">外卖校园</div>
        </div>
      </div>
      <div style="width: fit-content; padding-right: 10px; display: flex; align-items: center;">
        <img style="width: 40px; height: 40px; border-radius: 50%" :src="data.user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt="">
        <span style="color: #fff; margin-left: 5px">{{ data.user.name || '代码小白' }}</span>
      </div>
    </div>

    <div style="display: flex">
      <div style="width: 200px; border-right: 1px solid #ddd; min-height: calc(100vh - 60px)">
        <el-menu
            router
            style="border: none"
            :default-active="router.currentRoute.value.path"
            :default-openeds="['user','info','interaction','trade']"
        >
          <el-menu-item index="/manager/home">
            <el-icon><HomeFilled /></el-icon>
            <span>系统首页</span>
          </el-menu-item>
          <el-menu-item index="/manager/stats">
            <el-icon><DataAnalysis /></el-icon>
            <span>数据统计</span>
          </el-menu-item>
          <el-sub-menu index="info">
            <template #title>
              <el-icon><Folder /></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/manager/catgory">
              <el-icon><Collection /></el-icon>
              <span>商品分类</span>
            </el-menu-item>
            <el-menu-item index="/manager/goods">
              <el-icon><Goods /></el-icon>
              <span>商品信息</span>
            </el-menu-item>
            <el-menu-item index="/manager/carousel">
              <el-icon><Picture /></el-icon>
              <span>轮播图信息</span>
            </el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="trade">
            <template #title>
              <el-icon><Sell /></el-icon>
              <span>交易管理</span>
            </template>
            <el-menu-item index="/manager/orders">
              <el-icon><Tickets /></el-icon>
              <span>订单管理</span>
            </el-menu-item>
            <el-menu-item index="/manager/recharge">
              <el-icon><Wallet /></el-icon>
              <span>用户充值</span>
            </el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="interaction">
            <template #title>
              <el-icon><ChatDotSquare /></el-icon>
              <span>互动管理</span>
            </template>
            <el-menu-item index="/manager/comment">
              <el-icon><ChatDotRound /></el-icon>
              <span>评论管理</span>
            </el-menu-item>
            <el-menu-item index="/manager/collect">
              <el-icon><Star /></el-icon>
              <span>用户收藏</span>
            </el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="user">
            <template #title>
              <el-icon><UserFilled /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/manager/user">
              <el-icon><User /></el-icon>
              <span>普通用户</span>
            </el-menu-item>
            <el-menu-item index="/manager/admin">
              <el-icon><Avatar /></el-icon>
              <span>管理员信息</span>
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item @click="router.push('/manager/person')">
            <el-icon><User /></el-icon>
            <span>个人信息</span>
          </el-menu-item>
          <el-menu-item index="/manager/password">
            <el-icon><Lock /></el-icon>
            <span>修改密码</span>
          </el-menu-item>
          <el-menu-item @click="logout">
            <el-icon><SwitchButton /></el-icon>
            <span>退出系统</span>
          </el-menu-item>
        </el-menu>
      </div>
      <div style="flex: 1; width: 0; background-color: #f8f8ff; padding: 10px">
        <router-view @updateUser="updateUser" />
      </div>
    </div>

  </div>
</template>

<script setup>
import { reactive } from "vue";
import router from "@/router";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}')
})

if (!data.user?.id) {
  ElMessage.error('请登录！')
  router.push('/login')
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('system-user') || '{}')
}

const logout = () => {
  router.push('/login')
  ElMessage.success('退出成功')
  localStorage.removeItem('code2026-user')
}
</script>

<style scoped>
.el-menu-item.is-active {
  background-color: #c4f488 !important;
}
.el-menu-item:hover {
  color: #f89292;
}
</style>