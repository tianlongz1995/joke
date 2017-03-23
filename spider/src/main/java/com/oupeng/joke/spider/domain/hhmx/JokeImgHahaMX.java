package com.oupeng.joke.spider.domain.hhmx;

import com.oupeng.joke.spider.domain.Comment;
import com.oupeng.joke.spider.domain.JokeImg;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * Created by zongchao on 2017/3/13.
 */
@TargetUrl("http://www.haha.mx/joke/\\d{6,9}")
@HelpUrl("http://www.haha.mx/topic/16/new/\\d{1,2}")
public class JokeImgHahaMX extends JokeImg {

    @ExtractBy(value = "//p[@class='word-wrap joke-main-content-text']/text()", notNull = true)
    private String title;

    @ExtractBy(value = "//img[@class='joke-main-content-img']/@src", notNull = true)
    private String img;

    /**
     * 来源
     */
    @ExtractBy("//link[@rel='canonical']/@href")
    private String src;
    /**
     * 评论内容
     */


    @ExtractBy("//div[@class='joke-comment_content']/text()")
    private String commentContent;

    /**
     * 神评点赞数大于10
     */

    @ExtractBy("//div[@class='joke-comment_header-buttons-light']/text()")
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
        return 148;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

}
