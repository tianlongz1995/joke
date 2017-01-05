package com.oupeng.joke.domain;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Date;

/**
 * 精选
 */
public class Choice {
    @JsonInclude(Include.NON_NULL)
    private Integer id;
    @JsonInclude(Include.NON_NULL)
    private String title;
    @JsonInclude(Include.NON_NULL)
    private String content;
    @JsonInclude(Include.NON_NULL)
    private Integer status;
    @JsonInclude(Include.NON_NULL)
    private Date createTime;
    @JsonInclude(Include.NON_NULL)
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
}
