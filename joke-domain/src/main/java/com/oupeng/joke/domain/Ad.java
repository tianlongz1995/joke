package com.oupeng.joke.domain;

import java.util.Date;

public class Ad {
	/**	编号	*/
	private Integer id;
	/**	广告位编号	*/
	private Integer slotId;
	/**	位置 1:列表页中间 2:列表页底部 3:详情页上方 4:详情页中部 5:详情页底部	*/
	private Integer pos;
	/**	pos=1时，广告插入间隔	*/
	private Integer slide;
	/**	渠道id	*/
	private Integer did;
	/**	状态 0:下线 1:上线	*/
	private Integer status;
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
	public Integer getSlotId() {
		return slotId;
	}
	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}
	public Integer getPos() {
		return pos;
	}
	public void setPos(Integer pos) {
		this.pos = pos;
	}
	public Integer getSlide() {
		return slide;
	}
	public void setSlide(Integer slide) {
		this.slide = slide;
	}
	public Integer getDid() {
		return did;
	}
	public void setDid(Integer did) {
		this.did = did;
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
}