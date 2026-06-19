CREATE TABLE IF NOT EXISTS `comment` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `goods_id` int DEFAULT NULL COMMENT '商品ID',
  `user_id` int DEFAULT NULL COMMENT '用户ID',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `content` text COMMENT '评论内容',
  `rating` int DEFAULT '5' COMMENT '评分 1-5',
  `time` varchar(255) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品评论表';
