package com.oupeng.joke.back.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.TopicMapper;
import com.oupeng.joke.domain.Topic;

@Service
public class TopicService {

	@Autowired
	private TopicMapper topicMapper;
	@Autowired
	private JedisCache jedisCache;
	
	public List<Topic> getTopicList(Integer status){
		return topicMapper.getTopicList(status);
	}
	
	public Topic getTopicById(Integer id){
		return topicMapper.getTopicById(id);
	}
	
	public String updateTopicStatus(Integer id,Integer status){
		String result = null;
		if(Constants.TOPIC_STATUS_VALID == status){
			result = validTopic(topicMapper.getTopicById(id));
		}else{
			jedisCache.del(JedisKey.SORTEDSET_TOPIC_CHANNEL + id);
			
			Set<String> keys = jedisCache.keys(JedisKey.SORTEDSET_DISTRIBUTOR_TOPIC + "*");
			if(!CollectionUtils.isEmpty(keys)){
				for(String key : keys){
					jedisCache.zrem(key, String.valueOf(id));
				}
			}
		}
		if(result == null){
			topicMapper.updateTopicStatus(id, status);
		}
		return result;
	}
	
	public void insertTopic(String title,String img,String content,String dids,String publishTime){
		Topic topic = new Topic();
		topic.setContent(content);
		topic.setDids(dids);
		topic.setImg(img);
		topic.setPublishTimeString(publishTime);
		topic.setTitle(title);
		topicMapper.insertTopic(topic);
	}
	
	public void updateTopic(Integer id,String title,String img,String content,String dids,String publishTime){
		Topic topic = new Topic();
		topic.setId(id);
		topic.setContent(content);
		topic.setDids(dids);
		topic.setImg(img);
		topic.setPublishTimeString(publishTime);
		topic.setTitle(title);
		topicMapper.updateTopic(topic);
	}
	
	public void addTopicJoke(String jokeIds,Integer topicId){
		if(StringUtils.isNotBlank(jokeIds)){
			for(String jokeId : jokeIds.split(",")){
				topicMapper.insertTopicJoke(Integer.parseInt(jokeId), topicId);
			}
		}
	}
	
	public List<Topic> getTopicForPublish(){
		return topicMapper.getTopicForPublish();
	}
	
	private static String validTopic(Topic topic){
		String result = null;
		if(topic.getPublishTime() == null){
			result = "专题的发布时间不能为空";
		}else if(StringUtils.isBlank(topic.getDids())){
			result = "专题的渠道不能为空";
		}else if(StringUtils.isBlank(topic.getTitle())){
			result = "专题的主题不能为空";
		}else if(StringUtils.isBlank(topic.getImg())){
			result = "专题的图片不能为空";
		}else if(StringUtils.isBlank(topic.getContent())){
			result = "专题的简介不能为空";
		}else{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.HOUR_OF_DAY, 1);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			if(topic.getPublishTime().compareTo(calendar.getTime()) < 0){
				result = "发布时间最少要在下一个小时";
			}
		}
		return result;
	}
}
