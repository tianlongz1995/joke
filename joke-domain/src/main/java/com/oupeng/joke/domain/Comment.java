package com.oupeng.joke.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 评论
 * Created by hushuang on 2017/1/13.
 */
public class Comment {
    /**
     * 评论id
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id;
    /**
     * 评论数量(total)
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer total;
    /**
     * 评论内容 (content)
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String bc;
    /**
     * 用户头像 (avata)
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String avata;
    /**
     * 昵称 (nick)
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nick;
    /**
     * 段子id (sid)
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer jokeId;
    /**
     * 创建时间(int(11))
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer time;
    /**
     * 点赞数
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer good;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer uid;

    public Comment(Integer id, String bc, String avata, String nick, Integer jokeId, Integer time, Integer good) {
        this.id = id;
        this.bc = bc;
        this.avata = avata;
        this.nick = nick;
        this.jokeId = jokeId;
        this.time = time;
        this.good = good;
    }

    public Comment(String bc, String avata, String nick, Integer jokeId, Integer time, Integer uid) {
        this.bc = bc;
        this.avata = avata;
        this.nick = nick;
        this.jokeId = jokeId;
        this.time = time;
        this.uid = uid;
    }

    public Comment(Integer total, String content, String avata, String nick) {
        this.total = total;
        this.bc = content;
        this.avata = avata;
        this.nick = nick;
    }

    public Comment(Integer total, Integer jokeId) {
        this.total = total;
        this.jokeId = jokeId;
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

    public Integer getJokeId() {
        return jokeId;
    }

    public void setJokeId(Integer jokeId) {
        this.jokeId = jokeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getGood() {
        return good;
    }

    public void setGood(Integer good) {
        this.good = good;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }
}
