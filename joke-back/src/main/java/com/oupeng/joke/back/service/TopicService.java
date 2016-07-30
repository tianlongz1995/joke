package com.oupeng.joke.back.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.oupeng.joke.dao.mapper.JokeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.back.util.HttpUtil;
import com.oupeng.joke.back.util.ImgRespDto;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.TopicMapper;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.Topic;

@Service
public class TopicService {
	@Autowired
	private TopicMapper topicMapper;
	@Autowired
	private JedisCache jedisCache;
	@Autowired
	private JokeMapper jokeMapper;
	@Autowired
	private Environment env;
	
	public List<Topic> getTopicList(Integer status){
		List<Topic> topicList = topicMapper.getTopicList(status);
		if(!CollectionUtils.isEmpty(topicList)){
			for(Topic topic : topicList){
				if(StringUtils.isNotBlank(topic.getImg())){
					topic.setImg( env.getProperty("img.real.server.url") + topic.getImg());
				}
			}
		}
		return topicList;
	}
	
	public Topic getTopicById(Integer id){
		Topic topic = topicMapper.getTopicById(id);
		if(topic != null && StringUtils.isNotBlank(topic.getImg())){
			topic.setImg(env.getProperty("img.real.server.url") + topic.getImg());
		}
		return topic;
	}
	
	public String updateTopicStatus(Integer id,Integer status){
		String result = null;
		if(Constants.TOPIC_STATUS_VALID == status){
			result = validTopic(topicMapper.getTopicById(id));
		}else if(Constants.TOPIC_STATUS_PUBLISH != status){
			Set<String> keys = jedisCache.keys(JedisKey.SORTEDSET_DISTRIBUTOR_TOPIC + "*");
			if(!CollectionUtils.isEmpty(keys)){
				for(String key : keys){
					jedisCache.zrem(key, String.valueOf(id));
				}
			}
			
			jedisCache.del(JedisKey.STRING_TOPIC + id);
			jedisCache.del(JedisKey.SORTEDSET_TOPIC_CHANNEL + id);
		}
		if(result == null){
			topicMapper.updateTopicStatus(id, status);
		}
		return result;
	}
	
	public boolean insertTopic(String title,String img,String content,String dids,String publishTime){
		Topic topic = new Topic();
		topic.setContent(content);
		topic.setDids(dids);
		String newImg = handleImg(img);
		if(StringUtils.isBlank(newImg)){
			return false;
		}
		topic.setImg(newImg);
		topic.setPublishTimeString(publishTime);
		topic.setTitle(title);
		topicMapper.insertTopic(topic);
		return true;
	}
	
	public boolean updateTopic(Integer id,String title,String img,String content,String dids,String publishTime){
		Topic topic = new Topic();
		topic.setId(id);
		topic.setContent(content);
		topic.setDids(dids);
		String newImg = handleImg(img);
		if(StringUtils.isBlank(newImg)){
			return false;
		}
		topic.setImg(newImg);
		topic.setPublishTimeString(publishTime);
		topic.setTitle(title);
		topicMapper.updateTopic(topic);
		return true;
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
	
	private String validTopic(Topic topic){
		if(topic.getPublishTime() == null){
			return "专题的发布时间不能为空";
		}else if(StringUtils.isBlank(topic.getDids())){
			return "专题的渠道不能为空";
		}else if(StringUtils.isBlank(topic.getTitle())){
			return "专题的主题不能为空";
		}else if(StringUtils.isBlank(topic.getImg())){
			return "专题的图片不能为空";
		}else if(StringUtils.isBlank(topic.getContent())){
			return "专题的简介不能为空";
		}else{
			List<Joke> jokeIdList =  getJokeListByTopicId(topic.getId());
			if(CollectionUtils.isEmpty(jokeIdList)){
				return "专题的内容不能为空";
			}else{
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.HOUR_OF_DAY, 1);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				if(topic.getPublishTime().compareTo(calendar.getTime()) < 0){
					return "发布时间最少要在下一个小时";
				}
			}
		}
		return null;
	}
	
	public void delTopicJoke(String jokeIds,Integer topicId){
		if(StringUtils.isNotBlank(jokeIds)){
			for(String jokeId : jokeIds.split(",")){
				topicMapper.delTopicJoke(Integer.parseInt(jokeId), topicId);
			}
		}
	}
	
	public List<Joke> getJokeListByTopicId(Integer id){
		List<Joke> jokeList = topicMapper.getJokeListByTopicId(id);
		if(!CollectionUtils.isEmpty(jokeList)){
			for(Joke joke : jokeList){
				if(joke.getType() == Constants.JOKE_TYPE_IMG){
					joke.setImg( env.getProperty("img.real.server.url") + joke.getImg());
				}else if(joke.getType() == Constants.JOKE_TYPE_GIF){
					joke.setImg( env.getProperty("img.real.server.url") + joke.getImg());
					joke.setGif( env.getProperty("img.real.server.url") + joke.getGif());
				}
			}
		}
		return jokeList;
	}
	
	public String handleImg(String imgUrl){
		if(StringUtils.isNotBlank(imgUrl)){
			ImgRespDto imgRespDto = HttpUtil.handleImg(env.getProperty("remote.crop.img.server.url"),imgUrl, false);
			if(imgRespDto != null && imgRespDto.getErrorCode() == 0){
				return imgRespDto.getImgUrl();
			}
		}
		return null;
	}

	/**
	 * 添加原创数据内容
	 * @param title
	 * @param imgUrl
	 * @param gifUrl
	 * @param content
	 * @param topicId
	 */
	public boolean addOriginalContent(String title, String imgUrl, String gifUrl, String content, Integer topicId) {
		boolean result = false;
		Joke joke = new Joke();
		joke.setContent(content);
		joke.setTitle(title);
		if(StringUtils.isNotBlank(gifUrl)){//动图
			joke.setType(Constants.JOKE_TYPE_GIF);
			ImgRespDto imgRespDto = HttpUtil.handleImg(env.getProperty("remote.crop.img.server.url"),gifUrl, true);
			if(imgRespDto != null && imgRespDto.getErrorCode() == 0){
				joke.setGif(imgRespDto.getGifUrl());
				joke.setImg(imgRespDto.getImgUrl());
				joke.setWidth(imgRespDto.getWidth());
				joke.setHeight(imgRespDto.getHeight());
				result = true;
			}
		}else if(StringUtils.isNotBlank(imgUrl)){//静图
			joke.setType(Constants.JOKE_TYPE_IMG);
			ImgRespDto imgRespDto = HttpUtil.handleImg(env.getProperty("remote.crop.img.server.url"),imgUrl, true);
			if(imgRespDto != null && imgRespDto.getErrorCode() == 0){
				joke.setGif(null);
				joke.setImg(imgRespDto.getImgUrl());
				joke.setWidth(imgRespDto.getWidth());
				joke.setHeight(imgRespDto.getHeight());
				result = true;
			}
		}else{//纯文本内容
			joke.setType(Constants.JOKE_TYPE_TEXT);
			result = true;
		}
		
		if(result){
			jokeMapper.insertJoke(joke);//存储段子信息
			topicMapper.insertTopicJoke(joke.getId(), topicId);//存储段子专题关联关系
		}
		return result;
	}
}
