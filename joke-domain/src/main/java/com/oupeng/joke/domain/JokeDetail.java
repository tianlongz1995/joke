package com.oupeng.joke.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class JokeDetail {
	@JsonInclude(Include.NON_NULL)
	private String title;
	@JsonInclude(Include.NON_NULL)
	private String content;
	@JsonInclude(Include.NON_NULL)
	private String img;
	@JsonInclude(Include.NON_NULL)
	private String gif;
	@JsonInclude(Include.NON_NULL)
	private Integer type;
	@JsonInclude(Include.NON_NULL)
	private Integer good;
	@JsonInclude(Include.NON_NULL)
	private Integer bad;
	@JsonInclude(Include.NON_NULL)
	private Integer nextId;
	@JsonInclude(Include.NON_NULL)
	private Integer lastId;
//	@JsonInclude(Include.NON_NULL)
//	private List<Joke> relatedText;
//	@JsonInclude(Include.NON_NULL)
	private List<Joke> relatedImg;
//	private List<Joke> recommend;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getGif() {
		return gif;
	}
	public void setGif(String gif) {
		this.gif = gif;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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
	public Integer getNextId() {
		return nextId;
	}
	public void setNextId(Integer nextId) {
		this.nextId = nextId;
	}
	public Integer getLastId() {
		return lastId;
	}
	public void setLastId(Integer lastId) {
		this.lastId = lastId;
	}
//	public List<Joke> getRecommend() {
//		return recommend;
//	}
//	public void setRecommend(List<Joke> recommend) {
//		this.recommend = recommend;
//	}
//	public List<Joke> getRelatedText() {
//		return relatedText;
//	}
//	public void setRelatedText(List<Joke> relatedText) {
//		this.relatedText = relatedText;
//	}
	public List<Joke> getRelatedImg() {
		return relatedImg;
	}
	public void setRelatedImg(List<Joke> relatedImg) {
		this.relatedImg = relatedImg;
	}
}
