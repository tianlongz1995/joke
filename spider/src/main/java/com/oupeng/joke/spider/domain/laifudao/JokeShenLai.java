package com.oupeng.joke.spider.domain.laifudao;

import com.oupeng.joke.spider.domain.JokeText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.List;

/**
 */
@TargetUrl("http://www.laifudao.com/wangwen/\\d{5,8}.htm")
@HelpUrl("http://www.laifudao.com/shenhuifu.htm")
public class JokeShenLai extends JokeText implements AfterExtractor{

    /**
     * 标题
     */
    @ExtractBy("//header[@class='post-header']//a/allText()")
    private String title;
    /**
     * 内容
     */
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
     * 评论数量
     */
    private Integer commentNumber;
    /**
     * 评论点赞列表
     */
    @ExtractBy("//section[@class=\"post-comments general-comments\"]//p/span/em/text()")
    private List<Integer> hotGoods;
    /**
     * 评论内容列表
     */
    @ExtractBy("//section[@class=\"post-comments general-comments\"]//div[@class=\"text\"]/text()")
    private List<String> hotContents;


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

    @Override
    public Integer getCommentNumber() {
        return commentNumber;
    }

    @Override
    public void setCommentNumber(Integer commentNumber) {
        this.commentNumber = commentNumber;
    }

    @Override
    public List<Integer> getHotGoods() {
        return hotGoods;
    }

    @Override
    public void setHotGoods(List<Integer> hotGoods) {
        this.hotGoods = hotGoods;
    }

    @Override
    public List<String> getHotContents() {
        return hotContents;
    }

    @Override
    public void setHotContents(List<String> hotContents) {
        this.hotContents = hotContents;
    }

    @Override
    public void afterProcess(Page page) {
        //抓取到的神评数量
        if (!CollectionUtils.isEmpty(this.getHotGoods()) && !CollectionUtils.isEmpty(this.getHotContents())) {
            this.setCommentNumber(this.getHotGoods().size());
        } else {
            this.setCommentNumber(0);
        }
    }
}

