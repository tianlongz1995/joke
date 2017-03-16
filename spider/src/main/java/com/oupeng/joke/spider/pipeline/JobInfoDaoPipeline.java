package com.oupeng.joke.spider.pipeline;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.spider.domain.*;
import com.oupeng.joke.spider.mapper.UserDao;
import com.oupeng.joke.spider.producer.KafkaProducer;
import org.apache.commons.lang3.StringUtils;
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

    private Random random = new Random(3000);
    private String nick = "http://joke2.oupeng.com/comment/images/%d.png";

    @Autowired
    private UserDao userDao;

    private int maxCrawlPage = 300;

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
            nick = n;
        }
    }

    @Override
    public void process(JokeText jokeText, Task task) {
        long pageCount = ((Spider) task).getPageCount();

        if (pageCount > maxCrawlPage) {

            ((Spider) task).stop();
        }
        Joke joke = new Joke();
        joke.setSource(jokeText.getSrc());
        joke.setTitle(jokeText.getTitle());
        joke.setSource_id(jokeText.getSourceId());
        joke.setStatus(0);
        joke.setContent(jokeText.getContent());
        if (jokeText.getAgreeTotal() != null && jokeText.getAgreeTotal() > 10) {

            int id = random.nextInt(2090);
            User u = userDao.select(id);
            int last = u.getLast() + 1;

            String nick = StringUtils.trim(u.getNickname()) + Integer.toHexString(last);
            int uid = id * 10000 + last;
            int iconid = id % 20 + 1;
            String avata = nick.replace("%d", String.valueOf(iconid));

            //添加评论
            CommentT com = new CommentT();
            com.setUid(uid);
            com.setNickname(u.getNickname());
            com.setContent(jokeText.getCommentContent());
            com.setAvata(avata);
            com.setGood(jokeText.getAgreeTotal());
            joke.setComment(com);
        } else {
            joke.setComment(null);
        }
        String message = JSON.toJSON(joke).toString();
        kafkaProducer.sendMessage(message);
    }


}
