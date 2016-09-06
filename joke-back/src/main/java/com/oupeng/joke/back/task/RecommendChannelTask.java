package com.oupeng.joke.back.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Maps;
import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;

import javax.annotation.PostConstruct;

@Component
public class RecommendChannelTask {
	private static Logger logger = LoggerFactory.getLogger(RecommendChannelTask.class);

	@Autowired
	private JokeService jokeService;
	@Autowired
	private JedisCache jedisCache;
	@Autowired
	private Environment env;

	private static Integer PUBLISH_TEXT_SIZE = null;

	private static Integer PUBLISH_IMG_SIZE = null;

	private static Integer PUBLISH_GIF_SIZE = null;

	@PostConstruct
	public void initConstants(){
		logger.info("publish text:{}, img:{}, gif:{}", env.getProperty("publish.text.size"), env.getProperty("publish.img.size"), env.getProperty("publish.gif.size"));
		PUBLISH_TEXT_SIZE = Integer.valueOf(env.getProperty("publish.text.size"));
		PUBLISH_IMG_SIZE = Integer.valueOf(env.getProperty("publish.img.size"));
		PUBLISH_GIF_SIZE = Integer.valueOf(env.getProperty("publish.gif.size"));
	}
	/**
	 * 发布推荐频道下的段子数据，每天凌晨1点时候发布，按动图：静图：文字=1:2:2规则随机发布  200, 做到web页面,自己配置
	 * <li>动图取点赞数TOP20条数据</li>
	 * <li>文字取点赞数TOP40条数据</li>
	 * <li>静图取点赞数TOP40条数据</li>
	 * */
	@Scheduled(cron="0 0 1 * * ?")
	public void publishRecommendChannelJoke(){
		logger.info("Recommend Publish start...");
		List<String> gifJokeList = jokeService.getJokeListForPublishRecommend(Constants.JOKE_TYPE_GIF, PUBLISH_GIF_SIZE);
		if(CollectionUtils.isEmpty(gifJokeList) || gifJokeList.size() != PUBLISH_GIF_SIZE){
			return;
		}
		List<String> imgJokeList = jokeService.getJokeListForPublishRecommend(Constants.JOKE_TYPE_IMG,PUBLISH_IMG_SIZE);
		if(CollectionUtils.isEmpty(imgJokeList) || imgJokeList.size() != PUBLISH_IMG_SIZE){
			return;
		}
		List<String> textJokeList = jokeService.getJokeListForPublishRecommend(Constants.JOKE_TYPE_TEXT, PUBLISH_TEXT_SIZE);
		if(CollectionUtils.isEmpty(textJokeList) || textJokeList.size() != PUBLISH_TEXT_SIZE){
			return;
		}

		Map<String,Double> map = Maps.newHashMap();
		Double baseScore = Double.parseDouble(new SimpleDateFormat("yyyyMMdd000000").format(new Date()));
		for(int i = 0 ;i < PUBLISH_TEXT_SIZE ;i = i+2){
			map.put(imgJokeList.get(i), baseScore+(PUBLISH_IMG_SIZE-i));
			map.put(textJokeList.get(i), baseScore+(PUBLISH_TEXT_SIZE-i));
			map.put(imgJokeList.get(i+1), baseScore+(PUBLISH_IMG_SIZE-i-1));
			map.put(textJokeList.get(i+1), baseScore+(PUBLISH_TEXT_SIZE-i-1));
			map.put(gifJokeList.get(i/2), baseScore+(PUBLISH_TEXT_SIZE-i));
		}
		jedisCache.zadd(JedisKey.SORTEDSET_RECOMMEND_CHANNEL, map);
		logger.info("Recommend Publish over:{}", map.size());
		map.clear();
	}

}
