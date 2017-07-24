package com.oupeng.joke.domain;

import java.util.Date;

public class Distributor {
	/**	编号	*/
	private Integer id;
	/**	名称	*/
	private String name;
	/**	状态(0:无效、1:有效、2:删除)	*/
	private Integer status;
	/**	创建时间	*/
	private Date createTime;
	/**	创建人	*/
	private String createBy;
	/**	更新时间	*/
	private Date updateTime;
	/**	更新人	*/
	private String updateBy;
	/**	分页开始条数Index	*/
	private Integer offset;
	/**	当前页数	*/
	private Integer pageSize;

	/**
	 * 渠道下|列表页显示的限制条数/页
	 * @return
	 */
	private Integer limit_number;
	
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

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Integer getLimit_number() {
		return limit_number;
	}

	public void setLimit_number(Integer limit_number) {
		this.limit_number = limit_number;
	}
}