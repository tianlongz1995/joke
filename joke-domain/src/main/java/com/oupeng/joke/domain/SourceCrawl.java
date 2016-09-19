package com.oupeng.joke.domain;

import java.util.Date;

public class SourceCrawl {
	/**	主键id	*/
	private Integer id;
	/**	内容源id	*/
	private Integer sourceId;
	/**	内容源名称 */
	private String sourceName;
	/**	内容源URL */
	private String url;
	/**	抓取日期：yyyyMMdd	*/
	private Integer day;
    /**	当天爬虫抓取数量	*/
    private Integer grabTotal;
	/**	当天爬虫抓取次数(日)	*/
	private Integer grabCount;
	/**	内容格式	*/
	private Integer type;
	/**	审核通过数	*/
	private Integer passed;
	/**	未审核通过数	*/
	private Integer failed;
	/**	当天审核通过率	*/
	private Double verifyRate;
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

	public Double getVerifyRate() {
		return verifyRate;
	}

	public void setVerifyRate(Double verifyRate) {
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

    public Integer getGrabTotal() {
        return grabTotal;
    }

    public void setGrabTotal(Integer grabTotal) {
        this.grabTotal = grabTotal;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPassed() {
        return passed;
    }

    public void setPassed(Integer passed) {
        this.passed = passed;
    }

    public Integer getFailed() {
        return failed;
    }

    public void setFailed(Integer failed) {
        this.failed = failed;
    }
}