package com.oupeng.joke.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private Integer slot;
    @JsonInclude(Include.NON_NULL)
    private Integer sort;
    private Integer width;
    @JsonInclude(Include.NON_NULL)
    private Integer height;
    @JsonInclude(Include.NON_NULL)
    private Date publishTime;

   /** 渠道ID*/
    @JsonInclude(Include.NON_NULL)
    private Integer did;
    /** 渠道名称*/
    @JsonInclude(Include.NON_NULL)
    private String dName;

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

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
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

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
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

