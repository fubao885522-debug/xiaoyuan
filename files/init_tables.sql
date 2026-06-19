-- ============================================
-- 商品分类表
-- ============================================
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name` VARCHAR(100) NOT NULL COMMENT '分类名称',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '分类描述',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类';

-- ============================================
-- 商品信息表
-- ============================================
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name` VARCHAR(200) NOT NULL COMMENT '商品名称',
    `img` VARCHAR(500) DEFAULT NULL COMMENT '商品图片',
    `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '价格',
    `description` VARCHAR(1000) DEFAULT NULL COMMENT '简介',
    `content` TEXT DEFAULT NULL COMMENT '详情',
    `store` INT NOT NULL DEFAULT 0 COMMENT '库存',
    `category_id` INT DEFAULT NULL COMMENT '分类ID',
    `status` VARCHAR(10) NOT NULL DEFAULT '上架' COMMENT '上架状态',
    `views` INT NOT NULL DEFAULT 0 COMMENT '浏览量',
    `sale_count` INT NOT NULL DEFAULT 0 COMMENT '销量',
    `time` VARCHAR(50) DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品信息';
