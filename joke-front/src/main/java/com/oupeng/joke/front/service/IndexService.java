package com.oupeng.joke.front.service;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.IndexResource;
import com.oupeng.joke.front.util.FormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 首页服务接口
 */
@Service
public class IndexService {
    /** 日志 */
    private static final Logger log = LoggerFactory.getLogger(IndexService.class);
    /** 默认did */
    private static final String DEFAULT_DID = "0";
    @Autowired
    private JedisCache jedisCache;
    /** 配置集合 */
    private ConcurrentHashMap<String, String> configMap = new ConcurrentHashMap<>();
    /** 资源集合 */
    private ConcurrentHashMap<String, IndexResource> resourceMap = new ConcurrentHashMap<>();


    @PostConstruct
    public void initConstants() {
        IndexResource indexResource = JSON.parseObject(jedisCache.get(JedisKey.JOKE_RESOURCE_CONFIG_INDEX), IndexResource.class);
        if(indexResource != null){
            resourceMap.put(JedisKey.INDEX_CACHE_INDEX, indexResource);
        }
        IndexResource backResource = JSON.parseObject(jedisCache.get(JedisKey.JOKE_RESOURCE_CONFIG_BACK), IndexResource.class);
        if(backResource != null){
            resourceMap.put(JedisKey.INDEX_CACHE_BACK, backResource);
        }
        IndexResource testResource = JSON.parseObject(jedisCache.get(JedisKey.JOKE_RESOURCE_CONFIG_TEST), IndexResource.class);
        if(testResource != null){
            resourceMap.put(JedisKey.INDEX_CACHE_TEST, testResource);
        }
//        更新所以缓存
        Set<String> keys = jedisCache.hkeys(JedisKey.JOKE_DISTRIBUTOR_CONFIG);
        for(String key : keys){
            String config = jedisCache.hget(JedisKey.JOKE_DISTRIBUTOR_CONFIG, key);
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
                IndexResource testResource = resourceMap.get(JedisKey.INDEX_CACHE_TEST);
                if(testResource == null){
                    testResource = JSON.parseObject(jedisCache.get(JedisKey.JOKE_RESOURCE_CONFIG_TEST), IndexResource.class);;
                    if(testResource != null){
                        resourceMap.put(JedisKey.INDEX_CACHE_TEST, testResource);
                    }
                }
                model.addAttribute(JedisKey.INDEX_CACHE_INDEX, testResource);
                model.addAttribute(JedisKey.INDEX_CACHE_CONFIG, configMap.get(DEFAULT_DID));
            } else {
                IndexResource indexResource = resourceMap.get(JedisKey.INDEX_CACHE_INDEX);
                if(indexResource == null){
                    indexResource = JSON.parseObject(jedisCache.get(JedisKey.JOKE_RESOURCE_CONFIG_INDEX), IndexResource.class);
                    if(indexResource == null){
                        IndexResource backResource = resourceMap.get(JedisKey.INDEX_CACHE_INDEX);
                        if(indexResource == null){
                            indexResource = JSON.parseObject(jedisCache.get(JedisKey.JOKE_RESOURCE_CONFIG_BACK), IndexResource.class);
                            resourceMap.put(JedisKey.INDEX_CACHE_BACK, indexResource);
                        }
                        indexResource = backResource;
                        log.error("{}的首页请求失效,正在使用备用内容!", did);
                    }
                }
                String config = configMap.get(did);
                if(config == null){
                    config = jedisCache.hget(JedisKey.JOKE_DISTRIBUTOR_CONFIG, did);
                    if(config != null){
                        configMap.put(did, config);
                    }
                }
                model.addAttribute(JedisKey.INDEX_CACHE_INDEX, indexResource);
                model.addAttribute(JedisKey.INDEX_CACHE_CONFIG, config);
            }
        } else {
            IndexResource backResource = resourceMap.get(JedisKey.INDEX_CACHE_INDEX);
            if(backResource == null){
                backResource = JSON.parseObject(jedisCache.get(JedisKey.JOKE_RESOURCE_CONFIG_BACK), IndexResource.class);
                resourceMap.put(JedisKey.INDEX_CACHE_BACK, backResource);
            }
            model.addAttribute(JedisKey.INDEX_CACHE_INDEX, backResource);
            model.addAttribute(JedisKey.INDEX_CACHE_CONFIG, configMap.get(DEFAULT_DID));

            log.error("非法请求参数:{}", did);
        }

        if(log.isDebugEnabled()){
            long end = System.nanoTime();
            log.debug("获取首页信息完成,耗时:{}", FormatUtil.getTimeStr(end-start));
        }
    }

    /**
     * 刷新配置缓存
     * @param did
     */
    public void flushConfigCache(String did) {
        configMap.remove(did);
    }

    /**
     * 刷新资源缓存
     * @param environment
     */
    public void flushResourceCache(String environment) {
        resourceMap.remove(environment);
    }
}
