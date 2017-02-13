package com.oupeng.joke.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Success extends Result {

	private Integer total;
	private String name;
	private Integer bid ;
	private Integer cid;
	private String cname;

	public Success() {
		super(1, null, null);
	}
	
	public Success(Object data) {
		super(1, null, data);
	}
	public Success(Object data,Integer bid,Integer cid,String cname) {
		super(1, null, data);
		this.bid = bid;
		this.cid= cid;
		this.cname = cname;
	}

	public Success(String info) {
		super(1, info, null);
	}
	public Success(Object data, Integer total) {
		super(1, null, data);
		this.total = total;
	}
	public Success(String info, Object data) {
		super(1, info, data);
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getBid() {
		return bid;
	}

	public void setBid(Integer bid) {
		this.bid = bid;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}
}
