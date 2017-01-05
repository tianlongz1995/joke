package com.oupeng.joke.front.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.*;
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
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

@Service
public class JokeService {
	private static final String PNG = ".png";
	private static final String JPG = ".jpg";
    private static Logger logger = LoggerFactory.getLogger(JokeService.class);
private static List<String> addLikeIds = Lists.newCopyOnWriteArrayList();
/** 赞信息列表    */
	private static List<String> addStepIds = Lists.newCopyOnWriteArrayList();
/** 踩信息列表    */
	private static List<String> feedbackList = Lists.newCopyOnWriteArrayList();
/** 反馈信息列表    */

	private static int PUBLISH_SIZE = 500;	private static String IMG_REAL_SERVER_URL = "http://joke-img.adbxb.cn/";	/**	发送邮件间隔	*/
	private static long SEND_EMAIL_INTERVAL = 3 * 60 * 1000;	@Autowired
	private MailService mailService;
    @Autowired
    private JedisCache jedisCache;
	@Autowired
	private Environment env;
	/**	最后发送邮件时间	*/
	private long lastSendEmailTime = System.currentTimeMillis();
	/**	收件人	*/
	private String RECIPIENT = "shuangh@oupeng.com";
	/**	抄送	*/
	private String CC = "shuangh@oupeng.com";
	/**	抄送	*/
	private Integer ALARM_VALUE = 5;
	/**	渠道表	*/
	private Map<String, String> distributorMap;
	/**	频道表	*/
	private Map<String, String> channelMap;

	/**
	 * 修改图片后缀
	 * @param img
	 * @return
	 */
	private static String png2jpg(String img) {
		return img.replace(PNG, JPG);
	}
    
	@PostConstruct
	public void initConstants(){
		String size = env.getProperty("publish.size");
		String url = env.getProperty("img.real.server.url");
		String interval = env.getProperty("send.email.interval");

		if(StringUtils.isNumeric(size)){
			PUBLISH_SIZE = Integer.valueOf(size);
		}else{
			logger.error("getProperty(\"publish.size\") is null:{}", size);
		}
		if(StringUtils.isNotBlank(url)){
			IMG_REAL_SERVER_URL = url;
		}else{
			logger.error("getProperty(\"img.real.server.url\") is null:{}", url);
		}
		if(StringUtils.isNumeric(interval)){
			long sendEmailInterval = Long.valueOf(interval);
//			发送间隔不能小于1分钟
			if (sendEmailInterval >= 1){
				SEND_EMAIL_INTERVAL = sendEmailInterval * 60 * 1000;
			}
		}else{
			logger.error("getProperty(\"send.email.interval\") is null:{}", interval);
		}

		String recipient = env.getProperty("data.publish.recipient");
		String cc = env.getProperty("data.publish.cc");
		if(StringUtils.isNotBlank(recipient)){
			RECIPIENT = recipient;
		}
		if(StringUtils.isNotBlank(cc)){
			CC = cc;
		}
		String distributor = jedisCache.get(JedisKey.JOKE_STRING_DISTRIBUTOR_MAP);
		String channel = jedisCache.get(JedisKey.JOKE_STRING_CHANNEL_MAP);
		if(StringUtils.isNotBlank(distributor)){
			distributorMap = (Map<String, String>) JSON.parse(distributor);
			if(distributorMap == null){
				distributorMap = Maps.newHashMap();
			}
		}
		if(StringUtils.isNotBlank(channel)){
			channelMap = (Map<String, String>) JSON.parse(channel);
			if(channelMap == null){
				channelMap = Maps.newHashMap();
			}
		}

		String alarm = env.getProperty("alarm.value");
		if(StringUtils.isNumeric(alarm)){
			ALARM_VALUE = Integer.valueOf(alarm);
		}
	}

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

	/**
	 * 获取段子列表
	 * @param distributorId
	 * @param channelId
	 * @param topicId
	 * @param listType
	 * @param start
	 * @param end
	 * @param actionType	动作类型: 0:首次请求; 1:上拉刷新; 2:下拉刷新
	 * @return
	 */
    public Result getJokeList(Integer distributorId, Integer channelId, Integer topicId, Integer listType, Long start, Long end, int actionType) {
		List<?> result = null;
		if (Constants.LIST_TYPE_COMMON_CHANNEL == listType) {        // 普通频道列表页 lt = 0
			if (actionType == 1) {    // 获取普通频道历史记录列表页
				result = getJokeCacheList(JedisKey.SORTEDSET_COMMON_CHANNEL + channelId, start + PUBLISH_SIZE, end + PUBLISH_SIZE);
			} else {
				if (end > PUBLISH_SIZE) { // 下拉刷新超过 publishSize 条就停止刷新
					result = Lists.newArrayList();
				} else {
					result = getJokeCacheList(JedisKey.SORTEDSET_COMMON_CHANNEL + channelId, start, end);
				}
			}

		} else if (Constants.LIST_TYPE_TOPIC_CHANNEL == listType) {    // 专题标签页 lt = 1
			result = getTopicList4TopicChannel(start, end);
		} else if (Constants.LIST_TYPE_RECOMMEND_CHANNEL == listType) {// 推荐频道列表页  lt = 2
			result = getJokeList4RecommendChannel(start, end, actionType);
		} else if (Constants.LIST_TYPE_TOPIC == listType) {            // 专题详情页	lt = 9
			result = getJokeList4TopicChannel(topicId, start, end);
		}

		if (actionType == 0) {
//			if(start.intValue() != 0 || end.intValue() != 9){
//				logger.info("分页参数错误distributorId:{}, channelId:{}, topicId:{}, listType:{}, action:0, start:{}, end:{}", distributorId, channelId, topicId, listType, start, end);
////				return new Failed("分页参数错误",null);
//			}
			if(listType.intValue() == 0 || listType.intValue() == 2){
				if (CollectionUtils.isEmpty(result)) {
  					if(distributorMap != null && !distributorMap.isEmpty() && channelMap != null && !channelMap.isEmpty()){
  						if(distributorMap.keySet().contains(String.valueOf(distributorId)) && channelMap.keySet().contains(String.valueOf(channelId))){
							String alarmCount = jedisCache.hget(JedisKey.JOKE_HASH_SEND_EMAIL_TIME, String.valueOf(channelId));
							Integer count;
							if(StringUtils.isNumeric(alarmCount)){
								count = Integer.valueOf(alarmCount) + 1;
							} else {
								count = 1;
							}
							long currentTime = System.currentTimeMillis();
							if ((currentTime - lastSendEmailTime) > SEND_EMAIL_INTERVAL && count >= ALARM_VALUE) {
								String dName = getDistributorName(String.valueOf(distributorId));
								String cName = getChannelName(String.valueOf(channelId));
								mailService.sendMail(RECIPIENT, CC,
										"【段子】【前台】 数据获取失败",
										"【段子】【前台】获取列表页数据失败: 渠道:" + dName
												+ ", 频道:" + cName
												+ ", 专题编号:" + topicId
												+ "\r\n(listType:" + listType
												+ ", start:" + start
												+ ", end:" + end
												+ ", actionType:" + actionType + ")"
								);
								jedisCache.lpush(JedisKey.JOKE_LIST_FLUSH_CACHE, JSON.toJSONString(new Channel(channelId, listType)));
								logger.error("【段子】【前台】获取列表页数据失败!渠道编号:{}, 频道编号:{}, 专题编号:{}, (listType:{}, start:{}, end:{}, actionType:{})", distributorId, channelId, topicId, listType, start, end, actionType);
								lastSendEmailTime = System.currentTimeMillis();
								jedisCache.hset(JedisKey.JOKE_HASH_SEND_EMAIL_TIME, String.valueOf(channelId), String.valueOf(0));
							} else {
								jedisCache.hset(JedisKey.JOKE_HASH_SEND_EMAIL_TIME, String.valueOf(channelId), String.valueOf(count));
							}
						} else {
							logger.info("【段子】【前台】 数据获取失败:没有这个渠道[{}]或者频道[{}]", distributorId, channelId);
						}
					}else{
						logger.error("【段子】【前台】 渠道、频道缓存表空:渠道[{}]、频道[{}]", distributorMap.size(), channelMap.size());
					}
				}
			}
		}

		return new Success(result);
	}

	/**
	 * 获取频道名称
	 * @param channelId
	 * @return
	 */
	private String getChannelName(String channelId) {
		if(channelMap != null && !channelMap.isEmpty()){
			for(Map.Entry<String, String> map : channelMap.entrySet()){
				if(map.getKey() == channelId){
					return map.getValue();
				}
			}
		}
		return String.valueOf(channelId);
	}

	/**
	 * 获取渠道名称
	 * @param distributorId
	 * @return
	 */
	private String getDistributorName(String distributorId) {
		if(distributorMap != null && !distributorMap.isEmpty()){
			for(Map.Entry<String, String> map : distributorMap.entrySet()){
				if(map.getKey() == distributorId){
					return map.getValue();
				}
			}
		}
		return String.valueOf(distributorId);
	}

	/**
	 * 获取专题列表
	 * @param start
	 * @param end
	 * @return
	 */
    private List<Topic> getTopicList4TopicChannel(Long start,Long end){
    	Set<String> topicIdSet = jedisCache.zrevrange(JedisKey.SORTEDSET_TOPIC_LIST, start, end);
		if(CollectionUtils.isEmpty(topicIdSet)){
			return null;
		}
		List<Topic> list = Lists.newArrayList();
		Topic topic;
		for(String topicId : topicIdSet){
			topic = JSON.parseObject(jedisCache.get(JedisKey.STRING_TOPIC + topicId), Topic.class);
			if(topic != null){
				topic.setType(Constants.JOKE_TYPE_TOPIC_LIST);
				topic.setImg(IMG_REAL_SERVER_URL + topic.getImg());
				list.add(topic);
			}
		}
    	return list;
    }

	/**
	 * 获取推荐频道列表页
	 * @param start
	 * @param end
	 * @return
	 */
    private List<Joke> getJokeList4RecommendChannel(Long start,Long end, Integer actionType){
    	String key = JedisKey.SORTEDSET_RECOMMEND_CHANNEL;
		if(actionType == 1){
			return getJokeCacheList(key, start + PUBLISH_SIZE, end + PUBLISH_SIZE);
		} else {
			return getJokeCacheList(key, start, end);
		}
    }

	/**
	 * 获取专题详情页
	 * @param topicId
	 * @param start
	 * @param end
	 * @return
	 */
    private List<Joke> getJokeList4TopicChannel(Integer topicId, Long start, Long end){
    	String key = JedisKey.SORTEDSET_TOPIC_JOKE_SET + topicId;
		return getJokeCacheList(key, start, end);
    }

	/**
	 * 获取段子详情
	 * @param distributorId
	 * @param channelId
	 * @param topicId
	 * @param listType
	 * @param jokeId
	 * @return
	 */
    public JokeDetail getJoke(Integer distributorId,Integer channelId,Integer topicId,Integer listType,Integer jokeId){
    	JokeDetail joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + jokeId),JokeDetail.class);
    	if(joke != null){
    		if(joke.getType() == Constants.JOKE_TYPE_IMG){
				joke.setImg( IMG_REAL_SERVER_URL + joke.getImg());
			}else if(joke.getType() == Constants.JOKE_TYPE_GIF){
				joke.setImg( png2jpg(IMG_REAL_SERVER_URL + joke.getImg()));
				joke.setGif( IMG_REAL_SERVER_URL + joke.getGif());
			}
    		String key = "";
        	if(Constants.LIST_TYPE_COMMON_CHANNEL == listType){
        		key = JedisKey.SORTEDSET_COMMON_CHANNEL + channelId;
        	}else if(Constants.LIST_TYPE_RECOMMEND_CHANNEL == listType){
        		key = JedisKey.SORTEDSET_RECOMMEND_CHANNEL;
        	}else if(Constants.LIST_TYPE_TOPIC == listType){
        		key = JedisKey.SORTEDSET_TOPIC_JOKE_SET + topicId;
        	}
    		Long index = jedisCache.zrevrank(key, String.valueOf(jokeId));
    		if(index != null){
//    			获取下一条段子编号
    			if(index > 0){
        			Set<String> jokeLastIds = jedisCache.zrevrange(key, index -1, index -1);
        			if(!CollectionUtils.isEmpty(jokeLastIds)){
        				for(String jokeLastId : jokeLastIds){
        					joke.setLastId(Integer.valueOf(jokeLastId));
        				}
        			}
        		}
//        		获取上一条段子编号
        		Set<String> jokeNextIds = jedisCache.zrevrange(key, index +1, index +1);
    			if(!CollectionUtils.isEmpty(jokeNextIds)){
    				for(String jokeNextId : jokeNextIds){
    					joke.setNextId(Integer.valueOf(jokeNextId));
    				}
    			}
    		}
    		handleJokeDetail(joke); // 处理段子推荐信息
    	}
		return joke;
    }
    
	/**
	 * 处理段子详情中推荐段子
	 * @param jokeDetail
	 */
	private void handleJokeDetail(JokeDetail jokeDetail){
		//随机获取3条相关图片段子
    	List<String> relatedImgIdList = jedisCache.srandmember(JedisKey.SET_RELATED_JOKE_IMG, 6);
    	if(!CollectionUtils.isEmpty(relatedImgIdList)){
    		List<Joke> relatedImgList = Lists.newArrayList();
    		Joke joke = null;
    		for(String jokeId : relatedImgIdList){
    			joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + jokeId),Joke.class);
    			if(joke != null){
    				if(joke.getImg() != null){
    					joke.setImg(joke.getImg().replace("_600x_", "_200x_"));
    					joke.setImg( IMG_REAL_SERVER_URL + joke.getImg());
    					joke.setHeight(FormatUtil.getHeight(joke.getHeight(), joke.getWidth(), 200));
    					joke.setWidth(200);
    				}
    				relatedImgList.add(joke);
    			}
    		}
    		jokeDetail.setRelatedImg(relatedImgList);
    	}
    }

	/**
	 * 获取段子缓存列表
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	private List<Joke> getJokeCacheList(final String key, final Long start, final Long end) {
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
//					joke.setImg( getListPreviewImg(IMG_REAL_SERVER_URL + joke.getImg()));
					joke.setImg(IMG_REAL_SERVER_URL + joke.getImg());
				}else if(joke.getType() == Constants.JOKE_TYPE_GIF){
					joke.setImg( png2jpg(IMG_REAL_SERVER_URL + joke.getImg()));
//					joke.setImg( IMG_REAL_SERVER_URL + joke.getImg());
					joke.setGif( IMG_REAL_SERVER_URL + joke.getGif());
				}
				if(StringUtils.isNotBlank(joke.getContent()) && joke.getContent().length() > 184){
					joke.setContent(joke.getContent().substring(0, 184));
				}
				list.add(joke);
			}
		}
		return list;
	}

	/**
	 * 获取主题列表
	 * @param actionType	动作类型
	 * @param utc			时间:备用
	 * @param count			请求记录数量
	 * @param start			开始记录位置
	 * @param end			结束记录位置
	 * @return
	 */
	public List<Topic> topicList(Integer actionType, Long utc, Integer count, Long start, Long end) {
		return getTopicList4TopicChannel(start, end);
	}

	/**
	 * 获取主题详情
	 * @param topicId
	 * @return
	 */
	public Object topicDetails(Integer topicId) {
		return getJokeList4TopicChannel(topicId, 0L, -1L);
	}
}
