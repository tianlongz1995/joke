package com.oupeng.joke.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * <pre>
 * 返回结果
 * 状态码: 0:正常、1:缓存查询为空、2:缓存内容获取失败
 * Created by hushuang on 2017/1/13.
 * </pre>
 */
public class Result {
    /** 状态码(0:正常、1:缓存查询为空、2:缓存内容获取失败)    */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int status;
    /** 消息说明    */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String msg;
    /** 数据内容   */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Data data;

    /**
     * <pre>
     * 状态码: 0:正常、1:缓存查询为空、2:缓存内容获取失败
     * Created by hushuang on 2017/1/13.
     * @param size  记录总数
     * @param list  结果数组
     * </pre>
     */
    public Result(int size, List<?> list) {
        this.data = new Data(size, list);
    }

    /**
     * <pre>
     * 状态码: 0:正常、1:缓存查询为空、2:缓存内容获取失败
     * Created by hushuang on 2017/1/13.
     * @param  obj 结果对象
     * </pre>
     */
    public Result(Object obj) {
        this.data = new Data(obj);
    }

    /**
     * <pre>
     * Created by hushuang on 2017/1/13.
     * @param msg       消息说明
     * @param status    状态(0:正常、1:缓存查询为空、2:缓存内容获取失败)
     * </pre>
     */
    public Result(String msg, int status) {
        this.msg = msg;
        this.status = status;
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
