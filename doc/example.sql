

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `meeting_id` bigint(20) NOT NULL COMMENT '酒店ID',
  `name` varchar(128) NOT NULL COMMENT '酒店名称',
  `address` varchar(64) NOT NULL COMMENT '酒店地址',
  `add_user_id` bigint(20) NOT NULL COMMENT '提交用户ID',
  `del` int(11) NOT NULL DEFAULT '0' COMMENT '是否已删除[0:未删除、1:已删除]',
  `add_time` datetime NOT NULL COMMENT '创建时间',
  `up_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='会议酒店信息表';


DROP TABLE IF EXISTS `base_meeting_hotel`;
CREATE TABLE `base_meeting_hotel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `meeting_id` bigint(20) NOT NULL COMMENT '酒店ID',
  `name` varchar(128) NOT NULL COMMENT '酒店名称',
  `address` varchar(64) NOT NULL COMMENT '酒店地址',
  `add_user_id` bigint(20) NOT NULL COMMENT '提交用户ID',
  `del` int(11) NOT NULL DEFAULT '0' COMMENT '是否已删除[0:未删除、1:已删除]',
  `add_time` datetime NOT NULL COMMENT '创建时间',
  `up_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='会议酒店信息表';


CREATE TABLE `base_meeting_hotel_room` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `hotel_id` bigint(20) NOT NULL COMMENT '酒店ID',
  `type` TINYINT(1) DEFAULT NULL COMMENT '房间类型, 1标间 2单间 3商务套房',
  `count` int(8) DEFAULT '0' COMMENT '数量',
  `add_user_id` bigint(20) NOT NULL COMMENT '提交用户ID',
  `del` int(11) NOT NULL DEFAULT '0' COMMENT '是否已删除[0:未删除、1:已删除]',
  `add_time` datetime NOT NULL COMMENT '创建时间',
  `up_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='会议酒店房间信息表';

CREATE TABLE `base_meeting_hotel_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `hotel_id` bigint(20) NOT NULL COMMENT '酒店ID',
  `reg_id` bigint(20) NOT NULL COMMENT '注册ID',
  `type` TINYINT(1) DEFAULT NULL COMMENT '房间类型, 1标间 2单间 3商务套房',
  `sub_type` TINYINT(1) DEFAULT NULL COMMENT '房间子类型, 1包间 2拼间',
  `submit_type` TINYINT(1) DEFAULT '0' COMMENT '提交方式 0:本人提交 1:管理人员提交',
  `add_user_id` bigint(20) NOT NULL COMMENT '提交用户ID',
  `del` int(11) NOT NULL DEFAULT '0' COMMENT '是否已删除[0:未删除、1:已删除]',
  `add_time` datetime NOT NULL COMMENT '创建时间',
  `up_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='会议酒店入住信息表';


CREATE TABLE `base_meeting_reg_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `reg_id` bigint(20) NOT NULL COMMENT '注册ID',
   `meeting_id` bigint(20) NOT NULL COMMENT '会议ID',
  `title` TINYINT(1) DEFAULT NULL COMMENT '报告标题',
  `digest` text COLLATE utf8_bin COMMENT '报告摘要',
  `add_user_id` bigint(20) NOT NULL COMMENT '提交用户ID',
  `del` int(11) NOT NULL DEFAULT '0' COMMENT '是否已删除[0:未删除、1:已删除]',
  `add_time` datetime NOT NULL COMMENT '创建时间',
  `up_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='会议报告人录入信息表';


CREATE TABLE `base_meeting_remind_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `meeting_id` bigint(20) NOT NULL COMMENT '会议ID',
  `template` varchar(255) NOT NULL COMMENT '短信息内容',
  `is_link` TINYINT(1) DEFAULT '0' COMMENT '是否包含短链',
  `target_type` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '目标类型,同枚举MeetingRegStatus',
  `type` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '发送类型',
  `add_user_id` bigint(20) NOT NULL COMMENT '提交用户ID',
  `del` int(11) NOT NULL DEFAULT '0' COMMENT '是否已删除[0:未删除、1:已删除]',
  `add_time` datetime NOT NULL COMMENT '创建时间',
  `up_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='会议提醒任务';


CREATE TABLE `base_meeting_sign_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `reg_id` bigint(20) NOT NULL COMMENT '注册ID',
  `meeting_id` bigint(20) NOT NULL COMMENT '会议ID',
  `del` int(11) NOT NULL DEFAULT '0' COMMENT '是否已删除[0:未删除、1:已删除]',
  `add_time` datetime NOT NULL COMMENT '创建时间',
  `up_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='会议签到记录表';