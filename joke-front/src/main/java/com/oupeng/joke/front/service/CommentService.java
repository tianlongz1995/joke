package com.oupeng.joke.front.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Comment;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * Created by java_zong on 2017/4/19.
 */
@Service
public class CommentService {
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
}
