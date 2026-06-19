<template>
  <div>
    <div class="card">
      <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px">
        <h3 style="margin: 0">订单管理</h3>
        <div style="display: flex; gap: 10px; align-items: center">
          <el-input v-model="data.params.orderNo" placeholder="订单编号" style="width: 180px" size="small" clearable />
          <el-input v-model="data.params.userName" placeholder="用户名" style="width: 150px" size="small" clearable />
          <el-select v-model="data.params.status" placeholder="支付状态" style="width: 110px" size="small" clearable>
            <el-option label="已支付" value="已支付" />
            <el-option label="待支付" value="待支付" />
            <el-option label="已取消" value="已取消" />
          </el-select>
          <el-select v-model="data.params.orderStatus" placeholder="订单状态" style="width: 110px" size="small" clearable>
            <el-option label="未出货" value="待发货" />
            <el-option label="已出货" value="已出货" />
            <el-option label="配送中" value="已发货" />
            <el-option label="已完成" value="已完成" />
            <el-option label="已取消" value="已取消" />
          </el-select>
          <el-date-picker
            v-model="data.params.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 240px"
            size="small"
          />
          <el-button type="primary" size="small" @click="load">搜索</el-button>
          <el-button size="small" @click="resetSearch">重置</el-button>
          <el-button size="small" type="success" @click="load">刷新</el-button>
        </div>
      </div>

      <!-- 订单卡片列表 -->
      <div v-for="order in data.tableData" :key="order.id" class="order-card" style="margin-bottom: 16px; border: 1px solid #dcdfe6; border-radius: 4px; overflow: hidden;">
        <!-- 订单头部 -->
        <table style="width: 100%; border-collapse: collapse; font-size: 14px;">
          <thead>
            <tr style="background: #f5f7fa;">
              <th style="padding: 10px 8px; border: 1px solid #dcdfe6; font-weight: normal; color: #606266; width: 160px;">订单编号</th>
              <th style="padding: 10px 8px; border: 1px solid #dcdfe6; font-weight: normal; color: #606266; width: 100px;">总价格</th>
              <th style="padding: 10px 8px; border: 1px solid #dcdfe6; font-weight: normal; color: #606266; width: 80px;">下单人</th>
              <th style="padding: 10px 8px; border: 1px solid #dcdfe6; font-weight: normal; color: #606266; width: 180px;">收货地址</th>
              <th style="padding: 10px 8px; border: 1px solid #dcdfe6; font-weight: normal; color: #606266; width: 80px;">支付状态</th>
              <th style="padding: 10px 8px; border: 1px solid #dcdfe6; font-weight: normal; color: #606266; width: 100px;">配送信息</th>
              <th style="padding: 10px 8px; border: 1px solid #dcdfe6; font-weight: normal; color: #606266; width: 80px;">订单状态</th>
              <th style="padding: 10px 8px; border: 1px solid #dcdfe6; font-weight: normal; color: #606266; width: 140px;">下单时间</th>
              <th style="padding: 10px 8px; border: 1px solid #dcdfe6; font-weight: normal; color: #606266; width: 120px;">操作</th>
            </tr>
          </thead>
          <tbody>
            <tr style="background: #fff;">
              <td style="padding: 12px 8px; border: 1px solid #dcdfe6; color: #606266;">{{ order.orderNo }}</td>
              <td style="padding: 12px 8px; border: 1px solid #dcdfe6; color: #e74c3c; font-weight: bold;">¥{{ order.totalPrice }}元</td>
              <td style="padding: 12px 8px; border: 1px solid #dcdfe6; color: #606266;">{{ order.userName }}</td>
              <td style="padding: 12px 8px; border: 1px solid #dcdfe6; color: #606266; font-size: 12px;">{{ order.address || '-' }}</td>
              <td style="padding: 12px 8px; border: 1px solid #dcdfe6;">
                <el-tag :type="order.status === '已支付' ? 'success' : order.status === '待支付' ? 'warning' : 'info'" size="small">
                  {{ order.status }}
                </el-tag>
              </td>
              <td style="padding: 12px 8px; border: 1px solid #dcdfe6; color: #606266; text-align: center; font-size: 12px;">{{ order.deliver || order.deliveryType || '-' }}</td>
              <td style="padding: 12px 8px; border: 1px solid #dcdfe6;">
                <el-tag :type="order.orderStatus === '待发货' ? 'warning' : order.orderStatus === '已出货' ? 'primary' : order.orderStatus === '已发货' ? '' : order.orderStatus === '已完成' ? 'success' : order.orderStatus === '已取消' ? 'info' : 'info'" size="small">
                  {{ order.orderStatus === '待发货' ? '未出货' : order.orderStatus === '已出货' ? '已出货' : order.orderStatus === '已发货' ? '配送中' : order.orderStatus === '已完成' ? '已完成' : order.orderStatus === '已取消' ? '已取消' : order.orderStatus || '-' }}
                </el-tag>
              </td>
              <td style="padding: 12px 8px; border: 1px solid #dcdfe6; color: #909399; font-size: 13px;">{{ order.time }}</td>
              <td style="padding: 12px 8px; border: 1px solid #dcdfe6; text-align: center;">
                <el-button v-if="order.orderStatus === '待发货'" type="warning" size="small" @click="handleOut(order.id)">出货</el-button>
                <el-button v-if="order.orderStatus === '已出货' && order.deliveryType === '外送'" type="primary" size="small" @click="handleSend(order.id)">配送</el-button>
                <el-button v-if="order.orderStatus === '已出货' && order.deliveryType === '自提'" type="success" size="small" @click="handleComplete(order.id)">完成</el-button>
                <el-button type="danger" size="small" @click="handleDelete(order.id)">删除</el-button>
              </td>
            </tr>
          </tbody>
        </table>
        <!-- 订单商品明细 -->
        <el-table :data="order.details" :show-header="true" size="small" style="width: 100%" border stripe>
          <el-table-column label="商品图片" width="100" align="center">
            <template #default="{ row }">
              <el-image :src="row.goodsImg" style="width: 50px; height: 50px; border-radius: 4px" fit="cover" />
            </template>
          </el-table-column>
          <el-table-column label="商品名称" prop="goodsName" min-width="300" show-overflow-tooltip />
          <el-table-column label="商品单价" width="120" align="center">
            <template #default="{ row }">
              <span style="color: #606266;">¥{{ row.price }}</span>
            </template>
          </el-table-column>
          <el-table-column label="数量" width="80" align="center">
            <template #default="{ row }">
              <span style="color: #909399;">x {{ row.num }}</span>
            </template>
          </el-table-column>
          <el-table-column label="小计" width="120" align="center">
            <template #default="{ row }">
              <span style="color: #e74c3c; font-weight: bold;">¥{{ (row.price * row.num).toFixed(2) }} 元</span>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div style="margin-top: 16px; text-align: right">
        <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          :total="data.total"
          :page-sizes="[5, 10, 20]"
          layout="total, sizes, prev, pager, next"
          @current-change="load"
          @size-change="load"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import request from "@/utils/request";
import { reactive, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";

const data = reactive({
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  params: {
    orderNo: '',
    userName: '',
    status: '',
    orderStatus: '',
    dateRange: null
  }
})

const load = () => {
  const params = {
    pageNum: data.pageNum,
    pageSize: data.pageSize,
    ...data.params
  }
  // 处理日期范围
  if (data.params.dateRange && data.params.dateRange.length === 2) {
    params.startTime = data.params.dateRange[0] + ' 00:00:00'
    params.endTime = data.params.dateRange[1] + ' 23:59:59'
  }
  delete params.dateRange
  // 移除空值
  Object.keys(params).forEach(key => {
    if (!params[key]) delete params[key]
  })
  request.get('/orders/selectPage', { params }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total || 0
    }
  })
}

const resetSearch = () => {
  data.params = { orderNo: '', userName: '', status: '', orderStatus: '', dateRange: null }
  data.pageNum = 1
  load()
}

const handleOut = (id) => {
  ElMessageBox.confirm('确认该订单已出货吗？', '出货确认', { type: 'info' }).then(() => {
    request.put('/orders/update', { id, orderStatus: '已出货' }).then(res => {
      if (res.code === '200') {
        ElMessage.success('已出货')
        load()
      } else {
        ElMessage.error(res.msg || '操作失败')
      }
    })
  }).catch(() => {})
}

const handleSend = (id) => {
  ElMessageBox.prompt('请输入配送人员姓名和电话', '配送信息', {
    confirmButtonText: '确认配送',
    cancelButtonText: '取消',
    inputType: 'textarea',
    inputPlaceholder: '例如：张三 13800138000',
    inputValidator: (value) => {
      if (!value || !value.trim()) return '请输入配送人员姓名和电话'
      return true
    }
  }).then(({ value }) => {
    request.put('/orders/update', { id, orderStatus: '已发货', deliver: value.trim() }).then(res => {
      if (res.code === '200') {
        ElMessage.success('已配送')
        load()
      } else {
        ElMessage.error(res.msg || '操作失败')
      }
    })
  }).catch(() => {})
}

const handleComplete = (id) => {
  ElMessageBox.confirm('确认该自提订单已完成吗？', '完成确认', { type: 'info' }).then(() => {
    request.put('/orders/update', { id, orderStatus: '已完成' }).then(res => {
      if (res.code === '200') {
        ElMessage.success('订单已完成')
        load()
      } else {
        ElMessage.error(res.msg || '操作失败')
      }
    })
  }).catch(() => {})
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

onMounted(() => {
  load()
})
</script>
