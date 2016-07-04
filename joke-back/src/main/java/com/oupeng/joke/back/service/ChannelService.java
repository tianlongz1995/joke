package com.oupeng.joke.back.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public void updateChannelStatus(Integer id,Integer status){
		Integer good = 0;
		Integer bad = 0;
		channelMapper.updateChannelStatus(id, status, good, bad);
	}
	
	public Channel getChannelByType(Integer type){
		return channelMapper.getChannelByType(type);
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
