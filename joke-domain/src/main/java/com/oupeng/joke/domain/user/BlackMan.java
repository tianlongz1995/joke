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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String create_by;

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

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
