package com.oupeng.joke.domain;

import java.util.Date;

public class SourceCrawl {
	/**	主键id	*/
	private Integer id;
	/**	内容源id	*/
	private Integer sourceId;
	/**	内容源名称 */
	private Integer sourceName;
	/**	内容源URL */
	private Integer url;
	/**	抓取日期：yyyyMMdd	*/
	private Integer day;
	/**	当天爬虫抓取次数	*/
	private Integer grabCount;
	/**	当天审核通过率	*/
	private Integer verifyRate;
	/**	数据源状态	*/
	private Integer status;
	/**	当天最近一次抓取时间	*/
	private Date lastGrabTime;
	/**	创建时间	*/
	private Date createTime;
	/**	更新时间	*/
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSourceId() {
		return sourceId;
	}

	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

	public Integer getSourceName() {
		return sourceName;
	}

	public void setSourceName(Integer sourceName) {
		this.sourceName = sourceName;
	}

	public Integer getUrl() {
		return url;
	}

	public void setUrl(Integer url) {
		this.url = url;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getGrabCount() {
		return grabCount;
	}

	public void setGrabCount(Integer grabCount) {
		this.grabCount = grabCount;
	}

	public Integer getVerifyRate() {
		return verifyRate;
	}

	public void setVerifyRate(Integer verifyRate) {
		this.verifyRate = verifyRate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getLastGrabTime() {
		return lastGrabTime;
	}

	public void setLastGrabTime(Date lastGrabTime) {
		this.lastGrabTime = lastGrabTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}