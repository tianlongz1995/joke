package com.oupeng.joke.domain.log;

import com.alibaba.fastjson.JSON;

public class ImprLog {
	private Integer did;//渠道id
	private Integer cid;//频道id
	private String uid;//用户id
	private Integer type;//类型  1:渠道入口 2:频道入口 3:列表页 4:详情页
	private Long timestamp;//时间戳
	
	public ImprLog(){
		super();
	}
	
	public ImprLog(Integer did,Integer cid,String uid,Integer type){
		super();
		this.did = did;
		this.cid = cid;
		this.type = type;
		this.uid = uid;
		this.timestamp = System.currentTimeMillis();
	}

	public ImprLog(Integer did,Integer cid,String uid,Integer type, Long timestamp){
		super();
		this.did = did;
		this.cid = cid;
		this.type = type;
		this.uid = uid;
		this.timestamp = timestamp;
	}

	public Integer getDid() {
		return did;
	}
	public void setDid(Integer did) {
		this.did = did;
	}
	public Integer getCid() {
		return cid;
	}
	public void setCid(Integer cid) {
		this.cid = cid;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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
	public String toString() {
		return JSON.toJSONString(this);
	}
}