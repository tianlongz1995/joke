package com.oupeng.joke.spider.domain.pengfu;

import com.oupeng.joke.spider.domain.Comment;
import com.oupeng.joke.spider.domain.JokeImg;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * Created by zongchao on 2017/3/13.
 */

@TargetUrl("http://www.pengfu.com/content_\\d{6,9}_1.html")
@HelpUrl("http://www.pengfu.com/shen_30_\\d{1,3}.html")
public class JokeImgPeng extends JokeImg {

    private Integer id;

    @ExtractBy("//dl[@class='clearfix dl-con']//h1/text()")
    private String title;

    @ExtractBy(value = "//div[@class='content-txt pt10']/img/@src", notNull = true)
    private String img;

    private String gif;

    @ExtractBy("//div[@class='content-txt pt10']/img/@width")
    private Integer width;


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
    private String src;

    @ExtractBy("//div[@class='list-item bg1 b1 boxshadow']/@id")
    private Integer srcId;
    /**
     * 评论内容
     */

    @ExtractBy("//div[@class='shenhf-con']/text()")
    private String commentContent;

    /**
     * 用户头像URL
     */

    private String avata;
    /**
     * 神评点赞数大于10
     */

    @ExtractBy("//span[@class='shf-comment-ding fr none']//i/text()")
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

    public Integer getSrcId() {
        return srcId;
    }

    public void setSrcId(Integer srcId) {
        this.srcId = srcId;
    }

    public String getSrc() {
        src = "http://www.pengfu.com/content_" + srcId + "_1.html";
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Integer getSourceId() {
        return 146;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

}

