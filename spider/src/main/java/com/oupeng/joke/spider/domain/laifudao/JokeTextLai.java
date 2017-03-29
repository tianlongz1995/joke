package com.oupeng.joke.spider.domain.laifudao;


import com.oupeng.joke.spider.domain.Comment;
import com.oupeng.joke.spider.domain.JokeText;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;


/**
 * Created by zongchao on 2017/3/13.
 */
@TargetUrl("http://www.laifudao.com/wangwen/\\d{5,8}.htm")
@HelpUrl("http://www.laifudao.com/wangwen/\\w+_\\d{1,3}.htm")
public class JokeTextLai extends JokeText {

    @ExtractBy("//header[@class='post-header']//a/allText()")
    private String title;
    @ExtractBy(value = "//div[@class='post-content stickem-container']//p/allText()", notNull = true)
    private String content;


    /**
     * 来源
     */
    @ExtractBy("//header[@class='clearfix content-header breadcrumbs']//a[4]/@href")
    private String src;

    /**
     * 内容源
     */
    private Integer sourceId;

    /**
     * 评论内容
     */
    @ExtractBy("//section[@class='post-comments hot-comments']//ul/li/div[@class='text']/allText()")
    private String commentContent;

    /**
     * 神评点赞数大于10
     */
    @ExtractBy("//section[@class='post-comments hot-comments']//p/span/em/text()")
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
        return 141;
    }


    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }
}

