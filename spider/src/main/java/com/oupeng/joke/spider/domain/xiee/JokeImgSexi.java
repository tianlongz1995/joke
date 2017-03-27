package com.oupeng.joke.spider.domain.xiee;

import com.oupeng.joke.spider.domain.JokeImg;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * Created by zongchao on 2017/3/13.
 */
@TargetUrl("http://www.mhkkm.com/sexi/\\d{4}/\\d{4}/\\d{4}.html")
@HelpUrl("http://www.mhkkm.com/sexi/list_44_d{1,2}.html")
public class JokeImgSexi extends JokeImg implements AfterExtractor {

    @ExtractBy("//h1[@class='mhtitle yahei']/text()")
    private String title;

    @ExtractBy(value = "//ul[@class='mnlt']//img/@src", notNull = true)
    private String img;

    /**
     * 来源
     */
    private String src;
    /**
     * 评论内容
     */


    @ExtractBy("//section[@class='post-comments hot-comments']//ul/li/div[@class='text']/text()")
    private String commentContent;

    /**
     * 神评点赞数大于10
     */

    @ExtractBy("//section[@class='post-comments hot-comments']//p/span/em/text()")
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
        return 150;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public void afterProcess(Page page) {
        src = page.getUrl().toString();
    }
}

