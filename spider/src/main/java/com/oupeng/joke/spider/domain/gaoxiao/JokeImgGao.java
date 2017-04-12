package com.oupeng.joke.spider.domain.gaoxiao;

import com.oupeng.joke.spider.domain.JokeImg;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * 搞笑gif 推荐
 * Created by zongchao on 2017/4/11.
 */
@TargetUrl("http://www.gaoxiaogif.com/\\w+/\\d{4,5}.html")
@HelpUrl("http://www.gaoxiaogif.com/index_\\d{1,2}.html")
public class JokeImgGao extends JokeImg implements AfterExtractor {


    @ExtractBy(value = "//h1/allText()", notNull = true)
    private String title;

    @ExtractBy(value = "//div[@class='listgif-giftu content_pic']/p/img/@src", notNull = true)
    private String img;
    @ExtractBy(value = "//div[@class='listgif-giftu content_pic']/p[3]/img/@src")
    private String moreImg;

    /**
     * 来源
     */
    private String src;
    /**
     * 评论内容
     */
    @ExtractBy("//div[@class='cont-msg-gw']//p/span/text()")
    private String commentContent;

    /**
     * 神评点赞数大于10
     */
    @ExtractBy("//div[@class='cont-msg-gw']//span[@node-type='support']//em/text()")
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
        return 152;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public void afterProcess(Page page) {
        if (StringUtils.isNotBlank(moreImg)) {
            page.setSkip(true);
        }
        src = page.getUrl().toString();
    }
}

