package com.oupeng.joke.domain;


/**
 * Created by hushuang on 2017/1/4.
 */
public class IndexItem {
    /**
     * 值
     */
    private String value;
    /**
     * 类型(0:配置更新、1:线上资源、2:备份资源、3:测试资源)
     */
    private Integer type;

    public IndexItem() {
    }

    public IndexItem(String value, Integer type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
