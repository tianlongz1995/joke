package com.oupeng.joke.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Success extends Result {

	private Integer total;

	public Success() {
		super(1, null, null);
	}
	
	public Success(Object data) {
		super(1, null, data);
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
}
