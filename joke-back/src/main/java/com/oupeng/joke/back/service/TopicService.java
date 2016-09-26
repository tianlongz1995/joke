package com.oupeng.joke.back.service;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.oupeng.joke.dao.mapper.JokeMapper;
import com.oupeng.joke.domain.TopicCover;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.back.util.HttpUtil;
import com.oupeng.joke.back.util.ImgRespDto;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.TopicMapper;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.Topic;

@Service
public class TopicService {
	private static final Logger logger = LoggerFactory.getLogger(TopicService.class);

	@Autowired
	private TopicMapper topicMapper;
	@Autowired
	private JedisCache jedisCache;
	@Autowired
	private JokeMapper jokeMapper;
	@Autowired
	private Environment env;


	/**
	 * 获取专题列表记录总数
	 * @param topicCoverId
	 * @param status
	 * @return
	 */
	public int getTopicListCount(Integer topicCoverId, Integer status) {
		return topicMapper.getTopicListCount(topicCoverId, status);
	}

	/**
	 * 获取专题列表
	 * @param topicCoverId
	 * @param status
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List<Topic> getTopicList(Integer topicCoverId, Integer status, Integer offset,Integer pageSize){
		List<Topic> topicList = topicMapper.getTopicList(topicCoverId, status, offset, pageSize);
		if(!CollectionUtils.isEmpty(topicList)){
			for(Topic topic : topicList){
				if(StringUtils.isNotBlank(topic.getImg())){
					topic.setImg( env.getProperty("img.real.server.url") + topic.getImg());
				}
			}
		}
		return topicList;
	}

	/**
	 * 获取专题列表信息
	 * @param id
	 * @return
	 */
	public Topic getTopicById(Integer id){
		Topic topic = topicMapper.getTopicById(id);
		if(topic != null && StringUtils.isNotBlank(topic.getImg())){
			topic.setImg(env.getProperty("img.real.server.url") + topic.getImg());
		}
		return topic;
	}

	/**
	 * 更新专题状态
	 * @param id
	 * @param status
	 * @return
	 */
	public String updateTopicStatus(Integer id,Integer status){
		String result = null;
		if(Constants.TOPIC_STATUS_VALID == status){
			result = validTopic(topicMapper.getTopicById(id));
		}else if(Constants.TOPIC_STATUS_PUBLISH != status){
//			根据专题编号删除专题封面集合中的某个专题
			Topic topic = topicMapper.getTopicById(id);
			jedisCache.zrem(JedisKey.SORTEDSET_TOPIC_LIST + topic.getCoverId(), String.valueOf(id));
//			Set<String> keys = jedisCache.keys(JedisKey.SORTEDSET_DISTRIBUTOR_TOPIC + "*");
//			if(!CollectionUtils.isEmpty(keys)){
//				for(String key : keys){
//					jedisCache.zrem(key, String.valueOf(id));
//				}
//			}
//			删除专题缓存
			jedisCache.del(JedisKey.STRING_TOPIC + id);
//			删除专题频道缓存中的专题
			jedisCache.del(JedisKey.SORTEDSET_TOPIC_CHANNEL + id);
		}
		if(result == null){
			topicMapper.updateTopicStatus(id, status);
		}
		return result;
	}

	/**
	 * 新增专题内容
	 * @param title
	 * @param img
	 * @param content
	 * @param publishTime
	 * @param coverId
	 * @return
	 */
	public boolean insertTopic(String title, String img, String content, String publishTime, Integer coverId){
		Topic topic = new Topic();
		topic.setContent(content);
//		topic.setDids(dids);
		String newImg = handleImg(img);
		if(StringUtils.isBlank(newImg)){
			return false;
		}
		topic.setImg(newImg);
		topic.setPublishTimeString(publishTime);
		topic.setTitle(title);
		topic.setCoverId(coverId);
		topicMapper.insertTopic(topic);
		return true;
	}

	/**
	 * 更新专题列表记录
	 * @param id
	 * @param title
	 * @param img
	 * @param content
	 * @param coverId
	 * @param publishTime
	 * @return
	 */
	public boolean updateTopic(Integer id,String title,String img,String content,Integer coverId,String publishTime){
		Topic topic = new Topic();
		topic.setId(id);
		topic.setContent(content);
		topic.setCoverId(coverId);
		String newImg = handleImg(img);
		if(StringUtils.isBlank(newImg)){
			return false;
		}
		topic.setImg(newImg);
		topic.setPublishTimeString(publishTime);
		topic.setTitle(title);
		topicMapper.updateTopic(topic);
		return true;
	}
	
	public void addTopicJoke(String jokeIds,Integer topicId){
		if(StringUtils.isNotBlank(jokeIds)){
			for(String jokeId : jokeIds.split(",")){
				topicMapper.insertTopicJoke(Integer.parseInt(jokeId), topicId);
			}
		}
	}

	/**
	 * 获取待发布的专题
	 * @return
	 */
	public List<Topic> getTopicForPublish(){
		return topicMapper.getTopicForPublish();
	}

	/**
	 * 验证专题是否能正常发布
	 * @param topic
	 * @return
	 */
	private String validTopic(Topic topic){
		if(topic.getPublishTime() == null){
			return "专题的发布时间不能为空";
		}else if(topic.getCoverId() == null){
			return "专题的分类不能为空";
		}else if(StringUtils.isBlank(topic.getTitle())){
			return "专题的主题不能为空";
		}else if(StringUtils.isBlank(topic.getImg())){
			return "专题的图片不能为空";
		}/*else if(StringUtils.isBlank(topic.getContent())){
			return "专题的简介不能为空";
		}*/else{
			List<Joke> jokeIdList =  getJokeListByTopicId(topic.getId());
			if(CollectionUtils.isEmpty(jokeIdList)){
				return "专题的内容不能为空";
			}else{
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.HOUR_OF_DAY, 1);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				if(topic.getPublishTime().compareTo(calendar.getTime()) < 0){
					return "发布时间最少要在下一个小时";
				}
			}
		}
		return null;
	}
	
	public void delTopicJoke(String jokeIds,Integer topicId){
		if(StringUtils.isNotBlank(jokeIds)){
			for(String jokeId : jokeIds.split(",")){
				topicMapper.delTopicJoke(Integer.parseInt(jokeId), topicId);
			}
		}
	}

	/**
	 * 获取段子列表
	 * @param id	专题编号
	 * @return
	 */
	public List<Joke> getJokeListByTopicId(Integer id){
		List<Joke> jokeList = topicMapper.getJokeListByTopicId(id);
		handleJokePhoto(jokeList);
		return jokeList;
	}
	
	public String handleImg(String imgUrl){
		if(StringUtils.isNotBlank(imgUrl)){
			ImgRespDto imgRespDto = HttpUtil.handleImg(env.getProperty("remote.crop.img.server.url"),imgUrl, false);
			if(imgRespDto != null && imgRespDto.getErrorCode() == 0){
				return imgRespDto.getImgUrl();
			}
		}
		return null;
	}

	/**
	 * 添加原创数据内容
	 * @param title
	 * @param imgUrl
	 * @param gifUrl
	 * @param content
	 * @param topicId
	 * @param username
	 */
	public boolean addOriginalContent(String title, String imgUrl, String gifUrl, String content, Integer topicId, String username) {
		boolean result = false;
		Joke joke = new Joke();
		joke.setContent(content);
		joke.setTitle(title);
		if(StringUtils.isNotBlank(gifUrl)){//动图
			joke.setType(Constants.JOKE_TYPE_GIF);
			ImgRespDto imgRespDto = HttpUtil.handleImg(env.getProperty("remote.crop.img.server.url"),gifUrl, true);
			if(imgRespDto != null && imgRespDto.getErrorCode() == 0){
				joke.setGif(imgRespDto.getGifUrl());
				joke.setImg(imgRespDto.getImgUrl());
				joke.setWidth(imgRespDto.getWidth());
				joke.setHeight(imgRespDto.getHeight());
				result = true;
			}
		}else if(StringUtils.isNotBlank(imgUrl)){//静图
			joke.setType(Constants.JOKE_TYPE_IMG);
			ImgRespDto imgRespDto = HttpUtil.handleImg(env.getProperty("remote.crop.img.server.url"),imgUrl, true);
			if(imgRespDto != null && imgRespDto.getErrorCode() == 0){
				joke.setGif(null);
				joke.setImg(imgRespDto.getImgUrl());
				joke.setWidth(imgRespDto.getWidth());
				joke.setHeight(imgRespDto.getHeight());
				result = true;
			}
		}else{//纯文本内容
			joke.setType(Constants.JOKE_TYPE_TEXT);
			result = true;
		}
		
		if(result){
			joke.setVerifyUser(username);
			joke.setUuid(UUID.randomUUID().toString());
			jokeMapper.insertJoke(joke);//存储段子信息
			topicMapper.insertTopicJoke(joke.getId(), topicId);//存储段子专题关联关系
		}
		return result;
	}

	/**
	 * 获取专题封面记录总数
	 * @param status
	 * @return
	 */
	public int getTopicCoverListCount(Integer status) {
		return topicMapper.getTopicCoverListCount(status);
	}
	/**
	 * 获取专题封面列表
	 *
	 * @param status
	 * @param offset
	 * @param pageSize
	 * @return
	 */
    public List<TopicCover> getTopicCoverList(Integer status, int offset, Integer pageSize) {
		List<TopicCover> list = topicMapper.getTopicCoverList(status, offset, pageSize);
    	if(!CollectionUtils.isEmpty(list)){
			for(TopicCover topicCover : list){
				if(StringUtils.isNotBlank(topicCover.getLogo())){
					topicCover.setLogo(env.getProperty("img.real.server.url") + topicCover.getLogo());
				}
			}
		}
		return list;
    }

	/**
	 * 新增专题封面
	 * @param seq
	 * @param name
	 * @param logo
	 * @param userName
	 * @return
	 */
	public int addTopicCover(Integer seq, String name, String logo, String userName) {
		TopicCover t = new TopicCover();
		t.setSeq(seq);
		t.setName(name);
		int index = logo.indexOf("images/");
		if(index > -1){
			logo = logo.substring(index);
		}
		t.setLogo(logo);
		t.setUpdateBy(userName);
		return topicMapper.addTopicCover(t);
	}

	/**
	 * 删除专题封面
	 * @param id
	 * @return
	 */
	public int delTopicColver(Integer id) {
//		删除数据库中专题封面
		int count = topicMapper.delTopicColver(id);
		if(count == 1){
//			删除缓存中的专题封面记录
			jedisCache.del(JedisKey.STRING_TOPIC_COVER + id);
//			删除缓存中专题封面列表中的记录
			jedisCache.zrem(JedisKey.SORTEDSET_TOPIC_COVER_LIST, String.valueOf(id));
		}
		return count;
	}

	/**
	 * 修改专题封面
	 * @param id
	 * @param seq
	 * @param name
	 * @param logo
	 * @param userName
	 * @return
	 */
	public int modifyTopicCover(Integer id, Integer seq, String name, String logo,Integer status, String userName) {
		int index = logo.indexOf("images/");
		if(index > -1){
			logo = logo.substring(index);
		}
//		下线的序号都改为999
		if(status == 0){
			seq = 999;
		}
		return topicMapper.modifyTopicCover(id, seq, name, logo, status, userName);
	}

	/**
	 * 获取所有专题列表
	 * @param status
	 * @return
	 */
	public List<TopicCover> getAllTopicCoverMoveList(Integer status){
		return topicMapper.getAllTopicCoverMoveList(status);
	}
	/**
	 * 移动专题封面位置
	 * @param id
	 * @param seq
	 * @param type
	 * @return
	 */
	public boolean moveTopicCover(Integer id, Integer seq,Integer type) {
		List<TopicCover> list = topicMapper.getAllTopicCoverMoveList(1);
		if(!CollectionUtils.isEmpty(list)){
			TopicCover t = list.get(0);
			if(t.getId() == id){
				return false;
			}
			if(type == 0){ // 置顶
				topicMapper.updateTopicCoverSeq(id, t.getSeq());
				int index = 0;
				int lastId = 0;
				for(TopicCover tc: list){ // 从第一位依次往下交换序号
					if(index == 0){
						lastId = tc.getId();
						index++;
						continue;
					}
					if(tc.getId() == id){
						topicMapper.updateTopicCoverSeq(lastId, tc.getSeq());
						break;
					}else{
						topicMapper.updateTopicCoverSeq(lastId, tc.getSeq());
						lastId = tc.getId();
					}
					index++;
				}
				return true;
			} else if(type == 1){// 上移
				int lastIndex = 0;
				int lastId = 0;
				for(TopicCover tc : list){
					if(tc.getId() == id){
						break;
					}
					lastIndex = tc.getSeq();
					lastId = tc.getId();
				}
				topicMapper.updateTopicCoverSeq(id, lastIndex);
				topicMapper.updateTopicCoverSeq(lastId, seq);
				return true;
			} else if(type == 2){// 下移
				int lastIndex = 0;
				int lastId = 0;
				int end = 1;
				for(TopicCover tc : list){
					lastIndex = tc.getSeq();
					lastId = tc.getId();
					if(end == 0){
						break;
					}
					if(tc.getId() == id){
						end--;
					}
				}
				topicMapper.updateTopicCoverSeq(id, lastIndex);
				topicMapper.updateTopicCoverSeq(lastId, seq);
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取专题段子分页列表
	 * @param topicId
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List<Joke> getTopicJokeListByTopicId(Integer topicId, int offset, Integer pageSize) {
		List<Joke> jokeList = topicMapper.getTopicJokeListByTopicId(topicId, offset, pageSize);
		handleJokePhoto(jokeList);
		return jokeList;
	}

	/**
	 * 获取专题段子分页列表记录总数
	 * @param topicId
	 * @return
	 */
	public int getTopicJokeListCountByTopicId(Integer topicId) {
		return topicMapper.getTopicJokeListCountByTopicId(topicId);
	}

	/**
	 * 处理段子照片
	 * @param list
	 */
	private void handleJokePhoto(List<Joke> list){
		if(!CollectionUtils.isEmpty(list)){
			for(Joke joke : list){
				if(joke.getType() == Constants.JOKE_TYPE_IMG){
					joke.setImg( env.getProperty("img.real.server.url") + joke.getImg());
				}else if(joke.getType() == Constants.JOKE_TYPE_GIF){
					joke.setImg( env.getProperty("img.real.server.url") + joke.getImg());
					joke.setGif( env.getProperty("img.real.server.url") + joke.getGif());
				}
			}
		}
	}

	/**
	 * 刷新专题封面缓存
	 * @param flushPass
	 * @return
	 */
	public boolean flushTopicCoverCache(String flushPass) {
		long start = System.currentTimeMillis();
		int size = 0;
		logger.info("手动刷新专题封面开始...");
		if(flushPass.equals("admin@joke.com")){
			try{
//		查询正常上线的专题封面
				List<TopicCover> list = topicMapper.getAllTopicCoverMoveList(Constants.ENABLE_STATUS);
				if(!CollectionUtils.isEmpty(list)){
					Map<String,Double> map = Maps.newHashMap();
					for(TopicCover topicCover : list){
						map.put(topicCover.getId().toString(), Double.valueOf(topicCover.getSeq()));
//					缓存专题封面
						jedisCache.set(JedisKey.STRING_TOPIC_COVER + topicCover.getId(), JSON.toJSONString(topicCover));
					}
//				更新专题封面列表缓存
					jedisCache.delAndSetSortedSet(JedisKey.SORTEDSET_TOPIC_COVER_LIST, map);
					size = map.size();
					return true;
				} else {
					return false;
				}
			}catch (Exception e){
				logger.error("刷新专题封面缓存异常:" + e.getMessage(), e);
				return false;
			}finally {
				long end = System.currentTimeMillis();
				logger.info("定时更新专题封面结束: {}条记录更新, 耗时:{}ms", size, end - start);
			}
		} else {
			long end = System.currentTimeMillis();
			logger.info("定时更新专题封面结束: {}条记录更新, 耗时:{}ms", size, end - start);
			return false;
		}
	}
}
