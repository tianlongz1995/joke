package com.oupeng.joke.dao.sqlprovider;

import com.oupeng.joke.domain.Dictionary;
import com.oupeng.joke.domain.Joke;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

public class JokeSqlProvider {

	/**
	 * 获取专题段子列表总数SQL
	 * @param map
	 * @return
	 */
	public static String getJokeListForTopicCount(Map<String,Object> map){
		Object type = map.get("type");
		Object status = map.get("status");
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(t1.id) from joke t1 where t1.verify_time >= CURDATE()  ");
		if(type != null){
			sql.append(" and t1.type = ").append(type).append(" ");
		}
		if(status != null){
			sql.append(" and t1.status = ").append(status).append(" ");
		}
		sql.append(" and not exists (select 1 from topic_joke t2 where t2.j_id = t1.id) ");
		return sql.toString();
	}
	/**
	 * 获取专题段子列表总数SQL
	 * @param map
	 * @return
	 */
	public static String getJokeListForTopic(Map<String,Object> map){
		Object type = map.get("type");
		Object status = map.get("status");
		Object offset = map.get("offset");
		Object pageSize = map.get("pageSize");
		StringBuffer sql = new StringBuffer();
		sql.append(" select t1.id,t1.title,t1.content,t1.img,t1.gif,t1.type,t1.status,t1.source_id as sourceId,");
		sql.append(" t1.verify_user as verifyUser,t1.verify_time as verifyTime,t1.create_time as createTime,");
		sql.append(" t1.update_time as updateTime,t1.good,t1.bad from joke t1 where 1 = 1 ");
		if(type != null){
			sql.append(" and t1.type = ").append(type).append(" ");
		}
		if(status != null){
			sql.append(" and t1.status = ").append(status).append(" ");
		}
		sql.append(" and t1.verify_time >= CURDATE() and not exists ( select 1 from topic_joke t2 where t2.j_id = t1.id) ");
		sql.append(" order by t1.create_time desc ");
		if(offset != null && pageSize != null){
			sql.append(" limit ").append(offset).append(", ").append(pageSize);
		}
		return sql.toString();
	}
	/**
	 * 获取数据源列表SQL
	 * @param map
	 * @return
	 */
	public static String getJokeList(Map<String,Object> map){
		Object type = map.get("type");
		Object status = map.get("status");
		Object id = map.get("id");
		Object content = map.get("content");
		boolean isTopic = Boolean.parseBoolean(map.get("isTopic").toString());
		StringBuffer sql = new StringBuffer();
		sql.append(" select t1.id,t1.title,t1.content,t1.img,t1.gif,t1.type,t1.status,t1.source_id as sourceId,");
		sql.append(" t1.verify_user as verifyUser,t1.verify_time as verifyTime,t1.create_time as createTime,");
		sql.append(" t1.update_time as updateTime,t1.good,t1.bad,t1.weight from joke t1 where 1 = 1  ");
		if(type != null){
			sql.append(" and t1.type = ").append(type).append(" ");
		}
		if(status != null){
			sql.append(" and t1.status = ").append(status).append(" ");
		}
		if(id != null){
			sql.append(" and t1.id = ").append(id).append(" ");
		}
		if(content != null && !"".equals(content)){
			sql.append(" and ( t1.content like '%").append(content).append("%' or t1.title like '%").append(content).append("%') ");
		}
		if(isTopic){
			sql.append(" and t1.verify_time >= CURDATE() and not exists ( select 1 from topic_joke t2 where t2.j_id = t1.id)");
		}
//		t1.create_time desc ,
		sql.append(" order by t1.weight desc limit 12 ");
		return sql.toString();
	}

	/**
	 * 更新段子状态
	 * @param map
	 * @return
	 */
	public static String updateJokeStatus(Map<String,Object> map){
		Integer status = Integer.valueOf(map.get("status").toString());
		String ids = map.get("ids").toString();
		Object user = map.get("user");
		StringBuffer sql = new StringBuffer();
		sql.append(" update joke set update_time =now(), ");
		sql.append(" status= ").append(status);
		sql.append(" ,audit = ").append(status);
		if(user != null && !"".equals(user)){
			sql.append(",verify_time = now(), ").append("verify_user= '").append(user).append("' ");
		}
		sql.append(" where id in (").append(ids).append(")");
		return sql.toString();
	}

	/**
	 * 更新段子信息SQL
	 * @param joke
	 * @return
	 */
	public static String updateJoke(Joke joke){
		StringBuffer sql = new StringBuffer();
		sql.append(" update joke set update_time = now(), verify_time = now(), ");
        if (joke.getStatus() != null) {
            sql.append(" status = ").append(joke.getStatus()).append(", ");
        }else{
            sql.append(" status = 1, ");
        }
        if (joke.getAudit() != null) {
            sql.append(" audit = ").append(joke.getAudit()).append(", ");
        }else{
            sql.append(" audit = 6, ");
        }
        sql.append(" verify_user = '").append(joke.getVerifyUser()).append("',");
        if(StringUtils.isNotBlank(joke.getTitle())){
			sql.append(" title = '").append(joke.getTitle().trim()).append("',");
		}else{
			sql.append(" title = null,");
		}
		if(StringUtils.isNotBlank(joke.getContent())){
			sql.append(" content = '").append(joke.getContent().trim()).append("', ");
		}else{
			sql.append(" content = null, ");
		}
		if (joke.getWeight() != null) {
			sql.append(" weight = ").append(joke.getWeight());
		}else{
			sql.append(" weight = 0");
		}
		sql.append(" where id = ").append(joke.getId());
		return sql.toString();
	}
	
	public static String getJokeCountForChannel(String contentType){
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) from joke where status = 1 ");
		if(StringUtils.isNotBlank(contentType)){
			sql.append(" and type in (").append(contentType).append(") ");
		}
		return sql.toString();
	}

	/**
	 * 获取频道数据源列表SQL
	 * @param map
	 * @return
	 */
	public static String getJokeListForChannel(Map<String,Object> map){
		Object contentType = map.get("contentType");
		Integer start = Integer.valueOf(map.get("start").toString());
		Integer size = Integer.valueOf(map.get("size").toString());
		StringBuffer sql = new StringBuffer();
		sql.append(" select id,title,content,img,gif,type,verify_time as verifyTime from joke where status = 1 ");
		if(contentType != null && !"".equals(contentType)){
			sql.append(" and type in (").append(contentType).append(") ");
		}
		sql.append(" order by verify_time desc limit ").append(start).append(",").append(size);
		return sql.toString();
	}

	/**
	 * 获取待发布的段子列表SQL
	 * @param map
	 * @return
	 */
	public static String getJokeListForPublish(Map<String,Object> map){
		Object lastUpdateTime = map.get("lut");
		Object currentUpdateTime = map.get("cut");
		StringBuffer sql = new StringBuffer();
		sql.append(" select  id,title,content,img,gif,type,status,source_id as sourceId,verify_user as verifyUser,verify_time as verifyTime, ");
		sql.append(" create_time as createTime,update_time as updateTime,good,bad,width,height,comment_number as commentNumber, comment as commentContent, avata, nick, src from joke where `status` not in (0,2) ");
		if(lastUpdateTime != null){
			sql.append(" and update_time > '").append(lastUpdateTime).append("' ");
		}
		
		if(currentUpdateTime != null){
			sql.append(" and update_time <= '").append(currentUpdateTime).append("' ");
		}
		return sql.toString();
	}

	/**
	 * 存储段子
	 * @param joke
	 * @return
	 */
	public static String insertJoke(Joke joke){
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into joke(title, content, img, gif, width, height, type, uuid, verify_user, status,create_time, update_time, verify_time, good) value(");
		if(StringUtils.isNotBlank(joke.getTitle())){
			sql.append("'").append(joke.getTitle().trim()).append("', ");
		}else{
			sql.append("null,");
		}
		if(StringUtils.isNotBlank(joke.getContent())){
			sql.append("'").append(joke.getContent().trim()).append("', ");
		}else{
			sql.append(" null, ");
		}
		if(StringUtils.isNotBlank(joke.getImg())){
			sql.append(" '").append(joke.getImg()).append("', ");
		}else{
			sql.append(" null,");
		}
		if(StringUtils.isNotBlank(joke.getGif())){
			sql.append(" '").append(joke.getGif()).append("', ");
		}else{
			sql.append(" null,");
		}
		if(joke.getWidth() != null){
			sql.append(joke.getWidth()).append(", ");
		}else{
			sql.append(" 0, ");
		}
		if(joke.getHeight() != null){
			sql.append(joke.getHeight()).append(", ");
		}else{
			sql.append(" 0, ");
		}
		sql.append(joke.getType()).append(",'");
		sql.append(joke.getUuid()).append("','");
		sql.append(joke.getVerifyUser()).append("', 1, now(), now(), now(),30+FLOOR(RAND()*70) )");
		return sql.toString();
	}

    /**
     * 保存置顶段子SQL
     * @param map
     * @return
     */
    public static String insertJokeTop(Map<String,Object> map){
        StringBuffer sql = new StringBuffer();
        sql.append("insert into joke_top(jid, sort, status, create_time) values");
        String ids = map.get("ids").toString();
        String[] idArray = ids.split(",");
        if(idArray != null && idArray.length > 0){
            for(String id : idArray){
                if(id != null && id.length() > 0){
                    sql.append("('").append(id).append("', 0, 0, now()),");
                }
            }
        }
        return sql.substring(0, sql.length() - 1);
    }
	/**
	 * 添加权重字典
	 * @param dict
	 * @return
	 */
	public static String addDictionary(Dictionary dict){
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into dictionary(`code`, parent_code, `type`, `value`, `describe`, `seq`, create_time, update_time) value(");
		if(StringUtils.isNotBlank(dict.getCode())){
			sql.append("'").append(dict.getCode().trim()).append("', ");
		}else{
			sql.append("null,");
		}
		if(StringUtils.isNotBlank(dict.getParentCode())){
			sql.append("'").append(dict.getParentCode().trim()).append("', ");
		}else{
			sql.append(" null, ");
		}
		if(StringUtils.isNotBlank(dict.getParentCode())){
			sql.append(" '").append(dict.getParentCode()).append("', ");
		}else{
			sql.append(" null,");
		}
		if(StringUtils.isNotBlank(dict.getValue())){
			sql.append(" '").append(dict.getValue()).append("', ");
		}else{
			sql.append(" null,");
		}
		if(StringUtils.isNotBlank(dict.getDescribe())){
			sql.append(" '").append(dict.getDescribe()).append("', ");
		}else{
			sql.append(" null,");
		}
		if(dict.getSeq() != null){
			sql.append(dict.getSeq()).append(", ");
		}else{
			sql.append(" 0, ");
		}
		sql.append(" now(), now() )");
		return sql.toString();
	}

	/**
	 * 修改权重字典
	 * @param dict
	 * @return
	 */
	public static String weightEdit(Dictionary dict){
		StringBuffer sql = new StringBuffer();
		sql.append(" update dictionary set update_time =now(), ");
		if(StringUtils.isNotBlank(dict.getCode())){
			sql.append(" `code`='").append(dict.getCode().trim()).append("',");
		}else{
			sql.append(" `code`=null,");
		}
		if(StringUtils.isNotBlank(dict.getParentCode())){
			sql.append(" parent_code='").append(dict.getParentCode()).append("',");
		}else{
			sql.append(" parent_code=null,");
		}
		if(StringUtils.isNotBlank(dict.getValue())){
			sql.append(" `value`='").append(dict.getValue()).append("',");
		}else{
			sql.append(" `value`=null,");
		}
		if(StringUtils.isNotBlank(dict.getDescribe())){
			sql.append(" `describe`='").append(dict.getDescribe().trim()).append("'");
		}else{
			sql.append(" `describe`=null ");
		}
		sql.append(" where id =").append(dict.getId());
		return sql.toString();
	}

	/**
	 * 获取待审核的段子列表SQL
	 * @param map
	 * @return
	 */
	public static String getJokeListForVerify(Map<String,Object> map){
		Integer type = (Integer) map.get("type");
		Integer status = (Integer) map.get("status");
		Integer source = (Integer) map.get("source");
		String startDay = (String) map.get("startDay");
		String endDay = (String) map.get("endDay");
		Integer offset = (Integer) map.get("offset");
		Integer pageSize = (Integer) map.get("pageSize");
		StringBuffer sql = new StringBuffer();
		sql.append(" select t1.id,t1.title,t1.content,t1.img,t1.gif,t1.type,t1.status,t2.name as sourceName,");
		sql.append(" t1.verify_user as verifyUser,t1.verify_time as verifyTime,t1.create_time as createTime,");
		sql.append(" t1.update_time as updateTime,t1.weight,t1.audit from joke t1 left join `source` t2 on t1.source_id = t2.`id` where 1 = 1 ");
		if(type != null){
			sql.append(" and t1.type = ").append(type).append(" ");
		}
		if(status != null){
			if(status.equals(3)){
				sql.append(" and t1.status in (3,4) ");
            }else if(status.equals(1)){
                sql.append(" and t1.status in (1,6) ");
			}else{
				sql.append(" and t1.status = ").append(status).append(" ");
			}
		}
		if(source != null){
			sql.append(" and t1.source_id = ").append(source).append(" ");
		}
		if(StringUtils.isNotBlank(startDay)){
			sql.append(" and t1.`create_time` >= str_to_date('").append(startDay).append("', '%Y-%m-%d %H:%i:%s') ");
		}
		if(StringUtils.isNotBlank(endDay)){
			sql.append(" and t1.`create_time` <= str_to_date('").append(endDay).append("', '%Y-%m-%d %H:%i:%s') ");
		}
//		t1.create_time desc
		sql.append(" order by  t1.weight desc limit ");
		sql.append(offset).append(" , ").append(pageSize);
		return sql.toString();
	}

	/**
	 * 获取待审核的段子列表记录总数SQL
	 * @param map
	 * @return
	 */
    public static String getJokeListForVerifyCount(Map<String,Object> map){
		Integer type = (Integer) map.get("type");
		Integer status = (Integer) map.get("status");
		Integer source = (Integer) map.get("source");
		String startDay = (String) map.get("startDay");
		String endDay = (String) map.get("endDay");

		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) from joke t1 left join `source` t2 on t1.source_id = t2.`id` where 1 = 1 ");
		if(type != null){
			sql.append(" and t1.type = ").append(type).append(" ");
		}
		if(status != null){
            if(status.equals(3)) {
                sql.append(" and t1.status in (3,4) ");
            }else if(status.equals(1)){
                sql.append(" and t1.status in (1,6) ");
            }else{
                sql.append(" and t1.status = ").append(status).append(" ");
            }
		}
		if(source != null){
			sql.append(" and t1.source_id = ").append(source).append(" ");
		}
		if(StringUtils.isNotBlank(startDay)){
			sql.append(" and t1.`create_time` >= str_to_date('").append(startDay).append("', '%Y-%m-%d %H:%i:%s') ");
		}
		if(StringUtils.isNotBlank(endDay)){
			sql.append(" and t1.`create_time` <= str_to_date('").append(endDay).append("', '%Y-%m-%d %H:%i:%s') ");
		}
		return sql.toString();
	}

	/**
	 * 获取段子列表
	 * @param map
	 * @return
	 */
	public static String getPublishJokeListByType(Map<String,Object> map){
		Object type = map.get("type");
		Object count = map.get("count");
		StringBuffer sql = new StringBuffer();
		sql.append(" select id, update_time as updateTime from joke t1 where type = ").append(type);
		sql.append(" and status = 3 order by update_time desc ");
		if(count != null){
			sql.append(" limit ").append(count);
		}
		return sql.toString();
	}

	/**
	 * 查询推荐频道下数据内容
	 * @param map
	 * @return
	 */
	public static String getJokeListForPublishRecommend(Map<String,Object> map){
		Object type = map.get("type");
		Object num = map.get("num");
		StringBuffer sql = new StringBuffer();
		sql.append("select id FROM joke t where `status` = 3 and `type` =  ").append(type);
		sql.append(" and DATE_FORMAT(update_time,'%Y-%m-%d') = date_sub(curdate(),interval 1 day) ");
		sql.append(" and not EXISTS ( select 1 from topic_joke where j_id = t.id) ORDER BY good desc ");
		if(num != null){
			sql.append(" limit ").append(num);
		}
		return sql.toString();
	}

	/**
	 * 查询最近审核通过的数据
	 * @param map
	 * @return
	 */
	public static String getJokeForPublishChannel(Map<String,Object> map){
		Object contentType = map.get("contentType");
		Object size        = map.get("size");
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.id,t.verify_time as verifyTime from joke t where t.`status` = 1 and ");
		sql.append(" t.type in ( ").append(contentType).append(" )");
		sql.append(" and not EXISTS ( select 1 from topic_joke where j_id = t.id) order by t.verify_time desc ");
		if(size != null) {
			sql.append(" limit ").append(size);
		}
		return sql.toString();
	}

	/**
	 * 添加发布规则
	 * @param map
	 * @return
	 */
	public static String addPublishRole(Map<String,Object> map){
		Object code = map.get("code");
		Object value = map.get("value");
		StringBuffer sql = new StringBuffer();
		sql.append(" update dictionary set update_time = now(),");
		if(null != value){
			sql.append("  value= '").append(value).append("' ");
		}else{
			sql.append(" value = null,");
		}
		sql.append("where code = ").append(code);
		return sql.toString();
	}

    /**
     * 获取首页置顶段子总数SQL
     * @param map
     * @return
     */
    public static String getJokeTopListCount(Map<String,Object> map){
        Object type = map.get("type");
        Object status = map.get("status");
        Object source = map.get("source");
        Object startDay = map.get("startDay");
        Object endDay = map.get("endDay");
        StringBuffer sql = new StringBuffer();
        sql.append("select count(j.id) from joke j right join joke_top jt on j.id = jt.jid left join source s on j.source_id = s.id where 1 = 1 ");
        if(startDay != null && endDay != null){
            sql.append(" and jt.release_time >= '").append(startDay).append("' and jt.release_time <= '").append(endDay).append("' ");
        }
        if(type != null){
            sql.append("  and j.type = ").append(type).append(" ");
        }
        if(status != null){
            sql.append(" and jt.status = ").append(status).append(" ");
        }
        if(source != null){
            sql.append(" and s.id = ").append(source).append(" ");
        }
        return sql.toString();
    }

    /**
     * 获取首页置顶段子列表SQL
     * @param map
     * @return
     */
    public static String getJokeTopList(Map<String,Object> map){
        Object type = map.get("type");
        Object status = map.get("status");
        Object source = map.get("source");
        Object startDay = map.get("startDay");
        Object endDay = map.get("endDay");
        Object offset = map.get("offset");
        Object pageSize = map.get("pageSize");
        StringBuffer sql = new StringBuffer();
        sql.append("select j.id, j.title, j.content, j.img, j.gif, j.type, s.name as sourceName, j.create_time as createTime, jt.sort, jt.release_time as releaseTime,  jt.status from joke j right join joke_top jt on j.id = jt.jid left join source s on j.source_id = s.id where 1 = 1 ");
        if(startDay != null && endDay != null){
            sql.append(" and jt.release_time >= '").append(startDay).append("' and jt.release_time <= '").append(endDay).append("' ");
        }
        if(type != null){
            sql.append("  and j.type = ").append(type).append(" ");
        }
        if(status != null){
            sql.append(" and jt.status = ").append(status).append(" ");
        }
        if(source != null){
            sql.append(" and s.id = ").append(source).append(" ");
        }
        sql.append(" order by ");
        if(status != null && status.equals(1)){ // 已处理 - 审核通过 - 待发布
            sql.append("  jt.release_time asc ");
        } else {
            sql.append(" jt.sort desc ");
        }
        sql.append(" limit ").append(offset).append(", ").append(pageSize);

        return sql.toString();
    }

    /**
     * 新增段子SQL
     * @param joke
     * @return
     */
    public static String addJoke(Joke joke){
        StringBuffer sql = new StringBuffer();
        sql.append("insert into joke(title, type, img, gif, good, bad, uuid, release_avata, release_nick, content, weight, width, height, create_by, create_time, verify_user, verify_time, source_id, audit, status) value(");
        if(StringUtils.isNotBlank(joke.getTitle())){
            sql.append("'").append(joke.getTitle()).append("', ");
        } else {
            sql.append("null, ");
        }
        if(joke.getType() != null){
            sql.append(joke.getType()).append(", ");
        } else {
            sql.append("0, ");
        }
        if(StringUtils.isNotBlank(joke.getImg())){
            sql.append("'").append(joke.getImg()).append("', ");
        } else {
            sql.append("null, ");
        }
        if(StringUtils.isNotBlank(joke.getGif())){
            sql.append("'").append(joke.getGif()).append("', ");
        } else {
            sql.append("null, ");
        }
        if(joke.getGood() != null){
            sql.append(joke.getGood()).append(", ");
        } else {
            sql.append("15, ");
        }
        if(joke.getBad() != null){
            sql.append(joke.getBad()).append(", ");
        } else {
            sql.append("0, ");
        }
        if(StringUtils.isNotBlank(joke.getUuid())){
            sql.append("'").append(joke.getUuid()).append("', ");
        } else {
            sql.append("null, ");
        }
        if(StringUtils.isNotBlank(joke.getRa())){
            sql.append("'").append(joke.getRa()).append("', ");
        } else {
            sql.append("null, ");
        }
        if(StringUtils.isNotBlank(joke.getRn())){
            sql.append("'").append(joke.getRn()).append("', ");
        } else {
            sql.append("null, ");
        }
        if(StringUtils.isNotBlank(joke.getContent())){
            sql.append("'").append(joke.getContent()).append("', ");
        } else {
            sql.append("'', ");
        }
        if(joke.getWeight() != null){
            sql.append(joke.getWeight()).append(", ");
        } else {
            sql.append("0, ");
        }
        if(joke.getWidth() != null){
            sql.append(joke.getWidth()).append(", ");
        } else {
            sql.append("null, ");
        }
        if(joke.getHeight() != null){
            sql.append(joke.getHeight()).append(", ");
        } else {
            sql.append("null, ");
        }
        if(StringUtils.isNotBlank(joke.getCreateBy())){
            sql.append("'").append(joke.getCreateBy()).append("', ");
        } else {
            sql.append("null, ");
        }
        sql.append(" now(), ");
        if(StringUtils.isNotBlank(joke.getVerifyUser())){
            sql.append("'").append(joke.getVerifyUser()).append("', ");
        } else {
            sql.append("null, ");
        }
        sql.append(" now(), ");
        if(joke.getSourceId() != null){
            sql.append(joke.getSourceId()).append(", ");
        } else {
            sql.append("1, ");
        }
        if(joke.getAudit() != null){
            sql.append(joke.getAudit()).append(", ");
        } else {
            sql.append("6, ");
        }
        if(joke.getStatus() != null){
            sql.append(joke.getStatus()).append(")");
        } else {
            sql.append("1)");
        }
        return sql.toString();
    }


}
