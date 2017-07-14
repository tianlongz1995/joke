package com.oupeng.joke.spider.domain.neihan;

import com.oupeng.joke.spider.domain.JokeImg;
import com.oupeng.joke.spider.utils.HttpUtil;
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
 * Created by xiongyingl on 2017/6/14.
 */

@TargetUrl("http://neihanshequ.com/p6\\d+/")
@HelpUrl("http://neihanshequ.com/pic/")
public class JokeImgNeiHanJS extends JokeImg implements AfterExtractor {

    private static final int new_sourceId = 155;
    /**
     * 标题
     */
    @ExtractBy(value = "//h1[@class=\"title\"]/p/text()")
    private String title;
    /**
     * 图片地址
     */
    @ExtractBy(value = "//img[@id=\"groupImage\"]/@src", notNull = true)
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
     * 评论点赞
     */
    private List<Integer> hotGoods;
    /**
     * 评论内容
     */
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    @Override
    public void afterProcess(Page page) {

        String pageURL = page.getUrl().toString();
        this.setSrc(pageURL);

        String str = pageURL.substring("http://neihanshequ.com/p".length(), pageURL.length() - 1);
        String jsonURL = "http://neihanshequ.com/m/api/get_essay_comments/?group_id=" + str + "&app_name=neihanshequ_web&offset=0";

        Map<String, List> map = HttpUtil.getNeiHanGodMsg(jsonURL);
        if (CollectionUtils.isEmpty(map) || CollectionUtils.isEmpty(map.get("hotGoods")) || CollectionUtils.isEmpty(map.get("hotContents"))) {
            this.setCommentNumber(0);
        } else {
            this.setHotGoods(map.get("hotGoods"));
            this.setHotContents(map.get("hotContents"));

            //抓取到的神评数量
            if (this.getHotGoods() != null) {
                this.setCommentNumber(this.getHotGoods().size());
            } else {
                this.setCommentNumber(0);
            }
        }
    }
}


