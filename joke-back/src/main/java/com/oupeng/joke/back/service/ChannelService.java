package com.oupeng.joke.back.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.dao.mapper.ChannelMapper;
import com.oupeng.joke.domain.Channel;

@Service
public class ChannelService {

	@Autowired
	private ChannelMapper channelMapper;
	
	public List<Channel> getChannelList(Integer status){
		return channelMapper.getChannelList(status);
	}
	
	public Channel getChannelById(Integer id){
		return channelMapper.getChannelById(id);
	}
	
	public String updateChannelStatus(Integer id,Integer status){
		String result = null;
		Integer good = 0;
		Integer bad = 0;
		if(status == Constants.CHANNEL_STATUS_VALID ){
			Integer type = channelMapper.getChannelById(id).getType();
			if(type != Constants.CHANNEL_TYPE_COMMON
					&& !CollectionUtils.isEmpty(channelMapper.getChannelByType(type))){
				result = "专题频道、推荐频道同时只能存在一个";
			}
		}else{
			channelMapper.updateChannelStatus(id, status, good, bad);
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
}
