package com.oupeng.joke.front.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Comment;
import com.oupeng.joke.front.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by java_zong on 2017/4/19.
 */
@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    private Random random = new Random(3000);
    //点赞评论列表 key为id, value为good
    private Map<Integer, Integer> goodMap = Maps.newConcurrentMap();

    //神评点赞数阀值
    private Integer godGood = 10;


    @Autowired
    private Environment env;


    @Autowired
    private JedisCache jedisCache;


    @PostConstruct
    public void init() {
        String good = env.getProperty("god.comment.good");
        if (StringUtils.isNumeric(good)) {
            godGood = Integer.valueOf(good);
        }
    }


    /**
     * 获取评论
     *
     * @param jid
     * @param isGod true为神评 false为所有评论
     * @return
     */
    public List<Comment> getComment(Integer jid, Integer start, Integer end, boolean isGod) {
        List<Comment> commentList = Lists.newArrayList();
        String commentListKey;
        Set<String> keys = null;
        if (isGod) {//神评论
            commentListKey = JedisKey.JOKE_GOD_COMMENT + jid;
            keys = jedisCache.zrevrangebyscore(commentListKey);
        } else {//所有评论-分页显示
            commentListKey = JedisKey.JOKE_COMMENT_LIST + jid;
            keys = jedisCache.zrevrange(commentListKey, start.longValue(), end.longValue());
        }
        if (!CollectionUtils.isEmpty(keys)) {
            for (String commentId : keys) {
                String commentKey = JedisKey.STRING_COMMENT + commentId;
                Comment comment = JSON.parseObject(jedisCache.get(commentKey), Comment.class);
                commentList.add(comment);
            }
        }
        return commentList;
    }

    /**
     * 获取段子的评论条数
     *
     * @param jid
     * @return
     */
    public int getCommentCount(Integer jid) {
        Long size = jedisCache.zcard(JedisKey.JOKE_COMMENT_LIST + jid);
        return size.intValue();
    }


    /**
     * 评论点赞
     *
     * @param id
     * @return
     */
    public boolean likeComment(Integer id) {

        //更新评论详情缓存中点赞数
        Comment comment = JSON.parseObject(jedisCache.get(JedisKey.STRING_COMMENT + id), Comment.class);
        if (comment != null) {

            Integer good = comment.getGood();
            if (good != null) {
                good = good + 1;
            } else {
                good = 1;
            }
            comment.setGood(good);
            goodMap.put(id, good);
            jedisCache.set(JedisKey.STRING_COMMENT + id, JSON.toJSONString(comment));
            if (good >= godGood) {//更新神评缓存
                jedisCache.zadd(JedisKey.JOKE_GOD_COMMENT + comment.getJokeId(), good, String.valueOf(id));
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * 每三分钟入库更新一次
     */
    @Scheduled(initialDelay = 5000, fixedRate = 1000 * 60 * 3)
    public void addLikeQueue() {
        try {
            if (!CollectionUtils.isEmpty(goodMap)) {
                jedisCache.lpush(JedisKey.COMMENT_LIST_LIKE, JSON.toJSONString(goodMap));
                logger.info("增加评论点赞数记录" + goodMap.size() + "条");
                goodMap.clear();
            }
        } catch (Exception e) {
            logger.error("增加评论点赞数失败", e);
        }
    }


//    public Comment sendComment(Integer jid, String comment) {
//        Comment com = HttpUtil.getRandomUser("http://joke2.oupeng.com/comment/joke/user");
//        com.setBc(comment);
//        com.setUid(random.nextInt(2090));
//        com.setJokeId(jid);
//    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    Date date = new Date();
//    String dates = dateFormat.format(date);
//    createTime = Timestamp.valueOf(dates).getTime() / 1000;
//        commentMapper.insertComment(com);
//       return com;
//    }
}
