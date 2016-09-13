package com.oupeng.joke.back.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


import com.oupeng.joke.domain.Dictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static Logger logger = LoggerFactory.getLogger(RecommendChannelTask.class);

	@Autowired
	private JokeService jokeService;
	@Autowired
	private JedisCache jedisCache;

	/**	发布文字数量	*/
	private static Integer PUBLISH_TEXT_SIZE = 200;
	/**	发布静图数量	*/
	private static Integer PUBLISH_IMG_SIZE = 200;
	/**	发布动图数量	*/
	private static Integer PUBLISH_GIF_SIZE = 100;
	/**	文字权重	*/
	private static Integer TEXT_WEIGHT = 2;
	/**	静图权重	*/
	private static Integer IMG_WEIGHT = 2;
	/**	动图权重	*/
	private static Integer GIF_WEIGHT = 1;

//	@PostConstruct
	public void initParam(){
		List<Dictionary> dictionarys = jokeService.getDictionaryRecordList("10001", 0, 3);
		if(!CollectionUtils.isEmpty(dictionarys)){
			for(Dictionary dictionary : dictionarys){
				if(dictionary.getType().equals("0")) {
					PUBLISH_TEXT_SIZE = Integer.valueOf(dictionary.getValue());
					TEXT_WEIGHT = Integer.valueOf(dictionary.getCode());
				} else if (dictionary.getType().equals("1")){
					PUBLISH_IMG_SIZE = Integer.valueOf(dictionary.getValue());
					IMG_WEIGHT = Integer.valueOf(dictionary.getCode());
				} else if (dictionary.getType().equals("2")){
					PUBLISH_GIF_SIZE = Integer.valueOf(dictionary.getValue());
					GIF_WEIGHT = Integer.valueOf(dictionary.getCode());
				}
			}
		}

		logger.info("publish text:{}|{}, img:{}|{}, gif:{}|{}", PUBLISH_TEXT_SIZE, TEXT_WEIGHT, PUBLISH_IMG_SIZE, IMG_WEIGHT, PUBLISH_GIF_SIZE, GIF_WEIGHT);

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
//		初始化参数
		initParam();

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

		int gifSize = gifJokeList.size();
		int imgSize = imgJokeList.size();
		int textSize = textJokeList.size();
		int total = gifSize + imgSize + textSize;
		int gif = GIF_WEIGHT, img = IMG_WEIGHT, text = TEXT_WEIGHT;
//		int index = total;
//		根据权重将数据存储Map中
		for(int index = total; index > 0; index--){
			if(gif > 0){ // 频道权重是否用完
				map.put(gifJokeList.get(gifSize-1), baseScore + Double.valueOf(index));
				gif--;
				gifSize--;
			}
			if(img > 0){ // 频道权重是否用完
				map.put(imgJokeList.get(imgSize-1), baseScore + Double.valueOf(index));
				img--;
				imgSize--;
			}
			if(text > 0){ // 频道权重是否用完
				map.put(textJokeList.get(textSize-1), baseScore + Double.valueOf(index));
				text--;
				textSize--;
			}
//			使用的权重使用完就重新进行一轮权重轮询
			if(gif == 0 && img == 0 && text == 0){
				gif = GIF_WEIGHT;
				img = IMG_WEIGHT;
				text = TEXT_WEIGHT;
			}
//			map中数量与总数对应上说明数据记录完成;
			if(map.size() == total ){
				break;
			}
		}

		jedisCache.zadd(JedisKey.SORTEDSET_RECOMMEND_CHANNEL, map);
		logger.info("Recommend Publish over:{}", map.size());
		map.clear();
	}

}
