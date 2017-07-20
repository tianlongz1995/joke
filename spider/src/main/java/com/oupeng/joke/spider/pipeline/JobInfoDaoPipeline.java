package com.oupeng.joke.spider.pipeline;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.spider.domain.*;
import com.oupeng.joke.spider.mapper.UserDao;
import com.oupeng.joke.spider.producer.KafkaProducer;
import com.oupeng.joke.spider.service.URLBloomFilterService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;


import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by xiongyingl on 2017/6/16.
 */
@Component("JobInfoDaoPipeline")
public class JobInfoDaoPipeline implements PageModelPipeline<JokeText> {

    private static final Logger logger = LoggerFactory.getLogger(JobInfoDaoPipeline.class);

    //无效字符串
    private static final String[] SEARCH = {"　", "&quot;", "&rdquo;", "<br />", "\n", "&hellip;", "&middot;"};
    //替换字符串
    private static final String[] REPLACE = {"", "", "", "", "", "", ""};

    private Random random = new Random(3000);
    private String avataStr = "http://joke2.oupeng.com/comment/images/%d.png";
    private int maxCrawlPage = 300;
    private int txtLimitLength = 200;

    @Autowired
    private UserDao userDao;

    @Autowired
    private URLBloomFilterService urlBloomFilterService;

    @Autowired
    private Environment env;

    @Autowired
    private KafkaProducer kafkaProducer;

    @PostConstruct
    public void init() {
        String maxCrawl = env.getProperty("max.crawl.page");
        if (StringUtils.isNumeric(maxCrawl)) {
            maxCrawlPage = Integer.valueOf(maxCrawl);
        }
        String n = env.getProperty("nick");
        if (StringUtils.isNotBlank(n)) {
            avataStr = n;
        }
        String length = env.getProperty("spider.text.limit.length");
        if (StringUtils.isNumeric(length)) {
            txtLimitLength = Integer.valueOf(length);
        }
    }

    @Override
    public void process(JokeText jokeText, Task task) {
        long pageCount = ((Spider) task).getPageCount();
        logger.info("段子 - 当前抓取页数:{}", pageCount);
        if (pageCount > maxCrawlPage) {
            ((Spider) task).stop();
            logger.info("段子 - 最大抓取总页数:{} , 当前抓取总页数:{}", maxCrawlPage, pageCount);
        }
        //过滤无效字符
        String cont = StringUtils.replaceEach(jokeText.getContent(), SEARCH, REPLACE);
        //字数小于txtLength
        boolean isLessLimit = cont.length() < txtLimitLength ? true : false;

        if (!urlBloomFilterService.contains(jokeText.getSrc()) && isLessLimit) {

            Joke joke = new Joke();
            joke.setSource(jokeText.getSrc());
            joke.setTitle(jokeText.getTitle());
            joke.setSource_id(jokeText.getSourceId());
            joke.setContent(cont);
            joke.setStatus(0);

            int bad = 150 - (int) (Math.random() * 150);
            int good = 500 + (int) (Math.random() * 500);
            joke.setBad(bad);
            joke.setGood(good);

            //发布人
            int rid = random.nextInt(2089);
            if (rid == 0) {
                rid = rid + 1; //User表中id范围为[1,2089]
            }
            User ru = userDao.select(rid);
            joke.setReleaseNick(ru.getNickname());
            int riconid = rid % 20 + 1;
            String ravata = avataStr.replace("%d", String.valueOf(riconid));
            joke.setReleaseAvata(ravata);

            //存在神评论
            if (jokeText.getCommentNumber() != null && jokeText.getCommentNumber() > 0) {
                //记录最大点赞数神评信息
                int m_good = 0;
                String m_comment = null, m_avata = null, m_nick = null;

                //添加神评论: hotGoods--hotContents 神评点赞数和内容一一对应
                List<CommentT> list = new ArrayList<CommentT>();
                for (int i = 0; i < jokeText.getCommentNumber(); i++) {

                    Integer god = Integer.valueOf(String.valueOf(jokeText.getHotGoods().get(i)));
                    if(god == null){
                        god = 0;
                    }
                    String content = jokeText.getHotContents().get(i);

                    //神评评论点赞数>10
                    if (god <= 10) {
                        continue;
                    }
                    //过滤无效字符
                    content = StringUtils.replaceEach(content, SEARCH, REPLACE);
                    //字数小于txtLength
                    isLessLimit = content.length() > txtLimitLength ? true : false;
                    if(isLessLimit){
                        continue;
                    }

                    int id = random.nextInt(2089);
                    if (id == 0) {
                        id = id + 1; //User表中id范围为[1,2089]
                    }
                    User u = userDao.select(id);
                    int last = u.getLast() + 1;
                    userDao.update(last, id);
                    String nick = StringUtils.trim(u.getNickname()) + Integer.toHexString(last);
                    int uid = id * 10000 + last;
                    int iconid = id % 20 + 1;
                    String avata = avataStr.replace("%d", String.valueOf(iconid));

                    //记录最大点赞数的评论
                    if (god > m_good) {
                        m_good = god;
                        m_comment = content;
                        m_avata = avata;
                        m_nick = nick;
                    }

                    CommentT com = new CommentT();
                    com.setUid(uid);
                    com.setNickname(nick);
                    com.setContent(content);
                    com.setAvatar(avata);
                    com.setGood(god);

                    list.add(com);
                }
                //将list中点赞数最大的神评放到最前面
                if (list.size() > 1) {
                    int k = 0;
                    int kgood = list.get(0).getGood();
                    for (int i = 1; i < list.size(); i++) {
                        CommentT com = list.get(i);
                        if (com.getGood() > kgood) {
                            k = i;
                            kgood = com.getGood();
                        }

                    }
                    //交换0,k
                    if (k != 0) {
                        CommentT com0 = list.get(0);
                        CommentT comk = list.get(k);
                        list.set(0, comk);
                        list.set(k, com0);
                    }
                }
                joke.setComment_number(list.size());
                joke.setComment(list);

                joke.setAvata(m_avata);
                joke.setNick(m_nick);

            } else {
                joke.setComment(null);
                joke.setComment_number(0);
            }
            logger.info("爬取joke(kafaka)src:{},神评数量:{}", joke.getSource(), joke.getComment_number());

            //  与kafka服务端进行接口对接
            String message = JSON.toJSON(joke).toString();
            kafkaProducer.sendMessage(message);
            logger.info("text段子 - 向kafka服务端发送数据:{}", message);

            urlBloomFilterService.add(jokeText.getSrc());
            logger.info("段子 - 处理页面结束:{}", jokeText.getSrc());
        }
    }

}
