package com.oupeng.joke.spider.domain.mahua;

import com.oupeng.joke.spider.domain.Comment;
import com.oupeng.joke.spider.domain.JokeImg;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * Created by zongchao on 2017/3/13.
 */
@TargetUrl("http://www.mahua.com/xiaohua/\\d{5,8}.htm")
@HelpUrl("http://www.mahua.com/newjokes/\\w+_\\d{1,3}.htm")
public class JokeImgHua extends JokeImg {



    @ExtractBy("////h1[@class='joke-title']/text()")
    private String title;

    @ExtractBy(value = "//div[@class='joke-content']/img/@src", notNull = true)
    private String img;

    private String gif;
    @ExtractBy("//div[@class='joke-content']/img/@width")

    private Integer width;
    @ExtractBy("//div[@class='joke-content']/img/@height")
    private Integer height;

    /**
     * 来源
     */

    @ExtractBy("//link[@rel='canonical']/@href")
    private String src;
    /**
     * 评论内容
     */
    @ExtractBy("//ul[@class='comment-list']/li/div[@class='comment-content']/p[2]/text()")
    private String commentContent;
    /**
     * 神评点赞数大于10
     */
    @ExtractBy("//ul[@class='comment-list']/li/div[@class='comment-ding']/a[@class='comment-ding-icon']/text()")
    private Integer agreeTotal;

    private Integer sourceId;




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
        return 144;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }


}

