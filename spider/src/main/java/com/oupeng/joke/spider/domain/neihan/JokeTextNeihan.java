package com.oupeng.joke.spider.domain.neihan;

import com.oupeng.joke.spider.domain.JokeText;
import com.oupeng.joke.spider.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import java.util.List;
import java.util.Map;

/**
 * Created by xiongyingl on 2017/6/19.
 */

@TargetUrl("http://neihanshequ.com/p6\\d+/")
@HelpUrl("http://neihanshequ.com/")
public class JokeTextNeihan extends JokeText implements AfterExtractor {

    private static final Logger logger = LoggerFactory.getLogger(JokeTextNeihan.class);

    private static final int new_sourceId = 158;

    private String title;

    @ExtractBy(value = "//h1[@class=\"title\"]/p/text()")
    private String content;

    /**
     * 因内涵段子链接的特殊性，如果爬text时跑到了pic页面，跳过
     */
    @ExtractBy(value = "//img[@id=\"groupImage\"]/@src")
    private String img;

    /**
     * 来源
     */
    private String src;

    /**
     * 新建数据源，需要重写sourceId：new_sourceId
     */
    private Integer sourceId;


    /**
     * 评论数量
     */
    private Integer commentNumber;

    /**
     * 评论列表
     */
    private List<Integer> hotGoods;

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

    public Integer getSourceId() {
        return new_sourceId;
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

        /**
         * 因内涵段子链接的特殊性，如果爬text时跑到了pic页面，跳过
         */
        if(this.getImg()!=null){
            page.setSkip(true);
        }

        String pageURL = page.getUrl().toString();
        this.setSrc(pageURL);

        String str = pageURL.substring("http://neihanshequ.com/p".length(), pageURL.length() - 1);
        String jsonURL = "http://neihanshequ.com/m/api/get_essay_comments/?group_id=" + str + "&app_name=neihanshequ_web&offset=0";

        Map<String, List> map = HttpUtil.getGodMsg(jsonURL);
        this.setHotGoods(map.get("hotGoods"));
        this.setHotContents(map.get("hotContents"));


        //抓取到的神评数量
        if (this.getHotGoods() != null) {
            this.setCommentNumber(this.getHotGoods().size());
        } else {
            this.setCommentNumber(0);
        }

        logger.info("爬取内涵段子[" + pageURL + "]神评论数量: " + this.getCommentNumber());

    }
}


