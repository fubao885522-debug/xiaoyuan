<template>
  <div class="front-container" style="min-height: 100vh">
    <div class="card" v-if="data.goods.id" style="display: flex; padding: 30px">
      <!-- 左侧商品图片 -->
      <div style="width: 420px; flex-shrink: 0">
        <div class="goods-img-box">
          <img :src="data.goods.img" alt="" class="goods-img" />
        </div>
      </div>

      <!-- 右侧商品信息 -->
      <div style="flex: 1; margin-left: 40px">
        <h2 class="goods-name">{{ data.goods.name }}</h2>
        <div class="goods-desc">{{ data.goods.description || '暂无简介' }}</div>

        <div class="goods-price-box">
          <span style="color: #999; font-size: 14px">价格</span>
          <span style="color: #ff4d4f; font-size: 28px; font-weight: bold; margin-left: 16px">￥{{ data.goods.price }}</span>
        </div>

        <div class="goods-meta">
          <span>库存 {{ data.goods.store || 0 }}</span>
          <span>已售 {{ data.goods.saleCount || 0 }}</span>
          <span>浏览 {{ data.goods.views || 0 }}</span>
        </div>

        <!-- 数量 -->
        <div style="display: flex; align-items: center; margin-bottom: 30px">
          <span style="color: #666; font-size: 14px; margin-right: 16px">数量</span>
          <el-input-number v-model="data.buyCount" :min="1" :max="data.goods.store || 1" />
        </div>

        <!-- 操作按钮 -->
        <div style="display: flex; gap: 16px; align-items: center">
          <el-button style="width: 160px; height: 44px; font-size: 16px" @click="addToCart" :disabled="!data.goods.store">
            加入购物车
          </el-button>
          <el-button type="danger" style="width: 160px; height: 44px; font-size: 16px" @click="buyNow" :disabled="!data.goods.store">
            立即购买
          </el-button>
          <el-button
            :type="data.isCollected ? 'warning' : 'default'"
            style="width: 44px; height: 44px; padding: 0"
            @click="toggleCollect"
          >
            <span style="font-size: 22px">{{ data.isCollected ? '★' : '☆' }}</span>
          </el-button>
        </div>
      </div>
    </div>

    <!-- Tab 标签切换：商品详情 / 商品评论 -->
    <div class="card" style="margin-top: 16px" v-if="data.goods.id">
      <div class="tab-bar">
        <div
          :class="['tab-item', { active: data.activeTab === 'detail' }]"
          @click="data.activeTab = 'detail'"
        >
          商品详情
        </div>
        <div
          :class="['tab-item', { active: data.activeTab === 'comment' }]"
          @click="data.activeTab = 'comment'"
        >
          商品评论（{{ data.commentList.length }}）
        </div>
      </div>

      <!-- 商品详情内容 -->
      <div v-if="data.activeTab === 'detail'" style="padding: 20px 0">
        <div v-if="data.goods.content" class="goods-content" v-html="data.goods.content"></div>
        <div v-else style="color: #999; text-align: center; padding: 40px 0">暂无详情</div>
      </div>

      <!-- 评论内容 -->
      <div v-if="data.activeTab === 'comment'" style="padding: 20px 0">
        <!-- 发表评论 -->
        <div style="margin-bottom: 24px; padding: 16px; background: #fafafa; border-radius: 8px">
          <div style="display: flex; align-items: center; margin-bottom: 10px">
            <span style="color: #666; font-size: 14px; margin-right: 10px">评分</span>
            <el-rate v-model="data.rating" allow-half show-score />
          </div>
          <el-input v-model="data.commentContent" type="textarea" :rows="3" placeholder="请输入您的评论..." style="margin-bottom: 10px" />
          <el-button type="primary" @click="submitComment">发表评论</el-button>
        </div>

        <!-- 评论列表 -->
        <div v-if="data.commentList.length > 0">
          <div v-for="item in data.commentList" :key="item.id" class="comment-item">
            <div class="comment-header">
              <div class="comment-avatar">{{ (item.userName || '用户')[0] }}</div>
              <div>
                <div style="display: flex; align-items: center; gap: 10px">
                  <span style="font-weight: bold; font-size: 14px">{{ item.userName || '匿名用户' }}</span>
                  <el-rate v-model="item.rating" disabled allow-half show-score size="small" />
                  <el-tag v-if="item.orderId" type="success" size="small" effect="plain">已购买</el-tag>
                </div>
                <div style="color: #999; font-size: 12px; margin-top: 2px">{{ item.time }}</div>
              </div>
            </div>
            <div class="comment-content">{{ item.content }}</div>
          </div>
        </div>
        <div v-else style="text-align: center; color: #999; padding: 30px 0">暂无评论，快来发表第一条评论吧~</div>
      </div>
    </div>

    <div v-if="!data.goods.id" style="text-align: center; padding: 60px 0; color: #999">加载中...</div>

    <!-- 下单信息弹窗 -->
    <el-dialog v-model="data.orderDialogVisible" title="下单信息" width="550px" :close-on-click-modal="false">
      <el-form :model="data.orderForm" label-width="90px" style="padding: 10px 20px">
        <el-form-item label="配送类型" required>
          <el-radio-group v-model="data.orderForm.deliveryType">
            <el-radio-button label="自提">自提</el-radio-button>
            <el-radio-button label="外送">外送</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <template v-if="data.orderForm.deliveryType === '外送'">
          <el-form-item label="联系人" required>
            <el-input v-model="data.orderForm.contact" placeholder="请输入联系人姓名" />
          </el-form-item>
          <el-form-item label="联系电话" required>
            <el-input v-model="data.orderForm.phone" placeholder="请输入联系电话" />
          </el-form-item>
          <el-form-item label="收货地址" required>
            <el-input v-model="data.orderForm.address" type="textarea" :rows="2" placeholder="请输入收货地址" />
          </el-form-item>
          <el-form-item label="配送信息">
            <el-input v-model="data.orderForm.deliver" type="textarea" :rows="2" placeholder="请输入配送备注信息（选填）" />
          </el-form-item>
        </template>
      </el-form>
      <template #footer>
        <el-button @click="data.orderDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmOrder">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import request from "@/utils/request";
import { reactive, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";

const route = useRoute()
const router = useRouter()

const data = reactive({
  goods: {},
  buyCount: 1,
  commentList: [],
  commentContent: '',
  rating: 5,
  activeTab: 'detail',
  isCollected: false,
  collectId: null,
  orderDialogVisible: false,
  orderForm: {
    deliveryType: '自提',
    contact: '',
    phone: '',
    address: '',
    deliver: ''
  },
  pendingUser: null
})

// 加载商品详情
const loadGoods = () => {
  const id = route.query.id
  if (!id) {
    ElMessage.error('商品不存在')
    router.push('/front/home')
    return
  }
  request.get('/goods/selectById/' + id).then(res => {
    if (res.code === '200') {
      data.goods = res.data || {}
      if (data.goods.id) {
        // 检查商品是否已下架
        if (data.goods.status === '下架') {
          ElMessage.warning('该商品已下架')
          router.push('/front/home')
          return
        }
        request.put('/goods/update', { ...data.goods, views: (data.goods.views || 0) + 1 }).catch(() => {})
        checkCollect()
      }
    } else {
      ElMessage.error('商品不存在')
      router.push('/front/home')
    }
  })
}

// 加载评论
const loadComments = () => {
  const id = route.query.id
  if (!id) return
  request.get('/comment/selectAll', { params: { goodsId: id } }).then(res => {
    if (res.code === '200') {
      data.commentList = res.data || []
    }
  })
}

// 发表评论
const submitComment = () => {
  const user = JSON.parse(localStorage.getItem('system-user') || '{}')
  if (!user.id) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  if (!data.commentContent.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  request.post('/comment/add', {
    goodsId: data.goods.id,
    userId: user.id,
    userName: user.name || '用户',
    content: data.commentContent.trim(),
    rating: data.rating,
    time: new Date().toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-')
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success('评论成功')
      data.commentContent = ''
      data.rating = 5
      loadComments()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 加入购物车
const addToCart = () => {
  const user = JSON.parse(localStorage.getItem('system-user') || '{}')
  if (!user.id) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  request.post('/cart/add', {
    goodsId: data.goods.id,
    userId: user.id,
    goodsName: data.goods.name,
    goodsImg: data.goods.img,
    price: data.goods.price,
    num: data.buyCount,
    createTime: new Date().toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-')
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success('已加入购物车')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 立即购买
const buyNow = () => {
  const user = JSON.parse(localStorage.getItem('system-user') || '{}')
  if (!user.id) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  data.pendingUser = user
  data.orderForm.deliveryType = '自提'
  data.orderForm.contact = ''
  data.orderForm.phone = ''
  data.orderForm.address = ''
  data.orderForm.deliver = ''
  data.orderDialogVisible = true
}

const confirmOrder = () => {
  const form = data.orderForm
  if (form.deliveryType === '外送') {
    if (!form.contact.trim()) { ElMessage.warning('请输入联系人'); return }
    if (!form.phone.trim()) { ElMessage.warning('请输入联系电话'); return }
    if (!form.address.trim()) { ElMessage.warning('请输入收货地址'); return }
  }
  data.orderDialogVisible = false
  const user = data.pendingUser
  // 将联系人、电话拼接到地址前面
  const fullAddress = form.deliveryType === '外送'
    ? `联系人：${form.contact}，电话：${form.phone}，地址：${form.address}`
    : ''
  submitBuyNow(user, form.deliveryType, fullAddress, form.deliver)
}

const submitBuyNow = (user, deliveryType, address, deliver) => {
  request.post('/orders/buyNow', {
    goodsId: data.goods.id,
    goodsName: data.goods.name,
    goodsImg: data.goods.img,
    price: data.goods.price,
    num: data.buyCount,
    userId: user.id,
    userName: user.name || '用户',
    deliveryType: deliveryType,
    address: address || '',
    deliver: deliver || ''
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success('购买成功！')
      router.push('/front/orders')
    } else {
      ElMessage.error(res.msg || '购买失败')
    }
  })
}

// 检查是否已收藏
const checkCollect = () => {
  const user = JSON.parse(localStorage.getItem('system-user') || '{}')
  if (!user.id) return
  request.get('/collect/check', {
    params: { userId: user.id, goodsId: data.goods.id }
  }).then(res => {
    if (res.code === '200' && res.data) {
      data.isCollected = true
      data.collectId = res.data.id
    }
  })
}

// 切换收藏
const toggleCollect = () => {
  const user = JSON.parse(localStorage.getItem('system-user') || '{}')
  if (!user.id) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  if (data.isCollected) {
    // 取消收藏
    request.delete('/collect/delete/' + data.collectId).then(res => {
      if (res.code === '200') {
        data.isCollected = false
        data.collectId = null
        ElMessage.success('已取消收藏')
      } else {
        ElMessage.error(res.msg)
      }
    })
  } else {
    // 添加收藏
    request.post('/collect/add', {
      userId: user.id,
      goodsId: data.goods.id,
      goodsName: data.goods.name,
      goodsImg: data.goods.img,
      goodsPrice: data.goods.price,
      time: new Date().toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-')
    }).then(res => {
      if (res.code === '200') {
        data.isCollected = true
        ElMessage.success('收藏成功')
        checkCollect()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }
}

onMounted(() => {
  loadGoods()
  loadComments()
})
</script>

<style scoped>
.goods-img-box {
  border-radius: 4px;
  overflow: hidden;
  border: 1px solid #eee;
}
.goods-img {
  width: 420px;
  height: 420px;
  object-fit: cover;
  display: block;
}
.goods-name {
  font-size: 22px;
  color: #333;
  margin-bottom: 10px;
  line-height: 1.4;
}
.goods-desc {
  color: #999;
  font-size: 14px;
  margin-bottom: 24px;
  line-height: 1.6;
}
.goods-price-box {
  background: #fff5f5;
  padding: 14px 20px;
  border-radius: 4px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}
.goods-meta {
  display: flex;
  gap: 30px;
  color: #999;
  font-size: 13px;
  margin-bottom: 24px;
}
.goods-content {
  line-height: 1.8;
  color: #555;
  font-size: 15px;
}
.goods-content :deep(img) {
  max-width: 100%;
}

/* Tab 标签栏 */
.tab-bar {
  display: flex;
  border-bottom: 2px solid #e8e8e8;
}
.tab-item {
  padding: 14px 28px;
  font-size: 15px;
  color: #666;
  cursor: pointer;
  position: relative;
  transition: color 0.3s;
  user-select: none;
}
.tab-item:hover {
  color: #4a7cf7;
}
.tab-item.active {
  color: #4a7cf7;
  font-weight: 600;
}
.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  right: 0;
  height: 2px;
  background: #4a7cf7;
}

/* 评论样式 */
.comment-item {
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
}
.comment-item:last-child {
  border-bottom: none;
}
.comment-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}
.comment-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #4a7cf7;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: bold;
  flex-shrink: 0;
}
.comment-content {
  color: #555;
  font-size: 14px;
  line-height: 1.6;
  padding-left: 48px;
}
</style>
