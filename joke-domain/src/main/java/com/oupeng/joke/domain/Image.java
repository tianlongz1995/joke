package com.oupeng.joke.domain;

/**
 * Created by hushuang on 2017/3/10.
 */
public class Image {
    /**
     * 宽度
     */
    private Integer width;
    /**
     * 高度
     */
    private Integer height;
    /**
     * 路径
     */
    private String path;


    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
