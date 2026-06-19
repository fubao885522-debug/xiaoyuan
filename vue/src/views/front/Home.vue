<template>
  <div class="front-container">
    <!-- 顶部区域：轮播图 + 推荐 -->
    <el-row :gutter="16" style="margin-bottom: 16px; margin-top: 16px">
      <!-- 左侧轮播图 -->
      <el-col :span="17">
        <el-carousel height="400px" v-if="data.carouselList.length > 0">
          <el-carousel-item v-for="item in data.carouselList" :key="item.id">
            <img :src="item.img" style="width: 100%; height: 100%; object-fit: cover;" />
          </el-carousel-item>
        </el-carousel>
      </el-col>

      <!-- 右侧推荐 -->
      <el-col :span="7">
        <el-card class="recommend-card" style="height: 400px; display: flex; flex-direction: column;">
          <template #header>
            <span style="color: #e74c3c; font-weight: bold">❤ 为您推荐</span>
          </template>
          <div style="flex: 1; overflow-y: auto;">
            <div v-for="item in data.recommendGoods" :key="item.id"
                 style="display: flex; align-items: center; margin-bottom: 12px; cursor: pointer; padding: 8px; border-radius: 6px;"
                 @click="$router.push('/front/goodsDetail?id=' + item.id)"
                 @mouseover="$event.currentTarget.style.background='#f5f5f5'"
                 @mouseout="$event.currentTarget.style.background='none'">
              <img :src="item.img" style="width: 80px; height: 80px; border-radius: 4px; object-fit: cover; flex-shrink: 0;" />
              <div style="margin-left: 10px; overflow: hidden;">
                <div style="font-weight: bold; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">{{ item.name }}</div>
                <div style="color: #e74c3c; font-size: 15px; margin-top: 4px">￥{{ item.price }}</div>
              </div>
            </div>
            <div v-if="data.recommendGoods.length === 0" style="text-align: center; color: #999; padding: 20px 0">暂无推荐商品</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 商品列表 -->
    <div class="card">
      <h3 style="margin-bottom: 10px">最新商品</h3>
      <el-row :gutter="16">
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
    </div>

  </div>

  <!-- 底部 -->
  <div class="footer">
    <div class="footer-title">⭐ 外卖小卖部</div>
    <div class="footer-info">
      <span>联系电话：010-55668899</span>
      <span>邮箱：xxx@163.com</span>
    </div>
    <div class="footer-copy">©QING MUX, powered by qinggege THX.</div>
    <div class="footer-beian">备案：浙ICP备2025339988992号-1  浙公网安备 8312321999111 号</div>
  </div>
</template>

<script setup>
import request from "@/utils/request";
import { reactive, onMounted, watch } from "vue";
import { useRoute } from "vue-router";

const route = useRoute()

const data = reactive({
  carouselList: [],
  goodsList: [],
  recommendGoods: []
})

// 加载轮播图
const loadCarousel = () => {
  request.get('/carousel/selectAll').then(res => {
    if (res.code === '200') {
      const list = res.data || []
      // 随机打乱顺序
      for (let i = list.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [list[i], list[j]] = [list[j], list[i]]
      }
      // 只取前4张
      data.carouselList = list.slice(0, 4)
    }
  })
}

// 加载推荐商品
const loadRecommend = () => {
  request.get('/goods/selectAll', { params: { recommend: true } }).then(res => {
    if (res.code === '200') {
      data.recommendGoods = res.data?.list || (res.data || []).filter(item => item.recommend)
    }
  })
}

// 加载商品
const loadGoods = () => {
  const params = {}
  const name = route.query.name
  if (name) {
    params.name = name
  }
  request.get('/goods/selectAll', { params }).then(res => {
    if (res.code === '200') {
      data.goodsList = res.data || []
    }
  })
}

onMounted(() => {
  loadCarousel()
  loadRecommend()
  loadGoods()
})

// 监听路由变化，重新搜索
watch(() => route.query.name, () => {
  loadGoods()
})
</script>

<style scoped>
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
.footer {
  background: #2e3143;
  color: #aaa;
  padding: 30px 0;
  text-align: center;
  font-size: 13px;
  width: 100vw;
  margin-left: calc(-50vw + 50%);
  line-height: 2;
}
.footer-title {
  font-size: 18px;
  font-weight: bold;
  color: #fff;
  margin-bottom: 10px;
}
.footer-info span {
  margin: 0 15px;
  color: #ccc;
}
.footer-copy {
  color: #888;
  margin-top: 6px;
}
.footer-beian {
  color: #777;
  font-size: 12px;
}
</style>