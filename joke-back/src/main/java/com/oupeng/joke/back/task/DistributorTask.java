package com.oupeng.joke.back.task;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.back.service.AdService;
import com.oupeng.joke.back.service.ChannelService;
import com.oupeng.joke.back.service.DistributorService;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Ad;
import com.oupeng.joke.domain.AdConfig;
import com.oupeng.joke.domain.ChannelMenu;
import com.oupeng.joke.domain.response.DistributorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by hushuang on 16/7/12.
 */
@Component
public class DistributorTask {
    private static final Logger logger = LoggerFactory.getLogger(DistributorTask.class);
    @Autowired
    private DistributorService distributorService;
    @Autowired
    private ChannelService channelService;

    @Autowired
    private JedisCache jedisCache;
    @Autowired
    private AdService adService;
    /**
     * 10分钟同步一次频道广告配置缓存
     * */
    @Scheduled(cron="0 0/10 * * * ?")
    public void syncDistributorAdConfig(){
        long start = System.currentTimeMillis();
        logger.info("syncDistributorAdConfig starting...");
        List<Integer> ids = distributorService.getDistributorIds();
        if(!CollectionUtils.isEmpty(ids)){
            int index = 0;
            for(Integer id : ids){
                List<ChannelMenu> channels = channelService.getDistributorChannelList(id);
                List<Ad> ads = adService.getDistributorAdList(id);
                AdConfig adConfig = new AdConfig(ads);
                DistributorConfig dcr = new DistributorConfig();
                if(!CollectionUtils.isEmpty(channels)){
                    dcr.setChannels(channels);
                }
                if(!CollectionUtils.isEmpty(ads)){
                    dcr.setAdConfig(adConfig);
                }
                String value = JSON.toJSONString(dcr);
                if(value != null && value.length() > 0){
                    jedisCache.hset(JedisKey.JOKE_HASH_DISTRIBUTOR_CONFIG, String.valueOf(id), value);
                }else{
                    logger.info("syncDistributorAdConfig did[{}] body is null" , id);
                }
                index++;
            }
            long end = System.currentTimeMillis();
            logger.info("syncDistributorAdConfig ids size:[{}] sync size:[{}]! time:[{}]ms", ids.size(),index,end-start);
        }else{
            logger.error("syncDistributorAdConfig ids size:[{}] error! ids is empty!", ids);
        }
    }
}
