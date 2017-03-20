package com.oupeng.joke.spider.pipeline;

import com.oupeng.joke.spider.domain.Comment;
import com.oupeng.joke.spider.domain.JokeImg;
import com.oupeng.joke.spider.domain.User;
import com.oupeng.joke.spider.mapper.CommentDao;
import com.oupeng.joke.spider.mapper.JobInfoDao;
import com.oupeng.joke.spider.mapper.UserDao;
import com.oupeng.joke.spider.service.HandleImage;
import com.oupeng.joke.spider.service.ImgBloomFilterService;
import com.oupeng.joke.spider.service.URLBloomFilterService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import javax.annotation.PostConstruct;
import java.util.Random;

/**
 * Created by zongchao on 2017/3/8.
 */
@Component("JobInfoDaoImgPipeline")
public class JobInfoDaoImgPipeline implements PageModelPipeline<JokeImg> {

    private static final Logger logger = LoggerFactory.getLogger(JobInfoDaoPipeline.class);

    private Random random = new Random(3000);
    private String avataStr = "http://joke2.oupeng.com/comment/images/%d.png";
    private int maxCrawlPage = 300;

    @Autowired
    private JobInfoDao jobInfoDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private HandleImage handleImage;
    @Autowired
    private ImgBloomFilterService imgFilter;
    @Autowired
    private URLBloomFilterService urlFilter;


    @Autowired
    private Environment env;

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
    }

    @Override
    public void process(JokeImg jokeImg, Task task) {


        Long count = ((OOSpider) task).getPageCount();
        logger.info("图片 - 当前抓取页数:{}", count);
        if (count > maxCrawlPage) {
            ((OOSpider) task).stop();
            logger.info("图片 - 最大抓取总页数:{} , 当前抓取总页数:{}", maxCrawlPage, count);
        }
        if (!urlFilter.contains(jokeImg.getSrc())) {
            //处理图片
            String imgurl = handleImage.downloadImg(jokeImg.getImg());
            if (("gif").equalsIgnoreCase(FilenameUtils.getExtension(jokeImg.getImg()))) {
                jokeImg.setGif(FilenameUtils.getFullPath(imgurl) + FilenameUtils.getBaseName(imgurl) + ".gif");
                jokeImg.setType(2);
            } else {
                jokeImg.setType(1);
            }
            jokeImg.setImg(imgurl);
            //发布人
            int rid = random.nextInt(2089);
            User ru = userDao.select(rid);
            int rlast = ru.getLast() + 1;
            userDao.update(rlast, rid);
            String rnick = StringUtils.trim(ru.getNickname()) + Integer.toHexString(rlast);
            jokeImg.setReleaseNick(rnick);
            int ruid = rid * 10000 + rlast;
            int riconid = rid % 20 + 1;
            String ravata = avataStr.replace("%d", String.valueOf(riconid));
            jokeImg.setReleaseAvata(ravata);
            if (jokeImg.getAgreeTotal() != null && jokeImg.getAgreeTotal() > 10) {
                int id = rid + 1;
                User u = userDao.select(id);
                int last = u.getLast() + 1;
                userDao.update(last, id);
                String nick = StringUtils.trim(u.getNickname()) + Integer.toHexString(last);
                int uid = id * 10000 + last;
                int iconid = id % 20 + 1;
                String avata = avataStr.replace("%d", String.valueOf(iconid));
                jokeImg.setAvata(avata);
                jokeImg.setNick(nick);
                jobInfoDao.addImg(jokeImg);

                //获得sid
                int sid = jobInfoDao.getLastId();
                //添加评论
                Comment com = new Comment();
                com.setSid(sid);
                com.setUid(uid);
                com.setNickname(u.getNickname());
                com.setContent(jokeImg.getCommentContent());
                com.setAvata(avata);
                com.setGood(jokeImg.getAgreeTotal());
                commentDao.addComment(com);
            } else {
                jokeImg.setComment(null);
                jobInfoDao.addImg(jokeImg);
            }
            urlFilter.add(jokeImg.getSrc());
            logger.info("图片 - 处理页面结束:{}", jokeImg.getSrc());
        }
    }
}
