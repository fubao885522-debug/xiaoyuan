import {createRouter, createWebHistory} from 'vue-router'
import {ElMessage} from "element-plus";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/login' },
    {
      path: '/manager',
      component: () => import('@/views/Manager.vue'),
      redirect: '/manager/home',
      meta: { role: '管理员' },
      children: [
        { path: 'home', component: () => import('@/views/manager/Home.vue')},
        { path: 'admin', component: () => import('@/views/manager/Admin.vue')},
        { path: 'user', component: () => import('@/views/manager/User.vue')},
        { path: 'person', component: () => import('@/views/manager/Person.vue')},
        { path: 'password', component: () => import('@/views/manager/Password.vue')},
        { path: 'catgory', component: () => import('@/views/manager/Category.vue')},
        { path: 'goods', component: () => import('@/views/manager/Goods.vue')},
        { path: 'carousel', component: () => import('@/views/manager/Carousel.vue')},
        { path: 'comment', component: () => import('@/views/manager/Comment.vue')},
        { path: 'collect', component: () => import('@/views/manager/Collect.vue')},
        { path: 'recharge', component: () => import('@/views/manager/Recharge.vue')},
        { path: 'orders', component: () => import('@/views/manager/Orders.vue')},
        { path: 'stats', component: () => import('@/views/manager/Stats.vue')},
      ]
    },

    {
      path: '/front',
      component: () => import('@/views/Front.vue'),
      redirect: '/front/home',
      meta: { role: '用户' },
      children: [
        { path: 'home', component: () => import('@/views/front/Home.vue')},
        { path: 'jingxuan', component: () => import('@/views/front/Jingxuan.vue')},
        { path: 'goodsDetail', component: () => import('@/views/front/GoodsDetail.vue')},
        { path: 'person', component: () => import('@/views/front/Person.vue')},
        { path: 'password', component: () => import('@/views/front/Password.vue')},
        { path: 'collect', component: () => import('@/views/front/MyCollect.vue')},
        { path: 'recharge', component: () => import('@/views/front/MyRecharge.vue')},
        { path: 'cart', component: () => import('@/views/front/Cart.vue')},
        { path: 'orders', component: () => import('@/views/front/Orders.vue')},
      ]
    },
    { path: '/login', component: () => import('@/views/Login.vue') },
    { path: '/register', component: () => import('@/views/Register.vue') }
  ]
})

// 导航守卫：管理后台仅管理员，前台仅普通用户
router.beforeEach((to, from, next) => {
  const userStr = localStorage.getItem('system-user');
  const user = userStr ? JSON.parse(userStr) : null;
  const role = user?.role || '';

  if (to.path.startsWith('/manager')) {
    if (!user) {
      ElMessage.warning('请先登录');
      next('/login');
      return;
    }
    if (role !== '管理员') {
      ElMessage.warning('您没有权限访问管理后台，请使用管理员账号登录');
      next('/front/home');
      return;
    }
  }

  if (to.path.startsWith('/front')) {
    if (!user) {
      ElMessage.warning('请先登录');
      next('/login');
      return;
    }
    if (role !== '用户') {
      ElMessage.warning('请使用普通用户账号访问前台');
      next('/manager/home');
      return;
    }
  }

  next();
});

export default router
