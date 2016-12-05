package com.oupeng.joke.back.task;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.oupeng.joke.back.service.ChannelService;
import com.oupeng.joke.back.service.DistributorService;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.back.util.FormatUtil;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Channel;
import com.oupeng.joke.domain.Distributor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 渠道、频道编号名称表缓存任务
 * Created by hushuang on 2016/12/8.
 */
@Component
public class DistributorAndChannelCacheMapTask {
    private static final Logger logger = LoggerFactory.getLogger(CommonChannelTask.class);

    @Autowired
    private ChannelService channelService;
    @Autowired
    private JedisCache jedisCache;
    @Autowired
    private DistributorService distributorService;


    /**
     * 渠道、频道编号名称表缓存任务
     */
    @Scheduled(cron = "0 03 0/1 * * ?")
    public void scheduled() {
        new Thread(new DistributorAndChannelCacheMapThread(), "渠道频道编号名称缓存表任务处理线程").start();
    }

    class DistributorAndChannelCacheMapThread implements Runnable {

        @Override
        public void run() {
            logger.info("渠道、频道编号名称表缓存任务启动...");
            long start = System.currentTimeMillis();
            try {
                List<Channel> channelList = channelService.getChannelList(Constants.ENABLE_STATUS);
                Map<String, String> cMap = Maps.newHashMap();
                if (!CollectionUtils.isEmpty(channelList)) {
                    for (Channel channel : channelList) {
                        cMap.put(channel.getId().toString(), channel.getName());
                    }
                    if (!cMap.isEmpty()) {
                        jedisCache.set(JedisKey.JOKE_STRING_CHANNEL_MAP, JSON.toJSONString(cMap));
                    }
                }
                List<Distributor> distributorList = distributorService.getAllDistributorList();
                Map<String, String> dMap = Maps.newHashMap();
                if (!CollectionUtils.isEmpty(distributorList)) {
                    for (Distributor distributor : distributorList) {
                        dMap.put(distributor.getId().toString(), distributor.getName());
                    }
                    if (!dMap.isEmpty()) {
                        jedisCache.set(JedisKey.JOKE_STRING_DISTRIBUTOR_MAP, JSON.toJSONString(dMap));
                    }
                }

                long end = System.currentTimeMillis();
                logger.info("渠道、频道编号名称表缓存任务完成: 渠道[{}]条、频道[{}]条, 耗时:{}", dMap.size(), cMap.size(), FormatUtil.getTimeStr(end-start));
            } catch (Exception e) {
                logger.error("渠道、频道编号名称表缓存任务处理异常:" + e.getMessage(), e);
            }
        }
    }
}
