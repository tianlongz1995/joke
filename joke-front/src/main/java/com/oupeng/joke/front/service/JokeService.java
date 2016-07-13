package com.oupeng.joke.front.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Feedback;
import com.oupeng.joke.domain.response.DistributorConfig;
import com.oupeng.joke.domain.response.Failed;
import com.oupeng.joke.domain.response.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class JokeService {
    private static Logger logger = LoggerFactory.getLogger(JokeService.class);

    @Autowired
    private JedisCache jedisCache;
    /** 赞信息列表    */
    private static List<String> addLikeIds = Lists.newCopyOnWriteArrayList();
    /** 踩信息列表    */
    private static List<String> addStepIds = Lists.newCopyOnWriteArrayList();
    /** 反馈信息列表    */
    private static List<String> feedbackList = Lists.newCopyOnWriteArrayList();

    /**
     * 获取渠道配置
     * @param did
     * @return
     */
    public Result getDistributorConfig(String did) {
        if(StringUtils.isNumeric(did)){
            String result = jedisCache.hget(JedisKey.JOKE_HASH_DISTRIBUTOR_CONFIG, did);
            DistributorConfig dcr = JSON.parseObject(result, DistributorConfig.class);
            if(dcr != null){
                return new Result(200, "success", dcr);
            }
        }
        return new Failed();
    }

    /**
     * 缓存踩赞信息
     * @param id
     * @param type 1：赞；2：踩
     */
    public void stepLike(Integer id, Integer type) {
        if(type == 1){
            addLikeIds.add(String.valueOf(id));
        }else if(type == 2){
            addStepIds.add(String.valueOf(id));
        }
    }

    /**
     * 定时将赞列表中数据存储到缓存中
     */
    @Scheduled(fixedRate = 1000 * 60 * 30, initialDelay = 1000 * 60 * 5)
    public void addLikeQueue(){
        try{
            if(!CollectionUtils.isEmpty(addLikeIds)){
                logger.info("增加点赞数记录"+addLikeIds.size()+"条");
                jedisCache.lpush(JedisKey.JOKE_LIST_LIKE, addLikeIds.toArray(new String[]{}));
                addLikeIds.clear();
            }
        }catch(Exception e){
            logger.error("增加点赞数失败",e);
        }
    }
    /**
     * 定时将踩列表中数据存储到缓存中
     */
    @Scheduled(fixedRate = 1000 * 60 * 30, initialDelay = 1000 * 60 * 5)
    public void addStepQueue(){
        try{
            if(!CollectionUtils.isEmpty(addStepIds)){
                logger.info("增加踩数记录" + addStepIds.size() + "条");
                jedisCache.lpush(JedisKey.JOKE_LIST_STEP, addStepIds.toArray(new String[]{}));
                addStepIds.clear();
            }
        }catch(Exception e){
            logger.error("增加踩数失败",e);
        }
    }
    /**
     * 缓存反馈信息
     * @param feedback
     */
    public void feedback(Feedback feedback) {
        String feedbackJson = JSON.toJSONString(feedback);
            if(feedbackJson != null){
                feedbackList.add(feedbackJson);
            }
    }
    /**
     * 定时将反馈信息列表中数据存储到缓存中
     */
    @Scheduled(fixedRate = 1000 * 60 * 10, initialDelay = 1000 * 60 * 5)
    public void addFeedbackCache(){
        try{
            if(!CollectionUtils.isEmpty(feedbackList)){
                logger.info("增加反馈数记录" + feedbackList.size() + "条");
                jedisCache.lpush(JedisKey.JOKE_LIST_FEEDBACK, feedbackList.toArray(new String[]{}));
                feedbackList.clear();
            }
        }catch(Exception e){
            logger.error("增加反馈数失败",e);
        }
    }
}
