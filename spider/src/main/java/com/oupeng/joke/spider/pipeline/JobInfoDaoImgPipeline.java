package com.oupeng.joke.spider.pipeline;

import com.oupeng.joke.spider.domain.Comment;
import com.oupeng.joke.spider.domain.JokeImg;
import com.oupeng.joke.spider.domain.User;
import com.oupeng.joke.spider.mapper.CommentDao;
import com.oupeng.joke.spider.mapper.JobInfoDao;
import com.oupeng.joke.spider.mapper.UserDao;
import com.oupeng.joke.spider.utils.HandleImage;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import java.util.Random;

/**
 * Created by Administrator on 2017/3/8.
 */
@Component("JobInfoDaoImgPipeline")
public class JobInfoDaoImgPipeline implements PageModelPipeline<JokeImg> {

    //private static final String showPath = "http://localhost:8080/joke-back/resources/image/";

    //private static final String src="http://www.laifudao.com/tupian";

    private Random random = new Random(3000);
    @Autowired
    private JobInfoDao jobInfoDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CommentDao commentDao;

    @Override
    public void process(JokeImg jokeImg, Task task) {
        jokeImg.setSourceId(141);
        //处理图片
        String imgurl = HandleImage.downloadImg(jokeImg.getImg());
        if (("gif").equalsIgnoreCase(FilenameUtils.getExtension(jokeImg.getImg()))) {
            jokeImg.setGif(FilenameUtils.getFullPath(imgurl) + FilenameUtils.getBaseName(imgurl) + ".gif");
            jokeImg.setType(2);

        } else {
            jokeImg.setType(1);

        }
        jokeImg.setImg(imgurl);

        if (jokeImg.getAgreeTotal() != null && jokeImg.getAgreeTotal() > 10) {
            int id = random.nextInt(2090);
            User u = userDao.select(id);
            int last = u.getLast() + 1;
            userDao.update(last, id);
            String nick = StringUtils.trim(u.getNickname()) + Integer.toHexString(last);
            int uid = id * 10000 + last;
            int iconid = id % 20 + 1;
            String avata = "http://joke2.oupeng.com/comment/images/" + iconid + ".png";
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


    }
}
