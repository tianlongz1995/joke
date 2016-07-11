package com.oupeng.joke.domain.log;

import com.alibaba.fastjson.JSON;

public class ClickLog {
	private Integer jid;//进入详情页的段子id
	private String uid;//进入详情页的段子id
	private Integer type;//类型  1:赞 2:踩 3:进入详情页 
	private Long timestamp;//时间戳
	
	public ClickLog(){
		super();
	}
	public ClickLog(Integer jid,String uid,Integer type){
		super();
		this.jid = jid;
		this.uid = uid;
		this.type = type;
		this.timestamp = System.currentTimeMillis();
	}
	
	public Integer getJid() {
		return jid;
	}
	public void setJid(Integer jid) {
		this.jid = jid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String toString() {
		return JSON.toJSONString(this);
	}
}