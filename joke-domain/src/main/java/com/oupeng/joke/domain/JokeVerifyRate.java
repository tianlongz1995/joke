package com.oupeng.joke.domain;

public class JokeVerifyRate {
	private Integer soureId;
	private Integer validNum;
	private Integer inValidNum;
	public Integer getSoureId() {
		return soureId;
	}
	public void setSoureId(Integer soureId) {
		this.soureId = soureId;
	}
	public Integer getValidNum() {
		return validNum;
	}
	public void setValidNum(Integer validNum) {
		this.validNum = validNum;
	}
	public Integer getInValidNum() {
		return inValidNum;
	}
	public void setInValidNum(Integer inValidNum) {
		this.inValidNum = inValidNum;
	}
}
