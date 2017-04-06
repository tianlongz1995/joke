package com.oupeng.joke.back.task;

import com.oupeng.joke.back.service.BannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * banner的定时发布任务
 */
@Component
public class BannerPublishTask {
    private static final Logger logger = LoggerFactory.getLogger(BannerPublishTask.class);
    @Autowired
    private BannerService bannerService;

    /**
     * 发布banner
     * */
    @Scheduled(cron="0 0/5 * * * ?")
    public void publishBannerTask() {
        logger.info("开始发布banner数据...");
        int size = 0;
        List<Integer> list = bannerService.getBannerForPublish();
        if (!CollectionUtils.isEmpty(list)) {
            size = list.size();
            for (Integer bannerId : list) {
                bannerService.publishBannerNow(bannerId);
            }
        }
        logger.info("发布banner数据结束，共发布banner{}条", size);
    }
}
