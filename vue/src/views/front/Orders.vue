<template>
  <div class="front-container" style="min-height: 80vh">
    <div class="card">
      <h3 style="margin-bottom: 20px; padding-bottom: 15px; border-bottom: 1px solid #eee; display: flex; align-items: center; justify-content: space-between;">
        <span>我的订单</span>
        <el-button size="small" type="success" @click="load">刷新</el-button>
      </h3>

      <!-- 订单列表 -->
      <div v-if="data.orderList.length > 0">
        <!-- 表头 -->
        <div class="table-header">
          <span class="col-expand"></span>
          <span class="col-no">订单编号</span>
          <span class="col-price">总价格</span>
          <span class="col-user">下单人</span>
          <span class="col-delivery">配送类型</span>
          <span class="col-status">状态</span>
          <span class="col-time">下单时间</span>
          <span class="col-info">配送信息</span>
          <span class="col-action">订单操作</span>
        </div>

        <div v-for="order in data.orderList" :key="order.id" class="order-card">
          <!-- 订单头部信息行 -->
          <div class="order-header" @click="toggleExpand(order.id)">
            <div class="expand-icon" :class="{ expanded: !data.collapsedIds.has(order.id) }">
              <el-icon><ArrowRight /></el-icon>
            </div>
            <div class="order-info">
              <span class="order-no">{{ order.orderNo }}</span>
              <span class="order-price">¥{{ order.totalPrice }}元</span>
              <span class="order-user">{{ order.userName }}</span>
              <span class="order-delivery">{{ order.deliveryType === '快递' ? '' : (order.deliveryType || '') }}</span>
              <span class="order-status">
                <el-tag :type="order.orderStatus === '待发货' ? 'warning' : order.orderStatus === '已出货' ? 'primary' : order.orderStatus === '已发货' ? '' : order.orderStatus === '已完成' ? 'success' : order.orderStatus === '已取消' ? 'info' : 'info'" size="small">
                  {{ order.orderStatus === '待发货' ? '未出货' : order.orderStatus === '已出货' ? '已出货' : order.orderStatus === '已发货' ? '配送中' : order.orderStatus === '已完成' ? '已完成' : order.orderStatus === '已取消' ? '已取消' : order.orderStatus || '-' }}
                </el-tag>
              </span>
              <span class="order-time">{{ order.time }}</span>
              <span class="order-info-text">
                <div v-if="order.address" class="info-line">{{ order.address }}</div>
                <div v-if="order.deliver" class="info-line">{{ order.deliver }}</div>
                <span v-if="!order.address && !order.deliver">-</span>
              </span>
            </div>
            <div class="order-action">
              <el-button v-if="order.orderStatus === '待发货' || (order.orderStatus === '已出货' && order.deliveryType !== '自提')" type="danger" size="small" @click.stop="handleCancel(order.id)">取消</el-button>
              <el-button v-if="order.orderStatus === '已发货' || (order.orderStatus === '已出货' && order.deliveryType === '自提')" type="primary" size="small" @click.stop="handleReceive(order.id)">确认收货</el-button>
              <el-button v-if="order.orderStatus === '已完成'" type="success" size="small" @click.stop="openReview(order)">评价</el-button>
              <span v-if="order.orderStatus === '已取消'" class="no-action-text">-</span>
            </div>
          </div>

          <!-- 商品明细 -->
          <div v-show="!data.collapsedIds.has(order.id)" class="order-detail">
            <!-- 配送信息 -->
            <div v-if="order.address || order.deliver" class="delivery-info-box">
              <div class="delivery-info-title">配送信息</div>
              <div v-if="order.address" class="delivery-info-item">{{ order.address }}</div>
              <div v-if="order.deliver" class="delivery-info-item">
                <span class="delivery-label">配送人员：</span>
                <span>{{ order.deliver }}</span>
              </div>
            </div>
            <table class="detail-table">
              <thead>
                <tr>
                  <th style="width: 100px">商品图片</th>
                  <th>商品名称</th>
                  <th style="width: 120px">商品单价</th>
                  <th style="width: 80px">数量</th>
                  <th style="width: 120px">小计</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="detail in order.details" :key="detail.id">
                  <td>
                    <el-image :src="detail.goodsImg" style="width: 60px; height: 60px; border-radius: 4px" fit="cover" />
                  </td>
                  <td class="goods-name">{{ detail.goodsName }}</td>
                  <td>¥{{ detail.price }}</td>
                  <td>x {{ detail.num }}</td>
                  <td style="color: #e74c3c; font-weight: bold;">¥{{ (detail.price * detail.num).toFixed(2) }} 元</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- 空订单 -->
      <div v-else style="text-align: center; padding: 80px 0">
        <div style="font-size: 60px; margin-bottom: 16px">📋</div>
        <div style="color: #999; font-size: 16px; margin-bottom: 24px">暂无订单，快去购物吧~</div>
        <el-button type="primary" @click="router.push('/front/home')">去逛逛</el-button>
      </div>
    </div>

    <!-- 评价对话框 -->
    <el-dialog v-model="data.reviewDialogVisible" title="订单评价" width="550px" :close-on-click-modal="false">
      <div v-if="data.reviewOrder" style="padding: 0 10px">
        <div style="margin-bottom: 16px; color: #606266; font-size: 14px;">
          订单编号：<span style="color: #333; font-weight: 500;">{{ data.reviewOrder.orderNo }}</span>
        </div>
        <div v-for="(item, index) in data.reviewOrder.details" :key="item.id" style="margin-bottom: 20px; padding: 12px; background: #f8f9fa; border-radius: 6px;">
          <div style="display: flex; align-items: center; margin-bottom: 10px;">
            <el-image :src="item.goodsImg" style="width: 50px; height: 50px; border-radius: 4px; margin-right: 12px;" fit="cover" />
            <span style="font-weight: 500; color: #333;">{{ item.goodsName }}</span>
          </div>
          <div style="margin-bottom: 8px;">
            <span style="color: #606266; font-size: 13px;">评分：</span>
            <el-rate v-model="data.reviewForms[index].rating" :max="5" allow-half show-score style="display: inline-block;" />
          </div>
          <div>
            <el-input v-model="data.reviewForms[index].content" type="textarea" :rows="2" placeholder="请输入评价内容..." maxlength="200" show-word-limit />
          </div>
        </div>
      </div>
      <template #footer>
        <el-button @click="data.reviewDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReviews">提交评价</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import request from "@/utils/request";
import { reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { ArrowRight } from '@element-plus/icons-vue';

const router = useRouter();
const user = JSON.parse(localStorage.getItem('system-user') || '{}');

const data = reactive({
  orderList: [],
  collapsedIds: new Set(),  // 初始为空，全部展开
  reviewDialogVisible: false,
  reviewOrder: null,
  reviewForms: []  // 每个商品一个 { rating: 5, content: '' }
})

const toggleExpand = (orderId) => {
  if (data.collapsedIds.has(orderId)) {
    data.collapsedIds.delete(orderId)
  } else {
    data.collapsedIds.add(orderId)
  }
}

const load = () => {
  if (!user.id) {
    router.push('/login')
    return
  }
  request.get('/orders/selectAll', { params: { userId: user.id } }).then(res => {
    if (res.code === '200') {
      data.orderList = res.data || []
    }
  })
}

const handleReceive = (id) => {
  ElMessageBox.confirm('确认已收到该订单商品吗？', '确认收货', { type: 'info' }).then(() => {
    request.put('/orders/update', { id, orderStatus: '已完成' }).then(res => {
      if (res.code === '200') {
        ElMessage.success('已确认收货')
        load()
      } else {
        ElMessage.error(res.msg || '操作失败')
      }
    })
  }).catch(() => {})
}

const handleCancel = (id) => {
  request.put('/orders/cancel/' + id).then(res => {
    if (res.code === '200') {
      ElMessage.success('订单已取消，金额已退回余额')
      // 同步更新本地用户余额
      const user = JSON.parse(localStorage.getItem('system-user') || '{}')
      const order = data.orderList.find(o => o.id === id)
      if (order && user.id) {
        user.account = (parseFloat(user.account || 0) + parseFloat(order.totalPrice || 0)).toFixed(2)
        localStorage.setItem('system-user', JSON.stringify(user))
      }
      load()
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  })
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除该订单吗？', '删除确认', { type: 'warning' }).then(() => {
    request.delete('/orders/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('已删除')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(() => {})
}

// 打开评价对话框
const openReview = (order) => {
  data.reviewOrder = order
  data.reviewForms = (order.details || []).map(() => ({ rating: 5, content: '' }))
  data.reviewDialogVisible = true
}

// 提交评价
const submitReviews = () => {
  const user = JSON.parse(localStorage.getItem('system-user') || '{}')
  const details = data.reviewOrder.details || []
  const promises = details.map((item, index) => {
    const form = data.reviewForms[index]
    if (!form.content.trim()) {
      return Promise.resolve()  // 跳过空内容
    }
    return request.post('/comment/add', {
      goodsId: item.goodsId,
      userId: user.id,
      userName: user.name || '用户',
      content: form.content.trim(),
      rating: form.rating,
      orderId: data.reviewOrder.id,
      time: new Date().toLocaleString('zh-CN', { hour12: false })
    })
  })

  Promise.all(promises).then(results => {
    const hasContent = results.some(r => r)
    if (hasContent) {
      ElMessage.success('评价提交成功')
    }
    data.reviewDialogVisible = false
    load()
  }).catch(() => {
    ElMessage.error('评价提交失败')
  })
}

onMounted(() => {
  load()
})
</script>

<style scoped>
.table-header {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background: #f8f9fa;
  border: 1px solid #e8e8e8;
  border-radius: 4px 4px 0 0;
  font-size: 14px;
  color: #606266;
  font-weight: 500;
}
.order-card {
  border: 1px solid #e8e8e8;
  border-top: none;
  border-radius: 0;
  margin-bottom: 0;
  background: #fff;
}
.order-card:last-child {
  border-radius: 0 0 4px 4px;
  margin-bottom: 16px;
}
.order-header {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background: #f8f9fa;
  cursor: pointer;
  transition: background 0.2s;
}
.order-header:hover {
  background: #f0f1f2;
}
.expand-icon {
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  transition: transform 0.2s;
}
.expand-icon.expanded {
  transform: rotate(90deg);
}
.col-expand { width: 32px; flex-shrink: 0; }
.col-no { width: 180px; flex-shrink: 0; }
.col-price { width: 100px; flex-shrink: 0; text-align: center; }
.col-user { width: 80px; flex-shrink: 0; text-align: center; }
.col-delivery { width: 80px; flex-shrink: 0; text-align: center; }
.col-status { width: 80px; flex-shrink: 0; text-align: center; }
.col-time { width: 160px; flex-shrink: 0; text-align: center; }
.col-info { flex: 1; text-align: center; min-width: 0; }
.col-action { flex: 1; text-align: right; }

.order-info {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 0;
}
.order-no {
  width: 180px;
  color: #606266;
  font-size: 14px;
}
.order-price {
  width: 100px;
  color: #e74c3c;
  font-weight: bold;
  font-size: 14px;
  text-align: center;
}
.order-user {
  width: 80px;
  color: #606266;
  font-size: 14px;
  text-align: center;
}
.order-delivery {
  width: 80px;
  color: #606266;
  font-size: 14px;
  text-align: center;
}
.order-status {
  width: 80px;
  text-align: center;
  flex-shrink: 0;
}
.order-time {
  width: 160px;
  color: #909399;
  font-size: 13px;
  text-align: center;
}
.order-info-text {
  flex: 1;
  color: #606266;
  font-size: 13px;
  text-align: left;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding: 0 10px;
}
.info-line {
  word-break: break-all;
  line-height: 1.5;
}
.order-action {
  display: flex;
  gap: 8px;
}
.order-detail {
  padding: 16px;
  border-top: 1px solid #e8e8e8;
}
.delivery-info-box {
  background: #f0f7ff;
  border: 1px solid #d0e3f7;
  border-radius: 6px;
  padding: 12px 16px;
  margin-bottom: 16px;
}
.delivery-info-title {
  font-size: 14px;
  font-weight: 600;
  color: #409eff;
  margin-bottom: 8px;
}
.delivery-info-item {
  font-size: 13px;
  color: #606266;
  line-height: 1.8;
  word-break: break-all;
}
.delivery-label {
  color: #909399;
  font-weight: 500;
}
.detail-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}
.detail-table th {
  background: #f8f9fa;
  padding: 12px;
  text-align: center;
  font-weight: normal;
  color: #606266;
  border: 1px solid #e8e8e8;
}
.detail-table td {
  padding: 12px;
  text-align: center;
  border: 1px solid #e8e8e8;
  color: #606266;
}
.goods-name {
  text-align: left !important;
  max-width: 400px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
