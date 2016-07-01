package com.oupeng.joke.domain;

import java.util.Date;

/**
 * 反馈
 * Created by hushuang on 16/7/1.
 */
public class Feedback {
    /** 反馈编号    */
    private Integer id;
    /** 渠道编号    */
    private Integer channelId;
    /** 反馈问题类型    */
    private Integer type;
    /** 联系方式    */
    private String links;
    /** 反馈内容    */
    private String content;
    /** 创建时间    */
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
