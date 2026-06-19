<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.userId" style="width: 200px; margin-right: 10px" placeholder="充值人ID查询"></el-input>
      <el-select v-model="data.type" style="width: 150px; margin-right: 10px" placeholder="支付方式" clearable>
        <el-option label="微信支付" value="微信支付"></el-option>
        <el-option label="支付宝" value="支付宝"></el-option>
        <el-option label="银行卡" value="银行卡"></el-option>
      </el-select>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" style="margin-left: 10px" @click="reset">重置</el-button>
      <el-button type="success" style="margin-left: 10px" @click="handleAdd">新增充值</el-button>
    </div>

    <div class="card">
      <el-table :data="data.tableData" stripe style="width: 100%">
        <el-table-column label="ID" prop="id" width="70"></el-table-column>
        <el-table-column label="充值金额" prop="money" width="120">
          <template #default="scope">
            <span style="color: #e74c3c; font-weight: bold;">¥{{ scope.row.money }}</span>
          </template>
        </el-table-column>
        <el-table-column label="充值人ID" prop="userId" width="100"></el-table-column>
        <el-table-column label="支付方式" prop="type" width="120"></el-table-column>
        <el-table-column label="充值时间" prop="time" width="180"></el-table-column>
        <el-table-column label="操作" align="center" width="150">
          <template #default="scope">
            <el-button type="warning" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card" style="margin-top: 5px">
      <el-pagination background layout="prev, pager, next" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" @current-change="load"/>
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="data.dialogTitle" v-model="data.dialogVisible" width="500px">
      <el-form :model="data.form" label-width="100px">
        <el-form-item label="充值人ID">
          <el-input v-model="data.form.userId" placeholder="请输入用户ID"></el-input>
        </el-form-item>
        <el-form-item label="充值金额">
          <el-input v-model="data.form.money" placeholder="请输入充值金额">
            <template #prepend>¥</template>
          </el-input>
        </el-form-item>
        <el-form-item label="支付方式">
          <el-select v-model="data.form.type" style="width: 100%" placeholder="请选择支付方式">
            <el-option label="微信支付" value="微信支付"></el-option>
            <el-option label="支付宝" value="支付宝"></el-option>
            <el-option label="银行卡" value="银行卡"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="充值时间">
          <el-input v-model="data.form.time" placeholder="请输入充值时间"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="data.dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import request from "@/utils/request";
import { reactive, onMounted } from "vue";
import { ElMessageBox, ElMessage } from "element-plus";

const data = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  tableData: [],
  userId: null,
  type: null,
  dialogVisible: false,
  dialogTitle: '新增充值',
  form: {}
})

// 加载充值记录
const load = () => {
  const params = {
    pageNum: data.pageNum,
    pageSize: data.pageSize
  }
  if (data.userId) params.userId = data.userId
  if (data.type) params.type = data.type
  request.get('/recharge/selectPage', { params }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total || 0
    }
  })
}

// 新增
const handleAdd = () => {
  data.dialogTitle = '新增充值'
  data.form = {}
  data.dialogVisible = true
}

// 编辑
const handleEdit = (row) => {
  data.dialogTitle = '编辑充值'
  data.form = { ...row }
  data.dialogVisible = true
}

// 保存
const save = () => {
  if (data.form.id) {
    request.put('/recharge/update', data.form).then(res => {
      if (res.code === '200') {
        load()
        data.dialogVisible = false
        ElMessage.success('更新成功')
      } else {
        ElMessage.error(res.msg)
      }
    })
  } else {
    request.post('/recharge/add', data.form).then(res => {
      if (res.code === '200') {
        load()
        data.dialogVisible = false
        ElMessage.success('添加成功')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }
}

// 删除
const handleDelete = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(() => {
    request.delete('/recharge/delete/' + id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success('删除成功')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(() => {})
}

// 重置
const reset = () => {
  data.userId = null
  data.type = null
  load()
}

onMounted(() => {
  load()
})
</script>
