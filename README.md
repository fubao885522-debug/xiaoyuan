# 校园外卖平台（毕业设计）

基于 **Spring Boot 3 + MyBatis + MySQL + Vue 3 + Element Plus** 的校园外卖商城系统，包含前台用户端与后台管理端，覆盖商品、购物车、订单、评论、收藏、充值、数据统计等完整业务闭环。

## ✨ 功能特性

### 用户端（前台）
- 商品浏览、分类筛选、轮播图展示、精选推荐
- 商品详情（富文本介绍）
- 购物车管理、下单、订单查看
- 商品评论与收藏
- 余额充值（模拟支付）
- 个人中心、修改密码

### 管理端（后台）
- 管理员 / 用户账号管理
- 商品、分类、轮播图、评论、收藏管理
- 订单管理（含订单明细）
- 充值记录管理
- 数据统计看板（ECharts 可视化）

## 🛠 技术栈

| 端 | 技术 |
|---|---|
| 后端 | Java 21 · Spring Boot 3.3 · MyBatis · MySQL · PageHelper · Hutool · Lombok |
| 前端 | Vue 3 · Vite · Vue Router · Element Plus · ECharts · Axios · wangEditor |
| 鉴权 | JWT + 登录拦截器 |
| 其他 | Maven · Postman · 全局异常处理 · CORS · 文件上传 |

## 📦 数据库设计

8 张表：`category`（分类）、`goods`（商品）、`comment`（评论）、`collect`（收藏）、`recharge`（充值）、`cart`（购物车）、`orders`（订单）、`orders_detail`（订单明细）

## 🚀 快速开始

### 后端

1. 用 IDEA 打开 `springboot/` 目录（Maven 项目，自动拉取依赖）
2. 修改 `src/main/resources/application.yml` 中的数据库连接（默认端口 `3307`、库名 `code2026`；首次启动自动执行 `schema.sql` 建表）
3. 运行 `SpringbootApplication.java`，服务默认端口 `9090`

### 前端

```bash
cd vue
npm install
npm run dev
```

## 📁 目录结构

```
springboot/   # 后端：controller / mapper / entity / common(含 JWT、拦截器) / exception
vue/          # 前端：views(front 用户端 + manager 管理端) / router / utils
files/        # 界面截图等资源
```

## ⚠️ 说明

- 本仓库为个人毕业设计学习项目，部分配置（如数据库密码）为本地开发默认值，请按需修改
- 涉及隐私的凭据请勿提交到公开仓库
