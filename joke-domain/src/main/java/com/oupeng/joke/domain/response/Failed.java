package com.oupeng.joke.domain.response;

public class Failed extends Result {
	public Failed() {
		super(0, null, null);
	}
	
	public Failed(String info) {
		super(0, info, null);
	}
	
	public Failed(String info, Object data) {
		super(0, info, data);
	}
}
