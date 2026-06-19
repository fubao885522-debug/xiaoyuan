<template>
  <div>

    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.name" style="width: 300px; margin-right: 10px" placeholder="请输入名称查询"></el-input>
      <el-select v-model="data.categoryId" placeholder="选择分类" style="width: 200px; margin-right: 10px; margin-left: 10px">
        <el-option label="全部分类" :value="null"></el-option>
        <el-option v-for="item in data.categoryList" :key="item.id" :label="item.name" :value="item.id"></el-option>
      </el-select>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" @click="handleAdd">新增</el-button>
      </div>
      <el-table :data="data.tableData" stripe style="width: 100%" :row-style="{ height: '120px' }" :cell-style="{ padding: '10px 0' }">
        <el-table-column label="名称" prop="name" min-width="150"></el-table-column>
        <el-table-column label="图片" width="140" align="center">
          <template #default="scope">
            <el-image v-if="scope.row.img" :src="scope.row.img" style="width: 100px; height: 100px; border-radius: 4px; object-fit: cover;" fit="cover"></el-image>
          </template>
        </el-table-column>
        <el-table-column label="价格" prop="price" width="90" align="center"></el-table-column>
        <el-table-column label="简介" prop="description" min-width="120" show-overflow-tooltip></el-table-column>
        <el-table-column label="详情" prop="content" min-width="120" show-overflow-tooltip></el-table-column>
        <el-table-column label="库存" prop="store" width="70"></el-table-column>
        <el-table-column label="分类名称" width="100">
          <template #default="scope">
            {{ data.categoryList.find(c => c.id === scope.row.categoryId)?.name || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="上架状态" prop="status" width="90">
          <template #default="scope">
            <el-tag :type="scope.row.status === '上架' ? 'success' : 'info'">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="推荐" width="70" align="center">
          <template #default="scope">
            <el-switch v-model="scope.row.recommend" @change="changeRecommend(scope.row)"></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="浏览量" prop="views" width="80"></el-table-column>
        <el-table-column label="销量" prop="saleCount" width="70"></el-table-column>
        <el-table-column label="创建时间" prop="time" min-width="160"></el-table-column>
        <el-table-column label="操作" align="center" fixed="right" width="160">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination background layout="prev, pager, next" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" @current-change="load"/>
    </div>

    <el-dialog title="商品信息" width="60%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.form" label-width="80px" style="padding-right: 30px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="图片" prop="img">
          <el-upload :action="uploadUrl" list-type="picture" :on-success="handleImgSuccess" :limit="1"
            :file-list="data.fileList">
            <el-button type="primary">上传图片</el-button>
          </el-upload>
          <el-image v-if="data.form.img" :src="data.form.img" style="width: 60px; height: 60px; border-radius: 4px; margin-top: 8px" fit="cover"></el-image>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="data.form.price" :min="0" :precision="2" style="width: 200px" />
        </el-form-item>
        <el-form-item label="简介" prop="description">
          <el-input v-model="data.form.description" autocomplete="off" placeholder="请输入简介" />
        </el-form-item>
        <el-form-item label="库存" prop="store">
          <el-input-number v-model="data.form.store" :min="0" style="width: 200px" />
        </el-form-item>
        <el-form-item label="销量" prop="saleCount">
          <el-input-number v-model="data.form.saleCount" :min="0" style="width: 200px" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="data.form.categoryId" placeholder="请选择分类" style="width: 100%">
            <el-option v-for="item in data.categoryList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="上架状态" prop="status">
          <el-radio-group v-model="data.form.status">
            <el-radio value="上架">上架</el-radio>
            <el-radio value="下架">下架</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="推荐" prop="recommend">
          <el-switch v-model="data.form.recommend"></el-switch>
        </el-form-item>
        <el-form-item label="详情" prop="content">
          <div style="border: 1px solid #ccc; border-radius: 4px">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :defaultConfig="toolbarConfig"
                mode="default"
            />
            <Editor
                style="height: 300px"
                v-model="data.form.htmlContent"
                :defaultConfig="editorConfig"
                mode="default"
                @onCreated="handleEditorCreated"
            />
          </div>
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
import {reactive, ref, onMounted, onBeforeUnmount} from "vue";
import {ElMessageBox, ElMessage} from "element-plus";
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'

const uploadUrl = import.meta.env.VITE_BASE_URL + '/files/upload'

// 编辑器实例
const editorRef = ref()

// 工具栏配置
const toolbarConfig = {}

// 编辑器配置
const editorConfig = {
  placeholder: '请输入详情...',
  MENU_CONF: {
    uploadImage: {
      server: uploadUrl,
      fieldName: 'file',
      customInsert(res, insertFn) {
        if (res.data) {
          insertFn(res.data, '', '')
        }
      }
    }
  }
}

// 编辑器创建回调
const handleEditorCreated = (editor) => {
  editorRef.value = editor
}

// 销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

const data = reactive({
  pageNum: 1,
  pageSize: 5,
  total: 0,
  formVisible: false,
  form: {},
  tableData: [],
  name: null,
  categoryId: null,
  categoryList: [],
  fileList: []
})

// 加载分类列表
const loadCategory = () => {
  request.get('/category/selectAll').then(res => {
    if (res.code === '200') {
      data.categoryList = res.data || []
    }
  })
}

// 分页查询
const load = () => {
  request.get('/goods/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name || undefined,
      categoryId: data.categoryId || undefined
    }
  }).then(res => {
    data.tableData = res.data?.list
    data.total = res.data?.total
  })
}

// 新增
const handleAdd = () => {
  data.form = { status: '上架', htmlContent: '', recommend: false, views: 0, saleCount: 0 }
  data.fileList = []
  data.formVisible = true
}

// 编辑
const handleEdit = (row) => {
  const form = JSON.parse(JSON.stringify(row))
  form.htmlContent = form.content || ''
  data.form = form
  data.fileList = row.img ? [{ url: row.img }] : []
  data.formVisible = true
}

// 新增保存
const add = () => {
  data.form.content = data.form.htmlContent || ''
  data.form.time = new Date().toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-')
  const submitData = { ...data.form }
  delete submitData.htmlContent
  delete submitData.categoryList
  request.post('/goods/add', submitData).then(res => {
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
  data.form.content = data.form.htmlContent || ''
  const submitData = { ...data.form }
  delete submitData.htmlContent
  delete submitData.categoryList
  request.put('/goods/update', submitData).then(res => {
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
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/goods/delete/' + id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success('操作成功')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

// 重置
const reset = () => {
  data.name = null
  data.categoryId = null
  load()
}

// 切换推荐状态
const changeRecommend = (row) => {
  request.put('/goods/updateRecommend', { id: row.id, recommend: row.recommend }).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 图片上传成功回调
const handleImgSuccess = (res) => {
  if (res.data) {
    data.form.img = res.data
  }
}

onMounted(() => {
  loadCategory()
  load()
})
</script>
