package com.oupeng.joke.spider.domain.quishibaike;

import com.oupeng.joke.spider.domain.JokeText;
import com.oupeng.joke.spider.utils.HttpUtil;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.List;
import java.util.Map;

@TargetUrl("https://www.qiushibaike.com/article/\\d+")
@HelpUrl("https://www.qiushibaike.com/text/|https://www.qiushibaike.com/text/page/\\d+/?s=5001145")
public class JokeTextHot extends JokeText implements AfterExtractor {

    private static final int new_sourceId = 136;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    @ExtractBy(value = "//div[@id='single-next-link']/div[@class='content']/text()", notNull = true)
    private String content;
    /**
     * 图片跳过
     */
    @ExtractBy(value = "//div[@id='single-next-link']/div[@class='thumb']/img/@src")
    private String img;
    /**
     * video跳过
     */
    @ExtractBy(value = "//div[@id='single-next-link']/video/source/@src")
    private String video;
    /**
     * 来源
     */
    private String src;
    /**
     * 数据源
     */
    private Integer sourceId;
    /**
     * 评论数量
     */
    private Integer commentNumber;
    /**
     * 评论点赞
     */
    @ExtractBy("//article[@id=\"godCmt\"]//div[@class=\"comments-table\"]/a//div[@class=\"likenum\"]/allText()")
    private List<Integer> hotGoods;
    /**
     * 评论内容
     */
    @ExtractBy("//article[@id=\"godCmt\"]//div[@class=\"comments-table\"]/a//div[@class=\"main-text\"]/text()")
    private List<String> hotContents;


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

    public void setHotGoods(List<Integer> hotGoods) {
        this.hotGoods = hotGoods;
    }

    public List<String> getHotContents() {
        return hotContents;
    }

    public void setHotContents(List<String> hotContents) {
        this.hotContents = hotContents;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public Integer getSourceId() {
        return new_sourceId;
    }

    @Override
    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    @Override
    public void afterProcess(Page page) {
        src = page.getUrl().toString();
        if (img != null || video != null) {
            page.setSkip(true);
        }

        //抓取到的神评数量
        if (!CollectionUtils.isEmpty(this.getHotGoods()) && !CollectionUtils.isEmpty(this.getHotContents())) {
            this.setCommentNumber(this.getHotGoods().size());
        } else {
            this.setCommentNumber(0);
        }
    }
}

