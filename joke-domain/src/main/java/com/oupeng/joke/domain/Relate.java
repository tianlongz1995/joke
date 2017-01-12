package com.oupeng.joke.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 段子推荐
 * Created by hushuang on 2017/1/12.
 */
public class Relate {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer cid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String img;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String txt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}
