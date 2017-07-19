//package com.oupeng.joke.back.task;
//
//import com.alibaba.fastjson.JSON;
//import com.oupeng.joke.back.service.CommentService;
//import com.oupeng.joke.cache.JedisCache;
//import com.oupeng.joke.cache.JedisKey;
//import com.oupeng.joke.domain.Comment;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//
//import javax.annotation.PostConstruct;
//import java.util.List;
//
//
///**
// * 评论定时发布
// * Created by java_zong on 2017/4/17.
// */
//@Component
//public class CommentPublishTask {
//    private static final Logger logger = LoggerFactory.getLogger(CommentPublishTask.class);
//    //神评点赞数判断标准
//    private Integer godGood = 10;
//    @Autowired
//    private CommentService commentService;
//    @Autowired
//    private JedisCache jedisCache;
//    @Autowired
//    private Environment env;
//
//    @PostConstruct
//    public void init() {
//        String god = env.getProperty("god.comment.good");
//        if (StringUtils.isNumeric(god)) {
//            godGood = Integer.valueOf(god);
//        }
//    }
//
//    @Scheduled(cron = "0 0/5 * * * ?")
//    public void publishCommentTask() {
//        try {
//            logger.info("开始发布评论数据...");
//            int count = 0;
//            List<Comment> commentList = commentService.getCommentForPublish();
//            StringBuffer ids = new StringBuffer();
//            if (!CollectionUtils.isEmpty(commentList)) {
//                for (Comment comment : commentList) {
//                    //评论列表缓存-按更新时间排序 只存储id
//                    String commentListKey = JedisKey.JOKE_COMMENT_LIST + comment.getJokeId();
//                    jedisCache.zadd(commentListKey, comment.getTime(), String.valueOf(comment.getId()));
//                    if (comment.getGood() != null && comment.getGood() >= godGood) {//神评缓存
//                        String godKey = JedisKey.JOKE_GOD_COMMENT + comment.getJokeId();
//                        jedisCache.zadd(godKey, comment.getGood(), String.valueOf(comment.getId()));
//                    }
//                    //评论缓存
//                    String commentKey = JedisKey.STRING_COMMENT + comment.getId();
//                    jedisCache.set(commentKey, JSON.toJSONString(comment));
//                    ids.append(comment.getId()).append(",");
//                }
//                //更新已发布评论状态
//                commentService.updateCommentPubState(ids.deleteCharAt(ids.lastIndexOf(",")).toString(), 1);
//                count = commentList.size();
//            }
//            logger.info("发布评论数据结束，共发布{}条", count);
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }
//    }
//}
