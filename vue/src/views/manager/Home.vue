<template>
  <div class="home-page">
    <div class="welcome-card">
      <h2>欢迎回来，{{ data.user.name || '管理员' }}！</h2>
      <p>今天是 {{ today }}，祝您工作愉快！</p>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}')
});

const today = new Date().toLocaleDateString('zh-CN', {
  year: 'numeric',
  month: 'long',
  day: 'numeric',
  weekday: 'long'
});
</script>

<style scoped>
.home-page {
  padding: 0 5px;
}
.welcome-card {
  background: #fff;
  border-radius: 10px;
  padding: 40px 30px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  text-align: center;
}
.welcome-card h2 {
  font-size: 24px;
  color: #333;
  margin: 0 0 10px 0;
}
.welcome-card p {
  font-size: 15px;
  color: #999;
  margin: 0;
}
</style>
