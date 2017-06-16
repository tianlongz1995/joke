package com.oupeng.joke.spider.domain.neihan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oupeng.joke.spider.domain.JokeImg;
import com.oupeng.joke.spider.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeluo on 2017/6/14.
 */

@TargetUrl("http://neihanshequ.com/p6\\d+/")
@HelpUrl("http://neihanshequ.com/pic/")
public class JokeImgNeiHanJS extends JokeImg implements AfterExtractor {

    private static final Logger logger = LoggerFactory.getLogger(JokeImgNeiHanJS.class);

    private static final int new_sourceId = 155;

    @ExtractBy(value = "//h1[@class=\"title\"]/p/text()")
    private String title;

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

        /**
         *
         * (内涵段子图片)热门评论由JS渲染
         * 解决方案：请求json URL,获取json数据
         *
         * page URL：http://neihanshequ.com/p(--61863627690--)/
         * json URL：http://neihanshequ.com/m/api/get_essay_comments/?group_id=(--61863627690--)&app_name=neihanshequ_web&offset=0
         *
         * 从pageURL中截取出6186362769拼接到jsonURL
         */

        String str = pageURL.substring("http://neihanshequ.com/p".length(), pageURL.length() - 1);
        String jsonURL = "http://neihanshequ.com/m/api/get_essay_comments/?group_id=" + str + "&app_name=neihanshequ_web&offset=0";


        JSONObject retjson = HttpUtil.getAllJsonMsg(jsonURL);
        JSONObject data = (JSONObject) retjson.get("data");
        JSONArray array = data.getJSONArray("top_comments");

        List<Integer> goods = new ArrayList<Integer>();
        List<String> contents = new ArrayList<String>();
        for (int i = 0; i < array.size(); i++) {

            JSONObject obj = array.getJSONObject(i);
            String good = obj.getString("digg_count").trim();
            String content = obj.getString("text").trim();

            //为空或者不存在
            if (good == null || good.length() < 1 || content == null || content.length() < 1) {
                continue;
            }

            //判断good能否转化为Integer
            int god = 0;
            try {
                god = Integer.parseInt(good);
            } catch (NumberFormatException e) {
                god = 0;
            }

            //非神评论
            if (Integer.valueOf(good) <= 10) {
                continue;
            }

            goods.add(god);
            contents.add(content);
        }
        this.setHotGoods(goods);
        this.setHotContents(contents);


        //抓取到的神评数量
        if (this.getHotGoods() != null) {
            this.setCommentNumber(this.getHotGoods().size());
        } else {
            this.setCommentNumber(0);
        }

        logger.info("爬取内涵段子["+pageURL+"]神评论数量: "+this.getCommentNumber());

    }
}


