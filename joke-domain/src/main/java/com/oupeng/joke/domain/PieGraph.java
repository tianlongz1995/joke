package com.oupeng.joke.domain;

/**
 * 饼图
 * Created by hushuang on 16/7/1.
 */
public class PieGraph {
    /** 名称    */
    private String name;
    /** 数值    */
    private Double value;
    /** 颜色    */
    private String color;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
