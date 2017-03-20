package com.oupeng.joke.spider.service;


import com.oupeng.joke.spider.utils.BloomFilter;
import com.oupeng.joke.spider.utils.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.util.Collection;

/**
 * 布隆过滤器服务
 * Created by hushuang on 2017/2/13.
 */
@Component
public class URLBloomFilterService {
    private static final Logger log = LoggerFactory.getLogger(URLBloomFilterService.class);

    /**
     * 布隆过滤器
     */
    private BloomFilter<String> bloomFilter;
    @Autowired
    private RedisService redisService;

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        log.info("布隆过滤器服务 - 开始启动.....");

        bloomFilter = new BloomFilter<>(0.000001, (int) (10000000 * 1.5));
        Jedis jedis = redisService.getJedis();
        bloomFilter.bind(jedis, Key.BLOOM_FILTER_URL);

        log.info("布隆过滤器服务 - 启动完成.....");

    }

    /**
     * 是否存在
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        return bloomFilter.contains(key);
    }

    /**
     * 加入过滤器
     *
     * @param key
     */
    public void add(String key) {
        bloomFilter.add(key);
    }

    /**
     * 增加多个Key
     *
     * @param keys
     */
    public void addAll(Collection<? extends String> keys) {
        bloomFilter.addAll(keys);
    }


}
