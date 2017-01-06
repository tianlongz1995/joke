package com.oupeng.joke.back.service;

import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.dao.mapper.AdMapper;
import com.oupeng.joke.dao.mapper.ChannelMapper;
import com.oupeng.joke.dao.mapper.DistributorsMapper;
import com.oupeng.joke.domain.AdConfig;
import com.oupeng.joke.domain.Channel;
import com.oupeng.joke.domain.Distributor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistributorsService {
	private static final Logger logger = LoggerFactory.getLogger(DistributorsService.class);
	@Autowired
	private DistributorsMapper distributorsMapper;

	/**
	 * 获取渠道总数
	 * @param distributor
	 * @return
	 */
	public int getCount(Distributor distributor) {
		return distributorsMapper.getCount(distributor);
	}

	/**
	 * 获取渠道列表
	 * @param distributor
	 * @return
	 */
	public List<Distributor> getList(Distributor distributor) {
		return distributorsMapper.getList(distributor);
	}

	/**
	 * 新增渠道
	 * @param name
	 * @param status
	 * @param username
	 * @return
	 */
	public void add(String name, Integer status, String username, Integer[] channelIds) {
		Distributor distributors = new Distributor();
		distributors.setName(name);
		distributors.setStatus(status);
		distributors.setCreateBy(username);
		distributorsMapper.add(distributors);
		int length = 0;
		if(distributors.getId() != null && channelIds != null && channelIds.length > 0){
			for(int i = 1; i <= channelIds.length; i++){
				distributorsMapper.addChannels(distributors.getId(), channelIds[i-1], i);
				length++;
			}
		}
		logger.info("新增渠道[{}], 频道关联[{}]条", distributors.getId(), length);
	}

	/**
	 * 获取渠道
	 * @param id
	 * @return
	 */
	public Distributor getDistributors(Integer id) {
		return distributorsMapper.getDistributors(id);
	}

	/**
	 * 获取频道列表
	 * @param id
	 * @return
	 */
	public List<Channel> getChannels(Integer id) {
		return distributorsMapper.getChannels(id);
	}

	/**
	 * 修改渠道
	 * @param id
	 * @param name
	 * @param status
	 * @param channelIds
	 */
	public void edit(Integer id, String name, Integer status, String username, Integer[] channelIds, Integer s, Integer lc, Integer lb, Integer dt, Integer dc, Integer db, Integer di) {
		Distributor distributors = new Distributor();
		distributors.setId(id);
		distributors.setName(name);
		distributors.setStatus(status);
		distributors.setUpdateBy(username);
		distributorsMapper.edit(distributors);

		int count = 0, length = 0;
		if(channelIds != null && channelIds.length > 0){
			count = distributorsMapper.deleteChannels(id);
			for(int i = 1; i <= channelIds.length; i++){
				distributorsMapper.addChannels(id, channelIds[i-1], i);
				length++;
			}
		}

//		处理广告
		AdConfig ad = new AdConfig();
		ad.setLc(lc);
		ad.setLb(lb);
		ad.setDt(dt);
		ad.setDc(dc);
		ad.setDb(db);
		ad.setDi(di);
		ad.setS(s);



		logger.info("修改渠道[{} - {}], 频道关联删除[{}]条, 新增[{}]条", id, name, count, length);
	}
}
