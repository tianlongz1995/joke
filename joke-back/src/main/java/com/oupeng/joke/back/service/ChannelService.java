package com.oupeng.joke.back.service;

import java.util.List;
import java.util.Set;

import com.oupeng.joke.domain.ChannelMenu;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oupeng.joke.back.util.Constants;
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
	
	public List<Channel> getChannelList(Integer status){
		return channelMapper.getChannelList(status);
	}

	public Channel getChannelById(Integer id){
		return channelMapper.getChannelById(id);
	}
	
	public String updateChannelStatus(Integer id,Integer status){
		Channel channel = channelMapper.getChannelById(id);
		String result = null;
		if(status == Constants.CHANNEL_STATUS_VALID ){
			if(StringUtils.isBlank(channel.getContentType())){
				result = "专题内容属性不能为空";
			}else if(channel.getType() != Constants.CHANNEL_TYPE_COMMON
					&& !CollectionUtils.isEmpty(channelMapper.getChannelByType(channel.getType()))){
				result = "专题频道、推荐频道同时只能存在一个";
			}
				
		}else{
			if(channel.getType() == Constants.CHANNEL_TYPE_RECOMMEND){
				jedisCache.del(JedisKey.SORTEDSET_RECOMMEND_CHANNEL);
			}else if(channel.getType() == Constants.CHANNEL_TYPE_COMMON){
				jedisCache.del(JedisKey.SORTEDSET_COMMON_CHANNEL+id);
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
	
	public void insertChannel(String name,Integer type,String contentType){
		Channel channel = new Channel();
		channel.setName(name);
		channel.setType(type);
		channel.setContentType(contentType);
		channelMapper.insertChannel(channel);
	}
	
	public void updateChannel(Integer id ,String name,Integer type,String contentType){
		Channel channel = new Channel();
		channel.setId(id);
		channel.setName(name);
		channel.setType(type);
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
	 * 获取渠道下频道列表
	 * @param id
	 * @return
	 */
	public List<ChannelMenu> getDistributorChannelList(Integer id) {
		return channelMapper.getDistributorChannelList(id);
	}
}
