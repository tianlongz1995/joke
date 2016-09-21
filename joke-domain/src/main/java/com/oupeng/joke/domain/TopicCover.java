package com.oupeng.joke.domain;

import java.util.Date;

/**
 * 专题封面
 */
public class TopicCover {
	/**	主键编号	*/
	private Integer id;
	/**	名称	*/
	private String name;
	/**	logo地址	*/
	private String logo;
	/**	状态: 1:启用;0:停用	*/
	private Integer status;
	/**	序号	*/
	private Integer seq;
	/**	更新时间	*/
	private Date updateTime;
	/**	更新用户	*/
	private String updateBy;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
}