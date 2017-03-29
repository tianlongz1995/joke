package com.oupeng.joke.spider.domain.hhmx;


import com.oupeng.joke.spider.domain.Comment;
import com.oupeng.joke.spider.domain.JokeText;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;


/**
 * Created by zongchao on 2017/3/13.
 */
@TargetUrl("http://www.haha.mx/joke/\\d{6,9}")
@HelpUrl("http://www.haha.mx/topic/13/new/\\d{1,2}")
public class JokeTextHahaMX extends JokeText implements AfterExtractor {

    @ExtractBy(value = "//p[@class='word-wrap joke-main-content-text']/allText()", notNull = true)
    private String content;

    /**
     * 来源
     */
    @ExtractBy("//link[@rel='canonical']/@href")
    private String src;

    /**
     * 内容源
     */
    private Integer sourceId;

    /**
     * 评论内容
     */
    @ExtractBy("//div[@class='joke-comment_content']/allText()")
    private String commentContent;

    /**
     * 神评点赞数大于10
     */
    @ExtractBy("//div[@class='joke-comment_header-buttons-light']/text()")
    private Integer agreeTotal;


    @ExtractBy("//img[@class='joke-main-content-img']/@src")
    private String img;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public void afterProcess(Page page) {
        if (img == null || img.length() <= 0) {
            page.setSkip(true);
        }
    }
}

