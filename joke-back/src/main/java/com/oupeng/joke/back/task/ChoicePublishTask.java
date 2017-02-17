package com.oupeng.joke.back.task;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.back.service.ChoiceService;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.ChoiceMapper;
import com.oupeng.joke.domain.Choice;
import com.oupeng.joke.domain.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 精选的定时发布任务
 */
@Component
public class ChoicePublishTask {

    private static final Logger logger = LoggerFactory.getLogger(ChoicePublishTask.class);

    @Autowired
    private ChoiceService choiceService;
    @Autowired
    private ChoiceMapper choiceMapper;
    @Autowired
    private JedisCache jedisCache;

    /**
     * 发布精选
     * */
    @Scheduled(cron="0 0/5 * * * ?")
    public void publishChoiceTask() {
        logger.info("开始发布精选数据...");
        List<Choice> choiceList = choiceService.getChoiceForPublish();
        //精选id列表，缓存key
        String choiceListKey = JedisKey.JOKE_CHANNEL + 4;
        if (!CollectionUtils.isEmpty(choiceList)) {
            for (Choice choice : choiceList) {
                String choiceKey = JedisKey.STRING_JOKE + choice.getId();

                if(choice.getCommentNumber()!=null){
                    choice.setComment(new Comment(choice.getCommentNumber(), null, null,null));
                }
                choice.setType(4);
                jedisCache.set(choiceKey, JSON.toJSONString(choice));
                jedisCache.zadd(choiceListKey, System.currentTimeMillis(), choice.getId().toString());
                //更新状态
                choiceMapper.updateChoiceStatus(choice.getId(),3);
            }
        }
        logger.info("发布精选数据结束，共发布精选{}条",choiceList.size());
    }

}
