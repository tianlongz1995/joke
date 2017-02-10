package com.oupeng.joke.back.task;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.back.service.BannerService;
import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Banner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * banner的定时发布任务
 */
@Component
public class BannerChannelTask {

    private static final Logger logger = LoggerFactory.getLogger(BannerChannelTask.class);
    @Autowired
    private BannerService bannerService;
    @Autowired
    private JedisCache jedisCache;

    /**
     * 发布banner
     * */
    @Scheduled(cron="0 0/5 * * * ?")
    public void publishTopicChannelJoke() {
        logger.info("开始发布banner数据...");
        List<Banner> bannerList = bannerService.getBannerForPublish();
        if (!CollectionUtils.isEmpty(bannerList)) {
            for (Banner banner : bannerList) {
                String bannerKey = JedisKey.STRING_BANNER + banner.getId();
                String bannerListKey = JedisKey.JOKE_BANNER + banner.getCid();
                jedisCache.set(bannerKey, JSON.toJSONString(banner));
                jedisCache.zadd(bannerListKey, System.currentTimeMillis(), Integer.toString(banner.getCid()));
                bannerService.updateBannerStatus(banner.getId(),3);
            }
        }
        logger.info("发布banner数据结束，共发布banner{}条",bannerList.size());
    }
}
