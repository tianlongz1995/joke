package com.oupeng.joke.domain;

import java.util.Arrays;

/**
 * 折线图数据
 * Created by hushuang on 16/7/1.
 */
public class LineData {
    /** 类型名称    */
    private String name;
    /** 值    */
    private Integer[] value;
    /** 颜色    */
    private String color;
    /** 折线宽度    */
    private Integer line_width = 2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer[] getValue() {
        return value;
    }

    public void setValue(Integer[] value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getLine_width() {
        return line_width;
    }

    public void setLine_width(Integer line_width) {
        this.line_width = line_width;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LineData{");
        sb.append("name='").append(name).append('\'');
        sb.append(", value=").append(value == null ? "null" : Arrays.asList(value).toString());
        sb.append(", color='").append(color).append('\'');
        sb.append(", line_width=").append(line_width);
        sb.append('}');
        return sb.toString();
    }
}
