package com.oupeng.joke.back.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.back.util.HttpUtil;
import com.oupeng.joke.back.util.ImageUtils;
import com.oupeng.joke.back.util.ImgRespDto;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.JokeMapper;
import com.oupeng.joke.domain.*;
import com.oupeng.joke.domain.Dictionary;
import com.oupeng.joke.domain.response.Failed;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;

@Service
public class JokeService {
	private static final String ADMIN_PASS = "admin@joke.com";
	private static Logger logger = LoggerFactory.getLogger(JokeService.class);
	@Autowired
	private JokeMapper jokeMapper;
	@Autowired
	private JedisCache jedisCache;
	@Autowired
	private TaskService taskService;
	@Autowired
	private Environment env;
    private String uploadImagePath = "/nh/java/back/resources/image/";

    private String imgPrefix = "http://joke2-img.oupeng.com/";

    private String remoteImgPrefix = "http://joke2-img.oupeng.com/";

    private String randomUserUrl = "http://joke2.oupeng.com/comment/joke/user";

    private String cdnImagePath = "/data01/images/";

    @PostConstruct
    private void init(){
        String url = env.getProperty("img.real.server.url");
        if (StringUtils.isNoneBlank(url)) {
            imgPrefix = url;
        } else {
            logger.error("getProperty(\"img.real.server.url\") is null:{}", url);
        }
        String uploadPath = env.getProperty("upload_image_path");
        if (StringUtils.isNoneBlank(uploadPath)) {
            uploadImagePath = uploadPath;
        } else {
            logger.error("getProperty(\"upload_image_path\") is null:{}", uploadPath);
        }
        String cdnPath = env.getProperty("img.cdn.path");
        if (StringUtils.isNoneBlank(cdnPath)) {
            cdnImagePath = cdnPath;
        } else {
            logger.error("getProperty(\"img.cdn.path\") is null:{}", cdnPath);
        }

        String remoteImgUrl = env.getProperty("remote.crop.img.server.url");
        if (StringUtils.isNoneBlank(remoteImgUrl)) {
            remoteImgPrefix = remoteImgUrl;
        } else {
            logger.error("getProperty(\"img.real.server.url\") is null:{}", remoteImgUrl);
        }

        String userUrl = env.getProperty("random.user.url");
        if (StringUtils.isNoneBlank(userUrl)) {
            randomUserUrl = userUrl;
        } else {
            logger.error("getProperty(\"random.user.url\") is null:{}", userUrl);
        }
    }

	/**
	 * 获取段子列表
	 * @param type
	 * @param status
	 * @param source
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	public List<Joke> getJokeListForVerify(Integer type, Integer status, Integer source, String startDay, String endDay, Integer offset, Integer pageSize){
		List<Joke> jokeList = jokeMapper.getJokeListForVerify(type, status, source, startDay, endDay, offset, pageSize);
        handleJokesUrl(jokeList);
		return jokeList;
	}

	/**
	 * 获取待审核的段子列表总记录数
	 * @param type
	 * @param status
	 * @param source
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	public int getJokeListForVerifyCount(Integer type, Integer status, Integer source, String startDay, String endDay) {
        return jokeMapper.getJokeListForVerifyCount(type, status, source, startDay, endDay);
	}

	/**
	 * 处理段子列表记录URL
	 * @param jokeList
	 */
	private void handleJokesUrl(List<Joke> jokeList) {
		if(!CollectionUtils.isEmpty(jokeList)){
			for(Joke joke : jokeList){
			    handleJokeUrl(joke);
			}
		}
	}

    private void handleJokeUrl(Joke joke) {
        if(joke.getType() == Constants.JOKE_TYPE_IMG){
            joke.setImg( imgPrefix + joke.getImg());
        }else if(joke.getType() == Constants.JOKE_TYPE_GIF){
            joke.setImg( imgPrefix + joke.getImg());
            joke.setGif( imgPrefix + joke.getGif());
        }
    }

    /**
     * 处理置顶段子图片地址
     * @param jokeTops
     */
    private void handleJokeTopUrl(List<JokeTop> jokeTops) {
        if(!CollectionUtils.isEmpty(jokeTops)){
            for(JokeTop jokeTop : jokeTops) {
                if (jokeTop.getType() == Constants.JOKE_TYPE_IMG) {
                    jokeTop.setImg(imgPrefix + jokeTop.getImg());
                } else if (jokeTop.getType() == Constants.JOKE_TYPE_GIF) {
                    jokeTop.setImg(imgPrefix + jokeTop.getImg());
                    jokeTop.setGif(imgPrefix + jokeTop.getGif());
                }
            }
        }
    }

    /**
     * 修改审核状态
     * @param status    段子待修改状态
     * @param ids       段子编号
     * @param user      操作用户
     * @param allStatus 当前段子所在状态
     */
	public void verifyJoke(Integer status,String ids,String user,Integer allStatus){
		String[] jokeIds = ids.split(",");
		if(status.equals(Constants.JOKE_STATUS_VALID)){ // status = 1 通过
			// 更改状态
			jokeMapper.updateJokeStatus(status, ids, user);
			//加到缓存
            jokeToCache(ids);
            logger.info("内容审核：段子id[{}]：通过",ids);
		}else if(status.equals(Constants.JOKE_STATUS_NOVALID)){ // status = 2 不通过(删除)
//			  清除缓存
            cleanJokeCache(jokeIds);
//            更新状态
            jokeMapper.updateJokeStatus(status, ids, user);
            logger.info("内容审核：段子id[{}]:不通过",ids);
		}else if(status.equals(Constants.JOKE_STATUS_OFFLINE)){ // status = 5 下线
//			清除缓存
            cleanJokeCache(jokeIds);
//			更新状态
            jokeMapper.updateJokeStatus(Constants.JOKE_STATUS_NOVALID, ids, user); // 下线段子修改为不通过
            logger.info("内容审核：段子:[{}]下线!", ids);
		}else if(status.equals(Constants.JOKE_STATUS_TOP)) { // status = 6 置顶 只改status,不改audit
            if(allStatus != null && allStatus.equals(1)){ // 已通过的段子置顶
//              更新置顶状态不改变更新时间, 避免影响段子、趣图发布时的顺序
                jokeMapper.updateJokeTopAudit(ids, user);
//              保存置顶段子
                jokeMapper.insertJokeTop(ids);
            }
            if(allStatus != null && allStatus.equals(0)){ // 未审核的段子置顶
//                修改段子状态为已审核、置顶
                jokeMapper.updateJokeToTopAndAudit(ids, user);
                //加到缓存
                jokeToCache(ids);
//              保存置顶段子
                jokeMapper.insertJokeTop(ids);
            } else {
                logger.error("内容审核：未审核、已通过状态下不能置顶段子![{}]", ids);
            }

            logger.info("内容审核：段子id[{}]置顶!", ids);
        }
	}

    /**
     * 缓存段子
     * @param jokeIds
     */
	private void jokeToCache(String jokeIds){
        List<Joke> jokes = jokeMapper.getCacheJokeListByIds(jokeIds);
        if(CollectionUtils.isEmpty(jokes)){
            logger.error("缓存段子失败! 获取段子列表为空:[{}]", jokeIds);
        }
        //加到缓存
        for (Joke joke : jokes) {
            if (joke != null) {
                if(joke.getCommentNumber() != null
                        && joke.getCommentContent() != null
                        && joke.getAvata() != null
                        && joke.getNick() != null){
                    joke.setComment(new Comment(joke.getCommentNumber(), joke.getCommentContent(), joke.getAvata(),joke.getNick()));
                    joke.setCommentNumber(null);
                    joke.setCommentContent(null);
                    joke.setAvata(null);
                    joke.setNick(null);
                }
                jedisCache.set(JedisKey.STRING_JOKE + joke.getId(), JSON.toJSONString(joke));
            }
        }
    }

    /**
     * 清除段子缓存
     * @param jokeIds
     */
    private void cleanJokeCache(String[] jokeIds) {
        Set<String> keys = jedisCache.keys(JedisKey.SORTEDSET_ALL);
        if(!CollectionUtils.isEmpty(keys)){
            for(String key : keys){
                jedisCache.zrem(key, jokeIds);
            }
        }
        //删除段子
        for(String id : jokeIds){
            jedisCache.zrem(JedisKey.JOKE_CHANNEL + 1, id); // 从趣图频道删除
            jedisCache.zrem(JedisKey.JOKE_CHANNEL + 2, id); // 从段子频道删除
            jedisCache.zrem(JedisKey.JOKE_CHANNEL + 3, id); // 从精选频道删除
            jedisCache.zrem(JedisKey.JOKE_CHANNEL + 4, id); // 从精选频道删除
            //删除段子
            jedisCache.del(JedisKey.STRING_JOKE + id);      // 删除段子缓存
        }
    }

    public Joke getJokeById(Integer id){
		Joke joke = jokeMapper.getJokeById(id);
		if(joke != null){
            handleJokeUrl(joke);
		}
		return joke;
	}

	/**
	 * 更新段子点赞数
	 */
	@PostConstruct
	public void jokeLikeCountUpdate(){
		logger.debug("jokeLikeCountUpdate init...");
		new Thread(){
			public void run() {
				while(true){
					try{
						List<String> likeIdList = jedisCache.brpop(JedisKey.JOKE_LIST_LIKE, 60*5);
						logger.info("jokeLikeCountUpdate receved size:[{}]", likeIdList == null ? 0 : likeIdList.size());
						if(!CollectionUtils.isEmpty(likeIdList)){
							String likeId = likeIdList.get(1);
							logger.debug("update joke Like Count id:" + likeId);
							if(Integer.valueOf(likeId)>20000000) { //更新精选的点踩数
								jokeMapper.updateChoiceLikeCount(Integer.valueOf(likeId));
							}else {
								jokeMapper.updatejokeLikeCount(Integer.valueOf(likeId));
							}
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
		logger.debug("jokeStepCountUpdate init...");
		new Thread(){
			public void run() {
				while(true){
					try{
						List<String> stepIdList = jedisCache.brpop(JedisKey.JOKE_LIST_STEP, 60*5);
						logger.debug("jokeStepCountUpdate receved size:[{}]", stepIdList == null ? 0 : stepIdList.size());
						if(!CollectionUtils.isEmpty(stepIdList)){
							String stepId = stepIdList.get(1);
							logger.debug("update joke step Count id:" + stepId);
							if(Integer.valueOf(stepId)>20000000) { //更新精选的点踩数
								jokeMapper.updateChoiceStepCount(Integer.valueOf(stepId));
							}else {
								jokeMapper.updateJokeStepCount(Integer.valueOf(stepId));
							}
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
		logger.debug("insertJokeFeedback init...");
		new Thread(){
			public void run() {
				while(true){
					try{
						List<String> feedbackList = jedisCache.brpop(JedisKey.JOKE_LIST_FEEDBACK, 60*5);
						logger.debug("insertJokeFeedback receved size:[{}]", feedbackList == null ? 0 : feedbackList.size());
						if(!CollectionUtils.isEmpty(feedbackList)){
							String feedbackStr = feedbackList.get(1);
							logger.debug("update joke feedback String:" + feedbackStr);
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

	/**
	 * 更新段子信息
	 * @param id
	 * @param title
	 * @param img
	 * @param gif
	 * @param width
	 * @param height
	 * @param content
	 * @param user
	 * @return
	 */
	public boolean updateJoke(Integer id,String title,String img,String gif,Integer width,Integer height,String content,Integer weight,String user){
		Joke joke = new Joke();
		joke.setId(id);
		joke.setContent(content);
		joke.setTitle(title);
		joke.setVerifyUser(user);
		joke.setWeight(weight);
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
        handleJokesUrl(jokeList);
		return jokeList;
	}
	
	public int getJokeCountForChannel(String contentType){
		return jokeMapper.getJokeCountForChannel(contentType);
	}
	
	public List<Joke> getJokeListForChannel(String contentType, Integer start, Integer size){
		List<Joke> jokeList = jokeMapper.getJokeListForChannel(contentType, start, size);
        handleJokesUrl(jokeList);
		return jokeList;
	}

	/**
	 * 获取已审核的段子列表记录总数
	 * @param type
	 * @param status
	 * @return
	 */
	public int getJokeListForTopicCount(Integer type,Integer status) {
		return jokeMapper.getJokeListForTopicCount(type, status);
	}
	/**
	 * 获取已审核的段子列表  TODO
	 * @param type
	 * @param status
	 * @return
	 */
	public List<Joke> getJokeListForTopic(Integer type,Integer status, Integer offset, Integer pageSize){
		List<Joke> jokeList = jokeMapper.getJokeListForTopic(type, status, offset, pageSize);
        handleJokesUrl(jokeList);
		return jokeList;
	}

	/**
	 * 获取专题对应的段子编号
	 * @param topicId
	 * @return
	 */
	public List<Integer> getJokeForPublishTopic(Integer topicId){
		return jokeMapper.getJokeForPublishTopic(topicId);
	}

	/**
	 * 查询最近审核通过的数据
	 * @param contentType	发布类型
	 * @param size 			数量
	 * @return
	 */
	public List<Joke> getJokeForPublishChannel(String contentType, Integer size){
		return jokeMapper.getJokeForPublishChannel(contentType, size);
	}
	
	public int getJokeCountForPublishChannel(String contentType,Integer status){
		return jokeMapper.getJokeCountForPublishChannel(contentType,status);
	}
	
	public void updateJokeForPublishChannel(String jokeIds){
		jokeMapper.updateJokeStatus(Constants.JOKE_STATUS_PUBLISH, jokeIds, null);
	}

	/**
	 * 获取待发布的段子列表
	 * @param lastUpdateTime
	 * @param currentUpdateTime
	 * @return
	 */
	public List<Joke> getJokeListForPublish(String lastUpdateTime,String currentUpdateTime){
		return jokeMapper.getJokeListForPublish(lastUpdateTime,currentUpdateTime);
	}

	public List<String> getJokeListForPublishRecommend(Integer type,Integer num){
		return jokeMapper.getJokeListForPublishRecommend(type,num);
	}
	
	public List<JokeVerifyRate> getJokeVerifyRate(){
		return jokeMapper.getJokeVerifyRate();
	}
	
	private boolean handleJokeImg(String imgUrl,String gifUrl,Integer width,Integer height,Joke joke){
		if (StringUtils.isNotBlank(gifUrl)) {//动图
			joke.setType(Constants.JOKE_TYPE_GIF);
			ImgRespDto imgRespDto = HttpUtil.handleImg(remoteImgPrefix, gifUrl, true);
			if (imgRespDto != null && imgRespDto.getErrorCode() == 0) {
				joke.setGif(imgRespDto.getGifUrl());
				joke.setImg(imgRespDto.getImgUrl());
				joke.setWidth(imgRespDto.getWidth());
				joke.setHeight(imgRespDto.getHeight());
				return true;
			} 
		} else if (StringUtils.isNotBlank(imgUrl)) {//静图
			joke.setType(Constants.JOKE_TYPE_IMG);
			ImgRespDto imgRespDto = HttpUtil.handleImg(remoteImgPrefix, imgUrl, true);
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

	/**
	 * 获取字典记录总条数
	 * @param code
	 * @return
	 */
	public int getDictionaryRecordCount(String code) {
		return jokeMapper.getDictionaryRecordCount(code);
	}

	/**
	 * 获取字典记录列表
	 * @param code
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List<Dictionary> getDictionaryRecordList(String code, int offset, Integer pageSize) {
		return jokeMapper.getDictionaryRecordList(code, offset, pageSize);
	}

	/**
	 * 添加一条字典记录
	 * @param dict
	 * @return
	 */
	public int addDictionary(Dictionary dict) {
		return jokeMapper.addDictionary(dict);
	}

	/**
	 * 修改权重信息
	 * @param dict
	 * @return
	 */
	public int weightEdit(Dictionary dict) {
		return jokeMapper.weightEdit(dict);
	}

	/**
	 * 删除权限信息
	 * @param id
	 * @return
	 */
	public int weightDel(Integer id) {
		return jokeMapper.weightDel(id);
	}

	/**
	 * 获取字典信息
	 * @param id
	 * @return
	 */
	public Object weightGet(String id) {
		return jokeMapper.weightGet(id);
	}

	/**
	 * 修改发布数量
	 * @param id
	 * @param size
	 * @return
	 */
	public int editPublishSize(String id, Integer size) {
		return jokeMapper.editPublishSize(id, size);
	}

	/**
	 * 刷新频道缓存
	 * @param id
	 * @param pass
	 * @return
	 */
    public boolean flushCache(Integer id, String pass) {
    	if(pass.equals(ADMIN_PASS)){
			jedisCache.lpush(JedisKey.JOKE_LIST_FLUSH_CACHE, String.valueOf(id));
			return true;
		}
    	return false;
    }

	/**
	 * 获取段子数据
	 * @param type	段子类型: 0:文字、1:图片、2:动图
	 * @param count	获取段子数量
	 * @return
	 */
	public List<Joke> getPublishJokeListByType(Integer type, Integer count) {
		return jokeMapper.getPublishJokeListByType(type, count);
	}

	/**
	 * 自动发布段子
	 * @param type
	 * @param limit
	 */
	public void autoAuditJoke(int type, int limit) {
		jokeMapper.autoAuditJoke(type, limit);
	}


	/**
	 * 添加发布规则
	 * @param type  1:趣图、2:段子、3:推荐、4:精选
	 * @param textNum
	 * @param imageNum
	 * @param gifNum
	 * @param textWeight
	 * @param imageWeight
	 * @param gifWeight
	 */
	public void addPublishRole(Integer type,String role,Integer textNum,Integer imageNum,Integer gifNum,Integer textWeight,Integer imageWeight,Integer gifWeight){

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("role",role);
		jsonObject.put("textNum",textNum);
		jsonObject.put("imageNum",imageNum);
		jsonObject.put("gifNum",gifNum);
		jsonObject.put("textWeight",textWeight);
		jsonObject.put("imageWeight", imageWeight);
		jsonObject.put("gifWeight",gifWeight);
		if(type == 1){ //纯文
//			更新数据库
			jokeMapper.addPublishRole(10041,jsonObject.toString());
//			更新定时任务
			updateTask(10041, "14", role, 2, jsonObject);
		}else if (type == 2){ //趣图
//			更新数据库
			jokeMapper.addPublishRole(10042,jsonObject.toString());
//			更新定时任务
			updateTask(10042, "15", role, 1, jsonObject);
		}else if(type == 3){ //推荐
//			更新数据库
			jokeMapper.addPublishRole(10043,jsonObject.toString());
//			更新定时任务
			updateTask(10043, "16", role, 3, jsonObject);
		}
	}

	/**
	 * 更新定时任务
	 * @param code
	 * @param id
	 * @param role
	 * @param type
	 * @param jsonObject
	 */
	private void updateTask(int code, String id, String role, int type, JSONObject jsonObject) {
		Task task = new Task();
		task.setCron(role);
		task.setId(id);
		task.setType(type);
		task.setObject(jsonObject);
		taskService.updateTask(task);
	}

	/**
	 * 获取发布规则
	 * @param code
	 * @return
	 */
	public String getPublishRole(int code){
		return jokeMapper.getPublishRole(code);
	}

	/**
	 * 新增评论数量记录
	 * @param jid
	 * @return  TODO
	 */
    public boolean incrementComment(Integer[] jid) {
//    	更新数据库中段子评论数
        for(Integer id : jid){
            if(id != null){
            	if(id>20000000){
            		jokeMapper.incrementChoiceComment(id);
				}else{
					jokeMapper.incrementComment(id);
				}
                //		更新缓存中的段子评论数
                Joke joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + id),Joke.class);
                if(joke != null && joke.getComment() != null){
                    Comment comment = joke.getComment();
                    comment.setTotal(comment.getTotal() + 1);
                    jedisCache.set(JedisKey.STRING_JOKE + id, JSON.toJSONString(joke));
                } else {
                    logger.error("更新段子[{}]评论数失败!缓存中没有此段子!", id);
                }
            }
        }
    	return true;
    }

	/**
	 * 减少评论数量记录
	 * @param jid
	 * @return  TODO
	 */
	public boolean decrementComment(Integer[] jid) {
//    	更新数据库中段子评论数
		for(Integer id : jid){
			if(id != null){
				if(id>20000000) {
					jokeMapper.decrementChoiceComment(id);
				}else{
					jokeMapper.decrementComment(id);
				}
				//		更新缓存中的段子评论数
				Joke joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + id),Joke.class);
				if(joke != null && joke.getComment() != null){
					Comment comment = joke.getComment();
					comment.setTotal(comment.getTotal() - 1);
					jedisCache.set(JedisKey.STRING_JOKE + id, JSON.toJSONString(joke));
				} else {
					logger.error("更新段子[{}]评论数失败!缓存中没有此段子!", id);
				}
			}
		}
		return true;
	}





	/**
	 * 获取段子2.0文字、趣图段子发布列表
	 * @param limit
	 * @return
	 */
	public List<Joke> getJoke2PublishList(int status, int type, int limit) {
		return jokeMapper.getJoke2PublishList(status, type, limit);
    }

	/**
     * 更新段子2.0文字段子状态
     * @param idsStr
     */
    public void updateJoke2PublishStatus(String idsStr, int status) {
        jokeMapper.updateJoke2PublishStatus(idsStr, status);
    }
    /**
     * 更新段子2.0段子状态为已推荐
     * @param idsStr
     */
    public void updateJoke2RecommendPublishStatus(String idsStr) {
        jokeMapper.updateJoke2RecommendPublishStatus(idsStr);
    }
	/**
	 * 获取段子2.0发布任务
	 * @return
	 */
	public List<Task> getJoke2PublishTask() {
		return jokeMapper.getJoke2PublishTask();
	}

    /**
     * 获取段子2.0推荐段子发布列表
     * @param status
     * @param type
     * @param limit
     * @return
     */
    public List<Joke> getJoke2RecommendPublishList(int status, int type, Integer limit) {
        return jokeMapper.getJoke2RecommendPublishList(status, type, limit);
    }

    /**
     * 获取当前时间发布的段子列表
     * @param releaseDate
     * @param releaseHours
     * @return
     */
    public List<Joke> getJoke2RecommendTopList(String releaseDate, String releaseHours) {
        return jokeMapper.getJoke2RecommendTopList(releaseDate, releaseHours);
    }

    /**
     * 获取首页置顶段子总数
     * @param type
     * @param status
     * @param source
     * @param startDay
     * @param endDay
     * @return
     */
    public int getJokeTopListCount(Integer type, Integer status, Integer source, String startDay, String endDay) {
        return jokeMapper.getJokeTopListCount(type, status, source, startDay, endDay);
    }

    /**
     * 获取首页置顶段子列表
     * @param type
     * @param status
     * @param source
     * @param startDay
     * @param endDay
     * @param offset
     * @param pageSize
     * @return
     */
    public List<JokeTop> getJokeTopList(Integer type, Integer status, Integer source, String startDay, String endDay, int offset, Integer pageSize) {
        List<JokeTop> jokeList = jokeMapper.getJokeTopList(type, status, source, startDay, endDay, offset, pageSize);
        handleJokeTopUrl(jokeList);
        return jokeList;
    }

    /**
     * 更新首页置顶段子状态
     * @param idStr
     */
    public void updateJokeTopPublishStatus(String idStr) {
        jokeMapper.updateJokeTopPublishStatus(idStr);
    }

    /**
     * 发布推荐置顶段子
     * @param ids
     * @param releaseDate
     * @param releaseHours
     * @return
     */
    public Result releaseTopJoke(Integer[] ids, Integer[] sorts, String releaseDate, String releaseHours, String username) {
        if(ids == null || ids.length < 1 || sorts == null || ids.length != sorts.length){
            return new Failed("参数错误!");
        }
        int count = 0;
        for(int i = 0; i < ids.length; i++){
            jokeMapper.releaseTopJoke(ids[i], sorts[i], releaseDate, releaseHours, username);
            count++;
        }

        if(count > 0){
            return new Success("发布成功!");
        } else {
            return new Failed("段子["+JSON.toJSONString(ids)+"]发布失败!");
        }
    }

    /**
     * 修改排序值
     * @param id
     * @param sort
     * @param username
     * @return
     */
    public Result editTopJokeSort(Integer id, Integer sort, String username) {
        int count =  jokeMapper.editTopJokeSort(id, sort, username);
        if(count == 1){
            return new Success("修改成功!");
        } else {
            return new Failed("段子["+id+"]修改排序值["+sort+"]失败!");
        }
    }

    /**
     * 批量修改排序值
     * @param ids
     * @param sorts
     * @param username
     * @return
     */
    public Result editTopJokeSorts(Integer[] ids, Integer[] sorts, String username) {
        if(ids == null || ids.length < 1 || sorts == null || ids.length != sorts.length){
            return new Failed("参数错误!");
        }
        int count = 0;
        for(int i = 0; i < ids.length; i++){
            jokeMapper.editTopJokeSort(ids[i], sorts[i], username);
            count++;
        }

        if(count > 0){
            return new Success("修改成功!");
        } else {
            return new Failed("段子["+ JSON.toJSONString(ids)+"]修改排序值["+JSON.toJSONString(sorts)+"]失败!");
        }
    }

    /**
     * 获取段子2.0记录数量
     * @param type
     * @param status
     * @return
     */
    public int getJokeListForCount(Integer type, int status) {
        return jokeMapper.getJokeListForCount(type, status);
    }

    /**
     * 置顶段子下线
     * @param ids
     * @param username
     * @return
     */
    public Result topOffline(String ids, String username) {
//        清除缓存
        cleanJokeCache(ids.split(","));
//        更新置顶表数据库状态 - 下线(status=3)
        int count = jokeMapper.topJokeOffline(ids, username);
//        更新段子表数据状态为不通过 - 下线(status=5)
        jokeMapper.updateJokeStatus(5, ids, username);
        if(count > 0){
            return new Success("修改成功!");
        } else {
            return new Failed("段子["+ JSON.toJSONString(ids)+"]下线失败!");
        }
    }

    /**
     * 新增段子
     * @param title
     * @param type
     * @param img
     * @param content
     * @param weight
     * @param username
     * @return
     */
    public boolean addJoke(String title, Integer type, String img, String content, Integer weight, String username) {
//        随机点赞点踩数
        int bad  = 150 -(int)(Math.random()*150);
        int good = 500 +(int)(Math.random()*500);
        String uuid = UUID.randomUUID().toString();
        Comment comment = HttpUtil.getRandomUser(randomUserUrl);
        String fileName = getUrlImageFileName(img);
        String storeName = "";
        String gif = null;
        Image image = new Image();
        int random  = new Random().nextInt(3000);
        try {
            File dir = null;
//        将图片从临时目录上传到CDN目录(images)
            if(type > 0){
                if(fileName == null && fileName.length() < 1){
                    logger.error("上传的图片类型段子没有图片信息:[{}]", img);
                    return false;
                }
                dir = new File(cdnImagePath + random + "/");
                if(!dir.isDirectory()){
                    dir.mkdirs();
                }
//                所有静图都格式化为jpg
                if(type.equals(1)){
                    storeName = fileName.substring(0, fileName.lastIndexOf("."));
                    storeName = storeName + ".jpg";
//                    生成图片并获得宽高
                    ImageUtils.generateImage(uploadImagePath + fileName, dir.getCanonicalPath() + "/" + storeName, "jpg", image);
                    img = random + "/" + storeName;
                } else if(type.equals(2)){
                    //            将图片拷贝到CDN目录下
                    boolean copyStatus = ImageUtils.copyImageToCDN(uploadImagePath + fileName, dir.getCanonicalPath() + "/" + fileName);
                    if(!copyStatus){
                        return false;
                    }
//                  获得宽高
                    ImageUtils.getImgWidthAndHeight(new File(dir.getCanonicalPath() + "/" + fileName), image);
                    img = random + "/" + fileName;
                }

            }
            if(type == 2 & img != null && img.length() > 10){
                String newFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".jpg";
                boolean copyStatus = ImageUtils.getGifOneFrame(uploadImagePath + fileName, dir.getCanonicalPath() + "/" + newFileName, 5);
//                String jpgName = fileName.replace("gif", "jpg");
//                boolean copyStatus = ImageUtils.copyImageToCDN(uploadImagePath + jpgName, dir.getCanonicalPath() + jpgName);
                if(!copyStatus){
                    return false;
                }
                gif = img;
                img = random + "/" + newFileName;
            }
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return false;
        }

        int count = jokeMapper.addJoke(title, type, img, gif, good, bad, uuid, comment.getAvata(), comment.getNick(), content, weight, image.getWidth(), image.getHeight(), username);
        if(count > 0){


            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取远程URL图片名称
     * @param img
     * @return
     */
    private String getUrlImageFileName(String img) {
        int pos = img.lastIndexOf("/");
        if(pos < 0){
            return null;
        }
        return img.substring(pos + 1);
    }
}
