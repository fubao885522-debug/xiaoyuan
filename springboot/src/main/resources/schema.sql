-- ============================================
-- 商品分类表
-- ============================================
CREATE TABLE IF NOT EXISTS `category` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name` VARCHAR(100) NOT NULL COMMENT '分类名称',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '分类描述',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类';

-- ============================================
-- 商品信息表
-- ============================================
CREATE TABLE IF NOT EXISTS `goods` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name` VARCHAR(200) NOT NULL COMMENT '商品名称',
    `img` VARCHAR(500) DEFAULT NULL COMMENT '商品图片',
    `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '价格',
    `description` VARCHAR(1000) DEFAULT NULL COMMENT '简介',
    `content` TEXT DEFAULT NULL COMMENT '详情',
    `store` INT NOT NULL DEFAULT 0 COMMENT '库存',
    `category_id` INT DEFAULT NULL COMMENT '分类ID',
    `status` VARCHAR(10) NOT NULL DEFAULT '上架' COMMENT '上架状态',
    `recommend` TINYINT(1) DEFAULT 0 COMMENT '是否推荐',
    `views` INT NOT NULL DEFAULT 0 COMMENT '浏览量',
    `sale_count` INT NOT NULL DEFAULT 0 COMMENT '销量',
    `time` VARCHAR(50) DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品信息';

-- ============================================
-- 商品评论表
-- ============================================
CREATE TABLE IF NOT EXISTS `comment` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `goods_id` INT DEFAULT NULL COMMENT '商品ID',
    `user_id` INT DEFAULT NULL COMMENT '用户ID',
    `user_name` VARCHAR(255) DEFAULT NULL COMMENT '用户名',
    `content` TEXT COMMENT '评论内容',
    `rating` INT DEFAULT 5 COMMENT '评分 1-5',
    `order_id` INT DEFAULT NULL COMMENT '订单ID',
    `time` VARCHAR(255) DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品评论表';

-- ============================================
-- 用户收藏表
-- ============================================
CREATE TABLE IF NOT EXISTS `collect` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` INT DEFAULT NULL COMMENT '用户ID',
    `goods_id` INT DEFAULT NULL COMMENT '商品ID',
    `goods_name` VARCHAR(255) DEFAULT NULL COMMENT '商品名称',
    `goods_img` VARCHAR(500) DEFAULT NULL COMMENT '商品图片',
    `goods_price` DECIMAL(10,2) DEFAULT NULL COMMENT '商品价格',
    `time` VARCHAR(50) DEFAULT NULL COMMENT '收藏时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏';

-- ============================================
-- 用户充值表
-- ============================================
CREATE TABLE IF NOT EXISTS `recharge` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `money` DECIMAL(10,2) DEFAULT NULL COMMENT '充值金额',
    `user_id` INT DEFAULT NULL COMMENT '充值人',
    `type` VARCHAR(255) DEFAULT NULL COMMENT '支付方式',
    `time` VARCHAR(255) DEFAULT NULL COMMENT '充值时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户充值';

-- ============================================
-- 购物车表
-- ============================================
CREATE TABLE IF NOT EXISTS `cart` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `goods_id` INT NOT NULL COMMENT '商品ID',
    `user_id` INT NOT NULL COMMENT '用户ID',
    `num` INT NOT NULL DEFAULT 1 COMMENT '数量',
    `goods_name` VARCHAR(255) DEFAULT NULL COMMENT '商品名称',
    `goods_img` VARCHAR(500) DEFAULT NULL COMMENT '商品图片',
    `price` DECIMAL(10,2) DEFAULT NULL COMMENT '商品单价',
    `create_time` VARCHAR(50) DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- ============================================
-- 订单表
-- ============================================
CREATE TABLE IF NOT EXISTS `orders` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `order_no` VARCHAR(50) DEFAULT NULL COMMENT '订单编号',
    `user_id` INT DEFAULT NULL COMMENT '用户ID',
    `user_name` VARCHAR(255) DEFAULT NULL COMMENT '用户名',
    `total_price` DECIMAL(10,2) DEFAULT NULL COMMENT '总金额',
    `status` VARCHAR(50) DEFAULT NULL COMMENT '支付状态',
    `delivery_type` VARCHAR(50) DEFAULT NULL COMMENT '配送类型',
    `order_status` VARCHAR(50) DEFAULT NULL COMMENT '订单状态',
    `address` VARCHAR(500) DEFAULT NULL COMMENT '收货地址',
    `deliver` VARCHAR(500) DEFAULT NULL COMMENT '配送备注',
    `time` VARCHAR(50) DEFAULT NULL COMMENT '下单时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ============================================
-- 订单明细表
-- ============================================
CREATE TABLE IF NOT EXISTS `orders_detail` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `order_id` INT DEFAULT NULL COMMENT '订单ID',
    `goods_id` INT DEFAULT NULL COMMENT '商品ID',
    `goods_name` VARCHAR(255) DEFAULT NULL COMMENT '商品名称',
    `goods_img` VARCHAR(500) DEFAULT NULL COMMENT '商品图片',
    `price` DECIMAL(10,2) DEFAULT NULL COMMENT '商品单价',
    `num` INT DEFAULT NULL COMMENT '购买数量',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';
