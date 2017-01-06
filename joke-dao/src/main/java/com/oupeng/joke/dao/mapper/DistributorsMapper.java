package com.oupeng.joke.dao.mapper;

import com.oupeng.joke.dao.sqlprovider.DistributorsSqlProvider;
import com.oupeng.joke.domain.Channel;
import com.oupeng.joke.domain.Distributor;
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
	@Insert("insert into distributors(name, status, create_time, create_by) value(#{name}, #{status}, now(), #{createBy})")
	@SelectKey(statement="SELECT LAST_INSERT_ID() as id", keyProperty="id", before=false, resultType=Integer.class)
	void add(Distributor distributors);

	/**
	 * 获取渠道
	 * @param id
	 * @return
	 */
	@Select("select id, name, status, create_time as createTime, update_time as updateTime, create_by as createBy, update_by as updateBy from distributors where id = #{id}")
	Distributor getDistributors(Integer id);

	/**
	 * 获取频道列表
	 * @param id
	 * @return
	 */
	@Select("select id, name, sort, status from (select c.id, c.name, ifnull(dc.sort,'99') as sort, case dc.c_id when c.id then 1 else 0 end as status from channels c left join (select id, c_id, sort from distributors_channels where d_id = #{id}) dc on c.id = dc.c_id) t order by sort asc")
	List<Channel> getChannels(Integer id);

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
}
