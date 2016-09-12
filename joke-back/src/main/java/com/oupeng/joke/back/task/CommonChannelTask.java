package com.oupeng.joke.back.task;

import java.util.List;
import java.util.Map;

import com.oupeng.joke.domain.Joke;
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
	 * 发布普通频道下的段子数据，每天下午6点时候发布，每个频道最多发布100条数据
	 * */
	@Scheduled(cron="0 0 18 * * ?")
	public void publishCommonChannelJoke(){
//		获取有效频道列表
		List<Channel> channelList = channelService.getChannelList(Constants.CHANNEL_STATUS_VALID);
		if(!CollectionUtils.isEmpty(channelList)){
			StringBuffer log = new StringBuffer();
			log.append("common channel joke publish result:\r\n ");
			List<Joke> jokes = null;
			Map<String,Double> map = Maps.newHashMap();
			int surplusJokeCount = 0;
			int notAuditedJokeCount = 0;
			for(Channel channel : channelList){
				if(channel.getType() == Constants.CHANNEL_TYPE_COMMON && StringUtils.isNotBlank(channel.getContentType())){
//					查询最近审核通过的数据
					jokes = jokeService.getJokeForPublishChannel(channel.getContentType(), channel.getSize());
//					查询未审核的段子数
					notAuditedJokeCount = jokeService.getJokeCountForPublishChannel(channel.getContentType(), Constants.JOKE_STATUS_NOT_AUDITED);
					if(!CollectionUtils.isEmpty(jokes)){
						StringBuffer jokeids = new StringBuffer();
						for(Joke joke : jokes){
//							按照段子审核时间进行权重
							map.put(String.valueOf(joke.getId()), Double.valueOf(joke.getVerifyTime() != null ? joke.getVerifyTime().getTime() : joke.getId()));
							jokeids.append(joke.getId()).append(",");
						}
						jedisCache.zadd(JedisKey.SORTEDSET_COMMON_CHANNEL + channel.getId(),map);
//						更新段子发布状态
						jokeService.updateJokeForPublishChannel(jokeids.deleteCharAt(jokeids.lastIndexOf(",")).toString());
//						查询待审核段子数量
						surplusJokeCount = jokeService.getJokeCountForPublishChannel(channel.getContentType(),Constants.JOKE_STATUS_VALID);
						map.clear();
						log.append(String.format("		[id:%d,name:%s,size:%d,surplus:%d,notAudited:%d],\r\n",channel.getId(),channel.getName(),jokes.size(),surplusJokeCount,notAuditedJokeCount));
					}else{
						log.append(String.format("		[id:%d,name:%s,size:%d,surplus:%d,notAudited:%d],\r\n",channel.getId(),channel.getName(),0,0,notAuditedJokeCount));
					}
				}
			}
			logger.info(log.toString());
			mailService.sendMail(env.getProperty("data.publish.recipient"),env.getProperty("data.publish.cc"),"JOKE--Common channel data publish", log.toString());
			logger.info("邮件发送成功");
		}
	}
}
