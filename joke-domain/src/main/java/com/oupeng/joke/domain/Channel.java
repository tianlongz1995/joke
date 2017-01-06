package com.oupeng.joke.domain;

import java.util.Date;

public class Channel {
	/**	主键	*/
	private Integer id;
	/**	频道名称	*/
	private String name;
	/**	频道类型  0:普通 1:专题 2:推荐	*/
	private Integer type;
	/**	频道状态 0:下线、 1:上线、2:删除	*/
	private Integer status;
	/**	频道内容类型(笑话属性，多个用逗号分隔 0:纯文;1:图片;2:动图) */
	private String contentType;
	/**	点赞数	*/
	private Integer good;
	/**	被踩数	*/
	private Integer bad;
	/**	创建时间	*/
	private Date createTime;
	/**	更新时间	*/
	private Date updateTime;
	/**	定时发布数量	*/
	private Integer size;

	public Channel() {
	}

	/**
	 *
	 * @param id 	编号
	 * @param type	频道类型  0:普通 1:专题 2:推荐
	 */
	public Channel(Integer id, Integer type) {
		this.id = id;
		this.type = type;
	}

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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public Integer getGood() {
		return good;
	}
	public void setGood(Integer good) {
		this.good = good;
	}
	public Integer getBad() {
		return bad;
	}
	public void setBad(Integer bad) {
		this.bad = bad;
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

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
}