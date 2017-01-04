package com.oupeng.joke.front.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.IndexResource;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.LinkSource;
import com.oupeng.joke.domain.Topic;
import com.oupeng.joke.front.controller.IndexController;
import com.oupeng.joke.front.util.Constants;
import com.oupeng.joke.front.util.FormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class IndexService {
    /* 日志 */
    private static final Logger log = LoggerFactory.getLogger(IndexService.class);

    private static final String DEFAULT_DID = "0";

    private static final String INDEX = "index";

    private static final String CONFIG = "config";

    @Autowired
    private JedisCache jedisCache;

    private IndexResource indexResource = null;

    private IndexResource backResource = null;

    private IndexResource testResource = null;

    private ConcurrentHashMap<String, String> configMap = new ConcurrentHashMap<>();



    @PostConstruct
    public void initConstants() {
        indexResource = JSON.parseObject(jedisCache.get(JedisKey.JOKE_RESOURCE_CONFIG_PRO), IndexResource.class);
        backResource = JSON.parseObject(jedisCache.get(JedisKey.JOKE_RESOURCE_CONFIG_BACK), IndexResource.class);
        testResource = JSON.parseObject(jedisCache.get(JedisKey.JOKE_RESOURCE_CONFIG_TEST), IndexResource.class);

//        更新所以缓存
        Set<String> keys = jedisCache.hkeys(JedisKey.JOKE_HASH_DISTRIBUTOR_CONFIG);
        for(String key : keys){
            String config = jedisCache.hget(JedisKey.JOKE_HASH_DISTRIBUTOR_CONFIG, key);
            if(StringUtils.isNotBlank(config)){
                configMap.put(key, config);
            }
        }
    }

    /**
     * 获取首页配置信息
     * @param did
     * @param model
     */
    public void getIndexConfig(String did, Model model) {
        long start = System.nanoTime();
        if(StringUtils.isNumeric(did)){
//            测试渠道
            if(did.equals(DEFAULT_DID)){
                if(testResource == null){
                    testResource = JSON.parseObject(jedisCache.get(JedisKey.JOKE_RESOURCE_CONFIG_TEST), IndexResource.class);;
                }
                model.addAttribute(INDEX, testResource);
                model.addAttribute(CONFIG, configMap.get(DEFAULT_DID));
            } else {
                if(indexResource == null){
                    String key = JedisKey.JOKE_RESOURCE_CONFIG_PRO;
                    indexResource = JSON.parseObject(jedisCache.get(key), IndexResource.class);
                    if(indexResource == null){
                        indexResource = backResource;
                        key = JedisKey.JOKE_RESOURCE_CONFIG_BACK;
                        indexResource = JSON.parseObject(jedisCache.get(key), IndexResource.class);
                        log.error("{}的首页请求失效,正在使用备用内容!", did);
                    }
                }
                String config = configMap.get(did);
                if(config == null){
                    config = jedisCache.hget(JedisKey.JOKE_HASH_DISTRIBUTOR_CONFIG, did);
                    if(config != null){
                        configMap.put(did, config);
                    }
                }
                model.addAttribute(INDEX, indexResource);
                model.addAttribute(CONFIG, config);
            }
        } else {
            model.addAttribute(INDEX, backResource);
            model.addAttribute(CONFIG, configMap.get(DEFAULT_DID));

            log.error("非法请求参数:{}", did);
        }

        if(log.isDebugEnabled()){
            long end = System.nanoTime();
            log.debug("获取首页信息完成,耗时:{}", FormatUtil.getTimeStr(end-start));
        }
    }
}
