package com.oupeng.joke.back.util;

public class Constants {
	public static final Integer JOKE_TYPE_TEXT = 0;
	public static final Integer JOKE_TYPE_IMG = 1;
	public static final Integer JOKE_TYPE_GIF = 2;
	public static final Integer JOKE_STATUS_NOT_AUDITED = 0;
	public static final Integer JOKE_STATUS_VALID = 1;
	public static final Integer JOKE_STATUS_PUBLISH = 3;
	/**	渠道上线状态: 1	*/
	public static final Integer CHANNEL_STATUS_VALID = 1;
	public static final Integer CHANNEL_TYPE_COMMON = 0;
	public static final Integer CHANNEL_TYPE_TOPIC = 1;
	public static final Integer CHANNEL_TYPE_RECOMMEND = 2;
	public static final Integer DISTRIBUTOR_STATUS_VALID = 1;
	public static final Integer TOPIC_STATUS_VALID = 2;
	public static final Integer TOPIC_STATUS_PUBLISH = 3;
	public static final String[] STATIS_DAY_DETAIL = {"日期","渠道","频道","总pv","总uv","人均pv","入口pv","入口uv","入口人均pv","列表页pv","列表页uv","列表页人均pv",
			"详情页pv","详情页uv","详情页人均pv","新用户pv","新用户uv","新用户留存数","新用户留存率","老用户pv","老用户uv","老用户留存数","老用户留存率","活跃用户留存数","活跃用户留存率"};
	/**	内容源抓取统计报告表头	*/
	public static final String[] STATIS_SOURCE_CRAWL = {"统计日期","数据源名称","URL","格式","抓取数量","状态","当天抓取次数","最近一次抓取时间"};
	/**	内容源审核质量统计报告表头	*/
	public static final String[] STATIS_SOURCE_QUALITY = {"统计日期","数据源名称","URL","格式","审核总数","通过数","未通过数","审核通过率","最近一次抓取时间"};

}
