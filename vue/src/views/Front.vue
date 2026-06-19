<template>
<div>
  <div style="height: 60px; background: linear-gradient(135deg, #4a7cf7, #6c8cf5); display: flex; align-items: center; border-bottom: 1px solid #ddd">
    <div style="padding-left: 20px; display: flex; align-items: center">
      <img src="@/assets/imgs/logo.png" alt="" style="width: 40px">
      <div style="font-weight: bold; font-size: 22px; margin-left: 5px; color: #fff">校园小卖部</div>
    </div>
    <div style="display: flex; align-items: center; gap: 30px; margin-left: 150px">
      <span :style="{color: route.path === '/front/home' ? '#fff' : 'rgba(255,255,255,0.8)', fontSize: '15px', cursor: 'pointer', borderBottom: route.path === '/front/home' ? '2px solid #fff' : '2px solid transparent', paddingBottom: '2px'}" @click="router.push('/front/home')">首页</span>
      <span :style="{color: route.path === '/front/jingxuan' ? '#fff' : 'rgba(255,255,255,0.8)', fontSize: '15px', cursor: 'pointer', borderBottom: route.path === '/front/jingxuan' ? '2px solid #fff' : '2px solid transparent', paddingBottom: '2px'}" @click="router.push('/front/jingxuan')">精选商品</span>
      <span :style="{color: route.path === '/front/cart' ? '#fff' : 'rgba(255,255,255,0.8)', fontSize: '15px', cursor: 'pointer', borderBottom: route.path === '/front/cart' ? '2px solid #fff' : '2px solid transparent', paddingBottom: '2px'}" @click="router.push('/front/cart')">购物车</span>
      <span :style="{color: route.path === '/front/orders' ? '#fff' : 'rgba(255,255,255,0.8)', fontSize: '15px', cursor: 'pointer', borderBottom: route.path === '/front/orders' ? '2px solid #fff' : '2px solid transparent', paddingBottom: '2px'}" @click="router.push('/front/orders')">我的订单</span>
    </div>
    <div style="flex: 1; display: flex; justify-content: flex-end; padding-right: 20px; align-items: center">
      <el-input v-model="searchKey" placeholder="请输入商品名称查询" size="small" style="width: 260px" @keyup.enter="goSearch">
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      <el-button type="primary" size="small" style="margin-left: 8px; background: #fff; color: #4a7cf7; border: none" @click="goSearch">搜索</el-button>
      <!-- 购物车图标 -->
      <div style="margin-left: 20px; cursor: pointer; position: relative" @click="router.push('/front/cart')">
        <span style="font-size: 22px">🛒</span>
        <span v-if="data.cartCount > 0" style="position: absolute; top: -8px; right: -10px; background: #ff4d4f; color: #fff; border-radius: 10px; font-size: 11px; padding: 1px 6px; min-width: 16px; text-align: center">{{ data.cartCount }}</span>
      </div>
    </div>
    <div style="width: fit-content; padding-right: 10px; ">
      <el-dropdown>
        <div style="display: flex; align-items: center;">
          <img style="width: 40px; height: 40px; border-radius: 50%" :src="data.user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt="">
          <span style="color: #fff; margin-left: 5px">{{ data.user.name || '代码小白' }}</span>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click.native="router.push('/front/person')">个人信息</el-dropdown-item>
            <el-dropdown-item @click.native="router.push('/front/password')">修改密码</el-dropdown-item>
            <el-dropdown-item @click.native="router.push('/front/collect')">我的收藏</el-dropdown-item>
            <el-dropdown-item @click.native="router.push('/front/recharge')">我的充值</el-dropdown-item>
            <el-dropdown-item @click.native="router.push('/front/orders')">我的订单</el-dropdown-item>
            <el-dropdown-item @click.native="logout">退出系统</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>

    </div>
  </div>
  <div style="background-color:#d7dbfa ">
    <router-view  @updateUser="updateUser"/>
  </div>
</div>

</template>
<script setup>
import { reactive, ref, onMounted, watch } from "vue";
import router from "@/router";
import { useRoute } from "vue-router";

const route = useRoute()
import {ElMessage} from "element-plus";
import { Search } from "@element-plus/icons-vue";
import request from "@/utils/request";

const searchKey = ref('')

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
  cartCount: 0
})

// 加载购物车数量
const loadCartCount = () => {
  if (!data.user.id) return
  request.get('/cart/selectAll', { params: { userId: data.user.id } }).then(res => {
    if (res.code === '200') {
      data.cartCount = (res.data || []).length
    }
  })
}

onMounted(() => {
  loadCartCount()
})

// 监听路由变化，每次切换页面时刷新购物车数量
watch(() => route.path, () => {
  loadCartCount()
})

const goSearch = () => {
  if (searchKey.value.trim()) {
    router.push('/front/home?name=' + encodeURIComponent(searchKey.value.trim()))
  }
}

const logout = () => {
  localStorage.removeItem('system-user')
  router.push('/login')
  ElMessage.success('退出成功')
}
</script>

<style>
.el-tootip__trigger{
  outline: none;
  cursor: pointer;
  border: none;

}
</style>