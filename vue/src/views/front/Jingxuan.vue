<template>
  <div class="front-container">
    <div class="card">
      <h3 style="margin-bottom: 16px; font-size: 20px; color: #333">精选商品</h3>

      <!-- 分类标签 -->
      <div class="category-tabs">
        <span
          :class="['tab-item', { active: activeCategory === '' }]"
          @click="changeCategory('')"
        >全部</span>
        <span
          v-for="cat in data.categoryList"
          :key="cat.id"
          :class="['tab-item', { active: activeCategory === String(cat.id) }]"
          @click="changeCategory(String(cat.id))"
        >{{ cat.name }}</span>
      </div>

      <!-- 商品列表 -->
      <el-row :gutter="16" style="margin-top: 16px">
        <el-col :span="6" v-for="item in data.goodsList" :key="item.id">
          <div class="goods-item" @click="$router.push('/front/goodsDetail?id=' + item.id)">
            <img :src="item.img" style="width: 100%; height: 200px; object-fit: cover; border-radius: 4px;" />
            <div style="margin-top: 8px; font-size: 14px; line-height: 1.5; height: 42px; overflow: hidden;">{{ item.name }}</div>
            <div style="display: flex; align-items: center; justify-content: space-between; margin-top: 6px">
              <span style="color: #e74c3c; font-size: 18px;">￥{{ item.price }}</span>
              <span style="color: #999; font-size: 12px;">已售 {{ item.saleCount || 0 }}</span>
            </div>
          </div>
        </el-col>
      </el-row>
      <div v-if="data.goodsList.length === 0" style="text-align: center; color: #999; padding: 40px 0">暂无商品</div>
    </div>
  </div>
</template>

<script setup>
import request from "@/utils/request";
import { reactive, ref, onMounted } from "vue";

const activeCategory = ref('')

const data = reactive({
  categoryList: [],
  goodsList: []
})

// 加载分类
const loadCategories = () => {
  request.get('/category/selectAll').then(res => {
    if (res.code === '200') {
      data.categoryList = res.data || []
    }
  })
}

// 加载商品（已上架的所有商品）
const loadGoods = () => {
  const params = { status: '上架' }
  if (activeCategory.value) {
    params.categoryId = activeCategory.value
  }
  request.get('/goods/selectAll', { params }).then(res => {
    if (res.code === '200') {
      data.goodsList = res.data || []
    }
  })
}

// 切换分类
const changeCategory = (catId) => {
  activeCategory.value = catId
  loadGoods()
}

onMounted(() => {
  loadCategories()
  loadGoods()
})
</script>

<style scoped>
.category-tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  padding-bottom: 16px;
  border-bottom: 1px solid #eee;
}
.tab-item {
  display: inline-block;
  padding: 6px 18px;
  border-radius: 20px;
  font-size: 14px;
  color: #666;
  background: #f5f5f5;
  cursor: pointer;
  transition: all 0.3s;
}
.tab-item:hover {
  color: #4a7cf7;
  background: #e8edff;
}
.tab-item.active {
  color: #fff;
  background: #4a7cf7;
}
.goods-item {
  margin-bottom: 16px;
  cursor: pointer;
  padding: 12px;
  border-radius: 8px;
  border: 1px solid transparent;
  transition: all 0.3s;
}
.goods-item:hover {
  background: #f5f5f5;
  border-color: #000;
  transform: scale(1.05);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
}
</style>
