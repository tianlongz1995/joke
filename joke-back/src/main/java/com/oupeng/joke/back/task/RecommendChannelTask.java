package com.oupeng.joke.back.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Maps;
import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;

@Component
public class RecommendChannelTask {
	private static String FIX = "000000";

	@Autowired
	private JokeService jokeService;
	@Autowired
	private JedisCache jedisCache;
	
	/**
	 * 发布推荐频道下的段子数据，每天凌晨1点时候发布，按动图：静图：文字=1:2:2规则随机发布
	 * <li>动图取点赞数TOP20条数据</li>
	 * <li>文字取点赞数TOP40条数据</li>
	 * <li>静图取点赞数TOP40条数据</li>
	 * */
	@Scheduled(cron="0 0 1 * * ?")
	public void publishRecommendChannelJoke(){
		List<String> gifJokeList = jokeService.getJokeListForPublishRecommend(Constants.JOKE_TYPE_GIF,20);
		if(CollectionUtils.isEmpty(gifJokeList) || gifJokeList.size() != 20){
			return;
		}
		List<String> imgJokeList = jokeService.getJokeListForPublishRecommend(Constants.JOKE_TYPE_IMG,40);
		if(CollectionUtils.isEmpty(imgJokeList) || imgJokeList.size() != 40){
			return;
		}
		List<String> textJokeList = jokeService.getJokeListForPublishRecommend(Constants.JOKE_TYPE_TEXT,40);
		if(CollectionUtils.isEmpty(textJokeList) || textJokeList.size() != 40){
			return;
		}
		
		Map<String,Double> map = Maps.newHashMap();
		Double baseScore = Double.parseDouble(new SimpleDateFormat("yyyyMMdd").format(new Date()) + FIX);
		for(int i = 0 ;i < 40 ;i = i+2){
			map.put(imgJokeList.get(i), baseScore+(40-i));
			map.put(textJokeList.get(i), baseScore+(40-i));
			map.put(imgJokeList.get(i+1), baseScore+(40-i-1));
			map.put(textJokeList.get(i+1), baseScore+(40-i-1));
			map.put(gifJokeList.get(i/2), baseScore+(40-i));
		}
		jedisCache.zadd(JedisKey.SORTEDSET_RECOMMEND_CHANNEL, map);
		map.clear();
	}
}
