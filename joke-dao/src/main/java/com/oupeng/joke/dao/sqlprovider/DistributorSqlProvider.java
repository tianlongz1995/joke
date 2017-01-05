package com.oupeng.joke.dao.sqlprovider;

import com.oupeng.joke.domain.Distributor;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

public class DistributorSqlProvider {
    /**
     * 获取渠道记录数
     *
     * @param distributor
     * @return
     */
    public static String getDistributorListCount(Distributor distributor) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(1) from distributor where 1 = 1 ");
        if (distributor.getStatus() != null) {
            sql.append(" and status = ").append(distributor.getStatus());
        }
        return sql.toString();
    }

    /**
     * 获取渠道列表
     *
     * @param distributor
     * @return
     */
    public static String getDistributorList(Distributor distributor) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select id,name,status,create_time as createTime,update_time as updateTime from distributor where 1 = 1 ");
        if (distributor.getStatus() != null) {
            sql.append(" and status = ").append(distributor.getStatus());
        }
        sql.append(" order by create_time desc ");
        sql.append(" limit ").append(distributor.getOffset()).append(",").append(distributor.getPageSize());
        return sql.toString();
    }

    /**
     * 新增渠道
     *
     * @param distributor
     * @return
     */
    public static String insertDistributor(Distributor distributor) {
        StringBuffer sql = new StringBuffer();
        sql.append(" insert into distributor(name,status,create_time,update_time) value (");
        if (StringUtils.isNotBlank(distributor.getName())) {
            sql.append("'").append(distributor.getName()).append("',");
        } else {
            sql.append("null,");
        }
        sql.append(distributor.getStatus()).append(",now(),now())");
        return sql.toString();
    }

    /**
     * 获取渠道总数SQL
     *
     * @param distributor
     * @return
     */
    public static String getCount(Distributor distributor) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(1) from distributors where 1 = 1 ");
        if (distributor.getStatus() != null) {
            sql.append(" and status = ").append(distributor.getStatus());
        }
        return sql.toString();
    }

    /**
     * 获取渠道列表
     * @param distributor
     * @return
     */
    public static String getList(Distributor distributor) {
        StringBuffer sql = new StringBuffer();
        sql.append("select id, name, status, create_time as createTime, update_time as updateTime, create_by as createBy, update_by as updateBy from distributors where 1 = 1 ");
        if (distributor.getStatus() != null) {
            sql.append(" and status = ").append(distributor.getStatus());
        }
        sql.append(" order by create_time desc ");
        sql.append(" limit ").append(distributor.getOffset()).append(",").append(distributor.getPageSize());
        return sql.toString();
    }

    /** -----------------------------------JOKE 2.0-------------------------------------------**/

    /**
     * 更新渠道信息
     *
     * @param distributor
     * @return
     */
    public String updateDistributor(Distributor distributor) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update distributor set update_time=now(),name = ");
        if (StringUtils.isNotBlank(distributor.getName())) {
            sql.append("'").append(distributor.getName()).append("',");
        } else {
            sql.append("null,");
        }
        sql.append("status=").append(distributor.getStatus());
        sql.append(" where id = ").append(distributor.getId());
        return sql.toString();
    }

    /**
     * 存储渠道对应频道列表
     *
     * @param map
     * @return
     */
    public String insertDistributorChannels(Map<String, Object> map) {
        Integer[] ids = (Integer[]) map.get("channelIds");
        Object distributorId = map.get("distributorId");
        StringBuffer sql = new StringBuffer();
        sql.append("insert into distributor_channel(d_id,c_id,sort) values ");
        int index = 0;
        if (distributorId != null && ids != null) {
            for (Integer id : ids) {
                sql.append("(").append(distributorId).append(",");
                sql.append(id).append(",");
                sql.append(index + 1).append("),");
                index++;
            }
        }
        return sql.substring(0, sql.length() - 1);
    }
}
