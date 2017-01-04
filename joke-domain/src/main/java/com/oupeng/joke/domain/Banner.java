package com.oupeng.joke.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Date;

public class Banner {
    @JsonInclude(Include.NON_NULL)
    private Integer id;
    @JsonInclude(Include.NON_NULL)
    private String title;
    @JsonInclude(Include.NON_NULL)
    private String content;
    @JsonInclude(Include.NON_NULL)
    private Integer jid;
    @JsonInclude(Include.NON_NULL)
    private Integer type;
    @JsonInclude(Include.NON_NULL)
    private String img;
    @JsonInclude(Include.NON_NULL)
    private Integer cid;
    @JsonInclude(Include.NON_NULL)
    private Integer status;
    @JsonInclude(Include.NON_NULL)
    private Date createTime;
    @JsonInclude(Include.NON_NULL)
    private Date updateTime;
    @JsonInclude(Include.NON_NULL)
    private Integer adid;
    @JsonInclude(Include.NON_NULL)
    private Integer sort;

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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getJid() {
        return jid;
    }

    public void setJid(Integer jid) {
        this.jid = jid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAdid() {
        return adid;
    }

    public void setAdid(Integer adid) {
        this.adid = adid;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}

