package com.oupeng.joke.back.service;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.ResourceMapper;
import com.oupeng.joke.domain.IndexItem;
import com.oupeng.joke.domain.IndexResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 资源服务接口
 * Created by hushuang on 2017/1/3.
 */
@Service
public class ResourceService {
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private JedisCache jedisCache;

    @Autowired
    private IndexCacheFlushService indexCacheFlushService;

    public IndexResource getIndexResource() {
        return resourceMapper.getIndexResource();
    }

    public IndexResource getIndexResourceBack() {
        return resourceMapper.getIndexResourceBack();
    }

    public IndexResource getIndexResourceTest() {
        return resourceMapper.getIndexResourceTest();
    }

    /**
     * 更新配置项
     * @param libJs
     * @param buildJs
     * @param buildCss
     * @param type
     * @return
     */
    public boolean updateIndex(String libJs, String buildJs, String buildCss, int type) {
        if(StringUtils.isBlank(libJs) || libJs.length() < 1 || libJs.length() > 30 ||
                StringUtils.isBlank(buildJs) || buildJs.length() < 1 || buildJs.length() > 30 ||
                StringUtils.isBlank(buildCss) || buildCss.length() < 1 || buildCss.length() > 30){
            return false;
        }
        IndexResource indexResource = new IndexResource();
        if(type == 1){
            resourceMapper.updateIndex(libJs, "10011", "10010");
            resourceMapper.updateIndex(buildJs, "10012", "10010");
            resourceMapper.updateIndex(buildCss, "10013", "10010");
            indexResource.setLibJs(libJs);
            indexResource.setBuildJs(buildJs);
            indexResource.setBuildCss(buildCss);
            jedisCache.set(JedisKey.JOKE_RESOURCE_CONFIG_INDEX, JSON.toJSONString(indexResource));

//        更新首页缓存
            indexCacheFlushService.updateIndex(new IndexItem(JedisKey.INDEX_CACHE_INDEX, type));
        } else if(type == 2){
            resourceMapper.updateIndex(libJs, "10021", "10020");
            resourceMapper.updateIndex(buildJs, "10022", "10020");
            resourceMapper.updateIndex(buildCss, "10023", "10020");
            indexResource.setLibJs(libJs);
            indexResource.setBuildJs(buildJs);
            indexResource.setBuildCss(buildCss);
            jedisCache.set(JedisKey.JOKE_RESOURCE_CONFIG_BACK, JSON.toJSONString(indexResource));
//        更新首页缓存
            indexCacheFlushService.updateIndex(new IndexItem(JedisKey.INDEX_CACHE_BACK, type));
        } else if(type == 3){
            resourceMapper.updateIndex(libJs, "10031", "10030");
            resourceMapper.updateIndex(buildJs, "10032", "10030");
            resourceMapper.updateIndex(buildCss, "10033", "10030");
            indexResource.setLibJs(libJs);
            indexResource.setBuildJs(buildJs);
            indexResource.setBuildCss(buildCss);
            jedisCache.set(JedisKey.JOKE_RESOURCE_CONFIG_TEST, JSON.toJSONString(indexResource));
//        更新首页缓存
            indexCacheFlushService.updateIndex(new IndexItem(JedisKey.INDEX_CACHE_TEST, type));
        }
        return true;
    }
}
