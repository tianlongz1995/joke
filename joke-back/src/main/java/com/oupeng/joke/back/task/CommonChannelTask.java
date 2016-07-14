package com.oupeng.joke.back.task;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Maps;
import com.oupeng.joke.back.service.ChannelService;
import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Channel;

@Component
public class CommonChannelTask {

	@Autowired
	private JokeService jokeService;
	@Autowired
	private ChannelService channelService;
	@Autowired
	private JedisCache jedisCache;
	
	/**
	 * 发布普通频道下的段子数据，每天凌晨30分时候发布，每次发布前一天数据(审核通过且不属于专题频道的数据)
	 * */
	@Scheduled(cron="0 30 0 * * ?")
	public void publishCommonChannelJoke(){
		List<Channel> channelList = channelService.getChannelList(Constants.CHANNEL_STATUS_VALID);
		if(!CollectionUtils.isEmpty(channelList)){
			List<Integer> jokeIds = null;
			Map<String,Double> map = Maps.newHashMap();
			for(Channel channel : channelList){
				if(channel.getType() == Constants.CHANNEL_TYPE_COMMON
						&& StringUtils.isNotBlank(channel.getContentType())){
					jokeIds = jokeService.getJokeForPublishChannel(channel.getContentType());
					if(!CollectionUtils.isEmpty(jokeIds)){
						for(Integer jokeId : jokeIds){
							map.put(String.valueOf(jokeId), Double.valueOf(jokeId));
						}
						jedisCache.zadd(JedisKey.SORTEDSET_COMMON_CHANNEL+channel.getId(),map);
						map.clear();
					}
				}
			}
		}
	}
}
