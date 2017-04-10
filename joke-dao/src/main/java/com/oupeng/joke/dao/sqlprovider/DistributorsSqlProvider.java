package com.oupeng.joke.dao.sqlprovider;

import com.oupeng.joke.domain.Ads;
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
     *
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
     *
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

    /**
     * 存储广告
     *
     * @param ads
     * @return
     */
    public static String addAd(Ads ads) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ads(did, create_time, create_by, s, lc, lb, dt, dc , db, di, dm, dms) value(");
        sql.append(ads.getDid()).append(", now(),");
        if (ads.getCreateBy() != null) {
            sql.append(" '").append(ads.getCreateBy()).append("', ");
        } else {
            sql.append(" NULL, ");
        }
        if (ads.getS() != null) {
            sql.append(ads.getS()).append(",");
        } else {
            sql.append(" 0, ");
        }
        if (ads.getLc() != null) {
            sql.append(ads.getLc()).append(",");
        } else {
            sql.append(" 0, ");
        }
        if (ads.getLb() != null) {
            sql.append(ads.getLb()).append(",");
        } else {
            sql.append(" 0, ");
        }
        if (ads.getDt() != null) {
            sql.append(ads.getDt()).append(",");
        } else {
            sql.append(" 0, ");
        }
        if (ads.getDc() != null) {
            sql.append(ads.getDc()).append(",");
        } else {
            sql.append(" 0, ");
        }
        if (ads.getDb() != null) {
            sql.append(ads.getDb()).append(",");
        } else {
            sql.append(" 0, ");
        }
        if (ads.getDi() != null) {
            sql.append(ads.getDi()).append(",");
        } else {
            sql.append(" 0, ");
        }
        if (ads.getDm() != null) {
            sql.append(ads.getDm()).append(",");
        } else {
            sql.append(" 0, ");
        }
        if (ads.getDms() != null) {
            sql.append(ads.getDms()).append(")");
        } else {
            sql.append(" 0) ");
        }
        return sql.toString();
    }

    /**
     * 更新广告
     *
     * @param ads
     * @return
     */
    public static String editAd(Ads ads) {
        StringBuffer sql = new StringBuffer();
        sql.append("update ads set update_time=now(), ");
        if (StringUtils.isNotBlank(ads.getUpdateBy())) {
            sql.append(" update_by = ").append("'").append(ads.getUpdateBy()).append("',");
        }
        if (ads.getS() != null) {
            sql.append(" s = ").append(ads.getS()).append(",");
        } else {
            sql.append(" s = 0 ,");
        }
        if (ads.getLc() != null) {
            sql.append(" lc = ").append(ads.getLc()).append(",");
        } else {
            sql.append(" lc = 0 ,");
        }
        if (ads.getLb() != null) {
            sql.append(" lb = ").append(ads.getLb()).append(",");
        } else {
            sql.append(" lb = 0 ,");
        }
        if (ads.getDt() != null) {
            sql.append(" dt = ").append(ads.getDt()).append(",");
        } else {
            sql.append(" dt = 0 ,");
        }
        if (ads.getDc() != null) {
            sql.append(" dc = ").append(ads.getDc()).append(",");
        } else {
            sql.append(" dc = 0 ,");
        }
        if (ads.getDb() != null) {
            sql.append(" db = ").append(ads.getDb()).append(",");
        } else {
            sql.append(" db = 0 ,");
        }
        if (ads.getDr() != null) {
            sql.append(" dr = ").append(ads.getDr()).append(",");
        } else {
            sql.append(" dr = 0 ,");
        }
        if (ads.getDi() != null) {
            sql.append(" di = ").append(ads.getDi()).append(",");
        } else {
            sql.append(" di = 0 ,");
        }
        if (ads.getDm() != null) {
            sql.append(" dm = ").append(ads.getDm()).append(",");
        } else {
            sql.append(" dm = 0 ,");
        }
        if (ads.getDms() != null) {
            sql.append(" dms = ").append(ads.getDms());
        } else {
            sql.append(" dms = 0 ");
        }
        sql.append(" where did = ").append(ads.getDid());
        return sql.toString();
    }
}
