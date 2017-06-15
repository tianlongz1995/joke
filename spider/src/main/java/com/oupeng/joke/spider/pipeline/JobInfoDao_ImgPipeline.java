package com.oupeng.joke.spider.pipeline;

import com.oupeng.joke.spider.domain.Comment;
import com.oupeng.joke.spider.domain.ImageDto;
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
 * Created by xiongyingl on 2017/6/14.
 */


@Component("JobInfoDao_ImgPipeline")
public class JobInfoDao_ImgPipeline implements PageModelPipeline<JokeImg> {

    private static final Logger logger = LoggerFactory.getLogger(JobInfoDao_ImgPipeline.class);

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
            ImageDto img = handleImage.downloadImg(jokeImg.getImg());
            String imgurl = img.getImgUrl();
            if (StringUtils.isNotBlank(imgurl)) {
                if (("gif").equalsIgnoreCase(FilenameUtils.getExtension(jokeImg.getImg()))) {
                    jokeImg.setGif(FilenameUtils.getFullPath(imgurl) + FilenameUtils.getBaseName(imgurl) + ".gif");
                    jokeImg.setType(2);
                } else {
                    jokeImg.setType(1);
                }
                jokeImg.setImg(imgurl);
                jokeImg.setWidth(img.getWidth());
                jokeImg.setHeight(img.getHeight());
                //点赞数和点踩数
                int bad = 150 - (int) (Math.random() * 150);
                int good = 500 + (int) (Math.random() * 500);
                jokeImg.setBad(bad);
                jokeImg.setGood(good);

                /**
                 *   ReleaseNick  发布段子用户昵称
                 *   ReleaseAvata 发布段子用户头像url
                 */
                //发布人
                int rid = random.nextInt(2089);
                User ru = userDao.select(rid);
                jokeImg.setReleaseNick(ru.getNickname());

                int riconid = rid % 20 + 1;
                String ravata = avataStr.replace("%d", String.valueOf(riconid));
                jokeImg.setReleaseAvata(ravata);

                //存在神评论
                if (jokeImg.getCommentNumber() != null && jokeImg.getCommentNumber() > 0) {

                    jobInfoDao.addImg(jokeImg);
                    //获得sid
                    int sid = jobInfoDao.getLastId(jokeImg.getImg());

                    /**
                     *  添加神评论: hotGoods--hotContents 神评点赞数和内容一一对应
                     */
                    for (int i = 0; i < jokeImg.getCommentNumber(); i++) {

                        int god = jokeImg.getHotGoods().get(i);
                        String content = jokeImg.getHotContents().get(i);

                        //int id = rid + 1;
                        int id = random.nextInt(2089);

                        User u = userDao.select(id);
                        int last = u.getLast() + 1;
                        userDao.update(last, id);
                        String nick = StringUtils.trim(u.getNickname()) + Integer.toHexString(last);
                        int uid = id * 10000 + last;
                        int iconid = id % 20 + 1;
                        String avata = avataStr.replace("%d", String.valueOf(iconid));

                        Comment com = new Comment();
                        com.setSid(sid);
                        com.setUid(uid);
                        com.setNickname(nick);
                        com.setContent(content);
                        com.setAvata(avata);
                        com.setGood(god);
                        commentDao.addComment(com);

                        System.out.println("insert the " + (i + 1) + "th hot comment");
                    }

                } else {
                    jokeImg.setCommentNumber(0);
                    jobInfoDao.addImg(jokeImg);
                }
            }

            /**
             *  INSERT DISPLAY
             */
            System.out.println("INSERT:" + jokeImg.getSrc());

            urlFilter.add(jokeImg.getSrc());
            logger.info("图片 - 处理页面结束:{}", jokeImg.getSrc());
        }
    }
}

