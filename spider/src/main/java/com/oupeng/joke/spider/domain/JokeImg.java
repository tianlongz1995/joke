package com.oupeng.joke.spider.domain;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * Created by Administrator on 2017/3/8.
 */
@TargetUrl("http://www.laifudao.com/tupian/\\d{5,8}.htm")
@HelpUrl("http://www.laifudao.com/tupian/\\w+_\\d{1,3}.htm")
public class JokeImg implements AfterExtractor {
    private Integer id;
    @ExtractBy("//header[@class='post-header']//a/text()")
    private String title;

    @ExtractBy("//div[@class='post-content stickem-container']//a/img/@src")
    private String img;

    private String gif;
    @ExtractBy("//div[@class='post-content stickem-container']//a/img/@width")
    private Integer width;
    @ExtractBy("//div[@class='post-content stickem-container']//a/img/@height")
    private Integer height;
    /**
     * (0:文本、1:图片、2:动图、3:富文本、4:视频、10:广告)
     */

    private Integer type;

    private Integer good;

    private Integer bad;

    private Integer status;


    /**
     * 来源
     */
    @ExtractBy("//header[@class='clearfix content-header breadcrumbs']//a[4]/@href")
    private String src;
    /**
     * 评论内容
     */

    // @ExtractBy("//div[@class='comment-box']//ul/li/div[@class='text']/text()")
    @ExtractBy("//section[@class='post-comments hot-comments']//ul/li/div[@class='text']/text()")
    private String commentContent;

    /**
     * 用户头像URL
     */

    private String avata;
    /**
     * 神评点赞数大于10
     */
    // @ExtractBy("//div[@class='comment-box']//p/span/em/text()")
    @ExtractBy("//section[@class='post-comments hot-comments']//p/span/em/text()")
    private Integer agreeTotal;
    /**
     * 昵称
     */

    private String nick;
    /**
     * 评论
     */

    private Comment comment;

    private Integer sourceId;


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


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getGif() {
        return gif;
    }

    public void setGif(String gif) {
        this.gif = gif;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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


    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
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

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Integer getAgreeTotal() {
        return agreeTotal;
    }

    public void setAgreeTotal(Integer agreeTotal) {
        this.agreeTotal = agreeTotal;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public void afterProcess(Page page) {

    }
}

