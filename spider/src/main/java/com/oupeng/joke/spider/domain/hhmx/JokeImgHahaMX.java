package com.oupeng.joke.spider.domain.hhmx;

import com.oupeng.joke.spider.domain.Comment;
import com.oupeng.joke.spider.domain.JokeImg;
import com.oupeng.joke.spider.domain.neihan.JokeImgNeiHanJS;
import com.oupeng.joke.spider.utils.HttpUtil;
import com.oupeng.joke.spider.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.List;
import java.util.Map;

/**
 * Created by zongchao on 2017/3/13.
 */
@TargetUrl("http://www.haha.mx/joke/\\d{6,9}")
@HelpUrl("http://www.haha.mx/topic/16/new/\\d{1,2}")
public class JokeImgHahaMX extends JokeImg implements AfterExtractor{

    private static final Logger logger = LoggerFactory.getLogger(JokeImgHahaMX.class);

    @ExtractBy(value = "//p[@class='word-wrap joke-main-content-text']/allText()", notNull = true)
    private String title;

    @ExtractBy(value = "//img[@class='joke-main-content-img']/@src", notNull = true)
    private String img;

    /**
     * 来源
     */
    @ExtractBy("//link[@rel='canonical']/@href")
    private String src;

    /**
     * 评论数量
     */
    private Integer commentNumber;
    /**
     * 评论列表
     */
    private List<Integer> hotGoods;

    private List<String> hotContents;


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

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Integer getSourceId() {
        return 148;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public void afterProcess(Page page) {

        String pageURL = this.getSrc();
        String str;
        if(pageURL.endsWith("/")){
            str = pageURL.substring("http://www.haha.mx/joke/".length(), pageURL.length() - 1);
        }else{
            str = pageURL.substring("http://www.haha.mx/joke/".length(), pageURL.length());
        }
        String jsonURL = "http://www.haha.mx/mobile_read_api.php?r=mobile_comment&jid="+str+"&page=1&offset=10&order=light";

        Map<String, List> map = HttpUtil.getHHMXGodMsg(jsonURL);
        if(CollectionUtils.isEmpty(map)){
            page.setSkip(true);
        }
        if(CollectionUtils.isEmpty(map.get("hotGoods"))||CollectionUtils.isEmpty(map.get("hotContents"))){
            page.setSkip(true);
        }
        this.setHotGoods(map.get("hotGoods"));
        this.setHotContents(map.get("hotContents"));

        //抓取到的神评数量
        if (this.getHotGoods() != null) {
            this.setCommentNumber(this.getHotGoods().size());
        } else {
            this.setCommentNumber(0);
        }

        logger.info("爬取遨游哈哈[" + pageURL + "]神评论数量: " + this.getCommentNumber());
    }

}

