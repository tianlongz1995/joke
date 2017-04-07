package com.oupeng.joke.spider.service;


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
    private static final String KEY = Key.BLOOM_FILTER_URL;
    /**
     * 布隆过滤器
     */
    @Autowired
    private BloomFilter<String> bloomFilter;


    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        log.info("布隆过滤器服务 - 开始启动.....");
        bloomFilter.setBloomFilterName(KEY);
        log.info("布隆过滤器服务 - 启动完成.....");

    }

    /**
     * 是否存在
     *
     * @param key
     * @return
     */
    public boolean contains(final String key) {
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
