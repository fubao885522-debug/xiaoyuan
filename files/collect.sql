CREATE TABLE `collect` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int DEFAULT NULL COMMENT '用户ID',
  `goods_id` int DEFAULT NULL COMMENT '商品ID',
  `goods_name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `goods_img` varchar(500) DEFAULT NULL COMMENT '商品图片',
  `goods_price` decimal(10,2) DEFAULT NULL COMMENT '商品价格',
  `time` varchar(50) DEFAULT NULL COMMENT '收藏时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏';
