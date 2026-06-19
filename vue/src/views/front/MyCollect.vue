<template>
  <div style="max-width: 1200px; margin: 0 auto; padding: 20px;">
    <div class="card" style="margin-bottom: 20px;">
      <h3 style="margin-bottom: 15px;">我的收藏</h3>
      <div v-if="data.tableData.length === 0" style="text-align: center; padding: 50px; color: #999;">
        暂无收藏，快去逛逛吧~
      </div>
      <div v-else style="display: flex; flex-wrap: wrap; gap: 15px;">
        <div v-for="item in data.tableData" :key="item.id" style="width: 220px; border: 1px solid #eee; border-radius: 8px; overflow: hidden; background: #fff; cursor: pointer;" @click="goDetail(item.goodsId)">
          <img :src="item.goodsImg" alt="" style="width: 100%; height: 180px; object-fit: cover;" />
          <div style="padding: 10px;">
            <div style="font-size: 14px; font-weight: bold; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">{{ item.goodsName }}</div>
            <div style="color: #e74c3c; font-size: 16px; font-weight: bold; margin: 5px 0;">¥{{ item.goodsPrice }}</div>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span style="font-size: 12px; color: #999;">{{ item.time }}</span>
              <el-button type="danger" size="small" @click.stop="handleDelete(item.id)">取消收藏</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import request from "@/utils/request";
import { reactive, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";

const router = useRouter();
const user = JSON.parse(localStorage.getItem('system-user') || '{}');

const data = reactive({
  tableData: []
})

const load = () => {
  if (!user.id) return
  request.get('/collect/selectAll', { params: { userId: user.id } }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data || []
    }
  })
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定取消收藏吗?', '提示', { type: 'warning' }).then(() => {
    request.delete('/collect/delete/' + id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success('已取消收藏')
      }
    })
  }).catch(() => {})
}

const goDetail = (goodsId) => {
  router.push('/front/goodsDetail?id=' + goodsId)
}

onMounted(() => {
  load()
})
</script>
