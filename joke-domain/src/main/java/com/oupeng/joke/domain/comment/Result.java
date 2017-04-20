package com.oupeng.joke.domain.comment;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * <pre>
 * 返回结果
 * 状态码: 0:正常、1:缓存查询为空、2:缓存内容获取失败
 * Created by hushuang on 2017/1/13.
 * </pre>
 */
public class Result {
    /**
     * 状态码(0:正常、1:缓存查询为空、2:缓存内容获取失败)
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int status;
    /**
     * 消息说明
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String msg;
    /**
     * 数据内容
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map data;

    /**
     * @param status 状态(0:正常、1:缓存查询为空、2:缓存内容获取失败)
     */
    public Result(int status) {
        this.status = status;
    }

    /**
     * <pre>
     * 状态码: 0:正常、1:缓存查询为空、2:缓存内容获取失败
     * @param status  状态(0:正常、1:缓存查询为空、2:缓存内容获取失败)
     * @param data   结果集
     * </pre>
     */
    public Result(int status, Map data) {
        this.status = status;
        this.data = data;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }
}
