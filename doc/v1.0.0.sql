

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `nick_name` varchar(32) NOT NULL COMMENT '昵称',
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `salt` varchar(32) NOT NULL COMMENT '加密盐',
  `is_disable` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否已禁用[0:未禁用、1:已禁用]',
  `is_super` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否为超级管理员[0:未删除、1:已删除]',
   `is_delete` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否已删除[0:未删除、1:已删除]',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户';


DROP TABLE IF EXISTS `sys_tag`;
CREATE TABLE `sys_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(16) NOT NULL COMMENT '名称',
  `is_delete` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否已删除[0:未删除、1:已删除]',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='标签';


DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `content` varchar(32) NOT NULL COMMENT '用户名',
  `is_disable` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否已禁用[0:未禁用、1:已禁用]',
  `is_visible` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '是否可见[0:不可见、1:可见]',
  `is_delete` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否已删除[0:未删除、1:已删除]',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='博客内容';



DROP TABLE IF EXISTS `blog_category`;
CREATE TABLE `blog_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `name` varchar(32) NOT NULL COMMENT '目录名',
  `is_visible` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '是否可见[0:不可见、1:可见]',
  `is_delete` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否已删除[0:未删除、1:已删除]',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='博客目录';


DROP TABLE IF EXISTS `blog_comment`;
CREATE TABLE `blog_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `comment` varchar(128) NOT NULL COMMENT '评论',
  `is_delete` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否已删除[0:未删除、1:已删除]',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='博客评论';



DROP TABLE IF EXISTS `blog_up_down`;
CREATE TABLE `blog_up_down` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `up_down` TINYINT(1) NOT NULL COMMENT '踩赞[0点踩 1点赞]',
  `is_delete` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否已删除[0:未删除、1:已删除]',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='点踩/点赞表';