package com.oupeng.joke.spider.domain.xiha;


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
@TargetUrl("http://www.xxhh.com/content/\\d{6,9}.html")
@HelpUrl("http://www.xxhh.com/duanzi/page/\\d{1,2}")
public class JokeTextXiha extends JokeText implements AfterExtractor {

    @ExtractBy(value = "//div[@class='article']/pre/allText()", notNull = true)
    private String content;

    @ExtractBy(value = "//div[@class='article']/img/@src")
    private String imgsrc;

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
    @ExtractBy("//div[@class='comment-list-reply']/p/allText()")
    private String commentContent;

    /**
     * 神评点赞数大于10
     */
    @ExtractBy("//div[@class='comment-list-action']/a/span/text()")
    private Integer agreeTotal;


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
        return 147;
    }


    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    @Override
    public void afterProcess(Page page) {
        src = page.getUrl().toString();
        if(StringUtils.isNotBlank(imgsrc)){
            page.setSkip(true);
        }
    }
}

