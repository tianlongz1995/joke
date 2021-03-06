CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT into users (username,PASSWORD,ENABLEd,create_date) value ('admin','14f795033acefcc1caa03664ba7b9587',1,now());

CREATE TABLE `authorities` (
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL,
  UNIQUE KEY `ix_auth_username` (`username`,`authority`),
  CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT into authorities (username,authority) value ('admin','ROLE_ADMIN');

CREATE TABLE `ad` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `slot_id` int(11) NOT NULL DEFAULT '0' COMMENT '广告位id',
  `pos` int(11) DEFAULT '0' COMMENT '位置 1:列表页中间 2:列表页底部 3:详情页上方 4:详情页中部 5:详情页底部',
  `slide` int(11) DEFAULT '0' COMMENT 'pos=1时，广告插入间隔',
  `d_id` int(11) DEFAULT NULL COMMENT '渠道id',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态 0:下线 1:上线',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `did_index` (`d_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `channel` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '频道名称',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '类型 0:普通 1:专题 2:推荐',
  `status` int(11) DEFAULT '0' COMMENT '状态 0:下线 1:上线',
  `content_type` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '笑话属性，多个用逗号分隔',
  `good` int(11) DEFAULT '0' COMMENT '总点赞数',
  `bad` int(11) DEFAULT '0' COMMENT '总点踩数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


CREATE TABLE `distributor` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '渠道名称',
  `status` int(11) DEFAULT '0' COMMENT '状态 0:下线 1:上线',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `distributor_channel` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `d_id` int(11) NOT NULL COMMENT '渠道ID',
  `c_id` int(11) NOT NULL COMMENT '频道ID',
  `sort` int(11) DEFAULT NULL COMMENT '排序，从小到大',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `d_id` int(11) NOT NULL COMMENT '渠道编号',
  `c_id` int(11) NOT NULL COMMENT '频道编号',
  `type` int(11) NOT NULL DEFAULT '4' COMMENT '反馈问题类型 1:刷新慢；2:不好笑；3:闪退/黑屏；4:其他',
  `content` varchar(1024) CHARACTER SET utf8 DEFAULT NULL COMMENT '反馈内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `joke` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `title` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '标题',
  `content` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '文本内容',
  `img` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '静态图片地址',
  `gif` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '动态图片地址',
  `width` int(11) DEFAULT NULL COMMENT '图片宽带',
  `height` int(11) DEFAULT NULL COMMENT '图片高度',
  `good` int(11) DEFAULT '0' COMMENT '点赞数',
  `bad` int(11) DEFAULT '0' COMMENT '点踩数',
  `type` int(11) NOT NULL COMMENT '类型 0:纯文本 1:图片 2:动图',
  `status` int(11) DEFAULT '0' COMMENT '状态 0:未审核 1:通过 2:不通过',
  `uuid` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '爬虫抓取回来的唯一id，用作推荐、相关算法使用',
  `source_id` int(11) DEFAULT NULL COMMENT '数据源id',
  `verify_user` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '审核人',
  `verify_time` datetime DEFAULT NULL COMMENT '审核时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `source` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '数据源名称',
  `url` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '数据源url',
  `status` int(11) DEFAULT '0' COMMENT '状态 0:下线 1:上线',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `s_st_inx` (`status`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `source_monitor` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `source_id` int(11) DEFAULT NULL COMMENT '数据源id',
  `day` int(11) DEFAULT NULL COMMENT '日期：yyyyMMdd',
  `grab_count` int(11) DEFAULT '0' COMMENT '当天爬虫抓取次数',
  `verify_rate` double DEFAULT '0' COMMENT '当天审核通过率',
  `last_grab_time` datetime DEFAULT NULL COMMENT '当天最近一次抓取时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `source_inx` (`source_id`),
  KEY `day_inx` (`day`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `topic` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `title` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '主题',
  `content` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '内容',
  `img` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '静态图片地址',
  `d_ids` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '渠道id，多个用逗号分隔',
  `status` int(11) DEFAULT '0' COMMENT '状态 0:新建 1:下线 2:上线 3:已发布',
  `publish_time` datetime DEFAULT NULL COMMENT '定时发布时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `topic_joke` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `t_id` int(11) NOT NULL COMMENT '主题id',
  `j_id` int(11) NOT NULL COMMENT '段子id',
  `status` int(11) DEFAULT '0' COMMENT '状态： 0 正常 1删除',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
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

CREATE TABLE `feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `d_id` int(11) NOT NULL COMMENT '渠道编号',
  `c_id` int(11) NOT NULL COMMENT '频道编号',
  `type` int(11) NOT NULL COMMENT '反馈问题类型',
  `content` varchar(1024) CHARACTER SET utf8 DEFAULT NULL COMMENT '反馈内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


-- 列表页下拉刷新日统计表 2016-08-22
CREATE TABLE `stat_drop_total_day` (
  `day` int(11) NOT NULL COMMENT 'int type like 20160101',
  `pv` int(11) NOT NULL DEFAULT '0' COMMENT '列表页下拉刷新总PV',
  `uv` int(11) NOT NULL DEFAULT '0' COMMENT '列表页下拉刷新总UV'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT '列表页下拉刷新日统计表';

-- 列表页下拉刷新日统计明细表 2016-08-22
CREATE TABLE `stat_drop_detail_day` (
  `day` int(11) NOT NULL COMMENT 'int type like 20160101',
  `did` varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '渠道ID',
  `cid` varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '频道/标签ID',
  `pv` int(11) NOT NULL DEFAULT '0' COMMENT '列表页下拉刷新PV',
  `uv` int(11) NOT NULL DEFAULT '0' COMMENT '列表页下拉刷新UV'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT '列表页下拉刷新日统计明细表';


--banner表 2017-01-03
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '标题',
  `img` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '静态图片地址',
  `jid` int(11) DEFAULT NULL COMMENT '段子id',
  `cid` int(11) DEFAULT NULL COMMENT '频道id',
  `adid` int(11) DEFAULT NULL COMMENT '广告位id',
  `sort` int(11) DEFAULT '0' COMMENT '排序值',
  `type` int(11) DEFAULT '0' COMMENT '0 内容， 1 广告',
  `status` int(11) DEFAULT '0' COMMENT '状态 0:下线 1:上线',
  `content` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT 'banner表';

--精选表 2017-01-03
DROP TABLE IF EXISTS `choice`;
CREATE TABLE `choice` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `title` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '标题',
  `img` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `content` text CHARACTER SET utf8 COMMENT '内容',
  `status` int(255) DEFAULT '0' COMMENT '状态 0 下线，1上线',
  `width` int(11) DEFAULT '0' COMMENT '图片宽度',
  `height` int(11) DEFAULT '0' COMMENT '图片高度',
  `good` int(11) DEFAULT '0' COMMENT '点赞数',
  `bad` int(11) DEFAULT '0' COMMENT '踩数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



/** 发布规则 dictionary表 2017_01_11*/

INSERT INTO dictionary ( `code`, `parent_code`, `type`, `value`, `describe`, `seq`, `create_time`, `update_time`) VALUES ( '10041', '0', '10041', '', '纯文发布规则', NULL, now(), now());

INSERT INTO dictionary ( `code`, `parent_code`, `type`, `value`, `describe`, `seq`, `create_time`, `update_time`) VALUES ( '10042', '0', '10042', '', '趣图发布规则', NULL, now(), now());

INSERT INTO dictionary ( `code`, `parent_code`, `type`, `value`, `describe`, `seq`, `create_time`, `update_time`) VALUES ( '10043', '0', '10043', '', '推荐发布规则', NULL, now(), now());




--joke表 2017-01-10
alter table joke add COLUMN comment_number int(11) DEFAULT 0 COMMENT '评论数量';
alter table joke add COLUMN comment varchar(255) DEFAULT NULL COMMENT '神评论内容';
alter table joke add COLUMN avata varchar(255) DEFAULT NULL COMMENT '用户头像URL';
alter table joke add COLUMN nick varchar(64) DEFAULT NULL COMMENT '昵称';



-- ads表 2017-01-19
DROP TABLE IF EXISTS `ads`;
CREATE TABLE `ads` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `did` int(11) NOT NULL COMMENT '渠道id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '更新人',
  `s` int(11) NOT NULL DEFAULT '0' COMMENT '广告插入间隔',
  `lc` int(11) NOT NULL DEFAULT '0' COMMENT '列表页中间',
  `lb` int(11) NOT NULL DEFAULT '0' COMMENT '列表页底部',
  `dt` int(11) NOT NULL DEFAULT '0' COMMENT '详情页上方',
  `dc` int(11) NOT NULL DEFAULT '0' COMMENT '详情页中部',
  `db` int(11) NOT NULL DEFAULT '0' COMMENT '详情页底部',
  `di` int(11) NOT NULL DEFAULT '0' COMMENT '详情页插屏',
  `dr` int(11) NOT NULL DEFAULT '0' COMMENT '详情推荐宫格广告',
  PRIMARY KEY (`id`),
  KEY `did_index` (`did`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of ads
-- ----------------------------
INSERT INTO `ads` VALUES ('1', '2', '2017-01-06 13:50:52', 'admin', '2017-01-16 14:48:59', 'admin', '5', '231136991', '-770750974', '1774397970', '2062249474', '-1149476927', '548972251', '4');
INSERT INTO `ads` VALUES ('2', '13', '2017-01-06 13:56:19', 'admin', '2017-01-06 14:09:12', 'admin', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `ads` VALUES ('3', '14', '2017-01-06 14:06:38', 'admin', '2017-01-06 14:09:10', 'admin', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `ads` VALUES ('4', '15', '2017-01-06 14:06:51', 'admin', '2017-01-06 14:09:07', 'admin', '0', '0', '0', '0', '0', '0', '0', '0');


-- channels表 2017-01-19
DROP TABLE IF EXISTS `channels`;
CREATE TABLE `channels` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '频道名称',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '类型( 1:趣图 2:段子 3:推荐、4：精选)',
  `banner` tinyint(1) DEFAULT '0' COMMENT 'banner横幅状态（0：不显示、1：显示）',
  `status` int(11) DEFAULT '0' COMMENT '状态 0:下线 1:上线、2：删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8 DEFAULT '' COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(255) CHARACTER SET utf8 DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of channels
-- ----------------------------
INSERT INTO `channels` VALUES ('1', '趣图', '1', '1', '1', '2016-07-29 15:09:08', 'admin', null, '');
INSERT INTO `channels` VALUES ('2', '段子', '2', '1', '1', '2016-07-29 15:09:08', 'admin', null, null);
INSERT INTO `channels` VALUES ('3', '推荐', '3', '1', '1', '2016-07-29 15:09:08', 'admin', null, null);
INSERT INTO `channels` VALUES ('4', '精选', '4', '1', '1', '2016-07-29 15:09:08', 'admin', null, null);


-- distributors表 2017-02-19
DROP TABLE IF EXISTS `distributors`;
CREATE TABLE `distributors` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '名称',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态（0:下线、1:上线、2：删除）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(255) CHARACTER SET utf8 DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of distributors
-- ----------------------------
INSERT INTO `distributors` VALUES ('2', '默认频道', '1', '2017-01-05 16:56:32', 'admin', '2017-01-16 14:48:58', 'admin');

alter table choice add COLUMN `comment_number` int(11) DEFAULT '0' COMMENT '评论数量';

-- 修改choice自增初始值
alter table choice AUTO_INCREMENT = 20000000;


alter table joke add COLUMN `create_by` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人';

insert into source(id, name, status, create_time) value(1, '用户录入', 1, now());


-- 新增发布段子用户头像url、发布段子用户昵称字段
alter table joke add COLUMN `release_avata` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT '发布段子用户头像url';
alter table joke add COLUMN `release_nick` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT '发布段子用户昵称';

-- 新增段子内容源
INSERT INTO `source` VALUES ('141', '来福岛', 'http://www.laifudao.com/', '1', '2017-03-13 14:14:09', '2017-03-13 14:14:09');
INSERT INTO `source` VALUES ('144', '快乐麻花', 'http://www.mahua.com', '0', '2017-03-16 13:47:02', '2017-03-16 13:47:02');
INSERT INTO `source` VALUES ('145', '牛逼思维', 'http://nbsw.cc', '0', '2017-03-16 13:47:38', '2017-03-16 13:47:38');
INSERT INTO `source` VALUES ('146', '捧腹网', 'http://www.pengfu.com', '0', '2017-03-20 18:06:44', '2017-03-20 18:06:44');
INSERT INTO `source` VALUES ('147', '嘻嘻哈哈', 'http://www.xxhh.com', '0', '2017-03-20 18:10:05', '2017-03-20 18:10:05');
INSERT INTO `source` VALUES ('148', '哈哈MX', 'http://www.haha.mx/', '0', '2017-03-20 18:10:52', '2017-03-20 18:10:52');
INSERT INTO `source` VALUES ('149', '寸土吧', 'http://www.cuntuba520.com', '0', '2017-03-23 11:59:59', '2017-03-23 11:59:59');
INSERT INTO `source` VALUES ('150', '邪恶漫画', 'http://www.mhkkm.com', '0', '2017-03-27 12:29:18', '2017-03-27 12:29:18');
INSERT INTO `source` VALUES ('151', '3GIF', 'http://www.3gifs.com', '0', '2017-04-11 14:38:50', '2017-04-11 14:38:50');
INSERT INTO `source` VALUES ('152', '搞笑GIF', 'http://www.gaoxiaogif.com/', '0', '2017-04-11 14:39:28', '2017-04-11 14:39:28');


-- 新增详情页评论广告
alter table ads add COLUMN `dm` INT (11) NOT NULL DEFAULT '0' COMMENT '详情页评论中广告';
alter table ads add COLUMN `dms` INT (11) NOT NULL DEFAULT '0' COMMENT '详情页评论中广告间隔';

-- 段子置顶发布时间
alter table joke_top add COLUMN `release_time` datetime DEFAULT NULL COMMENT '发布时间';




CREATE TABLE `distributors_banner` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `d_id` int(11) NOT NULL COMMENT '渠道ID',
  `b_id` int(11) DEFAULT NULL COMMENT '横幅ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--修改评论表搜素引擎
alter TABLE  `comment` ENGINE=InnoDB;
--增加评论表发布状态字段
alter table  `comment` add COLUMN `publish_state` INT (11) NOT NULL DEFAULT '0' COMMENT '发布状态(0:未发布 1:已发布)';



--- 新建姐夫酷数据源 2017-06-12
INSERT INTO `source` VALUES ('153', '姐夫酷', 'http://www.gifcool.com/gif/list_12_1.html', '1', '2017-06-12 14:39:28', '2017-06-12 14:39:28');

--- 新建叶子猪数据源 2017-6-13
INSERT INTO `source` VALUES ('154', '叶子猪', 'http://xx.yzz.cn/', '1', '2017-06-13 11:00:00', '2017-06-13 11:00:00');

--- 新增内涵段子数据源 2017-6-15
INSERT INTO `source` VALUES ('155', '内涵段子', 'http://neihanshequ.com/', '1', '2017-06-13 11:00:00', '2017-06-13 11:00:00');

--- 新增我想网数据源 2017-6-15
INSERT INTO `source` VALUES ('156', '我想网', 'http://www.wowant.com/xieegif/', '1', '2017-06-15 09:46:09', '2017-06-15 14:14:09');


--- 新建拉黑数据库 2017-6-15
CREATE TABLE `black_man` (
  `id` varchar(40) NOT NULL DEFAULT '' COMMENT '主键',
  `nick` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--新增joke字段
alter table joke add isrespider int(11) default 0 comment '是否已经重爬：0否，1是'

--新增distributors字段
alter table distributors add limit_number int(11) default 30 comment '渠道下|列表页显示的限制条数/页'

--- 新增飞华健康网数据源 2017-7-31
INSERT INTO `source` VALUES ('157', '飞华健康网', 'https://sex.fh21.com.cn/qsyk/', '1', '2017-07-31 09:46:09', '2017-07-31 14:14:09');


