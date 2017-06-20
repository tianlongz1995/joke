package com.oupeng.joke.spider.domain;


import java.util.List;

/**
 * change by xiongyingl on 2017/6/16. 增加了评论相关List,可添加多条神评论
 */
public class JokeText {

    private Integer id;

    private String title;

    private String content;

    /**
     * (0:文本、1:图片、2:动图、3:富文本、4:视频、10:广告)
     */
    private Integer type;
    /**
     * 来源
     */
    private String src;
    /**
     * 内容源id
     */
    private Integer sourceId;
    /**
     * 评论内容
     */
    private String commentContent;
    /**
     * 神评点赞数大于10
     */
    private Integer agreeTotal;
    /**
     * 用户头像URL
     */
    private String avata;
    /**
     * 昵称
     */
    private String nick;
    /**
     * 神评数量
     */
    private Integer commentNumber;
    /**
     * 神评点赞数(与评论内容一一对应)
     */
    private List<Integer> hotGoods; //
    /**
     * 神评内容
     */
    private List<String> hotContents; //神评内容


    public Integer getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(Integer commentNumber) {
        this.commentNumber = commentNumber;
    }

    public List<Integer> getHotGoods() {
        return hotGoods;
    }

    public void setHotGoods(List<Integer> hotGoods) {
        this.hotGoods = hotGoods;
    }

    public List<String> getHotContents() {
        return hotContents;
    }

    public void setHotContents(List<String> hotContents) {
        this.hotContents = hotContents;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Integer getAgreeTotal() {
        return agreeTotal;
    }

    public void setAgreeTotal(Integer agreeTotal) {
        this.agreeTotal = agreeTotal;
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

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }
}
