package com.oupeng.joke.domain;

import java.util.Date;

public class JokeTop {
    /** 编号    */
	private Integer id;
    /** 标题    */
	private String title;
    /** 内容    */
	private String content;
    /** 图片    */
	private String img;
    /** 动图    */
	private String gif;
    /** (0:文本、1:图片、2:动图、3:富文本、4:视频、10:广告)     */
	private Integer type;
    /** 排序值    */
	private Integer sort;
    /** 状态    */
    private Integer status;
    /** 数据源编号    */
	private Integer sourceId;
    /** 数据源名称    */
	private String sourceName;
    /** 创建时间    */
	private Date createTime;
    /** 修改时间    */
	private Date updateTime;
    /** 发布日期    */
    private String releaseDate;
    /** 发布时间    */
    private String releaseHours;

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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseHours() {
        return releaseHours;
    }

    public void setReleaseHours(String releaseHours) {
        this.releaseHours = releaseHours;
    }
}
