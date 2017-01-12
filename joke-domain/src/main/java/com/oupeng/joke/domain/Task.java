package com.oupeng.joke.domain;

import com.alibaba.fastjson.JSONObject;

/**
 * 渠道频道删除任务
 * Created by hushuang on 2016/11/15.
 */
public class Task {
    /** 主键 */
    private String id;
    /** 名称 */
    private String name;
    /** 规则 */
    private String policy;
    /** 任务执行规则 */
    private String cron;
    /** 任务类型(1:趣图、2:文字段子、3:推荐、4:精选) */
    private Integer type;

    private JSONObject object;

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

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }
}
