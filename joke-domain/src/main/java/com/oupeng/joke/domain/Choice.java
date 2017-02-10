package com.oupeng.joke.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private String img;
    @JsonInclude(Include.NON_NULL)
    private String content;
    @JsonInclude(Include.NON_NULL)
    private Integer type;
    @JsonInclude(Include.NON_NULL)
    private Integer status;
    @JsonInclude(Include.NON_NULL)
    private Integer good;
    @JsonInclude(Include.NON_NULL)
    private Integer bad;
    @JsonInclude(Include.NON_NULL)
    private Integer width;
    @JsonInclude(Include.NON_NULL)
    private Integer height;
    @JsonInclude(Include.NON_NULL)
    private String createTime;
    @JsonInclude(Include.NON_NULL)
    private String updateTime;

    @JsonInclude(Include.NON_NULL)
    private Date publishTime;

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
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
