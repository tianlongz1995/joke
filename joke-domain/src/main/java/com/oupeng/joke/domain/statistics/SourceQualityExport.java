package com.oupeng.joke.domain.statistics;

public class SourceQualityExport {
	/**	抓取日期：yyyyMMdd	*/
	private String day;
	/**	内容源名称 */
	private String sourceName;
	/**	内容源URL */
	private String url;
	/**	内容格式	*/
	private String type;
	/**	审核总数	*/
	private String total;
	/**	审核通过数	*/
	private String passed;
	/**	未审核通过数	*/
	private String failed;
	/**	当天审核通过率	*/
	private String verifyRate;
	/**	当天最近一次抓取时间	*/
	private String lastGrabTime;

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

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

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getPassed() {
		return passed;
	}

	public void setPassed(String passed) {
		this.passed = passed;
	}

	public String getFailed() {
		return failed;
	}

	public void setFailed(String failed) {
		this.failed = failed;
	}

	public String getVerifyRate() {
		return verifyRate;
	}

	public void setVerifyRate(String verifyRate) {
		this.verifyRate = verifyRate;
	}

	public String getLastGrabTime() {
		return lastGrabTime;
	}

	public void setLastGrabTime(String lastGrabTime) {
		this.lastGrabTime = lastGrabTime;
	}
}
