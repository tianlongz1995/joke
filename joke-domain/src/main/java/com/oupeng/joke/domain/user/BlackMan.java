package com.oupeng.joke.domain.user;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

/**
 * Created by pengzheng on 17-6-14.
 */
public class BlackMan {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date updateTime;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
