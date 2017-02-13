package com.oupeng.joke.back.task;

import com.google.common.collect.Maps;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * 8点至晚上12点每3个小时更新一次接口数据。每次20条数据
 */
@Service
public class EzineJokePublishTask {
    private static Logger logger = LoggerFactory.getLogger(EzineJokePublishTask.class);

    @Autowired
    private JedisCache jedisCache;

//    @Scheduled(cron="0 */1 * * * ?")
    @Scheduled(cron="0 0 8,11,14,17,20,23 * * ?")
    public void publishEzineJoke(){
     logger.info("ezine 更新段子.....................");
        //  joke.channel.2 段子id  按照 weight+baseScore 排序
        Map<String,Double> map = Maps.newHashMap();
        Set<String> jokeSet = jedisCache.zrevrange(JedisKey.JOKE_CHANNEL + 2,0L,500L);

        Set<String> ezineSet = jedisCache.zrange(JedisKey.EZINE_JOKE,0,-1);
        int flag = 0;
        for(String jId: jokeSet){
            if(!ezineSet.contains(jId)){
                flag++;
                if(flag>20){
                    break;
                }
                map.put(jId,(double)System.currentTimeMillis());
            }
        }
        if(!map.isEmpty()){
            jedisCache.zadd(JedisKey.EZINE_JOKE , map);
        }
    }
}
