package com.oupeng.joke.back.service;

import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.back.util.HttpUtil;
import com.oupeng.joke.back.util.ImgRespDto;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.JokeMapper;
import com.oupeng.joke.dao.mapper.TopicMapper;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.Topic;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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


    private String imgPrefix = "http://joke2-img.oupeng.com/";

    private String remoteImgPrefix = "http://joke2-img.oupeng.com/";

    @PostConstruct
    private void init(){
        String url = env.getProperty("img.real.server.url");
        if (StringUtils.isNoneBlank(url)) {
            imgPrefix = url;
        } else {
            logger.error("getProperty(\"img.real.server.url\") is null:{}", url);
        }
        String remoteImgUrl = env.getProperty("remote.crop.img.server.url");
        if (StringUtils.isNoneBlank(remoteImgUrl)) {
            remoteImgPrefix = remoteImgUrl;
        } else {
            logger.error("getProperty(\"img.real.server.url\") is null:{}", url);
        }
    }
	/**
	 * 获取专题列表记录总数
	 * @param status
	 * @return
	 */
	public int getTopicListCount(Integer status) {
		return topicMapper.getTopicListCount(status);
	}

	/**
	 * 获取专题列表
	 * @param status
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List<Topic> getTopicList(Integer status, Integer offset,Integer pageSize){
		List<Topic> topicList = topicMapper.getTopicList(status, offset, pageSize);
		if(!CollectionUtils.isEmpty(topicList)){
			for(Topic topic : topicList){
				if(StringUtils.isNotBlank(topic.getImg())){
					topic.setImg( imgPrefix + topic.getImg());
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
			topic.setImg(imgPrefix + topic.getImg());
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
			jedisCache.zrem(JedisKey.SORTEDSET_TOPIC_LIST, String.valueOf(id));
//			删除专题缓存
			jedisCache.del(JedisKey.STRING_TOPIC + id);
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
	 * @return
	 */
	public boolean insertTopic(String title, String img, String content, String publishTime){
		Topic topic = new Topic();
		topic.setContent(content);
		String newImg = handleImg(img);
		if(StringUtils.isBlank(newImg)){
			return false;
		}
		topic.setImg(newImg);
		topic.setPublishTimeString(publishTime);
		topic.setTitle(title);
		topicMapper.insertTopic(topic);
		return true;
	}

	/**
	 * 更新专题列表记录
	 * @param id
	 * @param title
	 * @param img
	 * @param content
	 * @param publishTime
	 * @return
	 */
	public boolean updateTopic(Integer id,String title,String img,String content,String publishTime){
		Topic topic = new Topic();
		topic.setId(id);
		topic.setContent(content);
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
		}else if(StringUtils.isBlank(topic.getTitle())){
			return "专题的主题不能为空";
		}else if(StringUtils.isBlank(topic.getImg())){
			return "专题的图片不能为空";
		}else{
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
			ImgRespDto imgRespDto = HttpUtil.handleImg(remoteImgPrefix,imgUrl, false);
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
			ImgRespDto imgRespDto = HttpUtil.handleImg(remoteImgPrefix,gifUrl, true);
			if(imgRespDto != null && imgRespDto.getErrorCode() == 0 && StringUtils.isNotBlank(imgRespDto.getImgUrl()) && StringUtils.isNotBlank(imgRespDto.getGifUrl())){
				joke.setGif(imgRespDto.getGifUrl());
				joke.setImg(imgRespDto.getImgUrl());
				joke.setWidth(imgRespDto.getWidth());
				joke.setHeight(imgRespDto.getHeight());
				result = true;
			}else{
				logger.error("添加原创处理动图失败:" , imgRespDto);
			}
		}else if(StringUtils.isNotBlank(imgUrl)){//静图
			joke.setType(Constants.JOKE_TYPE_IMG);
			ImgRespDto imgRespDto = HttpUtil.handleImg(remoteImgPrefix,imgUrl, true);
			if(imgRespDto != null && imgRespDto.getErrorCode() == 0 && StringUtils.isNotBlank(imgRespDto.getImgUrl())){
				joke.setGif(null);
				joke.setImg(imgRespDto.getImgUrl());
				joke.setWidth(imgRespDto.getWidth());
				joke.setHeight(imgRespDto.getHeight());
				result = true;
			}else{
				logger.error("添加原创处理动图失败:" , imgRespDto);
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
					joke.setImg( imgPrefix + joke.getImg());
				}else if(joke.getType() == Constants.JOKE_TYPE_GIF){
					joke.setImg( imgPrefix + joke.getImg());
					joke.setGif( imgPrefix + joke.getGif());
				}
			}
		}
	}

}
