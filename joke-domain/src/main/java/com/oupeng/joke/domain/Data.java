package com.oupeng.joke.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 数据内容
 * Created by hushuang on 2017/1/13.
 */
public class Data {
    /** 记录数量   */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer size;
    /** 数据内容   */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    public Data(Object data){
        this.size = 1;
        this.data = data;
    }

    public Data(Integer size, Object data){
        this.size = size;
        this.data = data;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
