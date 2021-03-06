package com.oupeng.joke.front.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Comment;
import com.oupeng.joke.domain.comment.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by java_zong on 2017/4/19.
 */
@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);


    @Autowired
    private JedisCache jedisCache;


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
    public void likeComment(Integer id) {
        jedisCache.lpush(JedisKey.COMMENT_LIST_LIKE, String.valueOf(id));
    }

    /**
     * 评论
     * 如果发送评论的ID 在拉黑列表中存在,则直接返回
     *
     * @param jid
     * @param comment
     */
    public Result sendComment(Integer jid, String comment, Integer userId, String nick, String avata) {
        String num = jedisCache.get(JedisKey.COMMENT_NUMBER + userId);

        if (StringUtils.isNumeric(num)) {
            return new Result(5); // 超出每分钟发送限制
        }

        //查看缓存是否被拉黑
        String user = jedisCache.hget(JedisKey.BLACK_MAN, userId.toString());
        if (StringUtils.isNotBlank(user)) {
            return new Result(6);
        }

        Comment com = new Comment();
        com.setJokeId(jid);
        com.setBc(comment);
        com.setUid(userId);
        com.setNick(nick);
        com.setAvata(avata);
        jedisCache.lpush(JedisKey.NEW_COMMENT_LIST, JSON.toJSONString(com));
        return new Result(0);
    }
}
