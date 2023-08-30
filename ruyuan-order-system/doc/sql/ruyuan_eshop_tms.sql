CREATE database if NOT EXISTS `ruyuan_eshop_tms` default character set utf8 collate utf8_general_ci;
use `ruyuan_eshop_tms`;

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for logistic_order
-- ----------------------------
DROP TABLE IF EXISTS `logistic_order`;
CREATE TABLE `logistic_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `business_identifier` tinyint(4) NOT NULL COMMENT '接入方业务线标识  1, "自营商城"',
  `order_id` varchar(50) NOT NULL COMMENT '订单ID',
  `seller_id` varchar(50) DEFAULT NULL COMMENT '卖家编号',
  `user_id` varchar(50) DEFAULT NULL COMMENT '买家编号',
  `logistic_code` varchar(50) NOT NULL COMMENT '物流单号',
  `content` varchar(1024) NOT NULL COMMENT '物流单内容',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_logistic_code` (`logistic_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='物流单';

SET FOREIGN_KEY_CHECKS = 1;
