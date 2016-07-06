package com.oupeng.joke.dao.sqlprovider;

import com.oupeng.joke.domain.Ad;
import org.apache.commons.lang3.StringUtils;

public class AdSqlProvider {

	/**
	 * 获取广告列表
	 * @param distributorId 渠道编号
	 * @return
	 */
	public static String getAdList(Integer distributorId){
		StringBuffer sql = new StringBuffer();
		sql.append(" select id,slot_id as slotId,pos,slide,d_id as did,status,create_time as createTime,update_time as updateTime from ad where 1 = 1 ");
		if(distributorId != null){
			sql.append(" and d_id = ").append(distributorId);
		}
		return sql.toString();
	}

	/**
	 * 新增广告
	 * @param ad
	 * @return
	 */
	public static String insertAd(Ad ad){
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into ad(slot_id,pos,slide,d_id,status,create_time,update_time) value (");
		if(ad.getSlotId() != null){
			sql.append(ad.getSlotId()).append(",");
		}else{
			sql.append("0,");
		}
		if(ad.getPos() != null){
			sql.append(ad.getPos()).append(",");
		}else{
			sql.append("0,");
		}
		if(ad.getSlide() != null){
			sql.append(ad.getSlide()).append(",");
		}else{
			sql.append("0,");
		}
		if(ad.getDid() != null){
			sql.append(ad.getDid()).append(",");
		}else{
			sql.append("NULL,");
		}
		if(ad.getStatus() != null){
			sql.append(ad.getStatus()).append(",");
		}else{
			sql.append("0,");
		}
		sql.append(" now(),now())");
		return sql.toString();
	}

	/**
	 * 更新广告信息
	 * @param ad
	 * @return
	 */
	public String updateAd(Ad ad){
		StringBuffer sql = new StringBuffer();
		sql.append(" update ad set update_time=now(),slot_id = ");
		if(ad.getSlotId() != null){
			sql.append(ad.getSlotId()).append("',");
		}
		if(ad.getPos() != null){
			sql.append(",pos=").append(ad.getPos()).append(",");
		}
		if(ad.getSlide() != null){
			sql.append(",slide=").append(ad.getSlide()).append(",");
		}
		if(ad.getDid() != null){
			sql.append(",d_id=").append(ad.getDid()).append(",");
		}
		sql.append("status=").append(ad.getStatus());
		sql.append(" where id = ").append(ad.getId());
		return sql.toString();
	}
}
