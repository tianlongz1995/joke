package com.oupeng.joke.back.util;

/**
 * 渠道频道删除任务
 * Created by hushuang on 2016/11/15.
 */
public class Task {
    /** 主键 */
    private String id;
    /** 任务执行规则 */
    private String cron;
    /** 任务类型(1:文字段子、2:趣图、3:推荐、4:精选) */
    private Integer type;

    public Task(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
