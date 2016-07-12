package com.oupeng.joke.back.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.oupeng.joke.back.service.SourceService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.oupeng.joke.back.service.ChannelService;
import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.back.service.TopicService;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Channel;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.Topic;

@Component
public class JokeTask {
	private static final Logger logger = LoggerFactory.getLogger(JokeTask.class);
	@Autowired
	private JokeService jokeService;
	@Autowired
	private TopicService topicService;
	@Autowired
	private ChannelService channelService;
	@Autowired
	private JedisCache jedisCache;
	@Autowired
	private SourceService sourceService;
	
	/**
	 * 发布段子数据，每天凌晨5分时候发布，每次发布前一天数据
	 * */
	@Scheduled(cron="0 5 0 * * ?")
	public void publishJoke(){
		List<Joke> jokeList = jokeService.getJokeListForPublish();
		if(!CollectionUtils.isEmpty(jokeList)){
			for(Joke joke : jokeList){
				joke.setCreateTime(null);
				joke.setSourceId(null);
				joke.setStatus(null);
				joke.setUpdateTime(null);
				joke.setVerifyTime(null);
				joke.setVerifyUser(null);
				jedisCache.set(JedisKey.STRING_JOKE + joke.getId(), JSON.toJSONString(joke));
			}
		}
	}
	
	/**
	 * 发布普通频道下的段子数据，每天凌晨30分时候发布，每次发布前一天数据(审核通过且不属于专题频道的数据)
	 * */
	@Scheduled(cron="0 30 0 * * ?")
	public void publishCommonChannelJoke(){
		List<Channel> channelList = channelService.getChannelList(Constants.CHANNEL_STATUS_VALID);
		if(!CollectionUtils.isEmpty(channelList)){
			List<Integer> jokeIds = null;
			Map<String,Double> map = Maps.newHashMap();
			for(Channel channel : channelList){
				if(StringUtils.isNotBlank(channel.getContentType())){
					jokeIds = jokeService.getJokeForPublishChannel(channel.getContentType());
					if(!CollectionUtils.isEmpty(jokeIds)){
						for(Integer jokeId : jokeIds){
							map.put(String.valueOf(jokeId), Double.valueOf(jokeId));
						}
						jedisCache.zadd(JedisKey.SORTEDSET_COMMON_CHANNEL+channel.getId(),map);
						map.clear();
					}
				}
			}
		}
	}
	
	/**
	 * 发布专题频道下的段子数据，每小时零 5分的时候发布一次((审核通过且属于专题频道的数据))
	 * */
	@Scheduled(cron="0 10 * * * ?")
	public void publishTopicChannelJoke(){
		List<Topic> topicList = topicService.getTopicForPublish();
		if(!CollectionUtils.isEmpty(topicList)){
			List<Integer> jokeIds = null;
			Map<String,Double> map = Maps.newHashMap();
			String key = null;
			String[] dids = null;
			for(Topic topic : topicList){
				jokeIds = jokeService.getJokeForPublishTopic(topic.getId());
				if(!CollectionUtils.isEmpty(jokeIds)){
					for(Integer jokeId : jokeIds){
						map.put(String.valueOf(jokeId), Double.valueOf(jokeId));
					}
					key = JedisKey.SORTEDSET_TOPIC_CHANNEL + topic.getId();
					jedisCache.zadd(key, map);
					map.clear();
				}
				
				
				dids = topic.getDids().split(",");
				for(String did : dids){
					topic.setDids(null);
					jedisCache.zadd(JedisKey.SORTEDSET_DISTRIBUTOR_TOPIC + did,Double.valueOf(topic.getId()),JSON.toJSONString(topic));
				}
				topicService.updateTopicStatus(topic.getId(), Constants.TOPIC_STATUS_PUBLISH);
			}
		}
	}
	
	/**
	 * 发布推荐频道下的段子数据，每天凌晨1点时候发布，每次发布当前点赞数前100的段子
	 * */
	@Scheduled(cron="0 0 1 * * ?")
	public void publishRecommendChannelJoke(){
		List<Joke> jokeList = jokeService.getJokeListForPublishRecommend();
		if(!CollectionUtils.isEmpty(jokeList)){
			Map<String,Double> map = Maps.newHashMap();
			for(Joke joke : jokeList){
				map.put(String.valueOf(joke.getId()), Double.valueOf(joke.getGood()));
			}
			jedisCache.zadd(JedisKey.SORTEDSET_RECOMMEND_CHANNEL,map);
			map.clear();
		}
	}

	/**
	 * 每天凌晨零点生成数据源抓取记录
	 * */
	@Scheduled(cron="0 0 0 * * ?")
	public void insertSourceMonitor(){
		logger.info("insertSourceMonitor starting...");
		List<Integer> ids = sourceService.getSourceMonitorIds(1);
		if(!CollectionUtils.isEmpty(ids)){
			String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
			sourceService.insertSourceMonitors(ids, Integer.valueOf(today));
			logger.info("insertSourceMonitor ids size:[{}] success!", ids.size());
		}else{
			logger.error("insertSourceMonitor ids size:[{}] error! ids is empty!", ids);
		}
	}
}
