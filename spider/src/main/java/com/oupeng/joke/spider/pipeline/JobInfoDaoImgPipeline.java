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


@Component("JobInfoDaoImgPipeline")
public class JobInfoDaoImgPipeline implements PageModelPipeline<JokeImg> {

    private static final Logger logger = LoggerFactory.getLogger(JobInfoDaoImgPipeline.class);
    //无效字符串
    private static final String[] SEARCH = {"　", "&quot;", "&rdquo;", "<br />", "\n", "&hellip;", "&middot;","\\uD83C\\uDF83","\uD83D\uDC2D"};
    //替换字符串
    private static final String[] REPLACE = {"", "", "", "", "", "", "", "", ""};
    private static String PROTOCOL = "http:";
    private Random random = new Random(3000);
    private String avataStr = "http://joke2-img.oupeng.com/1/%d.png";
    private int maxCrawlPage = 300;
    private int txtLimitLength = 200;

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
            ((OOSpider) task).close();
            logger.info("图片 - 最大抓取总页数:{} , 当前抓取总页数:{}", maxCrawlPage, count);
            return;
        }
        jokeImg.setSrc(processProtocol(jokeImg.getSrc()));
        if (!urlFilter.contains(jokeImg.getSrc())) {
            //处理图片
            ImageDto img = handleImage.downloadImg(jokeImg.getImg());
            String imgurl = img.getImgUrl();
            if (StringUtils.isNotBlank(imgurl)) {
                if (img.getImgType() != null && img.getImgType().equals("gif")) {
                    jokeImg.setGif(FilenameUtils.getFullPath(imgurl) + FilenameUtils.getBaseName(imgurl) + ".gif");
                    jokeImg.setType(2);
                } else {
                    jokeImg.setType(1);
                }
                //标题去特殊字符
                String title = jokeImg.getTitle();
                if (title != null) {
                    title = StringUtils.replaceEach(title, SEARCH, REPLACE);
                    jokeImg.setTitle(title);
                }
                jokeImg.setImg(imgurl);
                jokeImg.setWidth(img.getWidth());
                jokeImg.setHeight(img.getHeight());
                //点赞数和点踩数
                int bad = 150 - (int) (Math.random() * 150);
                int good = 500 + (int) (Math.random() * 500);
                jokeImg.setBad(bad);
                jokeImg.setGood(good);

                // ReleaseNick  发布段子用户昵称
                // ReleaseAvata 发布段子用户头像url

                //发布人
                int rid = random.nextInt(2089);
                if (rid == 0) {
                    rid = rid + 1; //User表中id范围为[1,2089]
                }
                User ru = userDao.select(rid);
                jokeImg.setReleaseNick(ru.getNickname());

                int riconid = rid % 20 + 1;
                String ravata = avataStr.replace("%d", String.valueOf(riconid));
                jokeImg.setReleaseAvata(ravata);

                //存在神评论
                if (jokeImg.getCommentNumber() != null && jokeImg.getCommentNumber() > 0) {

                    jobInfoDao.addJokeImg(jokeImg);
                    //获得jokeId
                    int jokeId = jokeImg.getId();

                    //  记录最大点赞数神评信息
                    int m_good = 0;
                    String m_comment = null, m_avata = null, m_nick = null;

                    //添加神评论: hotGoods--hotContents 神评点赞数和内容一一对应
                    int godNum = 0; //记录有效的神评数
                    for (int i = 0; i < jokeImg.getCommentNumber(); i++) {

                        Integer  god = Integer.valueOf(String.valueOf(jokeImg.getHotGoods().get(i)));
                        if(god == null){
                            god = 0;
                        }
                        String content = jokeImg.getHotContents().get(i);

                        //神评评论点赞数>10
                        if (god <= 10) {
                            continue;
                        }
                        //过滤无效字符
                        content = StringUtils.replaceEach(content, SEARCH, REPLACE);
                        //字数小于txtLength
                        boolean isLessLimit = content.length() > txtLimitLength ? true : false;
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

                        Comment com = new Comment();
                        com.setSid(jokeId);
                        com.setUid(uid);
                        com.setNickname(nick);
                        com.setContent(content);
                        com.setAvata(avata);
                        com.setGood(god);
                        commentDao.addComment(com);
                        godNum++;
                    }

                    //获取上述神评中点赞数最大的一条神评的信息，将其插入到joke中
                    jobInfoDao.updateJokeOfGods(jokeId, godNum, m_comment, m_avata, m_nick);

                } else {
                    jokeImg.setCommentNumber(0);
                    jobInfoDao.addJokeImg(jokeImg);
                }
            }
            logger.info("爬取joke:{},src:{},神评数量:{}", jokeImg.getId(), jokeImg.getSrc(), jokeImg.getCommentNumber());

            urlFilter.add(jokeImg.getSrc());
            logger.info("图片 - 处理页面结束:{}", jokeImg.getSrc());
        }
    }

    /**
     * 解决自适应url地址的问题
     * @param src
     * @return
     */
    private String processProtocol(String src) {
        if(src.startsWith("//")){
            return PROTOCOL + src;
        }
        return src;
    }

}

