package com.oupeng.joke.dao.sqlprovider;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by java_zong on 2017/4/28.
 */
public class SensitiveSqlProvider {
    /**
     * 敏感词总数sql
     *
     * @param keyWord
     * @return
     */
    public static String getListForCount(String keyWord) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(id) from sensitive_words where 1 = 1");
        if (StringUtils.isNotBlank(keyWord)) {
            sql.append(" and word like '%").append(keyWord).append("%' ");
        }
        return sql.toString();
    }

    /**
     * 敏感词列表
     *
     * @param map
     * @return
     */
    public static String getList(Map<String, Object> map) {
        String keyWord = (String) map.get("keyWord");
        Integer offset = (Integer) map.get("offset");
        Integer pageSize = (Integer) map.get("pageSize");
        StringBuffer sql = new StringBuffer();
        sql.append("select id, word from sensitive_words where 1 = 1 ");
        if (StringUtils.isNotBlank(keyWord)) {
            sql.append(" and word like '%").append(keyWord).append("%' ");
        }
        sql.append(" limit ").append(offset).append(" , ").append(pageSize);
        return sql.toString();
    }

}
