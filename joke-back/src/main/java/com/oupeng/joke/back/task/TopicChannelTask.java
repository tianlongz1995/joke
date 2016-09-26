package com.oupeng.joke.back.task;

import java.util.List;
import java.util.Map;

import com.oupeng.joke.domain.TopicCover;
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
	 * 定时更新专题封面
	 */
	@Scheduled(cron="0 0/10 * * * ?")
	public void updateTopicCover(){
		long start = System.currentTimeMillis();
		int size = 0;
		logger.info("定时更新专题封面开始...");
		try{
//		查询正常上线的专题封面
			List<TopicCover> list = topicService.getAllTopicCoverMoveList(Constants.ENABLE_STATUS);
			if(!CollectionUtils.isEmpty(list)){
				Map<String,Double> map = Maps.newHashMap();
				for(TopicCover topicCover : list){
					map.put(topicCover.getId().toString(), Double.valueOf(topicCover.getSeq()));
//					缓存专题封面
					jedisCache.set(JedisKey.STRING_TOPIC_COVER + topicCover.getId(), JSON.toJSONString(topicCover));
				}
//				更新专题封面列表缓存
				jedisCache.delAndSetSortedSet(JedisKey.SORTEDSET_TOPIC_COVER_LIST, map);
				size = map.size();
			} else {
				logger.error("定时更新专题封面失败, 在线专题封面为空!");
			}
		}catch (Exception e){
			logger.error("定时更新专题封面异常:" + e.getMessage(), e);
		}
		long end = System.currentTimeMillis();
		logger.info("定时更新专题封面结束: {}条记录更新, 耗时:{}ms", size, end - start);
	}

	/**
	 * 发布专题频道下的段子数据，每小时发布一次 05分的时候发布((审核通过且属于专题频道的数据))
	 **/
	@Scheduled(cron="0 0/1 * * * ?")
	public void publishTopicChannelJoke(){
		long start = System.currentTimeMillis();
		int size = 0, topicSize = 0;
		logger.info("定时发布专题开始...");
		List<Topic> topicList = topicService.getTopicForPublish();
		if(!CollectionUtils.isEmpty(topicList)){
			topicSize = topicList.size();
			StringBuffer log = new StringBuffer();
			log.append("topic joke publish result:\r\n");
			List<Integer> jokeIds;
			Map<String,Double> map = Maps.newHashMap();
			String key;
//			String[] dids = null;
//			循环处理待发布的专题
			for(Topic topic : topicList){
//				获取专题对应的段子编号
				jokeIds = jokeService.getJokeForPublishTopic(topic.getId());
				size += jokeIds.size();
				if(!CollectionUtils.isEmpty(jokeIds)){
					StringBuffer jokeIdStr = new StringBuffer();
					for(Integer jokeId : jokeIds){
						map.put(String.valueOf(jokeId), Double.valueOf(jokeId));
						jokeIdStr.append(jokeId).append(",");
					}
//					存储一个专题频道段子编号集合
					key = JedisKey.SORTEDSET_TOPIC_CHANNEL + topic.getId();
					jedisCache.zadd(key, map);
//					更新段子表中的状态为已发布
					jokeService.updateJokeForPublishChannel(jokeIdStr.deleteCharAt(jokeIdStr.lastIndexOf(",")).toString());
					map.clear();
					log.append(String.format("		[id:%d,name:%s,size:%d],\r\n",topic.getId(),topic.getTitle(),jokeIds.size()));
				}else{
					log.append(String.format("		[id:%d,name:%s,size:%d],\r\n",topic.getId(),topic.getTitle(),jokeIds.size()));
				}
//				将专题缓存到对应的专题类型列表中
				jedisCache.zadd(JedisKey.SORTEDSET_TOPIC_LIST + topic.getCoverId(), Double.valueOf(topic.getId()), String.valueOf(topic.getId()));
//				缓存专题
				jedisCache.set(JedisKey.STRING_TOPIC + topic.getId(), JSON.toJSONString(topic));
//				更新专题状态为已发布 -
				topicService.updateTopicStatus(topic.getId(), Constants.TOPIC_STATUS_PUBLISH);
			}
			logger.info(log.toString());
			mailService.sendMail(env.getProperty("data.publish.recipient"),env.getProperty("data.publish.cc"),"JOKE--Topic data publish", log.toString());
			logger.info("邮件发送成功");
		}
		long end = System.currentTimeMillis();
		logger.info("定时发布专题结束: {}条专题{}条段子更新, 耗时:{}ms", topicSize, size, end - start);
	}
}
