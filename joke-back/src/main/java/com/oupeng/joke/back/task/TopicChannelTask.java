package com.oupeng.joke.back.task;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.back.service.MailService;
import com.oupeng.joke.back.service.TopicService;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Topic;

@Component
public class TopicChannelTask {

	private static final Logger logger = LoggerFactory.getLogger(CommonChannelTask.class);
	
	@Autowired
	private JokeService jokeService;
	@Autowired
	private TopicService topicService;
	@Autowired
	private JedisCache jedisCache;
	@Autowired
	private MailService mailService;
	@Autowired
	private Environment env;
	
	/**
	 * 发布专题频道下的段子数据，每 10分种的时候发布一次((审核通过且属于专题频道的数据))
	 * */
	@Scheduled(cron="0 10 * * * ?")
	public void publishTopicChannelJoke(){
		List<Topic> topicList = topicService.getTopicForPublish();
		if(!CollectionUtils.isEmpty(topicList)){
			StringBuffer log = new StringBuffer();
			log.append("topic joke publish result:\r\n");
			List<Integer> jokeIds = null;
			Map<String,Double> map = Maps.newHashMap();
			String key = null;
			String[] dids = null;
			for(Topic topic : topicList){
				jokeIds = jokeService.getJokeForPublishTopic(topic.getId());
				if(!CollectionUtils.isEmpty(jokeIds)){
					StringBuffer jokeids = new StringBuffer();
					for(Integer jokeId : jokeIds){
						map.put(String.valueOf(jokeId), Double.valueOf(jokeId));
						jokeids.append(jokeId).append(",");
					}
					key = JedisKey.SORTEDSET_TOPIC_CHANNEL + topic.getId();
					jedisCache.zadd(key, map);
					jokeService.updateJokeForPublishChannel(jokeids.deleteCharAt(jokeids.lastIndexOf(",")).toString());
					map.clear();
					log.append(String.format("		[id:%d,name:%s,size:%d],\r\n",topic.getId(),topic.getTitle(),jokeIds.size()));
				}else{
					log.append(String.format("		[id:%d,name:%s,size:%d],\r\n",topic.getId(),topic.getTitle(),jokeIds.size()));
				}
				
				
				dids = topic.getDids().split(",");
				for(String did : dids){
					topic.setDids(null);
					jedisCache.zadd(JedisKey.SORTEDSET_DISTRIBUTOR_TOPIC + did,Double.valueOf(topic.getId()),JSON.toJSONString(topic));
				}
				topicService.updateTopicStatus(topic.getId(), Constants.TOPIC_STATUS_PUBLISH);
			}
			logger.info(log.toString());
			mailService.sendMail(env.getProperty("data.publish.recipient"),env.getProperty("data.publish.cc"),"JOKE--Topic data publish", log.toString());
			logger.info("邮件发送成功");
		}
	}
}
