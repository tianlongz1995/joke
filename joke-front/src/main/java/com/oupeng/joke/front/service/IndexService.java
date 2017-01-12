package com.oupeng.joke.front.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Banner;
import com.oupeng.joke.domain.IndexResource;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.JokeDetail;
import com.oupeng.joke.front.util.Constants;
import com.oupeng.joke.front.util.FormatUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
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
    /** 图片前缀 */
    private static String IMG_PREFIX = "http://joke-img.adbxb.cn/";

    @Autowired
    private JedisCache jedisCache;
    /** 配置集合 */
    private ConcurrentHashMap<String, String> configMap = new ConcurrentHashMap<>();
    /** 资源集合 */
    private ConcurrentHashMap<String, IndexResource> resourceMap = new ConcurrentHashMap<>();
    @Autowired
    private CacheManager cacheManager;

    private Cache picturesCache;

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

        picturesCache = cacheManager.getCache("pictures");
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
                    testResource = JSON.parseObject(jedisCache.get(JedisKey.JOKE_RESOURCE_CONFIG_TEST), IndexResource.class);
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
                        IndexResource backResource = resourceMap.get(JedisKey.INDEX_CACHE_BACK);
                        if(backResource == null){
                            backResource = JSON.parseObject(jedisCache.get(JedisKey.JOKE_RESOURCE_CONFIG_BACK), IndexResource.class);
                            resourceMap.put(JedisKey.INDEX_CACHE_BACK, indexResource);
                        }
                        indexResource = backResource;
                        log.error("{}的首页请求失效,正在使用备用内容!", did);
                    }
                    resourceMap.put(JedisKey.INDEX_CACHE_INDEX, indexResource);
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
            IndexResource backResource = resourceMap.get(JedisKey.INDEX_CACHE_BACK);
            if(backResource == null){
                backResource = JSON.parseObject(jedisCache.get(JedisKey.JOKE_RESOURCE_CONFIG_BACK), IndexResource.class);
                resourceMap.put(JedisKey.INDEX_CACHE_BACK, backResource);
            }
            model.addAttribute(JedisKey.INDEX_CACHE_INDEX, backResource);
            model.addAttribute(JedisKey.INDEX_CACHE_CONFIG, configMap.get(DEFAULT_DID));

            log.error("已使用备用配置替代处理, 非法请求参数:{}", did);
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

    /**
     * 获取列表
     * @param did
     * @param cid 1:趣图、2:段子、3:推荐、4:精选
     * @param page
     * @param limit
     * @return
     */
    public List<Joke> list(Integer did, Integer cid, Integer page, Integer limit) {
        long start = System.currentTimeMillis();
        long s = (page - 1) * limit;
        long e = page * limit - 1;
        Set<String> keys = jedisCache.zrevrange(JedisKey.SORTEDSET_COMMON_CHANNEL + cid, s , e);
        List<Joke> list = Lists.newArrayList();
        if(!CollectionUtils.isEmpty(keys)){
            for(String id : keys){
                Joke joke = getJoke(id, cid);
                if(joke != null){
                    list.add(joke);
                }
            }
        }
        long end = System.currentTimeMillis();
        log.info("getPictures 总耗时:{}, 获取key:{}, 获取value:{}", FormatUtil.getTimeStr(end-start));
        return list;

    }
    /**
     * 获取趣图
     * @param id
     * @return
     */
    public Joke getJoke(String id, Integer cid) {
        Element element = picturesCache.get(cid+id);
        Joke joke;
        if(element == null){
            joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + id), Joke.class);
            if(joke != null){
                picturesCache.put(new Element(cid+id, joke));
            }
        } else {
            joke = (Joke) element.getObjectValue();
        }
        return joke;
    }

    /**
     * 获取段子详情页
     * @param did
     * @param cid
     * @param jid
     * @return
     */
    public JokeDetail getJokeDetail(Integer did, Integer cid, Integer jid) {
        JokeDetail joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + jid),JokeDetail.class);
        if(joke != null){
            if(joke.getType() == Constants.JOKE_TYPE_IMG){
                joke.setImg(IMG_PREFIX + joke.getImg());
            }else if(joke.getType() == Constants.JOKE_TYPE_GIF){
                joke.setImg(IMG_PREFIX + joke.getImg());
                joke.setGif(IMG_PREFIX + joke.getGif());
            }
            String key = JedisKey.JOKE_CHANNEL + cid;
            Long index = jedisCache.zrevrank(key, String.valueOf(jid));
            if(index != null){
//    			获取上一条段子和下一条编号
                if(index > 0){
                    Set<String> jokeLastIds = jedisCache.zrevrange(key, index -1, index + 1);
                    if(!CollectionUtils.isEmpty(jokeLastIds)){
                        int seq = 0;
                        for(String jokeLastId : jokeLastIds){
                            if(seq == 0){
                                joke.setLastId(Integer.valueOf(jokeLastId));
                            }
                            seq++;
                            if(seq == 2){
                                joke.setNextId(Integer.valueOf(jokeLastId));
                            }
                        }
                    }
                } else {
//        		    获取下一条段子编号
                    Set<String> jokeNextIds = jedisCache.zrevrange(key, index + 1, index + 1);
                    if (!CollectionUtils.isEmpty(jokeNextIds)) {
                        for (String jokeNextId : jokeNextIds) {
                            joke.setNextId(Integer.valueOf(jokeNextId));
                        }
                    }
                }
            }
            handleJokeDetail(joke); // 处理段子推荐信息
        }
        return joke;
    }

    /**
     * 处理段子详情中推荐段子
     *
     * @param jokeDetail
     */
    private void handleJokeDetail(JokeDetail jokeDetail) {
        //随机获取相关图片段子
        List<String> relatedImgIdList = jedisCache.srandmember(JedisKey.SET_RELATED_JOKE_IMG, 4);
        if (!CollectionUtils.isEmpty(relatedImgIdList)) {
            List<Joke> relatedImgList = Lists.newArrayList();
            Joke joke = null;
            for (String jokeId : relatedImgIdList) {
                joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + jokeId), Joke.class);
                if (joke != null) {
                    if (joke.getImg() != null) {
                        joke.setImg(joke.getImg().replace("_600x_", "_200x_"));
                        joke.setImg(IMG_PREFIX + joke.getImg());
                        joke.setHeight(FormatUtil.getHeight(joke.getHeight(), joke.getWidth(), 200));
                        joke.setWidth(200);
                    }
                    relatedImgList.add(joke);
                }
            }
            jokeDetail.setRelatedImg(relatedImgList);
        }
    }

    /**
     * 获取banner
     * @param cid
     * @return
     */
    public List<Banner> getBannerList(Integer cid) {
        String key = JedisKey.JOKE_BANNER + cid;
        Set<String> bannerSet = jedisCache.zrange(key, 0, -1);
        List<Banner> bannerList = new ArrayList<>();
        Banner banner;
        if(!CollectionUtils.isEmpty(bannerSet)){
            for(String b:bannerSet){
                banner = JSON.parseObject(jedisCache.get(JedisKey.STRING_BANNER + b),Banner.class);
                if(null != banner){
                    bannerList.add(banner);
                }
            }
        }
        return bannerList;
    }
}
