package com.oupeng.joke.back.task;

import java.text.SimpleDateFormat;
import java.util.Date;
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
	 * 发布段子数据，5分钟发布一次
	 * */
	@Scheduled(cron="0 */5 * * * ?")
	public void publishJoke(){
		String lastTime = jedisCache.get(JedisKey.JOKE_LAST_PUBLISH_TIME);
		String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		List<Joke> jokeList = jokeService.getJokeListForPublish(lastTime,currentTime);
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
		jedisCache.set(JedisKey.JOKE_LAST_PUBLISH_TIME, currentTime);
	}
}
