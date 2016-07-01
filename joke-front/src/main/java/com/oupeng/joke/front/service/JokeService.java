package com.oupeng.joke.front.service;

import com.google.common.collect.Lists;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Feedback;
import com.oupeng.joke.domain.response.DistributorConfigResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayDeque;
import java.util.List;

@Service
public class JokeService {
    private static Logger logger = LoggerFactory.getLogger(JokeService.class);

    @Autowired
    private JedisCache jedisCache;
    /**     */
    private static List<String> addStepIds = Lists.newCopyOnWriteArrayList();
//    private static ArrayDeque

    /**     */
    private static List<String> addLikeIds = Lists.newCopyOnWriteArrayList();

    /**
     * 获取渠道配置
     * @param did
     * @return
     */
    public DistributorConfigResult getDistributorConfig(Integer did) {

        return null;
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
//        jedisCache.lpush(feedback);
    }
}
