package com.oupeng.joke.back.service;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.back.util.FormatUtil;
import com.oupeng.joke.back.util.HttpUtil;
import com.oupeng.joke.back.util.Im4JavaUtils;
import com.oupeng.joke.back.util.ImageUtil;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.CommentMapper;
import com.oupeng.joke.dao.mapper.JokeMapper;
import com.oupeng.joke.dao.mapper.ManagerMapper;
import com.oupeng.joke.dao.mapper.UserMapper;
import com.oupeng.joke.domain.Comment;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.user.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;

/**
 * 管理服务接口
 * Created by hushuang on 2017/3/23.
 */
@Service
public class ManagerService {
    private static final Logger log = LoggerFactory.getLogger(ManagerService.class);
    @Autowired
    private ManagerMapper managerMapper;
    @Autowired
    private JokeMapper jokeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private JedisCache jedisCache;
    @Autowired
    private Environment env;
    private String cdnPrefix = "/data01/images/";

    @PostConstruct
    public void initPath() {
        String cdn = env.getProperty("img.cdn.path");
        if (StringUtils.isNotEmpty(cdn)) {
            cdnPrefix = cdn;
        }

    }


    /**
     * 精选切图
     */
    public void choiceCrop() {
        long start = System.currentTimeMillis();
//1. 查询出所有精选图片
        List<String> list = managerMapper.choiceList();
        if (CollectionUtils.isEmpty(list)) {
            log.error("查询精选图片为空!");
            return;
        }
//2. 替换路径后,切图
        int success = 0, error = 0;
        for (String url : list) {
            try {
                String path = cdnPrefix + url;
                File file = new File(path);
                if (file.exists()) {
                    String fileName = HttpUtil.getUrlImageFileName(url);
                    String dir = ImageUtil.getImageFileDir(path);
                    String resizeWidth600Name = fileName.replace(".jpg", "_w600.jpg");
                    Im4JavaUtils.cropImageByWidth(path, dir + "/" + resizeWidth600Name, 600, 257, 50.0f);
                    String resizeWidth200Name = fileName.replace(".jpg", "_w200.jpg");
                    Im4JavaUtils.cropImageByWidth(path, dir + "/" + resizeWidth200Name, 200, 86, 50.0f);
                    success++;
                    log.error("精选切图 - 切图完成:[{}]", path);
                } else {
                    log.error("精选切图 - 文件不存在:[{}]", path);
                    error++;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        long end = System.currentTimeMillis();
        log.info("精选切图完成, 成功:[{}]条, 失败:[{}]条, 耗时:{}", success, error, FormatUtil.getTimeStr(end - start));
    }

    /**
     * 段子头像补全
     */
    public void jokeAvatar() {
        long start = System.currentTimeMillis();
//    1. 查询出所有没有头像和昵称的段子
        List<Integer> list = managerMapper.getNotAvatarJoke();
        if (CollectionUtils.isEmpty(list)) {
            log.error("查询没有头像和昵称的段子为空!");
            return;
        }
        int index = 0;
        for (Integer id : list) {
//    2. 通过图像接口获取随机用户名和图像
            String nike = getReleaseNick("");
            String avatar = getReleaseAvatar(new Random().nextInt(100));
//            Comment comment = HttpUtil.getRandomUser("http://joke2.oupeng.com/comment/joke/user");
//    3. 更新段子头像和昵称
            managerMapper.updateAvatarAndNick(id, avatar, nike);
            index++;
//            更新缓存
            String key = JedisKey.STRING_JOKE + id;
            Joke joke = JSON.parseObject(jedisCache.get(key), Joke.class);
            if (joke != null) {
                joke.setRa(avatar);
                joke.setRn(nike);
                jedisCache.set(key, JSON.toJSONString(joke));
            }
        }
        long end = System.currentTimeMillis();
        log.info("段子头像补全完成, 补全:[{}]条, 耗时:{}", index, FormatUtil.getTimeStr(end - start));
    }

    /**
     * 获取发布者头像
     *
     * @param id
     * @return
     */
    private String getReleaseAvatar(Integer id) {
        int i = id % 40;
        if (i <= 19) {
            return "1/" + i + ".jpg";
        } else {
            return "1/" + i + ".png";
        }

    }

    /**
     * 获取段子发布人昵称
     *
     * @param name
     * @return
     */
    private String getReleaseNick(String name) {
        List<String> nickNames = jedisCache.srandmember(JedisKey.JOKE_NICK_NAME, 5);
        if (CollectionUtils.isEmpty(nickNames)) {
            return "笑料百出用户" + new Random().nextInt(10);
        }
        for (String nick : nickNames) {
            if (!nick.equals(name)) {
                return nick;
            }
        }
        return "笑料百出用户" + new Random().nextInt(10);
    }

    /**
     * 内涵段子
     */
    private final static Integer source_id = 155;
    private final static String dateTime = "2017-07-01 00:00:00";
    private final static String avataStr = "http://joke2-img.oupeng.com/1/%d.png";

    public void addJokeComment() {
        Random random = new Random(3000);

        //获取要添加神评论的jokeList
        List<Joke> jokeList = jokeMapper.getJokebeforeTime(dateTime, source_id);

        if (!CollectionUtils.isEmpty(jokeList)) {
            for (Joke joke : jokeList) {
                if (joke.getCommentContent() != null) { //存在神评
                    continue;
                }
                String pageURL = joke.getSrc();
                String str = pageURL.substring("http://neihanshequ.com/p".length(), pageURL.length() - 1);
                String jsonURL = "http://neihanshequ.com/m/api/get_essay_comments/?group_id=" + str + "&app_name=neihanshequ_web&offset=0";

                Map<String, List> map = HttpUtil.getGodMsg(jsonURL);


                List<Integer> hotGooods = map.get("hotGoods");
                List<String> hotContents = map.get("hotContents");
                int commentNumber = hotGooods.size();

                int jokeId = joke.getId();
                int m_good = 0;
                String m_comment = null, m_avata = null, m_nick = null;

                //记录有效的神评数
                int godNum = 0;
                List<Comment> commentList = new ArrayList<>();
                for (int i = 0; i < commentNumber; i++) {

                    int god = hotGooods.get(i);
                    String content = hotContents.get(i);

                    //神评评论点赞数>10
                    if (god <= 10) {
                        continue;
                    }

                    int id = random.nextInt(2089);
                    User u = userMapper.select(id);
                    int last = u.getLast() + 1;
                    userMapper.update(last, id);
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
                    com.setJokeId(jokeId);
                    com.setUid(uid);
                    com.setNick(nick);
                    com.setBc(content);
                    com.setAvata(avata);
                    com.setGood(god);
//                    commentMapper.insertComment(com);
                    commentList.add(com);
                    godNum++;
                }
                //批量插入comment
                if (commentList.size() > 0) {
                    commentMapper.insertBatchComment(commentList);
                }
                jokeMapper.updateJokeComment(jokeId, godNum, m_comment, m_avata, m_nick);
            }
        }
    }
}
