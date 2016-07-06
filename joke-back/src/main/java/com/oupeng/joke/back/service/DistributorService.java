package com.oupeng.joke.back.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oupeng.joke.dao.mapper.DistributorMapper;
import com.oupeng.joke.domain.Distributor;

@Service
public class DistributorService {
	private static final Logger logger = LoggerFactory.getLogger(DistributorService.class);
	@Autowired
	private DistributorMapper distributorMapper;

	/**
	 * 获取渠道列表
	 * @param status
	 * @return
	 */
	public List<Distributor> getDistributorList(Integer status){
		return distributorMapper.getDistributorList(status);
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
	 */
	public void insertDistributor(String name,Integer status){
		Distributor distributor = new Distributor();
		distributor.setName(name);
		distributor.setStatus(status);
		distributorMapper.insertDistributor(distributor);
	}

	/**
	 * 更新渠道信息
	 * @param id
	 * @param name
	 * @param status
	 */
	public void updateDistributor(Integer id ,String name,Integer status){
		Distributor distributor = new Distributor();
		distributor.setId(id);
		distributor.setName(name);
		distributor.setStatus(status);
		distributorMapper.updateDistributor(distributor);
	}
}
