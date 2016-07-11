package com.oupeng.joke.domain;

import java.util.Arrays;
import java.util.List;

/**
 * 折线图
 * Created by hushuang on 16/7/1.
 */
public class LineGraph {
    /** 折线数据数组    */
    private List<LineData> data;
    /** 数据标签    */
    private String[] labels;
    /** 图表显示最大值    */
    private Integer max;
    /** 图表数据间隔    */
    private Integer space;

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public List<LineData> getData() {
        return data;
    }

    public void setData(List<LineData> data) {
        this.data = data;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getSpace() {
        return space;
    }

    public void setSpace(Integer space) {
        this.space = space;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LineGraph{");
        sb.append("data=").append(data);
        sb.append(", labels=").append(labels == null ? "null" : Arrays.asList(labels).toString());
        sb.append(", max=").append(max);
        sb.append(", space=").append(space);
        sb.append('}');
        return sb.toString();
    }
}
