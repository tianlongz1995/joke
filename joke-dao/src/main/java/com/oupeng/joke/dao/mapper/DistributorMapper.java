package com.oupeng.joke.dao.mapper;

import com.oupeng.joke.dao.sqlprovider.DistributorSqlProvider;
import com.oupeng.joke.domain.Distributor;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface DistributorMapper {
	/**
	 * 获取渠道记录数
	 * @param distributor
	 * @return
	 */
	@SelectProvider(method="getDistributorListCount",type=DistributorSqlProvider.class)
	Integer getDistributorListCount(Distributor distributor);
	/**
	 * 获取渠道列表
	 * @param distributor
	 * @return
	 */
	@SelectProvider(method="getDistributorList",type=DistributorSqlProvider.class)
	List<Distributor> getDistributorList(Distributor distributor);

	/**
	 * 获取渠道信息
	 * @param id 渠道编号
	 * @return
	 */
	@Select(value="select id,name,status,create_time as createTime,"
			+ "update_time as updateTime from distributor where id = #{id}")
	Distributor getDistributorById(@Param(value = "id")Integer id);

	/**
	 * 修改状态
	 * @param id
	 * @param status
	 */
	@Select(value="update distributor set update_time=now(),status = #{status} where id = #{id}")
	void updateDistributorStatus(@Param(value = "id") Integer id, @Param(value = "status") Integer status);
	
	/**
	 * 新增渠道
	 * @param distributor
	 */
	@InsertProvider(method="insertDistributor",type=DistributorSqlProvider.class)
	@SelectKey(statement="SELECT LAST_INSERT_ID() as id", keyProperty="id", before=false, resultType=Integer.class)
	void insertDistributor(Distributor distributor);

	/**
	 * 更新渠道信息
	 * @param distributor
	 */
	@UpdateProvider(method="updateDistributor",type=DistributorSqlProvider.class)
	void updateDistributor(Distributor distributor);

	/**
	 *	存储渠道对应频道列表
	 * @param distributorId
	 * @param channelIds
	 */
	@InsertProvider(method="insertDistributorChannels",type=DistributorSqlProvider.class)
	void insertDistributorChannels(@Param(value = "distributorId")Integer distributorId, @Param(value = "channelIds")Integer[] channelIds);

	/**
	 * 删除渠道对应频道关系
	 * @param id
	 */
	@Delete(value = "delete from distributor_channel where d_id = #{id}")
	void deleteDistributorChannels(@Param(value = "id")Integer id);
	/**
	 * 获取所以渠道信息列表
	 * @return
	 */
	@Select(value="select id,name,status,create_time as createTime,update_time as updateTime from distributor where status = 1")
	List<Distributor> getAllDistributorList();

	@Select(value="select d.id  from distributor d left join distributor_channel  dc on d.id=dc.d_id where d.status = 1 group by d.id")
	List<Integer> getDistributorIds();
}
