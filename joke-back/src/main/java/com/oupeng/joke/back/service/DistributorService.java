package com.oupeng.joke.back.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oupeng.joke.dao.mapper.DistributorMapper;
import com.oupeng.joke.domain.Distributor;

@Service
public class DistributorService {
	@Autowired
	private DistributorMapper distributorMapper;


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

	public List<Integer> getDistributorIds() {
		return distributorMapper.getDistributorIds();
	}
}
