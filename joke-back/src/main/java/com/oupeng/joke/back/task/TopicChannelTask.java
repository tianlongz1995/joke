package com.oupeng.joke.back.task;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.back.service.TopicService;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Topic;

@Component
public class TopicChannelTask {

	@Autowired
	private JokeService jokeService;
	@Autowired
	private TopicService topicService;
	@Autowired
	private JedisCache jedisCache;
	
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
}
