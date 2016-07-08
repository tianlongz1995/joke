package com.oupeng.joke.back.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Feedback;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Maps;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.dao.mapper.JokeMapper;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.JokeVerifyInfo;

import javax.annotation.PostConstruct;

@Service
public class JokeService {
	private static Logger logger = LoggerFactory.getLogger(JokeService.class);

	@Autowired
	private JokeMapper jokeMapper;

	@Autowired
	private JedisCache jedisCache;
	
	public List<Joke> getJokeListForVerify(Integer type,Integer status){
		return jokeMapper.getJokeList(type, status,null,null);
	}
	
	public void verifyJoke(Integer status,String ids,String user){
		jokeMapper.verifyJoke(status, ids, user);
	}
	
	public Joke getJokeById(Integer id){
		return jokeMapper.getJokeById(id);
	}

	/**
	 * 更新段子点赞数
	 */
	@PostConstruct
	public void jokeLikeCountUpdate(){
		new Thread(){
			public void run() {
				while(true){
					try{
						List<String> likeIdList = jedisCache.brpop(JedisKey.JOKE_LIST_LIKE, 60*5);
						if(!CollectionUtils.isEmpty(likeIdList)){
							String likeId = likeIdList.get(1);
							logger.info("update joke Like Count id:" + likeId);
							jokeMapper.updatejokeLikeCount(Integer.valueOf(likeId));
						}
					}catch(Exception e){
						logger.error("update joke Like Count error!",e);
					}
				}
			}
		}.start();
	}
	/**
	 * 更新段子被踩数
	 */
	@PostConstruct
	public void jokeStepCountUpdate(){
		new Thread(){
			public void run() {
				while(true){
					try{
						List<String> stepIdList = jedisCache.brpop(JedisKey.JOKE_LIST_STEP, 60*5);
						if(!CollectionUtils.isEmpty(stepIdList)){
							String stepId = stepIdList.get(1);
							logger.info("update joke step Count id:" + stepId);
							jokeMapper.updateJokeStepCount(Integer.valueOf(stepId));
						}
					}catch(Exception e){
						logger.error("update joke step Count error!",e);
					}
				}
			}
		}.start();
	}

	/**
	 * 存储段子反馈信息
	 */
	@PostConstruct
	public void insertJokeFeedback(){
		new Thread(){
			public void run() {
				while(true){
					try{
						List<String> feedbackList = jedisCache.brpop(JedisKey.JOKE_LIST_FEEDBACK, 60*5);
						if(!CollectionUtils.isEmpty(feedbackList)){
							String feedbackStr = feedbackList.get(1);
							logger.info("update joke feedback String:" + feedbackStr);
							Feedback feedback = JSON.parseObject(feedbackStr, Feedback.class);
							if(feedback != null){
								jokeMapper.insertJokeFeedback(feedback);
							}
						}
					}catch(Exception e){
						logger.error("update joke feedback Count error!",e);
					}
				}
			}
		}.start();
	}

	public void updateJoke(Integer id,String title,String img,String gif,String content,String user){
		Joke joke = new Joke();
		joke.setId(id);
		joke.setGif(gif);
		joke.setContent(content);
		joke.setImg(img);
		joke.setTitle(title);
		joke.setVerifyUser(user);
		if(StringUtils.isNotBlank(gif)){
			joke.setType(Constants.JOKE_TYPE_GIF);
		}else if(StringUtils.isNotBlank(img)){
			joke.setType(Constants.JOKE_TYPE_IMG);
		}else{
			joke.setType(Constants.JOKE_TYPE_TEXT);
		}
		jokeMapper.updateJoke(joke);
	}
	
	public Map<String,Integer> getJokeVerifyInfoByUser(String user){
		Map<String,Integer> map = Maps.newHashMap();
		List<JokeVerifyInfo> list = jokeMapper.getJokeVerifyInfoByUser(user);
		String textKey = "type0";
		String imgKey = "type1";
		String gifKey = "type2";
		if(!CollectionUtils.isEmpty(list)){
			for(JokeVerifyInfo jokeVerifyInfo : list){
				if(Constants.JOKE_TYPE_GIF == jokeVerifyInfo.getType()){
					map.put(gifKey, jokeVerifyInfo.getNum());
				}else if(Constants.JOKE_TYPE_IMG == jokeVerifyInfo.getType()){
					map.put(imgKey, jokeVerifyInfo.getNum());
				}else if(Constants.JOKE_TYPE_TEXT == jokeVerifyInfo.getType()){
					map.put(textKey, jokeVerifyInfo.getNum());
				}
			}
		}
		if(!map.containsKey(textKey)) map.put(textKey, 0);
		if(!map.containsKey(imgKey)) map.put(imgKey, 0);
		if(!map.containsKey(gifKey)) map.put(gifKey, 0);
		return map;
	}
	
	public List<Joke> getJokeListForSearch(Integer id,String content){
		return jokeMapper.getJokeList(null,Constants.JOKE_STATUS_VALID,id,content);
	}
	
	public int getJokeCountForChannel(String contentType){
		return jokeMapper.getJokeCountForChannel(contentType);
	}
	
	public List<Joke> getJokeListForChannel(String contentType,Integer start,Integer size){
		return jokeMapper.getJokeListForChannel(contentType,start,size);
	}
}
