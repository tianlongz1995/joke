package com.oupeng.joke.dao.mapper;

import com.oupeng.joke.dao.sqlprovider.DistributorSqlProvider;
import com.oupeng.joke.domain.Distributor;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface DistributorMapper {

	/**
	 * 获取渠道列表
	 * @param status
	 * @return
	 */
	@SelectProvider(method="getDistributorList",type=DistributorSqlProvider.class)
	List<Distributor> getDistributorList(Integer status);

	/**
	 * 获取渠道信息
	 * @param id 渠道编号
	 * @return
	 */
	@Select(value="select id,name,status,create_time as createTime,"
			+ "update_time as updateTime from distributor where id = ${id}")
	Distributor getDistributorById(@Param(value = "id")Integer id);

	/**
	 * 修改状态
	 * @param id
	 * @param status
	 */
	@Select(value="update distributor set update_time=now(),status = #{status} where id = ${id}")
	void updateDistributorStatus(@Param(value = "id") Integer id, @Param(value = "status") Integer status);
	
	/**
	 * 新增渠道
	 * @param distributor
	 */
	@InsertProvider(method="insertDistributor",type=DistributorSqlProvider.class)
	void insertDistributor(Distributor distributor);

	/**
	 * 更新渠道信息
	 * @param distributor
	 */
	@UpdateProvider(method="updateDistributor",type=DistributorSqlProvider.class)
	void updateDistributor(Distributor distributor);
}
