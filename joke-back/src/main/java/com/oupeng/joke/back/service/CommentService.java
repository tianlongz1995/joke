package com.oupeng.joke.back.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.oupeng.joke.back.util.FormatUtil;
import com.oupeng.joke.back.util.HttpUtil;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.CommentMapper;
import com.oupeng.joke.domain.Comment;
import com.oupeng.joke.domain.Result;
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
import java.util.Random;
import java.util.Set;


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
    private Random random = new Random(3000);
    private String randomUserUrl = "http://joke2.oupeng.com/comment/joke/user";

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private JedisCache jedisCache;
    @Autowired
    private Environment env;
    @Autowired
    private SensitiveFilterService filterService;

    /**
     * 未发布评论
     *
     * @return
     */
    public List<Comment> getCommentForPublish() {
        return commentMapper.getCommentForPublish(null, 1, 0);
    }

    /**
     * 更新评论发布状态
     *
     * @param ids
     * @param state
     */
    public void updateCommentPubState(String ids, Integer state) {
        commentMapper.updateCommentPubState(ids, state);
    }

    @PostConstruct
    public void init() {
        String good = env.getProperty("god.comment.good");
        if (StringUtils.isNumeric(good)) {
            godGood = Integer.valueOf(good);
        }
        filterService.init();
        new Thread(new AddCommentThread(), "添加评论线程").start();
        new Thread(new UpdateLikeCacheThread(), "评论更新数据库线程").start();
        new Thread(new UpdateLikeDatabaseThread(), "评论更新数据库线程").start();
    }

    public int getListForVerifyCount(String keyWord, Integer state) {
        return commentMapper.getListForVerifyCount(keyWord, state);
    }

    public List<Comment> getListForVerify(String keyWord, Integer state, Integer offset, Integer pageSize) {
        return commentMapper.getListForVerify(keyWord, state, offset, pageSize);
    }

    /**
     * 更新评论状态
     *
     * @param ids
     * @param state
     * @param username
     * @param allState
     * @return
     */
    public void verifyComment(String ids, Integer state, Integer allState, String username) {
        if (allState == 1) {
            if (state == 3 || state == 4) {//已发布评论状态拉黑删除  删除缓存
                cleanCommentCache(ids);
            }
        } else if (allState != 2) {
            if (state == 1) {//重新加入缓存
                List<Comment> list = commentMapper.getCommentForPublish(ids, allState, 1);
                for (Comment comment : list) {
                    addCommentToCache(comment);
                }
            }
        }
        //更新时间
        Integer updateTime = FormatUtil.getTime();
        commentMapper.updateCommentState(ids, state, username, updateTime);
    }

    /**
     * 清除评论缓存
     *
     * @param ids
     */
    private void cleanCommentCache(String ids) {
        String[] commentIds = ids.split(",");
        if(commentIds == null || commentIds.length < 1){
            logger.error("清除评论缓存异常: ids:{}", ids);
            return;
        }
        //评论keys
        Set<String> keys = jedisCache.keys(JedisKey.JOKE_COMMENT_LIST + "*");
        for (String key : keys) {
            for(String id : commentIds){
                jedisCache.zrem(key, id);
            }
        }
        //神评keys
        Set<String> godKeys = jedisCache.keys(JedisKey.JOKE_GOD_COMMENT + "*");
        for (String godKey : godKeys) {
            for(String id : commentIds){
                jedisCache.zrem(godKey, id);
            }

        }
        //删除缓存中的评论详情
        for (String id : commentIds) {
            jedisCache.del(JedisKey.STRING_COMMENT + id);
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

    /**
     * 添加评论到缓存
     *
     * @param comment
     */
    private void addCommentToCache(Comment comment) {
        //评论列表缓存-按更新时间排序 只存储id
        String commentListKey = JedisKey.JOKE_COMMENT_LIST + comment.getJokeId();
        jedisCache.zadd(commentListKey, comment.getTime(), String.valueOf(comment.getId()));
        if (comment.getGood() >= godGood) {//神评论
            String godKey = JedisKey.JOKE_GOD_COMMENT + comment.getJokeId();
            jedisCache.zadd(godKey, comment.getGood(), String.valueOf(comment.getId()));
        }
        //评论缓存
        String commentKey = JedisKey.STRING_COMMENT + comment.getId();
        jedisCache.set(commentKey, JSON.toJSONString(comment));
    }

    /**
     * 更新评论点赞缓存
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
     * 添加评论线程
     */
    class AddCommentThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    List<String> list = jedisCache.brpop(JedisKey.NEW_COMMENT_LIST, 60 * 5);
                    if (!CollectionUtils.isEmpty(list)) {
                        Comment com = JSON.parseObject(list.get(1), Comment.class);
                        if(com != null){
                            String content = filterService.doFilter(com.getBc());
                            if (StringUtils.isNotBlank(content)) {
                                Comment comment = HttpUtil.getRandomUser(randomUserUrl);
                                comment.setBc(content);
                                comment.setJokeId(com.getJokeId());
                                comment.setTime(FormatUtil.getTime());
                                comment.setGood(0);
                                comment.setUid(com.getUid());
                                comment.setAvata(com.getAvata());
                                comment.setNick(com.getNick());
                                commentMapper.insertComment(comment);
                                addCommentToCache(comment);
                                jedisCache.setAndExpire(JedisKey.COMMENT_NUMBER + com.getUid(), "1", 20);
                            }

                        }
                    }
                } catch (Exception e) {
                    logger.error("【评论任务】执行异常:" + e.getMessage(), e);
                }
            }
        }
    }

}
