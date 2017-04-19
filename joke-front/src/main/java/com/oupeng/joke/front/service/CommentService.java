package com.oupeng.joke.front.service;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Comment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by java_zong on 2017/4/19.
 */
//@Service
public class CommentService {
//    //神评点赞数阀值
//    private Integer godGood = 10;
//
//    @Autowired
//    private Environment env;
//    @Autowired
//    private CommentMapper commentMapper;
//    @Autowired
//    private JedisCache jedisCache;
//
//    @PostConstruct
//    public void init() {
//        String good = env.getProperty("god.comment.good");
//        if (StringUtils.isNumeric(good)) {
//            godGood = Integer.valueOf(good);
//        }
//    }
//
//    /**
//     * 评论点赞
//     *
//     * @param id
//     * @param uid
//     * @return
//     */
//    public boolean likeComment(Integer id, Integer uid) {
//        //评论用户点赞缓存key
//        String userGoodKey=JedisKey.COMMENT_USER_GOOD+id;
//        String userId=String.valueOf(uid);
//        boolean repeat=jedisCache.sismember(userGoodKey,userId);
//        if(repeat){//已点赞用户
//            return false;
//        }else {//新点赞用户加入缓存中
//            jedisCache.sadd(userGoodKey, userId);
//        }
//        commentMapper.likeComment(id);
//        int incrementGood;
//
//        //更新评论详情缓存中点赞数
//        Comment comment = JSON.parseObject(jedisCache.get(JedisKey.STRING_COMMENT + id), Comment.class);
//        if (comment != null) {
//            Integer good = comment.getGood();
//            if (good != null) {
//                incrementGood = good + 1;
//            } else {
//                incrementGood = 1;
//            }
//            comment.setGood(incrementGood);
//            if (incrementGood >= godGood) {//更新神评缓存
//                jedisCache.zadd(JedisKey.JOKE_GOD_COMMENT + comment.getJokeId(), incrementGood, String.valueOf(id));
//            }
//        } else {
//            return false;
//        }
//        return true;
//    }
}
