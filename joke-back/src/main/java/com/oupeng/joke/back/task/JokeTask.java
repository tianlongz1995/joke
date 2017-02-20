//package com.oupeng.joke.back.task;
//
//import com.alibaba.fastjson.JSON;
//import com.oupeng.joke.back.service.JokeService;
//import com.oupeng.joke.back.util.Constants;
//import com.oupeng.joke.cache.JedisCache;
//import com.oupeng.joke.cache.JedisKey;
//import com.oupeng.joke.domain.Comment;
//import com.oupeng.joke.domain.Joke;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
////@Component
//public class JokeTask {
//	private static Logger logger = LoggerFactory.getLogger(JokeTask.class);
//	@Autowired
//	private JokeService jokeService;
//	@Autowired
//	private JedisCache jedisCache;
//
//	/**
//	 * 发布段子数据，5分钟发布一次
//	 * */
//	@Scheduled(cron="0 */5 * * * ?")
//	public void publishJoke(){
//		logger.debug("publishJoke start...");
//		String lastTime = jedisCache.get(JedisKey.JOKE_LAST_PUBLISH_TIME);
//		String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//		List<Joke> jokeList = jokeService.getJokeListForPublish(lastTime,currentTime);
//		int index = 0;
//		if(!CollectionUtils.isEmpty(jokeList)){
//			for(Joke joke : jokeList){
//				joke.setCreateTime(null);
//				joke.setSourceId(null);
//				joke.setStatus(null);
//				joke.setUpdateTime(null);
//				joke.setVerifyTime(null);
//				joke.setVerifyUser(null);
//
//				if(joke.getCommentNumber() != null
//						&& joke.getCommentContent() != null
//						&& joke.getAvata() != null
//						&& joke.getNick() != null){
//					joke.setComment(new Comment(joke.getCommentNumber(), joke.getCommentContent(), joke.getAvata(),joke.getNick()));
//					joke.setCommentNumber(null);
//					joke.setCommentContent(null);
//					joke.setAvata(null);
//					joke.setNick(null);
//				}
//
//				jedisCache.set(JedisKey.STRING_JOKE + joke.getId(), JSON.toJSONString(joke));
//				if(joke.getType() == Constants.JOKE_TYPE_TEXT){ //段子类型：纯文 0
//					jedisCache.sadd(JedisKey.SET_RELATED_JOKE_TEXT, String.valueOf(joke.getId()));
//					jedisCache.sadd(JedisKey.SET_RECOMMEDN_JOKE_TEXT, String.valueOf(joke.getId()));
//				}else if(joke.getType() == Constants.JOKE_TYPE_CHOICE){ //段子类型：精选 3
//					jedisCache.sadd(JedisKey.SET_RELATED_JOKE_IMG, String.valueOf(joke.getId()));
//				}
//				index++;
//			}
//		}
//		jedisCache.set(JedisKey.JOKE_LAST_PUBLISH_TIME, currentTime);
//
//		logger.debug("publishJoke over time:{} size:{}", currentTime, index);
//	}
//}
