package com.oupeng.joke.back.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Feedback;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Maps;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.back.util.HttpUtil;
import com.oupeng.joke.back.util.ImgRespDto;
import com.oupeng.joke.dao.mapper.JokeMapper;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.JokeVerifyInfo;
import com.oupeng.joke.domain.JokeVerifyRate;

import javax.annotation.PostConstruct;

@Service
public class JokeService {
	private static Logger logger = LoggerFactory.getLogger(JokeService.class);

	@Autowired
	private JokeMapper jokeMapper;
	@Autowired
	private JedisCache jedisCache;
	@Autowired
	private Environment env;
	
	public List<Joke> getJokeListForVerify(Integer type,Integer status){
		List<Joke> jokeList = jokeMapper.getJokeList(type, status,null,null,false);
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
	
	public void verifyJoke(Integer status,String ids,String user){
		if(status != Constants.JOKE_STATUS_VALID){
			String[] jokeIds = ids.split(",");
			Set<String> keys = jedisCache.keys(JedisKey.SORTEDSET_ALL);
			if(!CollectionUtils.isEmpty(keys)){
				for(String key : keys){
					jedisCache.zrem(key, jokeIds);
				}
			}
			
			for(String id : jokeIds){
				jedisCache.del(JedisKey.STRING_JOKE + id);
			}
		}
		jokeMapper.updateJokeStatus(status, ids, user);
	}
	
	public Joke getJokeById(Integer id){
		Joke joke = jokeMapper.getJokeById(id);
		if(joke != null){
			if(joke.getType() == Constants.JOKE_TYPE_IMG){
				joke.setImg( env.getProperty("img.real.server.url") + joke.getImg());
			}else if(joke.getType() == Constants.JOKE_TYPE_GIF){
				joke.setImg( env.getProperty("img.real.server.url") + joke.getImg());
				joke.setGif( env.getProperty("img.real.server.url") + joke.getGif());
			}
		}
		return joke;
	}

	/**
	 * 更新段子点赞数
	 */
	@PostConstruct
	public void jokeLikeCountUpdate(){
		logger.info("jokeLikeCountUpdate init...");
		new Thread(){
			public void run() {
				while(true){
					try{
						List<String> likeIdList = jedisCache.brpop(JedisKey.JOKE_LIST_LIKE, 60*5);
						logger.info("jokeLikeCountUpdate receved size:[{}]", likeIdList == null ? 0 : likeIdList.size());
						if(!CollectionUtils.isEmpty(likeIdList)){
							String likeId = likeIdList.get(1);
							logger.info("update joke Like Count id:" + likeId);
							jokeMapper.updatejokeLikeCount(Integer.valueOf(likeId));
							updateJokeLikeCache(likeId);
						}
					}catch(Exception e){
						logger.error("update joke Like Count error!",e);
					}
				}
			};
		}.start();
	}


	/**
	 * 更新段子被踩数
	 */
	@PostConstruct
	public void jokeStepCountUpdate(){
		logger.info("jokeStepCountUpdate init...");
		new Thread(){
			public void run() {
				while(true){
					try{
						List<String> stepIdList = jedisCache.brpop(JedisKey.JOKE_LIST_STEP, 60*5);
						logger.info("jokeStepCountUpdate receved size:[{}]", stepIdList == null ? 0 : stepIdList.size());
						if(!CollectionUtils.isEmpty(stepIdList)){
							String stepId = stepIdList.get(1);
							logger.info("update joke step Count id:" + stepId);
							jokeMapper.updateJokeStepCount(Integer.valueOf(stepId));
							updateJokeStepCache(stepId);
						}
					}catch(Exception e){
						logger.error("update joke step Count error!" + e.getMessage(),e);
					}
				}
			};
		}.start();
	}
	/**
	 * 存储段子反馈信息
	 */
	@PostConstruct
	public void insertJokeFeedback(){
		logger.info("insertJokeFeedback init...");
		new Thread(){
			public void run() {
				while(true){
					try{
						List<String> feedbackList = jedisCache.brpop(JedisKey.JOKE_LIST_FEEDBACK, 60*5);
						logger.info("insertJokeFeedback receved size:[{}]", feedbackList == null ? 0 : feedbackList.size());
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
			};
		}.start();
	}

	/**
	 * 更新段子点赞缓存数
	 * @param jokeId
	 */
	private void updateJokeLikeCache(String jokeId) {
		String jokeStr = jedisCache.get(JedisKey.STRING_JOKE + jokeId);
		if(jokeStr != null){
			Joke joke = JSON.parseObject(jokeStr, Joke.class);
			if(joke != null && joke.getGood() != null){
				joke.setGood(joke.getGood() + 1);
			}else {
				joke.setGood(1);
			}
			jedisCache.set(JedisKey.STRING_JOKE + jokeId, JSON.toJSONString(joke));
		}
	}
	/**
	 * 更新段子被踩缓存数
	 * @param jokeId
	 */
	private void updateJokeStepCache(String jokeId) {
		String jokeStr = jedisCache.get(JedisKey.STRING_JOKE + jokeId);
		if(jokeStr != null){
			Joke joke = JSON.parseObject(jokeStr, Joke.class);
			if(joke != null && joke.getBad() != null){
				joke.setBad(joke.getBad() + 1);
			}else{
				joke.setBad(1);
			}
			jedisCache.set(JedisKey.STRING_JOKE + jokeId, JSON.toJSONString(joke));
		}
	}

	public boolean updateJoke(Integer id,String title,String img,String gif,Integer width,Integer height,String content,String user){
		Joke joke = new Joke();
		joke.setId(id);
		joke.setContent(content);
		joke.setTitle(title);
		joke.setVerifyUser(user);
		boolean result = handleJokeImg(img,gif,width,height,joke);
		if(result){
			jokeMapper.updateJoke(joke);
		}
		return result;
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
		List<Joke> jokeList = jokeMapper.getJokeList(null,null,id,content,false);
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
	
	public int getJokeCountForChannel(String contentType){
		return jokeMapper.getJokeCountForChannel(contentType);
	}
	
	public List<Joke> getJokeListForChannel(String contentType,Integer start,Integer size){
		List<Joke> jokeList = jokeMapper.getJokeListForChannel(contentType, start, size);
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
	
	public List<Joke> getJokeListForTopic(Integer type,Integer status){
		List<Joke> jokeList = jokeMapper.getJokeList(type, status,null,null,true);
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
	
	public List<Integer> getJokeForPublishTopic(Integer topicId){
		return jokeMapper.getJokeForPublishTopic(topicId);
	}
	
	public List<Integer> getJokeForPublishChannel(String contentType){
		return jokeMapper.getJokeForPublishChannel(contentType);
	}
	
	public int getJokeCountForPublishChannel(String contentType){
		return jokeMapper.getJokeCountForPublishChannel(contentType);
	}
	
	public void updateJokeForPublishChannel(String jokeIds){
		jokeMapper.updateJokeStatus(Constants.JOKE_STATUS_PUBLISH, jokeIds, null);
	}
	
	public List<Joke> getJokeListForPublish(String lastUpdateTime,String currentUpdateTime){
		return jokeMapper.getJokeListForPublish(lastUpdateTime,currentUpdateTime);
	}
	
	public List<Joke> getJokeListForPublishRecommend(){
		return jokeMapper.getJokeListForPublishRecommend();
	}
	
	public List<JokeVerifyRate> getJokeVerifyRate(){
		return jokeMapper.getJokeVerifyRate();
	}
	
	private boolean handleJokeImg(String imgUrl,String gifUrl,Integer width,Integer height,Joke joke){
		if (StringUtils.isNotBlank(gifUrl)) {//动图
			joke.setType(Constants.JOKE_TYPE_GIF);
			ImgRespDto imgRespDto = HttpUtil.handleImg(env.getProperty("remote.crop.img.server.url"), gifUrl, true);
			if (imgRespDto != null && imgRespDto.getErrorCode() == 0) {
				joke.setGif(imgRespDto.getGifUrl());
				joke.setImg(imgRespDto.getImgUrl());
				joke.setWidth(imgRespDto.getWidth());
				joke.setHeight(imgRespDto.getHeight());
				return true;
			} 
		} else if (StringUtils.isNotBlank(imgUrl)) {//静图
			joke.setType(Constants.JOKE_TYPE_IMG);
			ImgRespDto imgRespDto = HttpUtil.handleImg(env.getProperty("remote.crop.img.server.url"), imgUrl, true);
			if (imgRespDto != null && imgRespDto.getErrorCode() == 0) {
				joke.setGif(null);
				joke.setImg(imgRespDto.getImgUrl());
				joke.setWidth(imgRespDto.getWidth());
				joke.setHeight(imgRespDto.getHeight());
				return true;
			} 
		} else {
			joke.setType(Constants.JOKE_TYPE_TEXT);
			joke.setGif(null);
			joke.setImg(null);
			joke.setWidth(null);
			joke.setHeight(null);
			return true;
		}
		return false;
	}
}
