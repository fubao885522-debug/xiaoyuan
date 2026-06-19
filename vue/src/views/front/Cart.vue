<template>
  <div class="front-container" style="min-height: 80vh">
    <div class="card" v-if="data.cartList.length > 0">
      <h3 style="margin-bottom: 20px; padding-bottom: 15px; border-bottom: 1px solid #eee">
        我的购物车（{{ data.cartList.length }}件商品）
      </h3>

      <!-- 购物车列表 -->
      <div style="display: flex; flex-direction: column; gap: 16px">
        <div v-for="item in data.cartList" :key="item.id" class="cart-item">
          <div style="display: flex; align-items: center; gap: 12px; flex: 1">
            <el-image :src="item.goodsImg" style="width: 80px; height: 80px; border-radius: 4px; flex-shrink: 0" fit="cover" />
            <div style="flex: 1; min-width: 0">
              <div class="cart-goods-name">{{ item.goodsName }}</div>
              <div style="color: #ff4d4f; font-size: 16px; font-weight: bold; margin-top: 6px">¥{{ item.price }}</div>
            </div>
          </div>

          <!-- 数量 -->
          <div style="display: flex; align-items: center; gap: 0; margin: 0 30px">
            <el-button size="small" @click="changeNum(item, -1)" :disabled="item.num <= 1">-</el-button>
            <el-input v-model="item.num" size="small" style="width: 60px" :input-style="{ textAlign: 'center' }" @change="updateNum(item)" />
            <el-button size="small" @click="changeNum(item, 1)">+</el-button>
          </div>

          <!-- 小计 -->
          <div style="color: #ff4d4f; font-weight: bold; font-size: 16px; min-width: 80px; text-align: center">
            ¥{{ (item.num * item.price).toFixed(2) }}
          </div>

          <!-- 删除 -->
          <el-button type="danger" size="small" @click="handleDelete(item.id)" style="margin-left: 20px">删除</el-button>
        </div>
      </div>

      <!-- 底部结算栏 -->
      <div style="display: flex; align-items: center; justify-content: flex-end; margin-top: 24px; padding-top: 20px; border-top: 1px solid #eee; gap: 20px">
        <span style="color: #666">合计：</span>
        <span style="color: #ff4d4f; font-size: 22px; font-weight: bold">¥{{ totalPrice }}</span>
        <el-button type="danger" size="large" @click="handleSettle">结算</el-button>
      </div>
    </div>

    <!-- 空购物车 -->
    <div v-else class="card" style="text-align: center; padding: 80px 0">
      <div style="font-size: 60px; margin-bottom: 16px">🛒</div>
      <div style="color: #999; font-size: 16px; margin-bottom: 24px">购物车空空如也，快去逛逛吧~</div>
      <el-button type="primary" @click="router.push('/front/home')">去逛逛</el-button>
    </div>
  </div>
</template>

<script setup>
import request from "@/utils/request";
import { reactive, computed, onMounted, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";

const router = useRouter();
const route = useRoute();
const user = JSON.parse(localStorage.getItem('system-user') || '{}');

const data = reactive({
  cartList: []
})

const totalPrice = computed(() => {
  return data.cartList.reduce((sum, item) => sum + (item.num || 0) * (item.price || 0), 0).toFixed(2)
})

// 加载购物车
const load = () => {
  if (!user.id) {
    router.push('/login')
    return
  }
  request.get('/cart/selectAll', { params: { userId: user.id } }).then(res => {
    if (res.code === '200') {
      data.cartList = res.data || []
    }
  })
}

// 修改数量
const changeNum = (item, delta) => {
  const val = (item.num || 1) + delta
  if (val < 1) return
  item.num = val
  updateNum(item)
}

// 更新数量到后端
const updateNum = (item) => {
  if (!item.num || item.num < 1) {
    item.num = 1
  }
  request.put('/cart/update', { ...item, num: item.num }).then(res => {
    if (res.code !== '200') {
      ElMessage.error(res.msg)
      load()
    }
  })
}

// 删除
const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除该商品吗？', '删除确认', { type: 'warning' }).then(() => {
    request.delete('/cart/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('已删除')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(() => {})
}

// 结算
const handleSettle = () => {
  ElMessageBox.confirm(`确认结算？合计 ¥${totalPrice.value}`, '结算确认', {
    confirmButtonText: '确认支付',
    cancelButtonText: '再想想',
    type: 'warning'
  }).then(() => {
    // 校验余额
    request.get('/user/selectById/' + user.id).then(res => {
      if (res.code === '200') {
        const account = Number(res.data?.account || 0)
        const total = Number(totalPrice.value)
        if (account < total) {
          ElMessage.warning(`余额不足！当前余额 ¥${account.toFixed(2)}，需要 ¥${total.toFixed(2)}`)
          return
        }
        // 扣款
        request.put('/user/update', { ...res.data, account: (account - total).toFixed(2) }).then(res2 => {
          if (res2.code === '200') {
            // 更新 localStorage
            const updatedUser = { ...JSON.parse(localStorage.getItem('system-user') || '{}'), account: (account - total).toFixed(2) }
            localStorage.setItem('system-user', JSON.stringify(updatedUser))
            // 创建订单
            request.post('/orders/create', {
              userId: user.id,
              userName: user.name
            }).then(res3 => {
              if (res3.code === '200') {
                data.cartList = []
                ElMessage.success('购买成功！即将跳转到订单页...')
                // 延迟跳转，给用户看到成功提示
                setTimeout(() => {
                  router.push('/front/orders')
                }, 800)
              } else {
                ElMessage.error(res3.msg)
              }
            })
          } else {
            ElMessage.error(res2.msg)
          }
        })
      }
    })
  }).catch(() => {})
}

onMounted(() => {
  load()
})

// 监听路由变化，每次进入购物车页面自动刷新
watch(() => route.path, (newPath) => {
  if (newPath === '/front/cart') {
    load()
  }
})
</script>

<style scoped>
.cart-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  transition: box-shadow 0.3s;
}
.cart-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}
.cart-goods-name {
  font-size: 15px;
  color: #333;
  font-weight: 500;
  word-break: break-all;
  line-height: 1.5;
}
</style>
