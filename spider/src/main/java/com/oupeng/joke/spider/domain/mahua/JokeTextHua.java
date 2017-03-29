package com.oupeng.joke.spider.domain.mahua;


import com.oupeng.joke.spider.domain.Comment;
import com.oupeng.joke.spider.domain.JokeText;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;


/**
 * Created by zongchao on 2017/3/13.
 */
@TargetUrl("http://www.mahua.com/xiaohua/\\d{5,8}.htm")
@HelpUrl("http://www.mahua.com/newjokes/\\w+_\\d{1,3}.htm")
public class JokeTextHua extends JokeText {

    @ExtractBy("//h1[@class='joke-title']/allText()")
    private String title;

    @ExtractBy(value = "//dl[@joke-type='text']/text()", notNull = true)
    private String jokeType;

    @ExtractBy("//div[@class='joke-content']/allText()")
    private String content;
    /**
     * 来源
     */
    @ExtractBy("//link[@rel='canonical']/@href")
    private String src;

    /**
     * 内容源id
     */
    private Integer sourceId;

    /**
     * 评论内容
     */
    @ExtractBy("//ul[@class='comment-list']/li/div[@class='comment-content']/p[2]/allText()")
    private String commentContent;

    /**
     * 神评点赞数大于10
     */
    @ExtractBy("//ul[@class='comment-list']/li/div[@class='comment-ding']/a[@class='comment-ding-icon']/text()")
    private Integer agreeTotal;




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

    public String getJokeType() {
        return jokeType;
    }

    public void setJokeType(String jokeType) {
        this.jokeType = jokeType;
    }

    public Integer getSourceId() {
        return 144;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }
}

