CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO users (username, password, enabled) VALUES ('admin','96e79218965eb72c92a549dd5a330112',1);

CREATE TABLE `authorities` (
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL,
  UNIQUE KEY `ix_auth_username` (`username`,`authority`),
  CONSTRAINT `fk_authorities_users` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO authorities (username, authority) VALUES ('admin','ROLE_ADMIN');

CREATE TABLE `channel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '频道名称',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态：0 有效 1 无效',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `distributor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '渠道名称',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态：0 有效 1 无效',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `distributor_channel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `d_id` int(11) NOT NULL COMMENT '渠道id',
  `c_id` int(11) NOT NULL COMMENT '频道id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `distributor_label` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `d_id` int(11) NOT NULL COMMENT '渠道id',
  `l_id` int(11) NOT NULL COMMENT '标签id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `gallery` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL COMMENT '图片标题',
  `picture_url` varchar(255) NOT NULL COMMENT '图片链接（封面图）',
  `picture_width` int(11) NOT NULL COMMENT '封面图片的宽度，px',
  `picture_height` int(11) NOT NULL COMMENT '封面图片的高度，px',
  `source_id` int(11) NOT NULL COMMENT '数据源id',
  `picture_count` int(11) NOT NULL COMMENT '图集中图片数量',
  `font_count` int(11) NOT NULL COMMENT '图集点赞数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `label` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '标签名称',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态： 0 有效 1 无效',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `pictures` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `picture_url` varchar(255) NOT NULL COMMENT '图片链接',
  `picture_width` int(11) NOT NULL COMMENT '图片的宽度，px',
  `picture_height` int(11) NOT NULL COMMENT '图片的高度，px',
  `gallery_id` int(11) NOT NULL COMMENT '图集id（标识属于同一图集）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `source` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '数据源名称',
  `url` varchar(255) NOT NULL COMMENT '数据源url',
  `c_id` int(11) DEFAULT NULL COMMENT '频道id',
  `l_id` int(11) DEFAULT NULL COMMENT '标签id',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态： 0 有效 1 无效',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;