package com.oupeng.joke.spider.domain.pengfu;

import com.oupeng.joke.spider.domain.Comment;
import com.oupeng.joke.spider.domain.JokeImg;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * Created by zongchao on 2017/3/13.
 */

@TargetUrl("http://www.pengfu.com/content_\\d{6,9}_1.html")
@HelpUrl("http://www.pengfu.com/shen_30_\\d{1,3}.html")
public class JokeImgPeng extends JokeImg implements AfterExtractor {


    @ExtractBy("//dl[@class='clearfix dl-con']//h1/text()")
    private String title;

    @ExtractBy(value = "//div[@class='content-txt pt10']/img/@src", notNull = true)
    private String img;

    /**
     * 来源
     */
    private String src;

    /**
     * 评论内容
     */

    @ExtractBy("//div[@class='shenhf-con']/text()")
    private String commentContent;

    /**
     * 神评点赞数大于10
     */

    @ExtractBy("//span[@class='shf-comment-ding fr none']//i/text()")
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
        return 146;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public void afterProcess(Page page) {
        src = page.getUrl().toString();
    }
}

