package com.oupeng.joke.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by hushuang on 2017/1/13.
 */
public class Comment {
    /** 评论数量(total)	*/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer t;
    /** 评论内容 (content)	*/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String c;
    /** 用户头像 (avata)	*/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String a;
    /** 昵称 (nick)	*/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String n;

    public Comment(Integer total, String content, String avata, String nick) {
        this.t = total;
        this.c = content;
        this.a = avata;
        this.n = nick;
    }

    public Comment() {
    }

    public Integer getT() {
        return t;
    }

    public void setT(Integer t) {
        this.t = t;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }
}
