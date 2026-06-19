<template>
  <div class="stats-page">
    <!-- 统计卡片 -->
    <div class="stats-row">
      <div class="stat-card purple">
        <div class="stat-icon">
          <el-icon size="28"><Coin /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">¥{{ formatNum(data.totalSales) }}</div>
          <div class="stat-label">销售总额</div>
        </div>
      </div>
      <div class="stat-card pink">
        <div class="stat-icon">
          <el-icon size="28"><Present /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">¥{{ formatNum(data.todaySales) }}</div>
          <div class="stat-label">今日销售</div>
        </div>
      </div>
      <div class="stat-card blue">
        <div class="stat-icon">
          <el-icon size="28"><Goods /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ data.goodsCount }}</div>
          <div class="stat-label">商品总数</div>
        </div>
      </div>
      <div class="stat-card green">
        <div class="stat-icon">
          <el-icon size="28"><User /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ data.userCount }}</div>
          <div class="stat-label">注册数量</div>
        </div>
      </div>
    </div>

    <!-- 今日数据 -->
    <div class="today-row">
      <div class="today-card">
        <div class="today-inner">
          <div class="today-icon-wrap">
            <el-icon size="22"><Tickets /></el-icon>
          </div>
          <div class="today-info">
            <div class="today-label">今日订单</div>
            <div class="today-value">{{ data.todayOrders }}</div>
          </div>
        </div>
      </div>
      <div class="today-card">
        <div class="today-inner">
          <div class="today-icon-wrap" style="background: linear-gradient(135deg, #f093fb, #f5576c);">
            <el-icon size="22"><Money /></el-icon>
          </div>
          <div class="today-info">
            <div class="today-label">今日销售额</div>
            <div class="today-value price">¥{{ formatNum(data.todaySales) }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-row">
      <div class="card chart-card">
        <div class="chart-title">近7天销售趋势</div>
        <div ref="salesChartRef" class="chart-box"></div>
      </div>
      <div class="card chart-card">
        <div class="chart-title">分类销售额占比</div>
        <div ref="categoryChartRef" class="chart-box"></div>
      </div>
    </div>

    <!-- 商品销量 Top5 -->
    <div class="card top5-card">
      <div class="chart-title">商品销量 Top5</div>
      <div class="top5-list" v-if="data.top5.length > 0">
        <div v-for="(item, index) in data.top5" :key="index" class="top5-item">
          <span class="top5-rank" :class="'rank-' + (index + 1)">{{ index + 1 }}</span>
          <span class="top5-name">{{ item.name }}</span>
          <div class="top5-bar-wrap">
            <div class="top5-bar" :style="{ width: top5Percent(index) + '%' }"></div>
          </div>
          <span class="top5-num">{{ item.num }} 件</span>
        </div>
      </div>
      <div v-else class="empty-tip">暂无销售数据</div>
    </div>
  </div>
</template>

<script setup>
import request from "@/utils/request";
import { reactive, onMounted, ref, nextTick } from "vue";
import * as echarts from "echarts";

const salesChartRef = ref(null);
const categoryChartRef = ref(null);
let salesChart = null;
let categoryChart = null;

const data = reactive({
  totalSales: 0,
  todaySales: 0,
  goodsCount: 0,
  userCount: 0,
  todayOrders: 0,
  sales7: [],
  top5: [],
  orderStatus: [],
  salesByCategory: []
});

const formatNum = (num) => {
  if (num === null || num === undefined) return '0';
  return Number(num).toLocaleString('zh-CN', { minimumFractionDigits: 0, maximumFractionDigits: 0 });
};

const top5Percent = (index) => {
  if (data.top5.length === 0) return 0;
  const max = Math.max(...data.top5.map(item => item.num), 1);
  return ((data.top5[index].num / max) * 100).toFixed(0);
};

const loadData = () => {
  // 加载前台统计
  request.get('/dashboard/frontStats').then(res => {
    if (res.code === '200') {
      const d = res.data || {};
      data.totalSales = d.totalSales || 0;
      data.todaySales = d.todaySales || 0;
      data.goodsCount = d.goodsCount || 0;
      data.userCount = d.userCount || 0;
    }
  });
  // 加载仪表盘数据
  request.get('/dashboard/data').then(res => {
    if (res.code === '200') {
      const d = res.data;
      data.todayOrders = d.todayOrders || 0;
      data.sales7 = d.sales7 || [];
      data.top5 = d.top5 || [];
      data.orderStatus = d.orderStatus || [];
      data.salesByCategory = d.salesByCategory || [];
      nextTick(() => {
        renderSalesChart();
        renderCategoryChart();
      });
    }
  });
};

const renderSalesChart = () => {
  if (!salesChartRef.value) return;
  if (salesChart) salesChart.dispose();
  salesChart = echarts.init(salesChartRef.value);

  const dates = data.sales7.map(item => item.date);
  const amounts = data.sales7.map(item => item.amount);

  salesChart.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: 50, right: 20, top: 20, bottom: 30 },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: { fontSize: 11, rotate: dates.length > 5 ? 30 : 0 }
    },
    yAxis: {
      type: 'value',
      axisLabel: { formatter: '¥{value}' }
    },
    series: [{
      data: amounts,
      type: 'line',
      smooth: true,
      symbol: 'circle',
      symbolSize: 8,
      lineStyle: { color: '#4a7cf7', width: 3 },
      itemStyle: { color: '#4a7cf7' },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(74,124,247,0.3)' },
          { offset: 1, color: 'rgba(74,124,247,0.02)' }
        ])
      }
    }]
  });
};

const renderCategoryChart = () => {
  if (!categoryChartRef.value) return;
  if (categoryChart) categoryChart.dispose();
  categoryChart = echarts.init(categoryChartRef.value);

  const list = data.salesByCategory || [];
  const names = list.map(item => item.name || '其他');
  const values = list.map(item => Number(item.amount) || 0);

  const colors = ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de', '#3ba272', '#fc8452', '#9a60b4'];

  categoryChart.setOption({
    tooltip: {
      trigger: 'item',
      formatter: '{b}: ¥{c} ({d}%)'
    },
    legend: {
      bottom: 0,
      textStyle: { fontSize: 12 }
    },
    series: [{
      type: 'pie',
      radius: ['40%', '68%'],
      center: ['50%', '43%'],
      avoidLabelOverlap: false,
      label: {
        show: true,
        position: 'outside',
        formatter: '{b}\n{d}%',
        fontSize: 11,
        color: '#666'
      },
      emphasis: {
        label: { show: true, fontSize: 16, fontWeight: 'bold' }
      },
      data: names.map((name, i) => ({
        name,
        value: values[i],
        itemStyle: { color: colors[i % colors.length] }
      }))
    }]
  });
};

onMounted(() => {
  loadData();
});

window.addEventListener('resize', () => {
  salesChart?.resize();
  categoryChart?.resize();
});
</script>

<style scoped>
.stats-page {
  padding: 0 5px;
}

/* 四列统计卡片 */
.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 16px;
}
.stat-card {
  background: #fff;
  border-radius: 10px;
  padding: 24px 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  transition: transform 0.2s;
}
.stat-card:hover { transform: translateY(-3px); }
.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
}
.stat-card.purple .stat-icon { background: linear-gradient(135deg, #667eea, #764ba2); }
.stat-card.pink .stat-icon { background: linear-gradient(135deg, #f093fb, #f5576c); }
.stat-card.blue .stat-icon { background: linear-gradient(135deg, #4facfe, #00f2fe); }
.stat-card.green .stat-icon { background: linear-gradient(135deg, #43e97b, #38f9d7); }

.stat-value {
  font-size: 30px;
  font-weight: bold;
  color: #333;
  line-height: 1;
}
.stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 6px;
}

/* 今日数据 */
.today-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-bottom: 16px;
}
.today-card {
  background: #fff;
  border-radius: 10px;
  padding: 20px 24px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}
.today-inner {
  display: flex;
  align-items: center;
  gap: 16px;
}
.today-icon-wrap {
  width: 48px;
  height: 48px;
  border-radius: 10px;
  background: linear-gradient(135deg, #4a7cf7, #6c9cff);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
}
.today-label { font-size: 14px; color: #999; }
.today-value { font-size: 26px; font-weight: bold; color: #333; margin-top: 4px; }
.today-value.price { font-size: 24px; }

/* 图表行 */
.charts-row {
  display: grid;
  grid-template-columns: 3fr 2fr;
  gap: 16px;
  margin-bottom: 16px;
}
.card {
  background: #fff;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}
.chart-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}
.chart-box {
  width: 100%;
  height: 280px;
}

/* Top5 列表 */
.top5-card {
  margin-bottom: 10px;
}
.top5-list {
  padding: 0 10px;
}
.top5-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;
  gap: 12px;
}
.top5-item:last-child { border-bottom: none; }
.top5-rank {
  width: 26px;
  height: 26px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: bold;
  color: #fff;
  flex-shrink: 0;
}
.rank-1 { background: #ff4d4f; }
.rank-2 { background: #ff7a45; }
.rank-3 { background: #ffa940; }
.rank-4, .rank-5 { background: #bbb; }
.top5-name {
  width: 120px;
  flex-shrink: 0;
  font-size: 14px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.top5-bar-wrap {
  flex: 1;
  height: 10px;
  background: #f0f0f0;
  border-radius: 5px;
  overflow: hidden;
}
.top5-bar {
  height: 100%;
  background: linear-gradient(90deg, #4a7cf7, #6c9cff);
  border-radius: 5px;
  transition: width 0.8s ease;
}
.top5-num {
  width: 50px;
  text-align: right;
  font-size: 13px;
  color: #666;
  flex-shrink: 0;
}
.empty-tip {
  text-align: center;
  color: #999;
  padding: 40px 0;
}
</style>
