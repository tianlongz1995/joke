package com.oupeng.joke.back.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.CommentMapper;
import com.oupeng.joke.domain.Comment;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;


/**
 * 评论
 * Created by java_zong on 2017/4/17.
 */
@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    private Map<String, Integer> map = Maps.newConcurrentMap();
    //神评点赞数阀值
    private Integer godGood = 10;

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private JedisCache jedisCache;
    @Autowired
    private Environment env;

    public List<Comment> getCommentForPublish() {
        return commentMapper.getCommentForPublish();
    }

    public void updateCommentState(String ids, Integer state) {
        commentMapper.updateCommentState(ids, state);
    }

    @PostConstruct
    public void init() {
        String good = env.getProperty("god.comment.good");
        if (StringUtils.isNumeric(good)) {
            godGood = Integer.valueOf(good);
        }
        new Thread(new UpdateLikeCacheThread(), "评论更新数据库线程").start();
        new Thread(new UpdateLikeDatabaseThread(), "评论更新数据库线程").start();
    }

    /**
     * 更新缓存
     */
    class UpdateLikeCacheThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    List<String> likeIdList = jedisCache.brpop(JedisKey.COMMENT_LIST_LIKE, 60 * 5);
                    if (!CollectionUtils.isEmpty(likeIdList)) {
                        String likeId = likeIdList.get(1);
                        updateCommentGoodCache(likeId);
                        Integer increment = map.get(likeId);
                        map.put(likeId, increment == null ? 1 : increment + 1);
                    }
                } catch (Exception e) {
                    logger.error("【评论更新任务】更新缓存执行异常:" + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 更新数据库线程
     */
    class UpdateLikeDatabaseThread implements Runnable {
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000 * 60 * 3);
                    for (Map.Entry<String, Integer> entry : map.entrySet()) {
                        String id = entry.getKey();
                        Integer good = entry.getValue();
                        try {
                            //更新评论点赞数
                            commentMapper.updateCommentGood(Integer.valueOf(id), good);
                            map.entrySet().remove(entry);
                        } catch (Exception e) {
                            logger.error("【评论更新任务】更新数据库执行异常:" + e.getMessage(), e);
                            Thread.sleep(1000 * 60 * 3);
                        }
                    }
                } catch (Exception e) {
                    logger.error("【评论更新任务】更新数据库执行异常:" + e.getMessage(), e);
                }

            }
        }
    }

    /**
     * 更新评论缓存
     *
     * @param id
     */
    private void updateCommentGoodCache(String id) {
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
            jedisCache.set(JedisKey.STRING_COMMENT + id, JSON.toJSONString(comment));
            if (good >= godGood) {//更新神评缓存
                jedisCache.zadd(JedisKey.JOKE_GOD_COMMENT + comment.getJokeId(), good, String.valueOf(id));
            }
        }

    }


}
