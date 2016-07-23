package com.oupeng.joke.front.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Feedback;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.JokeDetail;
import com.oupeng.joke.domain.Topic;
import com.oupeng.joke.domain.response.DistributorConfig;
import com.oupeng.joke.domain.response.Failed;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
import com.oupeng.joke.front.util.Constants;
import com.oupeng.joke.front.util.FormatUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@Service
public class JokeService {
    private static Logger logger = LoggerFactory.getLogger(JokeService.class);

    @Autowired
    private JedisCache jedisCache;
	@Autowired
	private Environment env;
    
    /** 赞信息列表    */
    private static List<String> addLikeIds = Lists.newCopyOnWriteArrayList();
    /** 踩信息列表    */
    private static List<String> addStepIds = Lists.newCopyOnWriteArrayList();
    /** 反馈信息列表    */
    private static List<String> feedbackList = Lists.newCopyOnWriteArrayList();

    /**
     * 获取渠道配置
     * @param did
     * @return
     */
    public Result getDistributorConfig(String did) {
        if(StringUtils.isNumeric(did)){
            String result = jedisCache.hget(JedisKey.JOKE_HASH_DISTRIBUTOR_CONFIG, did);
            DistributorConfig dcr = JSON.parseObject(result, DistributorConfig.class);
            if(dcr != null){
                return new Success(dcr);
            }
        }
        return new Failed();
    }

    /**
     * 缓存踩赞信息
     * @param id
     * @param type 1：赞；2：踩
     */
    public void stepLike(Integer id, Integer type) {
        if(type == 1){
            addLikeIds.add(String.valueOf(id));
        }else if(type == 2){
            addStepIds.add(String.valueOf(id));
        }
    }

    /**
     * 定时将赞列表中数据存储到缓存中
     */
    @Scheduled(fixedRate = 1000 * 60 * 10, initialDelay = 5000)
    public void addLikeQueue(){
        try{
            if(!CollectionUtils.isEmpty(addLikeIds)){
                jedisCache.lpush(JedisKey.JOKE_LIST_LIKE, addLikeIds.toArray(new String[]{}));
				logger.info("增加点赞数记录" + addLikeIds.size() + "条");
				addLikeIds.clear();
            }
        }catch(Exception e){
            logger.error("增加点赞数失败",e);
        }
    }
    /**
     * 定时将踩列表中数据存储到缓存中
     */
    @Scheduled(fixedRate = 1000 * 60 * 10, initialDelay = 8000)
    public void addStepQueue(){
        try{
            if(!CollectionUtils.isEmpty(addStepIds)){
                jedisCache.lpush(JedisKey.JOKE_LIST_STEP, addStepIds.toArray(new String[]{}));
				logger.info("增加踩数记录" + addStepIds.size() + "条");
				addStepIds.clear();
            }
        }catch(Exception e){
            logger.error("增加踩数失败",e);
        }
    }
    /**
     * 缓存反馈信息
     * @param feedback
     */
    public void feedback(Feedback feedback) {
        String feedbackJson = JSON.toJSONString(feedback);
            if(feedbackJson != null){
                feedbackList.add(feedbackJson);
            }
    }
    /**
     * 定时将反馈信息列表中数据存储到缓存中
     */
    @Scheduled(fixedRate = 1000 * 60 * 10, initialDelay = 10000)
    public void addFeedbackCache(){
        try{
            if(!CollectionUtils.isEmpty(feedbackList)){
                jedisCache.lpush(JedisKey.JOKE_LIST_FEEDBACK, feedbackList.toArray(new String[]{}));
				logger.info("增加反馈数记录" + feedbackList.size() + "条");
				feedbackList.clear();
            }
        }catch(Exception e){
            logger.error("增加反馈数失败",e);
        }
    }
    
    public Result getJokeList(Integer distributorId,Integer channelId,Integer topicId,Integer listType,Long start,Long end){
    	Object result = null;
    	if(Constants.LIST_TYPE_COMMON_CHANNEL == listType){
    		result = getJokeList4CommonChannel(channelId,start,end);
    	}else if(Constants.LIST_TYPE_TOPIC_CHANNEL == listType){
    		result = getTopicList4TopicChannel(distributorId,start,end);
    	}else if(Constants.LIST_TYPE_RECOMMEND_CHANNEL == listType){
    		result = getJokeList4RecommendChannel(start,end);
    	}else if(Constants.LIST_TYPE_TOPIC == listType){
    		result = getJokeList4TopicChannel(topicId,start,end);
    	}
    	return new Success(result);
    }
    
    private List<Joke> getJokeList4CommonChannel(Integer channelId,Long start,Long end){
    	String key = JedisKey.SORTEDSET_COMMON_CHANNEL + channelId;
    	Set<String> jokeIdSet = jedisCache.zrevrange(key, start, end);
		if(CollectionUtils.isEmpty(jokeIdSet)){
			return null;
		}
		
		List<Joke> list = Lists.newArrayList();
		Joke joke = null;
		for(String jokeId : jokeIdSet){
			joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + jokeId),Joke.class);
			if(joke != null){
				if(joke.getType() == Constants.JOKE_TYPE_IMG){
					joke.setImg( env.getProperty("img.real.server.url") + joke.getImg());
				}else if(joke.getType() == Constants.JOKE_TYPE_GIF){
					joke.setImg( env.getProperty("img.real.server.url") + joke.getImg());
					joke.setGif( env.getProperty("img.real.server.url") + joke.getGif());
				}
				list.add(joke);
			}
		}
		return list;
    }
    
    private List<Topic> getTopicList4TopicChannel(Integer distributorId,Long start,Long end){
    	String key = JedisKey.SORTEDSET_DISTRIBUTOR_TOPIC + distributorId;
    	Set<String> topicSet = jedisCache.zrevrange(key, start, end);
		if(CollectionUtils.isEmpty(topicSet)){
			return null;
		}
		
		List<Topic> list = Lists.newArrayList();
		Topic topic = null;
		for(String topicString : topicSet){
			topic = JSON.parseObject(topicString,Topic.class);
			if(topic != null){
				topic.setType(Constants.JOKE_TYPE_TOPIC_LIST);
				topic.setImg(env.getProperty("img.real.server.url") + topic.getImg());
				list.add(topic);
			}
		}
    	return list;
    }
    
    private List<Joke> getJokeList4RecommendChannel(Long start,Long end){
    	String key = JedisKey.SORTEDSET_RECOMMEND_CHANNEL;
    	Set<String> jokeIdSet = jedisCache.zrevrange(key, start, end);
		if(CollectionUtils.isEmpty(jokeIdSet)){
			return null;
		}
		
		List<Joke> list = Lists.newArrayList();
		Joke joke = null;
		for(String jokeId : jokeIdSet){
			joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + jokeId),Joke.class);
			if(joke != null){
				if(joke.getType() == Constants.JOKE_TYPE_IMG){
					joke.setImg( env.getProperty("img.real.server.url") + joke.getImg());
				}else if(joke.getType() == Constants.JOKE_TYPE_GIF){
					joke.setImg( env.getProperty("img.real.server.url") + joke.getImg());
					joke.setGif( env.getProperty("img.real.server.url") + joke.getGif());
				}
				list.add(joke);
			}
		}
		return list;
    }
    
    private List<Joke> getJokeList4TopicChannel(Integer topicId,Long start,Long end){
    	String key = JedisKey.SORTEDSET_TOPIC_CHANNEL + topicId;
    	Set<String> jokeIdSet = jedisCache.zrevrange(key, start, end);
		if(CollectionUtils.isEmpty(jokeIdSet)){
			return null;
		}
		
		List<Joke> list = Lists.newArrayList();
		Joke joke = null;
		for(String jokeId : jokeIdSet){
			joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + jokeId),Joke.class);
			if(joke != null){
				if(joke.getType() == Constants.JOKE_TYPE_IMG){
					joke.setImg( env.getProperty("img.real.server.url") + joke.getImg());
				}else if(joke.getType() == Constants.JOKE_TYPE_GIF){
					joke.setImg( env.getProperty("img.real.server.url") + joke.getImg());
					joke.setGif( env.getProperty("img.real.server.url") + joke.getGif());
				}
				list.add(joke);
			}
		}
		return list;
    }
    
    public JokeDetail getJoke(Integer distributorId,Integer channelId,Integer topicId,Integer listType,Integer jokeId){
    	JokeDetail joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + jokeId),JokeDetail.class);
    	if(joke != null){
    		if(joke.getType() == Constants.JOKE_TYPE_IMG){
				joke.setImg( env.getProperty("img.real.server.url") + joke.getImg());
			}else if(joke.getType() == Constants.JOKE_TYPE_GIF){
				joke.setImg( env.getProperty("img.real.server.url") + joke.getImg());
				joke.setGif( env.getProperty("img.real.server.url") + joke.getGif());
			}
    		String key = "";
        	if(Constants.LIST_TYPE_COMMON_CHANNEL == listType){
        		key = JedisKey.SORTEDSET_COMMON_CHANNEL + channelId;
        	}/*else if(Constants.LIST_TYPE_TOPIC_CHANNEL == listType){
        		key = JedisKey.SORTEDSET_DISTRIBUTOR_TOPIC + distributorId;
        	}*/else if(Constants.LIST_TYPE_RECOMMEND_CHANNEL == listType){
        		key = JedisKey.SORTEDSET_RECOMMEND_CHANNEL;
        	}else if(Constants.LIST_TYPE_TOPIC == listType){
        		key = JedisKey.SORTEDSET_TOPIC_CHANNEL + topicId;
        	}
    		Long index = jedisCache.zrevrank(key,String.valueOf(jokeId));
    		if(index != null){
    			if(index > 0){
        			Set<String> jokeLastIds = jedisCache.zrevrange(key, index -1, index -1);
        			if(!CollectionUtils.isEmpty(jokeLastIds)){
        				for(String jokeLastId : jokeLastIds){
        					joke.setLastId(Integer.valueOf(jokeLastId));
        				}
        			}
        		}
        		
        		Set<String> jokeNextIds = jedisCache.zrevrange(key, index +1, index +1);
    			if(!CollectionUtils.isEmpty(jokeNextIds)){
    				for(String jokeNextId : jokeNextIds){
    					joke.setNextId(Integer.valueOf(jokeNextId));
    				}
    			}
    		}
    		handleJokeDetail(joke);
    	}
		return joke;
    }
    
    private void handleJokeDetail(JokeDetail jokeDetail){
    	
    	
    	List<String> relatedTextIdList = jedisCache.srandmember(JedisKey.SET_RELATED_JOKE_TEXT, 3);
    	if(!CollectionUtils.isEmpty(relatedTextIdList)){
    		List<Joke> relatedTextList = Lists.newArrayList();
    		for(String jokeId : relatedTextIdList){
    			relatedTextList.add(JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + jokeId),Joke.class));
    		}
    		jokeDetail.setRelatedText(relatedTextList);
    	}
    	
    	List<String> relatedImgIdList = jedisCache.srandmember(JedisKey.SET_RELATED_JOKE_IMG, 3);
    	if(!CollectionUtils.isEmpty(relatedImgIdList)){
    		List<Joke> relatedImgList = Lists.newArrayList();
    		Joke joke = null;
    		for(String jokeId : relatedImgIdList){
    			joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + jokeId),Joke.class);
    			if(joke != null){
    				if(joke.getImg() != null){
    					joke.setImg(joke.getImg().replace("_600x_", "_200x_"));
    					joke.setImg( env.getProperty("img.real.server.url") + joke.getImg());
    					joke.setHeight(FormatUtil.getHeight(joke.getHeight(), joke.getWidth(), 200));
    					joke.setWidth(200);
    				}
    				relatedImgList.add(joke);
    			}
    		}
    		jokeDetail.setRelatedImg(relatedImgList);
    	}
    	
    	List<String> recommendIdList = jedisCache.srandmember(JedisKey.SET_RECOMMEDN_JOKE_TEXT, 3);
    	if(!CollectionUtils.isEmpty(recommendIdList)){
    		List<Joke> recommendTextList = Lists.newArrayList();
    		for(String jokeId : recommendIdList){
    			recommendTextList.add(JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + jokeId),Joke.class));
    		}
    		jokeDetail.setRecommend(recommendTextList);
    	}
    }
}
