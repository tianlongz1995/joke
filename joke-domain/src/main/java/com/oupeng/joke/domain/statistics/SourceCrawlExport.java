package com.oupeng.joke.domain.statistics;

public class SourceCrawlExport {
	/**	抓取日期：yyyyMMdd	*/
	private String day;
	/**	内容源名称 */
	private String sourceName;
	/**	内容源URL */
	private String url;
	/**	内容格式	*/
	private String type;
	/**	当天爬虫抓取数量	*/
	private String grabTotal;
	/**	数据源状态	*/
	private String status;
	/**	当天爬虫抓取次数(日)	*/
	private String grabCount;
	/**	当天最近一次抓取时间	*/
	private String lastGrabTime;

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getGrabTotal() {
		return grabTotal;
	}

	public void setGrabTotal(String grabTotal) {
		this.grabTotal = grabTotal;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGrabCount() {
		return grabCount;
	}

	public void setGrabCount(String grabCount) {
		this.grabCount = grabCount;
	}

	public String getLastGrabTime() {
		return lastGrabTime;
	}

	public void setLastGrabTime(String lastGrabTime) {
		this.lastGrabTime = lastGrabTime;
	}
}
