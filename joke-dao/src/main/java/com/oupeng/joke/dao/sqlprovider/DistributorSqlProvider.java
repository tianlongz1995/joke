package com.oupeng.joke.dao.sqlprovider;

import com.oupeng.joke.domain.Distributor;
import org.apache.commons.lang3.StringUtils;

public class DistributorSqlProvider {

	/**
	 * 获取渠道列表
	 * @param status
	 * @return
	 */
	public static String getDistributorList(Integer status){
		StringBuffer sql = new StringBuffer();
		sql.append(" select id,name,status,create_time as createTime,update_time as updateTime from distributor where 1 = 1 ");
		if(status != null){
			sql.append(" and status = ").append(status);
		}
		return sql.toString();
	}

	/**
	 * 新增渠道
	 * @param distributor
	 * @return
	 */
	public static String insertDistributor(Distributor distributor){
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into distributor(name,status,create_time,update_time) value (");
		if(StringUtils.isNotBlank(distributor.getName())){
			sql.append("'").append(distributor.getName()).append("',");
		}else{
			sql.append("null,");
		}
		sql.append(distributor.getStatus()).append(",now(),now())");
		return sql.toString();
	}

	/**
	 * 更新渠道信息
	 * @param distributor
	 * @return
	 */
	public String updateDistributor(Distributor distributor){
		StringBuffer sql = new StringBuffer();
		sql.append(" update distributor set update_time=now(),name = ");
		if(StringUtils.isNotBlank(distributor.getName())){
			sql.append("'").append(distributor.getName()).append("',");
		}else{
			sql.append("null,");
		}
		sql.append("status=").append(distributor.getStatus());
		sql.append(" where id = ").append(distributor.getId());
		return sql.toString();
	}
}
