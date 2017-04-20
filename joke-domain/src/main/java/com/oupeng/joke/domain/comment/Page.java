package com.oupeng.joke.domain.comment;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by java_zong on 2017/4/20.
 */

public class Page {
    /**
     * 每页数量
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int size;
    /**
     * 当前页码
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int current;
    /**
     * 总数
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int total;

    public Page(int size, int current, int total) {
        this.size = size;
        this.current = current;
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
