package com.oupeng.joke.back.service;

import com.alibaba.fastjson.JSONObject;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.CommentMapper;
import com.oupeng.joke.domain.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 评论
 * Created by java_zong on 2017/4/17.
 */
@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private JedisCache jedisCache;

    public List<Comment> getCommentForPublish() {
        return commentMapper.getCommentForPublish();
    }

    public void updateCommentState(String ids, Integer state) {
        commentMapper.updateCommentState(ids, state);
    }

    @PostConstruct
    public void updateCommentLike() {
        logger.info("评论点赞任务启动...");
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    List<String> likeIdList = jedisCache.brpop(JedisKey.COMMENT_LIST_LIKE, 60 * 5);
                    if (!CollectionUtils.isEmpty(likeIdList)) {
                        Map idAndGood = JSONObject.parseObject(likeIdList.get(1));
                        Iterator iterator = idAndGood.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry entry = (Map.Entry) iterator.next();
                            String id = String.valueOf(entry.getKey());
                            String good = String.valueOf(entry.getValue());
                            //更新评论点赞数
                            commentMapper.updateCommentGood(Integer.valueOf(id), Integer.valueOf(good));
                        }
                    }
                }
            }
        }.start();
    }


}
