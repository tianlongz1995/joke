package com.oupeng.joke.back.service;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.ResourceMapper;
import com.oupeng.joke.domain.IndexItem;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 更新首页缓存接口
 * Created by hushuang on 2017/1/4.
 */
@Service
public class IndexCacheFlushService {
    private static final Logger logger = LoggerFactory.getLogger(IndexCacheFlushService.class);
    @Autowired
    private JedisCache jedisCache;
    @Autowired
    private Environment env;

    private long sleep = 3000;

    /**
     * 更新首页缓存队列
     */
    private BlockingQueue<IndexItem> queue = new ArrayBlockingQueue<>(1000);
    /**
     * 运行状态
     */
    private AtomicBoolean running = new AtomicBoolean(true);

    /**
     * 更新首页缓存
     * @param indexItem
     * @return
     */
    public boolean updateIndex(IndexItem indexItem) {
        return queue.offer(indexItem);
    }


    /**
     * 跟随应用启动
     */
    @PostConstruct
    public void init(){
        String indexCacheFlushSleep = env.getProperty("index.cache.flush.sleep");
        if(StringUtils.isNumeric(indexCacheFlushSleep)){
            sleep = Long.parseLong(indexCacheFlushSleep);
        }
        logger.info("更新首页缓存接口启动...");
        new Thread(new IndexCacheFlushThread(),"更新首页缓存任务").start();
    }

    @PreDestroy
    public void preDestroy()  {
        logger.info("更新首页缓存任务停止!");
        running.set(false);
    }

    class IndexCacheFlushThread implements Runnable {
        public void run() {
            while (running.get()) {
                try {
                    /** ----------------------- 更新首页缓存任务 --------------------- */
                    IndexItem indexItem = queue.take();
                    if (indexItem != null && indexItem.getType() != null) {
                        logger.info("收到更新首页缓存任务:{}", JSON.toJSONString(indexItem));
                        int type = indexItem.getType();
                        String value = indexItem.getValue();
                        if(type >= 0 && type <=3){
                            flushResource(type, value);
                        } else {
                            logger.error("更新首页缓存任务: 非法更新参数:{}", JSON.toJSONString(indexItem));
                        }
                    }
                } catch (Exception e) {
                    logger.error("更新首页缓存任务异常:" + e.getMessage(), e);
                }
            }
        }

        /**
         * 刷新首页资源缓存
         * @param type
         * @param value
         */
        private void flushResource(int type, String value) {
            String prefix = JedisKey.INDEX_CACHE_FLUSH_PREFIX_RESOURCE;
            if(type == 0){
                prefix = JedisKey.INDEX_CACHE_FLUSH_PREFIX_CONFIG;
            }

            String sign = UUID.randomUUID().toString();
            Set<String> set = jedisCache.hkeys(JedisKey.JOKE_INDEX_CACHE_FLUSH_SIGN);
            for(String key : set){
//               发送暗号
                jedisCache.lpush(key, JedisKey.INDEX_SIGN + sign);
                logger.debug("发送给[{}]暗号:[{}]", key, JedisKey.INDEX_SIGN + sign);
            }
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
               logger.error("刷新首页缓存暂停异常:"+e.getMessage(), e);
            }

            for(String key : set){
                String s = jedisCache.hget(JedisKey.JOKE_INDEX_CACHE_FLUSH_SIGN, key);
//              确认暗号
                if(sign.equals(s)){
                    jedisCache.lpush(key, prefix + value);
                    logger.debug("确认[{}]暗号数据:[{}]", key, prefix + value);
                } else {
                    jedisCache.hdel(JedisKey.JOKE_INDEX_CACHE_FLUSH_SIGN, key);
                    jedisCache.del(key);
                    logger.info("删除[{}] Key:[{}]", JedisKey.JOKE_INDEX_CACHE_FLUSH_SIGN, key);
                }
            }
        }
    }


}
