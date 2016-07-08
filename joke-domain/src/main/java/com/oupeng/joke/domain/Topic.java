package com.oupeng.joke.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class Topic {
	private Integer id;
	private String title;
	private String content;
	private String img;
	private String dids;
	private Integer status;
	private Date publishTime;
	private Date createTime;
	private Date updateTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public String getDids() {
		return dids;
	}
	public void setDids(String dids) {
		this.dids = dids;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
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
	public String getPublishTimeString() {
		String result = null;
		if(publishTime != null){
			result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(publishTime);
		}
		return result;
	}
	public void setPublishTimeString(String publishTime){
		if(StringUtils.isNotBlank(publishTime)){
			try {
				this.publishTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(publishTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
}