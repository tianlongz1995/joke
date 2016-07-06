package com.oupeng.joke.dao.mapper;

import com.oupeng.joke.dao.sqlprovider.AdSqlProvider;
import com.oupeng.joke.domain.Ad;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AdMapper {

	/**
	 * 获取广告列表
	 * @param distributorId 渠道编号
	 * @return
	 */
	@SelectProvider(method="getAdList",type=AdSqlProvider.class)
	List<Ad> getAdList(Integer distributorId);

	/**
	 * 获取广告信息
	 * @param id 广告编号
	 * @return
	 */
	@Select(value="select id,slot_id as slotId,pos,slide,d_id as did,status,create_time as createTime,update_time as updateTime from ad where id = ${id}")
	Ad getAdById(@Param(value = "id") Integer id);

	/**
	 * 修改状态
	 * @param id
	 * @param status
	 */
	@Select(value="update ad set update_time=now(),status = #{status} where id = ${id}")
	void updateAdStatus(@Param(value = "id") Integer id, @Param(value = "status") Integer status);


	/**
	 * 新增广告
	 * @param ad
	 */
	@InsertProvider(method="insertAd",type=AdSqlProvider.class)
	void insertAd(Ad ad);

	/**
	 * 更新广告信息
	 * @param ad
	 */
	@UpdateProvider(method="updateAd",type=AdSqlProvider.class)
	void updateAd(Ad ad);
}
