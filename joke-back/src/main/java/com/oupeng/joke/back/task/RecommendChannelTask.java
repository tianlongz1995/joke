package com.oupeng.joke.back.task;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Maps;
import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Joke;

@Component
public class RecommendChannelTask {

	@Autowired
	private JokeService jokeService;
	@Autowired
	private JedisCache jedisCache;
	
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
}
