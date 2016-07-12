package com.oupeng.joke.domain;

import java.util.Date;

public class Distributor {
	private Integer id;
	private String name;
	private Integer status;
	private Date createTime;
	private Date updateTime;
	/**	分页开始条数Index	*/
	private Integer offset;
	/**	当前页数	*/
	private Integer pageSize;
	
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}