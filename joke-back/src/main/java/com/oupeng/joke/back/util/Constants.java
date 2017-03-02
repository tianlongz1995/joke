package com.oupeng.joke.back.util;

public class Constants {
    /**
     * 有效状态 (1:上线/启用/正常)
     */
    public static final Integer ENABLE_STATUS = 1;
    /**
     * 无效状态 (0:下线/停用/失效)
     */
    public static final Integer DISABLE_STATUS = 0;
    /**
     * 段子类型：纯文
     */
    public static final Integer JOKE_TYPE_TEXT = 0;
    /**
     * 段子类型：图片
     */
    public static final Integer JOKE_TYPE_IMG = 1;
    /**
     * 段子类型：动图
     */
    public static final Integer JOKE_TYPE_GIF = 2;
    /**
     * 段子类型：精选
     */
    public static final Integer JOKE_TYPE_CHOICE = 3;
    /**
     * 段子未审核状态 (0:未审核 1:通过 2:不通过 3:发布)
     */
    public static final Integer JOKE_STATUS_NOT_AUDITED = 0;
    /**
     * 段子审核通过状态 (0:未审核 1:通过 2:不通过 3:发布)
     */
    public static final Integer JOKE_STATUS_VALID = 1;
    /**
     * 段子审核通过状态 (0:未审核 1:通过 2:不通过 3:发布)
     */
    public static final Integer JOKE_STATUS_NOVALID = 2;
    /**
     * 段子发布状态 (0:未审核 1:通过 2:不通过 3:发布)
     */
    public static final Integer JOKE_STATUS_PUBLISH = 3;
    /**
     * 渠道上线状态: 1
     */
    public static final Integer CHANNEL_STATUS_VALID = 1;
    public static final Integer CHANNEL_TYPE_COMMON = 0;
    public static final Integer CHANNEL_TYPE_TOPIC = 1;
    public static final Integer CHANNEL_TYPE_RECOMMEND = 2;
    public static final Integer DISTRIBUTOR_STATUS_VALID = 1;
    /**
     * 专题待发布状态 (0:新建 1:下线 2:上线 3:已发布)
     */
    public static final Integer TOPIC_STATUS_VALID = 2;
    /**
     * 专题已发布状态 (0:新建 1:下线 2:上线 3:已发布)
     */
    public static final Integer TOPIC_STATUS_PUBLISH = 3;
    public static final String[] STATIS_DAY_DETAIL = {"日期", "渠道", "频道", "总pv", "总uv", "人均pv", "入口pv", "入口uv", "入口人均pv", "列表页pv", "列表页uv", "列表页人均pv",
            "详情页pv", "详情页uv", "详情页人均pv", "新用户pv", "新用户uv", "新用户留存数", "新用户留存率", "老用户pv", "老用户uv", "老用户留存数", "老用户留存率", "活跃用户留存数", "活跃用户留存率"};
    /**
     * 内容源抓取统计报告表头
     */
    public static final String[] STATIS_SOURCE_CRAWL = {"统计日期", "数据源名称", "URL", "格式", "抓取数量", "状态", "当天抓取次数", "最近一次抓取时间"};
    /**
     * 内容源审核质量统计报告表头
     */
    public static final String[] STATIS_SOURCE_QUALITY = {"统计日期", "数据源名称", "URL", "格式", "审核总数", "通过数", "未通过数", "审核通过率", "最近一次抓取时间"};

    /**
     * banner已发布状态 0 下线 1 上线
     */
    public static final Integer Banner_STATUS_VALID = 1;

    /**
     * banner待发布状态 (0:新建 1:下线 2:上线 3:已发布)	已上线
     */
    public static final Integer BANNER_STATUS_VALID = 2;
    /**
     * banner已发布状态 (0:新建 1:下线 2:上线 3:已发布)
     */
    public static final Integer BANNER_STATUS_PUBLISH = 3;

    /**
     * 精选待发布状态 (0:新建 1:下线 2:上线 3:已发布)	已上线
     */
    public static final Integer CHOICE_STATUS_VALID = 2;
    /**
     * choice已发布状态 (0:新建 1:下线 2:上线 3:已发布)
     */
    public static final Integer CHOICE_STATUS_PUBLISH = 3;
    /**
     * 段子已通过状态
     */
    public static final int AUD = 1;

    /**
     * 段子已发布状态
     */
    public static final int PUB = 3;
    /**
     * 段子未审核
     */
    public static final int NOAUD=0;




}
