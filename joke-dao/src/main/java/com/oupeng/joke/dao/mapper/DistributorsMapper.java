package com.oupeng.joke.dao.mapper;

import com.oupeng.joke.dao.sqlprovider.DistributorsSqlProvider;
import com.oupeng.joke.domain.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 渠道数据库接口
 */
public interface DistributorsMapper {
	/**
	 * 获取渠道总数
	 * @param distributor
	 * @return
	 */
	@SelectProvider(method="getCount",type=DistributorsSqlProvider.class)
	int getCount(Distributor distributor);

	/**
	 * 获取渠道列表
	 * @param distributor
	 * @return
	 */
	@SelectProvider(method="getList",type= DistributorsSqlProvider.class)
	List<Distributor> getList(Distributor distributor);

	/**
	 * 新增渠道
	 * @param distributors
	 * @return
	 */
	@Insert("insert into distributors(id, name, status, create_time, create_by) value(#{id}, #{name}, #{status}, now(), #{createBy})")
//	@SelectKey(statement="SELECT LAST_INSERT_ID() as id", keyProperty="id", before=false, resultType=Integer.class)
	void add(Distributor distributors);

	/**
	 * 获取渠道
	 * @param id
	 * @return
	 */
	@Select("select id, name, status, create_time as createTime, update_time as updateTime, create_by as createBy, update_by as updateBy from distributors where id = #{id}")
	Distributor getDistributors(Integer id);

	/**
	 * 获取渠道关联频道列表
	 * @param id
	 * @return
	 */
	@Select("select id, name, sort, status from (select c.id, c.name, ifnull(dc.sort,'99') as sort, case dc.c_id when c.id then 1 else 0 end as status from channels c left join (select id, c_id, sort from distributors_channels where d_id = #{id}) dc on c.id = dc.c_id) t order by sort asc")
	List<Channel> getChannelSelected(Integer id);

	/**
	 * 获取频道列表
	 * @return
	 */
	@Select("select id, name from channels where status = 1")
	List<Channel> getChannels();

	/**
	 * 修改渠道
	 * @param distributors
	 */
	@UpdateProvider(method="edit",type=DistributorsSqlProvider.class)
	int edit(Distributor distributors);

	/**
	 * 删除渠道频道关联
	 * @param id
	 * @return
	 */
	@Delete("delete from distributors_channels where d_id = #{id}")
	int deleteChannels(Integer id);

	/**
	 * 添加渠道频道关联
	 * @param did
	 * @param cid
	 * @param sort
	 * @return
	 */
	@Insert("insert into distributors_channels(d_id, c_id, sort) value(#{did}, #{cid}, #{sort})")
	int addChannels(@Param("did") int did, @Param("cid") int cid, @Param("sort") int sort);

	/**
	 * 保存广告信息
	 * @param ad
	 * @return
	 */
	@InsertProvider(method="addAd",type=DistributorsSqlProvider.class)
    int addAd(Ads ad);

	/**
	 * 修改广告信息
	 * @param ad
	 */
	@UpdateProvider(method="editAd",type=DistributorsSqlProvider.class)
	int editAd(Ads ad);

	/**
	 * 获取渠道广告配置
	 * @param id
	 * @return
	 */
	@Select("select id, did, s, lc, lb, dt, dc, db, di, dr from ads where did = #{id}")
	Ads getAds(Integer id);

	/**
	 * 修改上下线状态
	 * @param id
	 * @param status
	 * @param userName
	 * @return
	 */
	@Update("update distributors set status = #{status}, update_time=now(), update_by = #{userName} where id = #{id} ")
	int editStatus(@Param("id")Integer id, @Param("status")Integer status, @Param("userName")String userName);

	/**
	 * 删除渠道
	 * @param id
	 * @param username
	 * @return
	 */
	@Update("update distributors set status = 2, update_time = now(), update_by = #{username} where id = #{id} ")
	int del(@Param("id")Integer id, @Param("username")String username);

	/**
	 * 获取渠道下频道已配置频道列表
	 * @return
	 */
	@Select("select c.id as i, c.name as n, dc.sort as s, c.banner as b from channels c left join distributors_channels dc on c.id = dc.c_id where dc.d_id = #{id} order by dc.sort asc")
	List<Channels> getDistributorChannels(Integer id);


	/**
	 * 更新banner 显示不显示
	 * @param bannerStatus
	 */
	@Update(value = "update channels set banner = #{bannerStatus} where id = #{id}")
	void updateChannelsBanner(@Param(value = "bannerStatus") Integer bannerStatus,
							  @Param(value = "id") Integer id);


	/**
	 * 获取渠道ID和name
	 * @return
     */
	@Select(value = "select id,name from distributors")
	List<Distributor> getDistributorIdAndName();

    /**
     * 获取渠道数量
     * @param id
     * @return
     */
    @Select(value = "select count(id) from distributors where id = #{id}")
    int getDistributorsCount(@Param("id")Integer id);

    /**
     * 查询渠道横幅列表
     * @param did
     * @return
     */
    @Select("select b.id, b.title, b.cid, db.sort, db.id as dbId from banner b left join distributors_banner db on b.id = db.b_id where db.d_id = #{did} and db.cid = #{cid} and b.status = 3 order by db.sort asc")
    List<Banner> getDistributorsBanners(@Param("did")Integer did, @Param("cid")Integer cid);
}
