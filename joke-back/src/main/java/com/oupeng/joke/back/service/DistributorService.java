package com.oupeng.joke.back.service;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.back.util.TransformUtil;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.AdMapper;
import com.oupeng.joke.dao.mapper.ChannelMapper;
import com.oupeng.joke.dao.mapper.DistributorMapper;
import com.oupeng.joke.domain.Ad;
import com.oupeng.joke.domain.AdConfig;
import com.oupeng.joke.domain.ChannelMenu;
import com.oupeng.joke.domain.Distributor;
import com.oupeng.joke.domain.response.DistributorConfig;
import com.oupeng.joke.domain.response.Failed;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class DistributorService {
	private static final Logger logger = LoggerFactory.getLogger(DistributorService.class);
	private static final String MANAGER_KEY = "admin@joke.com";
	@Autowired
	private DistributorMapper distributorMapper;
	@Autowired
	private AdMapper adMapper;
	@Autowired
	private ChannelMapper channalMapper;
	@Autowired
	private JedisCache jedisCache;
	/**
	 * 获取渠道记录数
	 * @param distributor
	 * @return
	 */
	public int getDistributorListCount(Distributor distributor) {
		return distributorMapper.getDistributorListCount(distributor);
	}

	/**
	 * 获取渠道列表
	 * @param distributor
	 * @return
	 */
	public List<Distributor> getDistributorList(Distributor distributor){
		return distributorMapper.getDistributorList(distributor);
	}
	/**
	 * 获取渠道列表
	 * @return
	 */
	public List<Distributor> getAllDistributorList() {
		return distributorMapper.getAllDistributorList();
	}
	/**
	 * 获取渠道信息
	 * @param id 渠道编号
	 * @return
	 */
	public Distributor getDistributorById(Integer id){
		return distributorMapper.getDistributorById(id);
	}

	/**
	 * 修改状态
	 * @param id
	 * @param status
	 */
	public void updateDistributorStatus(Integer id,Integer status){
		distributorMapper.updateDistributorStatus(id, status);
	}
	
	/**
	 * 新增渠道
	 * @param name
	 * @param status
	 * @param channelIds
	 */
	public void insertDistributor(String name, Integer status, Integer[] channelIds){
		Distributor distributor = new Distributor();
		distributor.setName(name);
		distributor.setStatus(status);
		distributorMapper.insertDistributor(distributor);
		if(distributor.getId() != null && channelIds.length > 0){
			distributorMapper.insertDistributorChannels(distributor.getId(), channelIds);
		}
	}

	/**
	 * 更新渠道信息
	 * @param id
	 * @param name
	 * @param status
	 * @param channelIds
	 */
	public void updateDistributor(Integer id, String name, Integer status, Integer[] channelIds){
		Distributor distributor = new Distributor();
		distributor.setId(id);
		distributor.setName(name);
		distributor.setStatus(status);
		distributorMapper.updateDistributor(distributor);
		if(id != null){
			distributorMapper.deleteDistributorChannels(id);
			if(channelIds.length > 0){
				distributorMapper.insertDistributorChannels(id, channelIds);
			}
		}
	}
//
//	/**
//	 * 获取渠道列表
//	 * @return
//	 */
//	public List<Integer> getAllDistributorIds() {
//		return distributorMapper.getDistributorIds();
//	}

	/**
	 *
	 * @param managerKey
	 * @return
	 */
	public boolean checkManagerKey(String managerKey) {
		if(managerKey.equals(MANAGER_KEY)){
			return true;
		}
		return false;
	}

	/**
	 * 更新渠道广告配置缓存
	 * @param managerKey
	 * @return
	 */
	public Result updateDistributorAdConfigCache(String managerKey) {
		StringBuffer sb = new StringBuffer();
		if(!managerKey.equals(MANAGER_KEY)){
			return new Failed(sb.append("管理密码错误！").toString());
		}
		String result = null;
		long start = System.currentTimeMillis();
		logger.info("Manual syncDistributorAdConfig starting...");
		List<Integer> ids = distributorMapper.getDistributorIds();
		if(!CollectionUtils.isEmpty(ids)){
			int index = 0;
			for(Integer id : ids){
				List<ChannelMenu> channels = channalMapper.getDistributorChannelList(id);
				List<Ad> ads = adMapper.getDistributorAdList(id);
				AdConfig adConfig = new AdConfig(ads);
				DistributorConfig dcr = new DistributorConfig();
				if(!CollectionUtils.isEmpty(channels)){
					dcr.setChannels(channels);
				}
				if(!CollectionUtils.isEmpty(ads)){
					dcr.setAdConfig(adConfig);
				}
				String value = JSON.toJSONString(dcr);
				if(value != null && value.length() > 0){
					jedisCache.hset(JedisKey.JOKE_HASH_DISTRIBUTOR_CONFIG, String.valueOf(id), value);
				}else{
					logger.debug("Manual syncDistributorAdConfig did[{}] body is null", id);
				}
				index++;
			}
			long end = System.currentTimeMillis();
			sb.append("手动更新渠道广告配置缓存[").append(ids.size()).append("]条完成，");
			sb.append("实际完成:[").append(index).append("]条");
			sb.append("耗时:[").append(end-start).append("]ms");
			result = sb.toString();
			logger.info(result);
			return new Success(result, null);
		}else{
			sb.append("手动更新渠道广告配置缓存[").append(ids).append("]完成，");
			sb.append("更新失败！");
			result = sb.toString();
			logger.error(result);
			return new Failed(result);
		}
	}

	/**
	 * 获取所有渠道信息列表
	 * @return
	 */
	public List<Distributor> getAllDistributors() {
		return distributorMapper.getAllDistributors();
	}
	
	public String getDistributorIdListByName(String name){
		return TransformUtil.listToString(distributorMapper.getDistributorIdListByName(name));
	}

}
