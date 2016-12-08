package com.oupeng.joke.back.task;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.oupeng.joke.back.service.ChannelService;
import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.back.service.MailService;
import com.oupeng.joke.back.service.TopicService;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.back.util.FormatUtil;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Channel;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.Topic;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.oupeng.joke.cache.JedisKey.SORTEDSET_TOPIC_LIST;

/**
 * 缓存刷新任务
 */
@Component
public class FlushCacheTask {
    private static Logger logger = LoggerFactory.getLogger(FlushCacheTask.class);
    @Autowired
    private JokeService jokeService;
    @Autowired
    private JedisCache jedisCache;
    @Autowired
    private MailService mailService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private Environment env;
    @Autowired
    private ChannelService channelService;
    /**
     * 文字权重
     */
    private static Integer TEXT_WEIGHT = 2;
    /**
     * 静图权重
     */
    private static Integer IMG_WEIGHT = 2;
    /**
     * 动图权重
     */
    private static Integer GIF_WEIGHT = 1;
    /**
     * 动图权重
     */
    private static long FLUSH_INTERVAL = 30 * 60 * 1000;


    /**
     * 发布段子数据，5分钟发布一次
     */
    @PostConstruct
    public void init() {
        logger.info("【缓存刷新任务】 start...");
        new Thread(new FlushCacheTaskThread(), "").start();
    }

    class FlushCacheTaskThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    if (jedisCache != null) {
                        List<String> list = jedisCache.brpop(JedisKey.JOKE_LIST_FLUSH_CACHE, 60 * 5);
                        long start = System.currentTimeMillis();
                        if (!CollectionUtils.isEmpty(list)) {
                            logger.info("【缓存刷新任务】Received:[{}] process start ...", list.get(1));
                            String channelStr = list.get(1);
                            Channel channel = JSON.parseObject(channelStr, Channel.class);

                            if (channel != null && channel.getType() != null && channel.getId() != null) {
//                              频道缓存处理时间间隔必须大于半小时
                                String lastFlushCacheTimeStr = jedisCache.get(JedisKey.JOKE_STRING_LAST_FLUSH_CACHE_TIME + channel.getId());
                                if (StringUtils.isNumeric(lastFlushCacheTimeStr) && lastFlushCacheTimeStr.length() > 10) {
                                    long lastTime = Long.valueOf(lastFlushCacheTimeStr);
                                    long currentTime = System.currentTimeMillis();
                                    if (currentTime - lastTime < FLUSH_INTERVAL) {
                                        logger.info("【缓存刷新任务】中频道[{}]半小时内刷新过, 暂不处理!", channel.getId());
                                        continue;
                                    }
                                }

//                              将频道下的缓存刷新 - 1. 没有缓存就重置、2. 有缓存就排除无效缓存
                                if (channel.getType().intValue() == 2) { // 推荐频道
                                    Long count = jedisCache.zcard(JedisKey.SORTEDSET_RECOMMEND_CHANNEL);
                                    if (count < 1) {
//                                    重构推荐缓存
                                        reconstructionRecommendCache();
                                    } else {
//                                        检查推荐频道缓存数据是否正常
                                        checkRecommendCacheContents();
                                    }
                                } else if (channel.getType().intValue() == 1) { // 专题频道
                                    Long count = jedisCache.zcard(SORTEDSET_TOPIC_LIST);
                                    if (count < 1) {
//                                    重构专题频道缓存
                                        reconstructionTopicCache();
                                    } else {
//                                        检查专题频道缓存数据是否正常
                                        checkTopicCacheContents();
                                    }
                                } else {  // 普通频道
                                    Long count = jedisCache.zcard(JedisKey.SORTEDSET_COMMON_CHANNEL + channel.getId());
                                    if (count < 1) {
//                                    重构普通频道缓存
                                        reconstructionCommonCache(channel.getId());
                                    } else {
//                                        检查普通频道缓存数据是否正常
                                        checkCommonCacheContents(channel.getId());
                                    }
                                }
//                                记录已处理时间
                                jedisCache.set(JedisKey.JOKE_STRING_LAST_FLUSH_CACHE_TIME + channel.getId(), String.valueOf(System.currentTimeMillis()));

                                long end = System.currentTimeMillis();
                                logger.info("【缓存刷新任务】执行完成, 处理类型[{}]-频道[{}]缓存耗时:[{}]", channel.getType(), channel.getId(), FormatUtil.getTimeStr(end - start));
                            } else {
                                mailService.sendMail(env.getProperty("data.publish.recipient"),
                                        env.getProperty("data.publish.cc"),
                                        "【段子】【后台】刷新缓存异常",
                                        "【段子】【后台】待刷新频道为空, 【缓存刷新任务】无法确定要刷新的缓存信息!");
                                logger.error("【段子】【后台】待刷新频道为空, 【缓存刷新任务】无法确定要刷新的缓存信息!");
                            }
                        } else {
                            logger.error("Received:[{}] null", list);
                        }
                    } else {
//                        发邮件,暂停3分钟
                        mailService.sendMail(env.getProperty("data.publish.recipient"),
                                env.getProperty("data.publish.cc"),
                                "【段子】【后台】刷新缓存异常",
                                "【段子】【后台】Redis缓存服务为空, 【缓存刷新任务】无法操作缓存!");
                        logger.error("【段子】【后台】Redis缓存服务为空, 【缓存刷新任务】无法操作缓存!");
                        Thread.sleep(3 * 60 * 1000);
                    }
                } catch (Exception e) {
                    logger.error("【缓存刷新任务】执行异常:" + e.getMessage(), e);
                }
            }
        }


    }

    /**
     * 检查普通频道缓存数据是否正常
     */
    private void checkCommonCacheContents(Integer cid) {
        //缓存中段子编号数据
        String key = JedisKey.SORTEDSET_COMMON_CHANNEL + cid;
        Set<String> commonList = jedisCache.zrange(key, 0, -1);
        //无效id缓存key
        List<String> invalidListSetKey = new ArrayList<>();
        //无效内容缓存key
        List<String> invalidListStringKey = new ArrayList<>();
        String jokeContent;
        if (!CollectionUtils.isEmpty(commonList)) {
            for (String jokeId : commonList) {
                jokeContent = jedisCache.get(JedisKey.STRING_JOKE + jokeId);
                if (!StringUtils.isNotBlank(jokeContent)) {
                    if (StringUtils.isNumeric(jokeId)) {
                        invalidListSetKey.add(jokeId);
                        invalidListStringKey.add(JedisKey.STRING_JOKE + jokeId);
                    }
                }
            }
        }
        //删除无效段子
        if (!CollectionUtils.isEmpty(invalidListSetKey)) {
            jedisCache.zrem(key, invalidListSetKey.toArray(new String[invalidListSetKey.size()]));
            jedisCache.del(invalidListStringKey.toArray(new String[invalidListStringKey.size()]));
            logger.info("普通频道缓存检查：删除无效数据：{}条", invalidListSetKey.size());
        } else {
            logger.info("普通频道缓存检查：无无效数据");
        }
    }

    /**
     * 检查专题频道缓存内容
     */
    private void checkTopicCacheContents() {
        Set<String> TopicIdList = jedisCache.zrange(JedisKey.SORTEDSET_TOPIC_LIST, 0, -1);
        //无效 id 缓存 key
        List<String> invalidListSetKey = new ArrayList<>();
        //无效内容 缓存 key
        List<String> invalidListStringKey = new ArrayList<>();
        Set<String> jokeIdSet;
        //joke内容
        String jokeContent;
        for (String topicId : TopicIdList) {
            jokeIdSet = jedisCache.zrange(JedisKey.SORTEDSET_TOPIC_JOKE_SET + topicId, 0, -1);
            //专题 joke内容
            if (!CollectionUtils.isEmpty(jokeIdSet)) {
                for (String jokeId : jokeIdSet) {
                    jokeContent = jedisCache.get(JedisKey.STRING_JOKE + jokeId);
                    if (!StringUtils.isNotBlank(jokeContent)) {
                        if (StringUtils.isNumeric(jokeId)) {
                            invalidListSetKey.add(jokeId);
                            invalidListStringKey.add(JedisKey.STRING_JOKE + jokeId);
                        }
                    }
                }

            }
            if (!CollectionUtils.isEmpty(invalidListSetKey)) {
                jedisCache.zrem(JedisKey.SORTEDSET_TOPIC_JOKE_SET + topicId, invalidListSetKey.toArray(new String[invalidListSetKey.size()]));
                jedisCache.del(invalidListStringKey.toArray(new String[invalidListStringKey.size()]));
                logger.info("专题频道缓存检查：删除无效数据：{}条", invalidListSetKey.size());
            } else {
                logger.info("专题频道缓存检查：无无效数据");
            }
        }
    }

    /**
     * 检查推荐频道缓存内容
     */
    private void checkRecommendCacheContents() {
        //获取推荐频道下joke id
        Set<String> recommendList = jedisCache.zrange(JedisKey.SORTEDSET_RECOMMEND_CHANNEL, 0, -1);
        //无效id 缓存 key
        List<String> invalidListSetKey = new ArrayList<>();
        //无效内容 缓存 key
        List<String> invalidListStringKey = new ArrayList<>();
        String jokeContent;
        for (String jokeId : recommendList) {
            jokeContent = jedisCache.get(JedisKey.STRING_JOKE + jokeId);
            if (!StringUtils.isNotBlank(jokeContent)) {
                if (StringUtils.isNumeric(jokeId)) {
                    invalidListSetKey.add(jokeId);
                    invalidListStringKey.add(JedisKey.STRING_JOKE + jokeId);
                }
            }
        }
        //删除无效段子
        if (!CollectionUtils.isEmpty(invalidListSetKey)) {
            jedisCache.zrem(JedisKey.SORTEDSET_RECOMMEND_CHANNEL, invalidListSetKey.toArray(new String[invalidListSetKey.size()]));
            jedisCache.del(invalidListStringKey.toArray(new String[invalidListStringKey.size()]));
            logger.info("推荐频道缓存检查：删除无效数据：{}条", invalidListSetKey.size());
        } else {
            logger.info("推荐频道缓存检查：无无效数据");
        }

    }

    /**
     * 重构普通频道缓存
     *
     * @param cid
     */
    private void reconstructionCommonCache(Integer cid) {
        Channel channel = channelService.getChannelById(cid);
        if (channel != null && channel.getContentType() != null && channel.getSize() != null) {
            Map<String, Double> map = Maps.newHashMap();
//        待审核段子数量
            int surplusJokeCount;
//        未审核段子数量
            int notAuditedJokeCount = 0;
//        查询最近审核通过的数据
            List<Joke> jokes = jokeService.getJokeForPublishChannel(channel.getContentType(), channel.getSize());
            if (!CollectionUtils.isEmpty(jokes)) {
                StringBuffer jokeids = new StringBuffer();
                for (Joke joke : jokes) {
//	  按照段子审核时间进行权重
                    map.put(String.valueOf(joke.getId()), Double.valueOf(joke.getVerifyTime() != null ? joke.getVerifyTime().getTime() : joke.getId()));
                    jokeids.append(joke.getId()).append(",");
                }
                jedisCache.zadd(JedisKey.SORTEDSET_COMMON_CHANNEL + channel.getId(), map);
//            更新段子发布状态
                jokeService.updateJokeForPublishChannel(jokeids.deleteCharAt(jokeids.lastIndexOf(",")).toString());
                //	查询待审核段子数量
                surplusJokeCount = jokeService.getJokeCountForPublishChannel(channel.getContentType(), Constants.JOKE_STATUS_VALID);
                //	查询未审核的段子数
                notAuditedJokeCount = jokeService.getJokeCountForPublishChannel(channel.getContentType(), Constants.JOKE_STATUS_NOT_AUDITED);

                map.clear();
                logger.info("[id:{},name:{},size:{},surplus:{},notAudited:{}]", channel.getId(), channel.getName(), jokes.size(), surplusJokeCount, notAuditedJokeCount);
            } else {
                logger.info("[id:{},name{},size:{},surplus:{},notAudited:{}]", channel.getId(), channel.getName(), 0, 0, notAuditedJokeCount);
            }
        }
    }

    /**
     * 重构专题频道缓存
     */
    private void reconstructionTopicCache() {
        Map<String, Double> map = Maps.newHashMap();
        List<Topic> topicList = topicService.getTopicForPublish();
        List<Integer> jokeIdList;
        String key;
        for (Topic topic : topicList) {
            jokeIdList = jokeService.getJokeForPublishTopic(topic.getId());
            if (!CollectionUtils.isEmpty(jokeIdList)) {
                StringBuffer jokeids = new StringBuffer();
                for (Integer jokeId : jokeIdList) {
                    map.put(String.valueOf(jokeId), Double.valueOf(jokeId));
                    jokeids.append(jokeId).append(",");
                }
                key = JedisKey.SORTEDSET_TOPIC_JOKE_SET + topic.getId();
//					缓存专题段子集合
                jedisCache.zadd(key, map);
//					更新段子发布状态
                jokeService.updateJokeForPublishChannel(jokeids.deleteCharAt(jokeids.lastIndexOf(",")).toString());
                map.clear();
                logger.info("[id:{},name:{},size{}]", topic.getId(), topic.getTitle(), jokeIdList.size());
            } else {
                logger.info("[id:{},name:{},size{}]", topic.getId(), topic.getTitle(), jokeIdList.size());
            }
//				将专题段子集合编号加入到专题列表中
            jedisCache.zadd(SORTEDSET_TOPIC_LIST, Double.valueOf(topic.getId()), String.valueOf(topic.getId()));
//				缓存专题
            jedisCache.set(JedisKey.STRING_TOPIC + topic.getId(), JSON.toJSONString(topic));
//				更新专题发布状态
            topicService.updateTopicStatus(topic.getId(), Constants.TOPIC_STATUS_PUBLISH);
        }
    }

    /**
     * 重构推荐缓存
     */
    private void reconstructionRecommendCache() {
//        查询平台所有段子信息
        List<Joke> gifJokeList = jokeService.getPublishJokeListByType(Constants.JOKE_TYPE_GIF, null);
        List<String> imgJokeList = jokeService.getJokeListForPublishRecommend(Constants.JOKE_TYPE_IMG, null);
        List<String> textJokeList = jokeService.getJokeListForPublishRecommend(Constants.JOKE_TYPE_TEXT, null);
        Map<String, Double> map = Maps.newHashMap();
        Double baseScore = Double.parseDouble(new SimpleDateFormat("yyyyMMdd000000").format(new Date()));
//        if (CollectionUtils.isEmpty(gifJokeList) || CollectionUtils.isEmpty(imgJokeList) || CollectionUtils.isEmpty(textJokeList)) {
//            return;
//        }
        int gifSize = gifJokeList.size();
        int imgSize = imgJokeList.size();
        int textSize = textJokeList.size();
        int total = gifSize + imgSize + textSize;
        int gif = GIF_WEIGHT, img = IMG_WEIGHT, text = TEXT_WEIGHT;
//		int index = total;
        int seq = 0;
//		根据权重将数据存储Map中
        for (int index = total; index > 0; index--) {
            if (gifSize - 1 > 0) { //动图缓存添加完
                if (gif > 0) {      //频道权重是否用完
                    map.put(gifJokeList.get(gifSize - 1).getId().toString(), baseScore + seq);
                    gif--;
                    gifSize--;
                    seq++;
                }
            }
            if (imgSize - 1 > 0) { //图片缓存添加完
                if (img > 0) {      //频道权重是否用完
                    map.put(imgJokeList.get(imgSize - 1), baseScore + seq);
                    img--;
                    imgSize--;
                    seq++;
                }
            }else {
                img = 0;
            }
            if (textSize - 1 > 0) { //文字缓存添加完
                if (text > 0) {     //频道权重是否用完
                    map.put(textJokeList.get(textSize - 1), baseScore + seq);
                    text--;
                    textSize--;
                    seq++;
                }
            }else{
                text = 0;
            }
//			使用的权重使用完就重新进行一轮权重轮询
            if (gif == 0 && img == 0 && text == 0) {
                gif = GIF_WEIGHT;
                img = IMG_WEIGHT;
                text = TEXT_WEIGHT;
            }
//			map中数量与总数对应上说明数据记录完成;
            if (map.size() == total) {
                break;
            }
        }
        jedisCache.zadd(JedisKey.SORTEDSET_RECOMMEND_CHANNEL, map);
        logger.info("Recommend Publish over:{}", map.size());
        map.clear();

    }
}
