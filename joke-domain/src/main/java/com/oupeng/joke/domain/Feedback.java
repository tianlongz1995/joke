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
    private Integer distributorId;
    /** 频道编号    */
    private Integer channelId;
    /** 反馈问题类型 (1:刷新慢；2:不好笑；3:闪退/黑屏；4:其他)   */
    private Integer type;
    /** 反馈内容    */
    private String content;
    /** 反馈时间    */
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Feedback{");
        sb.append("id=").append(id);
        sb.append(", distributorId=").append(distributorId);
        sb.append(", channelId=").append(channelId);
        sb.append(", type=").append(type);
        sb.append(", content='").append(content).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append('}');
        return sb.toString();
    }
}
