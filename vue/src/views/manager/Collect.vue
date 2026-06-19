<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.userId" style="width: 200px; margin-right: 10px" placeholder="用户ID查询"></el-input>
      <el-input v-model="data.goodsName" style="width: 200px; margin-right: 10px" placeholder="商品名称查询"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="card">
      <el-table :data="data.tableData" stripe style="width: 100%">
        <el-table-column label="ID" prop="id" width="70"></el-table-column>
        <el-table-column label="用户ID" prop="userId" width="90"></el-table-column>
        <el-table-column label="商品图片" width="100">
          <template #default="scope">
            <img :src="scope.row.goodsImg" alt="" style="width: 50px; height: 50px; object-fit: cover; border-radius: 4px" />
          </template>
        </el-table-column>
        <el-table-column label="商品名称" prop="goodsName" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column label="商品价格" prop="goodsPrice" width="100"></el-table-column>
        <el-table-column label="收藏时间" prop="time" width="180"></el-table-column>
        <el-table-column label="操作" align="center" width="100">
          <template #default="scope">
            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card" style="margin-top: 5px">
      <el-pagination background layout="prev, pager, next" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" @current-change="load"/>
    </div>
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
  goodsName: null
})

// 加载收藏
const load = () => {
  const params = {
    pageNum: data.pageNum,
    pageSize: data.pageSize
  }
  if (data.userId) params.userId = data.userId
  if (data.goodsName) params.goodsName = data.goodsName
  request.get('/collect/selectPage', { params }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total || 0
    }
  })
}

// 删除
const handleDelete = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(() => {
    request.delete('/collect/delete/' + id).then(res => {
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
  data.goodsName = null
  load()
}

onMounted(() => {
  load()
})
</script>
