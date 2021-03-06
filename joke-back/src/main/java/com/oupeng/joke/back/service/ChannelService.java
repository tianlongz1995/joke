package com.oupeng.joke.back.service;

import java.util.List;
import java.util.Set;

import com.oupeng.joke.domain.ChannelMenu;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.back.util.TransformUtil;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.ChannelMapper;
import com.oupeng.joke.domain.Channel;

@Service
public class ChannelService {

	@Autowired
	private ChannelMapper channelMapper;
	@Autowired
	private JedisCache jedisCache;

	/**
	 * 获取频道列表
	 * @param status
	 * @return
	 */
	public List<Channel> getChannelList(Integer status){
		return channelMapper.getChannelList(status);
	}

	/**
	 * 获取频道
	 * @param id
	 * @return
	 */
	public Channel getChannelById(Integer id){
		return channelMapper.getChannelById(id);
	}

	/**
	 * 更新频道状态
	 * @param id
	 * @param status 状态 0:下线 1:上线
	 * @return
	 */
	public String updateChannelStatus(Integer id,Integer status){
		Channel channel = channelMapper.getChannelById(id);
		String result = null;
//		渠道上线
		if(status == Constants.CHANNEL_STATUS_VALID ){
			if(StringUtils.isBlank(channel.getContentType())){
				result = "专题内容属性不能为空";
			}else if(channel.getType() != Constants.CHANNEL_TYPE_COMMON
					&& !CollectionUtils.isEmpty(channelMapper.getChannelByType(channel.getType()))){
				result = "专题频道、推荐频道同时只能存在一个";
			}
//		渠道下线 - 删除缓存
		}else{
			if(channel.getType() == Constants.CHANNEL_TYPE_RECOMMEND){
				jedisCache.del(JedisKey.SORTEDSET_RECOMMEND_CHANNEL);
			}else if(channel.getType() == Constants.CHANNEL_TYPE_COMMON){
				jedisCache.del(JedisKey.SORTEDSET_COMMON_CHANNEL + id);
			}else if(channel.getType() == Constants.CHANNEL_TYPE_TOPIC){
				Set<String> keys = jedisCache.keys(JedisKey.SORTEDSET_TOPIC_ALL);
				if(!CollectionUtils.isEmpty(keys)){
					jedisCache.del(keys.toArray(new String[]{}));
				}
			}
		}
		
		if(result == null){
			channelMapper.updateChannelStatus(id, status);
		}
		return result;
	}

	/**
	 * 新增频道
	 * @param name
	 * @param type
	 * @param contentType
	 */
	public void insertChannel(String name,Integer type,String contentType,Integer size){
		Channel channel = new Channel();
		channel.setName(name);
		channel.setType(type);
		channel.setContentType(contentType);
		channel.setSize(size);
		channelMapper.insertChannel(channel);
	}

	/**
	 * 修改频道
	 * @param id
	 * @param name
	 * @param type
	 * @param contentType
	 */
	public void updateChannel(Integer id ,String name,Integer type,String contentType,Integer size){
		Channel channel = new Channel();
		channel.setId(id);
		channel.setName(name);
		channel.setType(type);
		channel.setSize(size);
		channel.setContentType(contentType);
		channelMapper.updateChannel(channel);
	}

	/**
	 *	获取频道状态列表
	 * @return
	 */
	public List<Channel> getChannelStatusList(Integer id){
		return channelMapper.getChannelStatusList(id);
	}
	/**
	 *	获取频道列表
	 * @return
	 */
	public List<ChannelMenu> getDistributorChannelList(Integer id){
		return channelMapper.getDistributorChannelList(id);
	}

	public String getChannelIdListByName(String name){
		return TransformUtil.listToString(channelMapper.getChannelIdListByName(name));
	}
}
