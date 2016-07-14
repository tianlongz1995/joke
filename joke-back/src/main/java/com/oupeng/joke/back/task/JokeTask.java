package com.oupeng.joke.back.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Joke;

@Component
public class JokeTask {
	@Autowired
	private JokeService jokeService;
	@Autowired
	private JedisCache jedisCache;
	
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
				if(joke.getType() == Constants.JOKE_TYPE_TEXT){
					jedisCache.sadd(JedisKey.SET_RELATED_JOKE_TEXT, String.valueOf(joke.getId()));
					jedisCache.sadd(JedisKey.SET_RECOMMEDN_JOKE_TEXT, String.valueOf(joke.getId()));
				}else if(joke.getType() == Constants.JOKE_TYPE_IMG){
					jedisCache.sadd(JedisKey.SET_RELATED_JOKE_IMG, String.valueOf(joke.getId()));
				}
			}
		}
	}
}
