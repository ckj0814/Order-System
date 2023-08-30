CREATE database if NOT EXISTS `ruyuan_eshop_order` default character set utf8 collate utf8_general_ci;
use `ruyuan_eshop_order`;

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for after_sale_info
-- ----------------------------
DROP TABLE IF EXISTS `after_sale_info`;
CREATE TABLE `after_sale_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `business_identifier` tinyint(4) DEFAULT NULL COMMENT '接入方业务线标识  1, "自营商城"',
  `after_sale_id` bigint(20) NOT NULL COMMENT '售后id',
  `order_id` varchar(50) NOT NULL DEFAULT '' COMMENT '订单号',
  `order_source_channel` tinyint(4) NOT NULL COMMENT '订单来源渠道',
  `user_id` varchar(20) NOT NULL COMMENT '购买用户id',
  `after_sale_status` tinyint(4) NOT NULL COMMENT '售后单状态',
  `order_type` tinyint(4) NOT NULL COMMENT '订单类型',
  `apply_source` tinyint(4) DEFAULT NULL COMMENT '申请售后来源',
  `apply_time` datetime DEFAULT NULL COMMENT '申请售后时间',
  `apply_reason_code` tinyint(4) DEFAULT NULL COMMENT '申请原因编码',
  `apply_reason` varchar(1024) DEFAULT NULL COMMENT '申请原因',
  `review_time` datetime DEFAULT NULL COMMENT '客服审核时间',
  `review_source` tinyint(4) DEFAULT NULL COMMENT '客服审核来源',
  `review_reason_code` tinyint(4) DEFAULT NULL COMMENT '客服审核结果编码',
  `review_reason` varchar(1024) DEFAULT NULL COMMENT '客服审核结果',
  `after_sale_type` tinyint(4) NOT NULL COMMENT '售后类型',
  `after_sale_type_detail` tinyint(4) DEFAULT NULL COMMENT '售后类型详情枚举',
  `apply_refund_amount` int(11) DEFAULT NULL COMMENT '申请退款金额',
  `real_refund_amount` int(11) DEFAULT NULL COMMENT '实际退款金额',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_after_sale_type` (`after_sale_type`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8 COMMENT='订单售后表';

-- ----------------------------
-- Records of after_sale_info
-- ----------------------------
BEGIN;
INSERT INTO `after_sale_info` VALUES (125, 1, 2021120932956945103, '1021120932958737103', 1, '103', 40, 1, 20, '2021-12-09 20:41:17', 80, '取消订单', '2021-12-09 20:41:17', NULL, NULL, NULL, 1, 2, 9600, 9600, '超时未支付自动取消', '2021-12-09 20:41:17', '2021-12-09 20:41:17');
INSERT INTO `after_sale_info` VALUES (126, 1, 2021121032907520104, '1021121032958993104', 1, '104', 40, 1, 20, '2021-12-10 12:06:49', 80, '取消订单', '2021-12-10 12:06:49', NULL, NULL, NULL, 1, 2, 9600, 9600, '超时未支付自动取消', '2021-12-10 12:06:49', '2021-12-10 12:06:49');
INSERT INTO `after_sale_info` VALUES (127, 1, 2021121032907776105, '1021121032909568105', 1, '105', 40, 1, 20, '2021-12-10 12:53:59', 80, '取消订单', '2021-12-10 12:53:59', NULL, NULL, NULL, 1, 2, 9600, 9600, '超时未支付自动取消', '2021-12-10 12:53:59', '2021-12-10 12:53:59');
COMMIT;

-- ----------------------------
-- Table structure for after_sale_item
-- ----------------------------
DROP TABLE IF EXISTS `after_sale_item`;
CREATE TABLE `after_sale_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `after_sale_id` bigint(20) NOT NULL COMMENT '售后id',
  `order_id` varchar(50) NOT NULL DEFAULT '' COMMENT '订单id',
  `sku_code` varchar(20) NOT NULL DEFAULT '' COMMENT 'sku id',
  `product_name` varchar(1024) NOT NULL COMMENT '商品名',
  `return_quantity` tinyint(4) NOT NULL COMMENT '商品退货数量',
  `product_img` varchar(1024) NOT NULL COMMENT '商品图片地址',
  `origin_amount` int(11) NOT NULL COMMENT '商品总金额',
  `apply_refund_amount` int(11) NOT NULL COMMENT '申请退款金额',
  `real_refund_amount` int(11) NOT NULL COMMENT '实际退款金额',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='订单售后条目表';

-- ----------------------------
-- Records of after_sale_item
-- ----------------------------
BEGIN;
INSERT INTO `after_sale_item` VALUES (1, 2021120932956945103, '1021120932958737103', '10101010', '测试商品', 10, 'test.img', 10000, 10000, 9504, '2021-12-09 20:41:17', '2021-12-09 20:41:17');
INSERT INTO `after_sale_item` VALUES (2, 2021120932956945103, '1021120932958737103', '10101011', 'demo商品', 1, 'demo.img', 100, 100, 96, '2021-12-09 20:41:17', '2021-12-09 20:41:17');
INSERT INTO `after_sale_item` VALUES (3, 2021121032907520104, '1021121032958993104', '10101010', '测试商品', 10, 'test.img', 10000, 10000, 9504, '2021-12-10 12:06:49', '2021-12-10 12:06:49');
INSERT INTO `after_sale_item` VALUES (4, 2021121032907520104, '1021121032958993104', '10101011', 'demo商品', 1, 'demo.img', 100, 100, 96, '2021-12-10 12:06:49', '2021-12-10 12:06:49');
INSERT INTO `after_sale_item` VALUES (5, 2021121032907776105, '1021121032909568105', '10101010', '测试商品', 10, 'test.img', 10000, 10000, 9504, '2021-12-10 12:53:59', '2021-12-10 12:53:59');
INSERT INTO `after_sale_item` VALUES (6, 2021121032907776105, '1021121032909568105', '10101011', 'demo商品', 1, 'demo.img', 100, 100, 96, '2021-12-10 12:53:59', '2021-12-10 12:53:59');
COMMIT;

-- ----------------------------
-- Table structure for after_sale_log
-- ----------------------------
DROP TABLE IF EXISTS `after_sale_log`;
CREATE TABLE `after_sale_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `after_sale_id` varchar(50) NOT NULL COMMENT '售后单号',
  `pre_status` tinyint(4) NOT NULL COMMENT '前一个状态',
  `current_status` tinyint(4) NOT NULL COMMENT '当前状态',
  `remark` varchar(1024) NOT NULL COMMENT '备注',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8 COMMENT='售后单变更表';

-- ----------------------------
-- Records of after_sale_log
-- ----------------------------
BEGIN;
INSERT INTO `after_sale_log` VALUES (40, '2021120932956945103', 0, 20, '超时未支付自动取消', '2021-12-09 20:41:17', '2021-12-09 20:41:17');
INSERT INTO `after_sale_log` VALUES (41, '2021120932956945103', 20, 40, '售后退款中', '2021-12-09 20:41:18', '2021-12-09 20:41:18');
INSERT INTO `after_sale_log` VALUES (42, '2021121032907520104', 0, 20, '超时未支付自动取消', '2021-12-10 12:06:49', '2021-12-10 12:06:49');
INSERT INTO `after_sale_log` VALUES (43, '2021121032907520104', 20, 40, '售后退款中', '2021-12-10 12:06:49', '2021-12-10 12:06:49');
INSERT INTO `after_sale_log` VALUES (44, '2021121032907776105', 0, 20, '超时未支付自动取消', '2021-12-10 12:53:59', '2021-12-10 12:53:59');
INSERT INTO `after_sale_log` VALUES (45, '2021121032907776105', 20, 40, '售后退款中', '2021-12-10 12:53:59', '2021-12-10 12:53:59');
COMMIT;

-- ----------------------------
-- Table structure for after_sale_refund
-- ----------------------------
DROP TABLE IF EXISTS `after_sale_refund`;
CREATE TABLE `after_sale_refund` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `after_sale_id` varchar(64) NOT NULL COMMENT '售后单号',
  `order_id` varchar(64) NOT NULL COMMENT '订单号',
  `after_sale_batch_no` varchar(64) DEFAULT NULL COMMENT '售后批次编号',
  `account_type` tinyint(4) NOT NULL COMMENT '账户类型',
  `pay_type` tinyint(4) NOT NULL COMMENT '支付类型',
  `refund_status` tinyint(4) NOT NULL COMMENT '退款状态',
  `refund_amount` int(11) NOT NULL COMMENT '退款金额',
  `refund_pay_time` datetime DEFAULT NULL COMMENT '退款支付时间',
  `out_trade_no` varchar(64) DEFAULT NULL COMMENT '交易单号',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='售后支付表';

-- ----------------------------
-- Records of after_sale_refund
-- ----------------------------
BEGIN;
INSERT INTO `after_sale_refund` VALUES (1, '2021120932956945103', '1021120932958737103', '10211209329587371032727502051', 1, 10, 10, 9600, NULL, '4886684122681188166', '未退款', '2021-12-09 20:41:17', '2021-12-09 20:41:17');
INSERT INTO `after_sale_refund` VALUES (2, '2021121032907520104', '1021121032958993104', '10211210329589931042172181132', 1, 10, 10, 9600, NULL, '5604472766815226085', '未退款', '2021-12-10 12:06:49', '2021-12-10 12:06:49');
INSERT INTO `after_sale_refund` VALUES (3, '2021121032907776105', '1021121032909568105', '10211210329095681054854423457', 1, 10, 10, 9600, NULL, '8722848625454525184', '未退款', '2021-12-10 12:53:59', '2021-12-10 12:53:59');
COMMIT;

-- ----------------------------
-- Table structure for order_amount
-- ----------------------------
DROP TABLE IF EXISTS `order_amount`;
CREATE TABLE `order_amount` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` varchar(50) NOT NULL COMMENT '订单编号',
  `amount_type` int(11) NOT NULL COMMENT '收费类型',
  `amount` int(11) NOT NULL COMMENT '收费金额',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 COMMENT='订单费用表';

-- ----------------------------
-- Records of order_amount
-- ----------------------------
BEGIN;
INSERT INTO `order_amount` VALUES (21, '1021120832956929100', 10, 10100, '2021-12-08 21:15:36', '2021-12-08 21:15:36');
INSERT INTO `order_amount` VALUES (22, '1021120832956929100', 20, 500, '2021-12-08 21:15:36', '2021-12-08 21:15:36');
INSERT INTO `order_amount` VALUES (23, '1021120832956929100', 30, 0, '2021-12-08 21:15:36', '2021-12-08 21:15:36');
INSERT INTO `order_amount` VALUES (24, '1021120832956929100', 50, 9600, '2021-12-08 21:15:36', '2021-12-08 21:15:36');
INSERT INTO `order_amount` VALUES (25, '1021120832958977102', 10, 10100, '2021-12-08 22:58:57', '2021-12-08 22:58:57');
INSERT INTO `order_amount` VALUES (26, '1021120832958977102', 20, 500, '2021-12-08 22:58:57', '2021-12-08 22:58:57');
INSERT INTO `order_amount` VALUES (27, '1021120832958977102', 30, 0, '2021-12-08 22:58:57', '2021-12-08 22:58:57');
INSERT INTO `order_amount` VALUES (28, '1021120832958977102', 50, 9600, '2021-12-08 22:58:57', '2021-12-08 22:58:57');
INSERT INTO `order_amount` VALUES (29, '1021120932958737103', 10, 10100, '2021-12-09 20:11:17', '2021-12-09 20:11:17');
INSERT INTO `order_amount` VALUES (30, '1021120932958737103', 20, 500, '2021-12-09 20:11:17', '2021-12-09 20:11:17');
INSERT INTO `order_amount` VALUES (31, '1021120932958737103', 30, 0, '2021-12-09 20:11:17', '2021-12-09 20:11:17');
INSERT INTO `order_amount` VALUES (32, '1021120932958737103', 50, 9600, '2021-12-09 20:11:17', '2021-12-09 20:11:17');
INSERT INTO `order_amount` VALUES (33, '1021121032958993104', 10, 10100, '2021-12-10 11:36:48', '2021-12-10 11:36:48');
INSERT INTO `order_amount` VALUES (34, '1021121032958993104', 20, 500, '2021-12-10 11:36:48', '2021-12-10 11:36:48');
INSERT INTO `order_amount` VALUES (35, '1021121032958993104', 30, 0, '2021-12-10 11:36:48', '2021-12-10 11:36:48');
INSERT INTO `order_amount` VALUES (36, '1021121032958993104', 50, 9600, '2021-12-10 11:36:48', '2021-12-10 11:36:48');
INSERT INTO `order_amount` VALUES (37, '1021121032909568105', 10, 10100, '2021-12-10 12:23:58', '2021-12-10 12:23:58');
INSERT INTO `order_amount` VALUES (38, '1021121032909568105', 20, 500, '2021-12-10 12:23:58', '2021-12-10 12:23:58');
INSERT INTO `order_amount` VALUES (39, '1021121032909568105', 30, 0, '2021-12-10 12:23:58', '2021-12-10 12:23:58');
INSERT INTO `order_amount` VALUES (40, '1021121032909568105', 50, 9600, '2021-12-10 12:23:58', '2021-12-10 12:23:58');
INSERT INTO `order_amount` VALUES (41, '1021121232909584100', 10, 10100, '2021-12-12 12:15:05', '2021-12-12 12:15:05');
INSERT INTO `order_amount` VALUES (42, '1021121232909584100', 20, 500, '2021-12-12 12:15:05', '2021-12-12 12:15:05');
INSERT INTO `order_amount` VALUES (43, '1021121232909584100', 30, 0, '2021-12-12 12:15:05', '2021-12-12 12:15:05');
INSERT INTO `order_amount` VALUES (44, '1021121232909584100', 50, 9600, '2021-12-12 12:15:05', '2021-12-12 12:15:05');
COMMIT;

-- ----------------------------
-- Table structure for order_amount_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_amount_detail`;
CREATE TABLE `order_amount_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` varchar(50) NOT NULL COMMENT '订单编号',
  `product_type` tinyint(4) NOT NULL COMMENT '产品类型',
  `order_item_id` varchar(50) NOT NULL COMMENT '订单明细编号',
  `product_id` varchar(50) NOT NULL COMMENT '商品编号',
  `sku_code` varchar(50) NOT NULL COMMENT 'sku编码',
  `sale_quantity` int(11) NOT NULL COMMENT '销售数量',
  `sale_price` int(11) NOT NULL COMMENT '销售单价',
  `amount_type` tinyint(4) NOT NULL COMMENT '收费类型',
  `amount` int(11) NOT NULL COMMENT '收费金额',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8 COMMENT='订单费用明细表';

-- ----------------------------
-- Records of order_amount_detail
-- ----------------------------
BEGIN;
INSERT INTO `order_amount_detail` VALUES (28, '1021120832956929100', 1, '1021120832956929100_001', '1001010', '10101010', 10, 1000, 10, 10000, '2021-12-08 21:15:36', '2021-12-08 21:15:36');
INSERT INTO `order_amount_detail` VALUES (29, '1021120832956929100', 1, '1021120832956929100_001', '1001010', '10101010', 10, 1000, 20, 496, '2021-12-08 21:15:36', '2021-12-08 21:15:36');
INSERT INTO `order_amount_detail` VALUES (30, '1021120832956929100', 1, '1021120832956929100_001', '1001010', '10101010', 10, 1000, 50, 9504, '2021-12-08 21:15:36', '2021-12-08 21:15:36');
INSERT INTO `order_amount_detail` VALUES (31, '1021120832956929100', 1, '1021120832956929100_002', '1001011', '10101011', 1, 100, 10, 100, '2021-12-08 21:15:36', '2021-12-08 21:15:36');
INSERT INTO `order_amount_detail` VALUES (32, '1021120832956929100', 1, '1021120832956929100_002', '1001011', '10101011', 1, 100, 20, 4, '2021-12-08 21:15:36', '2021-12-08 21:15:36');
INSERT INTO `order_amount_detail` VALUES (33, '1021120832956929100', 1, '1021120832956929100_002', '1001011', '10101011', 1, 100, 50, 96, '2021-12-08 21:15:36', '2021-12-08 21:15:36');
INSERT INTO `order_amount_detail` VALUES (34, '1021120832958977102', 1, '1021120832958977102_001', '1001010', '10101010', 10, 1000, 10, 10000, '2021-12-08 22:58:57', '2021-12-08 22:58:57');
INSERT INTO `order_amount_detail` VALUES (35, '1021120832958977102', 1, '1021120832958977102_001', '1001010', '10101010', 10, 1000, 20, 496, '2021-12-08 22:58:57', '2021-12-08 22:58:57');
INSERT INTO `order_amount_detail` VALUES (36, '1021120832958977102', 1, '1021120832958977102_001', '1001010', '10101010', 10, 1000, 50, 9504, '2021-12-08 22:58:57', '2021-12-08 22:58:57');
INSERT INTO `order_amount_detail` VALUES (37, '1021120832958977102', 1, '1021120832958977102_002', '1001011', '10101011', 1, 100, 10, 100, '2021-12-08 22:58:57', '2021-12-08 22:58:57');
INSERT INTO `order_amount_detail` VALUES (38, '1021120832958977102', 1, '1021120832958977102_002', '1001011', '10101011', 1, 100, 20, 4, '2021-12-08 22:58:57', '2021-12-08 22:58:57');
INSERT INTO `order_amount_detail` VALUES (39, '1021120832958977102', 1, '1021120832958977102_002', '1001011', '10101011', 1, 100, 50, 96, '2021-12-08 22:58:57', '2021-12-08 22:58:57');
INSERT INTO `order_amount_detail` VALUES (40, '1021120932958737103', 1, '1021120932958737103_001', '1001010', '10101010', 10, 1000, 10, 10000, '2021-12-09 20:11:17', '2021-12-09 20:11:17');
INSERT INTO `order_amount_detail` VALUES (41, '1021120932958737103', 1, '1021120932958737103_001', '1001010', '10101010', 10, 1000, 20, 496, '2021-12-09 20:11:17', '2021-12-09 20:11:17');
INSERT INTO `order_amount_detail` VALUES (42, '1021120932958737103', 1, '1021120932958737103_001', '1001010', '10101010', 10, 1000, 50, 9504, '2021-12-09 20:11:17', '2021-12-09 20:11:17');
INSERT INTO `order_amount_detail` VALUES (43, '1021120932958737103', 1, '1021120932958737103_002', '1001011', '10101011', 1, 100, 10, 100, '2021-12-09 20:11:17', '2021-12-09 20:11:17');
INSERT INTO `order_amount_detail` VALUES (44, '1021120932958737103', 1, '1021120932958737103_002', '1001011', '10101011', 1, 100, 20, 4, '2021-12-09 20:11:17', '2021-12-09 20:11:17');
INSERT INTO `order_amount_detail` VALUES (45, '1021120932958737103', 1, '1021120932958737103_002', '1001011', '10101011', 1, 100, 50, 96, '2021-12-09 20:11:17', '2021-12-09 20:11:17');
INSERT INTO `order_amount_detail` VALUES (46, '1021121032958993104', 1, '1021121032958993104_001', '1001010', '10101010', 10, 1000, 10, 10000, '2021-12-10 11:36:48', '2021-12-10 11:36:48');
INSERT INTO `order_amount_detail` VALUES (47, '1021121032958993104', 1, '1021121032958993104_001', '1001010', '10101010', 10, 1000, 20, 496, '2021-12-10 11:36:48', '2021-12-10 11:36:48');
INSERT INTO `order_amount_detail` VALUES (48, '1021121032958993104', 1, '1021121032958993104_001', '1001010', '10101010', 10, 1000, 50, 9504, '2021-12-10 11:36:48', '2021-12-10 11:36:48');
INSERT INTO `order_amount_detail` VALUES (49, '1021121032958993104', 1, '1021121032958993104_002', '1001011', '10101011', 1, 100, 10, 100, '2021-12-10 11:36:48', '2021-12-10 11:36:48');
INSERT INTO `order_amount_detail` VALUES (50, '1021121032958993104', 1, '1021121032958993104_002', '1001011', '10101011', 1, 100, 20, 4, '2021-12-10 11:36:48', '2021-12-10 11:36:48');
INSERT INTO `order_amount_detail` VALUES (51, '1021121032958993104', 1, '1021121032958993104_002', '1001011', '10101011', 1, 100, 50, 96, '2021-12-10 11:36:48', '2021-12-10 11:36:48');
INSERT INTO `order_amount_detail` VALUES (52, '1021121032909568105', 1, '1021121032909568105_001', '1001010', '10101010', 10, 1000, 10, 10000, '2021-12-10 12:23:58', '2021-12-10 12:23:58');
INSERT INTO `order_amount_detail` VALUES (53, '1021121032909568105', 1, '1021121032909568105_001', '1001010', '10101010', 10, 1000, 20, 496, '2021-12-10 12:23:58', '2021-12-10 12:23:58');
INSERT INTO `order_amount_detail` VALUES (54, '1021121032909568105', 1, '1021121032909568105_001', '1001010', '10101010', 10, 1000, 50, 9504, '2021-12-10 12:23:58', '2021-12-10 12:23:58');
INSERT INTO `order_amount_detail` VALUES (55, '1021121032909568105', 1, '1021121032909568105_002', '1001011', '10101011', 1, 100, 10, 100, '2021-12-10 12:23:58', '2021-12-10 12:23:58');
INSERT INTO `order_amount_detail` VALUES (56, '1021121032909568105', 1, '1021121032909568105_002', '1001011', '10101011', 1, 100, 20, 4, '2021-12-10 12:23:58', '2021-12-10 12:23:58');
INSERT INTO `order_amount_detail` VALUES (57, '1021121032909568105', 1, '1021121032909568105_002', '1001011', '10101011', 1, 100, 50, 96, '2021-12-10 12:23:58', '2021-12-10 12:23:58');
INSERT INTO `order_amount_detail` VALUES (58, '1021121232909584100', 1, '1021121232909584100_001', '1001010', '10101010', 10, 1000, 10, 10000, '2021-12-12 12:15:05', '2021-12-12 12:15:05');
INSERT INTO `order_amount_detail` VALUES (59, '1021121232909584100', 1, '1021121232909584100_001', '1001010', '10101010', 10, 1000, 20, 496, '2021-12-12 12:15:05', '2021-12-12 12:15:05');
INSERT INTO `order_amount_detail` VALUES (60, '1021121232909584100', 1, '1021121232909584100_001', '1001010', '10101010', 10, 1000, 50, 9504, '2021-12-12 12:15:05', '2021-12-12 12:15:05');
INSERT INTO `order_amount_detail` VALUES (61, '1021121232909584100', 1, '1021121232909584100_002', '1001011', '10101011', 1, 100, 10, 100, '2021-12-12 12:15:05', '2021-12-12 12:15:05');
INSERT INTO `order_amount_detail` VALUES (62, '1021121232909584100', 1, '1021121232909584100_002', '1001011', '10101011', 1, 100, 20, 4, '2021-12-12 12:15:05', '2021-12-12 12:15:05');
INSERT INTO `order_amount_detail` VALUES (63, '1021121232909584100', 1, '1021121232909584100_002', '1001011', '10101011', 1, 100, 50, 96, '2021-12-12 12:15:05', '2021-12-12 12:15:05');
COMMIT;

-- ----------------------------
-- Table structure for order_auto_no
-- ----------------------------
DROP TABLE IF EXISTS `order_auto_no`;
CREATE TABLE `order_auto_no` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COMMENT='订单编号表';

-- ----------------------------
-- Records of order_auto_no
-- ----------------------------
BEGIN;
INSERT INTO `order_auto_no` VALUES (25, '2021-12-08 10:33:51', '2021-12-08 10:33:51');
INSERT INTO `order_auto_no` VALUES (26, '2021-12-08 21:11:46', '2021-12-08 21:11:46');
INSERT INTO `order_auto_no` VALUES (27, '2021-12-08 22:54:23', '2021-12-08 22:54:23');
INSERT INTO `order_auto_no` VALUES (28, '2021-12-09 11:20:33', '2021-12-09 11:20:33');
INSERT INTO `order_auto_no` VALUES (29, '2021-12-09 11:21:07', '2021-12-09 11:21:07');
INSERT INTO `order_auto_no` VALUES (30, '2021-12-09 20:41:17', '2021-12-09 20:41:17');
INSERT INTO `order_auto_no` VALUES (31, '2021-12-10 11:35:45', '2021-12-10 11:35:45');
INSERT INTO `order_auto_no` VALUES (32, '2021-12-10 12:06:49', '2021-12-10 12:06:49');
INSERT INTO `order_auto_no` VALUES (33, '2021-12-10 12:23:00', '2021-12-10 12:23:00');
INSERT INTO `order_auto_no` VALUES (34, '2021-12-10 12:53:59', '2021-12-10 12:53:59');
INSERT INTO `order_auto_no` VALUES (35, '2021-12-11 02:14:13', '2021-12-11 02:14:13');
INSERT INTO `order_auto_no` VALUES (36, '2021-12-11 17:36:49', '2021-12-11 17:36:49');
INSERT INTO `order_auto_no` VALUES (37, '2021-12-12 11:38:01', '2021-12-12 11:38:01');
INSERT INTO `order_auto_no` VALUES (38, '2021-12-12 12:35:10', '2021-12-12 12:35:10');
COMMIT;

-- ----------------------------
-- Table structure for order_delivery_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_delivery_detail`;
CREATE TABLE `order_delivery_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` varchar(50) NOT NULL COMMENT '订单编号',
  `delivery_type` tinyint(4) DEFAULT NULL COMMENT '配送类型',
  `province` varchar(50) DEFAULT NULL COMMENT '省',
  `city` varchar(50) DEFAULT NULL COMMENT '市',
  `area` varchar(50) DEFAULT NULL COMMENT '区',
  `street` varchar(50) DEFAULT NULL COMMENT '街道',
  `detail_address` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `lon` decimal(20,10) DEFAULT NULL COMMENT '经度',
  `lat` decimal(20,10) DEFAULT NULL COMMENT '维度',
  `receiver_name` varchar(50) DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(50) DEFAULT NULL COMMENT '收货人电话',
  `modify_address_count` tinyint(4) NOT NULL DEFAULT '0' COMMENT '调整地址次数',
  `deliverer_no` varchar(50) DEFAULT NULL COMMENT '配送员编号',
  `deliverer_name` varchar(50) DEFAULT NULL COMMENT '配送员姓名',
  `deliverer_phone` varchar(50) DEFAULT NULL COMMENT '配送员手机号',
  `out_stock_time` datetime DEFAULT NULL COMMENT '出库时间',
  `signed_time` datetime DEFAULT NULL COMMENT '签收时间',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='订单配送信息表';

-- ----------------------------
-- Records of order_delivery_detail
-- ----------------------------
BEGIN;
INSERT INTO `order_delivery_detail` VALUES (6, '1021120832956929100', 1, '110000', '110100', '110105', '110101007', '北京北京市东城区朝阳门街道北京路10号', 100.1000000000, 1010.2010100000, '张三', '13434545545', 0, NULL, NULL, NULL, NULL, NULL, '2021-12-08 21:15:36', '2021-12-08 21:15:36');
INSERT INTO `order_delivery_detail` VALUES (7, '1021120832958977102', 1, '110000', '110100', '110105', '110101007', '北京北京市东城区朝阳门街道北京路10号', 100.1000000000, 1010.2010100000, '张三', '13434545545', 0, NULL, NULL, NULL, NULL, NULL, '2021-12-08 22:58:57', '2021-12-08 22:58:57');
INSERT INTO `order_delivery_detail` VALUES (8, '1021120932958737103', 1, '110000', '110100', '110105', '110101007', '北京北京市东城区朝阳门街道北京路10号', 100.1000000000, 1010.2010100000, '张三', '13434545545', 0, NULL, NULL, NULL, NULL, NULL, '2021-12-09 20:11:16', '2021-12-09 20:11:16');
INSERT INTO `order_delivery_detail` VALUES (9, '1021121032958993104', 1, '110000', '110100', '110105', '110101007', '北京北京市东城区朝阳门街道北京路10号', 100.1000000000, 1010.2010100000, '张三', '13434545545', 0, '1032', '张三', '13432434456', '2021-12-06 00:00:00', '2021-12-06 10:00:00', '2021-12-10 11:36:47', '2021-12-10 11:36:47');
INSERT INTO `order_delivery_detail` VALUES (10, '1021121032909568105', 1, '310000', '310100', '310101', '310101002', '清海路101号', 100.1643216523, 1010.2010100000, '张三', '13434545545', 1, NULL, NULL, NULL, '2021-12-06 00:00:00', NULL, '2021-12-10 12:23:58', '2021-12-10 12:23:58');
INSERT INTO `order_delivery_detail` VALUES (11, '1021121232909584100', 1, '110000', '110100', '110105', '110101007', '北京北京市东城区朝阳门街道北京路10号', 100.1000000000, 1010.2010100000, '张三', '13434545545', 0, NULL, NULL, NULL, NULL, NULL, '2021-12-12 12:15:04', '2021-12-12 12:15:04');
COMMIT;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `business_identifier` tinyint(4) NOT NULL COMMENT '接入方业务线标识  1, "自营商城"',
  `order_id` varchar(50) NOT NULL DEFAULT '' COMMENT '订单编号',
  `parent_order_id` varchar(50) DEFAULT NULL COMMENT '父订单编号',
  `business_order_id` varchar(50) DEFAULT NULL COMMENT '接入方订单号',
  `order_type` tinyint(4) NOT NULL COMMENT '订单类型 1:一般订单  255:其它',
  `order_status` tinyint(4) NOT NULL COMMENT '订单状态 10:已创建, 30:已履约, 40:出库, 50:配送中, 60:已签收, 70:已取消, 100:已拒收, 255:无效订单',
  `cancel_type` varchar(255) DEFAULT NULL COMMENT '订单取消类型',
  `cancel_time` datetime DEFAULT NULL COMMENT '订单取消时间',
  `seller_id` varchar(50) DEFAULT NULL COMMENT '卖家编号',
  `user_id` varchar(50) DEFAULT NULL COMMENT '买家编号',
  `total_amount` int(11) DEFAULT NULL COMMENT '交易总金额（以分为单位存储）',
  `pay_amount` int(11) DEFAULT NULL COMMENT '交易支付金额',
  `pay_type` tinyint(4) DEFAULT NULL COMMENT '支付方式 10:微信支付 20:支付宝支付',
  `coupon_id` varchar(50) DEFAULT NULL COMMENT '使用的优惠券编号',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `expire_time` datetime NOT NULL COMMENT '支付订单截止时间',
  `user_remark` varchar(255) DEFAULT NULL COMMENT '用户备注',
  `delete_status` tinyint(4) NOT NULL COMMENT '订单删除状态 0:未删除  1:已删除',
  `comment_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '订单评论状态 0:未发表评论  1:已发表评论',
  `ext_json` varchar(1024) DEFAULT NULL COMMENT '扩展信息',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_order_id` (`order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='订单表';

-- ----------------------------
-- Records of order_info
-- ----------------------------
BEGIN;
INSERT INTO `order_info` VALUES (7, 1, '1021120832956929100', NULL, NULL, 1, 70, '1', '2021-12-08 21:45:37', '101', '101', 10100, 9600, 10, '1001002', NULL, '2021-12-08 21:45:35', 'test reamark', 0, 0, NULL, '2021-12-08 21:15:35', '2021-12-08 21:45:37');
INSERT INTO `order_info` VALUES (8, 1, '1021120832958977102', NULL, NULL, 1, 70, '1', '2021-12-08 23:28:58', '101', '102', 10100, 9600, 10, '1001003', NULL, '2021-12-08 23:28:56', 'test reamark', 0, 0, NULL, '2021-12-08 22:58:56', '2021-12-08 23:28:58');
INSERT INTO `order_info` VALUES (9, 1, '1021120932958737103', NULL, NULL, 1, 70, '1', '2021-12-09 20:41:17', '101', '103', 10100, 9600, 10, '1001004', '2021-12-09 20:13:47', '2021-12-09 20:41:16', 'test reamark', 0, 0, NULL, '2021-12-09 20:11:16', '2021-12-09 20:41:17');
INSERT INTO `order_info` VALUES (10, 1, '1021121032958993104', NULL, NULL, 1, 60, '', NULL, '101', '104', 10100, 9600, 10, '1001005', '2021-12-10 11:38:13', '2021-12-10 12:06:47', 'test reamark', 1, 0, NULL, '2021-12-10 11:36:47', '2021-12-10 12:06:48');
INSERT INTO `order_info` VALUES (11, 1, '1021121032909568105', NULL, NULL, 1, 70, '1', '2021-12-10 12:53:59', '101', '105', 10100, 9600, 10, '1001006', '2021-12-10 12:24:34', '2021-12-10 12:53:57', 'test reamark', 0, 0, NULL, '2021-12-10 12:23:57', '2021-12-10 12:53:59');
INSERT INTO `order_info` VALUES (12, 1, '1021121232909584100', NULL, NULL, 1, 10, NULL, NULL, '101', '100', 10100, 9600, 10, '1001001', NULL, '2021-12-12 12:45:03', 'test reamark', 0, 0, NULL, '2021-12-12 12:15:04', '2021-12-12 12:15:04');
COMMIT;

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` varchar(50) NOT NULL COMMENT '订单编号',
  `order_item_id` varchar(50) NOT NULL COMMENT '订单明细编号',
  `product_type` tinyint(4) NOT NULL COMMENT '商品类型 1:普通商品,2:预售商品',
  `product_id` varchar(50) NOT NULL COMMENT '商品编号',
  `product_img` varchar(255) DEFAULT NULL COMMENT '商品图片',
  `product_name` varchar(50) NOT NULL COMMENT '商品名称',
  `sku_code` varchar(50) NOT NULL COMMENT 'sku编码',
  `sale_quantity` int(11) NOT NULL COMMENT '销售数量',
  `sale_price` int(11) NOT NULL COMMENT '销售单价',
  `origin_amount` int(11) NOT NULL COMMENT '当前商品支付原总价',
  `pay_amount` int(11) NOT NULL COMMENT '交易支付金额',
  `product_unit` varchar(10) NOT NULL COMMENT '商品单位',
  `purchase_price` int(11) DEFAULT NULL COMMENT '采购成本价',
  `seller_id` varchar(50) DEFAULT NULL COMMENT '卖家编号',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='订单条目表';

-- ----------------------------
-- Records of order_item
-- ----------------------------
BEGIN;
INSERT INTO `order_item` VALUES (10, '1021120832956929100', '1021120832956929100_001', 1, '1001010', 'test.img', '测试商品', '10101010', 10, 1000, 10000, 9504, '个', 500, '101', '2021-12-08 21:15:36', '2021-12-08 21:15:36');
INSERT INTO `order_item` VALUES (11, '1021120832956929100', '1021120832956929100_002', 1, '1001011', 'demo.img', 'demo商品', '10101011', 1, 100, 100, 96, '瓶', 50, '101', '2021-12-08 21:15:36', '2021-12-08 21:15:36');
INSERT INTO `order_item` VALUES (12, '1021120832958977102', '1021120832958977102_001', 1, '1001010', 'test.img', '测试商品', '10101010', 10, 1000, 10000, 9504, '个', 500, '101', '2021-12-08 22:58:57', '2021-12-08 22:58:57');
INSERT INTO `order_item` VALUES (13, '1021120832958977102', '1021120832958977102_002', 1, '1001011', 'demo.img', 'demo商品', '10101011', 1, 100, 100, 96, '瓶', 50, '101', '2021-12-08 22:58:57', '2021-12-08 22:58:57');
INSERT INTO `order_item` VALUES (14, '1021120932958737103', '1021120932958737103_001', 1, '1001010', 'test.img', '测试商品', '10101010', 10, 1000, 10000, 9504, '个', 500, '101', '2021-12-09 20:11:16', '2021-12-09 20:11:16');
INSERT INTO `order_item` VALUES (15, '1021120932958737103', '1021120932958737103_002', 1, '1001011', 'demo.img', 'demo商品', '10101011', 1, 100, 100, 96, '瓶', 50, '101', '2021-12-09 20:11:16', '2021-12-09 20:11:16');
INSERT INTO `order_item` VALUES (16, '1021121032958993104', '1021121032958993104_001', 1, '1001010', 'test.img', '测试商品', '10101010', 10, 1000, 10000, 9504, '个', 500, '101', '2021-12-10 11:36:47', '2021-12-10 11:36:47');
INSERT INTO `order_item` VALUES (17, '1021121032958993104', '1021121032958993104_002', 1, '1001011', 'demo.img', 'demo商品', '10101011', 1, 100, 100, 96, '瓶', 50, '101', '2021-12-10 11:36:47', '2021-12-10 11:36:47');
INSERT INTO `order_item` VALUES (18, '1021121032909568105', '1021121032909568105_001', 1, '1001010', 'test.img', '测试商品', '10101010', 10, 1000, 10000, 9504, '个', 500, '101', '2021-12-10 12:23:58', '2021-12-10 12:23:58');
INSERT INTO `order_item` VALUES (19, '1021121032909568105', '1021121032909568105_002', 1, '1001011', 'demo.img', 'demo商品', '10101011', 1, 100, 100, 96, '瓶', 50, '101', '2021-12-10 12:23:58', '2021-12-10 12:23:58');
INSERT INTO `order_item` VALUES (20, '1021121232909584100', '1021121232909584100_001', 1, '1001010', 'test.img', '测试商品', '10101010', 10, 1000, 10000, 9504, '个', 500, '101', '2021-12-12 12:15:04', '2021-12-12 12:15:04');
INSERT INTO `order_item` VALUES (21, '1021121232909584100', '1021121232909584100_002', 1, '1001011', 'demo.img', 'demo商品', '10101011', 1, 100, 100, 96, '瓶', 50, '101', '2021-12-12 12:15:04', '2021-12-12 12:15:04');
COMMIT;

-- ----------------------------
-- Table structure for order_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `order_operate_log`;
CREATE TABLE `order_operate_log` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `order_id` varchar(50) NOT NULL COMMENT '订单编号',
  `operate_type` tinyint(4) DEFAULT NULL COMMENT '操作类型',
  `pre_status` tinyint(4) DEFAULT NULL COMMENT '前置状态',
  `current_status` tinyint(4) DEFAULT NULL COMMENT '当前状态',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注说明',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单操作日志表';

-- ----------------------------
-- Records of order_operate_log
-- ----------------------------
BEGIN;
INSERT INTO `order_operate_log` VALUES (1468569955112665089, '1021120832956929100', 10, 0, 10, '创建订单操作0-10', '2021-12-08 21:15:37', '2021-12-08 21:15:37');
INSERT INTO `order_operate_log` VALUES (1468577507422584833, '1021120832956929100', 30, 10, 70, '超时未支付自动取消订单10-70', '2021-12-08 21:45:37', '2021-12-08 21:45:37');
INSERT INTO `order_operate_log` VALUES (1468595962142081025, '1021120832958977102', 10, 0, 10, '创建订单操作0-10', '2021-12-08 22:58:57', '2021-12-08 22:58:57');
INSERT INTO `order_operate_log` VALUES (1468603513567002625, '1021120832958977102', 30, 10, 70, '超时未支付自动取消订单10-70', '2021-12-08 23:28:58', '2021-12-08 23:28:58');
INSERT INTO `order_operate_log` VALUES (1468916153719799809, '1021120932958737103', 10, 0, 10, '创建订单操作0-10', '2021-12-09 20:11:17', '2021-12-09 20:11:17');
INSERT INTO `order_operate_log` VALUES (1468917500510814209, '1021120932958737103', 40, 10, 20, '订单支付回调操作10-20', '2021-12-09 20:16:38', '2021-12-09 20:16:38');
INSERT INTO `order_operate_log` VALUES (1468917501379035138, '1021120932958737103', 50, 20, 30, '推送订单至履约', '2021-12-09 20:16:38', '2021-12-09 20:16:38');
INSERT INTO `order_operate_log` VALUES (1468923704830148610, '1021120932958737103', 30, 30, 70, '超时未支付自动取消订单30-70', '2021-12-09 20:41:17', '2021-12-09 20:41:17');
INSERT INTO `order_operate_log` VALUES (1469149068642828289, '1021121032958993104', 10, 0, 10, '创建订单操作0-10', '2021-12-10 11:36:48', '2021-12-10 11:36:48');
INSERT INTO `order_operate_log` VALUES (1469149587037831170, '1021121032958993104', 40, 10, 20, '订单支付回调操作10-20', '2021-12-10 11:38:52', '2021-12-10 11:38:52');
INSERT INTO `order_operate_log` VALUES (1469149587620839425, '1021121032958993104', 50, 20, 30, '推送订单至履约', '2021-12-10 11:38:52', '2021-12-10 11:38:52');
INSERT INTO `order_operate_log` VALUES (1469150520090116097, '1021121032958993104', 60, 30, 40, '订单已出库', '2021-12-10 11:42:34', '2021-12-10 11:42:34');
INSERT INTO `order_operate_log` VALUES (1469150842183303169, '1021121032958993104', 70, 40, 50, '订单已配送', '2021-12-10 11:43:51', '2021-12-10 11:43:51');
INSERT INTO `order_operate_log` VALUES (1469151024241262594, '1021121032958993104', 80, 50, 60, '订单已签收', '2021-12-10 11:44:34', '2021-12-10 11:44:34');
INSERT INTO `order_operate_log` VALUES (1469156619912560642, '1021121032958993104', 30, 60, 70, '超时未支付自动取消订单60-70', '2021-12-10 12:06:48', '2021-12-10 12:06:48');
INSERT INTO `order_operate_log` VALUES (1469160939303288833, '1021121032909568105', 10, 0, 10, '创建订单操作0-10', '2021-12-10 12:23:58', '2021-12-10 12:23:58');
INSERT INTO `order_operate_log` VALUES (1469161204903395329, '1021121032909568105', 40, 10, 20, '订单支付回调操作10-20', '2021-12-10 12:25:02', '2021-12-10 12:25:02');
INSERT INTO `order_operate_log` VALUES (1469161205549318145, '1021121032909568105', 50, 20, 30, '推送订单至履约', '2021-12-10 12:25:02', '2021-12-10 12:25:02');
INSERT INTO `order_operate_log` VALUES (1469161573750489089, '1021121032909568105', 60, 30, 40, '订单已出库', '2021-12-10 12:26:30', '2021-12-10 12:26:30');
INSERT INTO `order_operate_log` VALUES (1469168490472357890, '1021121032909568105', 30, 40, 70, '超时未支付自动取消订单40-70', '2021-12-10 12:53:59', '2021-12-10 12:53:59');
INSERT INTO `order_operate_log` VALUES (1469883478325456898, '1021121232909584100', 10, 0, 10, '创建订单操作0-10', '2021-12-12 12:15:05', '2021-12-12 12:15:05');
COMMIT;

-- ----------------------------
-- Table structure for order_payment_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_payment_detail`;
CREATE TABLE `order_payment_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` varchar(50) NOT NULL COMMENT '订单编号',
  `account_type` tinyint(4) NOT NULL COMMENT '账户类型',
  `pay_type` tinyint(4) NOT NULL COMMENT '支付类型  10:微信支付, 20:支付宝支付',
  `pay_status` tinyint(4) NOT NULL COMMENT '支付状态 10:未支付,20:已支付',
  `pay_amount` int(11) NOT NULL DEFAULT '0' COMMENT '支付金额',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `out_trade_no` varchar(50) DEFAULT NULL COMMENT '支付系统交易流水号',
  `pay_remark` varchar(255) DEFAULT NULL COMMENT '支付备注信息',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1469883475938897923 DEFAULT CHARSET=utf8 COMMENT='订单支付明细表';

-- ----------------------------
-- Records of order_payment_detail
-- ----------------------------
BEGIN;
INSERT INTO `order_payment_detail` VALUES (1468569952344424450, '1021120832956929100', 1, 10, 10, 9600, NULL, NULL, NULL, '2021-12-08 21:15:36', '2021-12-08 21:15:36');
INSERT INTO `order_payment_detail` VALUES (1468595960296587265, '1021120832958977102', 1, 10, 10, 9600, NULL, NULL, NULL, '2021-12-08 22:58:57', '2021-12-08 22:58:57');
INSERT INTO `order_payment_detail` VALUES (1468916151983357953, '1021120932958737103', 1, 10, 20, 9600, '2021-12-09 20:13:47', '4886684122681188166', NULL, '2021-12-09 20:11:16', '2021-12-09 20:16:38');
INSERT INTO `order_payment_detail` VALUES (1469149066621173762, '1021121032958993104', 1, 10, 20, 9600, '2021-12-10 11:38:13', '5604472766815226085', NULL, '2021-12-10 11:36:48', '2021-12-10 11:38:52');
INSERT INTO `order_payment_detail` VALUES (1469160937491349505, '1021121032909568105', 1, 10, 20, 9600, '2021-12-10 12:24:34', '8722848625454525184', NULL, '2021-12-10 12:23:58', '2021-12-10 12:25:02');
INSERT INTO `order_payment_detail` VALUES (1469883475938897922, '1021121232909584100', 1, 10, 10, 9600, NULL, NULL, NULL, '2021-12-12 12:15:04', '2021-12-12 12:15:04');
COMMIT;

-- ----------------------------
-- Table structure for order_snapshot
-- ----------------------------
DROP TABLE IF EXISTS `order_snapshot`;
CREATE TABLE `order_snapshot` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `snapshot_type` tinyint(4) unsigned NOT NULL COMMENT '快照类型',
  `snapshot_json` varchar(2000) NOT NULL COMMENT '订单快照内容',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COMMENT='订单快照表';

-- ----------------------------
-- Records of order_snapshot
-- ----------------------------
BEGIN;
INSERT INTO `order_snapshot` VALUES (3, '1021120832956929100', 1, '{\"userId\":\"101\",\"couponConfigId\":\"2001001\",\"couponId\":\"1001002\",\"name\":\"测试优惠券\",\"type\":2,\"amount\":500,\"conditionAmount\":1000,\"validStartTime\":\"2021-11-01 12:24:29\",\"validEndTime\":\"2024-06-30 12:24:35\"}', '2021-12-08 21:15:37', '2021-12-08 21:15:37');
INSERT INTO `order_snapshot` VALUES (4, '1021120832956929100', 2, '[{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021120832956929100\",\"amountType\":10,\"amount\":10100},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021120832956929100\",\"amountType\":20,\"amount\":500},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021120832956929100\",\"amountType\":30,\"amount\":0},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021120832956929100\",\"amountType\":50,\"amount\":9600}]', '2021-12-08 21:15:37', '2021-12-08 21:15:37');
INSERT INTO `order_snapshot` VALUES (5, '1021120832956929100', 3, '[{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021120832956929100\",\"orderItemId\":\"1021120832956929100_001\",\"productType\":1,\"productId\":\"1001010\",\"productImg\":\"test.img\",\"productName\":\"测试商品\",\"skuCode\":\"10101010\",\"saleQuantity\":10,\"salePrice\":1000,\"originAmount\":10000,\"payAmount\":9504,\"productUnit\":\"个\",\"purchasePrice\":500,\"sellerId\":\"101\"},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021120832956929100\",\"orderItemId\":\"1021120832956929100_002\",\"productType\":1,\"productId\":\"1001011\",\"productImg\":\"demo.img\",\"productName\":\"demo商品\",\"skuCode\":\"10101011\",\"saleQuantity\":1,\"salePrice\":100,\"originAmount\":100,\"payAmount\":96,\"productUnit\":\"瓶\",\"purchasePrice\":50,\"sellerId\":\"101\"}]', '2021-12-08 21:15:37', '2021-12-08 21:15:37');
INSERT INTO `order_snapshot` VALUES (6, '1021120832958977102', 1, '{\"userId\":\"102\",\"couponConfigId\":\"2001001\",\"couponId\":\"1001003\",\"name\":\"测试优惠券\",\"type\":2,\"amount\":500,\"conditionAmount\":1000,\"validStartTime\":\"2021-11-01 12:24:29\",\"validEndTime\":\"2024-06-30 12:24:35\"}', '2021-12-08 22:58:57', '2021-12-08 22:58:57');
INSERT INTO `order_snapshot` VALUES (7, '1021120832958977102', 2, '[{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021120832958977102\",\"amountType\":10,\"amount\":10100},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021120832958977102\",\"amountType\":20,\"amount\":500},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021120832958977102\",\"amountType\":30,\"amount\":0},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021120832958977102\",\"amountType\":50,\"amount\":9600}]', '2021-12-08 22:58:57', '2021-12-08 22:58:57');
INSERT INTO `order_snapshot` VALUES (8, '1021120832958977102', 3, '[{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021120832958977102\",\"orderItemId\":\"1021120832958977102_001\",\"productType\":1,\"productId\":\"1001010\",\"productImg\":\"test.img\",\"productName\":\"测试商品\",\"skuCode\":\"10101010\",\"saleQuantity\":10,\"salePrice\":1000,\"originAmount\":10000,\"payAmount\":9504,\"productUnit\":\"个\",\"purchasePrice\":500,\"sellerId\":\"101\"},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021120832958977102\",\"orderItemId\":\"1021120832958977102_002\",\"productType\":1,\"productId\":\"1001011\",\"productImg\":\"demo.img\",\"productName\":\"demo商品\",\"skuCode\":\"10101011\",\"saleQuantity\":1,\"salePrice\":100,\"originAmount\":100,\"payAmount\":96,\"productUnit\":\"瓶\",\"purchasePrice\":50,\"sellerId\":\"101\"}]', '2021-12-08 22:58:57', '2021-12-08 22:58:57');
INSERT INTO `order_snapshot` VALUES (9, '1021120932958737103', 1, '{\"userId\":\"103\",\"couponConfigId\":\"2001001\",\"couponId\":\"1001004\",\"name\":\"测试优惠券\",\"type\":2,\"amount\":500,\"conditionAmount\":1000,\"validStartTime\":\"2021-11-01 12:24:29\",\"validEndTime\":\"2024-06-30 12:24:35\"}', '2021-12-09 20:11:17', '2021-12-09 20:11:17');
INSERT INTO `order_snapshot` VALUES (10, '1021120932958737103', 2, '[{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021120932958737103\",\"amountType\":10,\"amount\":10100},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021120932958737103\",\"amountType\":20,\"amount\":500},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021120932958737103\",\"amountType\":30,\"amount\":0},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021120932958737103\",\"amountType\":50,\"amount\":9600}]', '2021-12-09 20:11:17', '2021-12-09 20:11:17');
INSERT INTO `order_snapshot` VALUES (11, '1021120932958737103', 3, '[{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021120932958737103\",\"orderItemId\":\"1021120932958737103_001\",\"productType\":1,\"productId\":\"1001010\",\"productImg\":\"test.img\",\"productName\":\"测试商品\",\"skuCode\":\"10101010\",\"saleQuantity\":10,\"salePrice\":1000,\"originAmount\":10000,\"payAmount\":9504,\"productUnit\":\"个\",\"purchasePrice\":500,\"sellerId\":\"101\"},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021120932958737103\",\"orderItemId\":\"1021120932958737103_002\",\"productType\":1,\"productId\":\"1001011\",\"productImg\":\"demo.img\",\"productName\":\"demo商品\",\"skuCode\":\"10101011\",\"saleQuantity\":1,\"salePrice\":100,\"originAmount\":100,\"payAmount\":96,\"productUnit\":\"瓶\",\"purchasePrice\":50,\"sellerId\":\"101\"}]', '2021-12-09 20:11:17', '2021-12-09 20:11:17');
INSERT INTO `order_snapshot` VALUES (12, '1021121032958993104', 1, '{\"userId\":\"104\",\"couponConfigId\":\"2001001\",\"couponId\":\"1001005\",\"name\":\"测试优惠券\",\"type\":2,\"amount\":500,\"conditionAmount\":1000,\"validStartTime\":\"2021-11-01 12:24:29\",\"validEndTime\":\"2024-06-30 12:24:35\"}', '2021-12-10 11:36:48', '2021-12-10 11:36:48');
INSERT INTO `order_snapshot` VALUES (13, '1021121032958993104', 2, '[{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021121032958993104\",\"amountType\":10,\"amount\":10100},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021121032958993104\",\"amountType\":20,\"amount\":500},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021121032958993104\",\"amountType\":30,\"amount\":0},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021121032958993104\",\"amountType\":50,\"amount\":9600}]', '2021-12-10 11:36:48', '2021-12-10 11:36:48');
INSERT INTO `order_snapshot` VALUES (14, '1021121032958993104', 3, '[{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021121032958993104\",\"orderItemId\":\"1021121032958993104_001\",\"productType\":1,\"productId\":\"1001010\",\"productImg\":\"test.img\",\"productName\":\"测试商品\",\"skuCode\":\"10101010\",\"saleQuantity\":10,\"salePrice\":1000,\"originAmount\":10000,\"payAmount\":9504,\"productUnit\":\"个\",\"purchasePrice\":500,\"sellerId\":\"101\"},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021121032958993104\",\"orderItemId\":\"1021121032958993104_002\",\"productType\":1,\"productId\":\"1001011\",\"productImg\":\"demo.img\",\"productName\":\"demo商品\",\"skuCode\":\"10101011\",\"saleQuantity\":1,\"salePrice\":100,\"originAmount\":100,\"payAmount\":96,\"productUnit\":\"瓶\",\"purchasePrice\":50,\"sellerId\":\"101\"}]', '2021-12-10 11:36:48', '2021-12-10 11:36:48');
INSERT INTO `order_snapshot` VALUES (15, '1021121032909568105', 1, '{\"userId\":\"105\",\"couponConfigId\":\"2001001\",\"couponId\":\"1001006\",\"name\":\"测试优惠券\",\"type\":2,\"amount\":500,\"conditionAmount\":1000,\"validStartTime\":\"2021-11-01 12:24:29\",\"validEndTime\":\"2024-06-30 12:24:35\"}', '2021-12-10 12:23:58', '2021-12-10 12:23:58');
INSERT INTO `order_snapshot` VALUES (16, '1021121032909568105', 2, '[{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021121032909568105\",\"amountType\":10,\"amount\":10100},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021121032909568105\",\"amountType\":20,\"amount\":500},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021121032909568105\",\"amountType\":30,\"amount\":0},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021121032909568105\",\"amountType\":50,\"amount\":9600}]', '2021-12-10 12:23:58', '2021-12-10 12:23:58');
INSERT INTO `order_snapshot` VALUES (17, '1021121032909568105', 3, '[{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021121032909568105\",\"orderItemId\":\"1021121032909568105_001\",\"productType\":1,\"productId\":\"1001010\",\"productImg\":\"test.img\",\"productName\":\"测试商品\",\"skuCode\":\"10101010\",\"saleQuantity\":10,\"salePrice\":1000,\"originAmount\":10000,\"payAmount\":9504,\"productUnit\":\"个\",\"purchasePrice\":500,\"sellerId\":\"101\"},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021121032909568105\",\"orderItemId\":\"1021121032909568105_002\",\"productType\":1,\"productId\":\"1001011\",\"productImg\":\"demo.img\",\"productName\":\"demo商品\",\"skuCode\":\"10101011\",\"saleQuantity\":1,\"salePrice\":100,\"originAmount\":100,\"payAmount\":96,\"productUnit\":\"瓶\",\"purchasePrice\":50,\"sellerId\":\"101\"}]', '2021-12-10 12:23:58', '2021-12-10 12:23:58');
INSERT INTO `order_snapshot` VALUES (18, '1021121232909584100', 1, '{\"userId\":\"100\",\"couponConfigId\":\"2001001\",\"couponId\":\"1001001\",\"name\":\"测试优惠券\",\"type\":2,\"amount\":500,\"conditionAmount\":1000,\"validStartTime\":\"2021-11-01 12:24:29\",\"validEndTime\":\"2024-06-30 12:24:35\"}', '2021-12-12 12:15:05', '2021-12-12 12:15:05');
INSERT INTO `order_snapshot` VALUES (19, '1021121232909584100', 2, '[{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021121232909584100\",\"amountType\":10,\"amount\":10100},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021121232909584100\",\"amountType\":20,\"amount\":500},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021121232909584100\",\"amountType\":30,\"amount\":0},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021121232909584100\",\"amountType\":50,\"amount\":9600}]', '2021-12-12 12:15:05', '2021-12-12 12:15:05');
INSERT INTO `order_snapshot` VALUES (20, '1021121232909584100', 3, '[{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021121232909584100\",\"orderItemId\":\"1021121232909584100_001\",\"productType\":1,\"productId\":\"1001010\",\"productImg\":\"test.img\",\"productName\":\"测试商品\",\"skuCode\":\"10101010\",\"saleQuantity\":10,\"salePrice\":1000,\"originAmount\":10000,\"payAmount\":9504,\"productUnit\":\"个\",\"purchasePrice\":500,\"sellerId\":\"101\"},{\"id\":null,\"gmtCreate\":null,\"gmtModified\":null,\"orderId\":\"1021121232909584100\",\"orderItemId\":\"1021121232909584100_002\",\"productType\":1,\"productId\":\"1001011\",\"productImg\":\"demo.img\",\"productName\":\"demo商品\",\"skuCode\":\"10101011\",\"saleQuantity\":1,\"salePrice\":100,\"originAmount\":100,\"payAmount\":96,\"productUnit\":\"瓶\",\"purchasePrice\":50,\"sellerId\":\"101\"}]', '2021-12-12 12:15:05', '2021-12-12 12:15:05');
COMMIT;

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
