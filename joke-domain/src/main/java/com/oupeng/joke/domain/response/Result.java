package com.oupeng.joke.domain.response;

/**
 * This is a base response including status, information, data of response
 * return this object by json
 * 
 * @author luke
 */
public class Result {
	private int status;
	private String info;
	private String msg;
	private Object data;
	
	public Result() {}
	
	public Result(int status, String info, Object data) {
		this.status = status;
		this.info = info;
		this.data = data;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
