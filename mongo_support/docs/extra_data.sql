/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.7
Source Server Version : 50537
Source Host           : 192.168.1.7:3306
Source Database       : oakonggangMongoDB

Target Server Type    : MYSQL
Target Server Version : 50537
File Encoding         : 65001

Date: 2017-01-20 14:43:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for extra_data
-- ----------------------------
DROP TABLE IF EXISTS `extra_data`;
CREATE TABLE `extra_data` (
  `id` varchar(64) NOT NULL,
  `name_code_alias` varchar(255) DEFAULT NULL COMMENT 'name_code的别名，为了避免将真实的name_code暴露在前台造成安全隐患',
  `name_code` varchar(54) DEFAULT NULL COMMENT '表或类的英文名，用于编码中',
  `name` varchar(54) DEFAULT NULL COMMENT '表或类中文名称',
  `extra_name_code` varchar(64) DEFAULT NULL COMMENT '扩展的数据集或数据表编码',
  `extra_name` varchar(64) DEFAULT NULL COMMENT '扩展的中文名称',
  `create_time` datetime DEFAULT NULL,
  `desc` varchar(255) DEFAULT NULL COMMENT '此集合（表）的功能及作用描述',
  `status` int(11) DEFAULT NULL COMMENT '状态 1，开启  0，关闭',
  `ed_type` int(11) DEFAULT NULL COMMENT '1、model扩展（name_code为java的class fullName）  \r\n2、mongo动态扩展(name_code为mongodb的collection name) \r\n',
  `is_independent` int(1) DEFAULT NULL COMMENT '是否主业务表  1、是 0、否\r\n如果是主业务表，则说明extra_*的相关数据则为null，否则则有值，代表其为扩展extra_*的相关数据，在加载时将自动加载。此字段也同时是为了防止一条数据的数据过长，音响到查询效率。',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for extra_data_cols
-- ----------------------------
DROP TABLE IF EXISTS `extra_data_cols`;
CREATE TABLE `extra_data_cols` (
  `id` varchar(64) NOT NULL,
  `col_name` varchar(24) DEFAULT NULL COMMENT '列的名字',
  `col_val_type` varchar(1) DEFAULT NULL COMMENT '列的值的类型\r\nt、时间\r\ns、字符值\r\nd、浮点\r\ni、整数\r\nb、boolean\r\nf、文件\r\nm、model类型',
  `col_code` varchar(64) DEFAULT NULL COMMENT '列的编码，文本框的name，用于编码',
  `is_null` int(11) DEFAULT NULL COMMENT '是否可空',
  `access_role` varchar(64) DEFAULT NULL COMMENT '访问角色，用于角色访问字段的权限控制',
  `extral_data_id` varchar(64) DEFAULT NULL,
  `validate` varchar(255) DEFAULT NULL COMMENT '验证规则',
  `reference_extral_data_id` varchar(64) DEFAULT NULL COMMENT '引用的扩展数据,当列类型为m时，此字段可以根据相对应的id查询其model值',
  `status` int(11) DEFAULT NULL COMMENT '1、启动  0、停用',
  `is_alert` int(1) DEFAULT NULL COMMENT '是否触发消息提醒，当字段发送变更时',
  `message_tpl` varchar(255) DEFAULT NULL COMMENT '触发提醒的通知模版：\r\n如：企业{name}已经在{create_time}完成了{pp_val}的工作，目前后续工作{pp_name}需要您尽快完成。\r\n其中模版的变量为本列所属的表的所有列的任意一个列名。',
  `is_write` int(11) DEFAULT NULL COMMENT '字段值是否可以进行修改  1，可以  0，不可以',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
