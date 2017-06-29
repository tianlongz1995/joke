package com.oupeng.joke.spider.domain.quishibaike;


import com.oupeng.joke.spider.domain.JokeText;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;


/**
 * Created by zongchao on 2017/3/13.
 */
@TargetUrl("https://www.qiushibaike.com/article/\\d+")
@HelpUrl("https://www.qiushibaike.com/8hr/page/\\d+/?s=4993143")
public class JokeTextHot extends JokeText implements AfterExtractor {


    private String title;

    @ExtractBy(value = "//div[@class='content']/allText()", notNull = true)
    private String content;

    /**
     * 来源
     */
    private String src;


    /**
     * 内容源
     */
    private Integer sourceId;

    /**
     * 评论内容
     */
    @ExtractBy("//div[@class='comments-list-item']/allText()")
    private String commentContent;

    /**
     * 神评点赞数大于10
     */
    @ExtractBy("//span[@class='shf-comment-ding fr none']//i/text()")
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


    public Integer getSourceId() {
        return 136;
    }


    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public void afterProcess(Page page) {
        src = page.getUrl().toString();
    }
}

