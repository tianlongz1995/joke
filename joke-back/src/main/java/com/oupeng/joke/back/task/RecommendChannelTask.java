package com.oupeng.joke.back.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

	private static long ONE_DAY = 1000 * 60 * 60 * 24;
	private static String FIX = "000000";

	@Autowired
	private JokeService jokeService;
	@Autowired
	private JedisCache jedisCache;
	
	/**
	 * 发布推荐频道下的段子数据，每天凌晨1点时候发布，每次发布昨天发布数据中点赞数前100的段子
	 * */
	@Scheduled(cron="0 0 1 * * ?")
	public void publishRecommendChannelJoke(){
//		查询推荐频道下数据内容 图片55条，GIF15条，文字30条
		List<Joke> jokeList = jokeService.getJokeListForPublishRecommend();
		if(!CollectionUtils.isEmpty(jokeList)){
			Map<String,Double> map = Maps.newHashMap();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			for(Joke joke : jokeList){
				if(joke.getVerifyTime() == null){
					joke.setVerifyTime(new Date(new Date().getTime() - ONE_DAY));
				}
				if(joke.getGood() == null){
					joke.setGood(1);
				}
				Long time = Long.parseLong(sdf.format(joke.getVerifyTime()) + FIX) + joke.getGood();
				map.put(String.valueOf(joke.getId()), Double.valueOf(time));
			}
			jedisCache.zadd(JedisKey.SORTEDSET_RECOMMEND_CHANNEL, map);
			map.clear();
		}
	}
}
