package com.oupeng.joke.back.task;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.back.service.MailService;
import com.oupeng.joke.back.service.TopicService;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

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
	 * 发布专题频道下的段子数据，每小时发布一次(审核通过且属于专题频道的数据)
	 * */
	@Scheduled(cron="0 5 * * * ?")
	public void publishTopicChannelJoke(){
		logger.info("开始发布专题频道数据...");
		List<Topic> topicList = topicService.getTopicForPublish();
		if(!CollectionUtils.isEmpty(topicList)){
			StringBuffer log = new StringBuffer();
			log.append("topic joke publish result:\r\n");
			List<Integer> jokeIds;
			Map<String,Double> map = Maps.newHashMap();
			String key;
			for(Topic topic : topicList){
				jokeIds = jokeService.getJokeForPublishTopic(topic.getId());
				if(!CollectionUtils.isEmpty(jokeIds)){
					StringBuffer jokeids = new StringBuffer();
					for(Integer jokeId : jokeIds){
						map.put(String.valueOf(jokeId), Double.valueOf(jokeId));
						jokeids.append(jokeId).append(",");
					}
					key = JedisKey.SORTEDSET_TOPIC_JOKE_SET + topic.getId();
//					缓存专题段子集合
					jedisCache.zadd(key, map);
//					更新段子发布状态
					jokeService.updateJokeForPublishChannel(jokeids.deleteCharAt(jokeids.lastIndexOf(",")).toString());
					map.clear();
					log.append(String.format("		[id:%d,name:%s,size:%d],\r\n",topic.getId(),topic.getTitle(),jokeIds.size()));
				}else{
					log.append(String.format("		[id:%d,name:%s,size:%d],\r\n",topic.getId(),topic.getTitle(),jokeIds.size()));
				}
//				将专题段子集合编号加入到专题列表中
				jedisCache.zadd(JedisKey.SORTEDSET_TOPIC_LIST, Double.valueOf(topic.getId()), String.valueOf(topic.getId()));
//				缓存专题
				jedisCache.set(JedisKey.STRING_TOPIC + topic.getId(), JSON.toJSONString(topic));
//				更新专题发布状态
				topicService.updateTopicStatus(topic.getId(), Constants.TOPIC_STATUS_PUBLISH);
			}
			logger.info(log.toString());
			mailService.sendMail(env.getProperty("data.publish.recipient"),env.getProperty("data.publish.cc"),"JOKE--Topic data publish", log.toString());
			logger.info("邮件发送成功");
		}
	}
}
