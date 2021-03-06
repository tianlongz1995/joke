package com.oupeng.joke.dao.mapper;

import com.oupeng.joke.dao.sqlprovider.AdSqlProvider;
import com.oupeng.joke.domain.Ad;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AdMapper {
	/**
	 * 获取广告列表总数
	 * @param ad
	 * @return
	 */
	@SelectProvider(method="getAdListCount",type=AdSqlProvider.class)
	int getAdListCount(Ad ad);
	/**
	 * 获取广告列表
	 * @param ad        广告
	 * @return
	 */
	@SelectProvider(method="getAdList",type=AdSqlProvider.class)
	List<Ad> getAdList(Ad ad);

	/**
	 * 获取广告信息
	 * @param id 广告编号
	 * @return
	 */
	@Select(value="select a.id,a.slot_id as slotId,a.pos,slide,a.d_id as did,d.name as dName, a.status,a.create_time as createTime,a.update_time as updateTime from ad a left join distributor d on a.d_id = d.id where a.id = #{id}")
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

	@Select(value="select slot_id as slotId, pos,slide from ad where status = 1 and d_id = #{id}")
	List<Ad> getDistributorAdList(@Param(value = "id")Integer id);
	/**
	 * 获取广告数量
	 * @param did
	 * @param pos
	 * @return
	 */
	@Select(value="select count(1) from ad where pos = #{pos} and d_id = #{did}")
	Integer getAdCount(@Param(value = "did")Integer did, @Param(value = "pos")Integer pos);
}
