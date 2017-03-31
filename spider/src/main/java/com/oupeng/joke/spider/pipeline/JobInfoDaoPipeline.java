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
import java.util.Random;

/**
 * Created by zongchao on 2017/3/8.
 */
@Component("JobInfoDaoPipeline")
public class JobInfoDaoPipeline implements PageModelPipeline<JokeText> {

    private static final Logger logger = LoggerFactory.getLogger(JobInfoDaoPipeline.class);

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
        //过滤空格
        String content = StringUtils.replace(jokeText.getContent(), "　", "");
        //字数小于txtLength
        boolean isLessLimit = content.length() < txtLimitLength ? true : false;
        if (!urlBloomFilterService.contains(jokeText.getSrc()) && isLessLimit) {
            Joke joke = new Joke();
            int bad = 150 - (int) (Math.random() * 150);
            int good = 500 + (int) (Math.random() * 500);
            joke.setBad(bad);
            joke.setGood(good);
            joke.setSource(jokeText.getSrc());
            joke.setTitle(jokeText.getTitle());
            joke.setSource_id(jokeText.getSourceId());
            joke.setStatus(0);
            joke.setContent(content);

            //发布人
            int rid = random.nextInt(2089);
            User ru = userDao.select(rid);
            joke.setReleaseNick(ru.getNickname());
            int riconid = rid % 20 + 1;
            String ravata = avataStr.replace("%d", String.valueOf(riconid));
            joke.setReleaseAvata(ravata);

            if (jokeText.getAgreeTotal() != null && jokeText.getAgreeTotal() > 10) {
                //评论
                int id = rid + 1;
                User u = userDao.select(id);
                int last = u.getLast() + 1;
                userDao.update(last, id);
                String nick = StringUtils.trim(u.getNickname()) + Integer.toHexString(last);
                joke.setNick(nick);
                int uid = id * 10000 + last;
                int iconid = id % 20 + 1;
                String avata = avataStr.replace("%d", String.valueOf(iconid));
                joke.setAvata(avata);

                //添加评论
                CommentT com = new CommentT();
                com.setUid(uid);
                com.setNickname(nick);
                com.setContent(jokeText.getCommentContent());
                com.setAvata(avata);
                com.setGood(jokeText.getAgreeTotal());
                joke.setComment(com);
            } else {
                joke.setComment(null);
            }
            String message = JSON.toJSON(joke).toString();
          //  kafkaProducer.sendMessage(message);
            urlBloomFilterService.add(jokeText.getSrc());
            logger.info("段子 - 处理页面结束:{}", jokeText.getSrc());
        }
    }

}
