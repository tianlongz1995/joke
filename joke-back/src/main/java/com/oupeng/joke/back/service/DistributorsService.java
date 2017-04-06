package com.oupeng.joke.back.service;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.DistributorsMapper;
import com.oupeng.joke.domain.*;
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
	@Autowired
	private JedisCache jedisCache;
	@Autowired
	private IndexCacheFlushService indexCacheFlushService;

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
	public void add(Integer id, String name, Integer status, String username, Integer[] channelIds, Integer s, Integer lc, Integer lb, Integer dt, Integer dc, Integer db, Integer di) {
	    Distributor distributors = new Distributor();
        distributors.setId(id);
        distributors.setName(name);
		distributors.setStatus(status);
		distributors.setCreateBy(username);
		distributorsMapper.add(distributors);
		DistributorsConfig d = new DistributorsConfig();
		List<Channels> channels;
		//		修改频道
		int length = 0;
		if(distributors.getId() != null && channelIds != null && channelIds.length > 0){
			for(int i = 1; i <= channelIds.length; i++){
				distributorsMapper.addChannels(distributors.getId(), channelIds[i-1], i);
				length++;
			}
			channels = distributorsMapper.getDistributorChannels(distributors.getId());
			d.setChannels(channels);
		}
		//		处理广告
		if(distributors.getId() != null){
			Ads ad = new Ads();
			ad.setLc(lc);
			ad.setLb(lb);
			ad.setDt(dt);
			ad.setDc(dc);
			ad.setDb(db);
			ad.setDi(di);
			ad.setS(s);
			ad.setDid(distributors.getId());
			ad.setCreateBy(username);
			distributorsMapper.addAd(ad);
			if(status == 1){ // 新增上线才缓存
//			缓存渠道菜单
				ad.setCreateBy(null);
				ad.setCreateTime(null);
				d.setAds(ad);
				jedisCache.hset(JedisKey.JOKE_DISTRIBUTOR_CONFIG, distributors.getId().toString(), JSON.toJSONString(d));
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
	 * 获取渠道关联频道列表
	 * @param id
	 * @return
	 */
	public List<Channel> getChannelSelected(Integer id) {
		return distributorsMapper.getChannelSelected(id);
	}

	/**
	 * 获取频道列表
	 * @return
	 */
	public List<Channel> getChannels() {
		return distributorsMapper.getChannels();
	}
	/**
	 * 修改渠道
	 * @param id
	 * @param name
	 * @param status
	 * @param channelIds
	 */
	public void edit(Integer id, String name, Integer status, String username, Integer[] channelIds, Integer s, Integer lc, Integer lb, Integer dt, Integer dc, Integer db, Integer di,Integer dr) {
		Distributor distributors = new Distributor();
		distributors.setId(id);
		distributors.setName(name);
		distributors.setStatus(status);
		distributors.setUpdateBy(username);
		distributorsMapper.edit(distributors);
		DistributorsConfig d = new DistributorsConfig();
		List<Channels> channels;
//		修改频道
		int count = 0, length = 0;
		if(channelIds != null && channelIds.length > 0){
			count = distributorsMapper.deleteChannels(id);
			for(int i = 1; i <= channelIds.length; i++){
				distributorsMapper.addChannels(id, channelIds[i-1], i);
				length++;
			}
			channels = distributorsMapper.getDistributorChannels(distributors.getId());
			d.setChannels(channels);
		}

//		处理广告
		Ads ad = new Ads();
		ad.setLc(lc);
		ad.setLb(lb);
		ad.setDt(dt);
		ad.setDc(dc);
		ad.setDb(db);
		ad.setDi(di);
		ad.setDr(dr);
		ad.setS(s);
		ad.setDid(id);
		ad.setUpdateBy(username);
        Ads ads = distributorsMapper.getAds(id);
        if(ads == null){
            ad.setCreateBy(username);
            distributorsMapper.addAd(ad);
        } else {
            distributorsMapper.editAd(ad);
        }
		if(status == 1){
//		缓存渠道菜单
			ad.setCreateBy(null);
			ad.setCreateTime(null);
			ad.setUpdateBy(null);
			ad.setUpdateTime(null);
			ad.setDid(null);
			d.setAds(ad);
			jedisCache.hset(JedisKey.JOKE_DISTRIBUTOR_CONFIG, id.toString(), JSON.toJSONString(d));
//		删除应用内缓存 - 用来刷新
			indexCacheFlushService.updateIndex(new IndexItem(id.toString(), 0));
		} else if(status == 0){
//			处理缓存 - 下线后删除缓存
			jedisCache.hdel(JedisKey.JOKE_DISTRIBUTOR_CONFIG, id.toString());
//			删除应用内缓存
			indexCacheFlushService.updateIndex(new IndexItem(id.toString(), 0));
		}

		logger.info("修改渠道[{} - {}], 频道关联删除[{}]条, 新增[{}]条", id, name, count, length);
	}

	/**
	 * 获取渠道广告配置
	 * @param id
	 * @return
	 */
	public Ads getAds(Integer id) {
		return distributorsMapper.getAds(id);
	}

	/**
	 * 修改上下线状态
	 * @param did
	 * @param code
	 * @param status
	 * @return
	 */
	public boolean editStatus(Integer did, String code, Integer status, String username) {
		String vCode = jedisCache.get(JedisKey.VALIDATION_CODE_PREFIX + username);
		if(vCode != null && code.equals(vCode)){
//			删除验证码缓存
			jedisCache.del(JedisKey.VALIDATION_CODE_PREFIX + username);
			int count =  distributorsMapper.editStatus(did, status, username);
			if(count == 1){
				if(status == 1){ // 上线
					DistributorsConfig d = new DistributorsConfig();
					List<Channels> channels = distributorsMapper.getDistributorChannels(did);
					Ads ad = distributorsMapper.getAds(did);
					d.setChannels(channels);
					ad.setCreateBy(null);
					ad.setCreateTime(null);
					ad.setUpdateBy(null);
					ad.setUpdateTime(null);
					d.setAds(ad);
//					缓存渠道菜单
					jedisCache.hset(JedisKey.JOKE_DISTRIBUTOR_CONFIG, did.toString(), JSON.toJSONString(d));
				} else if (status == 0){ // 下线
//					处理缓存 - 下线后删除缓存
					jedisCache.hdel(JedisKey.JOKE_DISTRIBUTOR_CONFIG, did.toString());
//					删除应用内缓存
					indexCacheFlushService.updateIndex(new IndexItem(did.toString(), 0));
				}
				return true;
			}
		}
		return false;

	}

	/**
	 * 删除渠道
	 * @param did
	 * @param code
	 * @param username
	 * @return
	 */
	public boolean del(Integer did, String code, String username) {
		String vCode = jedisCache.get(JedisKey.VALIDATION_CODE_PREFIX + username);
		if(vCode != null && code.equals(vCode)){
//			删除验证码缓存
			jedisCache.del(JedisKey.VALIDATION_CODE_PREFIX + username);
			int count =  distributorsMapper.del(did, username);
			if(count == 1){
//				处理缓存 - 删除菜单缓存
				jedisCache.hdel(JedisKey.JOKE_DISTRIBUTOR_CONFIG, did.toString());
//				删除应用内缓存
				indexCacheFlushService.updateIndex(new IndexItem(did.toString(), 0));
				return true;
			}
		}
		return false;

	}

    /**
     * 渠道是否存在
     * @param id
     * @return
     */
    public boolean exist(Integer id) {
        return distributorsMapper.getDistributorsCount(id) > 0;
    }

    /**
     * 查询渠道横幅列表
     * @param id
     * @return
     */
    public List<Banner> getDistributorsBanners(Integer id) {
        return distributorsMapper.getDistributorsBanners(id);
    }
}
