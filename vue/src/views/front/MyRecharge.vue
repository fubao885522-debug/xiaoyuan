<template>
  <div style="max-width: 1200px; margin: 0 auto; padding: 20px;">
    <h3 style="margin-bottom: 15px;">我的充值记录（{{ data.total }}）</h3>

    <div class="card" style="margin-bottom: 5px;">
      <div style="display: flex; align-items: center; gap: 10px;">
        <el-date-picker v-model="data.searchTime" type="date" style="width: 200px" placeholder="请选择日期查询" clearable value-format="YYYY-MM-DD"></el-date-picker>
        <el-button type="primary" @click="load">查询</el-button>
        <el-button type="info" @click="reset">重置</el-button>
        <el-button type="success" @click="openDialog">发起充值</el-button>
        <span style="margin-left: auto; color: #e74c3c; font-weight: bold;">当前账户余额：{{ data.balance }}元</span>
      </div>
    </div>

    <div class="card">
      <el-table :data="data.tableData" stripe style="width: 100%" empty-text="暂无数据">
        <el-table-column label="充值金额" prop="money" min-width="150">
          <template #default="scope">
            <span style="color: #e74c3c; font-weight: bold;">+¥{{ scope.row.money }}</span>
          </template>
        </el-table-column>
        <el-table-column label="支付方式" prop="type" min-width="150"></el-table-column>
        <el-table-column label="充值时间" prop="time" min-width="200"></el-table-column>
      </el-table>
    </div>

    <div class="card" style="margin-top: 5px">
      <el-pagination background layout="total, prev, pager, next" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" @current-change="load"/>
    </div>

    <!-- 充值弹窗 -->
    <el-dialog title="用户充值" v-model="data.dialogVisible" width="450px">
      <div style="display: flex; justify-content: flex-end; margin-bottom: 15px;">
        <span style="color: #e74c3c; font-weight: bold;">当前账户余额：{{ data.balance }}元</span>
      </div>
      <el-form :model="data.form" label-width="100px">
        <el-form-item label="充值金额" required>
          <div style="display: flex; align-items: center; border: 1px solid #dcdfe6; border-radius: 4px; overflow: hidden; width: 200px;">
            <el-button 
              style="border: none; border-radius: 0; background: #f5f7fa;" 
              @click="changeMoney(-1)"
              :disabled="(data.form.money || 0) <= 1"
            >-</el-button>
            <el-input 
              v-model="data.form.money" 
              style="flex: 1;"
              :input-style="{ textAlign: 'center', border: 'none', borderRadius: 0 }"
            />
            <el-button 
              style="border: none; border-radius: 0; background: #f5f7fa;" 
              @click="changeMoney(1)"
            >+</el-button>
          </div>
        </el-form-item>
        <el-form-item label="支付方式" required>
          <div style="display: flex; gap: 20px; align-items: center;">
            <label style="display: flex; align-items: center; cursor: pointer; gap: 5px;">
              <input type="radio" v-model="data.form.type" value="微信支付" style="cursor: pointer;">
              <span style="display: flex; align-items: center; gap: 3px;">
                <span style="display: inline-block; width: 20px; height: 20px; background: #07c160; border-radius: 50%; display: flex; align-items: center; justify-content: center; color: white; font-size: 12px;">微</span>
                <span>微信支付</span>
              </span>
            </label>
            <label style="display: flex; align-items: center; cursor: pointer; gap: 5px;">
              <input type="radio" v-model="data.form.type" value="支付宝" style="cursor: pointer;">
              <span style="display: flex; align-items: center; gap: 3px;">
                <span style="display: inline-block; width: 20px; height: 20px; background: #1677ff; border-radius: 3px; display: flex; align-items: center; justify-content: center; color: white; font-size: 10px;">支</span>
                <span>支付宝</span>
              </span>
            </label>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="data.dialogVisible = false">取消</el-button>
        <el-button type="primary" @click.prevent="handleRecharge">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import request from "@/utils/request";
import { reactive, onMounted } from "vue";
import { ElMessage } from "element-plus";

const user = JSON.parse(localStorage.getItem('system-user') || '{}');

const data = reactive({
  balance: Number(user.account || 0).toFixed(2),
  tableData: [],
  total: 0,
  pageNum: 1,
  pageSize: 10,
  searchTime: null,
  dialogVisible: false,
  form: {}
})

// 加载充值记录
const load = () => {
  if (!user.id) return
  const params = {
    pageNum: data.pageNum,
    pageSize: data.pageSize,
    userId: user.id
  }
  if (data.searchTime) params.time = data.searchTime

  request.get('/recharge/selectPage', { params }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total || 0
    }
  })
}

// 刷新余额（从数据库重新查）
const refreshBalance = () => {
  request.get('/user/selectById/' + user.id).then(res => {
    if (res.code === '200' && res.data) {
      const newAccount = res.data.account || 0
      data.balance = Number(newAccount).toFixed(2)
      // 同步更新localStorage
      user.account = newAccount
      localStorage.setItem('system-user', JSON.stringify(user))
    }
  })
}

// 重置
const reset = () => {
  data.searchTime = null
  data.pageNum = 1
  load()
}

// 打开弹窗
const openDialog = () => {
  data.form = { money: '', type: '' }
  data.dialogVisible = true
}

// 金额加减
const changeMoney = (delta) => {
  let val = parseInt(data.form.money) || 0
  val = Math.max(0, val + delta)
  data.form.money = val
}

// 充值
const handleRecharge = () => {
  const money = Number(data.form.money)
  if (!money || money <= 0) {
    ElMessage.warning('请输入有效的充值金额')
    return
  }
  if (!data.form.type) {
    ElMessage.warning('请选择支付方式')
    return
  }
  const now = new Date()
  const timeStr = now.getFullYear() + '-' +
    String(now.getMonth() + 1).padStart(2, '0') + '-' +
    String(now.getDate()).padStart(2, '0') + ' ' +
    String(now.getHours()).padStart(2, '0') + ':' +
    String(now.getMinutes()).padStart(2, '0') + ':' +
    String(now.getSeconds()).padStart(2, '0')

  request.post('/recharge/add', {
    userId: user.id,
    money: money,
    type: data.form.type,
    time: timeStr
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success('充值成功！')
      data.dialogVisible = false
      data.form = {}
      refreshBalance()
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

onMounted(() => {
  refreshBalance()
  load()
})
</script>
