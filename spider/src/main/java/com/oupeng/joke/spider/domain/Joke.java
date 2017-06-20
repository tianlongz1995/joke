package com.oupeng.joke.spider.domain;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zongchao on 2017/3/13.
 *
 * change by xiongyingl: comment_number(神评数量),List<CommentT>(神评列表) 2017/6/19
 */
public class Joke {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String title;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer source_id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String source;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String create_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String content;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String avata;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nick;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer comment_number;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer good;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer bad;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String releaseAvata;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String releaseNick;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CommentT> comment;



    public Integer getComment_number() {
        return comment_number;
    }

    public void setComment_number(Integer comment_number) {
        this.comment_number = comment_number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSource_id() {
        return source_id;
    }

    public void setSource_id(Integer source_id) {
        this.source_id = source_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCreate_time() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String dates = dateFormat.format(date);
        return dates;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getReleaseAvata() {
        return releaseAvata;
    }

    public void setReleaseAvata(String releaseAvata) {
        this.releaseAvata = releaseAvata;
    }

    public String getReleaseNick() {
        return releaseNick;
    }

    public void setReleaseNick(String releaseNick) {
        this.releaseNick = releaseNick;
    }

    public Integer getGood() {
        return good;
    }

    public void setGood(Integer good) {
        this.good = good;
    }

    public Integer getBad() {
        return bad;
    }

    public void setBad(Integer bad) {
        this.bad = bad;
    }

    public List<CommentT> getComment() {
        return comment;
    }

    public void setComment(List<CommentT> comment) {
        this.comment = comment;
    }
}
