package com.oupeng.joke.dao.sqlprovider;

import com.oupeng.joke.domain.Ad;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayDeque;

public class AdSqlProvider {
	/**
	 * 获取广告列表记录总数
	 * @param ad		广告
	 * @return
	 */
	public String getAdListCount(Ad ad){
		StringBuffer sql = new StringBuffer();
		sql.append("select count(a.id) from ad a left join distributor d on a.d_id = d.id where 1=1 ");
		if(ad.getDid() != null){
			sql.append(" and a.d_id = ").append(ad.getDid());
		}
		if(ad.getPos() != null){
			sql.append(" and a.pos = ").append(ad.getPos());
		}
		if(ad.getStatus() != null){
			sql.append(" and a.status = ").append(ad.getStatus());
		}
		if(ad.getSlotId() != null){
			sql.append(" and a.slot_id = '").append(ad.getSlotId()).append("'");
		}
		return sql.toString();
	}
	/**
	 * 获取广告列表
	 * @param ad		广告
	 * @return
	 */
	public String getAdList(Ad ad){
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id,a.slot_id as slotId,a.pos,a.slide,d.name as dName,a.status,a.create_time as createTime,a.update_time as updateTime from ad a left join distributor d on a.d_id = d.id where 1=1 ");
		if(ad.getDid() != null){
			sql.append(" and a.d_id = ").append(ad.getDid());
		}
		if(ad.getPos() != null){
			sql.append(" and a.pos = ").append(ad.getPos());
		}
		if(ad.getStatus() != null){
			sql.append(" and a.status = ").append(ad.getStatus());
		}
		if(ad.getSlotId() != null){
			sql.append(" and a.slot_id = '").append(ad.getSlotId()).append("'");
		}
		sql.append(" order by a.create_time desc ");
		sql.append(" limit ").append(ad.getOffset()).append(",").append(ad.getPageSize());
		return sql.toString();
	}

	/**
	 * 新增广告
	 * @param ad
	 * @return
	 */
	public String insertAd(Ad ad){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into ad(slot_id,pos,slide,d_id,status,create_time,update_time) value (");
		if(ad.getSlotId() != null){
			sql.append("'").append(ad.getSlotId()).append("',");
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
		sql.append(" update ad set update_time=now() ");
		if(ad.getSlotId() != null){
			sql.append(",slot_id = '").append(ad.getSlotId()).append("'");
		}
		if(ad.getPos() != null){
			sql.append(",pos=").append(ad.getPos());
		}
		if(ad.getSlide() != null){
			sql.append(",slide=").append(ad.getSlide());
		}
		if(ad.getDid() != null){
			sql.append(",d_id=").append(ad.getDid());
		}
		sql.append(",status=").append(ad.getStatus());
		sql.append(" where id = ").append(ad.getId());
		return sql.toString();
	}
}
