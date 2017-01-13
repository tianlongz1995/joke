package com.oupeng.joke.domain;

public class Channels {
	/**	主键	ID*/
	private Integer i;
	/**	频道名称	*/
	private String n;
	/**	排序位置	*/
	private Integer s;
	/**	banner位置状态	*/
	private Boolean b;

	public Channels() {
	}

	public Integer getI() {
		return i;
	}

	public void setI(Integer i) {
		this.i = i;
	}

	public String getN() {
		return n;
	}

	public void setN(String n) {
		this.n = n;
	}

	public Integer getS() {
		return s;
	}

	public void setS(Integer s) {
		this.s = s;
	}

	public Boolean getB() {
		return b;
	}

	public void setB(Boolean b) {
		this.b = b;
	}
}