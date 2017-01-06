package com.oupeng.joke.dao.sqlprovider;

import com.oupeng.joke.domain.Distributor;
import org.apache.commons.lang3.StringUtils;

public class DistributorsSqlProvider {

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

    /**
     * 修改渠道
     * @param distributor
     * @return
     */
    public static String edit(Distributor distributor) {
        StringBuffer sql = new StringBuffer();
        sql.append("update distributors set update_time=now(), ");
        if (StringUtils.isNotBlank(distributor.getName())) {
            sql.append(" name = ").append("'").append(distributor.getName()).append("',");
        }
        if (StringUtils.isNotBlank(distributor.getUpdateBy())) {
            sql.append(" update_by = ").append("'").append(distributor.getUpdateBy()).append("',");
        }
        sql.append(" status = ").append(distributor.getStatus());
        sql.append(" where id = ").append(distributor.getId());
        return sql.toString();
    }
}
