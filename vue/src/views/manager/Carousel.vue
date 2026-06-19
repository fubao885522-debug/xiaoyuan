<template>
  <div>

    <div class="card" style="margin-bottom: 5px;">
      <el-button type="primary" @click="handleAdd">新增</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" stripe style="width: 100%" :row-style="{ height: '120px' }" :cell-style="{ padding: '10px 0' }">
        <el-table-column label="商品" prop="goodsName" width="400"></el-table-column>
        <el-table-column label="图片" align="center">
          <template #default="scope">
            <el-image v-if="scope.row.img" :src="scope.row.img" style="width: 100px; height: 80px; border-radius: 4px; object-fit: cover;" fit="cover"></el-image>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" fixed="right" width="180">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog title="轮播图信息" width="50%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" label-width="80px" style="padding-right: 30px">
        <el-form-item label="商品" prop="goodsId">
          <el-select v-model="data.form.goodsId" placeholder="请选择关联商品" style="width: 100%">
            <el-option v-for="item in data.goodsList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="图片" prop="img">
          <el-upload :action="uploadUrl" list-type="picture" :on-success="handleImgSuccess" :limit="1"
            :file-list="data.fileList">
            <el-button type="primary">上传图片</el-button>
          </el-upload>
          <el-image v-if="data.form.img" :src="data.form.img" style="width: 120px; height: 100px; border-radius: 4px; margin-top: 8px" fit="cover"></el-image>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="data.formVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">保 存</el-button>
      </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import request from "@/utils/request";
import {reactive, onMounted} from "vue";
import {ElMessageBox, ElMessage} from "element-plus";

const uploadUrl = import.meta.env.VITE_BASE_URL + '/files/upload'

const data = reactive({
  formVisible: false,
  form: {},
  tableData: [],
  goodsList: [],
  fileList: []
})

// 加载商品列表
const loadGoods = () => {
  request.get('/goods/selectAll').then(res => {
    if (res.code === '200') {
      data.goodsList = res.data || []
    }
  })
}

// 查询
const load = () => {
  request.get('/carousel/selectAll').then(res => {
    data.tableData = res.data || []
  })
}

// 新增
const handleAdd = () => {
  data.form = {}
  data.fileList = []
  data.formVisible = true
}

// 编辑
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.fileList = row.img ? [{ url: row.img }] : []
  data.formVisible = true
}

// 新增保存
const add = () => {
  const submitData = { ...data.form }
  delete submitData.goodsList
  request.post('/carousel/add', submitData).then(res => {
    if (res.code === '200') {
      load()
      ElMessage.success('操作成功')
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 编辑保存
const update = () => {
  const submitData = { ...data.form }
  delete submitData.goodsList
  request.put('/carousel/update', submitData).then(res => {
    if (res.code === '200') {
      load()
      ElMessage.success('操作成功')
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 弹窗保存
const save = () => {
  data.form.id ? update() : add()
}

// 删除
const handleDelete = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(() => {
    request.delete('/carousel/delete/' + id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success('操作成功')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(() => {})
}

// 图片上传成功回调
const handleImgSuccess = (res) => {
  if (res.data) {
    data.form.img = res.data
  }
}

onMounted(() => {
  loadGoods()
  load()
})
</script>
