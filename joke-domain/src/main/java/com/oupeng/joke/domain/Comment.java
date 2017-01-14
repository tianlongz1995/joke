package com.oupeng.joke.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 评论
 * Created by hushuang on 2017/1/13.
 */
public class Comment {
    /** 评论数量(total)	*/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer total;
    /** 评论内容 (content)	*/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String bc;
    /** 用户头像 (avata)	*/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String avata;
    /** 昵称 (nick)	*/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nick;

    public Comment(Integer total, String content, String avata, String nick) {
        this.total = total;
        this.bc = content;
        this.avata = avata;
        this.nick = nick;
    }

    public Comment() {
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getBc() {
        return bc;
    }

    public void setBc(String bc) {
        this.bc = bc;
    }

    public String getAvata() {
        return avata;
    }

    public void setAvata(String avata) {
        this.avata = avata;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
