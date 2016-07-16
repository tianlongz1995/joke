package com.oupeng.joke.back.task;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Maps;
import com.oupeng.joke.back.service.ChannelService;
import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.back.service.MailService;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Channel;

@Component
public class CommonChannelTask {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonChannelTask.class);
	
	@Autowired
	private JokeService jokeService;
	@Autowired
	private ChannelService channelService;
	@Autowired
	private JedisCache jedisCache;
	@Autowired
	private MailService mailService;
	@Autowired
	private Environment env;
	
	/**
	 * 发布普通频道下的段子数据，每天凌晨30分时候发布，每次发布前一天数据(审核通过且不属于专题频道的数据)
	 * */
	@Scheduled(cron="0 30 0 * * ?")
	public void publishCommonChannelJoke(){
		List<Channel> channelList = channelService.getChannelList(Constants.CHANNEL_STATUS_VALID);
		if(!CollectionUtils.isEmpty(channelList)){
			StringBuffer log = new StringBuffer();
			log.append("common channel joke publish result:\r\n ");
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
						log.append(String.format("		[id:%d,name:%s,size:%d],\r\n",channel.getId(),channel.getName(),jokeIds.size()));
					}else{
						log.append(String.format("		[id:%d,name:%s,size:%d],\r\n",channel.getId(),channel.getName(),0));
					}
				}
			}
			logger.info(log.toString());
			mailService.sendMail(env.getProperty("data.publish.recipient"),env.getProperty("data.publish.cc"),"JOKE--Common channel data publish", log.toString());
			logger.info("邮件发送成功");
		}
	}
}
