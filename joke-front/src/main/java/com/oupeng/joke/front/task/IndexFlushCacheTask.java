package com.oupeng.joke.front.task;

import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.front.service.IndexService;
import com.oupeng.joke.front.util.FormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

/**
 * 首页缓存刷新任务
 */
@Component
public class IndexFlushCacheTask {
    private static Logger logger = LoggerFactory.getLogger(IndexFlushCacheTask.class);
    @Autowired
    private JedisCache jedisCache;
    /** 队列名称 */
    private String QUEUE_NAME;
    @Autowired
    private IndexService indexService;

    /**
     * 首页缓存刷新任务启动
     */
    @PostConstruct
    public void init() {
        logger.info("【首页缓存刷新任务】 start...");
        QUEUE_NAME = JedisKey.JOKE_INDEX_CACHE_FLUSH_QUEUE_PREFIX + UUID.randomUUID().toString();
        jedisCache.hset(JedisKey.JOKE_INDEX_CACHE_FLUSH_SIGN, QUEUE_NAME, JedisKey.INDEX_SIGN);
        new Thread(new IndexFlushCacheTaskThread(), "首页缓存刷新线程").start();
        logger.info("【首页缓存刷新任务】启动, 队列名称:[{}] ", QUEUE_NAME);
    }

    class IndexFlushCacheTaskThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    if (jedisCache != null) {
                        List<String> list = jedisCache.brpop(QUEUE_NAME, 60 * 5);
                        long start = System.currentTimeMillis();
                        if (!CollectionUtils.isEmpty(list)) {
                            logger.info("【首页缓存刷新任务】收到请求:[{}] process start ...", list.get(1));
                            String content = list.get(1);
//                            确认队列有效
                            if(StringUtils.isNotBlank(content)){
                                if(content.startsWith(JedisKey.INDEX_SIGN) && content.length() > 5){
                                    String sign = content.substring(content.indexOf(JedisKey.INDEX_SIGN) + 5);
                                    jedisCache.hset(JedisKey.JOKE_INDEX_CACHE_FLUSH_SIGN, QUEUE_NAME, sign);
                                } else {
                                    if(content.startsWith(JedisKey.INDEX_CACHE_FLUSH_PREFIX_CONFIG)){
                                        String did = content.substring(content.indexOf(JedisKey.INDEX_CACHE_FLUSH_PREFIX_CONFIG) + 6);
                                        indexService.flushConfigCache(did);
                                        logger.debug("【首页缓存刷新任务】- 刷新资配置存完成:[{}] ", did);
                                    } else if(content.startsWith(JedisKey.INDEX_CACHE_FLUSH_PREFIX_RESOURCE)){
                                        String environment = content.substring(content.indexOf(JedisKey.INDEX_CACHE_FLUSH_PREFIX_RESOURCE) + 8);
                                        indexService.flushResourceCache(environment);
                                        logger.debug("【首页缓存刷新任务】- 刷新资源缓存完成:[{}] ", environment);
                                    }
                                }
                            } else {
                                logger.debug("【首页缓存刷新任务】接收数据异常:[{}]", content);
                            }
                            long end = System.currentTimeMillis();
                            logger.info("【首页缓存刷新任务】处理完成, 耗时:[{}]", FormatUtil.getTimeStr(end-start));
                        } else {
                            logger.debug("Received:[{}] null", list);
                        }
                    } else {
//                        发邮件,暂停3分钟
                        logger.error("【首页缓存刷新任务】Redis缓存服务为空, 无法操作缓存!");
                        Thread.sleep(3 * 60 * 1000);
                    }
                } catch (Exception e) {
                    logger.error("【首页缓存刷新任务】执行异常:" + e.getMessage(), e);
                }
            }
        }
    }
}
